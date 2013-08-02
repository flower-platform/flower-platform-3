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
	 * A rule that is able to return tokens with different types, depending on a specific key. The rule uses an internal
	 * PatternRule that must be provided when creating the rule. Specific types are mapped to keys; to add a new type, 
	 * use the addType method.
	 * 
	 * By default, it returns the token type mapped to the key it detects, or the type returned by the internal rule; 
	 * however if the keyOnly flag is set to true and there is no type mapped to the key, this rule returns an UNDEFINED 
	 * token even if the internal rule matches.
	 * 
	 * Extending classes may implement the getKey() function. The default key is the first word (delimited by whitspace
	 * characters) after the start delimiter (eg. tag names: key for the <mx:Label text="text" /> is mx:Label).
	 * 
	 * 
	 */
	public class MultipleContentTypePatternRule implements IRule {
		
		/**
		 * The pattern rule that is evaluated.
		 */ 
		private var patternRule:PatternRule;
		
		/**
		 * A map of types this rule matches.
		 */ 
		private var types:Dictionary;
		
		/**
		 * A list of types this rule matches.
		 */ 
		private var typesList:ArrayCollection;
		
		/**
		 * If true, this rule returns an UNDEFINED token even if the internal rule matches, i.e. it only returns types
		 * mapped to the keys, or UNDEFINED if there is no type mapped to the key.
		 */ 
		private var keyOnly:Boolean;
			
		/**
		 * Sets the internal pattern rule. Initializes the map and list of types.
		 */ 	
		public function MultipleContentTypePatternRule(rule:PatternRule) {
			this.patternRule = rule;
			types = new Dictionary();
			typesList = new ArrayCollection([rule.getTypes().getItemAt(0)]);
			keyOnly = false;
		}
		
		public function getTypes():ArrayCollection {
			return typesList;
		}
		
		/**
		 * Evaluates the internal pattern rule at the scanner's current position. If the found token is not UNDEFINED,
		 * it computes a key, and if there is any type mapped to this key, it creates and returns a token with this type.
		 */ 
		public function evaluate(scanner:ICharacterScanner):Token {
			var startOffset:int = scanner.getOffset();
			var end:int = scanner.getEnd();
			
			// get the detected token from the internal rule
			// if the token is UNDEFINED, return it
			var foundToken:Token = Token(patternRule.evaluate(scanner));
			if (foundToken.isUndefined())
				return foundToken;
			var endOffset:int = scanner.getOffset();
			// get the key that maps the type of the token to be returned
			var key:String = getKey(scanner, startOffset + patternRule.getStartDelimiterLength(), endOffset - patternRule.getEndDelimiterLength());
			scanner.setOffset(endOffset);
			scanner.setEnd(end);
			
			// if there is a type mapped to the found key, return a token of that type
			// we check if the keyOnly flag is true to avoid returning the default token of the internal rule
			if (keyOnly || types[key] != null) 
				foundToken.type = types[key];
			// if there is no type mapped to the found key, but the keyOnly flag is true, return an UNDEFINED token
			if (keyOnly && types[key] == null) 
				foundToken = Token.UNDEFINED;
			
			// if the keyOnly flag is set to false and there is no type mapped to the key, return the default token from the internal rule
			return foundToken;
		}
		
		/**
		 * Sets the optimized mode on the internal pattern rule.
		 */ 
		public function setOptimizedMode():void {
			patternRule.setOptimizedMode();
		}
		
		/**
		 * Resets the internal pattern rule.
		 */ 
		public function resetOptimizedMode():void {
			patternRule.resetOptimizedMode();
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
		 * Add a new type mapped to a key.
		 */ 
		public function addType(key:String, type:String):void {
			types[key] = type;
			typesList.addItem(type);
		}
		
		public function setKeyOnly(b:Boolean):void {
			keyOnly = b;
		}
		
		/**
		 * Uses the characters returned by the scanner to create a key that identifies this type of token.
		 * This base implementation returns the first word found at the current position of the scanner.
		 */ 
		protected function getKey(scanner:ICharacterScanner, startOffset:int, endOffset:int):String {
			scanner.setOffset(startOffset);
			scanner.setEnd(endOffset);
			var key:String = "";
			var char:int = scanner.read();
			while (!Util.isWhitespace(char) && char != 62) {
				key += String.fromCharCode(char);
				char = scanner.read();
			}
			return key;
		}
	}
}