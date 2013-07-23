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
package com.crispico.flower.texteditor.java.tokenizing {
	
	import com.crispico.flower.texteditor.java.JavaContentTypeConstants;
	import com.crispico.flower.texteditor.rules.MultipleContentTypeWordRule;
	import com.crispico.flower.texteditor.rules.PatternRule;
	import com.crispico.flower.texteditor.scanners.RuleBasedScanner;

	public class JavaScanner extends RuleBasedScanner {
		
		public function JavaScanner() {
			super(JavaContentTypeConstants.JAVA_DEFAULT_DEFAULT);
			
			var keywords:Array = ["abstract", "do", "if", "package", "synchronized", 
								"boolean", "double", "implements", "private", "this",
								"break", "else", "import", "protected", "throw",
								"byte", "extends", "instanceof", "public", "throws", 																
								"case", "false", "int", "return", "transient",
								"catch", "final", "interface", "short", "true",
								"char", "finally", "long", "static", "try",
								"class", "float", "native", "strictfp", "void",
								"const", "for", "new", "super", "volatile",
								"continue", "goto", "null", "switch", "while",
								"default", "assert"];
			 var keywordsRule:MultipleContentTypeWordRule = new MultipleContentTypeWordRule(JavaContentTypeConstants.JAVA_DEFAULT_DEFAULT);
			 keywordsRule.addWords(keywords, JavaContentTypeConstants.JAVA_DEFAULT_KEYWORD);
			 addRule(keywordsRule);
			 
		 	 addRule(new PatternRule("@", " ", JavaContentTypeConstants.JAVA_DEFAULT_ANNOTATION));
		}
	}
}