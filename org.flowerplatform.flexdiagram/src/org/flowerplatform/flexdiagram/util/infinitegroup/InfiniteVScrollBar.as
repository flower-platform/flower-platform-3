package org.flowerplatform.flexdiagram.util.infinitegroup {
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import mx.core.InteractionMode;
	import mx.core.mx_internal;
	import mx.events.PropertyChangeEvent;
	import mx.events.ResizeEvent;
	
	import spark.components.VScrollBar;
	import spark.events.TrackBaseEvent;
	
	use namespace mx_internal;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class InfiniteVScrollBar extends VScrollBar {
		
		private var clickOffset:Point;   
		
		override mx_internal function viewportVerticalScrollPositionChangeHandler(event:PropertyChangeEvent):void {
			super.viewportVerticalScrollPositionChangeHandler(event);
			if (viewport) {					
				if (!isNaN(contentMinimum)) {
					minimum = Math.min(contentMinimum, viewport.verticalScrollPosition);
				} else {
					if (event.oldValue <= event.newValue) {
						minimum = minimum < 0 ? minimum = viewport.verticalScrollPosition : 0;
					} else {
						minimum = Math.min(minimum, viewport.verticalScrollPosition - 1);
					}
				}
				if (!isNaN(contentMaximum)) {
					maximum = Math.max(contentMaximum, viewport.verticalScrollPosition);
				} else {					
					if (event.oldValue >= event.newValue) {						
						maximum = maximum > viewport.contentHeight - viewport.height ? viewport.verticalScrollPosition : viewport.contentHeight - viewport.height;
					} else {
						maximum =  Math.max(maximum, viewport.verticalScrollPosition + 1);
					}
				}	
			}
		}
		
		override protected function thumb_mouseDownHandler(event:MouseEvent) : void {
			super.thumb_mouseDownHandler(event);
			clickOffset = thumb.globalToLocal(new Point(event.stageX, event.stageY));        
		}
		
		override protected function system_mouseMoveHandler(event:MouseEvent):void {     
			if (!track)
				return;
			
			var p:Point = track.globalToLocal(new Point(event.stageX, event.stageY));
			var newValue:Number = pointToValue(p.x - clickOffset.x, p.y - clickOffset.y);
			newValue = nearestValidValue(newValue, snapInterval);
			
			if (!isNaN(contentMinimum)) {
				if (newValue < contentMinimum) {
					newValue = contentMinimum;
				}
			}
			if (!isNaN(contentMaximum)) {
				if (newValue > contentMaximum) {
					newValue = contentMaximum;
				}
			}
			if (newValue != pendingValue) {
				dispatchEvent(new TrackBaseEvent(TrackBaseEvent.THUMB_DRAG));
				if (getStyle("liveDragging") === true) {
					setValue(newValue);
					dispatchEvent(new Event(Event.CHANGE));
				} else {
					pendingValue = newValue;
				}
			}			
			event.updateAfterEvent();
		}
		
		override protected function nearestValidValue(value:Number, interval:Number):Number	{					
			var scale:Number = 1;
			
			if (interval != Math.round(interval)) {
				// calculate scale and compute new scaled values.
				const parts:Array = (new String(1 + interval)).split("."); 
				scale = Math.pow(10, parts[1].length);				
				interval = Math.round(interval * scale);
				value = Math.round((value * scale));
			}
			
			var lower:Number = Math.max(minimum - 1, Math.floor(value / interval) * interval);
			var upper:Number = Math.min(maximum + 1, Math.floor((value + interval) / interval) * interval);
			var validValue:Number = ((value - lower) >= ((upper - lower) / 2)) ? upper : lower;
			
			return validValue / scale;
		}		
	}
}