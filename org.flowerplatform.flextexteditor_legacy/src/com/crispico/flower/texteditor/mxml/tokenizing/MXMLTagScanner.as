package com.crispico.flower.texteditor.mxml.tokenizing {
	
	import com.crispico.flower.texteditor.mxml.MXMLContentTypeConstants;
	import com.crispico.flower.texteditor.rules.BackslashEscapePatternRule;
	import com.crispico.flower.texteditor.rules.PatternRule;
	import com.crispico.flower.texteditor.rules.WordRule;
	import com.crispico.flower.texteditor.scanners.RuleBasedScanner;
	import com.crispico.flower.texteditor.utils.Util;
	
	import mx.collections.ArrayCollection;

	public class MXMLTagScanner extends RuleBasedScanner {
		
		public function MXMLTagScanner() {
			super(MXMLContentTypeConstants.MXML_TAG);
			
			addRule(new WordRule(MXMLContentTypeConstants.MXML_TAG_DELIMITER, ["<", ">", "</", "/>"], false, false));
			var tagRule:WordRule = new WordRule(MXMLContentTypeConstants.MXML_TAG_NAME);
			tagRule.startDelimiters = Util.getCharCodes("<");
			var endDelimiters:ArrayCollection = Util.getCharCodes(" \t\n>");
			endDelimiters.addItem(Util.EOF);
			tagRule.endDelimiters = endDelimiters;
			addRule(tagRule);
			
			addRule(new BackslashEscapePatternRule('"', '"', MXMLContentTypeConstants.MXML_DOUBLE_QUOTES_STRING));
			addRule(new BackslashEscapePatternRule("'", "'", MXMLContentTypeConstants.MXML_SINGLE_QUOTES_STRING));
		}
	}
}