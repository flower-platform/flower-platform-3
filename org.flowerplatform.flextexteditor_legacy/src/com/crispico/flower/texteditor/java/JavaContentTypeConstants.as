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
package com.crispico.flower.texteditor.java {
	
	public class JavaContentTypeConstants {
		
		public static const JAVA_DEFAULT:String = "JAVA_DEFAULT_CONTENT";
			public static const JAVA_DEFAULT_DEFAULT:String = JAVA_DEFAULT + "_DEFAULT";
			public static const JAVA_DEFAULT_KEYWORD:String = JAVA_DEFAULT + "_KEYWORD";
			public static const JAVA_DEFAULT_ANNOTATION:String = JAVA_DEFAULT + "_ANNOTATION";
		
		public static const JAVA_SINGLE_LINE_COMMENT:String = "JAVA_SINGLE_LINE_COMMENT";
		public static const JAVA_MULTILINE_COMMENT:String = "JAVA_MULTILINE_COMMENT";
		public static const JAVA_EMPTY_COMMENT:String = "JAVA_EMPTY_COMMENT";
			public static const JAVA_COMMENT_DEFAULT:String = "JAVA_COMMENT_DEFAULT";
			public static const JAVA_COMMENT_TODO:String = "JAVA_COMMENT_TODO";
			
		public static const JAVA_SINGLE_QUOTES_STRING:String = "JAVA_SINGLE_QUOTES_STRING";		
		public static const JAVA_DOUBLE_QUOTES_STRING:String = "JAVA_DOUBLE_QUOTES_STRING";
			public static const JAVA_STRING_DEFAULT:String = "JAVA_STRING_DEFAULT";
			
		public static const JAVA_DOC:String = "JAVA_DOC";
			public static const JAVA_DOC_DEFAULT:String = JAVA_DOC + "_DEFAULT";
			public static const JAVA_DOC_LINK:String = JAVA_DOC + "_LINK";
			public static const JAVA_DOC_TAG:String = JAVA_DOC + "_TAG";
			public static const JAVA_DOC_HTML_TAG:String = JAVA_DOC + "_HTML_TAG";
	}
}