package org.flowerplatform.web.git.action {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.git.GitNodeType;
	import org.flowerplatform.web.git.GitPlugin;
	import org.flowerplatform.web.git.ui.CheckoutWindow;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class CheckoutAction extends ActionBase	{
		
		public function CheckoutAction() {
			label = GitPlugin.getInstance().getMessage("git.action.checkout.label");	
			icon = GitPlugin.getInstance().getResourceUrl("images/full/obj16/checkout.gif");
			orderIndex = int(GitPlugin.getInstance().getMessage("git.action.checkout.sortIndex"));
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {			
				var node:TreeNode = TreeNode(selection.getItemAt(0));
				return node.pathFragment.type == GitNodeType.NODE_TYPE_REMOTE_BRANCH ||
					node.pathFragment.type == GitNodeType.NODE_TYPE_TAG;
			}
			return false;
		}
		
		private function getName(fullName:String):String {
			return fullName.substring(fullName.lastIndexOf("/") + 1);
		}
		
		override public function run():void {
			var node:TreeNode = TreeNode(selection.getItemAt(0));
			if (node.pathFragment.type == GitNodeType.NODE_TYPE_TAG) {
				var popup:CheckoutWindow = new CheckoutWindow();
				popup.node = node;
				popup.showPopup();
			} else {
				var localBranches:ArrayCollection = TreeNode(node.parent.parent.children.getItemAt(0)).children;
				if (localBranches != null && localBranches.length > 0) {
					for each (var branch:TreeNode in localBranches) {
						if (getName(branch.pathFragment.name) == getName(node.pathFragment.name)) {
							FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
								.setText("Branch already checked out!")
								.showMessageBox();
							return;
						}
					}
				}
				GitPlugin.getInstance().service.checkout(node, getName(node.pathFragment.name), null, null);
			}
		}
	}
}