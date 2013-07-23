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
	 * This rule is used to match eiter specific words, or a string delimited by start and end delimiters, 
	 * but does NOT include them (unlike the pattern rule), or a combination of both.
	 * 
	 * The start and end delimiters cannot be strings; they are either specific characters or general delimiters 
	 * (any character that is not a letter, digit or underscore).
	 * 
	 * How to use a WordRule:
	 *  - for specific words delimited by general delimiters, such as keywords (eg. public, package), 
	 *    provide a list of these words (eg. new WordRule(type, wordsArray))
	 *  - for specific words delimited by specific characters (eg. [Event), provide a list of these words and lists
	 * 	  of the delimiters (eg. new WordRule(type, wordsArray, false), and then set the start delimiters list)
	 *  - for words that are delimited by specific characters (eg. tag names), provide lists of the delimiters only, 
	 *    leaving the words list null (eg. new WordRule(type), and then set the lists of delimiters)
	 * 
	 * @flowerModelElementId _MtLZkN3QEeCGOND4c9bKyA
	 */
	public class WordRule implements IRule {
		
		/**
		 * A list of words that this rule matches.
		 */ 
		private var _words:ArrayCollection;
		
		/**
		 * The default type of the string this rule matches.
		 */ 
		private var defaultType:String;
		
		/**
		 * The character scanner that provides the characters.
		 */ 
		private var scanner:ICharacterScanner;
		
		/**
		 * A list of start delimiters. A delimiter can only be a character, not a string.
		 * 
		 * If this list is null, then the delimiters will be considered any character that is not a word part
		 * (letter, digit or underscore). If this list is empty, then there are no delimiters.
		 */ 
		private var _startDelimiters:ArrayCollection;
		
		/**
		 * A list of end delimiters. A delimiter can only be a character, not a string.
		 * 
		 * If this list is null, then the delimiters will be considered any character that is not a word part
		 * (letter, digit or underscore). If this list is empty, then there are no delimiters.
		 */ 
		private var _endDelimiters:ArrayCollection;
		
		/**
		 * A flag that is true if the start delimiter is considered found, and false otherwise.
		 */ 
		private var skipStartDelimiter:Boolean = false;
		
		public function WordRule(defaultType:String = null, words:Array = null, useDefaultStartDelimiters:Boolean = true, useDefaultEndDelimiters:Boolean = true) {
			if (words != null)
				_words = new ArrayCollection(words);
			this.defaultType = defaultType;
			if (!useDefaultStartDelimiters)
				_startDelimiters = new ArrayCollection();
			if (!useDefaultEndDelimiters)
				_endDelimiters = new ArrayCollection();
		}
		
		public function getTypes():ArrayCollection {
			return new ArrayCollection([defaultType]);
		}
		
		/**
		 * Returns a token with a specific type if the rule matches.
		 * 
		 * First checks if the previous character was a start delimiter. If so (or there is no need for start delimiters),
		 * either scan for words in the words list, or if there are no words, scan until an end delimiter is found.
		 */ 
		public function evaluate(scanner:ICharacterScanner):Token {
			this.scanner = scanner;
			var offset:int = scanner.getOffset();
			var char:int = scanner.unread();
			scanner.setOffset(offset);
			// check if there is a start delimiter at the start of the region to be scanned, or if the start delimiter should be skipped
			if (isDelimiter(char, _startDelimiters) || skipStartDelimiter) 
				// if there are no words (this is a rule that recognizes strings delimited by specific start and end delimiters,
				// search for an end delimiter
				if (_words == null) { 
					while(true) {
						var t:Token = checkEndDelimiter(offset);
						if (t != null)
							return t;
					}
				} else {
					// for each word, if it starts after the start delimiter, return the default token
					for (var i:int=0; i<_words.length; i++) {
						scanner.setOffset(offset);
						var word:String = _words.getItemAt(i) as String;
						var b:Boolean = readSequence(word);
						if (b) {
							var t:Token = checkEndDelimiter(offset);
							if (t != null && !t.isUndefined())
								return t;
						}
					}
				}
			return Token.UNDEFINED;
		}
		
		/**
		 * Returns a default token if it finds the end delimiter, otherwise the UNDEFINED token.
		 */ 		
		private function checkEndDelimiter(offset:int):Token {
			var char:int = scanner.read();
			if (isDelimiter(char, _endDelimiters)) {
				if (char!=Util.EOF && char!=Util.EOLN) {
					scanner.unread();
				}
				if (offset!=scanner.getOffset()) {
					return token;
				} else {
					return Token.UNDEFINED;
				}
			}
			return null;
		}
		
		/** 
		 * Sets the skipStartDelimiter to true.
		 */ 
		public function setOptimizedMode():void {
			skipStartDelimiter = true;
		}
		
		public function resetOptimizedMode():void {
			skipStartDelimiter = false;
		}
		
		public function getStartDelimiterLength():int {
			return -1;
		}
		public function set startDelimiters(value:ArrayCollection):void {
			_startDelimiters = value;
		}
		public function getEndDelimiterLength():int {
			return -1;
		}
		public function set endDelimiters(value:ArrayCollection):void {
			_endDelimiters = value;
		}
		public function set words(value:ArrayCollection):void {
			_words = value;
		}
		public function get words():ArrayCollection {
			return _words;
		}
		
		private function get token():Token {
			return new Token(defaultType);
		}
		
		/**
		 * Returns true if the character is a delimiter, otherwise returns false.
		 * 
		 * If the delimiters list is null, checks if the character is a word part (defined in Utils).
		 */ 
		private function isDelimiter(char:int, delimiters:ArrayCollection):Boolean {
			// EOF is always considered a delimiter
			if (char == Util.EOF)
				return true;
			// if the list is null, then any non-word part (not letter, digit or underscore) are considered delimiters
			if (delimiters == null) 
				return !Util.isWordPart(char);
			// if the list is empty, then there are no delimiters				
			if (delimiters.length == 0)
				return true;
				
			for each (var delimiter:int in delimiters)
				if (char == delimiter)
					return true;
			return false;
		}
		
		/**
		 * Reads a substring character by character, and stops at the first character that does not match the word.
		 * Returns true if the word is found, and false otherwise.
		 */ 
		private function readSequence(word:String):Boolean {
			if (word == null)
				return true;
			var n:int = word.length;
			for (var i:int=0; i<n; i++)
				if (scanner.read() != word.charCodeAt(i)) 
					break;
			return i==n;
		}
	}
}