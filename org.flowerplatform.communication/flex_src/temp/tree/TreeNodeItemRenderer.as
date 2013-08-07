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
	import com.crispico.flower.flexdiagram.util.common.BitmapContainer;
	import com.crispico.flower.util.tree.CustomIconTreeItemRenderer;
	
	import mx.core.IFlexDisplayObject;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;

	/**
	 * Renderer implementation that uses <code>TreeNode.iconUrls</code> to
	 * retrieve an image from the factory.
	 * 
	 * @author Cristi
	 * 
	 */ 
	public class TreeNodeItemRenderer extends CustomIconTreeItemRenderer {
		
		/**
		 * @see Class doc.
		 */ 
		override protected function createIcon():IFlexDisplayObject {
//			if (data == null)
				return null;
//			
//			var treeNode:TreeNode = TreeNode(data);
//			if (icon == null) // First time when populating item renderer
//				return new BitmapContainer(treeNode.iconUrls);
//
//			// Reuse the icon. Although it will be removed, the CustomIconTreeItemRenderer will add it back. 
//			BitmapContainer(icon).retrieveImage(treeNode.iconUrls)
//			return icon;
		}
	}
}