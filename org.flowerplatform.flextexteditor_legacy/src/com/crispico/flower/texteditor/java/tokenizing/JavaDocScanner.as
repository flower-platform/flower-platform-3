package com.crispico.flower.texteditor.java.tokenizing {
	
	import com.crispico.flower.texteditor.java.JavaContentTypeConstants;
	import com.crispico.flower.texteditor.rules.PatternRule;
	import com.crispico.flower.texteditor.scanners.RuleBasedScanner;

	public class JavaDocScanner extends RuleBasedScanner {
		
		public function JavaDocScanner() {
			super(JavaContentTypeConstants.JAVA_DOC_DEFAULT);
			
			addRule(new PatternRule("{@link", "}", JavaContentTypeConstants.JAVA_DOC_LINK, false));
			addRule(new PatternRule("@", " ", JavaContentTypeConstants.JAVA_DOC_TAG));
			addRule(new PatternRule("<", ">", JavaContentTypeConstants.JAVA_DOC_HTML_TAG));
		}
	}
}