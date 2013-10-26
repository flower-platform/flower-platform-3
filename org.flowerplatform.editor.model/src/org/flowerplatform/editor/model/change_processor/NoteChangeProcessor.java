package org.flowerplatform.editor.model.change_processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.flowerplatform.editor.model.remote.ViewDetailsUpdate;
import org.flowerplatform.emf_model.notation.NotationPackage;
import org.flowerplatform.emf_model.notation.Note;
import org.flowerplatform.emf_model.notation.View;

public class NoteChangeProcessor implements
		IDiagrammableElementFeatureChangesProcessor {

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
		if (featureChange == null || NotationPackage.eINSTANCE.getNote_Text().equals(featureChange.getFeature())) {
			viewDetails.put("text", ((Note) associatedViewOnOpenDiagram).getText());
		}		
	}

}
