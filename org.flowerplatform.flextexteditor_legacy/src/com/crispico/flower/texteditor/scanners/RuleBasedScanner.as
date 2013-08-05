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
package com.crispico.flower.texteditor.scanners
{
	import com.crispico.flower.texteditor.Document;
	import com.crispico.flower.texteditor.model.Token;
	import com.crispico.flower.texteditor.rules.IRule;
	import com.crispico.flower.texteditor.utils.Util;
	
	import mx.collections.ArrayCollection;
	
	/**
	 * Uses a set of rules to scan the content of a document to detect tokens.
	 * 
	 * To create a RuleBasedScanner, set the default token type it will return
	 * if no specific token was detected. Then use the addRule method to add
	 * each rule this scanner will use.
	 * 
	 * 
	 */
	public class RuleBasedScanner extends CharacterScanner implements ITokenScanner {
		
		/**
		 * A list of rules used when scanning.
		 * 
		 * 
		 */
		protected var rules:ArrayCollection;
		
		/**
		 * The default token to be returned when no specific token is detected.
		 */ 
		protected var defaultToken:Token;
		
		/**
		 * The index of the last token returned.
		 */ 
		private var lastTokenIndex:int;
		
		/**
		 * The last token detected.
		 */ 
		private var lastToken:Token;
		
		/** 
		 * Initializes the list of rules and the default token.
		 */ 
		public function RuleBasedScanner(defaultType:String) {
			rules = new ArrayCollection();
			defaultToken = new Token(defaultType);
		}
		
		override public function setRange(document:Document, startOffset:int, length:int):void {
			super.setRange(document, startOffset, length);
			lastTokenIndex = offset;
			lastToken = null;
		}
	
		public function addRule(rule:IRule):void {
			rules.addItem(rule);
		}
		
		/**
		 * Gets a subset of this scanner's rules to be used when searching for the next token.
		 */ 
		protected function getProposedRules():ArrayCollection {
			return rules;
		}
		
		/**
		 * Detects the next token using a subset of rules.
		 * Caches the detected token to be returned later if it does not start at the end of the last returned token
		 * and returns a default token instead; this way any gap is covered.
		 * 
		 * 
		 */
		public function nextToken():Token {
			var resultToken:Token = null;
			// if there was a cached token from a previous call, return it
			if (lastToken!=null) {
				resultToken = lastToken;
				lastToken = null;
			} else {
				var proposedRules:ArrayCollection = getProposedRules();
				var tokenOffset:int = offset;
				// if there are no rules, we should return a token of type EOF
				if (proposedRules.length == 0) {
					lastToken = Token.EOF;
					lastToken.offset = end;
					lastToken.length = 0;
					offset = end;	
				}	
				while (lastToken == null) {
					offset = tokenOffset;
					var char:int = read();
					// if the read character is the EOF, we should return a EOF token
					if (char == Util.EOF) {
						lastToken = Token.EOF;
						lastToken.offset = offset;
						lastToken.length = 0;
						break;
					}
					// evaluate each rule
					for each (var rule:IRule in proposedRules) {
						offset = tokenOffset;
						var token:Token = rule.evaluate(this);
						if (token.type == defaultToken.type) {
							tokenOffset = offset-1;
							break;
						}
						// keep the first token that is not undefined
						if (!token.isUndefined()) {
							lastToken = token;
							lastToken.offset = tokenOffset;
							lastToken.length = offset - tokenOffset;
							break;	
						}
					}
					tokenOffset++;	
				}
				// if the new token doesn't start at the end of the previous token, create a default token to cover the gap and cache the new token
				if (lastTokenIndex < lastToken.offset) {
					defaultToken.offset = lastTokenIndex;
					defaultToken.length = lastToken.offset - lastTokenIndex;
					resultToken = defaultToken;
				} else {
					resultToken = lastToken;
					lastToken = null;
				}					
			}
			lastTokenIndex = resultToken.offset + resultToken.length;
			return resultToken;
		}
	}
}