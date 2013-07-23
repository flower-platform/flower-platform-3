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
package com.crispico.flower.texteditor.mxml.providers {
	
	import com.crispico.flower.texteditor.actionscript.providers.AS3PartitionTokenizerProvider;
	import com.crispico.flower.texteditor.css.providers.CSSPartitionTokenizerProvider;
	import com.crispico.flower.texteditor.mxml.MXMLContentTypeConstants;
	import com.crispico.flower.texteditor.mxml.tokenizing.MXMLCommentScanner;
	import com.crispico.flower.texteditor.mxml.tokenizing.MXMLSpecialTagScanner;
	import com.crispico.flower.texteditor.mxml.tokenizing.MXMLTagScanner;
	import com.crispico.flower.texteditor.providers.IComposedPartitionTokenizerProvider;
	import com.crispico.flower.texteditor.scanners.RuleBasedScanner;
	
	import mx.collections.ArrayCollection;

	public class MXMLPartitionTokenizerProvider implements IComposedPartitionTokenizerProvider {
		
		/**
		 * MXML needs coloring scanners already declared in the ActionScript and CSS providers.
		 */ 
		private const as3Provider:AS3PartitionTokenizerProvider = new AS3PartitionTokenizerProvider();
		private const cssProvider:CSSPartitionTokenizerProvider = new CSSPartitionTokenizerProvider();
		private const internalProvidersList:ArrayCollection = new ArrayCollection([as3Provider, cssProvider]);
		
		/**
		 * Lazy initializations to prevent creating new objects every time a scanner is needed.
		 */ 
		private const cdataTagScanner:RuleBasedScanner = new RuleBasedScanner(MXMLContentTypeConstants.MXML_DEFAULT);
		private const mxmlTagScanner:MXMLTagScanner = new MXMLTagScanner();
		private const mxmlSpecialTagScanner:MXMLSpecialTagScanner = new MXMLSpecialTagScanner();
		private const mxmlCommentTagScanner:MXMLCommentScanner = new MXMLCommentScanner();

		public function getPartitionTokenizer(partitionContentType:String):RuleBasedScanner {
			switch (partitionContentType) {
				case MXMLContentTypeConstants.MXML_DEFAULT: 
				case MXMLContentTypeConstants.MXML_CDATA_START_TAG:
				case MXMLContentTypeConstants.MXML_CDATA_END_TAG: 
					return cdataTagScanner;
				case MXMLContentTypeConstants.MXML_TAG: 
					return mxmlTagScanner;
				case MXMLContentTypeConstants.MXML_SCRIPT_TAG:
				case MXMLContentTypeConstants.MXML_SCRIPT_START_TAG:
				case MXMLContentTypeConstants.MXML_SCRIPT_END_TAG: 
				case MXMLContentTypeConstants.MXML_STYLE_TAG:
				case MXMLContentTypeConstants.MXML_STYLE_START_TAG: 
				case MXMLContentTypeConstants.MXML_STYLE_END_TAG: 
					return mxmlSpecialTagScanner;
				case MXMLContentTypeConstants.MXML_COMMENT_TAG: 
					return mxmlCommentTagScanner;

				default: 
					return null;
			}
		} 
		
		public function getProviders():ArrayCollection {
			return internalProvidersList;
		}		
	}
}