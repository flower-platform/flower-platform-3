package org.flowerplatform.flexutil.samples.tree {
	import mx.collections.IList;
	
	import org.flowerplatform.flexutil.tree.IHierarchicalModelAdapter;
	
	public class TreeNodeHierarchicalModelAdapter implements IHierarchicalModelAdapter {
		public function getChildren(treeNode:Object):IList {
			return TreeNode(treeNode).children;
		}
		
		public function hasChildren(treeNode:Object):Boolean {
			return TreeNode(treeNode).hasChildren;
		}
	}
}