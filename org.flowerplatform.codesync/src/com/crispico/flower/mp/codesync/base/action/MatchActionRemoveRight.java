package com.crispico.flower.mp.codesync.base.action;

import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.Match;

public class MatchActionRemoveRight extends MatchActionRemoveAbstract {

	@Override
	protected Object getThis(Match match) {
		return match.getRight();
	}

	@Override
	protected Object getOpposite(Match match) {
		return match.getLeft();
	}

	@Override
	protected IModelAdapter getModelAdapter(Match match) {
		return match.getEditableResource().getModelAdapterFactorySet().getRightFactory().getModelAdapter(match.getRight());
	}

	@Override
	protected void unsetThis(Match match) {
		match.setDiffsModifiedRight(false);
		match.setChildrenModifiedRight(false);
		match.setRight(null);
	}

}
