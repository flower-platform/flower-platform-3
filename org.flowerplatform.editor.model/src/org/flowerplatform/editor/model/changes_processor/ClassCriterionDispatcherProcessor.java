package org.flowerplatform.editor.model.changes_processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

/**
 * @author Cristi
 */
public class ClassCriterionDispatcherProcessor extends AbstractCriterionDispatcherProcessor<Class<?>> {

	@Override
	protected List<IChangesProcessor> getProcessorsThatMeetCriterion(Map<String, Object> context, EObject object, Changes changes) {
		List<IChangesProcessor> result = new ArrayList<IChangesProcessor>();
		for (Map.Entry<Class<?>, List<IChangesProcessor>> entry : processors.entrySet()) {
			if (entry.getKey().isAssignableFrom(object.getClass())) {
				// found a class that's compatible (i.e. same or superclass) to the class of the current object
				result.addAll(entry.getValue());
			}
		}
		if (result.isEmpty()) {
			return null;
		} else {
			return result;
		}
	}

}
