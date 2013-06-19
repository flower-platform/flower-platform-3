package com.crispico.flower.texteditor.java.tokenizing {
	
	import com.crispico.flower.texteditor.java.JavaContentTypeConstants;
	import com.crispico.flower.texteditor.scanners.RuleBasedScanner;

	public class JavaStringScanner extends RuleBasedScanner {
		
		public function JavaStringScanner() {
			super(JavaContentTypeConstants.JAVA_STRING_DEFAULT);
		}
	}
}