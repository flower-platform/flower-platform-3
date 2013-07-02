package com.crispico.flower.mp.codesync.base.action;

import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.Match;


/**
 * @flowerModelElementId _zTWUELZ-EeCZUu0W6cecrg
 */
public class MatchActionAddAncestorToLeft extends MatchActionAddAncestorAbstract {

	public MatchActionAddAncestorToLeft(boolean processDiffs) {
		super(processDiffs);
	}

	protected Object getOpposite(Match match) {
		return match.getLeft();
	}

	protected IModelAdapter getOppositeModelAdapter(Match match) {
		return match.getEditableResource().getModelAdapterFactorySet().getLeftFactory().getModelAdapter(getOpposite(match));
	}
	
	protected void setOpposite(Match match, Object elment) {
		match.setLeft(elment);
	}

	@Override
	protected void setChildrenModified(Match match) {
		match.setChildrenModifiedLeft(false);
	}
}