package com.crispico.flower.texteditor.java.tokenizing {
	
	import com.crispico.flower.texteditor.java.JavaContentTypeConstants;
	import com.crispico.flower.texteditor.rules.WordRule;
	import com.crispico.flower.texteditor.scanners.RuleBasedScanner;
	
	import mx.collections.ArrayCollection;

	public class JavaCommentScanner extends RuleBasedScanner {
		
		public function JavaCommentScanner() {
			super(JavaContentTypeConstants.JAVA_COMMENT_DEFAULT);
			
			addRule(new WordRule(JavaContentTypeConstants.JAVA_COMMENT_TODO, ["TODO"]));
		}
	}
}