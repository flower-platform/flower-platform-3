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
package com.crispico.flower.texteditor.java.providers {
	
	import com.crispico.flower.texteditor.java.JavaContentTypeConstants;
	import com.crispico.flower.texteditor.java.tokenizing.JavaCommentScanner;
	import com.crispico.flower.texteditor.java.tokenizing.JavaDocScanner;
	import com.crispico.flower.texteditor.java.tokenizing.JavaScanner;
	import com.crispico.flower.texteditor.java.tokenizing.JavaStringScanner;
	import com.crispico.flower.texteditor.providers.IPartitionTokenizerProvider;
	import com.crispico.flower.texteditor.scanners.RuleBasedScanner;

	/**
	 * Provides partition tokenizers specific for Java.
	 */ 
	public class JavaPartitionTokenizerProvider implements IPartitionTokenizerProvider {
		
		/**
		 * Lazy initializations to prevent creating new objects every time a scanner is needed.
		 */ 
		private const defaultScanner:JavaScanner = new JavaScanner();
		private const docScanner:JavaDocScanner = new JavaDocScanner();
		private const commentScanner:JavaCommentScanner = new JavaCommentScanner();
		private const stringScanner:JavaStringScanner = new JavaStringScanner();

		public function getPartitionTokenizer(partitionContentType:String):RuleBasedScanner {
			switch (partitionContentType) {
				case JavaContentTypeConstants.JAVA_DEFAULT: 
					return defaultScanner;
				case JavaContentTypeConstants.JAVA_DOC: 
					return docScanner;
				case JavaContentTypeConstants.JAVA_SINGLE_LINE_COMMENT:
				case JavaContentTypeConstants.JAVA_MULTILINE_COMMENT:
				case JavaContentTypeConstants.JAVA_EMPTY_COMMENT: 
					return commentScanner;
				case JavaContentTypeConstants.JAVA_DOUBLE_QUOTES_STRING:
				case JavaContentTypeConstants.JAVA_SINGLE_QUOTES_STRING: 
					return stringScanner;
					
				default: 
				    return null;
			}
		}
	}
}