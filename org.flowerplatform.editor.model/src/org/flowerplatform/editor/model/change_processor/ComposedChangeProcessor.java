package org.flowerplatform.editor.model.change_processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.change.ChangeDescription;

public class ComposedChangeProcessor implements IChangeProcessor {

	protected List<IChangeProcessor> processors = new ArrayList<IChangeProcessor>();
	
	public void addChangeDescriptionProcessor(IChangeProcessor processor) {
		processors.add(processor);
	}
	
	@Override
	public void processChangeDescription(ChangeDescription changeDescription, Map<String, Object> context) {
		for (IChangeProcessor processor : processors) {
			processor.processChangeDescription(changeDescription, context);
		}
	}

}
