package com.crispico.flower.texteditor.css.providers {
	
	import com.crispico.flower.texteditor.css.CSSContentTypeConstants;
	import com.crispico.flower.texteditor.css.tokenizing.CSSDeclarationsScanner;
	import com.crispico.flower.texteditor.providers.IPartitionTokenizerProvider;
	import com.crispico.flower.texteditor.scanners.RuleBasedScanner;

	/**
	 * Provides partition tokenizers specific for CSS.
	 */ 
	public class CSSPartitionTokenizerProvider implements IPartitionTokenizerProvider {
		
		/**
		 * Lazy initializations to prevent creating new objects every time a scanner is needed.
		 */ 
		private const defaultScanner:RuleBasedScanner = new RuleBasedScanner(CSSContentTypeConstants.CSS_SELECTOR);
		private const declarationsScanner:CSSDeclarationsScanner = new CSSDeclarationsScanner();
		private const bracketScanner:RuleBasedScanner = new RuleBasedScanner(CSSContentTypeConstants.CSS_DEFAULT);
		private const commentScanner:RuleBasedScanner = new RuleBasedScanner(CSSContentTypeConstants.CSS_COMMENT);
		private const stringScanner:RuleBasedScanner = new RuleBasedScanner(CSSContentTypeConstants.CSS_DOUBLE_QUOTES_STRING);
		
		public function getPartitionTokenizer(partitionContentType:String):RuleBasedScanner {
			switch (partitionContentType) {
				case CSSContentTypeConstants.CSS_DEFAULT : 
					return defaultScanner;
				case CSSContentTypeConstants.CSS_DECLARATIONS: 
					return declarationsScanner;
				case CSSContentTypeConstants.CSS_DECLARATIONS_START_BRACKET:
				case CSSContentTypeConstants.CSS_DECLARATIONS_END_BRACKET: 
					return bracketScanner;
				case CSSContentTypeConstants.CSS_SINGLE_LINE_COMMENT:
				case CSSContentTypeConstants.CSS_MULTILINE_COMMENT:
					return commentScanner; 
				case CSSContentTypeConstants.CSS_DOUBLE_QUOTES_STRING: 
				case CSSContentTypeConstants.CSS_SINGLE_QUOTES_STRING:
					return stringScanner;
				
				default: 
					return null;
			}
		}
	}
}