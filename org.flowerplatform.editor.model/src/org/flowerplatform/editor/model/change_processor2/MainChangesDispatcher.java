package org.flowerplatform.editor.model.change_processor2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristi
 */
public class MainChangesDispatcher {

	private static final Logger logger = LoggerFactory.getLogger(MainChangesDispatcher.class);
	
	protected List<IChangesProcessor> processors = new ArrayList<IChangesProcessor>();
	
	public MainChangesDispatcher addProcessor(IChangesProcessor processor) {
		processors.add(processor);
		return this;
	}
	
	private Changes getOrCreateDataForProcessors(Map<EObject, Changes> map, EObject key) {
		Changes dataForProcessors = map.get(key);
		if (dataForProcessors == null) {
			dataForProcessors = new Changes();
			map.put(key, dataForProcessors);
		}
		return dataForProcessors;
	}
	
	protected void findAddedToRemovedFrom(Map.Entry<EObject, EList<FeatureChange>> entry) {
		
	}
	
	public void processChangeDescription(Map<String, Object> context, ChangeDescription changeDescription) {
		// We have 2 passes and not just one because we want to only one notification for a given
		// object. E.g. if it has featureChanges, addedTo, removedFrom, we want to send them together. For
		// this purpose we buffer the data into this variable.
		//
		// If we didn't have 2 passes, the objects would receive multiple invocations, because of the findAddedToRemovedFrom()
		// algorithm
		Map<EObject, Changes> dataForAllProcessors = new HashMap<EObject, Changes>();
		for (Map.Entry<EObject, EList<FeatureChange>> entry : changeDescription.getObjectChanges().entrySet()) {
			if (logger.isTraceEnabled()) {
				logger.trace("Pass1: processing change description for object = {}, feature changes = {}", entry.getKey(), Changes.printFeatureChanges(entry.getValue()));
			}
			Changes dataForProcessors = getOrCreateDataForProcessors(dataForAllProcessors, entry.getKey());
			dataForProcessors.setFeatureChanges(entry.getValue());
			findAddedToRemovedFrom(entry);
		}
		
		for (Map.Entry<EObject, Changes> entry : dataForAllProcessors.entrySet()) {
			if (logger.isDebugEnabled()) {
				logger.debug("Pass2: dispatching changes for object = {}, changes = {}", entry.getKey(), entry.getValue());
			}
			for (IChangesProcessor processor : processors) {
				processor.processChanges(context, entry.getKey(), entry.getValue());
			}
		}

	}
}
