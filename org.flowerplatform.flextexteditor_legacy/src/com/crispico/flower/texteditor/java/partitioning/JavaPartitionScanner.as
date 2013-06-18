package com.crispico.flower.texteditor.java.partitioning {
	
	import com.crispico.flower.texteditor.java.JavaContentTypeConstants;
	import com.crispico.flower.texteditor.partitioning.scanners.PartitionScanner;
	import com.crispico.flower.texteditor.rules.PatternRule;
	import com.crispico.flower.texteditor.rules.BackslashEscapePatternRule;
	import com.crispico.flower.texteditor.rules.WordRule;
	
	import mx.collections.ArrayCollection;

	/**
	 * A PartitionScanner specific for Java. It recognizes comment partitions (single line and multiline and empty comments),
	 * string partitions and documentation partitions.
	 * 
	 * Note: the order is very important when adding the rules. For Java, the correct order is empty comment, documentation and then
	 * multiline comment, because the start delimiter for the multiline comment is a substring of the start delimiter for the
	 * javadoc partition, which is also a substring for the word that matches the empty comment.
	 */ 
	public class JavaPartitionScanner extends PartitionScanner {
		
		public function JavaPartitionScanner() {
			super(JavaContentTypeConstants.JAVA_DEFAULT);
			
			addRule(new PatternRule("//", "\n", JavaContentTypeConstants.JAVA_SINGLE_LINE_COMMENT));
			addRule(new BackslashEscapePatternRule('"', '"', JavaContentTypeConstants.JAVA_DOUBLE_QUOTES_STRING));
			addRule(new BackslashEscapePatternRule("'", "'", JavaContentTypeConstants.JAVA_SINGLE_QUOTES_STRING));
			
			addRule(new WordRule(JavaContentTypeConstants.JAVA_EMPTY_COMMENT, ["/**/"], false, false));
			addRule(new PatternRule("/**", "*/", JavaContentTypeConstants.JAVA_DOC, false));
			addRule(new PatternRule("/*", "*/", JavaContentTypeConstants.JAVA_MULTILINE_COMMENT, false));
		}
	}
}