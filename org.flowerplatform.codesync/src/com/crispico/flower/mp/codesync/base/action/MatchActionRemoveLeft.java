package com.crispico.flower.mp.codesync.base.action;

import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.Match;

public class MatchActionRemoveLeft extends MatchActionRemoveAbstract {

	@Override
	protected Object getThis(Match match) {
		return match.getLeft();
	}

	@Override
	protected Object getOpposite(Match match) {
		return match.getRight();
	}

	@Override
	protected IModelAdapter getModelAdapter(Match match) {
		return match.getEditableResource().getModelAdapterFactorySet().getLeftFactory().getModelAdapter(match.getLeft());
	}

	@Override
	protected void unsetThis(Match match) {
		match.setDiffsModifiedLeft(false);
		match.setChildrenModifiedLeft(false);
		match.setLeft(null);
	}

}
