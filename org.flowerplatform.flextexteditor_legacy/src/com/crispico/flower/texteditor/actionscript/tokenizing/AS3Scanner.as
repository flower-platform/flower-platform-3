/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
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