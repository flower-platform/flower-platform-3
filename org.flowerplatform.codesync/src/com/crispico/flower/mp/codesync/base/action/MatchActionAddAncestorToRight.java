package com.crispico.flower.mp.codesync.base.action;

import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.Match;


/**
 * @flowerModelElementId _2Z5OQLZ-EeCZUu0W6cecrg
 */
public class MatchActionAddAncestorToRight extends MatchActionAddAncestorAbstract {

	public MatchActionAddAncestorToRight(boolean processDiffs) {
		super(processDiffs);
	}

	protected Object getOpposite(Match match) {
		return match.getRight();
	}

	protected IModelAdapter getOppositeModelAdapter(Match match) {
		return match.getEditableResource().getModelAdapterFactorySet().getRightFactory().getModelAdapter(getOpposite(match));
	}
	
	protected void setOpposite(Match match, Object elment) {
		match.setRight(elment);
	}
	
	@Override
	protected void setChildrenModified(Match match) {
		match.setChildrenModifiedRight(false);
	}
}