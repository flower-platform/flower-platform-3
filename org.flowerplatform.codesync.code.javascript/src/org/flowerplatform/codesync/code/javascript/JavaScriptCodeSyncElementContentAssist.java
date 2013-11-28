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
package org.flowerplatform.codesync.code.javascript;

import static org.flowerplatform.codesync.code.javascript.config.JavaScriptDescriptors.TYPE_BACKBONE_CLASS;
import static org.flowerplatform.codesync.code.javascript.config.JavaScriptDescriptors.TYPE_FORM;
import static org.flowerplatform.codesync.code.javascript.config.JavaScriptDescriptors.TYPE_HTML_FILE;
import static org.flowerplatform.codesync.code.javascript.config.JavaScriptDescriptors.TYPE_JAVASCRIPT_FILE;
import static org.flowerplatform.codesync.code.javascript.config.JavaScriptDescriptors.TYPE_TABLE;

import java.util.Arrays;
import java.util.List;

import com.crispico.flower.mp.codesync.base.CodeSyncElementContentAssist;


/**
 * @author Mariana Gheorghe
 */
public class JavaScriptCodeSyncElementContentAssist extends CodeSyncElementContentAssist {

	private List<String> allowedTypes = Arrays.asList(
			TYPE_BACKBONE_CLASS,
			TYPE_JAVASCRIPT_FILE,
			TYPE_HTML_FILE,
			TYPE_TABLE,
			TYPE_FORM);
	
	@Override
	protected List<String> getAllowedTypes() {
		return allowedTypes;
	}

}
