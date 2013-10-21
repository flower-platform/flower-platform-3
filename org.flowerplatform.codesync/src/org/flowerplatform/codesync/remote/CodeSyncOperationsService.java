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
package org.flowerplatform.codesync.remote;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.flowerplatform.codesync.feature_converter.CodeSyncElementFeatureValueConverter;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.AstCacheElement;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncFactory;
import com.crispico.flower.mp.model.codesync.FeatureChange;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncOperationsService {

	public CodeSyncElement create(String codeSyncType) {
		CodeSyncElement codeSyncElement = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
		codeSyncElement.setType(codeSyncType);
		// test name
		codeSyncElement.setName(codeSyncType);
		return codeSyncElement;
	}
	
	public void add(CodeSyncElement parent, CodeSyncElement elementToAdd) {
		elementToAdd.setAdded(true);
		parent.getChildren().add(elementToAdd);
		propagateParentSyncFalse(elementToAdd);
	}
	
	/**
	 * Delegates to registered {@link CodeSyncElementFeatureValueConverter}s.
	 */
	public Object getFeatureValue(CodeSyncElement codeSyncElement, String feature) {
		List<CodeSyncElementFeatureValueConverter> converters = 
				CodeSyncPlugin.getInstance().getCodeSyncElementFeatureConverters();
		for (CodeSyncElementFeatureValueConverter converter : converters) {
			if (converter.getFeature(feature) != null) {
				return converter.getValue(codeSyncElement, feature);
			}
		}
		return null;
	}
	
	/**
	 * Returns the value of <code>feature</code> on the <code>codeSyncElement</code>, first from the
	 * list of {@link org.eclipse.emf.ecore.change.FeatureChange}s, if it exists.
	 */
	public Object getFeatureValue(CodeSyncElement codeSyncElement, EStructuralFeature feature) {
		FeatureChange featureChange = codeSyncElement.getFeatureChanges().get(feature);
		if (featureChange != null) {
			return featureChange.getNewValue();
		}
		
		if (feature.getEContainingClass().isSuperTypeOf(codeSyncElement.eClass())) {
			return codeSyncElement.eGet(feature);
		} else {
			AstCacheElement astElement = codeSyncElement.getAstCacheElement();
			if (astElement != null) {
				return astElement.eGet(feature);
			}
		}
	
		return null;
	}
	
	public Object getOldFeatureValue(CodeSyncElement codeSyncElement, EStructuralFeature feature) {
		FeatureChange featureChange = codeSyncElement.getFeatureChanges().get(feature);
		if (featureChange != null) {
			return featureChange.getOldValue();
		} else {
			return getFeatureValue(codeSyncElement, feature);
		}
	}
	
	/**
	 * Delegates to registered {@link CodeSyncElementFeatureValueConverter}s.
	 */
	public void setFeatureValue(CodeSyncElement codeSyncElement, String feature, Object newValue) {
		List<CodeSyncElementFeatureValueConverter> converters = 
				CodeSyncPlugin.getInstance().getCodeSyncElementFeatureConverters();
		for (CodeSyncElementFeatureValueConverter converter : converters) {
			if (converter.getFeature(feature) != null) {
				converter.setValue(codeSyncElement, feature, newValue);
			}
		}
	}
	
	public void setFeatureValue(CodeSyncElement codeSyncElement, EStructuralFeature feature, Object newValue) {
		Object oldValue = getOldFeatureValue(codeSyncElement, feature);
		if (!codeSyncElement.isAdded()) {
			createAndAddFeatureChange(codeSyncElement, feature, oldValue, newValue);
		} else {
			setFeatureValueDirectly(codeSyncElement, feature, newValue);
		}
	}
	
	/**
	 * Creates and adds a {@link FeatureChange} if the <code>oldValue</code> and <code>newValue</code> are different. 
	 * 
	 * Important: we first remove, and then add a new feature change to trigger a change description on the {@link CodeSyncElement},
	 * so the processors can update the views.
	 * 
	 * @author Sebastian Solomon
	 */
	protected void createAndAddFeatureChange(CodeSyncElement element,
			EStructuralFeature feature, Object oldValue, Object newValue) {
		element.getFeatureChanges().removeKey(feature);
		if (!equal(newValue, oldValue)) {
			FeatureChange featureChange = CodeSyncFactory.eINSTANCE
					.createFeatureChange();
			featureChange.setOldValue(oldValue);
			featureChange.setNewValue(newValue);
			element.getFeatureChanges().put(feature, featureChange);

			if (element.isSynchronized()) {
				element.setSynchronized(false);
				propagateParentSyncFalse(element);
			}

		} else if (element.getFeatureChanges().size() == 0) {
			propagateParentSyncTrue(element);
		}
	}
	
	/**
	 * @author Sebastian Solomon
	 */
	public void propagateParentSyncFalse(CodeSyncElement element) {
		while (element.eContainer() != null) {

			EObject parent = element.eContainer();
			if (parent instanceof CodeSyncElement) {
				element = (CodeSyncElement) parent;
				if (element.isSynchronized()) {
					element.setChildrenSynchronized(false);
				}
			} else
				return;

		}

	}
	
	/**
	 * @author Sebastian Solomon
	 */
	public void propagateParentSyncTrue(CodeSyncElement element) {
		if (!element.isAdded() && !element.isDeleted()
				&& element.getFeatureChanges().size() == 0) {
			element.setSynchronized(true); // orange
			if (allChildrenGreen(element)) // if all childs green =>become green
				element.setChildrenSynchronized(true);
			// * walk whole parent hierarchy; set childrenSync = true if all
			// children are sync,not newly added not deleted
			while (element.eContainer() != null) {
				if (element.eContainer() instanceof CodeSyncElement) {
					element = (CodeSyncElement) element.eContainer();
					if (allChildrenGreen(element)) {
						element.setChildrenSynchronized(true);
					} else
						return; // if one child is notSync, return
				}
			}
		}
	}
	
	/**
	 * @author Sebastian Solomon
	 */
	private boolean allChildrenGreen(CodeSyncElement element) {
		for (CodeSyncElement cse : element.getChildren()) {
			if (cse.isAdded() || cse.isDeleted() || !cse.isSynchronized()
					|| !cse.isChildrenSynchronized())
				return false;
		}
		return true;
	}
	
	protected void setFeatureValueDirectly(CodeSyncElement codeSyncElement, EStructuralFeature feature, Object newValue) {
		if (feature.getEContainingClass().isSuperTypeOf(codeSyncElement.eClass())) {
			codeSyncElement.eSet(feature, newValue);
		} else {
			AstCacheElement astElement = codeSyncElement.getAstCacheElement();
			if (astElement != null) {
				astElement.eSet(feature, newValue);
			}
		}
	}
	
	protected boolean equal(Object a, Object b) {
		if (a == null) {
			return b == null;
		} else {
			return a.equals(b);
		}
	}
	
	public void markDeleted(CodeSyncElement element) {
		// TODO
	}
	
	/**
	 * @author Sebastian Solomon
	 */
	public void propagateOnChildDelete(CodeSyncElement cse) {

		for (CodeSyncElement child : cse.getChildren()) {
			child.setDeleted(true);
			propagateOnChildDelete(child);
		}

	}
	
	public List<String> getFeatures(String codeSyncType) {
		CodeSyncElementDescriptor descriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(codeSyncType);
		return descriptor.getFeatures();
	}
	
}
