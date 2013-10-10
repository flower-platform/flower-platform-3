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
package com.crispico.flower.mp.codesync.base;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.flowerplatform.editor.model.ContentAssistItem;
import org.flowerplatform.editor.model.IContentAssist;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * @author Mariana Gheorghe
 */
public abstract class CodeSyncElementContentAssist implements IContentAssist {

//	@Override
//	public List<ContentAssistItem> findMatches(Map<String, Object> context, String pattern) {
//		File file = (File) context.get(RESOURCE);
//		IResource resource = ProjectsService.getInstance().getProjectWrapperResourceFromFile(file);
//		if (resource == null) {
//			throw new RuntimeException(CodeSyncPlugin.getInstance().getMessage("contentAssist.resourceIsNotInProject"));
//		}
//		ResourceSet resourceSet = CodeSyncPlugin.getInstance().getOrCreateResourceSet(file, "diagramEditorStatefulService");
//		Resource codeSyncMapping = getCodeSyncElementsResource(resource.getProject(), resourceSet); 
//		List<ContentAssistItem> matches = new ArrayList<ContentAssistItem>();
//		for (EObject object : codeSyncMapping.getContents()) {
//			if (object instanceof CodeSyncElement) {
//				iterateContents((CodeSyncElement) object, pattern, matches);
//			}
//		}
//		return matches;
//	}
//	
//	protected void iterateContents(CodeSyncElement element, String pattern, List<ContentAssistItem> matches) {
//		if (element.isAdded() && getAllowedTypes().contains(element.getType())) {
//			String name = (String) CodeSyncPlugin.getInstance().getFeatureValue(element, 
//					CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name());
//			if (name.toLowerCase().startsWith(pattern.toLowerCase())) {
//				matches.add(createContentAssistItem(element));
//			}
//		}
//		for (CodeSyncElement child : element.getChildren()) {
//			iterateContents(child, pattern, matches);
//		}
//	}
//
//	abstract protected List<String> getAllowedTypes();
//	abstract protected Resource getCodeSyncElementsResource(IProject project, ResourceSet resourceSet);
//	abstract protected ContentAssistItem createContentAssistItem(CodeSyncElement element);
}
