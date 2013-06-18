package com.crispico.flower.texteditor.actionscript.tokenizing {
	
	import com.crispico.flower.texteditor.actionscript.AS3ContentTypeConstants;
	import com.crispico.flower.texteditor.scanners.RuleBasedScanner;

	public class AS3StringScanner extends RuleBasedScanner {
		
		public function AS3StringScanner() {
			super(AS3ContentTypeConstants.AS3_STRING_DEFAULT);
		}	
	}
}