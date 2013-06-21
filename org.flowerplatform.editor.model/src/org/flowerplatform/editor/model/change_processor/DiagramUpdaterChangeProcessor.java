package org.flowerplatform.editor.model.change_processor;

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
import org.flowerplatform.emf_model.notation.NotationElement;
import org.flowerplatform.emf_model.notation.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiagramUpdaterChangeProcessor implements IChangeProcessor {

	private static final Logger logger = LoggerFactory.getLogger(DiagramUpdaterChangeProcessor.class);
	
	// TODO CS/CS3 vizib public de test
	public Map<String, IDiagrammableElementFeatureChangesProcessor> diagrammableElementFeatureChangeProcessors = new HashMap<String, IDiagrammableElementFeatureChangesProcessor>();
	
	public void addDiagrammableElementFeatureChangeProcessor(String viewType, IDiagrammableElementFeatureChangesProcessor processor) {
		diagrammableElementFeatureChangeProcessors.put(viewType, processor);
	}
	
	protected boolean isDiagramOpen(NotationElement diagramChildObject) {
		return true;
//		while (diagramChildObject != null) {
//			if (diagramChildObject instanceof Diagram) {
//				return ((Diagram) diagramChildObject).isOpen();
//			} else {
//				if (diagramChildObject.eContainer() instanceof ChangeDescription) {
//					// TODO CS/CS3: idBeforeRemoval am putea face si un mecanism de retinut diagrama, sau id-ul diagramei. Astfel, voi putea sa zic cu precizie daca apartine unei diagra deschise, si pentru
//					// un view sters
//					return true;
//				}
//				// TODO CS/CS3: idBeforeRemoval am comentat pentru ca pentru cazul stergerii unui view cu sub-view-uri, da exceptie. Propunere:
//				// sa modificam acest feature sa nu arunce notificare. Altfel, ar trebui ca aici fie sa nu mai fac verificare, fie inainte sa mai fac
//				// o parcurgere a listei de feature, sa ma sigur ca nu este idBeforeRemoval
////				if (!(diagramChildObject.eContainer() instanceof NotationElement)) {
////					throw new IllegalArgumentException("Finding diagram for NotationElement; illegal containment: " + diagramChildObject.eContainer() + " for NotationElement: " + diagramChildObject);
////				}
//				diagramChildObject = (NotationElement) diagramChildObject.eContainer();
//			}
//		}
////		throw new IllegalArgumentException("Could not find diagram for NotationElement: " + diagramChildObject);
//		return false;
	}
	
	protected void processAddedOrRemovedObjectRecursive(EObject object, DiagramUpdaterChangeProcessorContext diagramUpdaterChangeDescriptionProcessingContext, boolean added, StringBuffer debugStringBuffer) {
		for (EObject child : object.eContents()) {
			processAddedOrRemovedObjectRecursive(child, diagramUpdaterChangeDescriptionProcessingContext, added, debugStringBuffer);
		}
		if (debugStringBuffer != null) {
			debugStringBuffer.append(object);
			debugStringBuffer.append(',');
		}
		if (added) {
			diagramUpdaterChangeDescriptionProcessingContext.getObjectsToUpdate().add(object);
		} else {
//			// removed
//			diagramUpdaterChangeDescriptionProcessingContext.getObjectIdsToDispose().add(((NotationElement) object).getIdBeforeRemoval());
		}
	}
	
	
	protected void addNewlyAddedObject(EObject object, DiagramUpdaterChangeProcessorContext diagramUpdaterChangeDescriptionProcessingContext) {
		StringBuffer debugStringBuffer = null;
		if (logger.isDebugEnabled()) {
			debugStringBuffer = new StringBuffer();
		}
		
		// we use this recursive method (instead of a TreeIterator/object.eAllContens()), because we prefer to have the
		// children before the parents. This may be convenient during deserialization on Flex: when we deserialize an object,
		// we can use its child references right away. At the moment of writing, this was not a MUST, but this may be helpful 
		// in the future.
		processAddedOrRemovedObjectRecursive(object, diagramUpdaterChangeDescriptionProcessingContext, true, debugStringBuffer);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Added newly added objects = [{}]", debugStringBuffer);
		}
	}
	
	protected void addRemovedObject(EObject object, DiagramUpdaterChangeProcessorContext diagramUpdaterChangeDescriptionProcessingContext) {
		StringBuffer debugStringBuffer = null;
		if (logger.isDebugEnabled()) {
			debugStringBuffer = new StringBuffer();
		}
		
		processAddedOrRemovedObjectRecursive(object, diagramUpdaterChangeDescriptionProcessingContext, false, debugStringBuffer);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Added objects id to dispose for objects = [{}]", debugStringBuffer);
		}
	}

	
	protected void processFeatureChangesForNotationElement(Map.Entry<EObject, EList<FeatureChange>> entry, Map<String, Object> context) {
		if (!(entry.getKey() instanceof NotationElement)) {
			return;
		} 
		NotationElement notationElement = (NotationElement) entry.getKey();
		if (!isDiagramOpen(notationElement)) {
			return;
		}
		
		DiagramUpdaterChangeProcessorContext diagramUpdaterChangeDescriptionProcessingContext = DiagramUpdaterChangeProcessorContext.getDiagramUpdaterChangeDescriptionProcessingContext(context, true);
		
		for (FeatureChange featureChange : entry.getValue()) {
//			if (FlowerDiagramNotationPackage.eINSTANCE.getDiagramNotationElement_IdBeforeRemoval().equals(featureChange.getFeature())) {
//				// TODO CS/CS3 idBeforeRemoval: de hotarat; deocamdata pot spune ca daca modific aici, sigur pot sa ignor si celelalte
//				return;
//			}
			if (featureChange.getFeature() instanceof EReference && ((EReference) featureChange.getFeature()).isContainment()) {
				if (!featureChange.getFeature().isMany()) {
					// TODO CS/CS3 LOW de prelucrat si cazul in care avem containment pe feature care nu e many
					throw new UnsupportedOperationException("Containment feature that is not many is not yet supported; feature = " + featureChange.getFeatureName());
				} else {
					// containment & many
					if (featureChange.getListChanges().isEmpty()) {
						// special case: the list was empty before adding => we don't have listChanges; this happens because
						// it's not actually an ADD operation; it's a SET operation, from an empty list. I think the same scenario
						// happens if the user does a SET instead of an ADD (on a non-empty list)
						@SuppressWarnings("unchecked")
						List<EObject> oldList = (List<EObject>) featureChange.getValue();
						@SuppressWarnings("unchecked")
						List<EObject> currentList = (List<EObject>) notationElement.eGet(featureChange.getFeature());
						
						for (EObject mostProbablyNewlyAddedElement : currentList) {
							if (oldList == null || !oldList.contains(mostProbablyNewlyAddedElement)) {
								// the .contains(...) is for the case where the current object was both in the old + new list;
								// I named the variable "probably..." because this case is unlikely to exist, but not impossible
								addNewlyAddedObject(mostProbablyNewlyAddedElement, diagramUpdaterChangeDescriptionProcessingContext);
								// TODO CS/CS3 LOW De testat pentru cazul setList(null), sau setList(ceva): cred ca tot aici picam, si tr sa raportam niste obiecte suplimentare ca sterse;
								// de asemenea de testat si ca cazul de mai sus, pt. add in care avem o lista noua care contine si elemente care erau in lista veche; eu l-am copiat, insa
								// nu l-am vazut cu ochii mei.
							}
						}
					} else {
						// normal case: the list was not empty before adding
						for (ListChange listChange : featureChange.getListChanges()) {
							if (listChange.getKind() == ChangeKind.REMOVE_LITERAL) {
								// an element was added; the recording only indicates the index, so we need to look it up
								// by ourselves
								@SuppressWarnings("unchecked")
								EObject newlyAddedObject = ((List<EObject>) notationElement.eGet(featureChange.getFeature())).get(listChange.getIndex());
								addNewlyAddedObject(newlyAddedObject, diagramUpdaterChangeDescriptionProcessingContext);
							} else if (listChange.getKind() == ChangeKind.ADD_LITERAL) {
								// an element was removed
								for (EObject removedObject : listChange.getReferenceValues()) {
									addRemovedObject(removedObject, diagramUpdaterChangeDescriptionProcessingContext);
								}
							}
						}
					}
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Adding object to update = {}", notationElement);
		}
		diagramUpdaterChangeDescriptionProcessingContext.getObjectsToUpdate().add(notationElement);
	}

	protected void processFeatureChangesForDiagrammableElement(Map.Entry<EObject, EList<FeatureChange>> entry, Map<String, Object> context) {
//		if (!(entry.getKey() instanceof IDiagrammableElement)) {
//			return;
//		} 
//		IDiagrammableElement diagrammableElement = (IDiagrammableElement) entry.getKey();
//
//		for (View view : diagrammableElement.getViews()) {
//			if (isDiagramOpen(view)) {
//				IDiagrammableElementFeatureChangesProcessor processor = diagrammableElementFeatureChangeProcessors.get(view.getViewType());
//				if (processor != null) {
//					if (logger.isDebugEnabled()) {
//						logger.debug("Delegating to IDiagrammableElementFeatureChangesProcessor for view = {}, object = {}", view, diagrammableElement);
//					}
//					processor.processFeatureChanges(diagrammableElement, entry.getValue(), view, context);
//				}
//			}
//		}
	}
	
	private String printFeatureChanges(List<FeatureChange> list) {
		StringBuffer sb = new StringBuffer("[");
		for (FeatureChange fc : list) {
			sb.append("(");
			sb.append(fc.getFeatureName()); 
			sb.append("),");
		}
		sb.append("]");
		return sb.toString();
	}
	
	@Override
	public void processChangeDescription(ChangeDescription changeDescription, Map<String, Object> context) {
		for (Map.Entry<EObject, EList<FeatureChange>> entry : changeDescription.getObjectChanges().entrySet()) {
			if (logger.isDebugEnabled()) {
				logger.debug("Processing change description for object = {}, feature changes = {}", entry.getKey(), printFeatureChanges(entry.getValue()));
			}
			
			processFeatureChangesForNotationElement(entry, context);
			processFeatureChangesForDiagrammableElement(entry, context);
		}
	}
	
}
