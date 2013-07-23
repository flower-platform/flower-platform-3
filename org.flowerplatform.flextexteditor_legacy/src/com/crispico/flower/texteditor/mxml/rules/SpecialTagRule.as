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
package com.crispico.flower.texteditor.mxml.rules
{
	import com.crispico.flower.texteditor.model.Token;
	import com.crispico.flower.texteditor.rules.MultipleContentTypePatternRule;
	import com.crispico.flower.texteditor.rules.PatternRule;
	import com.crispico.flower.texteditor.scanners.ICharacterScanner;
	
	/**
	 * Special rule to separate a special tag from a special start tag that would trigger the use
	 * of a specific partitioner (i.e. <mx:Style source="test.css" /> vs. 
	 * <mx:Style> 
	 * 		// style declarations
	 * </mx:Style>).
	 */ 
	public class SpecialTagRule extends MultipleContentTypePatternRule {
		
		public function SpecialTagRule(rule:PatternRule) {
			super(rule);
		}
		
		override protected function getKey(scanner:ICharacterScanner, startOffset:int, endOffset:int):String {
			var char:int = scanner.unread();
			char = scanner.unread();
			var flag:Boolean = false;
			if (char == 47) // code for slash, this tag is not a start tag
				flag = true;
			scanner.read(); scanner.read();
			var key:String = super.getKey(scanner, startOffset, endOffset);
			if (flag) {
				if (key == "mx:Style" || key == "mx:Script")
					key += "_SPECIAL";
				if (key == "/mx:Script")
					key = null;
			}
			return key;
		}
	}
}