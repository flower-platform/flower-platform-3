/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.web.common {
	
	import flash.events.IEventDispatcher;
	import flash.net.registerClassAlias;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.core.FlexGlobals;
	
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.popup.ClassFactoryActionProvider;
	import org.flowerplatform.flexutil.popup.IActionProvider;
	import org.flowerplatform.flexutil.resources.ResourceUpdatedEvent;
	import org.flowerplatform.flexutil.resources.ResourcesUtils;
	import org.flowerplatform.web.common.communication.AuthenticationManager;
	import org.flowerplatform.web.common.communication.AuthenticationViewProvider;
	import org.flowerplatform.web.common.communication.heartbeat.HeartbeatStatefulClient;
	import org.flowerplatform.web.common.entity.dto.NamedDto;
	import org.flowerplatform.web.common.explorer.ExplorerViewProvider;
	import org.flowerplatform.web.common.projects.ProjectPropertiesAction;
	import org.flowerplatform.web.common.projects.remote.CreateDirectoryAction;
	import org.flowerplatform.web.common.projects.remote.CreateOrImportProjectAction;
	import org.flowerplatform.web.common.projects.remote.MarkAsWorkingDirectoryAction;
	import org.flowerplatform.web.common.remote.InitializeNodeTypeCategoryToNodeTypesMapClientCommand;
	import org.flowerplatform.web.common.security.dto.GroupAdminUIDto;
	import org.flowerplatform.web.common.security.dto.InitializeCurrentUserLoggedInClientCommand;
	import org.flowerplatform.web.common.security.dto.OrganizationAdminUIDto;
	import org.flowerplatform.web.common.security.dto.OrganizationUserAdminUIDto;
	import org.flowerplatform.web.common.security.dto.PermissionAdminUIDto;
	import org.flowerplatform.web.common.security.dto.PermissionsByResourceFilter;
	import org.flowerplatform.web.common.security.dto.UserAdminUIDto;
	import org.flowerplatform.web.common.security.dto.User_CurrentUserLoggedInDto;
	
	/**
	 * @author Cristi
	 */
	public class WebCommonPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:WebCommonPlugin;
		
		public static function getInstance():WebCommonPlugin {
			return INSTANCE;
		}
		
		public static const TREE_NODE_FILE_SYSTEM_IS_DIRECTORY:String = "isDirectory"; 
		
		public static const NODE_TYPE_ORGANIZATION:String = "organization";
		
		public static const NODE_TYPE_PROJECT:String = "project";
		
		public static const NODE_TYPE_PROJ_FILE:String = "projFile";
		
		public static const NODE_TYPE_CATEGORY_PATH_FRAGMENT_NAME_POINTS_TO_FILE:String = "pathFragmentNamePointsToFile";
		
		public static const NODE_TYPE_CATEGORY_DECORATABLE_FILE:String = "decoratableFile";
		
		public var authenticationManager:AuthenticationManager;
		
		/**
		 * @author Mariana
		 */
		public var heartbeatStatefulClient:HeartbeatStatefulClient;
		
		public var explorerTreeActionProviders:Vector.<IActionProvider> = new Vector.<IActionProvider>();
		
		public var explorerTreeClassFactoryActionProvider:ClassFactoryActionProvider = new ClassFactoryActionProvider();
		
		public var nodeTypeCategoryToNodeTypesMap:Object;
		
		public var projectPropertyProviders:ArrayList = new ArrayList();
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
			
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new ExplorerViewProvider());
			explorerTreeActionProviders.push(explorerTreeClassFactoryActionProvider);
			explorerTreeActionProviders.push(EditorPlugin.getInstance().editorTreeActionProvider);
			
//			if (FlexUtilGlobals.getInstance().isMobile)
				FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new AuthenticationViewProvider());
			
			EditorPlugin.getInstance().addPathFragmentToEditableResourcePathCallback = function (treeNode:TreeNode):String {
				if (treeNode.pathFragment == null) {
					return null;
				}
				
				if (nodeTypeBelongsToNodeTypeCategory(treeNode.pathFragment.type, NODE_TYPE_CATEGORY_PATH_FRAGMENT_NAME_POINTS_TO_FILE)) {
					return treeNode.pathFragment.name;
				} else {
					return null;
				}
			}
				
			// actions
			explorerTreeClassFactoryActionProvider.actionClasses.push(MarkAsWorkingDirectoryAction);
			explorerTreeClassFactoryActionProvider.actionClasses.push(CreateOrImportProjectAction);
			explorerTreeClassFactoryActionProvider.actionClasses.push(ProjectPropertiesAction);
			explorerTreeClassFactoryActionProvider.actionClasses.push(CreateDirectoryAction);			
		}
		
		/**
		 * @author Cristi
		 * @author Mariana
		 */
		override public function start():void {
			super.start();
			authenticationManager = new AuthenticationManager();
			var application:IEventDispatcher = IEventDispatcher(FlexGlobals.topLevelApplication);
			application.addEventListener(ResourceUpdatedEvent.RESOURCE_UPDATED, function(event:ResourceUpdatedEvent):void {
				if (authenticationManager.authenticationView != null) {
					// this was null e.g. while testing and calling connect(user, pass) directly, so no popup
					authenticationManager.authenticationView.dispatchEvent(event);
				}
			});
			heartbeatStatefulClient = new HeartbeatStatefulClient();
		}
		
		/**
		 * @author Mariana
		 */
		override protected function registerMessageBundle():void {
			ResourcesUtils.registerMessageBundle("en_US", getResourceUrl(""), getResourceUrl(MESSAGES_FILE), FlexGlobals.topLevelApplication);
		}
		
		override protected function registerClassAliases():void {
			super.registerClassAliases();
			registerClassAlias("org.flowerplatform.web.entity.dto.NamedDto", NamedDto);
			registerClassAlias("org.flowerplatform.web.security.dto.UserAdminUIDto", UserAdminUIDto);
			registerClassAlias("org.flowerplatform.web.security.dto.User_CurrentUserLoggedInDto", User_CurrentUserLoggedInDto);
			registerClassAlias("org.flowerplatform.web.security.dto.GroupAdminUIDto", GroupAdminUIDto);
			registerClassAlias("org.flowerplatform.web.security.dto.OrganizationAdminUIDto", OrganizationAdminUIDto);
			registerClassAlias("org.flowerplatform.web.security.dto.OrganizationUserAdminUIDto", OrganizationUserAdminUIDto);
			registerClassAlias("org.flowerplatform.web.security.dto.PermissionAdminUIDto", PermissionAdminUIDto);
			registerClassAlias("org.flowerplatform.web.security.dto.PermissionsByResourceFilter", PermissionsByResourceFilter);
			registerClassAliasFromAnnotation(InitializeCurrentUserLoggedInClientCommand);
			registerClassAliasFromAnnotation(InitializeNodeTypeCategoryToNodeTypesMapClientCommand);
		}
		
		public function nodeTypeBelongsToNodeTypeCategory(nodeType:String, nodeTypeCategory:String):Boolean {
			var categories_pathFragmentNamePointsToFile:ArrayCollection = ArrayCollection(nodeTypeCategoryToNodeTypesMap[nodeTypeCategory]);
			if (categories_pathFragmentNamePointsToFile == null) {
				return false;
			}
			
			return categories_pathFragmentNamePointsToFile.getItemIndex(nodeType) >= 0;
		}
	}
}