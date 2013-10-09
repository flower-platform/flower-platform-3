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

import org.eclipse.emf.ecore.EObject;
import org.flowerplatform.common.ied.InplaceEditorLabelParseResult;

import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * @author Mariana Gheorghe
 */
public class JavaClassAttributeProcessor extends JavaClassChildProcessor {

	@Override
	public String getLabel(EObject object, boolean forEditing) {
		CodeSyncElement cse = getCodeSyncElement(object);
		return labelParser.createAttributeLabel(
				new InplaceEditorLabelParseResult()
					.setName((String) getFeatureValue(cse, CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name()))
					.setType((String) getFeatureValue(cse, AstCacheCodePackage.eINSTANCE.getTypedElement_Type()))
					.setVisibility(encodeVisibility(cse))
					.setDefaultValue((String) getFeatureValue(cse, AstCacheCodePackage.eINSTANCE.getAttribute_Initializer())),
				forEditing);
	}

	@Override
	protected String getImageForVisibility(int type) {
		switch (type) {
		case org.eclipse.jdt.core.dom.Modifier.PUBLIC:		return "images/obj16/SyncProperty_public.gif";
		case org.eclipse.jdt.core.dom.Modifier.PROTECTED:	return "images/obj16/SyncProperty_protected.gif";
		case org.eclipse.jdt.core.dom.Modifier.PRIVATE:		return "images/obj16/SyncProperty_private.gif";
		default: 											return "images/obj16/SyncProperty_package.gif";
		}
	}
	
}