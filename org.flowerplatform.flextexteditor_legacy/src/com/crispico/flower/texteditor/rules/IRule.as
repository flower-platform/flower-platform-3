package com.crispico.flower.texteditor.rules {
	
	import com.crispico.flower.texteditor.scanners.ICharacterScanner;
	import com.crispico.flower.texteditor.model.Token;
	import mx.collections.ArrayCollection;
	
	/**
	 * A rule matches a range of text that starts with a specific start delimiter and ends at a specific end delimiter.
	 * 
	 * Each rule has specific flags that influence the way this rule is evaluated. To set/reset these flags, use the
	 * setOptimizedMode and resetOptimizedMode functions.
	 * 
	 * @flowerModelElementId _OmbEYL8qEeCCBN1yQIvzhA
	 */
	public interface IRule {
		
		/**
		 * The type of token this rule matches.
		 */ 
		function getTypes():ArrayCollection;
		
		/**
		 * Evaluates the characters scanned by the character scanner and returns a token with a specific type if the
		 * rule matches at this scanner's position, an EOF token if the scanner reaches the end of the file (or range
		 * to scan), or an UNDEFINED token otherwise.
		 */ 
		function evaluate(scanner:ICharacterScanner):Token;
		  
		/**
		 * Sets specific flags to run in an optimized mode (eg. skipStartDelimiter and stopOnEof on a PatternRule).
		 * 
		 * @flowerModelElementId _Mq0CEN3QEeCGOND4c9bKyA
		 */
		function setOptimizedMode():void;
		
		/** 
		 * Resets specific flags to their default values.
		 */ 
		function resetOptimizedMode():void;
		
		/**
		 * Gets the start delimiter's length.
		 */   
		function getStartDelimiterLength():int;
		
		/**
		 * Gets the end delimiter's length.
		 */ 
		function getEndDelimiterLength():int;
	}
}