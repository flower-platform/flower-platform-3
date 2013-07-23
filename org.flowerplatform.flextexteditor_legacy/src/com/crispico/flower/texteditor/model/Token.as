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
	 * A range of text that has a specific type.
	 * 
	 * @flowerModelElementId _0rUU0O2uEeCF5Ozw-0NJ0A
	 */
	public class Token extends Range {
		
		/**
		 * The type of this token.
		 */ 
		public var type:String;
		
		private static const TYPE_EOF:String = "EOF";
		private static const TYPE_UNDEFINED:String = "UNDEFINED";
		
		/**
		 * Returned when the end of file (end of range to scan) is reached.
		 */ 
		public static const EOF:Token = new Token(TYPE_EOF);
		
		/**
		 * Returned when the rule does not match.
		 */ 
		public static const UNDEFINED:Token = new Token(TYPE_UNDEFINED);
		
		public function Token(value:String):void {
			super();
			this.type = value;
		}
		
		/**
		 * Returns true if this is an undefined token, false otherwise.
		 */ 
		public function isUndefined():Boolean {
			return type == TYPE_UNDEFINED;
		}
		
		/**
		 * Returns true if this is an EOF token, false otherwise.
		 */ 
		public function isEof():Boolean {
			return type == TYPE_EOF;
		}
	}
}