package org.flowerplatform.communication.tree {
	import mx.collections.IList;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
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