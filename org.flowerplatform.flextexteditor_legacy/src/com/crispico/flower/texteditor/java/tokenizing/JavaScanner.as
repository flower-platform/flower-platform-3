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