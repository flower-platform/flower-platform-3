package org.flowerplatform.communication.tree {
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.tree.HierarchicalModelWrapper;
	import org.flowerplatform.flexutil.tree.TreeListItemRenderer;
	
	public class GenericTreeItemRenderer extends TreeListItemRenderer {
		
		public function GenericTreeItemRenderer() {
			super();
			labelFunction = getLabelFunction;
			iconFunction = getIconFunction;
		}
		
		protected function getLabelFunction(data:HierarchicalModelWrapper):String {
			return TreeNode(data.treeNode).label;
		}
		
		protected function getIconFunction(data:HierarchicalModelWrapper):String {
			return TreeNode(data.treeNode).icon;
		}
	}
}