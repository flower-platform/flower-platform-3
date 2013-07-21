package org.flowerplatform.editor.mindmap.processor;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.flowerplatform.emf_model.notation.View;

/**
 * @author Cristina Constantinescu
 */
public class MindMapParagraphProcessor extends AbstractMindMapChangeProcessor {

	protected void processFeatureChange(EObject object, FeatureChange featureChange, View associatedViewOnOpenDiagram, Map<String, Object> viewDetails) {
		super.processFeatureChange(object, featureChange, associatedViewOnOpenDiagram, viewDetails);
		viewDetails.put("icon", "images/paragraph.png");
	}
	
}
