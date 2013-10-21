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
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.MindMapElement;
import com.crispico.flower.mp.model.codesync.MindMapRoot;

/**
 * @author Cristina Constantinescu
 */
public class MindMapDefaultProcessor implements IDiagrammableElementFeatureChangesProcessor {

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
		if (featureChange == null || CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children().equals(featureChange.getFeature())) {
			viewDetails.put("hasChildren", ((CodeSyncElement) object).getChildren().size() > 0);
		}		
		if (featureChange == null || CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name().equals(featureChange.getFeature())) {
			if (object instanceof MindMapRoot) {
				viewDetails.put("text", "Root");
			} else {
				viewDetails.put("text", CodeSyncPlugin.getInstance().getCodeSyncOperationsService()
						.getFeatureValue((CodeSyncElement) object, CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name()));
			}
		}		
		if (featureChange == null || CodeSyncPackage.eINSTANCE.getMindMapElement_Expanded().equals(featureChange.getFeature())) {
			viewDetails.put("expanded", CodeSyncPlugin.getInstance().getCodeSyncOperationsService()
					.getFeatureValue((MindMapElement) object, CodeSyncPackage.eINSTANCE.getMindMapElement_Expanded()));
		}		
		if (featureChange == null || CodeSyncPackage.eINSTANCE.getMindMapElement_Icons().equals(featureChange.getFeature())) {
			viewDetails.put("icons", CodeSyncPlugin.getInstance().getCodeSyncOperationsService()
					.getFeatureValue((MindMapElement) object, CodeSyncPackage.eINSTANCE.getMindMapElement_Icons()));
		} 
		if (featureChange == null || CodeSyncPackage.eINSTANCE.getMindMapElement_MinWidth().equals(featureChange.getFeature())) {
			viewDetails.put("minWidth", CodeSyncPlugin.getInstance().getCodeSyncOperationsService()
					.getFeatureValue((MindMapElement) object, CodeSyncPackage.eINSTANCE.getMindMapElement_MinWidth()));
		}
		if (featureChange == null ||CodeSyncPackage.eINSTANCE.getMindMapElement_MaxWidth().equals(featureChange.getFeature())) {
			viewDetails.put("maxWidth", CodeSyncPlugin.getInstance().getCodeSyncOperationsService()
					.getFeatureValue((MindMapElement) object, CodeSyncPackage.eINSTANCE.getMindMapElement_MaxWidth()));
		}	
		if (featureChange == null || CodeSyncPackage.eINSTANCE.getMindMapElement_Side().equals(featureChange.getFeature())) {
			int side = (int) CodeSyncPlugin.getInstance().getCodeSyncOperationsService()
					.getFeatureValue((MindMapElement) object, CodeSyncPackage.eINSTANCE.getMindMapElement_Side());
			if (featureChange == null && side == 0 && !(object instanceof MindMapRoot)) {
				side = 1;
			}
			viewDetails.put("side", side);
		}	
	}
	
}