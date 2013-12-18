package org.flowerplatform.codesync.config.extension;

import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.emf_model.notation.Bounds;
import org.flowerplatform.emf_model.notation.NotationFactory;
import org.flowerplatform.emf_model.notation.Note;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

public class AddNewExtension_Note extends AddNewExtension_TopLevelElement {

	@Override
	public boolean addNew(CodeSyncElement codeSyncElement, View parent,
			Resource codeSyncMappingResource, Map<String, Object> parameters) {
		// check if top-level element
		if (parent != null || !"note".equals(codeSyncElement.getType())) {
			return true;
		}
				
		Note node = NotationFactory.eINSTANCE.createNote();
				
		// set layout constraints
		Bounds bounds = NotationFactory.eINSTANCE.createBounds();
		bounds.setX(getParameterValue(parameters, X, 100));
		bounds.setY(getParameterValue(parameters, Y, 100));
		bounds.setWidth(getParameterValue(parameters, WIDTH, 100));
		bounds.setHeight(getParameterValue(parameters, HEIGHT, 100));
		node.setLayoutConstraint(bounds);	
		
		parameters.put(CodeSyncPlugin.VIEW, node);
		
		return false;
	}

}
