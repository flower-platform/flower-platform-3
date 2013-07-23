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
	
	import flash.events.Event;

	/**
	 * An event dispatched when text is edited.
	 * @flowerModelElementId _x-yn8CyKEeGsGrJcrtxw9Q
	 */ 
	public class TextChangedEvent extends Event {
		
		/**
		 * The position in the text where the editing was done.
		 */ 
		public var offset:int;
		
		/**
		 * The inserted text.
		 */ 
		public var newText:String;
	
		/**
		 * The deleted text.
		 */ 
		public var oldTextLength:int;
		
		public static const TEXT_CHANGED:String = "SYNTAX_TEXT_AREA_TEXT_CHANGED";
		
		public function TextChangedEvent(offset:int, oldTextLength:int, newText:String):void {
			super(TEXT_CHANGED);
			
			this.offset = offset;
			this.newText = newText;
			this.oldTextLength = oldTextLength;
		}
	}
}