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
	
	import mx.collections.ArrayCollection;

	/**
	 * Dispatched from the SyntaxTextArea. Notifies the parent that there were changes
	 * in the text. The changes are in the buffer and they should be applied to other
	 * editors.
	 * 
	 * NOTE: bubbles is true by default, because this event has to get to the parent
	 */ 
	public class BufferedTextChangesEvent extends Event	{
		
		/**
		 * The buffer that contains the events.
		 */ 
		public var buffer:ArrayCollection;
		
		public static const BUFFERED_TEXT_CHANGES:String = "SYNTAX_TEXT_AREA_BUFFERED_TEXT_CHANGES";
		
		public function BufferedTextChangesEvent(buffer:ArrayCollection) {
			// bubbles = true to get to the parent
			super(BUFFERED_TEXT_CHANGES, true);
			
			this.buffer = buffer;
		}
		
	}
}