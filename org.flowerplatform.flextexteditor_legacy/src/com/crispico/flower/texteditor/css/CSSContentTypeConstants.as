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
package com.crispico.flower.texteditor.css {
	
	public class CSSContentTypeConstants {
		
		public static const CSS_DEFAULT:String = "CSS_DEFAULT";
		public static const CSS_SELECTOR:String = "CSS_SELECTOR";
	
		public static const CSS_COMMENT:String = "CSS_COMMENT";
			public static const CSS_SINGLE_LINE_COMMENT:String = CSS_COMMENT + "_SINGLE_LINE";
			public static const CSS_MULTILINE_COMMENT:String = CSS_COMMENT + "_MULTILINE";
	
		public static const CSS_DOUBLE_QUOTES_STRING:String = "CSS_DOUBLE_QUOTES_STRING";
		public static const CSS_SINGLE_QUOTES_STRING:String = "CSS_SINGLE_QUOTES_STRING";
		
		public static const CSS_DECLARATIONS:String = "CSS_DECLARATIONS";
			public static const CSS_DECLARATIONS_START_BRACKET:String = CSS_DECLARATIONS + "_START_BRACKET";
			public static const CSS_DECLARATIONS_END_BRACKET:String = CSS_DECLARATIONS + "_END_BRACKET";
			public static const CSS_DECLARATIONS_WORDS:String = CSS_DECLARATIONS + "_WORDS";
	}
}