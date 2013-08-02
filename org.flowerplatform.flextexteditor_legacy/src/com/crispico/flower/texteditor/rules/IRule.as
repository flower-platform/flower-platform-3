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
	 * 
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
		 * 
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