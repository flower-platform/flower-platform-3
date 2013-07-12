package org.flowerplatform.editor.model.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.editor.model.change_processor.DiagramUpdaterChangeProcessorContext;
import org.flowerplatform.editor.model.change_processor.IDiagrammableElementFeatureChangesProcessor;
import org.flowerplatform.emf_model.notation.Node;
import org.flowerplatform.emf_model.notation.NotationFactory;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana
 */
public class JavaClassProcessor implements IDiagrammableElementFeatureChangesProcessor {

	@Override
	public void processFeatureChanges(EObject object, List<FeatureChange> featureChanges, View associatedViewOnOpenDiagram, Map<String, Object> context) {
		if (object == null) {
			return;
		}
		
		List<Object> objectsToUpdate = new ArrayList<Object>();
		
		if (featureChanges == null) {
			// full content
			processFeatureChange(object, null, associatedViewOnOpenDiagram, context, objectsToUpdate);
		} else {
			// TODO diff
		}
		
		if (!objectsToUpdate.isEmpty()) {
			DiagramUpdaterChangeProcessorContext.getDiagramUpdaterChangeDescriptionProcessingContext(context, true).
				getObjectsToUpdate().addAll(objectsToUpdate);
		}
	}

	protected void processFeatureChange(EObject object, FeatureChange featureChange, View associatedViewOnOpenDiagram,  Map<String, Object> context, List<Object> objectsToUpdate) {
		if (featureChange == null) {
			// TODO test for attributes
			for (EObject child : getCodeSyncElement(object).getChildren()) {
				Node node = NotationFactory.eINSTANCE.createNode();
//				node.setViewType(getCodeSyncElement(child).getType());
				node.setViewType("classAttribute");
				node.setDiagrammableElement(child);
				associatedViewOnOpenDiagram.getPersistentChildren().add(node);
				objectsToUpdate.add(node);
				
				IDiagrammableElementFeatureChangesProcessor processor = EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().getDiagrammableElementFeatureChangesProcessor(node.getViewType());
				if (processor != null) {
					processor.processFeatureChanges(node.getDiagrammableElement(), null, node, context);
				}
			}
		}
	}
	
	protected CodeSyncElement getCodeSyncElement(EObject object) { 
		return (CodeSyncElement) object;
	}
	
}
