package com.crispico.flower.mp.codesync.merge;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.crispico.flower.mp.codesync.base.CodeSyncAlgorithm;
import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactorySet;
import com.crispico.flower.mp.codesync.base.action.ActionResult;

/**
 * @flowerModelElementId _syFsMK3bEeC2ycztxbwS7Q
 */
public class EObjectModelAdapter implements IModelAdapter {

	private ModelAdapterFactorySet modelAdapterFactorySet;
	
	@Override
	public ModelAdapterFactorySet getModelAdapterFactorySet() {
		return modelAdapterFactorySet;
	}
	
	@Override
	public IModelAdapter setModelAdapterFactorySet(ModelAdapterFactorySet modelAdapterFactorySet) {
		this.modelAdapterFactorySet = modelAdapterFactorySet;
		return this;
	}
	
	@Override
	public boolean hasChildren(Object modelElement) {
		return !((EObject) modelElement).eContents().isEmpty();
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		return ((EObject) modelElement).eContents();
	}

	@Override
	public String getLabel(Object modelElement) {
//		if (modelElement instanceof NamedElement)
//			return ((NamedElement) modelElement).getName();
//		else 
			return "%" + modelElement.getClass().getSimpleName() + "%";
	}

	@Override
	public List<String> getIconUrls(Object modelElement) {
//		return ImageURLFactory.INSTANCE.computeIconPathListForEObject((EObject) modelElement, false);
		return null;
	}

	/**
	 * @author Cristi
	 * @author Mariana
	 */
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		EObject object = (EObject) element;
		if (object.eResource() == null) {
			return getUndefinedList(correspondingIterable);
		}
		EStructuralFeature structuralFeature = (EStructuralFeature) feature;
		Object listCandidate = object.eGet(structuralFeature);
		if (structuralFeature.isMany())
			return (List) listCandidate; // "many" feature
		else if (listCandidate != null)
			return Collections.singletonList(listCandidate); // "one" feature
		else
			return Collections.emptyList();
	}
	
	/**
	 * @author Mariana
	 */
	protected Iterable<?> getUndefinedList(Iterable<?> result) {
		if (result == null)
			return Collections.emptyList();
		return Collections.nCopies(((Collection) result).size(), CodeSyncAlgorithm.UNDEFINED);
	}
	
	/**
	 * @author Mariana
	 */
	protected boolean isUndefinedList(Iterable<?> result) {
		List list = (List) result;
		if (list.size() == 0) {
			return false;
		}
		if (list.get(0).equals(CodeSyncAlgorithm.UNDEFINED)) {
			return true;
		}
		return false;
	}

//	@Override
//	public List<?> getFeatures(Object element) {
//		return ((EObject) element).eClass().getEAllStructuralFeatures();
//	}
//
//	@Override
//	public int getFeatureType(Object feature) {
//		EStructuralFeature structuralFeature = (EStructuralFeature) feature;
//		if (structuralFeature instanceof EReference && ((EReference) structuralFeature).isContainment())
//			return FEATURE_TYPE_CONTAINMENT;
//		else if (structuralFeature.isDerived() || 
//				structuralFeature.isTransient() ||	
//				!structuralFeature.isChangeable() ||
////				ModelMerge.codeSyncTimeStampFeature.equals(structuralFeature) ||
////				UMLPackage.eINSTANCE.getNamedElement_ClientDependency().equals(structuralFeature) ||
////			UMLPackage.eINSTANCE.getAssociation_NavigableOwnedEnd().equals(feature) ||
//				structuralFeature instanceof EReference && structuralFeature.isMany() && ((EReference) structuralFeature).getEOpposite() != null && !((EReference) structuralFeature).getEOpposite().isMany())
//			return FEATURE_TYPE_DONT_PROCESS; // filter out features that don't need to be processed
//		else
//			return FEATURE_TYPE_VALUE;
//
//	}
//	
//	public String getFeatureName(Object feature) {
//		EStructuralFeature structuralFeature = (EStructuralFeature) feature;
//		return structuralFeature.getName();
//	}

	public Object getMatchKey(Object element) {
		EObject object = (EObject) element;
		return object.eResource() == null ? null : object.eResource().getURIFragment(object);
	}

	@Override
	public void addToMap(Object element, Map<Object, Object> map) {
		map.put(getMatchKey(element), element);
	}

	@Override
	public Object removeFromMap(Object element, Map<Object, Object> leftOrRightMap, boolean isRight) {
		return leftOrRightMap.remove(getMatchKey(element));
	}

	/**
	 * @author Cristi
	 * @author Mariana
	 */
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		EStructuralFeature structuralFeature = (EStructuralFeature) feature;
		EObject object = (EObject) element;
		if (object.eResource() == null) {
			return CodeSyncAlgorithm.UNDEFINED;
		}
		if (feature instanceof EReference) {
			Object value = object.eGet(structuralFeature);
			if (structuralFeature.isMany())
				if (((List) value).size() > 1) {
//					throw new RuntimeException(String.format("%s feature %s has more that 1 references; this is not supported.", getFullyQualifiedName(object), feature));
//					System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>>>> " + feature.getName());
					return null;
				} else if (((List) value).size() == 1) {
					value = ((List) value).get(0);
				} else
					value = null; // empty list
			return new Reference((EObject) value);
		}
		return object.eGet(structuralFeature);
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object newValue) {
		Object newValueForLog = newValue;
		EObject object = (EObject) element;
		EStructuralFeature structuralFeature = (EStructuralFeature) feature;
		if (newValue instanceof Reference) {
			Reference ref = (Reference) newValue;
			if (ref.getReferencedObject() == null)
				newValue = null; // the referenced object may be null
			else {
				newValue = object.eResource().getEObject(ref.getXmiId());
				if (newValue == null) {
//					// the referenced object doesn't exist (yet) in the resource; setting it should be deferred
//					log("\t\tThe referenced object doesn't exist (yet) in the resource; deferring the feature set.");
//					deferredReferenceToSetEntries.add(new DeferredReferenceToSetEntry(object, feature, ref));
//					return false;
					throw new UnsupportedOperationException("Setting a reference that doens't exist on target.");
				}
			}
		}
//		if (ancestorExists || logNewElementNestedAddAndCopy)
//			log("%s%s, feature %s: COPIED right to left\n\tOld left value:\t%s\n\tNew left value:\t%s", 
//					ancestorExists ? "" : ">>>", getFullyQualifiedName(object), feature.getName(), 
//					getFeature(object, feature), newValueForLog);
		if (structuralFeature instanceof EReference && structuralFeature.isMany()) {
			if (newValue == null)
				return;
//				throw new UnsupportedOperationException(String.format("%s feature %s is being set to null. This is not normal and might mean that the EReference has more than 1 values, which is not supported. ", ModelMerge.getFullyQualifiedName(object), structuralFeature.getName()));
			List<EObject> list = (List<EObject>) object.eGet(structuralFeature);
			if (list.size() > 1) {
				throw new UnsupportedOperationException(String.format("%s feature %s has more that 1 references; this is not supported.", ModelMerge.getFullyQualifiedName(object), structuralFeature.getName()));
			} else if (list.size() == 1) {
				list.set(0, (EObject) newValue);
			} else
				list.add((EObject) newValue);
		} else
			object.eSet(structuralFeature, newValue);
	}

	/**
	 * @author Cristi
	 * @author Mariana
	 */
	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild) {
		EObject object = (EObject) element;
		EStructuralFeature structuralFeature = (EStructuralFeature) feature;
		Object newChild = createCorrespondingModelElement(correspondingChild);
		if (structuralFeature.isMany()) {
			((List<Object>) object.eGet(structuralFeature)).add(newChild);
		} else {
			object.eSet(structuralFeature, newChild);
		}
		return newChild;
	}

	/**
	 * @author Mariana
	 */
	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		EStructuralFeature structuralFeature = (EStructuralFeature) feature;
		EObject parentEObject = (EObject) parent;
		if (structuralFeature.isMany()) {
			((List<Object>) parentEObject.eGet(structuralFeature)).remove(child);
		} else {
			if (parentEObject.eGet(structuralFeature).equals(child)) {
				parentEObject.eSet(structuralFeature, null);
			}
		}
	}

	/**
	 * @author Mariana
	 */
	@Override
	public Object createCorrespondingModelElement(Object element) {
		EObject corresp = (EObject) element;
		EObject newObject = corresp.eClass().getEPackage().getEFactoryInstance().create(corresp.eClass());
		return newObject;
	}

	/**
	 * @author Mariana
	 */
	@Override
	public boolean save(Object element) {
		EObject eObject = (EObject) element;
		CodeSyncMergePlugin.getInstance().saveResource(eObject.eResource());
		return false;
	}
	
	/**
	 * @author Mariana
	 */
	@Override
	public boolean discard(Object element) {
		EObject eObject = (EObject) element;
		CodeSyncMergePlugin.getInstance().discardResource(eObject.eResource());
		return false;
	}
	
	/**
	 * @author Mariana
	 */
	@Override
	public void actionPerformed(Object element, Object feature, ActionResult result) {
		// nothing to do
	}

	/**
	 * @author Mariana
	 */
	@Override
	public void allActionsPerformedForFeature(Object element, Object correspondingElement, Object feature) {
		// nothing to do
	}

	/**
	 * @author Mariana
	 */
	@Override
	public void allActionsPerformed(Object element, Object correspondingElement) {
		for (Object feature : getModelAdapterFactorySet().getFeatureProvider(element).getFeatures(element)) {
			if (getModelAdapterFactorySet().getFeatureProvider(element).getFeatureType(feature) == FEATURE_TYPE_CONTAINMENT) {
				allActionsPerformedForFeature(element, correspondingElement, feature);
			}
		}
	}

	/**
	 * @author Mariana
	 */
	@Override
	public void beforeFeaturesProcessed(Object element, Object correspondingElement) {
		// nothing to do
	}
	
	/**
	 * @author Mariana
	 */
	@Override
	public void featuresProcessed(Object element) {
		// nothing to do
	}

}