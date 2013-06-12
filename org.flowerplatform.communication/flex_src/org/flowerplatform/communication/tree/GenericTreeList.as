package org.flowerplatform.communication.tree {
	import mx.collections.ArrayCollection;
	import mx.core.ClassFactory;
	
	import org.flowerplatform.communication.tree.remote.GenericTreeStatefulClient;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.tree.HierarchicalModelWrapper;
	import org.flowerplatform.flexutil.tree.TreeList;
	
	public class GenericTreeList extends TreeList {
		
		public var statefulClient:GenericTreeStatefulClient;
		
		public var dispatchEnabled:Boolean;
		
		public function GenericTreeList() {
			super();
			itemRenderer = new ClassFactory(GenericTreeItemRenderer);
			hierarchicalModelAdapter = new TreeNodeHierarchicalModelAdapter();
			var root:TreeNode = new TreeNode();
			root.children = new ArrayCollection();
			rootNode = root;
		}
		
		override public function expandCollapseNode(modelWrapper:HierarchicalModelWrapper):void	{
			if (!modelWrapper.expanded) {
				// i.e. state = collapsed
				statefulClient.openNode(modelWrapper.treeNode);
			} else if (dispatchEnabled) {
				// i.e. state = expanded; call server only if dispatch type tree
				statefulClient.closeNode(modelWrapper.treeNode);
			}
			super.expandCollapseNode(modelWrapper);
		}
		
	}
}