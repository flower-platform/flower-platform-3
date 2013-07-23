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
package com.crispico.flower.texteditor.java.tokenizing {
	
	import com.crispico.flower.texteditor.java.JavaContentTypeConstants;
	import com.crispico.flower.texteditor.rules.PatternRule;
	import com.crispico.flower.texteditor.scanners.RuleBasedScanner;

	public class JavaDocScanner extends RuleBasedScanner {
		
		public function JavaDocScanner() {
			super(JavaContentTypeConstants.JAVA_DOC_DEFAULT);
			
			addRule(new PatternRule("{@link", "}", JavaContentTypeConstants.JAVA_DOC_LINK, false));
			addRule(new PatternRule("@", " ", JavaContentTypeConstants.JAVA_DOC_TAG));
			addRule(new PatternRule("<", ">", JavaContentTypeConstants.JAVA_DOC_HTML_TAG));
		}
	}
}