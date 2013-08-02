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
package temp.tree {
	import mx.collections.ArrayCollection;
	import mx.collections.ICollectionView;
	import mx.controls.treeClasses.ITreeDataDescriptor;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	
	/**
	 * <code>ITreeDataDescriptor</code> that knows to describe a 
	 * <code>TreeNode</code>.
	 * 
	 * @author Cristi
	 * 
	 */
	public class TreeNodeDataDescriptor implements ITreeDataDescriptor {
		
		public function getChildren(node:Object, model:Object=null):ICollectionView {
			return TreeNode(node).children;
		}
		
		public function hasChildren(node:Object, model:Object=null):Boolean {
			return TreeNode(node).hasChildren;
		}
		
		public function isBranch(node:Object, model:Object=null):Boolean {
			return hasChildren(node);
		}
		
		public function getData(node:Object, model:Object=null):Object {
			return node;
		}
		
		public function addChildAt(parent:Object, newChild:Object, index:int, model:Object=null):Boolean {
			throw "not implemented";
		}
		
		public function removeChildAt(parent:Object, child:Object, index:int, model:Object=null):Boolean {
			throw "not implemented";
		}
	}
}