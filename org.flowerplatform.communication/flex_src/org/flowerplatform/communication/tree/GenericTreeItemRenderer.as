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
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
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
		
		protected function getIconFunction(data:HierarchicalModelWrapper):Object {
			return FlexUtilGlobals.getInstance().adjustImageBeforeDisplaying(TreeNode(data.treeNode).icon);
		}
	}
}