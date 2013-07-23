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
package com.crispico.flower.texteditor {
	
	/**
	 * Contains the text.
	 */ 
	public class Document {
		
		/**
		 * The text in this document.
		 */ 
		public var text:String;
		
		/**
		 * The index of the last character that was loaded in the editor from this document.
		 */ 
		public var endLimit:int = 0;
		
		/**
		 * Adds the text to this document and initializes the PartitionUpdater.
		 */ 
		public function Document(text:String=null) {
			this.text = text == null ? new String() : new String(text);
		}
		
		public function get length():int {
			return text.length;
		}
		
		/**
		 * Returns the character at the specified offset.
		 */ 
		public function getChar(offset:int):int {
			return text.charCodeAt(offset);
		}
	}
}