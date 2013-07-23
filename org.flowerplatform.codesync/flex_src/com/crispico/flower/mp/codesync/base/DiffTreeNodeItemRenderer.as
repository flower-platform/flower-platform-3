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
package com.crispico.flower.mp.codesync.base {
	import com.crispico.flower.mp.codesync.base.communication.DiffTreeNode;
	
	import org.flowerplatform.communication.tree.GenericTreeItemRenderer;
	import org.flowerplatform.flexutil.tree.HierarchicalModelWrapper;
	
	public class DiffTreeNodeItemRenderer extends GenericTreeItemRenderer {
		
		protected override function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);

			if (!(HierarchicalModelWrapper(data).treeNode is DiffTreeNode))
				return;

			const alpha:Number = 1;

			var diffTreeNode:DiffTreeNode = DiffTreeNode(HierarchicalModelWrapper(data).treeNode);
			
			graphics.clear();
			graphics.lineStyle(NaN);
			
			var labelX:int = labelDisplay.x;
			var labelWidth:int = labelDisplay.textWidth / 2 + 2;

			graphics.beginFill(diffTreeNode.topLeftColor, alpha);
			graphics.drawRect(labelX, 0, labelWidth, unscaledHeight / 2);

			graphics.beginFill(diffTreeNode.bottomLeftColor, alpha);
			graphics.drawRect(labelX, unscaledHeight / 2, labelWidth, unscaledHeight / 2);
			
			graphics.beginFill(diffTreeNode.topRightColor, alpha);
			graphics.drawRect(labelX + labelWidth, 0, labelWidth, unscaledHeight / 2);
			
			graphics.beginFill(diffTreeNode.bottomRightColor, alpha);
			graphics.drawRect(labelX + labelWidth, unscaledHeight / 2, labelWidth, unscaledHeight / 2);
			
			graphics.lineStyle(1, 0);
			graphics.beginFill(0xFFFFFF, 0);
			graphics.drawRect(labelX, 0, labelWidth * 2, unscaledHeight);
			
			if (diffTreeNode.crossColor < 0xFFFFFF) {
				graphics.lineStyle(1, diffTreeNode.crossColor);
				
				graphics.moveTo(labelX, 0);
				graphics.lineTo(labelX + labelWidth * 2, unscaledHeight);
	
				graphics.moveTo(labelX, unscaledHeight);
				graphics.lineTo(labelX + labelWidth * 2, 0);
			}
		}
	}
}