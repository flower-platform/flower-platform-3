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
package org.flowerplatform.editor.model.java;

import static com.crispico.flower.mp.codesync.code.java.adapter.JavaTypeModelAdapter.ANNOTATION;
import static com.crispico.flower.mp.codesync.code.java.adapter.JavaTypeModelAdapter.CLASS;
import static com.crispico.flower.mp.codesync.code.java.adapter.JavaTypeModelAdapter.ENUM;
import static com.crispico.flower.mp.codesync.code.java.adapter.JavaTypeModelAdapter.INTERFACE;
import static org.flowerplatform.editor.model.java.JavaTypeIconsConstants.ANNOTATION_ICON;
import static org.flowerplatform.editor.model.java.JavaTypeIconsConstants.CLASS_ICON;
import static org.flowerplatform.editor.model.java.JavaTypeIconsConstants.ENUM_ICON;
import static org.flowerplatform.editor.model.java.JavaTypeIconsConstants.INTERFACE_ICON;

import java.util.Arrays;
import java.util.List;

import com.crispico.flower.mp.codesync.base.CodeSyncElementContentAssist;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana Gheorghe
 */
public class JavaCodeSyncElementContentAssist extends CodeSyncElementContentAssist {

	private List<String> allowedTypes = Arrays.asList(
			CLASS,
			INTERFACE,
			ENUM,
			ANNOTATION);
	
	@Override
	protected List<String> getAllowedTypes() {
		return allowedTypes;
	}

	protected String getIconUrl(CodeSyncElement element) {
		if (element.getType().equals(CLASS)) {
			return CLASS_ICON;
		}
		if (element.getType().equals(INTERFACE)) {
			return INTERFACE_ICON;
		}
		if (element.getType().equals(ENUM)) {
			return ENUM_ICON;
		}
		if (element.getType().equals(ANNOTATION)) {
			return ANNOTATION_ICON;
		}
		return null;
	}
	
}
