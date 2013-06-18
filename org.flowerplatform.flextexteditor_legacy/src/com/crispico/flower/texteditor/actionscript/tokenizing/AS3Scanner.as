package com.crispico.flower.texteditor.actionscript.tokenizing {
	
	import com.crispico.flower.texteditor.actionscript.AS3ContentTypeConstants;
	import com.crispico.flower.texteditor.rules.MultipleContentTypeWordRule;
	import com.crispico.flower.texteditor.scanners.RuleBasedScanner;
	import com.crispico.flower.texteditor.utils.Util;
 
	public class AS3Scanner extends RuleBasedScanner {
		
		public function AS3Scanner() {
			super(AS3ContentTypeConstants.AS3_DEFAULT_DEFAULT);
			
			var identifierRule:MultipleContentTypeWordRule = new MultipleContentTypeWordRule(AS3ContentTypeConstants.AS3_DEFAULT_DEFAULT);
			identifierRule.addWords(["class", "interface", "package"], AS3ContentTypeConstants.AS3_DEFAULT_TYPE);
			identifierRule.addWords(["function"], AS3ContentTypeConstants.AS3_DEFAULT_FUNCTION);
			identifierRule.addWords(["var"], AS3ContentTypeConstants.AS3_DEFAULT_VAR);
			identifierRule.addWords(["trace"], AS3ContentTypeConstants.AS3_DEFAULT_TRACE);
			var keywords:Array = ["as", "break", "case", "catch", "const", "continue", 
								  "default", "delete", "do", "else", "extends", "false",
								  "finally", "for", "if", "implements", "import", "in",
								  "instanceof", "internal", "is", "native", "new", "null", 
								  "private", "protected", "public", "return",
								  "super", "switch", "this", "throw", "to", "true",
								  "try", "typeof", "use", "void", "while", "with", "each", 
								  "get", "set", "namespace", "include", "dynamic", "final",
								  "native", "override", "static"];
			identifierRule.addWords(keywords, AS3ContentTypeConstants.AS3_DEFAULT_KEYWORD);
			
			var metadataKeywords:Array = ["Alternative", "ArrayElementType", "Bindable", 
										  "DefaultProperty", "Deprecated", "Effect", 
										  "Embed", "Event", "Exclude", "ExcludeClass", 
										  "HostComponent", "IconFile", "Inspectable", 
										  "InstanceType", "NonCommittingChangeEvent",
										  "RemoteClass", "RichTextContent", "SkinPart",
										  "SkinState", "Style", "SWF", "Transient"]; 
			var code:int = Util.getCharCodes("[").getItemAt(0) as int;																
			identifierRule.addWords(metadataKeywords, AS3ContentTypeConstants.AS3_DEFAULT_KEYWORD, code);	
			
			addRule(identifierRule);											
		}
	}
}