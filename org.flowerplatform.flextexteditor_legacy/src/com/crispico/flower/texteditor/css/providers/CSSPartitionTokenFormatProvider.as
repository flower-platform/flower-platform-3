package com.crispico.flower.texteditor.css.providers {
	
	import com.crispico.flower.texteditor.css.CSSContentTypeConstants;
	import com.crispico.flower.texteditor.providers.IPartitionTokenFormatProvider;
	import com.crispico.flower.texteditor.utils.Util;
	
	import flashx.textLayout.formats.TextLayoutFormat;

	/**
	 * Provides formats specific to CSS.
	 */
	public class CSSPartitionTokenFormatProvider implements IPartitionTokenFormatProvider {
		
		/**
		 * Lazy initializations to prevent creating new objects every time a format is needed.
		 */ 
		private const defaultFormat:TextLayoutFormat = Util.getFormat(0x000000, false, false);
		private const selectorFormat:TextLayoutFormat = Util.getFormat(0xff00ff, false, false);
		private const commentFormat:TextLayoutFormat = Util.getFormat(0x999999, true, false);
		private const stringFormat:TextLayoutFormat = Util.getFormat(0x990000, false, false);
		private const declarationsFormat:TextLayoutFormat = Util.getFormat(0x330099, false, false);
		
		public function getFormat(tokenType:String):TextLayoutFormat {
			switch (tokenType) {
				case CSSContentTypeConstants.CSS_DEFAULT: 
					return defaultFormat;
				case CSSContentTypeConstants.CSS_SELECTOR: 
					return selectorFormat;
				case CSSContentTypeConstants.CSS_COMMENT: 
					return commentFormat;
				case CSSContentTypeConstants.CSS_DOUBLE_QUOTES_STRING: 
				case CSSContentTypeConstants.CSS_SINGLE_QUOTES_STRING:
					return stringFormat;
				case CSSContentTypeConstants.CSS_DECLARATIONS_WORDS: 
					return declarationsFormat;
				
				default: 
					return null;
			}
		}
	}
}