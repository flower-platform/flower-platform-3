package org.flowerplatform.editor.model.change_processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.flowerplatform.editor.model.change_processor.DiagramUpdaterChangeProcessorContext;
import org.flowerplatform.editor.model.change_processor.IDiagrammableElementFeatureChangesProcessor;
import org.flowerplatform.editor.model.remote.ViewDetailsUpdate;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.notation.NotationPackage;
import org.flowerplatform.emf_model.notation.View;

/**
 * @author Cristina Constantinescu
 */
public class DiagramPropertiesChangeProcessor implements IDiagrammableElementFeatureChangesProcessor {

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
		if (featureChange == null || NotationPackage.eINSTANCE.getDiagram_ShowNewElementsPathDialog().equals(featureChange.getFeature())) {
			viewDetails.put("showNewElementsPathDialog", ((Diagram) associatedViewOnOpenDiagram).isShowNewElementsPathDialog());
		}		
		if (featureChange == null || NotationPackage.eINSTANCE.getDiagram_NewElementsPath().equals(featureChange.getFeature())) {
			viewDetails.put("newElementsPath", ((Diagram) associatedViewOnOpenDiagram).getNewElementsPath());
		} 
	}

	

}
