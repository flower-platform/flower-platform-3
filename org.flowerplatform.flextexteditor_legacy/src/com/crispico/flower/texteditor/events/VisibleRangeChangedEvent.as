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