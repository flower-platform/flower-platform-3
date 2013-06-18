package com.crispico.flower.texteditor.actionscript.providers {
	
	import com.crispico.flower.texteditor.actionscript.AS3ContentTypeConstants;
	import com.crispico.flower.texteditor.actionscript.tokenizing.AS3CommentScanner;
	import com.crispico.flower.texteditor.actionscript.tokenizing.AS3DocScanner;
	import com.crispico.flower.texteditor.actionscript.tokenizing.AS3Scanner;
	import com.crispico.flower.texteditor.actionscript.tokenizing.AS3StringScanner;
	import com.crispico.flower.texteditor.providers.IPartitionTokenizerProvider;
	import com.crispico.flower.texteditor.scanners.RuleBasedScanner;

	/**
	 * Provides partition tokenizers specific for ActionScript.
	 */ 
	public class AS3PartitionTokenizerProvider implements IPartitionTokenizerProvider {
		
		/**
		 * Lazy initializations to prevent creating new objects every time a scanner is needed.
		 */ 
		private const defaultScanner:AS3Scanner = new AS3Scanner();
		private const docScanner:AS3DocScanner = new AS3DocScanner();
		private const commentScanner:AS3CommentScanner = new AS3CommentScanner();
		private const stringScanner:AS3StringScanner = new AS3StringScanner();
		
		public function getPartitionTokenizer(partitionContentType:String):RuleBasedScanner {
			switch (partitionContentType) {
				case AS3ContentTypeConstants.AS3_DEFAULT: 
					return defaultScanner;
				case AS3ContentTypeConstants.AS3_DOC: 
					return docScanner; 
				case AS3ContentTypeConstants.AS3_SINGLE_LINE_COMMENT:
				case AS3ContentTypeConstants.AS3_MULTILINE_COMMENT:
				case AS3ContentTypeConstants.AS3_EMPTY_COMMENT: 
					return commentScanner; 
				case AS3ContentTypeConstants.AS3_DOUBLE_QUOTES_STRING:
				case AS3ContentTypeConstants.AS3_SINGLE_QUOTES_STRING: 
					return stringScanner; 
				
				default: 
					return null;
			}
		}
	}
}