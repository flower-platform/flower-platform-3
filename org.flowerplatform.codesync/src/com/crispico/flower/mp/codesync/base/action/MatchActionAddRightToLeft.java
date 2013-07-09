package com.crispico.flower.mp.codesync.base.action;

import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.Match;


/**
 * @flowerModelElementId _K-SU0LZ-EeCZUu0W6cecrg
 */
public class MatchActionAddRightToLeft extends MatchActionAddLateralAbstract {

	public MatchActionAddRightToLeft(boolean processDiffs) {
		super(processDiffs);
	}

	protected Object getThis(Match match) {
		return match.getRight();
	}
	
	protected Object getOpposite(Match match) {
		return match.getLeft();
	}
	
	protected IModelAdapter getThisModelAdapter(Match match) {
		return match.getEditableResource().getModelAdapterFactorySet().getRightFactory().getModelAdapter(getThis(match));
	}
	
	protected IModelAdapter getOppositeModelAdapter(Match match) {
		return match.getEditableResource().getModelAdapterFactorySet().getLeftFactory().getModelAdapter(getOpposite(match));
	}
	
	protected void setOpposite(Match match, Object elment) {
		match.setLeft(elment);
	}
	
	@Override
	protected void setChildrenModified(Match match) {
		match.setChildrenModifiedLeft(true);
	}
}