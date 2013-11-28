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
import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;
import org.flowerplatform.editor.model.ContentAssistItem;
import org.flowerplatform.editor.model.IContentAssist;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;

/**
 * @author Mariana Gheorghe
 */
public abstract class CodeSyncElementContentAssist implements IContentAssist {

	@Override
	public List<ContentAssistItem> findMatches(Map<String, Object> context, String pattern) {
		File file = (File) context.get(RESOURCE);
		Object project = CodeSyncPlugin.getInstance().getProjectsProvider().getContainingProjectForFile(file);
		if (project == null) {
			throw new RuntimeException(CodeSyncPlugin.getInstance().getMessage("contentAssist.resourceIsNotInProject"));
		}
		ResourceSet resourceSet = CodeSyncPlugin.getInstance().getOrCreateResourceSet(file, "diagramEditorStatefulService");
		Resource codeSyncMapping = getCodeSyncElementsResource(project, resourceSet); 
		List<ContentAssistItem> matches = new ArrayList<ContentAssistItem>();
		for (EObject object : codeSyncMapping.getContents()) {
			if (object instanceof CodeSyncElement) {
				iterateContents((CodeSyncElement) object, pattern, matches);
			}
		}
		return matches;
	}
	
	protected void iterateContents(CodeSyncElement element, String pattern, List<ContentAssistItem> matches) {
		if (getAllowedTypes().contains(element.getType())) {
			CodeSyncElementDescriptor descriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(element.getType());
			String name = (String) CodeSyncOperationsService.getInstance().getFeatureValue(element, descriptor.getKeyFeature());
			if (name.toLowerCase().startsWith(pattern.toLowerCase())) {
				matches.add(createContentAssistItem(element));
			}
		}
		for (CodeSyncElement child : element.getChildren()) {
			iterateContents(child, pattern, matches);
		}
	}

	abstract protected List<String> getAllowedTypes();

	protected Resource getCodeSyncElementsResource(Object project, ResourceSet resourceSet) {
		return CodeSyncPlugin.getInstance().getCodeSyncMapping((File) project, resourceSet);
	}
	
	/**
	 * Gets the fully qualified name for the {@code element} by going up its parents hierarchy.
	 */
	protected ContentAssistItem createContentAssistItem(CodeSyncElement element) {
		CodeSyncElementDescriptor descriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(element.getType());
		String simpleName = (String) CodeSyncOperationsService.getInstance()
				.getFeatureValue(element, descriptor.getKeyFeature());
		StringBuilder pckBuilder = new StringBuilder();
		CodeSyncElement parent = element;
		while (parent != null) {
			if (parent.getType().equals(CodeSyncPlugin.FOLDER) && !(parent instanceof CodeSyncRoot)) {
				descriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(parent.getType());
				pckBuilder.insert(0, "." + 
							CodeSyncOperationsService.getInstance().getFeatureValue(parent, descriptor.getKeyFeature()));
			}
			parent = (CodeSyncElement) parent.eContainer();
		}
		if (pckBuilder.length() > 0) {
			pckBuilder.deleteCharAt(0);
		}

		String pck = pckBuilder.toString();
		String fullyQualifiedName = pck + (pck.length() > 0 ? "." : "") + simpleName;
		
		return new ContentAssistItem(fullyQualifiedName, simpleName, pck, getIconUrl(element));
	}

	protected String getIconUrl(CodeSyncElement element) {
		CodeSyncElementDescriptor descriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(element.getType());
		String image = descriptor.getIconUrl();
		if (image == null) {
			return null;
		}
		String codeSyncPackage = CodeSyncPlugin.getInstance()
				.getBundleContext().getBundle().getSymbolicName();
		if (!image.startsWith("/")) {
			image = "/" + image;
		}
		return codeSyncPackage + image;
	}
}
