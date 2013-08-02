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
package com.crispico.flower.mp.codesync.merge;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Contains an algorithm based on the CodeSync algorithm that is used 
 * when merging models during team work - (C-MRG).
 * 
 * <p>
 * It has been adapted, so that this class may be overridden and
 * used for other purposes: 
 * <ul>
 * 	<li>in the internal action that combines 2 duplicate elements (that
 * 		may result after the merge of 2 models that were not 100% in sync
 * 		with the code,
 * 	<li>when merging user libraries within the application - (CS-LIB).
 * </ul>
 * 
 * The logic in this class is intended for (C-MRG). Some parts of the
 * algorithm have been broken down into methods (instead of blocks of
 * code) in order to disable and/or customize blocks of code, for (CS-LIB).
 * 
 * <p> 
 * Although it uses the same concepts and principles as the CodeSync
 * algorithm, it was rewritten because we thought it was dangerous to
 * alter the CodeSync code (no unit tests, no clear spec; the "dam"
 * effect). However, on long term, this code will be merged with the
 * CodeSync code. 
 * 
 * <p>
 * There are activity diagrams that depict this algorithm.
 * 
 * @author Cristi
 * 
 */
public class ModelMerge {

//	/**
//	 * 
//	 * Replace the usual DeleteCommand to make sure validation errors annotations are also removed
//	 * preventing Dangling HRefExceptions to happen
//	 * 
//	 * @author Luiza
//	 */
//	public static class ModelMergeDeleteCommand extends FlowerDeleteCommand {
//		
//		public ModelMergeDeleteCommand(FlowerEditingDomain domain, Collection<?> collection) {
//			super(domain, collection);
//		}
//		
//		public ModelMergeDeleteCommand(EditingDomain domain, DeleteContext context) {
//			super(domain, context);
//		}
//		
//		protected boolean shouldNotRemoveInverseReference(EObject currentDeletedObject, Setting setting) {
//			return false; // remove all inverse references no exception
//		}
//		
//		protected boolean shouldRemoveReferencingEObject(EObject referencingEObject, EStructuralFeature eStructuralFeature) {
//			// remove validation errors along with the object
//			if(referencingEObject instanceof EAnnotation && "Validation error".equals(((EAnnotation)referencingEObject).getSource())) {
//				return true;
//			}
//			return super.shouldRemoveReferencingEObject(referencingEObject, eStructuralFeature);
//		}
//		
//		protected Command create(EditingDomain domain, Collection<?> collection) {
//			ModelMergeDeleteCommand cmd =  new ModelMergeDeleteCommand(domain, new DeleteContext(collection, ancestorAllContentsList, additionalInfo));
//			return cmd;
//		}
//	}
//	
	/**
	 * Needs to be initialized by the CodeSync metamodel with
	 * CodeSyncPackage.eINSTANCE.getTimeStampedSyncElement_SyncTimeStamp()
	 * This hack is done because ~.mp project cannot depend on ~.mp.codesync,
	 * and a more general approach, just for this small case, seems to be quite an overkill.
	 * 
	 */
	public static EStructuralFeature codeSyncTimeStampFeature;
//	
//	/**
//	 * Switch that can be used to show/hide "COPY" log messages for newly added elements.
//	 * This kind of operations are unlikely to cause problems and they pollute the log
//	 * considerably.
//	 * 
//	 * If enabled, this kind of entries are prefixed with ">>>".
//	 */
//	protected boolean logNewElementNestedAddAndCopy = false;
//	
//	/**
//	 * Used by <code>logWithTime()</code>.
//	 * 
//	 */
//	protected long previousTime;

//	/**
//	 * Variable used during the algorithm.
//	 * 
//	 * @see #setFeature()
//	 * @see #processDeferredOperations()
//	 * 
//	 */
//	protected List<DeferredReferenceToSetEntry> deferredReferenceToSetEntries = new ArrayList<DeferredReferenceToSetEntry>();
//	
//	/**
//	 * Variable used during the algorithm.
//	 * 
//	 * @see #processDeferredOperations()
//	 */
//	protected Set<EObject> deferredObjectToDelete = new HashSet<EObject>();
//
//	/**
//	 * Variable used during the algorithm.
//	 * 
//	 * @see #mergeStandardFeature()
//	 * @see #getAncestorResource()
//	 * 
//	 */
//	protected Resource ancestorResource;
//
//	/**
//	 * The classes which provides functionality for showing 
//	 * the merge mechanism log must implement this interface.
//	 * 
//	 * @author Cristina
//	 */
//	public interface ILogProvider {
//		/**
//		 * The method writes the given <code>message</code> in log file.
//		 * @param message
//		 */
//		void writeToLog(String message);
//	}
//	
//	/**
//	 * 
//	 */
//	protected ILogProvider logProvider;
//	
//	/**
//	 * @return the logProvider
//	 */
//	public ILogProvider getLogProvider() {
//		return logProvider;
//	}
//
//	/**
//	 * @param logProvider the logProvider to set
//	 * 
//	 */
//	public void setLogProvider(ILogProvider logProvider) {
//		this.logProvider = logProvider;
//	}
//
//	/**
//	 * @return The xmi:id of the element.
//	 * 
//	 */
//	protected Object getKeyForEObject(EObject object) {
//		return object.eResource().getURIFragment(object);
//	}
//
//	/**
//	 * External entry point for algorithm. Resets <code>deferredSetReferenceEntries</code>,
//	 * invokes the algorithm and invokes <code>processDeferredSetReferenceEntries</code>.
//	 * 
//	 * See <code>merge()</code> for additional doc.
//	 * 
//	 */
//	public boolean doMerge(EObject ancestor, EObject left, EObject right) {
//		deferredReferenceToSetEntries.clear();
//		this.ancestorResource = ancestor.eResource();
//		try {
//			if (!merge(ancestor, left, right, false))
//				return false;
//			processDeferredOperations();
//		} finally {
//			// this is necessary to avoid memory leak.
//			this.ancestorResource = null;
//		}
//		return true;
//	}
//	
//	/**
//	 * The algorithm's (recursive) entry point. 
//	 * 
//	 * @param ancestor A common ancestor of left and right. May be null, meaning that object was newly added in right. 
//	 * 		A new (corresponding) instance has been created in left, and a full copy is in progress.
//	 * @param left Contains MY modifications; it is also the destination of the merge.
//	 * @param right Contains OTHER's modifications (i.e. from SVN); the source of the merge.
//	 * @param leftWasDeleted If <code>true</code> then the processing looks that left was
//	 * 		deleted and we do the processing only to look for modifications (and to generate
//	 * 		conflicts).
//	 * @return <code>false</code> if conflicts were detected.
//	 * 
//	 */
//	protected boolean merge(EObject ancestor, EObject left, EObject right, boolean leftWasDeleted) {
//		for (EStructuralFeature feature : left.eClass().getEAllStructuralFeatures()) {
//			if (feature.isDerived() || feature.isTransient() ||	!feature.isChangeable())
//				continue; // filter out features that don't need to be processed
//			else if (feature instanceof EReference && ((EReference) feature).isContainment()) {
//				// feature with children
//				if (!mergeContainmentFeature(feature, ancestor, left, right, leftWasDeleted))
//					return false;
//			} else {
//				// "normal" feature
//				if (!mergeStandardFeature(feature, ancestor, left, right, leftWasDeleted))
//					return false;
//			}
//		}
//		return true;
//	}
//	
//	/**
//	 * Merges EAttributes or EReferences that are not containment.
//	 * 
//	 * @param ancestor May be null, meaning that object was newly added in right. 
//	 * 		A new (corresponding) instance has been created in left, and a full copy is in progress.
//	 * @return false if conflicts.
//	 * 
//	 */
//	protected boolean mergeStandardFeature(EStructuralFeature feature, EObject ancestor, EObject left, EObject right, boolean leftWasDeleted) {
//		Object leftValue = getFeature(left, feature);
//		Object rightValue = getFeature(right, feature);
//		Object ancestorValue = ancestor != null ? getFeature(ancestor, feature) : leftValue; 
//		if (!safeEquals(rightValue, leftValue) && !safeEquals(rightValue, ancestorValue)) 
//			if (!safeEquals(ancestorValue, leftValue)) {
//				// conflict
//				log("%s, feature %s: CONFLICT\n\tAncestor value:\t%s\n\tLeft value:\t%s\n\tRight value:\t%s", getFullyQualifiedName(left), feature.getName(), ancestorValue, leftValue, rightValue);
//				return false;
//			} else {
//				if (leftWasDeleted) {
//					// conflict
//					log("%s, feature %s: CONFLICT on left DELETE. The left element (or someone from the upper hierarchy) was deleted.\n\tAncestor value:\t%s\n\tLeft value:\t%s\n\tRight value:\t%s", getFullyQualifiedName(left), feature.getName(), ancestorValue, leftValue, rightValue);
//					return false;
//				}
//				// left == ancestor, so it was unchanged; we'll copy
//				if (!setFeature(left, feature, rightValue, ancestor != null)) {
//					// the reference set has been deferred
//					
//					// the algorithm might be optimized to get rid of ancestorResource
//					// but for the moment we need it, as ancestor might be null (by choice, used
//					// as flag for logging
//					
//					// this block might be disabled by overridding class
//					if (getAncestorResource() != null && getAncestorResource().getEObject(((Reference) rightValue).getXmiId()) != null) {
//						// CONFLICT the referenced object was deleted on left
//						log("%s, feature %s: reference beying COPIED right to left but left DELETE CONFLICT. The referenced left element (or someone from the upper hierarchy) was deleted.\n\tOld left value:\t%s\n\tNew left value:\t%s", getFullyQualifiedName(left), feature.getName(), getFeature(left, feature), rightValue);
//						return false;
//					}
//				}
//			}
//		return true;
//	}
//	
//	/**
//	 * By default invokes <code>eGet()</code>. There are special cases:
//	 * <ul>
//	 * 	<li>timeStamp feature is completely ignored
//	 * 	<li>clientDependency feature is ignored because has an opposite that
//	 * 		is already processed
//	 * 	<li>EReference that are single (or many with a single value), 
//	 * 		are wrapped within a <code>Reference</code>
//	 * 	<li>if is EReference, many and has several entries => ignored
//	 * </ul>
//	 * 
//	 */
//	@SuppressWarnings("unchecked")
//	protected Object getFeature(EObject object, EStructuralFeature feature) {
//		// TODO santier
//		// special cases
//		if (codeSyncTimeStampFeature.equals(feature) ||
//				UMLPackage.eINSTANCE.getNamedElement_ClientDependency().equals(feature) ||
////				UMLPackage.eINSTANCE.getAssociation_NavigableOwnedEnd().equals(feature) ||
//				feature instanceof EReference && feature.isMany() && ((EReference) feature).getEOpposite() != null && !((EReference) feature).getEOpposite().isMany())
//			return null;
//		if (feature instanceof EReference) {
//			Object value = object.eGet(feature);
//			if (feature.isMany())
//				if (((List<EObject>) value).size() > 1) {
////					throw new RuntimeException(String.format("%s feature %s has more that 1 references; this is not supported.", getFullyQualifiedName(object), feature));
////					System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>>>> " + feature.getName());
//					return null;
//				} else if (((List<EObject>) value).size() == 1) {
//					value = ((List<EObject>) value).get(0);
//				} else
//					value = null; // empty list
//			return new Reference((EObject) value);
//		}
//		return object.eGet(feature);
//	}
//	
//	/**
//	 * The default case: calls <code>EObject.eSet()</code>. There are some
//	 * exceptions however:
//	 * <ul>
//	 * 	<li>If the value is a <code>Reference</code>, the wrapped referenced object
//	 * 		is set ONLY if it exists in the resource; otherwise schedules a <code>
//	 * 		DeferredSetReferenceEntry</code>.
//	 * </ul>
//	 * 
//	 * @param ancestorExists Used together with <code>logNewElementNestedAddAndCopy</code>,
//	 * 		to control logging.
//	 * @see #processDeferredOperations()
//	 * 
//	 */
//	@SuppressWarnings("unchecked")
//	protected boolean setFeature(EObject object, EStructuralFeature feature, Object newValue, boolean ancestorExists) {
//		Object newValueForLog = newValue;
//		if (newValue instanceof Reference) {
//			Reference ref = (Reference) newValue;
//			if (ref.getReferencedObject() == null)
//				newValue = null; // the referenced object may be null
//			else {
//				newValue = object.eResource().getEObject(ref.getXmiId());
//				if (newValue == null) {
//					// the referenced object doesn't exist (yet) in the resource; setting it should be deferred
//					log("\t\tThe referenced object doesn't exist (yet) in the resource; deferring the feature set.");
//					deferredReferenceToSetEntries.add(new DeferredReferenceToSetEntry(object, feature, ref));
//					return false;
//				}
//			}
//		}
//		if (ancestorExists || logNewElementNestedAddAndCopy)
//			log("%s%s, feature %s: COPIED right to left\n\tOld left value:\t%s\n\tNew left value:\t%s", 
//					ancestorExists ? "" : ">>>", getFullyQualifiedName(object), feature.getName(), 
//					getFeature(object, feature), newValueForLog);
//		if (feature instanceof EReference && feature.isMany()) {
//			if (newValue == null)
//				throw new UnsupportedOperationException(String.format("%s feature %s is being set to null. This is not normal and might mean that the EReference has more than 1 values, which is not supported. ", getFullyQualifiedName(object), feature.getName()));
//			List<EObject> list = (List<EObject>) object.eGet(feature);
//			if (list.size() > 1) {
//				throw new UnsupportedOperationException(String.format("%s feature %s has more that 1 references; this is not supported.", getFullyQualifiedName(object), feature.getName()));
//			} else if (list.size() == 1) {
//				list.set(0, (EObject) newValue);
//			} else
//				list.add((EObject) newValue);
//		} else
//			object.eSet(feature, newValue);
//		return true;
//	}
//	
//	/**
//	 * Getter for <code>ancestorResource</code> used within
//	 * <code>mergeStandardFeature()</code> that might be disabled.
//	 * 
//	 */
//	protected Resource getAncestorResource() {
//		return ancestorResource;
//	}
//	
//	/**
//	 * Invoked at the end of the merge:
//	 * <ul>
//	 * 	<li>sets references that were deferred (postponed),
//	 * 	<li>deletes objects scheduled for delete.
//	 * </ul>
//	 * 
//	 * 
//	 */
//	protected void processDeferredOperations() {
//		log("Processing deferred set references...");
//		for (DeferredReferenceToSetEntry entry : deferredReferenceToSetEntries) 
//			if (!setFeature(entry.object, entry.feature, entry.reference, true))
//				throw new RuntimeException("A deferred set reference has failed. See the last log entry for details.");
//		deferredReferenceToSetEntries.clear();
//		log("Processing scheduled deletes...");
//		for (EObject object : deferredObjectToDelete) {
//			// a previous delete in the list might have cascaded a delete on an
//			// item that follows; that's why this check 
//			if (object.eResource() != null) {
//				log("\t%s is being DELETED (because it was deleted on right => scheduled for delete)", getFullyQualifiedName(object));
//				new ModelMergeDeleteCommand(FlowerEditingDomain.getFlowerEditingDomainFor(object), Collections.singleton(object)).executeNoUndo();
////				FlowerDeleteCommand.create(FlowerEditingDomain.getFlowerEditingDomainFor(object), object, true).executeNoUndo();
//			}
//		}
//		deferredObjectToDelete.clear();
//	}
//
//	/**
//	 * Merges EReferences that are containment:
//	 * 
//	 * <ul>
//	 * 	<li>matches right children with left children and processes deeper (recurses),
//	 * 	<li>detects newly added elements,
//	 * 	<li>removes deleted elements.
//	 * </ul>
//	 * 
//	 * For parameter description, see <code>merge()</code>.
//	 * 
//	 * @param feature May by many (list) or single (object).
//	 * 
//	 */
//	@SuppressWarnings("unchecked")
//	protected boolean mergeContainmentFeature(EStructuralFeature feature, EObject ancestor, EObject left, EObject right, boolean leftWasDeleted) {
//		List<EObject> leftList = getListFromContainmentFeature(left, feature);
//		// filter out features that are many and null
//		if (leftList == null)
//			return true;
//		// FILL_LEFT_MAP: fill a map with LEFT children 
//		Map<Object, EObject> leftChildrenMap = new HashMap<Object, EObject>();
//		for (EObject o : leftList)
//			leftChildrenMap.put(getKeyForEObject(o), o);
//		
//		// FILL_LEFT_DELETED_MAP: fill a map with chilren DELETED from LEFT
//		// externalized to be able to disable this step
//		Map<Object, EObject> childrenDeletedFromLeftMap = fillLeftDeletedMap(ancestor, feature, leftChildrenMap);
//		
//		// ITERATE_RIGHT: iterate on RIGHT children
//		for (EObject rightObject : getListFromContainmentFeature(right, feature)) {
//			EObject leftObject;
//			EObject ancestorObject = null;
//			// for a "normal" list, proceed with the algorithm (i.e. remove from the map)
//			if (leftList instanceof EList) {
//				leftObject = leftChildrenMap.remove(getKeyForEObject(rightObject));
//				if (ancestor != null) {
//					// externalized; see method doc
//					ancestorObject = getCorrespondingAncestor(ancestor, rightObject);
//				}
//			} else {
//				// otherwise (a single list, because the feature is not many), get the element directly
//				// even if the xmi:ids (keys) are different; the clearing the map is necessary to avoid
//				// detecting a delete conflict
//				leftObject = (EObject) left.eGet(feature);
//				if (ancestor != null)
//					ancestorObject = (EObject) ancestor.eGet(feature);
//				leftChildrenMap.clear();
//			}
//			if (leftObject != null) {
//				// a 3 match was found; dig deeper (recurse)
//				if (!merge(ancestorObject, leftObject, rightObject, leftWasDeleted))
//					return false;
//			} else {
//				EObject leftDeletedObject = childrenDeletedFromLeftMap.remove(getKeyForEObject(rightObject));
//				// the following block is NOT executed if left == ancestor
//				if (leftDeletedObject != null) {
//					// left was deleted; continue digging but the purpose is to find
//					// modifications; on first modification found => conflict raised
//					if (!merge(leftDeletedObject, leftDeletedObject, rightObject, true))
//						return false;
//				} else {
//					// right element newly added
//					// conflict; an addition was performed on right, but left was deleted (directly
//					// or indirectly by some parent in the hierarchy)
//					if (leftWasDeleted) {
//						log("%s.%s has been ADDED but is in DELETE CONFLICT. The left element (or someone from the upper hierarchy) was deleted.", getFullyQualifiedName(left), getName(rightObject));
//						return false;
//					}
//					
//					// ADD_OR_MOVE_OBJECT
//					if (!(Boolean) addNewObject(ancestor, left, feature, leftList, rightObject)[0])
//						return false;
//				}
//			}
//		}
//		
//		// ITERATE_REMAINING_LEFT_MAP: iterate objects that are still in the LEFT children map
//		// i.e. that were deleted on right (or newly added on left)
//		for (EObject leftObject : leftChildrenMap.values()) {
//			if (!processRemaingObjectInLeftMap(ancestor, leftObject, leftWasDeleted))
//				return false;
//		}
//		
//		// if deleted both on left and right, don't do nothing
//		
//		return true;
//	}
//
//	/**
//	 * Utility method used by <code>mergeContainmentFeature()</code> that
//	 * was externalized in order to be able to disable this block (if needed).
//	 * 
//	 * @return A map with elements deleted from left. See calling method for details.
//	 * 
//	 */
//	protected Map<Object, EObject> fillLeftDeletedMap(EObject ancestor, EStructuralFeature feature, Map<Object, EObject> leftChildrenMap) {
//		Map<Object, EObject> childrenDeletedFromLeftMap = new HashMap<Object, EObject>();
// 		if (ancestor != null) {
// 			for (EObject o : getListFromContainmentFeature(ancestor, feature))
//				if (leftChildrenMap.get(getKeyForEObject(o)) == null)
//					childrenDeletedFromLeftMap.put(getKeyForEObject(o), o);
// 		}
// 		return childrenDeletedFromLeftMap;
//	}
//	
//	/**
//	 * Utility method used by <code>mergeContainmentFeature()</code> that
//	 * was externalized in order to be able to alter this block (if needed).
//	 * 
//	 * @return A child of <code>parentAncestor</code> that corresponds to 
//	 * 		<code>object</code>. This implementation lookups globally
//	 * 		based on xmi:id.
//	 * 
//	 */
//	protected EObject getCorrespondingAncestor(EObject parentAncestor, EObject object) {
//		// TODO santier pentru breakpoint
//		try {
//			return parentAncestor.eResource().getEObject((String) getKeyForEObject(object));
//		} catch (Exception e) {
//			throw new RuntimeException("Temporary: should not happen; actually it happens when ancestor == null but left & right != null. See code...", e);
//		}
//	}
//
//	/**
//	 * Utility method used by <code>mergeContainmentFeature()</code> that
//	 * was externalized in order to be able to invoke this block (if needed)
//	 * separately.
//	 * 
//	 * <p>
//	 * It adds a new object and recurses in order to initialize it OR if a
//	 * corresponding element is found by xmi:id, it moves it and recurses to
//	 * continue the merge algorithm.
//	 * 
//	 * @return result[0] - boolean that indicates the conflict state (according to the
//	 * 		convention); result[1] the new (or moved) EObject 
//	 * 
//	 */
//	@SuppressWarnings("unchecked")
//	protected Object[] addNewObject(EObject ancestor, EObject left, EStructuralFeature feature, List<EObject> leftList, EObject rightObject) {
//		boolean isMove;
//		EObject existingAncestorWithSameId = null;
//
//		String newXmiId = rightObject.eResource().getURIFragment(rightObject);
//		// look for the new object within left
//		EObject newObject = left.eResource().getEObject(newXmiId);
//		if (newObject != null) {
//			// the new xmi:id of the element that we are adding in left already
//			// exists; that means it was moved (in right)
//			isMove = true;
//			
//			// this is not normally null for correct input files; if it's null, probably
//			// the ancestor file is not really the ancestor of left/right, but it doesn't
//			// really matter; the algorithm will consider the element as newly added
//			existingAncestorWithSameId = ancestorResource.getEObject(newXmiId);
//			// the object that we just found might be scheduled for delete	
//			deferredObjectToDelete.remove(newObject);
//			log("%s was moved to %s", getFullyQualifiedName(newObject), getFullyQualifiedName(rightObject));
//		} else {
//			isMove = false;
//			newObject = rightObject.eClass().getEPackage().getEFactoryInstance().create(rightObject.eClass());
//
//			if (ancestor != null || logNewElementNestedAddAndCopy)
//				log("%s%s.%s has been ADDED; copying it...", ancestor != null ? "" : ">>>", getFullyQualifiedName(left), getName(rightObject));
//		}
//		
//		if (leftList instanceof EList)
//			leftList.add(newObject); // for "many" features
//		else
//			left.eSet(feature, newObject); // for "single" features
//
//		if (!isMove)
//			((XMLResource) left.eResource()).setID(newObject, newXmiId);
//
//		// for !isMove case, existingAncestorWithSameId == null;
//		// we could have put newObject as ancestor and the result would have been
//		// the same; we put null however, to be able to determine (in the nested calls)
//		// that it is not a 3-way copy, but a "initialize new instance" copy; we need to 
//		// know this to be able to disable the logging in this case
//		return new Object[] {
//				merge(existingAncestorWithSameId, newObject, rightObject, false),
//				newObject
//		};
//	}
//	
//	/**
//	 * Utility method used by <code>mergeContainmentFeature()</code> that
//	 * was externalized in order to be able to alter this block (if needed).
//	 * 
//	 * <p>
//	 * Processes object that are still left in <code>leftMap</code> that may
//	 * be deleted on right or newly added on left. 
//	 * 
//	 */ 
//	protected boolean processRemaingObjectInLeftMap(EObject ancestor, EObject leftObject, boolean leftWasDeleted) {
//		EObject ancestorObject = getCorrespondingAncestor(ancestor, leftObject); 
//		if (ancestorObject != null) {
//			// an object with the same xmi:id exists in ancestor...
//			if (ancestorObject.eContainer() == ancestor) {
//				// ... and its parent is a child of ancestor => a match exists between 
//				// ancestor & left; right doesn't exist => object deleted from right
//				// recurse to find modifications (meaning conflicts)
//				if (!merge(ancestorObject, ancestorObject, leftObject, true))
//					return false;
//	
//				// no conflicts found, so the element can be deleted
//			
//				// the delete is deferred until the end of the algorithm; the associated
//				// activity diagram explains why
//				
//				// if leftWasDeleted, we don't report a conflict, but there is
//				// no need for a delete command (which, anyway, would operate on ancestor, 
//				// because when that flag == true => ancestor == left)
//				if (!leftWasDeleted) {
//					deferredObjectToDelete.add(leftObject);
//					log("%s is scheduled for DELETE (because it was deleted on right)", getFullyQualifiedName(leftObject));
//				}
//			}
//			// else the object exists somewhere in ancestor, but not in its original
//			// location => it was moved; do nothing
//		}
//		// else newly added on left; don't do anything
//		return true;
//	}
//
//	/**
//	 * Utility method for <code>mergeContainmentFeature()</code>.
//	 * This solution was adopted to keep the code more compact. Otherwise some duplication 
//	 * would be needed.
//	 * 
//	 * @return A list with children. If the feature is many, the result is the existing EList.
//	 * 		If the feature is single, a singleton list is returned.
//	 * 
//	 */
//	@SuppressWarnings("unchecked")
//	protected List<EObject> getListFromContainmentFeature(EObject object, EStructuralFeature feature) {
//		Object listCandidate = object.eGet(feature);
//		if (feature.isMany())
//			return (List<EObject>) listCandidate; // "many" feature
//		else if (listCandidate != null)
//			return Collections.singletonList((EObject) listCandidate); // "one" feature
//		else
//			return Collections.emptyList();
//	}
//	
	/**
	 * Uses <code>equals()</code> and works even if parameters are <code>null</code>.
	 * 
	 */
	public static boolean safeEquals(Object a, Object b) {
		if (a == null && b == null)
			return true;
		else if (a == null || b == null)
			return false;
		else
			return a.equals(b);
	}
	
	/**
	 * Used in logging.
	 * 
	 * @return The name of the element if it is a <code>NamedElement</code>
	 * 		or %eClassName% otherwise.
	 * 
	 */
	public static String getName(EObject object) {
		String name = null;
		if (object == null)
			return "null";
//		else if (object instanceof NamedElement)
//			name = ((NamedElement) object).getName();
//		
//		if (name == null) {
//			if (object instanceof View && ((View) object).getRefElement() != object) {
//				// second check is done because at the time of writing
//				// note connectors have themselves as refElement; this doesn't seem
//				// right and I created #3610
//				name = "%" + object.eClass().getName() + "[RefEl:" +  getName(((View) object).getRefElement()) + "]%";
//			}
//			else
				name = "%" + object.eClass().getName() + "%";
//		}
		return name;
	}
	
	/**
	 * Used in logging.
	 * 
	 * @param object Accepts a <code>null</code> object.
	 * @return The fully qualified name; uses <code>getName()</code>. 
	 * 
	 */
	public static String getFullyQualifiedName(EObject object) {
		if (object == null)
			return "null";
		EObject parent = object.eContainer();
		StringBuffer result = new StringBuffer(getName(object));
		while (parent != null) {
			result.insert(0, getName(parent) + ".");
			parent = parent.eContainer();
		}
		return result.toString();
	}
//	
//	/**
//	 * Works like <code>String.format()</code>, but if there are parameters
//	 * that have more than 1 line, they get truncated.
//	 * 
//	 * 
//	 */
//	protected void log(String format, Object ... params) {
//		for (int i = 0; i < params.length; i++) 
//			if (params[i] instanceof String) {
//				String string = (String) params[i];
//				int pos = string.indexOf('\n');
//				if (pos != -1) {
//					if (pos > 0 && '\r' == string.charAt(pos - 1))
//						pos--;
//					params[i] = string.substring(0, pos) + "...";
//				}
//			}
//		if (logProvider != null) {
//			logProvider.writeToLog(String.format(format, params) + "\n");
//		} else {
//			System.out.println(String.format(format, params));
//		}
//	}
//	
//	/**
//	 * 
//	 */
//	protected void logWithTime(String message) {
//		long currentTime = new Date().getTime();
//		if (logProvider != null) {
//			logProvider.writeToLog(String.format("[%5.2fs] ", ((float)(currentTime - previousTime)) / 1000));
//		} else {
//			System.out.print(String.format("[%5.2fs] ", ((float)(currentTime - previousTime)) / 1000));
//		}
//		previousTime = currentTime;
//		log(message);
//	}
//		
}

