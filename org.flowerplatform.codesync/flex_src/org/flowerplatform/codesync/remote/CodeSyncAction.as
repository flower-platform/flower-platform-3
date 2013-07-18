package org.flowerplatform.codesync.remote {
	import mx.collections.IList;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.popup.ActionBase;
	
	[RemoteClass(alias="org.flowerplatform.codesync.remote.CodeSyncAction")]
	public class CodeSyncAction extends ActionBase {
		
		public var pathWithRoot:IList;
		
		public var technology:String;
		
		public function CodeSyncAction(label:String, technology:String) {
			super();
			
			// TODO CS/FP2 msg
			this.label = label;
			this.technology = technology;
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