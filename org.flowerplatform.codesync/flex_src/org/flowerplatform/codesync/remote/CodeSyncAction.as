package org.flowerplatform.codesync.remote {
	import mx.collections.IList;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.popup.ActionBase;
	
	[RemoteClass(alias="com.crispico.flower.mp.codesync.code.java.JavaDragOnDiagramHandler")]
	public class CodeSyncAction extends ActionBase {
		
		public var pathWithRoot:IList;
		
		public function CodeSyncAction() {
			super();
			// TODO CS/FP2 msg
			label = "Code Sync";
		}
		
		override public function get visible():Boolean {
			if (selection == null || selection.length != 1) {
				return false;
			}
			return true;
		}
		
		
		override public function run():void {
			pathWithRoot = TreeNode(selection.getItemAt(0)).getPathForNode(true);
			CommunicationPlugin.getInstance().bridge.sendObject(this);
		}
		
	}
}