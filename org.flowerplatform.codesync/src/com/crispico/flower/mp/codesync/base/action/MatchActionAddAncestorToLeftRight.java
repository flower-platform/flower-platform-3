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
 * 
 */
public class MatchActionAddAncestorToLeftRight extends DiffAction {

	@Override
	public ActionResult execute(Match match, int diffIndex) {
		Object child = match.getAncestor();
		new MatchActionAddAncestorToLeft(false).execute(match, -1);
		new MatchActionAddAncestorToRight(false).execute(match, -1);
		IModelAdapter adapter = match.getEditableResource().getModelAdapterFactorySet().getAncestorFactory().getModelAdapter(child);
		return new ActionResult(false, false, false, adapter.getMatchKey(child), true);
	}

}