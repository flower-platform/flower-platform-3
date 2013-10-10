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
package org.flowerplatform.editor.mindmap.processor;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.MindMapElement;

/**
 * @author Cristina Constantinescu
 */
public class MindMapDefaultProcessor extends AbstractMindMapChangeProcessor {

	protected void processFeatureChange(EObject object, FeatureChange featureChange, View associatedViewOnOpenDiagram, Map<String, Object> viewDetails) {
		super.processFeatureChange(object, featureChange, associatedViewOnOpenDiagram, viewDetails);
		
		if (featureChange == null || CodeSyncPackage.eINSTANCE.getMindMapElement_Icons().equals(featureChange.getFeature())) {
			viewDetails.put("icons", CodeSyncPlugin.getInstance().getFeatureValue((MindMapElement) object, CodeSyncPackage.eINSTANCE.getMindMapElement_Icons()));
		} 
		if (featureChange == null || CodeSyncPackage.eINSTANCE.getMindMapElement_MinWidth().equals(featureChange.getFeature())) {
			viewDetails.put("minWidth", CodeSyncPlugin.getInstance().getFeatureValue((MindMapElement) object, CodeSyncPackage.eINSTANCE.getMindMapElement_MinWidth()));
		}
		if (featureChange == null ||CodeSyncPackage.eINSTANCE.getMindMapElement_MaxWidth().equals(featureChange.getFeature())) {
			viewDetails.put("maxWidth", CodeSyncPlugin.getInstance().getFeatureValue((MindMapElement) object, CodeSyncPackage.eINSTANCE.getMindMapElement_MaxWidth()));
		}		
	}
	
}