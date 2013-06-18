package com.crispico.flower.texteditor.mxml.rules
{
	import com.crispico.flower.texteditor.model.Token;
	import com.crispico.flower.texteditor.rules.MultipleContentTypePatternRule;
	import com.crispico.flower.texteditor.rules.PatternRule;
	import com.crispico.flower.texteditor.scanners.ICharacterScanner;
	
	/**
	 * Special rule to separate a special tag from a special start tag that would trigger the use
	 * of a specific partitioner (i.e. <mx:Style source="test.css" /> vs. 
	 * <mx:Style> 
	 * 		// style declarations
	 * </mx:Style>).
	 */ 
	public class SpecialTagRule extends MultipleContentTypePatternRule {
		
		public function SpecialTagRule(rule:PatternRule) {
			super(rule);
		}
		
		override protected function getKey(scanner:ICharacterScanner, startOffset:int, endOffset:int):String {
			var char:int = scanner.unread();
			char = scanner.unread();
			var flag:Boolean = false;
			if (char == 47) // code for slash, this tag is not a start tag
				flag = true;
			scanner.read(); scanner.read();
			var key:String = super.getKey(scanner, startOffset, endOffset);
			if (flag) {
				if (key == "mx:Style" || key == "mx:Script")
					key += "_SPECIAL";
				if (key == "/mx:Script")
					key = null;
			}
			return key;
		}
	}
}