package com.crispico.flower.texteditor.actionscript.tokenizing {
	
	import com.crispico.flower.texteditor.actionscript.AS3ContentTypeConstants;
	import com.crispico.flower.texteditor.rules.PatternRule;
	import com.crispico.flower.texteditor.scanners.RuleBasedScanner;

	public class AS3DocScanner extends RuleBasedScanner {
		
		public function AS3DocScanner() {
			super(AS3ContentTypeConstants.AS3_DOC_DEFAULT);
			
			addRule(new PatternRule("@", " ", AS3ContentTypeConstants.AS3_DOC_TAG));
			addRule(new PatternRule("<", ">", AS3ContentTypeConstants.AS3_DOC_HTML_TAG));
		}
	}
}