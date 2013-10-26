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
package org.flowerplatform.codesync.processor;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.flowerplatform.editor.model.change_processor.DiagramUpdaterChangeProcessorContext;
import org.flowerplatform.editor.model.change_processor.IDiagrammableElementFeatureChangesProcessor;
import org.flowerplatform.editor.model.remote.ViewDetailsUpdate;
import org.flowerplatform.emf_model.notation.View;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncCategorySeparatorProcessor implements IDiagrammableElementFeatureChangesProcessor {

	@Override
	public void processFeatureChanges(EObject object, List<FeatureChange> featureChanges, 
			View associatedViewOnOpenDiagram, Map<String, Object> context) {
		ViewDetailsUpdate update = new ViewDetailsUpdate();
		update.setViewId(associatedViewOnOpenDiagram.eResource().getURIFragment(associatedViewOnOpenDiagram));
		update.setViewDetails((Map<String, Object>) associatedViewOnOpenDiagram.getViewDetails());
	
		DiagramUpdaterChangeProcessorContext.getDiagramUpdaterChangeDescriptionProcessingContext(context, true).
			getViewDetailsUpdates().add(update);
	}

}
