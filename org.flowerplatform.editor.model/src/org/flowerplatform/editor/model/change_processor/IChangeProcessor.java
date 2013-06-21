package org.flowerplatform.editor.model.change_processor;

import java.util.Map;

import org.eclipse.emf.ecore.change.ChangeDescription;

public interface IChangeProcessor {

	public void processChangeDescription(ChangeDescription changeDescription, Map<String, Object> context);
	
}
