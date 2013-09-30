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
package org.flowerplatform.editor.open_resources_view
{
	import mx.collections.ArrayCollection;
	import mx.collections.ICollectionView;
	import mx.collections.IList;
	import mx.controls.treeClasses.ITreeDataDescriptor;
	import mx.skins.spark.EditableComboBoxSkin;
	
	import org.flowerplatform.communication.stateful_service.StatefulClient;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.remote.*;
	import org.flowerplatform.flexutil.tree.IHierarchicalModelAdapter;
	
	
	public class EditableResourceHierachicalModelAdapter implements IHierarchicalModelAdapter
	{

		/**
		 * @author Sebastian
		 */	
		public function getChildren(treeNode:Object):IList
		{					
			if (treeNode is TreeNode)
				return TreeNode(treeNode).children;
			
			if (treeNode is ArrayCollection) {
				return ArrayCollection(treeNode);
			} else if (treeNode is EditableResource) {
				return EditableResource(treeNode).clients;
			}else if(treeNode is EditableResourceClient){				
				return new ArrayCollection(); 
			}else {
				return null;
			}
		}
		
		public function hasChildren(treeNode:Object):Boolean
		{
			if (treeNode is TreeNode) {
				return TreeNode(treeNode).hasChildren;
			}
			return (treeNode is ArrayCollection) || 
				( (treeNode is EditableResource) && (EditableResource(treeNode).clients.length > 0) );
		}
	}
}