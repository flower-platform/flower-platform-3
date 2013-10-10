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
package org.flowerplatform.editor {
	import com.crispico.flower.util.layout.PopupHostViewWrapper;
	
	import flash.utils.Dictionary;
	
	import mx.collections.IList;
	
	import org.flowerplatform.common.CommonPlugin;
	import org.flowerplatform.common.link.ILinkHandler;
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.action.EditorTreeActionProvider;
	import org.flowerplatform.editor.remote.ContentTypeDescriptor;
	import org.flowerplatform.editor.remote.CreateEditorStatefulClientCommand;
	import org.flowerplatform.editor.remote.EditableResource;
	import org.flowerplatform.editor.remote.EditableResourceClient;
	import org.flowerplatform.editor.remote.EditorStatefulClient;
	import org.flowerplatform.editor.remote.EditorStatefulClientLocalState;
	import org.flowerplatform.editor.remote.InitializeEditorPluginClientCommand;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.layout.event.ViewsRemovedEvent;
	
	/**
	 * @author Cristi
	 */
	public class EditorPlugin extends AbstractFlowerFlexPlugin implements ILinkHandler {
		
		protected static var INSTANCE:EditorPlugin;
		
		public static function getInstance():EditorPlugin {
			return INSTANCE;
		}
		
		public static const TREE_NODE_KEY_CONTENT_TYPE:String = "contentType";
		public static const OPEN_RESOURCES:String = "openResources";
		public static const SELECT_RESOURCE_AT_INDEX:String = "selectResourceAtIndex";
		
		// initialized by server, at client app startup
		public var contentTypeDescriptors:IList;
		
		public var lockLeaseSeconds:int;
		
		// these need to be populated by the web plugin
		public var addPathFragmentToEditableResourcePathCallback:Function;
		
		// normal attributes (i.e. don't come from server)
		public var editorTreeActionProvider:EditorTreeActionProvider = new EditorTreeActionProvider();
		
		public var editorDescriptors:Vector.<BasicEditorDescriptor> = new Vector.<BasicEditorDescriptor>();
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
			
			CommonPlugin.getInstance().linkHandlers[OPEN_RESOURCES] = this;			
		}
		
		override protected function registerClassAliases():void	{
			registerClassAliasFromAnnotation(ContentTypeDescriptor);
			registerClassAliasFromAnnotation(InitializeEditorPluginClientCommand);
			registerClassAliasFromAnnotation(CreateEditorStatefulClientCommand);
			registerClassAliasFromAnnotation(EditableResource);
			registerClassAliasFromAnnotation(EditableResourceClient);
			registerClassAliasFromAnnotation(EditorStatefulClientLocalState);
		}
		
		public function getEditorDescriptorByName(name:String):EditorDescriptor {
			for each (var ed:EditorDescriptor in editorDescriptors) {
				if (ed.getEditorName() == name) {
					return ed;
				}
			}
			return null;
		}
		
		public function getEditableResourcePathFromTreeNode(treeNode:TreeNode):String {
			if (addPathFragmentToEditableResourcePathCallback == null) {
				throw new Error("Cannot calculate editable resource path from tree node because addPathFragmentToEditableResourcePathCallback is not initialized!");
			}
			var result:String = "";
			while (treeNode != null) {
				var pathFragment:String = addPathFragmentToEditableResourcePathCallback(treeNode);
				if (pathFragment != null) {
					result = "/" + pathFragment + result;
				}
				treeNode = treeNode.parent;
			}
			return result;
		}
		
		public function viewsRemoved(event:ViewsRemovedEvent):void {
			var editorStatefulClients:Dictionary = new Dictionary();
			for each (var view:Object in event.removedViews) {
				var editorStatefulClient:EditorStatefulClient = null;
				if (view is EditorFrontend) {
					editorStatefulClient = EditorFrontend(view).editorStatefulClient;
				}
				if (view is PopupHostViewWrapper) {
					if (PopupHostViewWrapper(view).activePopupContent is EditorFrontend) {
						editorStatefulClient = EditorFrontend(PopupHostViewWrapper(view).activePopupContent).editorStatefulClient;
					}
				}
				if (editorStatefulClient != null) {
					if (editorStatefulClients[editorStatefulClient.editableResourcePath] == null) {
						CommunicationPlugin.getInstance().statefulClientRegistry.unregister(editorStatefulClient, null);
						editorStatefulClients[editorStatefulClient.editableResourcePath] = editorStatefulClient;
					}
				}
			}
		}
				
		public function getFirstEditorDescriptorForNode(contentTypeIndex:int, throwErrorIfNotFound:Boolean = true):BasicEditorDescriptor {			
			var ctDescriptor:ContentTypeDescriptor = EditorPlugin.getInstance().contentTypeDescriptors[contentTypeIndex];
			
			var editorName:String = String(ctDescriptor.compatibleEditors[0]);
			var descriptor:BasicEditorDescriptor = EditorPlugin.getInstance().getEditorDescriptorByName(editorName);
			if (descriptor == null && throwErrorIfNotFound) {
				throw new Error("There is no compatible editor descriptor found for editor: " + editorName + "!");
			}
			return descriptor;
		}
		
		public function handleLink(command:String, parameters:String):void {
			if (command == OPEN_RESOURCES) {
				// parameters format = file1,file2,file3|selectResourceAtIndex=1
				var files:String = parameters;
				var index:String;
				
				if (parameters.lastIndexOf("|") != -1) { // index exists
					files = parameters.split("|")[0];
					index = parameters.split("|")[1];	
					if (index.match(SELECT_RESOURCE_AT_INDEX + "=[0-9]")) {
						index = index.substring(index.lastIndexOf("=") + 1);
					}					
				}
				CommunicationPlugin.getInstance().bridge.sendObject(
					new InvokeServiceMethodServerCommand(
						"editorOperationsService", "navigateFriendlyEditableResourcePathList", 
						[files, index == null ? -1 : int(index)]));
			}		
		}
	}
}