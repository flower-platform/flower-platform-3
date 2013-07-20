package com.crispico.flower.mp.codesync.code.adapter;

import java.util.List;

import com.crispico.flower.mp.codesync.base.ModelAdapterFactory;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactorySet;
import com.crispico.flower.mp.codesync.merge.SyncElementModelAdapter;
import com.crispico.flower.mp.model.codesync.AstCacheElement;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * Mapped to {@link CodeSyncElement}. Delegates to the internal {@link #astCacheElementModelAdapter}.
 * 
 * @author Mariana
 */
public class CodeSyncElementModelAdapter extends SyncElementModelAdapter {

	protected SyncElementModelAdapter astCacheElementModelAdapter;
	
	public CodeSyncElementModelAdapter() {
		// nothing to do
	}
	
	public CodeSyncElementModelAdapter(SyncElementModelAdapter adapter) {
		this.astCacheElementModelAdapter = adapter;
	}
	
	@Override
	public String getLabel(Object modelElement) {
		if (astCacheElementModelAdapter != null) {
			return astCacheElementModelAdapter.getLabel(modelElement);
		}
		return super.getLabel(modelElement);
	}
	
	@Override
	public List<String> getIconUrls(Object modelElement) {
		if (astCacheElementModelAdapter != null) {
			return astCacheElementModelAdapter.getIconUrls(modelElement);
		}
		return super.getIconUrls(modelElement);
	}
	
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (astCacheElementModelAdapter != null) {
			return astCacheElementModelAdapter.getContainmentFeatureIterable(element, feature, correspondingIterable);
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (astCacheElementModelAdapter != null) {
			return astCacheElementModelAdapter.getValueFeatureValue(element, feature, correspondingValue);
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
	
	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild) {
		if (astCacheElementModelAdapter != null) {
			return astCacheElementModelAdapter.createChildOnContainmentFeature(element, feature, correspondingChild);
		}
		return super.createChildOnContainmentFeature(element, feature, correspondingChild);
	}

	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		if (astCacheElementModelAdapter != null) {
			astCacheElementModelAdapter.removeChildrenOnContainmentFeature(parent, feature, child);
		} else {
			super.removeChildrenOnContainmentFeature(parent, feature, child);
		}
	}
	
	@Override
	public void setValueFeatureValue(Object element, Object feature, Object newValue) {
		if (astCacheElementModelAdapter != null) {
			astCacheElementModelAdapter.setValueFeatureValue(element, feature, newValue);
		} else {
			super.setValueFeatureValue(element, feature, newValue);
		}
	}
	
	@Override
	public Object createCorrespondingModelElement(Object element) {
		if (astCacheElementModelAdapter != null) {
			return astCacheElementModelAdapter.createCorrespondingModelElement(element);
		}
		return super.createCorrespondingModelElement(element);
	}
	
	@Override
	public void addToResource(AstCacheElement element) {
		if (astCacheElementModelAdapter != null) {
			astCacheElementModelAdapter.addToResource(element);
		} else {
			super.addToResource(element);
		}
	}
	
	@Override
	public ModelAdapterFactorySet getModelAdapterFactorySet() {
		if (astCacheElementModelAdapter != null) {
			return astCacheElementModelAdapter.getModelAdapterFactorySet();
		}
		return super.getModelAdapterFactorySet();
	}
	
	@Override
	public ModelAdapterFactory getModelAdapterFactory() {
		if (astCacheElementModelAdapter != null) {
			return astCacheElementModelAdapter.getModelAdapterFactory();
		}
		return super.getModelAdapterFactory();
	}

	@Override
	public ModelAdapterFactory getEObjectConverter() {
		if (astCacheElementModelAdapter != null) {
			return astCacheElementModelAdapter.getEObjectConverter();
		}
		return super.getEObjectConverter();
	}
	
	@Override
	public Object getMatchKey(Object element) {
		if (astCacheElementModelAdapter != null) {
			return astCacheElementModelAdapter.getMatchKey(element);
		}
		return super.getMatchKey(element);
	}
}
