package org.flowerplatform.editor.action {
	import mx.collections.IList;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.remote.ContentTypeDescriptor;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.flexutil.popup.ComposedAction;
	import org.flowerplatform.flexutil.popup.IAction;
	import org.flowerplatform.flexutil.popup.IActionProvider;

	public class EditorTreeActionProvider implements IActionProvider {
		
		public static const OPEN_WITH_ACTION_ID:String = "openWith";
		
		public function EditorTreeActionProvider() {
		}
		
		public function getActions(selection:IList):Vector.<IAction> {
			var result:Vector.<IAction> = new Vector.<IAction>();
			
//			if (selection.length == 1) {
//				// add Open and Open with
//				var treeNode:TreeNode = TreeNode.getTreeNodeFromTreeListDataProviderItem(selection.getItemAt(0));
//				if (treeNode.customData == null || treeNode.customData[EditorPlugin.TREE_NODE_KEY_CONTENT_TYPE] == null) {
//					// node not openable
//					return null;
//				}
//				result.push(new OpenWithDefaultEditorAction());
//				var openWithAction:ComposedAction = new ComposedAction();
//				openWithAction.label = OpenWithDefaultEditorAction.ICON_URL;
//				openWithAction.icon = EditorPlugin.getInstance().getMessage("editor.openWith");
//				openWithAction.id = OPEN_WITH_ACTION_ID;
//				result.push(openWithAction);
//				
//				var ctIndex:int = treeNode.customData[EditorPlugin.TREE_NODE_KEY_CONTENT_TYPE];
//				var ctDescriptor:ContentTypeDescriptor = EditorPlugin.getInstance().contentTypeDescriptors[ctIndex];
//				var defaultEditorDescriptorProcessed:Boolean = false;
//				for each (var editorName:String in ctDescriptor.compatibleEditors) {
//					result.push(new OpenWithSpecifiedEditorAction(EditorPlugin.getInstance().getEditorDescriptorByName(editorName), !defaultEditorDescriptorProcessed));
//					if (!defaultEditorDescriptorProcessed) {
//						// i.e. for index == 0; this ensures that the first item will have an action with "force..."
//						defaultEditorDescriptorProcessed = true;
//					}
//				}
//			} else {
//				for each (var treeNode:TreeNode in selection) {
//					if (treeNode.customData[EditorPlugin.TREE_NODE_KEY_CONTENT_TYPE] == null) {
//						// found at least one node not openable
//						return null;
//					}
//				}
				result.push(new OpenWithDefaultEditorAction());
//			}
			
			return result;
		}
		
	}
}