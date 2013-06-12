package org.flowerplatform.flexutil.tree{
	import mx.collections.IList;

	public interface IHierarchicalModelAdapter {
		function getChildren(treeNode:Object):IList;
		function hasChildren(treeNode:Object):Boolean;
	}
}