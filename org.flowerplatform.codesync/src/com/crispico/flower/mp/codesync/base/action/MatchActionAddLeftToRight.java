package com.crispico.flower.mp.codesync.base.action;

import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.Match;

/**
 * @flowerModelElementId _wHDMoLZ9EeCZUu0W6cecrg
 */
public class MatchActionAddLeftToRight extends MatchActionAddLateralAbstract {
	
	public MatchActionAddLeftToRight(boolean processDiffs) {
		super(processDiffs);
		// TODO Auto-generated constructor stub
	}

	protected Object getThis(Match match) {
		return match.getLeft();
	}
	
	protected Object getOpposite(Match match) {
		return match.getRight();
	}
	
	protected IModelAdapter getThisModelAdapter(Match match) {
		return match.getEditableResource().getModelAdapterFactorySet().getLeftFactory().getModelAdapter(getThis(match));
	}
	
	protected IModelAdapter getOppositeModelAdapter(Match match) {
		return match.getEditableResource().getModelAdapterFactorySet().getRightFactory().getModelAdapter(getOpposite(match));
	}
	
	protected void setOpposite(Match match, Object elment) {
		match.setRight(elment);
	}
	
	@Override
	protected void setChildrenModified(Match match) {
		match.setChildrenModifiedRight(true);
	}

}
