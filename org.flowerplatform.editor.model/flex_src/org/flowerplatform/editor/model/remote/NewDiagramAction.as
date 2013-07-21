package org.flowerplatform.editor.model.remote {
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.flexutil.popup.ActionBase;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class NewDiagramAction extends ActionBase {
		
		public var parentPath:String;
		public var name:String;
		
		override public function get visible():Boolean {
			if (selection == null || selection.length != 1) {
				return false;
			}
			return true;
		}
		
		override public function run():void {
			parentPath = EditorPlugin.getInstance().getEditableResourcePathFromTreeNode(TreeNode(selection.getItemAt(0)));
			CommunicationPlugin.getInstance().bridge.sendObject(this);
		}
	}
}