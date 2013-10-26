package org.flowerplatform.editor.model.changes_processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.ChangeKind;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.eclipse.emf.ecore.change.ListChange;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.editor.model.change_processor.DiagramUpdaterChangeProcessorContext;
import org.flowerplatform.editor.model.change_processor.IDiagrammableElementFeatureChangesProcessor;
import org.flowerplatform.emf_model.notation.NotationElement;
import org.flowerplatform.emf_model.notation.NotationPackage;
import org.flowerplatform.emf_model.notation.View;
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
	
	protected void findAddedToRemovedFrom(Map<EObject, Changes> changesInPreparation, Map.Entry<EObject, EList<FeatureChange>> entry) {
		EObject object = entry.getKey();
		for (FeatureChange featureChange : entry.getValue()) {
			if (featureChange.getFeature() instanceof EReference) {
				if (!featureChange.getFeature().isMany()) {
					// TODO CS/CS3 LOW de prelucrat si cazul in care avem containment pe feature care nu e many
//					throw new UnsupportedOperationException("Reference feature that is not many is not yet supported; feature = " + featureChange.getFeatureName());
				} else {
					// many
					if (featureChange.getListChanges().isEmpty()) {
						// special case: the list was empty before adding => we don't have listChanges; this happens because
						// it's not actually an ADD operation; it's a SET operation, from an empty list. I think the same scenario
						// happens if the user does a SET instead of an ADD (on a non-empty list)
						@SuppressWarnings("unchecked")
						List<EObject> oldList = (List<EObject>) featureChange.getValue();
						@SuppressWarnings("unchecked")
						List<EObject> currentList = (List<EObject>) object.eGet(featureChange.getFeature());
						
						for (EObject mostProbablyNewlyAddedElement : currentList) {
							if (oldList == null || !oldList.contains(mostProbablyNewlyAddedElement)) {
								// the .contains(...) is for the case where the current object was both in the old + new list;
								// I named the variable "probably..." because this case is unlikely to exist, but not impossible
								getOrCreateDataForProcessors(changesInPreparation, mostProbablyNewlyAddedElement).addAndLogAddedTo(mostProbablyNewlyAddedElement, object, featureChange.getFeature());
								// TODO CS/CS3 LOW De testat pentru cazul setList(null), sau setList(ceva): cred ca tot aici picam, si tr sa raportam niste obiecte suplimentare ca sterse;
								// de asemenea de testat si ca cazul de mai sus, pt. add in care avem o lista noua care contine si elemente care erau in lista veche; eu l-am copiat, insa
								// nu l-am vazut cu ochii mei.
							}
						}
					} else {
						// normal case: the list was not empty before adding
						int indexCorrectionBecauseOfPreviousItems = 0;
						for (ListChange listChange : featureChange.getListChanges()) {
							if (listChange.getKind() == ChangeKind.REMOVE_LITERAL) {
								// an element was added; the recording indicates the index where we
								// should delete, in order to remove it. This happens because the changes are in a reverse
								// delta form. E.g. if we have a list with 2 elements (a, b), and we add 2 elements at the end
								// (c, d), we would have c = 2, d = 2; meaning remove twice at 2, and you'll get the initial list.
								//
								// That's why we need a correction, that we increment each time. It seems that the list is always sorted
								// according to the index, and if there are additions AND removals, the additions are processed first. So 
								// the index correction doesn't need to happen there.
								@SuppressWarnings("unchecked")
								EObject newlyAddedObject = ((List<EObject>) object.eGet(featureChange.getFeature())).get(listChange.getIndex() + indexCorrectionBecauseOfPreviousItems);
								getOrCreateDataForProcessors(changesInPreparation, newlyAddedObject).addAndLogAddedTo(newlyAddedObject, object, featureChange.getFeature());
								indexCorrectionBecauseOfPreviousItems++;
							} else if (listChange.getKind() == ChangeKind.ADD_LITERAL) {
								// an element was removed
								for (EObject removedObject : listChange.getReferenceValues()) {
									getOrCreateDataForProcessors(changesInPreparation, removedObject).addAndLogRemovedFrom(removedObject, object, featureChange.getFeature());
								}
							}
						}
					}
				}
			}
		}

	}
	
	public void processChangeDescription(Map<String, Object> context, ChangeDescription changeDescription) {
		// We have 2 passes and not just one because we want to only one notification for a given
		// object. E.g. if it has featureChanges, addedTo, removedFrom, we want to send them together. For
		// this purpose we buffer the data into this variable.
		//
		// If we didn't have 2 passes, the objects would receive multiple invocations, because of the findAddedToRemovedFrom()
		// algorithm
		Map<EObject, Changes> changesInPreparation = new HashMap<EObject, Changes>();
		for (Map.Entry<EObject, EList<FeatureChange>> entry : changeDescription.getObjectChanges().entrySet()) {
			if (logger.isTraceEnabled()) {
				logger.trace("Pass1: processing change description for object = {}, feature changes = {}", entry.getKey(), Changes.printFeatureChanges(entry.getValue()));
			}
			Changes dataForProcessors = getOrCreateDataForProcessors(changesInPreparation, entry.getKey());
			dataForProcessors.setFeatureChanges(entry.getValue());
			findAddedToRemovedFrom(changesInPreparation, entry);
		}
		
		for (Map.Entry<EObject, Changes> entry : changesInPreparation.entrySet()) {
			if (logger.isDebugEnabled()) {
				logger.debug("Pass2: dispatching changes for object = {}, changes = {}", entry.getKey(), entry.getValue());
			}
			for (IChangesProcessor processor : processors) {
				processor.processChanges(context, entry.getKey(), entry.getValue());
			}
		}
	}
}
