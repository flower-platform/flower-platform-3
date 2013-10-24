package org.flowerplatform.editor.model.change_processor2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

/**
 * @author Cristi
 */
public abstract class AbstractCriterionDispatcherProcessor<T> implements IChangesProcessor {
	protected Map<T, List<IChangesProcessor>> processors = new HashMap<T, List<IChangesProcessor>>();
	
	public AbstractCriterionDispatcherProcessor<T> addProcessor(T key, IChangesProcessor processor) {
		List<IChangesProcessor> list = processors.get(key);
		if (list == null) {
			list = new ArrayList<IChangesProcessor>();
			processors.put(key, list);
		}
		
		list.add(processor);
		return this;
	}

	@Override
	public void processChanges(Map<String, Object> context, EObject object, Changes changes) {
		List<IChangesProcessor> procesorsThatMeetCriterion = getProcessorsThatMeetCriterion(context, object, changes);
		if (procesorsThatMeetCriterion == null) {
			return;
		}
		for (IChangesProcessor processor : procesorsThatMeetCriterion) {
			processor.processChanges(context, object, changes);
		}
	}
	
	protected abstract List<IChangesProcessor> getProcessorsThatMeetCriterion(Map<String, Object> context, EObject object, Changes changes);
	
}
