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
package org.flowerplatform.editor.model.change_processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.flowerplatform.editor.model.remote.ViewDetailsUpdate;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana Gheorghe
 */
public abstract class IconDiagrammableElementFeatureChangesProcessor implements IDiagrammableElementFeatureChangesProcessor {

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
			if (associatedViewOnOpenDiagram.eResource() != null) {
				
				update.setViewId(associatedViewOnOpenDiagram.eResource().getURIFragment(associatedViewOnOpenDiagram));
				update.setViewDetails(viewDetails);
			
				DiagramUpdaterChangeProcessorContext.getDiagramUpdaterChangeDescriptionProcessingContext(context, true).
					getViewDetailsUpdates().add(update);
			}
		}
	}
	
	protected void processFeatureChange(EObject object, FeatureChange featureChange, View associatedViewOnOpenDiagram, Map<String, Object> viewDetails) {
		viewDetails.put("label", getLabel(object, false));
		viewDetails.put("iconUrls", getIconUrls(object));
	}
	
	abstract public String getLabel(EObject object, boolean forEditing);

	abstract protected String getIconUrls(EObject object);
	
}
