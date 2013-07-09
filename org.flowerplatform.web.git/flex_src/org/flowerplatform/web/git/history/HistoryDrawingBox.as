package org.flowerplatform.web.git.history {
	
	import mx.collections.ArrayCollection;
	import mx.containers.HBox;
	
	import org.flowerplatform.web.git.history.remote.dto.HistoryDrawingDto;
	import org.flowerplatform.web.git.history.remote.dto.HistoryEntryDto;

	/**
	 *	@author Cristina Constantinescu
	 */ 
	public class HistoryDrawingBox extends HBox {
		
		private function drawLine(x1:int, y1:int, x2:int, y2:int, width:int, color:uint):void {
			graphics.lineStyle(width, color);
			graphics.moveTo(x1, y1);
			graphics.lineTo(x2, y2);			
		}
		
		private function drawDot(x:int, y:int, w:int, h:int, foreground:uint, backgroundColor:uint):void {
			graphics.beginFill(backgroundColor);
			graphics.lineStyle(2, foreground);
			graphics.drawEllipse(x + 2, y + 1, w - 2, h - 2);
		}
			
		private function parseColor(color:String):uint {
			return uint("0x" + color.substring(1));
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
						
			graphics.clear();
			for each (var drawing:HistoryDrawingDto in ArrayCollection(HistoryEntryDto(data).drawings)) {
				if (drawing.type == HistoryDrawingDto.DRAW_LINE) {
					drawLine(
						int(drawing.params.getItemAt(0)), int(drawing.params.getItemAt(1)),
						int(drawing.params.getItemAt(2)), int(drawing.params.getItemAt(3)),
						int(drawing.params.getItemAt(4)), parseColor(String(drawing.params.getItemAt(5))));				
				} else if (drawing.type == HistoryDrawingDto.DRAW_DOT) {
					drawDot(
						int(drawing.params.getItemAt(0)), int(drawing.params.getItemAt(1)), 
						int(drawing.params.getItemAt(2)), int(drawing.params.getItemAt(3)),
						parseColor(String(drawing.params.getItemAt(4))), parseColor(String(drawing.params.getItemAt(5))));
				}
			}						
		}
	}
	
}