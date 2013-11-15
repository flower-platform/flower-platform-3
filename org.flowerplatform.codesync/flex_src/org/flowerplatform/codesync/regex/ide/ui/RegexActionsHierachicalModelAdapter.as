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
package org.flowerplatform.codesync.regex.ide.ui {
	import mx.collections.ArrayCollection;
	import mx.collections.ICollectionView;
	import mx.collections.IList;
	import mx.controls.treeClasses.ITreeDataDescriptor;
	import mx.skins.spark.EditableComboBoxSkin;
	
	import org.flowerplatform.codesync.regex.ide.remote.RegexDto;
	import org.flowerplatform.communication.stateful_service.StatefulClient;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.remote.*;
	import org.flowerplatform.flexutil.tree.IHierarchicalModelAdapter;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class RegexActionsHierachicalModelAdapter implements IHierarchicalModelAdapter {

		public function getChildren(treeNode:Object):IList {		
			if (treeNode is RegexDto) {
				return RegexDto(treeNode).matches;
			}
			return null;
		}
		
		public function hasChildren(treeNode:Object):Boolean {			
			if (treeNode is RegexDto) {
				return RegexDto(treeNode).matches.length != 0;
			}
			return false;
		}
		
	}
}