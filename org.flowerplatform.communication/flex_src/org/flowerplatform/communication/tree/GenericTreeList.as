/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.communication.tree {
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.collections.IList;
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
		
		/**
		 * This method is inspired from <code>get selectedItems()</code>. We convert
		 * the selection like this (and not by converting selectedItems), because
		 * <code>get selectedItems()</code> does an iteration as well; so that would
		 * mean 2 iterations. This way, we iterate only once.
		 */
		public function getSelection():IList {
			var result:Array = new Array();
			
			if (selectedIndices) {
				var count:int = selectedIndices.length;
				
				for (var i:int = 0; i < count; i++)
					result.push(HierarchicalModelWrapper(dataProvider.getItemAt(selectedIndices[i])).treeNode);  
			}
			
			return new ArrayList(result);
		}
		
		override public function expandCollapseNode(modelWrapper:HierarchicalModelWrapper):void	{
			if (statefulClient.requestDataOnServer) {
				if (!modelWrapper.expanded) {
					// i.e. state = collapsed				
					statefulClient.openNode(modelWrapper.treeNode);			
				} else if (dispatchEnabled) {
					// i.e. state = expanded; call server only if dispatch type tree
					statefulClient.closeNode(modelWrapper.treeNode);
				}
			}
			super.expandCollapseNode(modelWrapper);
		}
		
	}
}