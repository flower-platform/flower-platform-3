package com.crispico.flower.mp.codesync.base.action;

import com.crispico.flower.mp.codesync.base.Diff;
import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.Match;


/**
 * @flowerModelElementId _qKFpgLZ-EeCZUu0W6cecrg
 */
public abstract class MatchActionAddAncestorAbstract extends MatchActionAddAbstract {

	public MatchActionAddAncestorAbstract(boolean processDiffs) {
		super(processDiffs);
	}

	protected Object getThis(Match match) {
		return match.getAncestor();
	}

	protected IModelAdapter getThisModelAdapter(Match match) {
		return match.getEditableResource().getModelAdapterFactorySet().getAncestorFactory().getModelAdapter(getThis(match));
	}

	@Override
	protected void processDiffs(Match match) {
		for (Diff diff : match.getDiffs()) {
			diff.setConflict(false);
		}
		match.setDiffsConflict(false);
	}
}