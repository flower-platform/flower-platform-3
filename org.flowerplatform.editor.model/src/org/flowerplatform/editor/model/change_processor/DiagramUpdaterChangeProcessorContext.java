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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.editor.model.remote.ViewDetailsUpdate;
import org.flowerplatform.emf_model.notation.View;

public class DiagramUpdaterChangeProcessorContext {
	
	public static final String KEY = "DiagramUpdaterChangeProcessorContext";
	
	public static DiagramUpdaterChangeProcessorContext getDiagramUpdaterChangeDescriptionProcessingContext(Map<String, Object> context, boolean createIfNeeded) {
		DiagramUpdaterChangeProcessorContext diagramUpdaterChangeDescriptionProcessingContext = (DiagramUpdaterChangeProcessorContext) context.get(DiagramUpdaterChangeProcessorContext.KEY);
		if (diagramUpdaterChangeDescriptionProcessingContext == null && createIfNeeded) {
			diagramUpdaterChangeDescriptionProcessingContext = new DiagramUpdaterChangeProcessorContext();
			context.put(DiagramUpdaterChangeProcessorContext.KEY, diagramUpdaterChangeDescriptionProcessingContext);
		}
		return diagramUpdaterChangeDescriptionProcessingContext;
	}
	
	// TODO CS/CS3: de facut aceste liste lazy; de asemenea, cred ca ID-ul tr. facut generic Object
	private List<Object> objectsToUpdate = new ArrayList<Object>();
	
	private List<String> objectIdsToDispose = new ArrayList<String>();
	
	private List<ViewDetailsUpdate> viewDetailsUpdates = new ArrayList<ViewDetailsUpdate>();

	public List<Object> getObjectsToUpdate() {
		return objectsToUpdate;
	}

	public List<String> getObjectIdsToDispose() {
		return objectIdsToDispose;
	}

	public List<ViewDetailsUpdate> getViewDetailsUpdates() {
		return viewDetailsUpdates;
	}
	
	public boolean isEmpty() {
		return objectsToUpdate.isEmpty() && objectIdsToDispose.isEmpty() && viewDetailsUpdates.isEmpty();
	}
	
	public void addObjectToUpdate(EObject object, boolean notifyProcessorsIfView, Map<String, Object> context) {
		getObjectsToUpdate().add(object);
		if (notifyProcessorsIfView && object instanceof View) {
			View view = (View) object;
			List<IDiagrammableElementFeatureChangesProcessor> processors = EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().getDiagrammableElementFeatureChangesProcessors(view.getViewType());
			if (processors != null) {
				for (IDiagrammableElementFeatureChangesProcessor processor : processors) {
					processor.processFeatureChanges(view.getDiagrammableElement(), null, view, context);
				}
			}
		}
	}
}