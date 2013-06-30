package org.flowerplatform.editor.model.change_processor;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.flowerplatform.emf_model.notation.View;

public interface IDiagrammableElementFeatureChangesProcessor {

	public void processFeatureChanges(EObject object, List<FeatureChange> featureChanges, View associatedViewOnOpenDiagram, Map<String, Object> context);
	
}
