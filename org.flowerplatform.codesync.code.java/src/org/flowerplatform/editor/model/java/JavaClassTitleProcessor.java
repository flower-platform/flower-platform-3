package org.flowerplatform.editor.model.java;

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

public class JavaClassTitleProcessor implements IDiagrammableElementFeatureChangesProcessor {

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
		viewDetails.put("label", CodeSyncPlugin.getInstance().getFeatureValue((CodeSyncElement) object, CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name()));
		viewDetails.put("iconUrls", new String[] {"images/obj16/SyncClass.gif"});
	}

}
