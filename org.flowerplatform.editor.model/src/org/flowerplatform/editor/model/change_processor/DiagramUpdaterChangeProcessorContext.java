package org.flowerplatform.editor.model.change_processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

	public List<Object> getObjectsToUpdate() {
		return objectsToUpdate;
	}

	public List<String> getObjectIdsToDispose() {
		return objectIdsToDispose;
	}
	
}
