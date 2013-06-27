package org.flowerplatform.web.common {
	
	import flash.events.Event;
	import flash.events.IEventDispatcher;
	import flash.net.registerClassAlias;
	
	import mx.core.FlexGlobals;
	
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.popup.IActionProvider;
	import org.flowerplatform.flexutil.resources.ResourceUpdatedEvent;
	import org.flowerplatform.flexutil.resources.ResourcesUtils;
	import org.flowerplatform.web.common.communication.AuthenticationManager;
	import org.flowerplatform.web.common.communication.AuthenticationViewProvider;
	import org.flowerplatform.web.common.communication.heartbeat.HeartbeatStatefulClient;
	import org.flowerplatform.web.common.entity.dto.NamedDto;
	import org.flowerplatform.web.common.explorer.ExplorerViewProvider;
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
		
		public static const NODE_TYPE_ORGANIZATION:String = "or";
		
		public static const NODE_TYPE_FILE:String = "f";
		
		public var authenticationManager:AuthenticationManager;
		
		/**
		 * @author Mariana
		 */
		public var heartbeatStatefulClient:HeartbeatStatefulClient;
		
		public var explorerTreeActionProviders:Vector.<IActionProvider> = new Vector.<IActionProvider>();
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
			
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new ExplorerViewProvider());
			explorerTreeActionProviders.push(EditorPlugin.getInstance().editorTreeActionProvider);
			explorerTreeActionProviders.push(new TestSampleExplorerTreeActionProvider());
			
//			if (FlexUtilGlobals.getInstance().isMobile)
				FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new AuthenticationViewProvider());
			
			EditorPlugin.getInstance().addPathFragmentToEditableResourcePathCallback = function (treeNode:TreeNode):String {
				if (treeNode.pathFragment == null) {
					return null;
				}
				if (treeNode.pathFragment.type == NODE_TYPE_ORGANIZATION || treeNode.pathFragment.type == NODE_TYPE_FILE) {
					return treeNode.pathFragment.name;
				} else {
					return null;
				}
			}
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
				authenticationManager.authenticationView.dispatchEvent(event);
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
		}
		
	}
}