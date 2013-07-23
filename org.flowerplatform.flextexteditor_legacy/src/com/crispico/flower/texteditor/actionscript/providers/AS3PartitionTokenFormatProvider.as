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
package com.crispico.flower.texteditor.actionscript.providers {
	
	import com.crispico.flower.texteditor.actionscript.AS3ContentTypeConstants;
	import com.crispico.flower.texteditor.providers.IPartitionTokenFormatProvider;
	import com.crispico.flower.texteditor.utils.Util;
	
	import flashx.textLayout.formats.TextLayoutFormat;

	/**
	 * Provides formats specific to ActionScript.
	 */
	public class AS3PartitionTokenFormatProvider implements IPartitionTokenFormatProvider {
		
		/**
		 * Lazy initializations to prevent creating new objects every time a format is needed.
		 */ 
		private const defaultFormat:TextLayoutFormat = Util.getFormat(0x000000, false, false);
		private const keywordFormat:TextLayoutFormat = Util.getFormat(0x0033ff, false, true);
		private const typeFormat:TextLayoutFormat = Util.getFormat(0x9900cc, false, true);
		private const functionFormat:TextLayoutFormat = Util.getFormat(0x339966, false, true);
		private const varFormat:TextLayoutFormat = Util.getFormat(0x6699cc, false, true);
		private const traceFormat:TextLayoutFormat = Util.getFormat(0xcc6666, false, true);
		private const commentFormat:TextLayoutFormat = Util.getFormat(0x009900, true, false);
		private const todoFormat:TextLayoutFormat = Util.getFormat(0xAAAAFF, false, true);
		private const docFormat:TextLayoutFormat = Util.getFormat(0x3f5fbf, false, false);
		private const tagFormat:TextLayoutFormat = Util.getFormat(0xAAAAAA, false, false); 
		private const stringFormat:TextLayoutFormat = Util.getFormat(0x990000, false, true);
		
		public function getFormat(tokenType:String):TextLayoutFormat {
			switch (tokenType) {
				case AS3ContentTypeConstants.AS3_DEFAULT_DEFAULT: 
					return defaultFormat;
				case AS3ContentTypeConstants.AS3_DEFAULT_KEYWORD: 
					return keywordFormat;
				case AS3ContentTypeConstants.AS3_DEFAULT_TYPE: 
					return typeFormat;
				case AS3ContentTypeConstants.AS3_DEFAULT_FUNCTION: 
					return functionFormat;
				case AS3ContentTypeConstants.AS3_DEFAULT_VAR: 
					return varFormat;
				case AS3ContentTypeConstants.AS3_DEFAULT_TRACE:
					return traceFormat;
				case AS3ContentTypeConstants.AS3_COMMENT_DEFAULT: 
					return commentFormat;
				case AS3ContentTypeConstants.AS3_COMMENT_TODO: 
					return todoFormat;
				case AS3ContentTypeConstants.AS3_DOC_DEFAULT: 
					return docFormat;
				case AS3ContentTypeConstants.AS3_DOC_TAG:
				case AS3ContentTypeConstants.AS3_DOC_HTML_TAG: 
					return tagFormat;
				case AS3ContentTypeConstants.AS3_STRING_DEFAULT: 
					return stringFormat;

				default: 
					return null;
			}
		}
	}
}