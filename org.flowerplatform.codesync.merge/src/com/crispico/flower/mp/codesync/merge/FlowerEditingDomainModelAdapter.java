package com.crispico.flower.mp.codesync.merge;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;

import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.action.ActionResult;

public class FlowerEditingDomainModelAdapter implements IModelAdapter {

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
//		return Collections.singleton(FlowerMPUtils.getRootPackage(((FlowerEditingDomain) element).getMainResource()));
		return Collections.singleton(((Resource) element).getContents().get(1));
	}

	@Override
	public String getFeatureName(Object feature) {
		return null;
	}

	@Override
	public int getFeatureType(Object feature) {
		return IModelAdapter.FEATURE_TYPE_CONTAINMENT;
	}

	@Override
	public List<?> getFeatures(Object element) {
		return Collections.singletonList(null);
	}

	public Object getMatchKey(Object element) {
		return "Root";
	}

	@Override
	public void addToMap(Object element, Map<Object, Object> map) {
		map.put(getMatchKey(element), element);
	}

	@Override
	public Object removeFromMap(Object element, Map<Object, Object> leftOrRightMap, boolean isRight) {
		return leftOrRightMap.remove(getMatchKey(element));
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		return null;
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		return ((Resource) modelElement).getContents();
	}

	@Override
	public List<String> getIconUrls(Object modelElement) {
		return null;
	}

	@Override
	public String getLabel(Object modelElement) {
		return ((Resource) modelElement).getURI().lastSegment();
	}

	@Override
	public boolean hasChildren(Object modelElement) {
		return true;
	}
	
	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @author Mariana
	 */
	@Override
	public Object createCorrespondingModelElement(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @author Mariana
	 */
	@Override
	public boolean save(Object element) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @author Mariana
	 */
	@Override
	public boolean discard(Object element) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * @author Mariana
	 */
	@Override
	public void actionPerformed(Object element, Object feature, ActionResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allActionsPerformedForFeature(Object element, Object correspondingElement, Object feature) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allActionsPerformed(Object element, Object correspondingElement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeFeaturesProcessed(Object element, Object correspondingElement) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void featuresProcessed(Object element) {
		// TODO Auto-generated method stub
		
	}

}
