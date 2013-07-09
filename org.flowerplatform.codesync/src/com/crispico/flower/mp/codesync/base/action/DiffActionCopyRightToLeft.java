package com.crispico.flower.mp.codesync.base.action;

import com.crispico.flower.mp.codesync.base.Diff;
import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.Match;


/**
 * @flowerModelElementId _hCxh4LQcEeCPJvLYgj5VDQ
 */
public class DiffActionCopyRightToLeft extends DiffAction {

	@Override
	public ActionResult execute(Match match, int diffIndex) {
		Diff diff = match.getDiffs().get(diffIndex);
		IModelAdapter leftModelAdapter = match.getEditableResource().getModelAdapterFactorySet().getLeftFactory().getModelAdapter(match.getLeft());
		IModelAdapter rightModelAdapter = match.getEditableResource().getModelAdapterFactorySet().getRightFactory().getModelAdapter(match.getRight());
		
		Object value = rightModelAdapter.getValueFeatureValue(match.getRight(), diff.getFeature(), null);
		leftModelAdapter.setValueFeatureValue(match.getLeft(), diff.getFeature(), value);
		diff.setConflict(false);
		diff.setLeftModified(true);
		return new ActionResult(false, true, diff.isRightModified());
	}
}