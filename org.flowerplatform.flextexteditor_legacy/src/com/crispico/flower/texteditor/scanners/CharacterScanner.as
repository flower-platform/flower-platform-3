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
package com.crispico.flower.texteditor.scanners {
	
	import com.crispico.flower.texteditor.Document;
	import com.crispico.flower.texteditor.utils.Util;

	/**
	 * Basic implementation of a character scanner. Provides methods to read
	 * and unread one character at a time from the set range in a document, and also
	 * methods to find the start and end of a line at a specified position.
	 */ 
	public class CharacterScanner implements ICharacterScanner {
		
		/**
		 * The document to be scanned.
		 */ 
		protected var document:Document;
		
		/**
		 * The start index of the region to be scanned.
		 */ 
		protected var start:int;
		
		/**
		 * The end index of the region to be scanned.
		 */ 
		protected var end:int;
		
		/** 
		 * The current scanned index.
		 */ 
		protected var offset:int;

		/**
		 * Sets the document to scan and the range that this scanner can access.
		 * Sets the scanner's offset to the start of the range.
		 */ 
		public function setRange(document:Document, startOffset:int, length:int):void {
			this.document = document;
			start = startOffset;
			end = startOffset + length;
			offset = startOffset;
		}
		
		/**
		 * Returns the previous character at this scanner's offset.
		 * If there are no more characters, returns the EOF character constant.
		 */ 
		public function unread():int {
			if (offset == start)
				return Util.EOF;
			offset--;
			return document.getChar(offset);
		}
		
		/**
		 * Returns the next character at this scanner's offset.
		 * If there are no more characters, returns the EOF character constant.
		 */ 
		public function read():int {
			var char:int;
			if (offset < end) {
				char = document.getChar(offset);
				offset++;
			} else {
				char = Util.EOF;
			}
			return char;
		}
		
		public function getOffset():int {
			return offset;
		}
		
		public function setOffset(value:int):void {
			offset = Math.max(0, value); 
		}
		
		public function getEnd():int {
			return end;
		}
		
		public function setEnd(value:int):void {
			end = value;	
		}
		
		public function getDocument():Document {
			return document; 
		}
		
		public function setDocument(value:Document):void {
			document = value;
		}
		
		/**
		 * Returns the index where the line at the specified position starts.
		 * After getting the index, it sets the scanner's offset to where it was before.
		 */ 
		public function getLineStart(position:int):int {
			var rememberedOffset:int = offset;
			var offsetToReturn:int;
			setRange(document, 0, position);
			offset = position;
			var char:int =0;
			while (char != Util.EOLN && char != Util.EOF) {
				char = unread();
			}
			if (char == Util.EOF) {
				offsetToReturn = offset;
			} else {
				offsetToReturn = offset + 1;
			}
			offset = rememberedOffset;
			return offsetToReturn;
		}
		
		/**
		 * Returns the index where the line at the specified position ends.
		 * After getting the index, it sets the scanner's offset to where it was before.
		 */ 
		public function getLineEnd(position:int):int {
			var rememberedOffset:int = offset;
			var offsetToReturn:int;
			setRange(document, position, document.endLimit - position);
			var char:int =0;
			while (char != Util.EOLN && char != Util.EOF) {
				char = read();
			}
			if (char == Util.EOF) {
				offsetToReturn = offset;
			} else {
				offsetToReturn = offset - 1;
			}
			offset = rememberedOffset;
			return offsetToReturn;
		}
		
	}
}