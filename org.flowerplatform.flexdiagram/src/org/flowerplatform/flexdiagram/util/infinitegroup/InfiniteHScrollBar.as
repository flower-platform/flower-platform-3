package org.flowerplatform.flexdiagram.util.infinitegroup {
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import mx.core.mx_internal;
	import mx.events.PropertyChangeEvent;
	
	import spark.components.HScrollBar;
	import spark.events.TrackBaseEvent;
	
	use namespace mx_internal;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class InfiniteHScrollBar extends HScrollBar {
				
		private var clickOffset:Point;      
				
		override mx_internal function viewportHorizontalScrollPositionChangeHandler(event:PropertyChangeEvent):void {
			super.viewportHorizontalScrollPositionChangeHandler(event);
			if (viewport) {
				if (!isNaN(contentMinimum)) {
					minimum = Math.min(contentMinimum, viewport.horizontalScrollPosition);
				}else {
					if (event.oldValue <= event.newValue) {
						minimum = minimum < 0 ? minimum = viewport.horizontalScrollPosition : 0;
					} else {
						minimum = Math.min(minimum, viewport.horizontalScrollPosition - 1);
					}
				}
				if (!isNaN(contentMaximum)) {
					maximum = Math.max(contentMaximum, viewport.horizontalScrollPosition);
				} else {					
					if (event.oldValue >= event.newValue) {
						maximum = maximum > viewport.contentWidth - viewport.width ? viewport.horizontalScrollPosition : viewport.contentWidth - viewport.width;
					} else {
						maximum =  Math.max(maximum, viewport.horizontalScrollPosition + 1);
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