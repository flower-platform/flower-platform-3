package com.crispico.flower.mp.codesync.base.action;

import com.crispico.flower.mp.codesync.base.Diff;
import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.Match;


/**
 * @flowerModelElementId _WeIecLQcEeCPJvLYgj5VDQ
 */
public class DiffActionCopyLeftToRight extends DiffAction {

	@Override
	public ActionResult execute(Match match, int diffIndex) {
		Diff diff = match.getDiffs().get(diffIndex);
		IModelAdapter leftModelAdapter = match.getEditableResource().getModelAdapterFactorySet().getLeftFactory().getModelAdapter(match.getLeft());
		IModelAdapter rightModelAdapter = match.getEditableResource().getModelAdapterFactorySet().getRightFactory().getModelAdapter(match.getRight());
		
		Object value = leftModelAdapter.getValueFeatureValue(match.getLeft(), diff.getFeature(), null);
		rightModelAdapter.setValueFeatureValue(match.getRight(), diff.getFeature(), value);
		diff.setConflict(false);
		diff.setRightModified(true);
		return new ActionResult(false, diff.isLeftModified(), true);
	}
}