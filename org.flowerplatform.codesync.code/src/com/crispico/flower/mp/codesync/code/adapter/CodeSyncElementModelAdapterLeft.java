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
package com.crispico.flower.mp.codesync.code.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.crispico.flower.mp.codesync.base.CodeSyncAlgorithm;
import com.crispico.flower.mp.codesync.base.FilteredIterable;
import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.action.ActionResult;
import com.crispico.flower.mp.codesync.merge.SyncElementModelAdapter;
import com.crispico.flower.mp.model.codesync.AstCacheElement;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.FeatureChange;

/**
 * @author Mariana
 */
public class CodeSyncElementModelAdapterLeft extends CodeSyncElementModelAdapter {

	public CodeSyncElementModelAdapterLeft() {
		super();
	}
	
	public CodeSyncElementModelAdapterLeft(SyncElementModelAdapter modelAdapter) {
		super(modelAdapter);
	}

	@Override
	public Object getMatchKey(Object element) {
		CodeSyncElement codeSyncElement = (CodeSyncElement) element;
		FeatureChange change = codeSyncElement.getFeatureChanges().get(CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name());
		if (change != null) {
			return change.getNewValue();
		}
		return super.getMatchKey(element);
	}

	/**
	 * Filters out deleted {@link CodeSyncElement}s. Returns the new containment list from the {@link FeatureChange}s map for
	 * the <code>feature</code>, if it exists. Also recreates the children from <code>correspondingIterable</code> from the 
	 * right side, in case the AST cache was deleted.
	 */
	@Override
	public Iterable<?> getContainmentFeatureIterable(final Object element, Object feature, Iterable<?> correspondingIterable) {
		Iterable<?> list = super.getContainmentFeatureIterable(element, feature, correspondingIterable);
		List<Object> result = (List<Object>) list;
		// if the AST cache was deleted, recreate the children using the corresponding iterable from the right side
		if (isUndefinedList(list)) {
			result = new ArrayList<Object>();
			for (Object correspondingElement : correspondingIterable) {
				result.add(createChildOnContainmentFeature(element, feature, correspondingElement));
			}
		}
		// get the children from the FeatureChange, if it exists
		FeatureChange change = getFeatureChange(element, feature);
		if (change != null) {
			result = (List<Object>) change.getNewValue();
		}
		
		// filter out deleted elements
		return new FilteredIterable<Object, Object>((Iterator<Object>) result.iterator()) {
			protected boolean isAccepted(Object candidate) {
				if (candidate instanceof CodeSyncElement && ((CodeSyncElement) candidate).isDeleted())
					return false;
				return true;
			}
		
		};
	}
	
	/**
	 * Returns the new value from the {@link FeatureChange}s map for the <code>feature</code>, if it exists.
	 * Also sets the <code>correspondingValue</code> from the right side, in case the AST cache was deleted.
	 */
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		// if the AST cache was deleted, recreate the value using the corresponding value from the right side
		Object value = astCacheElementModelAdapter != null 
				? astCacheElementModelAdapter.getValueFeatureValue(element, feature, correspondingValue)
				: super.getValueFeatureValue(element, feature, correspondingValue);
		if (CodeSyncAlgorithm.UNDEFINED.equals(value)) {
			setValueFeatureValue(element, feature, correspondingValue);
		}
		// get the value from the FeatureChange, if it exists
		FeatureChange change = getFeatureChange(element, feature);
		if (change != null) {
			return change.getNewValue();
		}
		return value;
	}
	
	/**
	 * Before the features are processed for <code>element</code>, checks if the AST cache was deleted, and 
	 * recreates it. Note: we cannot do this while the features are processed because upon requesting the 
	 * value for a 2nd feature, the AST cache will be refreshed again, thus losing the value for a previously
	 * processed feature.
	 */
	@Override
	public void beforeFeaturesProcessed(Object element, Object correspondingElement) {
		CodeSyncElement cse = getCodeSyncElement(element);
		if (cse != null) {
			if (cse.getAstCacheElement() == null || cse.getAstCacheElement().eResource() == null) {
				AstCacheElement ace = (AstCacheElement) createCorrespondingModelElement(correspondingElement);
				cse.setAstCacheElement(ace);
			}
		}
	}

	/**
	 * Adds the {@link AstCacheElement} to the AST cache resource.
	 */
	@Override
	public void featuresProcessed(Object element) {
		CodeSyncElement cse = getCodeSyncElement(element);
		if (cse != null) {
			AstCacheElement ace = cse.getAstCacheElement();
			if (ace != null && ace.eResource() == null) {
				addToResource(ace);
			}
		}
	}

	@Override
	public void actionPerformed(Object element, Object feature, ActionResult result) {
		if (result == null || result.conflict) {
			return;
		}
		
		CodeSyncElement cse = getCodeSyncElement(element);
		if (cse != null) {
			FeatureChange change = cse.getFeatureChanges().get(feature);
			if (change != null) {
				int featureType = getModelAdapterFactorySet().getFeatureProvider(cse).getFeatureType(feature);
				if (featureType == IModelAdapter.FEATURE_TYPE_VALUE) {
					actionPerformed_ValueFeature(cse, feature, change);
				} else {
					if (featureType == IModelAdapter.FEATURE_TYPE_CONTAINMENT) {
						actionPerformed_ContainmentFeature(cse, feature, change, result);
					}
				}
			} else {
				List<Object> children = (List<Object>) super.getContainmentFeatureIterable(element, feature, null);
				Object child = findChild(children, result.childMatchKey);
				if (child != null && child instanceof CodeSyncElement) {
					if (result.childAdded) {
						((CodeSyncElement) child).setAdded(false);
					} else {
						children.remove(child);
					}
				}
			}
			boolean sync = cse.getFeatureChanges().size() == 0;
			if (cse.getChildren().size() == 0) {
				cse.setChildrenSynchronized(true);
			}
			if (sync != cse.isSynchronized()) {
				cse.setSynchronized(sync);
				if (cse.eContainer() instanceof CodeSyncElement) {
					setChildrenSync((CodeSyncElement) cse.eContainer());
				}
			}
		}
	}
	
	/**
	 * Removes the feature change after the feature was processed.
	 */
	private void actionPerformed_ValueFeature(CodeSyncElement element, Object feature, FeatureChange change) {
//		setValueFeatureValue(element, feature, change.getNewValue());
		element.getFeatureChanges().removeKey(feature);
	}
	
	/**
	 * Compares the actual value of the containment feature to the value from the feature changes map;
	 * removes the entry if the new value is set on the model element.
	 */
	private void actionPerformed_ContainmentFeature(CodeSyncElement element, Object feature, FeatureChange change, ActionResult result) {
		List<Object> children = (List<Object>) super.getContainmentFeatureIterable(element, feature, null);
		List<Object> newValues = (List<Object>) change.getNewValue();
		if (result.childAdded) {
			actionPerformed_ChildAdded(children, newValues, result);
		} else if (result.childModified) {
			actionPerformed_ChildModified(children, newValues, result);
		} else {
			actionPerformed_ChildRemoved(children, newValues, result);
		}
		if (listEquals(children, newValues)) {
			element.getFeatureChanges().removeKey(feature);
		}
	}
	
	private void actionPerformed_ChildAdded(List<Object> children, List<Object> newValues, ActionResult result) {
		Object existingChild = findChild(children, result.childMatchKey);
		if (existingChild == null) {
			// child added to source from new children => add it to the actual children as well
			Object newChild = findChild(newValues, result.childMatchKey);
			if (newChild != null) {
				children.add(newChild instanceof EObject ? EcoreUtil.copy((EObject) newChild) : newChild);
			}
		} else {
			// child added to model from source => add it to the new values as well
			Object newChild = findChild(newValues, result.childMatchKey);
			if (newChild == null) {
				newValues.add(existingChild instanceof EObject ? EcoreUtil.copy((EObject) existingChild) : existingChild);
			}
		}
	}
	
	private void actionPerformed_ChildRemoved(List<Object> children, List<Object> newValues, ActionResult result) {
		Object existingChild = findChild(children, result.childMatchKey);
		if (existingChild != null) {
			// child removed from source => remove it from the actual children as well
			children.remove(existingChild);
		} else {
			Object newChild = findChild(newValues, result.childMatchKey);
			if (newChild != null) {
				// child removed from model => remove it from the new children as well
				newValues.remove(newChild);
			}
		}
	}
	
	private void actionPerformed_ChildModified(List<Object> children, List<Object> newValues, ActionResult result) {
		Object existingChild = findChild(children, result.childMatchKey);
		if (existingChild != null) {
			Object newChild = findChild(newValues, result.childMatchKey);
			IModelAdapter adapter = getModelAdapterFactory().getModelAdapter(existingChild);
			Object feature = result.modifiedChildFeature;
			adapter.setValueFeatureValue(existingChild, feature, adapter.getValueFeatureValue(newChild, feature, null));
		}
	}
	
	private void setChildrenSync(CodeSyncElement element) {
		boolean childrenSync = true;
		for (CodeSyncElement child : element.getChildren()) {
			if (!child.isSynchronized() || !child.isChildrenSynchronized()) {
				childrenSync = false;
				break; // found one child that is not sync
			}
		}
		boolean oldChildrenSync = element.isChildrenSynchronized();
		if (oldChildrenSync != childrenSync) {
			// set new childrenSync status and go up on the parent
			element.setChildrenSynchronized(childrenSync);
			CodeSyncElement parent = (CodeSyncElement) element.eContainer();
			if (parent != null) {
				setChildrenSync(parent);
			}
		}
	}
	
	/**
	 * Needed because some implementations of {@link EList} do not use equals.
	 */
	private boolean listEquals(List<Object> list1, List<Object> list2) {
		if (list1 == list2) {
			return true;
		}
		if (list1.size() != list2.size()) {
			return false;
		}
		for (Object o1 : list1) {
			boolean containsObject = false;
			for (Object o2 : list2) {
				if (o1.equals(o2)) {
					containsObject = true;
					break;
				}
			}
			if (!containsObject) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks if the <code>list</code> contains the <code>child</code> based on its match key.
	 */
	private Object findChild(List<Object> list, Object matchKey) {
		if (matchKey == null)
			return null;
		for (Object existingChild : list) {
			if (matchKey.equals(getModelAdapterFactory().getModelAdapter(existingChild).getMatchKey(existingChild))) {
				return existingChild;
			}
		}
		return null;
	}
	
}