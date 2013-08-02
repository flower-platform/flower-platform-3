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
	import com.crispico.flower.texteditor.utils.Util;
	import com.crispico.flower.texteditor.model.Token;
	
	import mx.collections.ArrayCollection;

	/**
	 * A rule that matches a pattern: a string that starts with the specified start delimiter and ends with the 
	 * specified end delimiter.
	 * 
	 * To create a PatternRule, the constructor requires a start delimiter and an end delimiter, and the token
	 * type to return when the rule matches. Also, a special flag may be set to force the rule to match at the
	 * end of the line (this flag is true by default).
	 * 
	 * The rule may be evaluated in an optimized mode; by default, the optimized mode forces the rule to consider
	 * that its start delimiter was already found (useful when scanning in the middle of a partition) and to match
	 * when it reaches the end of the scanner's range.
	 * 
	 * 
	 */
	public class PatternRule implements IRule {
		
		/** 
		 * The substring this rule starts with.
		 */ 
		private var startDelimiter:String;
		
		/**
		 * The substring this rule ends with.
		 */ 
		private var endDelimiter:String;
		
		/**
		 * The default type of the string this rule matches.
		 */ 
		private var defaultType:String;
		
		/**
		 * A flag that is true if the rule matches when reaching the EOLN, and false otherwise.
		 * Default value is true.
		 */ 
		private var matchOnEoLn:Boolean;
		
		/**
		 * A flag that is true if the rule matches when reaching the EOF, and false otherwise.
		 */ 
		private var matchOnEof:Boolean = true;
		
		/**
		 * The character scanner that provides the characters.
		 */ 
		private var scanner:ICharacterScanner;
		
		/**
		 * A flag that is true if the start delimiter is considered found, and false otherwise.
		 */ 
		private var skipStartDelimiter:Boolean = false;
		
		/**
		 * A flag that is false if the rule ignores the end delimiter, and false otherwise.
		 */ 
		protected var allowEndDelimiter:Boolean = true;
		
		public function PatternRule(startSequence:String, endSequence:String,  defaultType:String, matchOnEoLn:Boolean = true) {
			this.startDelimiter = startSequence;
			this.endDelimiter = endSequence;
			this.matchOnEoLn = matchOnEoLn;
			this.defaultType = defaultType;
		}
		
		public function getTypes():ArrayCollection {
			return new ArrayCollection([defaultType]);
		}
		
		/**
		 * Returns a token with a specific type if the rule matches.
		 * 
		 * First checks if the start delimiter is found at the scanner's current position (in case the skipStartDelimiter
		 * flag is set to false). 
		 * 
		 * If the start delimiter is found, keeps scanning until it finds the end delimiter (in case the allowEndDelimiter
		 * flag is set to true).
		 * 
		 * Return a token with a specific type if the end delimiter is found (or the EOF, in case the matchOnEOF flag is
		 * set to true), or an UNDEFINED token if the rule does not match.
		 */ 
		public function evaluate(scanner:ICharacterScanner):Token {
			this.scanner = scanner;
			var foundStartDelimiter:int = scanner.getOffset();
			// check if the start delimiter is at the start of the region to scan, unless it should be skipped (considered found already)
			if (!skipStartDelimiter) {
				var b:Boolean = readSequence(startDelimiter);
				if (!b) {
					return Token.UNDEFINED;
				}	
			}
			
			// keep scanning until we reach the end delimiter or until the EOF 
			while (true) {
				var char:int = scanner.read();
				checkChar(char);
				// if we reach the EOF and the rule is set to match on EOF, 
				// return the default token, otherwise return the UNDEFINED token
				if (char == Util.EOF) 
					if (matchOnEof) {
						return token;
					} else {
						return Token.UNDEFINED;
					}
				// if we reach the EOLN and the rule is set to match on EOLN, 
				// return the default token, otherwise return the UNDEFINED token
				if (matchOnEoLn && char == Util.EOLN) {
					return token;
				}
					
				// remember the scanner's offset before scanning for the end delimiter
			 	var offset:int = scanner.getOffset(); 
				
				// if we reach the end delimiter and it is allowed, return the default token
				if (allowEndDelimiter)
					if (char == endDelimiter.charCodeAt(0))
						if (readSequence(endDelimiter, 1))
							return token;
				
				// restore the scanner's offset
				scanner.setOffset(offset); 
				
			}	
			return Token.UNDEFINED;
		}
		
		/**
		 * Sets the skipStartDelimiter flag to true. Depending on the range to scan (until the end of the document or not),
		 * also sets the stopOnEof flag.
		 */ 
		public function setOptimizedMode():void {
			skipStartDelimiter = true;
			matchOnEof = scanner.getEnd() == scanner.getDocument().endLimit;
		}
		
		public function resetOptimizedMode():void {
			skipStartDelimiter = false;
			matchOnEof = true;
		}
		
		
		public function getStartDelimiterLength():int {
			return startDelimiter.length;
		}
		
		public function getEndDelimiterLength():int {
			return endDelimiter.length;
		}
		
		/**
		 * Returns a token with the default type.
		 */ 
		private function get token():Token {
			return new Token(defaultType);
		}
		
		/**
		 * Reads a substring character by character, and stops at the first character that does not match the pattern.
		 * Returns true if the pattern is found, and false otherwise.
		 */ 
		private function readSequence(pattern:String, startIndex:int=0):Boolean {
			var n:int = pattern.length;
			for (var i:int=startIndex; i<n; i++)
				if (scanner.read() != pattern.charCodeAt(i)) 
					break;
			return i==n;
		}
		
		/**
		 * Check if the current character is valid. This base implementation of the pattern rule does not have 
		 * any restrictions on the characters in the string it matches.
		 */ 
		protected function checkChar(char:int):void {	}
	}
}