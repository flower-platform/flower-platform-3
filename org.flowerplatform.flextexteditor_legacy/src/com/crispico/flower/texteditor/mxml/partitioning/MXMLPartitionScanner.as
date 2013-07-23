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
	
	import com.crispico.flower.texteditor.actionscript.partitioning.AS3PartitionScanner;
	import com.crispico.flower.texteditor.css.partitioning.CSSPartitionScanner;
	import com.crispico.flower.texteditor.mxml.MXMLContentTypeConstants;
	import com.crispico.flower.texteditor.partitioning.scanners.ComposedPartitionScanner;
	import com.crispico.flower.texteditor.partitioning.scanners.PartitionScanner;
	import com.crispico.flower.texteditor.rules.MultipleContentTypePatternRule;
	import com.crispico.flower.texteditor.rules.PatternRule;
	import com.crispico.flower.texteditor.rules.WordRule;
	
	import mx.collections.ArrayCollection;
	
	/**
	 * A ComposedPartitionScanner for MXML.
	 * 
	 * It uses internal partiton scanners for the script area (this is the basic ActionScript 
	 * partition scanner with an added rule for the CDATA end tag), the style area (this is
	 * the basic CSS partition scanner with an added rule for the style end tag </mx:Style>)
	 * and an XML partition scanner (with tags, special tags and comment partitions and a 
	 * special partition for the CDATA start tag).
	 * 
	 * The script partition scanner is mapped to the special CDATA start tag partition, so it 
	 * will be used after a CDATA start tag is detected. The style partition scanner is mapped
	 * to the style start tag <mx:Style>, so it will be used after a style start tag is detected.
	 * The default XML partition scanner is mapped to the CDATA end tag and the style end tag.
	 * 
	 * The internal scanner that will be used when this ComposedPartitionScanner will be used
	 * for the first time will be the default XML partition scanner.
	 */ 
	public class MXMLPartitionScanner extends ComposedPartitionScanner {
		
		public function MXMLPartitionScanner() {
			super();
			
			var scriptScanner:PartitionScanner = new AS3PartitionScanner();
			scriptScanner.setParentScanner(this);
			scriptScanner.addRule(new WordRule(MXMLContentTypeConstants.MXML_CDATA_END_TAG, ["]]>"], false, false));
			addScanner(scriptScanner, MXMLContentTypeConstants.MXML_CDATA_START_TAG);
	
			var styleScanner:CSSPartitionScanner = new CSSPartitionScanner();
			styleScanner.setParentScanner(this);
			var endStyleRule:MultipleContentTypePatternRule = new MultipleContentTypePatternRule(new PatternRule("</", ">", MXMLContentTypeConstants.MXML_TAG));
			endStyleRule.addType("mx:Style", MXMLContentTypeConstants.MXML_STYLE_END_TAG);
			endStyleRule.setKeyOnly(true);
			styleScanner.addRule(endStyleRule);
			addScanner(styleScanner, MXMLContentTypeConstants.MXML_STYLE_START_TAG);
	
			var defaultScanner:PartitionScanner = new MXMLDefaultPartitionScanner();
			defaultScanner.setParentScanner(this);
			addScanner(defaultScanner, MXMLContentTypeConstants.MXML_CDATA_END_TAG);
			addScanner(defaultScanner, MXMLContentTypeConstants.MXML_STYLE_END_TAG);
			
			currentScanner = defaultScanner;
		}
	}
}