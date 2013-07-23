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
	
	import com.crispico.flower.texteditor.rules.PatternRule;

	/**
	 * A PatternRule that imposes a restriction on the characters in the string it matches: the backslash character
	 * is used as an escape character. Eg. the \" sequence is used to replace a " inside a string, so the rule must
	 * not match at \".
	 * 
	 * To use this rule, provide the start and end delimiters, and the token type to return when the rule matches. 
	 * Also, a special flag may be set to force the rule to match at the end of the line (this flag is true by default). 
	 */ 
	public class BackslashEscapePatternRule extends PatternRule {
		
		/**
		 * The code of the backslash character.
		 */ 
		private const backslashCode:int = 92;
		
		/**
		 * The code of the previous character.
		 */ 
		private var prev:int = -1;
		
		/**
		 * Initializes the rule.
		 */ 
		public function BackslashEscapePatternRule(startSequence:String, endSequence:String, defaultType:String, matchOnEoLn:Boolean = true) {
			super(startSequence, endSequence, defaultType, matchOnEoLn);
		}
		
		/**
		 * Checks if the previous character was a backslash. If it was, sets the allowEndCharacter to false to prevent 
		 * the rule from matching the string if the next character is the end delimiter.
		 */ 
		override protected function checkChar(char:int):void {
			allowEndDelimiter = (prev != backslashCode && prev != -1);
			prev = char;
		}
	}
}