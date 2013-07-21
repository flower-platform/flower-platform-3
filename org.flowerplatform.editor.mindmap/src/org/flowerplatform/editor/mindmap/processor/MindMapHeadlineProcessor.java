package org.flowerplatform.editor.mindmap.processor;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.codesync.wiki.WikiPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Cristina Constantinescu
 */
public class MindMapHeadlineProcessor extends AbstractMindMapChangeProcessor {

	protected void processFeatureChange(EObject object, FeatureChange featureChange, View associatedViewOnOpenDiagram, Map<String, Object> viewDetails) {
		super.processFeatureChange(object, featureChange, associatedViewOnOpenDiagram, viewDetails);
		if (WikiPlugin.HEADLINE_LEVEL_1_CATEGORY.equals(((CodeSyncElement) object).getType())) {
			viewDetails.put("icon", "images/heading_1.png");
		} else if (WikiPlugin.HEADLINE_LEVEL_2_CATEGORY.equals(((CodeSyncElement) object).getType())) {
			viewDetails.put("icon", "images/heading_2.png");
		} else if (WikiPlugin.HEADLINE_LEVEL_3_CATEGORY.equals(((CodeSyncElement) object).getType())) {
			viewDetails.put("icon", "images/heading_3.png");
		}		
	}
	
}
