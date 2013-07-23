/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
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