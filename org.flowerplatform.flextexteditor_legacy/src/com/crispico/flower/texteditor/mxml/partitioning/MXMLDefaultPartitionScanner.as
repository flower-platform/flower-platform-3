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
package com.crispico.flower.texteditor.mxml.partitioning {
	
	import com.crispico.flower.texteditor.mxml.MXMLContentTypeConstants;
	import com.crispico.flower.texteditor.mxml.rules.SpecialTagRule;
	import com.crispico.flower.texteditor.partitioning.scanners.PartitionScanner;
	import com.crispico.flower.texteditor.rules.MultipleContentTypePatternRule;
	import com.crispico.flower.texteditor.rules.PatternRule;
	import com.crispico.flower.texteditor.rules.WordRule;
	
	import mx.collections.ArrayCollection;
	
	/**
	 * A PartitionScanner specific for MXML that will be used as a
	 * internal partition scanner for the MXMLPartitionScanner. 
	 * 
	 * It recognizes comment partitions, tags (start and end tags)
	 * and special tags (script and style tags) used to switch the 
	 * current internal partition scanner of its parent.
	 */ 
	public class MXMLDefaultPartitionScanner extends PartitionScanner {
		
		public function MXMLDefaultPartitionScanner() {
			super(MXMLContentTypeConstants.MXML_DEFAULT);
			
			addRule(new WordRule(MXMLContentTypeConstants.MXML_CDATA_START_TAG, ["<![CDATA["], false, false));
			addRule(new PatternRule("<!--", "-->",MXMLContentTypeConstants.MXML_COMMENT_TAG, false));
			
			var tagRule:PatternRule = new PatternRule("<", ">", MXMLContentTypeConstants.MXML_TAG, false);
			var specialTagRule:SpecialTagRule = new SpecialTagRule(tagRule);
			specialTagRule.addType("mx:Script", MXMLContentTypeConstants.MXML_SCRIPT_START_TAG);
			specialTagRule.addType("/mx:Script", MXMLContentTypeConstants.MXML_SCRIPT_END_TAG);
			specialTagRule.addType("mx:Style", MXMLContentTypeConstants.MXML_STYLE_START_TAG);
			// special types to separate a simple special tag from a start tag that would trigger the use of a different partitioner
			specialTagRule.addType("mx:Script_SPECIAL", MXMLContentTypeConstants.MXML_SCRIPT_TAG);
			specialTagRule.addType("mx:Style_SPECIAL", MXMLContentTypeConstants.MXML_STYLE_TAG);
			addRule(specialTagRule);
		}	
	}
}