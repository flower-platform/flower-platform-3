package com.crispico.flower.texteditor.css.partitioning {
	
	import com.crispico.flower.texteditor.css.CSSContentTypeConstants;
	import com.crispico.flower.texteditor.partitioning.scanners.ComposedPartitionScanner;
	import com.crispico.flower.texteditor.partitioning.scanners.PartitionScanner;
	import com.crispico.flower.texteditor.rules.BackslashEscapePatternRule;
	import com.crispico.flower.texteditor.rules.PatternRule;
	import com.crispico.flower.texteditor.rules.WordRule;
	
	import mx.collections.ArrayCollection;
	
	/**
	 * A ComposedPartitionScanner specific to CSS.
	 * 
	 * It uses internal partition scanners for the selectors area (with comment partitions 
	 * and a special partition for the start bracket) and for the declarations area (with 
	 * string and comment partitions, and a special partition that matches the end bracket).
	 * 
	 * The special partitions determine the use of each scanner. The declarations partition
	 * scanner is mapped to the start bracket partition, so it will be used after a start
	 * bracket partition is detected, and the selectors partition scanner is mapped to the
	 * end bracket partition, so it will be used after an end bracket partition is detected.
	 * 
	 * The internal scanner that will be used when this ComposedPartitionScanner will be used
	 * for the first time will be the selectors partition scanner.
	 */ 
	public class CSSPartitionScanner extends ComposedPartitionScanner {
		
		public function CSSPartitionScanner() {
			super();
			
			var declarationsPartitionScanner:PartitionScanner = new PartitionScanner(CSSContentTypeConstants.CSS_DECLARATIONS, this);
			declarationsPartitionScanner.addRule(new WordRule(CSSContentTypeConstants.CSS_DECLARATIONS_END_BRACKET, ["}"], false, false));
			declarationsPartitionScanner.addRule(new BackslashEscapePatternRule('"', '"', CSSContentTypeConstants.CSS_DOUBLE_QUOTES_STRING));
			declarationsPartitionScanner.addRule(new BackslashEscapePatternRule("'", "'", CSSContentTypeConstants.CSS_SINGLE_QUOTES_STRING));
			declarationsPartitionScanner.addRule(new PatternRule("//", "\n", CSSContentTypeConstants.CSS_SINGLE_LINE_COMMENT));
			declarationsPartitionScanner.addRule(new PatternRule("/*", "*/", CSSContentTypeConstants.CSS_MULTILINE_COMMENT, false));
				
			var selectorsPartitionScanner:PartitionScanner = new PartitionScanner(CSSContentTypeConstants.CSS_DEFAULT, this);
			selectorsPartitionScanner.addRule(new WordRule(CSSContentTypeConstants.CSS_DECLARATIONS_START_BRACKET, ["{"], false, false));
			selectorsPartitionScanner.addRule(new PatternRule("//", "\n", CSSContentTypeConstants.CSS_SINGLE_LINE_COMMENT));
			selectorsPartitionScanner.addRule(new PatternRule("/*", "*/", CSSContentTypeConstants.CSS_MULTILINE_COMMENT, false));
			
			addScanner(selectorsPartitionScanner, CSSContentTypeConstants.CSS_DECLARATIONS_END_BRACKET);
			addScanner(declarationsPartitionScanner, CSSContentTypeConstants.CSS_DECLARATIONS_START_BRACKET);
			currentScanner = selectorsPartitionScanner;			
		}
	}
}