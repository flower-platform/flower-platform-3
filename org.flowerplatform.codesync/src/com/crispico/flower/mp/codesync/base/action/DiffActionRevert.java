package com.crispico.flower.mp.codesync.base.action;

import com.crispico.flower.mp.codesync.base.Diff;
import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.Match;


/**
 * @flowerModelElementId _ms82cLQcEeCPJvLYgj5VDQ
 */
public class DiffActionRevert extends DiffAction {

	@Override
	public ActionResult execute(Match match, int diffIndex) {
		Diff diff = match.getDiffs().get(diffIndex);
		IModelAdapter ancestorModelAdapter = match.getEditableResource().getModelAdapterFactorySet().getAncestorFactory().getModelAdapter(match.getAncestor());
		IModelAdapter leftModelAdapter = null;
		if (diff.isLeftModified())
			leftModelAdapter = match.getEditableResource().getModelAdapterFactorySet().getLeftFactory().getModelAdapter(match.getLeft());
		IModelAdapter rightModelAdapter = null;
		if (diff.isRightModified())
			rightModelAdapter = match.getEditableResource().getModelAdapterFactorySet().getRightFactory().getModelAdapter(match.getRight());
		
		Object value = ancestorModelAdapter.getValueFeatureValue(match.getAncestor(), diff.getFeature(), null);
		if (diff.isLeftModified()) {
			leftModelAdapter.setValueFeatureValue(match.getLeft(), diff.getFeature(), value);
		}
		if (diff.isRightModified()) {
			rightModelAdapter.setValueFeatureValue(match.getRight(), diff.getFeature(), value);
		}
		match.getDiffs().remove(diffIndex);
		
		return new ActionResult(false, false, false);
	}
}