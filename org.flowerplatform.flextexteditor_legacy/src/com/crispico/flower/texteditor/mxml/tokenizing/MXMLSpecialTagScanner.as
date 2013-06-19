package com.crispico.flower.texteditor.mxml.tokenizing {
	
	import com.crispico.flower.texteditor.mxml.MXMLContentTypeConstants;
	import com.crispico.flower.texteditor.rules.BackslashEscapePatternRule;
	import com.crispico.flower.texteditor.rules.PatternRule;
	import com.crispico.flower.texteditor.rules.WordRule;
	import com.crispico.flower.texteditor.scanners.RuleBasedScanner;
	import com.crispico.flower.texteditor.utils.Util;

	public class MXMLSpecialTagScanner extends RuleBasedScanner {
		
		public function MXMLSpecialTagScanner() {
			super(MXMLContentTypeConstants.MXML_DEFAULT);
			
			addRule(new WordRule(MXMLContentTypeConstants.MXML_SPECIAL_TAG_DELIMITER, ["<", ">", "</", "/>"], false, false));
			var tagRule:WordRule = new WordRule(MXMLContentTypeConstants.MXML_SPECIAL_TAG_NAME);
			tagRule.startDelimiters = Util.getCharCodes("<");
			tagRule.endDelimiters = Util.getCharCodes(" \t\n>");
			addRule(tagRule);
			
			var stringRule:PatternRule = new BackslashEscapePatternRule('"', '"', MXMLContentTypeConstants.MXML_DOUBLE_QUOTES_STRING);
			addRule(stringRule);
			addRule(new BackslashEscapePatternRule("'", "'", MXMLContentTypeConstants.MXML_SINGLE_QUOTES_STRING));
		}
	}
}