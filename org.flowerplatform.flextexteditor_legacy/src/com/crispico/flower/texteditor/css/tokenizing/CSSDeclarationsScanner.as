package com.crispico.flower.texteditor.css.tokenizing {
	
	import com.crispico.flower.texteditor.css.CSSContentTypeConstants;
	import com.crispico.flower.texteditor.rules.WordRule;
	import com.crispico.flower.texteditor.scanners.RuleBasedScanner;

	public class CSSDeclarationsScanner extends RuleBasedScanner {
		public function CSSDeclarationsScanner() {
			super(CSSContentTypeConstants.CSS_DEFAULT);
			
			addRule(new WordRule(CSSContentTypeConstants.CSS_DECLARATIONS_WORDS));
		}
	}
}