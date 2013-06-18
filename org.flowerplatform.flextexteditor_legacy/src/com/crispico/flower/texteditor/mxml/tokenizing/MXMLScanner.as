package com.crispico.flower.texteditor.mxml.tokenizing {
	
	import com.crispico.flower.texteditor.mxml.MXMLContentTypeConstants;
	import com.crispico.flower.texteditor.scanners.RuleBasedScanner;

	public class MXMLScanner extends RuleBasedScanner {
		public function MXMLScanner() {
			super(MXMLContentTypeConstants.MXML_DEFAULT);
		}
	}
}