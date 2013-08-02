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
	
	import com.crispico.flower.texteditor.model.Range;
	
	/**
	 * A change in the document starting at a specific offset. The length of the deleted text is length,
	 * and the length of the inserted text is textLength.
	 * 
	 * 
	 */
	public class DocumentChange extends Range {
		
		/**
		 * The length of the inserted text.
		 */ 
		public var textLength:int;
		
		public function DocumentChange(offset:int, length:int, textLength:int)	{
			super(offset, length);
			this.textLength = textLength;
		}
	}
}