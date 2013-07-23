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
package com.crispico.flower.texteditor.model {
	
	/** 
	 * A region in the document determined by its offset and length.
	 */ 
	public class Range {
		
		/**
		 * The position in the document where this range starts.
		 */ 
		public var offset:int;
		
		/**
		 * The length of this range.
		 */ 
		public var length:int;
		
		public function Range(offset:int=-1, length:int=-1) {
			this.offset = offset;
			this.length = length;
		}
	}
}