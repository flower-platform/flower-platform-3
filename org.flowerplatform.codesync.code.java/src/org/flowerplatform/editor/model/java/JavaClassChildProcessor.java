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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.flowerplatform.editor.model.change_processor.DiagramUpdaterChangeProcessorContext;
import org.flowerplatform.editor.model.change_processor.IDiagrammableElementFeatureChangesProcessor;
import org.flowerplatform.editor.model.remote.ViewDetailsUpdate;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;
import com.crispico.flower.mp.model.astcache.code.ExtendedModifier;
import com.crispico.flower.mp.model.astcache.code.Modifier;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * @author Mariana Gheorghe
 */
public abstract class JavaClassChildProcessor implements IDiagrammableElementFeatureChangesProcessor {

	@Override
	public void processFeatureChanges(EObject object, List<FeatureChange> featureChanges, View associatedViewOnOpenDiagram, Map<String, Object> context) {
		Map<String, Object> viewDetails = new HashMap<String, Object>();
		
		if (featureChanges == null) {
			// i.e. full content update when opening diagram
			processFeatureChange(object, null, associatedViewOnOpenDiagram, viewDetails);
		} else {
			// i.e. differential, during change description processing
			for (FeatureChange featureChange : featureChanges) {
				processFeatureChange(object, featureChange, associatedViewOnOpenDiagram, viewDetails);
			}
		}
		
		if (!viewDetails.isEmpty()) {
			ViewDetailsUpdate update = new ViewDetailsUpdate();
			update.setViewId(associatedViewOnOpenDiagram.eResource().getURIFragment(associatedViewOnOpenDiagram));
			update.setViewDetails(viewDetails);
			
			DiagramUpdaterChangeProcessorContext.getDiagramUpdaterChangeDescriptionProcessingContext(context, true).
				getViewDetailsUpdates().add(update);
		}
	}

	protected void processFeatureChange(EObject object, FeatureChange featureChange, View associatedViewOnOpenDiagram, Map<String, Object> viewDetails) {
		if (featureChange == null) {
			viewDetails.put("label", getLabel(object));
			viewDetails.put("iconUrls", getIconUrls(object));
		}
	}
	
	/**
	 * Default: {visibility} {name} : {type}
	 * 
	 * <p>
	 * 
	 * E.g. +attribute:int
	 * 		~getAttribute():int
	 */
	protected String getLabel(EObject object) {
		CodeSyncElement cse = getCodeSyncElement(object);
		String name = (String) CodeSyncPlugin.getInstance().getFeatureValue(cse, CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name());
		String type = (String) CodeSyncPlugin.getInstance().getFeatureValue(cse, AstCacheCodePackage.eINSTANCE.getTypedElement_Type());
		return String.format("%s%s:%s", encodeVisibility(cse), name, type);
	}
	
	protected String[] getIconUrls(EObject object) {
		return composeImage(getCodeSyncElement(object));
	}
	
	abstract protected String getImageForVisibility(int type);
	
	//////////////////////////////////
	// Utility methods
	//////////////////////////////////
		
	public CodeSyncElement getCodeSyncElement(EObject object) {
		return (CodeSyncElement) object;
	}
	
	public String encodeVisibility(CodeSyncElement object) {
		List<ExtendedModifier> modifiers = (List<ExtendedModifier>) 
				CodeSyncPlugin.getInstance().getFeatureValue(object, AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers());
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
	
	public String[] composeImage(CodeSyncElement object) {
		List<String> result = new ArrayList<String>();
		
		// decorate for visibility
		List<ExtendedModifier> modifiers = (List<ExtendedModifier>) 
				CodeSyncPlugin.getInstance().getFeatureValue(object, AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers());
		Modifier visibility = null;
		if (modifiers != null) {
			for (ExtendedModifier modifier : modifiers) {
				if (modifier instanceof Modifier) {
					switch (((Modifier) modifier).getType()) {
					case org.eclipse.jdt.core.dom.Modifier.PUBLIC:		
					case org.eclipse.jdt.core.dom.Modifier.PROTECTED:	
					case org.eclipse.jdt.core.dom.Modifier.PRIVATE:	
						visibility = (Modifier) modifier; break;
					case org.eclipse.jdt.core.dom.Modifier.STATIC:
						result.add("images/ovr16/Static.gif");  break;
					case org.eclipse.jdt.core.dom.Modifier.FINAL:
						result.add("images/ovr16/Final.gif"); break;
					}
				}
			}
		}
		result.add(0, getImageForVisibility(visibility == null ? 0 : visibility.getType()));
		
		return result.toArray(new String[0]);
	}
	
}