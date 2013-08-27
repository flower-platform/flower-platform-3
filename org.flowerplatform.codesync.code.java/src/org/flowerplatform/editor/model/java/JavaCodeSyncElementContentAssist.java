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

import java.util.Arrays;
import java.util.List;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.flowerplatform.editor.model.ContentAssistItem;
import com.crispico.flower.mp.codesync.base.CodeSyncElementContentAssist;
import com.crispico.flower.mp.codesync.code.CodeSyncCodePlugin;
import com.crispico.flower.mp.codesync.code.adapter.FolderModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaTypeModelAdapter;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;

/**
 * @author Mariana Gheorghe
 */
public class JavaCodeSyncElementContentAssist extends CodeSyncElementContentAssist {

	private List<String> allowedTypes = Arrays.asList(
			JavaTypeModelAdapter.CLASS,
			JavaTypeModelAdapter.INTERFACE,
			JavaTypeModelAdapter.ENUM,
			JavaTypeModelAdapter.ANNOTATION);
	
	@Override
	protected List<String> getAllowedTypes() {
		return allowedTypes;
	}

	@Override
	protected Resource getCodeSyncElementsResource(IProject project, ResourceSet resourceSet) {
		return CodeSyncCodePlugin.getInstance().getCodeSyncMapping(project, resourceSet);
	}


	@Override
	protected ContentAssistItem createContentAssistItem(CodeSyncElement element) {
		String simpleName = element.getName();
		StringBuilder pckBuilder = new StringBuilder();
		CodeSyncElement parent = element;
		while (parent != null) {
			if (parent.getType().equals(FolderModelAdapter.FOLDER) && !(parent instanceof CodeSyncRoot)) {
				pckBuilder.insert(0, "." + parent.getName());
			}
			parent = (CodeSyncElement) parent.eContainer();
		}
		if (pckBuilder.length() > 0) {
			pckBuilder.deleteCharAt(0);
		}

		String pck = pckBuilder.toString();
		String fullyQualifiedName = pck + "." + simpleName;
		
		return new ContentAssistItem(fullyQualifiedName, simpleName, pck, null);
	}

}
