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
	
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;

	/**
	 * A rule that matches words and returns a token of the type mapped to a word, 
	 * or the default token if there is no type mapped to the word.
	 * 
	 * To use this rule, provide the token type that will be returned when there 
	 * is no type mapped to the word it finds in the text. To add words with specific
	 * types, use the addWords method, and provide the array of words and token type
	 * to return when one of the words is found in the text. Also, you may specify a
	 * start delimiter or end delimiter. Note: the delimiters may not be strings, only
	 * characters.
	 * 
	 * 
	 */
	public class MultipleContentTypeWordRule implements IRule {
		
		/**
		 * A list of types this rule recognizes.
		 */ 
		private var wordTypesList:ArrayCollection;
		
		/**
		 * A map of types this rule recognizes. Types are mapped to words.
		 */ 
		private var wordTypes:Dictionary;
		
		/**
		 * A map of start delimiters for each word.
		 */ 
		private var wordStartDelimiters:Dictionary;
		
		/**
		 * A map of end delimiters for each word.
		 */ 
		private var wordEndDelimiters:Dictionary;
		
		/**
		 * The default type of a word.
		 */ 
		private var defaultType:String;
		
		public function MultipleContentTypeWordRule(defaultType:String) {
			this.defaultType = defaultType;
			wordTypesList = new ArrayCollection([defaultType]);
			wordTypes = new Dictionary();
			wordStartDelimiters = new Dictionary();
			wordEndDelimiters = new Dictionary();
		}
		public function getTypes():ArrayCollection {
			return wordTypesList;
		}
		
		/**
		 * Returns a token with a specific type, or the default type if there is no type mapped to the word this 
		 * rule matches.
		 * 
		 * Detects words in the text (delimited by non-word part characters i.e. not a letter, digit or underscore).
		 * If the word is delimited by its specific start delimiters and end delimiters (if it requires any) and 
		 * there is a type mapped to this word, it returns a token with that type. Otherwise, it returns the default token.
		 */ 
		public function evaluate(scanner:ICharacterScanner):Token {
			var word:String = "";
			var startOffset:int = scanner.getOffset();
			var char:int = scanner.read();
			// create the word with the characters from the scanner
			while (Util.isWordPart(char)) {
				word += String.fromCharCode(char);
				char = scanner.read();
			}
			
			// if there is no word, returned the UNDEFINED token
			if (word == "")
				return Token.UNDEFINED;
			// get the type mapped to this word
			var type:String = wordTypes[word];
			
			// if the type is not valid, we should return the default token
			if (!isValidType(type))
				type = null;
			var token:Token;
			if (type == null) {
				token = new Token(defaultType);
			} else {
				if (char != -1)
					scanner.setOffset(scanner.getOffset()-1);
				token = new Token(type);
				// if there is any end delimiter mapped to this word, and it is not in the text, return a default token
				var endDelimiter:int = wordEndDelimiters[word];
				if (endDelimiter > 0) {
					var x:int = scanner.read();
					if (x != endDelimiter)
						token = new Token(defaultType);
				}
				
				// if there is any start delimiter mapped to this word, and it is not in the text, return a default token
				// note: make sure that the scanner's position is set back to where it was
				var startDelimiter:int = wordStartDelimiters[word];
				if (startDelimiter > 0) {
					var endOfffset:int = scanner.getOffset();
					scanner.setOffset(startOffset);
					var x:int = scanner.unread();
					scanner.setOffset(endOfffset);
					if (x != startDelimiter)
						token = new Token(defaultType);
				}
			}
			return token;
		}
		
		public function setOptimizedMode():void {
			// This implementation has no flags to set.
		}
		
		public function resetOptimizedMode():void {
			// This implementation has no flags to set.
		}
		
		/**
		 * This implementation does not use this flag.
		 */ 
		public function getStartDelimiterLength():int {
			return -1;
		}
		
		/**
		 * This implementation does not use this flag.
		 */ 
		public function getEndDelimiterLength():int {
			return -1;
		}
		
		/**
		 * Adds the list of words, and maps the type to each word.
		 */ 
		public function addWords(words:Array, type:String, startDelimiter:int=-1, endDelimiter:int=-1):void {
			wordTypesList.addItem(type); 
			for each (var word:String in words) {
				wordTypes[word] = type;
				wordStartDelimiters[word] = startDelimiter;
				wordEndDelimiters[word] = endDelimiter;
			}
		}
		
		/**
		 * Checks if the type is found in the types list.
		 */ 
		private function isValidType(type:String):Boolean {
			for each (var wordType:String in wordTypesList)
				if (type == wordType)
					return true;
			return false;
		}
	}
}