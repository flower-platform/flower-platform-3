package com.crispico.flower.texteditor.mxml.tokenizing {
	
	import com.crispico.flower.texteditor.mxml.MXMLContentTypeConstants;
	import com.crispico.flower.texteditor.scanners.RuleBasedScanner;

	public class MXMLCommentScanner extends RuleBasedScanner {
		
		public function MXMLCommentScanner() {
			super(MXMLContentTypeConstants.MXML_COMMENT_TAG);
		}
		
	}
}