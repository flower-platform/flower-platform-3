package com.crispico.flower.texteditor.mxml.providers {
	
	import com.crispico.flower.texteditor.actionscript.providers.AS3PartitionTokenFormatProvider;
	import com.crispico.flower.texteditor.css.providers.CSSPartitionTokenFormatProvider;
	import com.crispico.flower.texteditor.mxml.MXMLContentTypeConstants;
	import com.crispico.flower.texteditor.providers.IComposedPartitionTokenFormatProvider;
	import com.crispico.flower.texteditor.utils.Util;
	
	import flashx.textLayout.formats.TextLayoutFormat;
	
	import mx.collections.ArrayCollection;
	
	public class MXMLPartitionTokenFormatProvider implements IComposedPartitionTokenFormatProvider {
		
		/**
		 * MXML needs formats already declared in the ActionScript and CSS providers.
		 */ 
		private const as3Provider:AS3PartitionTokenFormatProvider = new AS3PartitionTokenFormatProvider();
		private const cssProvider:CSSPartitionTokenFormatProvider = new CSSPartitionTokenFormatProvider();
		private const internalProvidersList:ArrayCollection = new ArrayCollection([as3Provider, cssProvider]);
		
		/**
		 * Lazy initializations to prevent creating new objects every time a format is needed.
		 */ 
		private const defaultFormat:TextLayoutFormat = Util.getFormat(0x000000, false, false);
		private const tagFormat:TextLayoutFormat = Util.getFormat(0x0000ff, false, false);
		private const commentFormat:TextLayoutFormat = Util.getFormat(0x800000, false, false);
		private const specialTagFormat:TextLayoutFormat = Util.getFormat(0x006633, false, false);
		private const stringFormat:TextLayoutFormat = Util.getFormat(0x990000, false, false);

		public function getFormat(tokenType:String):TextLayoutFormat {
			switch (tokenType) {
				case MXMLContentTypeConstants.MXML_DEFAULT:
				case MXMLContentTypeConstants.MXML_TAG: 
					return defaultFormat;
				case MXMLContentTypeConstants.MXML_TAG_DELIMITER:
				case MXMLContentTypeConstants.MXML_TAG_NAME: 
					return tagFormat;
				case MXMLContentTypeConstants.MXML_COMMENT_TAG: 
					return commentFormat;
				case MXMLContentTypeConstants.MXML_SPECIAL_TAG_NAME:
				case MXMLContentTypeConstants.MXML_SPECIAL_TAG_DELIMITER: 
					return specialTagFormat;
				case MXMLContentTypeConstants.MXML_SINGLE_QUOTES_STRING:
				case MXMLContentTypeConstants.MXML_DOUBLE_QUOTES_STRING: 
					return stringFormat;
		
				default: 
					return null;
			}
		}
		
		public function getProviders():ArrayCollection {
			return internalProvidersList;
		}
	}
}