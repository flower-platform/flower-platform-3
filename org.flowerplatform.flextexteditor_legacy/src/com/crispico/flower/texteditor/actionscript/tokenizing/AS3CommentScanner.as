package com.crispico.flower.texteditor.actionscript.tokenizing {
	
	import com.crispico.flower.texteditor.actionscript.AS3ContentTypeConstants;
	import com.crispico.flower.texteditor.rules.WordRule;
	import com.crispico.flower.texteditor.scanners.RuleBasedScanner;
	
	import mx.collections.ArrayCollection;
	
	public class AS3CommentScanner extends RuleBasedScanner {
		
		public function AS3CommentScanner() {
			super(AS3ContentTypeConstants.AS3_COMMENT_DEFAULT);
			
			addRule(new WordRule(AS3ContentTypeConstants.AS3_COMMENT_TODO, ["TODO"]));
		}
	}
}