package com.crispico.flower.texteditor.java.providers {
	
	import com.crispico.flower.texteditor.java.JavaContentTypeConstants;
	import com.crispico.flower.texteditor.providers.IPartitionTokenFormatProvider;
	import com.crispico.flower.texteditor.utils.Util;
	
	import flashx.textLayout.formats.TextLayoutFormat;

	/**
	 * Provides formats specific to Java.
	 */ 
	public class JavaPartitionTokenFormatProvider implements IPartitionTokenFormatProvider {
		
		/**
		 * Lazy initializations to prevent creating new objects every time a format is needed.
		 */ 
		private const defaultFormat:TextLayoutFormat = Util.getFormat(0x000000, false, false);
		private const keywordFormat:TextLayoutFormat = Util.getFormat(0x7F0055, false, true);
		private const commentFormat:TextLayoutFormat = Util.getFormat(0x3F7F5F, true, false);
		private const todoFormat:TextLayoutFormat = Util.getFormat(0x7F9FBF, false, true);
		private const docFormat:TextLayoutFormat = Util.getFormat(0x3F5FBF, false, false);
		private const docLinkFormat:TextLayoutFormat = Util.getFormat(0x3F3FBF, false, false);
		private const docTagFormat:TextLayoutFormat = Util.getFormat(0x7F9FBF, false, true);
		private const docHtmlTagFormat:TextLayoutFormat = Util.getFormat(0x7F7F9F, false, false);
		private const stringFormat:TextLayoutFormat = Util.getFormat(0x2A00FF, false, false);
		private const annotationFormat:TextLayoutFormat = Util.getFormat(0x646464, false, false);
		
		public function getFormat(tokenType:String):TextLayoutFormat {
			switch (tokenType) {
				case JavaContentTypeConstants.JAVA_DEFAULT_DEFAULT: 
					return defaultFormat;
				case JavaContentTypeConstants.JAVA_DEFAULT_KEYWORD: 
					return keywordFormat;
				case JavaContentTypeConstants.JAVA_COMMENT_DEFAULT: 
					return commentFormat;
				case JavaContentTypeConstants.JAVA_COMMENT_TODO: 
					return todoFormat;
				case JavaContentTypeConstants.JAVA_DOC_DEFAULT: 
					return docFormat;
				case JavaContentTypeConstants.JAVA_DOC_LINK:
					return docLinkFormat;
				case JavaContentTypeConstants.JAVA_DOC_TAG:
					return docTagFormat;
				case JavaContentTypeConstants.JAVA_DOC_HTML_TAG: 
					return docHtmlTagFormat;
				case JavaContentTypeConstants.JAVA_STRING_DEFAULT: 
					return stringFormat;
				case JavaContentTypeConstants.JAVA_DEFAULT_ANNOTATION:
					return annotationFormat;
				
				default: 
					return null;;
			}
		}
	}
}