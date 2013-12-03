/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.editor.model.change_processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.ChangeKind;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.eclipse.emf.ecore.change.ListChange;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.emf_model.notation.NotationElement;
import org.flowerplatform.emf_model.notation.NotationPackage;
import org.flowerplatform.emf_model.notation.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiagramUpdaterChangeProcessor implements IChangeProcessor {

	private static final Logger logger = LoggerFactory.getLogger(DiagramUpdaterChangeProcessor.class);
	
	// TODO CS/CS3 vizib public de test
	public Map<String, List<IDiagrammableElementFeatureChangesProcessor>> diagrammableElementFeatureChangeProcessors = new HashMap<String, List<IDiagrammableElementFeatureChangesProcessor>>();
	
	public void addDiagrammableElementFeatureChangeProcessor(String viewType, IDiagrammableElementFeatureChangesProcessor processor) {
		List<IDiagrammableElementFeatureChangesProcessor> processors = diagrammableElementFeatureChangeProcessors.get(viewType);
		if (processors == null) {
			diagrammableElementFeatureChangeProcessors.put(viewType, new ArrayList<IDiagrammableElementFeatureChangesProcessor>());
		}
		diagrammableElementFeatureChangeProcessors.get(viewType).add(processor);
	}
	
	public List<IDiagrammableElementFeatureChangesProcessor> getDiagrammableElementFeatureChangesProcessors(String viewType) {
		return diagrammableElementFeatureChangeProcessors.get(viewType);
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
	
	protected void processAddedOrRemovedObjectRecursive(EObject object, DiagramUpdaterChangeProcessorContext diagramUpdaterChangeDescriptionProcessingContext, Map<String, Object> context, boolean added, StringBuffer debugStringBuffer) {
		for (EObject child : object.eContents()) {
			processAddedOrRemovedObjectRecursive(child, diagramUpdaterChangeDescriptionProcessingContext, context, added, debugStringBuffer);
		}
		if (debugStringBuffer != null) {
			debugStringBuffer.append(object);
			debugStringBuffer.append(',');
		}
		if (added) {
			diagramUpdaterChangeDescriptionProcessingContext.getObjectsToUpdate().add(object);
			if (object instanceof View) {
				View view = (View) object;
				List<IDiagrammableElementFeatureChangesProcessor> processors = EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().getDiagrammableElementFeatureChangesProcessors(view.getViewType());
				if (processors != null) {
					for (IDiagrammableElementFeatureChangesProcessor processor : processors) {
						processor.processFeatureChanges(view.getDiagrammableElement(), null, view, context);
					}
				}
			}
		} else {			
			// removed
			diagramUpdaterChangeDescriptionProcessingContext.getObjectIdsToDispose().add(((NotationElement) object).getIdBeforeRemoval());
			diagramUpdaterChangeDescriptionProcessingContext.getObjectsToDispose().add(object);
		}
	}
	
	
	protected void addNewlyAddedObject(EObject object, DiagramUpdaterChangeProcessorContext diagramUpdaterChangeDescriptionProcessingContext, Map<String, Object> context) {
		StringBuffer debugStringBuffer = null;
		if (logger.isDebugEnabled()) {
			debugStringBuffer = new StringBuffer();
		}
		
		// we use this recursive method (instead of a TreeIterator/object.eAllContens()), because we prefer to have the
		// children before the parents. This may be convenient during deserialization on Flex: when we deserialize an object,
		// we can use its child references right away. At the moment of writing, this was not a MUST, but this may be helpful 
		// in the future.
		processAddedOrRemovedObjectRecursive(object, diagramUpdaterChangeDescriptionProcessingContext, context, true, debugStringBuffer);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Added newly added objects = [{}]", debugStringBuffer);
		}
	}
	
	protected void addRemovedObject(EObject object, DiagramUpdaterChangeProcessorContext diagramUpdaterChangeDescriptionProcessingContext, Map<String, Object> context) {
		StringBuffer debugStringBuffer = null;
		if (logger.isDebugEnabled()) {
			debugStringBuffer = new StringBuffer();
		}
		
		processAddedOrRemovedObjectRecursive(object, diagramUpdaterChangeDescriptionProcessingContext, context, false, debugStringBuffer);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Added objects id to dispose for objects = [{}]", debugStringBuffer);
		}
	}

	/**
	 * @author Cristina Constantinescu
	 */
	protected void processFeatureChangesForNotationElement(Map.Entry<EObject, EList<FeatureChange>> entry, Map<String, Object> context) {
		if (!(entry.getKey() instanceof NotationElement)) {
			return;
		} 
		NotationElement notationElement = (NotationElement) entry.getKey();
		if (!isDiagramOpen(notationElement)) {
			return;
		}
		
		DiagramUpdaterChangeProcessorContext diagramUpdaterChangeDescriptionProcessingContext = DiagramUpdaterChangeProcessorContext.getDiagramUpdaterChangeDescriptionProcessingContext(context, true);
		
		boolean diagrammableElementWasSetToNull = false;
		for (FeatureChange featureChange : entry.getValue()) {
//			if (FlowerDiagramNotationPackage.eINSTANCE.getDiagramNotationElement_IdBeforeRemoval().equals(featureChange.getFeature())) {
//				// TODO CS/CS3 idBeforeRemoval: de hotarat; deocamdata pot spune ca daca modific aici, sigur pot sa ignor si celelalte
//				return;
//			}
			if (featureChange.getFeature().equals(NotationPackage.eINSTANCE.getView_DiagrammableElement()) && notationElement.eGet(featureChange.getFeature()) == null) {
				diagrammableElementWasSetToNull = true;				
			}
			if (featureChange.getFeature() instanceof EReference && ((EReference) featureChange.getFeature()).isContainment()) {
				if (!featureChange.getFeature().isMany()) {
					// TODO CS/CS3 LOW de prelucrat si cazul in care avem containment pe feature care nu e many
					throw new UnsupportedOperationException("Containment feature that is not many is not yet supported; feature = " + featureChange.getFeatureName());
				} else {
					// containment & many
//					if (featureChange.getListChanges().isEmpty()) {
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
								addNewlyAddedObject(mostProbablyNewlyAddedElement, diagramUpdaterChangeDescriptionProcessingContext, context);
								// TODO CS/CS3 LOW De testat pentru cazul setList(null), sau setList(ceva): cred ca tot aici picam, si tr sa raportam niste obiecte suplimentare ca sterse;
								// de asemenea de testat si ca cazul de mai sus, pt. add in care avem o lista noua care contine si elemente care erau in lista veche; eu l-am copiat, insa
								// nu l-am vazut cu ochii mei.
							}
						}
//					} else {
						// normal case: the list was not empty before adding
						for (ListChange listChange : featureChange.getListChanges()) {
							// TODO pentru cazul in care avem mai multe listChanges datorate la mai multe add-uri in lista, nu gasim corect elementele noi => de vazut
							// cum interpretam corect cazul acesta.
							if (listChange.getKind() == ChangeKind.REMOVE_LITERAL) {
//								// an element was added; the recording only indicates the index, so we need to look it up
//								// by ourselves
//								@SuppressWarnings("unchecked")
//								EObject newlyAddedObject = ((List<EObject>) notationElement.eGet(featureChange.getFeature())).get(listChange.getIndex());
//								addNewlyAddedObject(newlyAddedObject, diagramUpdaterChangeDescriptionProcessingContext, context);
							} else if (listChange.getKind() == ChangeKind.ADD_LITERAL) {
								// an element was removed
								for (EObject removedObject : listChange.getReferenceValues()) {
									addRemovedObject(removedObject, diagramUpdaterChangeDescriptionProcessingContext, context);
								}
							}
						}
//					}
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Adding object to update = {}", notationElement);
		}
		if (diagrammableElementWasSetToNull) {		
			return;
		}
		
		// only if the element was not removed; in theory this check shouldn't be needed
		// however using binary serialization, the idBeforeRemoval is not set when the resource is loaded
		// so we need to set it manually, which triggers a change that is being processed here
		if (notationElement.eResource() != null) {
			if (!diagramUpdaterChangeDescriptionProcessingContext.getObjectsToUpdate().contains(notationElement)) {			
				if (notationElement instanceof View) {
					View view = (View) notationElement;
					List<IDiagrammableElementFeatureChangesProcessor> processors = EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().getDiagrammableElementFeatureChangesProcessors(view.getViewType());
					if (processors != null) {
						for (IDiagrammableElementFeatureChangesProcessor processor : processors) {
							// view.getDiagrammableElement() is null for diagram, so the if was removed
							// be aware: there were cases when processFeatureChanges had crashed if null was sent
							// as view.getDiagrammableElement()
							processor.processFeatureChanges(view.getDiagrammableElement(), null, view, context);	
						}
					}
				}			
			} else {
				diagramUpdaterChangeDescriptionProcessingContext.getObjectsToUpdate().remove(notationElement);
			}			
			diagramUpdaterChangeDescriptionProcessingContext.getObjectsToUpdate().add(notationElement);	
		}
	}

	protected void processFeatureChangesForDiagrammableElement(Map.Entry<EObject, EList<FeatureChange>> entry, Map<String, Object> context) {
		ECrossReferenceAdapter adapter = ECrossReferenceAdapter.getCrossReferenceAdapter(entry.getKey());
		EObject diagrammableElement = entry.getKey();
				
		for (Setting setting : adapter.getNonNavigableInverseReferences(diagrammableElement)) {
			if (NotationPackage.eINSTANCE.getView_DiagrammableElement().equals(setting.getEStructuralFeature())) {
				View view = (View) setting.getEObject();
				List<IDiagrammableElementFeatureChangesProcessor> processors = getDiagrammableElementFeatureChangesProcessors(view.getViewType());
				if (processors != null) {
					if (logger.isDebugEnabled()) {
						logger.debug("Delegating to IDiagrammableElementFeatureChangesProcessor for view = {}, object = {}", view, diagrammableElement);
					}
					for (IDiagrammableElementFeatureChangesProcessor processor : processors) {					
						processor.processFeatureChanges(diagrammableElement, entry.getValue(), view, context);						
					}
				}
			}			
		}
//			if (isDiagramOpen(view)) {
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