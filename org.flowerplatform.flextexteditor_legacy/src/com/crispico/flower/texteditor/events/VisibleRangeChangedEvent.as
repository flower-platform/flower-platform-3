package com.crispico.flower.texteditor.events {
	
	import com.crispico.flower.texteditor.model.Range;
	
	import flash.events.Event;

	/**
	 * An event dispatched when the editor is scrolled.
	 * @flowerModelElementId _x-wLsCyKEeGsGrJcrtxw9Q
	 */ 
	public class VisibleRangeChangedEvent extends Event {
		
		/**
		 * The new visible range.
		 */ 
		public var visibleRange:Range;

		public static const VISIBLE_RANGE_CHANGED:String = "SYNTAX_TEXT_AREA_VISIBLE_RANGE_CHANGED";
		
		public function VisibleRangeChangedEvent(visibleRange:Range):void {
			super(VISIBLE_RANGE_CHANGED);
			
			this.visibleRange = visibleRange;
		}
	}
}