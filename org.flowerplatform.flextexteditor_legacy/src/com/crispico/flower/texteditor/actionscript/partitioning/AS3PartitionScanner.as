package com.crispico.flower.texteditor.actionscript.partitioning {
	
	import com.crispico.flower.texteditor.actionscript.AS3ContentTypeConstants;
	import com.crispico.flower.texteditor.partitioning.scanners.PartitionScanner;
	import com.crispico.flower.texteditor.rules.PatternRule;
	import com.crispico.flower.texteditor.rules.BackslashEscapePatternRule;
	import com.crispico.flower.texteditor.rules.WordRule;
	
	import mx.collections.ArrayCollection;
	
	/**
	 * A PartitionScanner specific for ActionScript. It recognizes comment partitions (single line and multiline and empty comments),
	 * string partitions and documentation partitions.
	 * 
	 * Note: the order is very important when adding the rules. For ActionScript, the correct order is empty comment, 
	 * documentation and then multiline comment, because the start delimiter for the multiline comment is a substring
	 * of the start delimiter for the javadoc partition, which is also a substring for the word that matches the empty 
	 * comment.
	 */ 	
	public class AS3PartitionScanner extends PartitionScanner {
		public function AS3PartitionScanner() {
			super(AS3ContentTypeConstants.AS3_DEFAULT);
			
			addRule(new PatternRule("//", "\n", AS3ContentTypeConstants.AS3_SINGLE_LINE_COMMENT));
			addRule(new BackslashEscapePatternRule('"', '"', AS3ContentTypeConstants.AS3_DOUBLE_QUOTES_STRING));
			addRule(new BackslashEscapePatternRule("'", "'", AS3ContentTypeConstants.AS3_SINGLE_QUOTES_STRING));
			addRule(new WordRule(AS3ContentTypeConstants.AS3_EMPTY_COMMENT, ["/**/"], false, false));
			addRule(new PatternRule("/**", "*/", AS3ContentTypeConstants.AS3_DOC, false));
			addRule(new PatternRule("/*", "*/", AS3ContentTypeConstants.AS3_MULTILINE_COMMENT, false));
		}
	}
}