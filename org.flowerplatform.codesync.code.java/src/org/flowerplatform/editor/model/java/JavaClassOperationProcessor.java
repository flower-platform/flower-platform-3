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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.flowerplatform.common.ied.InplaceEditorLabelParseResult;

import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;
import com.crispico.flower.mp.model.astcache.code.Parameter;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * @author Mariana Gheorghe
 */
public class JavaClassOperationProcessor extends JavaClassChildProcessor {

	@Override
	protected String getLabel(EObject object) {
		CodeSyncElement cse = getCodeSyncElement(object);
		Collection<InplaceEditorLabelParseResult> parameters = new ArrayList<InplaceEditorLabelParseResult>();
		Collection<Parameter> modelParameters = (Collection<Parameter>) getFeatureValue(cse, AstCacheCodePackage.eINSTANCE.getOperation_Parameters());
		for (Parameter parameter : modelParameters) {
			parameters.add(new InplaceEditorLabelParseResult()
					.setName(parameter.getName())
					.setType(parameter.getType()));
		}
		String name = (String) getFeatureValue(cse, CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name());
		int index = name.indexOf("(");
		if (index >= 0) {
			name = name.substring(0, index);
		}
		return labelParser.createOperationLabel(
				new InplaceEditorLabelParseResult()
				.setName(name)
				.setType((String) getFeatureValue(cse, AstCacheCodePackage.eINSTANCE.getTypedElement_Type()))
				.setVisibility(encodeVisibility(cse))
				.setParameters(parameters));
	}
	
	@Override
	protected String getImageForVisibility(int type) {
		switch (type) {
		case org.eclipse.jdt.core.dom.Modifier.PUBLIC:		return "images/obj16/SyncOperation_public.gif";
		case org.eclipse.jdt.core.dom.Modifier.PROTECTED:	return "images/obj16/SyncOperation_protected.gif";
		case org.eclipse.jdt.core.dom.Modifier.PRIVATE:		return "images/obj16/SyncOperation_private.gif";
		default: 											return "images/obj16/SyncOperation_package.gif";
		}
	}

}