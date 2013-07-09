package com.crispico.flower.mp.codesync.base {
	import com.crispico.flower.mp.codesync.base.communication.DiffTreeNode;
	
	import temp.tree.TreeNodeItemRenderer;

	public class DiffTreeNodeItemRenderer extends TreeNodeItemRenderer {
		
		protected override function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);

			if (!(data is DiffTreeNode))
				return;

			const alpha:Number = 1;

			var diffTreeNode:DiffTreeNode = DiffTreeNode(data);
			
			graphics.clear();
			graphics.lineStyle(NaN);

			graphics.beginFill(diffTreeNode.topLeftColor, alpha);
			graphics.drawRect(label.x, 0, label.measuredWidth / 2, unscaledHeight / 2);

			graphics.beginFill(diffTreeNode.bottomLeftColor, alpha);
			graphics.drawRect(label.x, unscaledHeight / 2, label.measuredWidth / 2, unscaledHeight / 2);
			
			graphics.beginFill(diffTreeNode.topRightColor, alpha);
			graphics.drawRect(label.x + label.measuredWidth / 2, 0, label.measuredWidth / 2, unscaledHeight / 2);
			
			graphics.beginFill(diffTreeNode.bottomRightColor, alpha);
			graphics.drawRect(label.x + label.measuredWidth / 2, unscaledHeight / 2, label.measuredWidth / 2, unscaledHeight / 2);
			
			graphics.lineStyle(1, 0);
			graphics.beginFill(0xFFFFFF, 0);
			graphics.drawRect(label.x, 0, label.measuredWidth, unscaledHeight);
			
			if (diffTreeNode.crossColor < 0xFFFFFF) {
				graphics.lineStyle(1, diffTreeNode.crossColor);
				
				graphics.moveTo(label.x, 0);
				graphics.lineTo(label.x + label.measuredWidth, unscaledHeight);
	
				graphics.moveTo(label.x, unscaledHeight);
				graphics.lineTo(label.x + label.measuredWidth, 0);
			}
		}
	}
}