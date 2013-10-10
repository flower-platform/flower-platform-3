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
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.flowerplatform.editor.model.ContentAssistItem;

import com.crispico.flower.mp.codesync.base.CodeSyncElementContentAssist;
import com.crispico.flower.mp.codesync.code.CodeSyncCodePlugin;
import com.crispico.flower.mp.codesync.code.adapter.FolderModelAdapter;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;

/**
 * @author Mariana Gheorghe
 */
public class JavaCodeSyncElementContentAssist extends CodeSyncElementContentAssist {

	@Override
	public List<ContentAssistItem> findMatches(Map<String, Object> context, String pattern) {
		// TODO Auto-generated method stub
		return null;
	}

//	private List<String> allowedTypes = Arrays.asList(
//			CLASS,
//			INTERFACE,
//			ENUM,
//			ANNOTATION);
//	
//	@Override
//	protected List<String> getAllowedTypes() {
//		return allowedTypes;
//	}
//
//	@Override
//	protected Resource getCodeSyncElementsResource(IProject project, ResourceSet resourceSet) {
//		return CodeSyncCodePlugin.getInstance().getCodeSyncMapping(project, resourceSet);
//	}
//
//	@Override
//	protected ContentAssistItem createContentAssistItem(CodeSyncElement element) {
//		String simpleName = element.getName();
//		StringBuilder pckBuilder = new StringBuilder();
//		CodeSyncElement parent = element;
//		while (parent != null) {
//			if (parent.getType().equals(FolderModelAdapter.FOLDER) && !(parent instanceof CodeSyncRoot)) {
//				pckBuilder.insert(0, "." + parent.getName());
//			}
//			parent = (CodeSyncElement) parent.eContainer();
//		}
//		if (pckBuilder.length() > 0) {
//			pckBuilder.deleteCharAt(0);
//		}
//
//		String pck = pckBuilder.toString();
//		String fullyQualifiedName = pck + (pck.length() > 0 ? "." : "") + simpleName;
//		
//		return new ContentAssistItem(fullyQualifiedName, simpleName, pck, getIconUrl(element));
//	}
//
//	protected String getIconUrl(CodeSyncElement element) {
//		if (element.getType().equals(CLASS)) {
//			return CLASS_ICON;
//		}
//		if (element.getType().equals(INTERFACE)) {
//			return INTERFACE_ICON;
//		}
//		if (element.getType().equals(ENUM)) {
//			return ENUM_ICON;
//		}
//		if (element.getType().equals(ANNOTATION)) {
//			return ANNOTATION_ICON;
//		}
//		return null;
//	}
	
}
