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

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.flowerplatform.codesync.processor.CodeSyncDecoratorsProcessor;
import org.flowerplatform.common.ied.InplaceEditorLabelParser;
import org.flowerplatform.editor.model.EditorModelPlugin;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;
import com.crispico.flower.mp.model.astcache.code.ExtendedModifier;
import com.crispico.flower.mp.model.astcache.code.Modifier;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana Gheorghe
 */
public abstract class JavaClassChildProcessor extends CodeSyncDecoratorsProcessor {

	protected InplaceEditorLabelParser labelParser = new InplaceEditorLabelParser(new JavaInplaceEditorProvider());

	@Override
	public String getIconBeforeCodeSyncDecoration(EObject object) {
		return composeImage(getCodeSyncElement(object));
	}
	
	abstract protected String getImageForVisibility(int type);
	
	//////////////////////////////////
	// Utility methods
	//////////////////////////////////
		
	protected CodeSyncElement getCodeSyncElement(EObject object) {
		return (CodeSyncElement) object;
	}
	
	protected Object getFeatureValue(CodeSyncElement codeSyncElement, EStructuralFeature feature) {
		return CodeSyncPlugin.getInstance().getFeatureValue(codeSyncElement, feature);
	}
	
	protected String encodeVisibility(CodeSyncElement object) {
		List<ExtendedModifier> modifiers = (List<ExtendedModifier>) 
				getFeatureValue(object, AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers());
		if (modifiers != null) {
			for (ExtendedModifier modifier : modifiers) {
				if (modifier instanceof Modifier) {
					switch (((Modifier) modifier).getType()) {
					case org.eclipse.jdt.core.dom.Modifier.PUBLIC:		return "+";
					case org.eclipse.jdt.core.dom.Modifier.PROTECTED:	return "#";
					case org.eclipse.jdt.core.dom.Modifier.PRIVATE:		return "-";
					default: 											return "~";
					}
				}
				break;
			}
		}
		return "";
	}	
	
	/**
	 * @author Sebastian Solomon
	 */
	public String composeImage(CodeSyncElement object) {
		String result = new String();
		String editorModelPakege = EditorModelPlugin.getInstance()
				.getBundleContext().getBundle().getSymbolicName();

		// decorate for visibility
		List<ExtendedModifier> modifiers = (List<ExtendedModifier>) CodeSyncPlugin
				.getInstance().getFeatureValue(
						object,
						AstCacheCodePackage.eINSTANCE
								.getModifiableElement_Modifiers());
		Modifier visibility = null;
		if (modifiers != null) {
			for (ExtendedModifier modifier : modifiers) {
				if (modifier instanceof Modifier) {
					switch (((Modifier) modifier).getType()) {
					case org.eclipse.jdt.core.dom.Modifier.PUBLIC:
					case org.eclipse.jdt.core.dom.Modifier.PROTECTED:
					case org.eclipse.jdt.core.dom.Modifier.PRIVATE:
						visibility = (Modifier) modifier;
						break;
					case org.eclipse.jdt.core.dom.Modifier.STATIC:
						result += editorModelPakege
								+ "/images/ovr16/Static.gif|";
						break;
					case org.eclipse.jdt.core.dom.Modifier.FINAL: // "org.flowerplatform.editor.model/images/ovr16/Final.gif|";
						result += editorModelPakege
								+ "/images/ovr16/Final.gif|";
						break;
					}
				}
			}
		}

		result = (getImageForVisibility(visibility == null ? 0 : visibility
				.getType())) + "|" + result;
		if (result.length() != 0)
			result = result.substring(0, result.length() - 1);
		return result;
	}
	
}
