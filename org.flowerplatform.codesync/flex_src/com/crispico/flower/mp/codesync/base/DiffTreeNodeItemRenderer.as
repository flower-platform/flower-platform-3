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

			graphics.beginFill(diffTreeNode.topLeftColor, alpha);
			graphics.drawRect(labelDisplay.x, 0, labelDisplay.textWidth / 2, unscaledHeight / 2);

			graphics.beginFill(diffTreeNode.bottomLeftColor, alpha);
			graphics.drawRect(labelDisplay.x, unscaledHeight / 2, labelDisplay.textWidth / 2, unscaledHeight / 2);
			
			graphics.beginFill(diffTreeNode.topRightColor, alpha);
			graphics.drawRect(labelDisplay.x + labelDisplay.textWidth / 2, 0, labelDisplay.textWidth / 2, unscaledHeight / 2);
			
			graphics.beginFill(diffTreeNode.bottomRightColor, alpha);
			graphics.drawRect(labelDisplay.x + labelDisplay.textWidth / 2, unscaledHeight / 2, labelDisplay.textWidth / 2, unscaledHeight / 2);
			
			graphics.lineStyle(1, 0);
			graphics.beginFill(0xFFFFFF, 0);
			graphics.drawRect(labelDisplay.x, 0, labelDisplay.textWidth, unscaledHeight);
			
			if (diffTreeNode.crossColor < 0xFFFFFF) {
				graphics.lineStyle(1, diffTreeNode.crossColor);
				
				graphics.moveTo(labelDisplay.x, 0);
				graphics.lineTo(labelDisplay.x + labelDisplay.textWidth, unscaledHeight);
	
				graphics.moveTo(labelDisplay.x, unscaledHeight);
				graphics.lineTo(labelDisplay.x + labelDisplay.textWidth, 0);
			}
		}
	}
}