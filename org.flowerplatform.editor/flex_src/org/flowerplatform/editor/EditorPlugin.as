package org.flowerplatform.editor {
	import mx.collections.IList;
	
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.action.EditorTreeActionProvider;
	import org.flowerplatform.editor.remote.ContentTypeDescriptor;
	import org.flowerplatform.editor.remote.CreateEditorStatefulClientCommand;
	import org.flowerplatform.editor.remote.EditableResource;
	import org.flowerplatform.editor.remote.EditableResourceClient;
	import org.flowerplatform.editor.remote.EditorStatefulClientLocalState;
	import org.flowerplatform.editor.remote.InitializeEditorPluginClientCommand;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Cristi
	 */
	public class EditorPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:EditorPlugin;
		
		public static function getInstance():EditorPlugin {
			return INSTANCE;
		}
		
		public static const TREE_NODE_KEY_CONTENT_TYPE:String = "contentType";
		
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
	}
}