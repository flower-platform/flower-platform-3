package com.crispico.flower.mp.codesync.base.action;

import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.Match;

/**
 * @flowerModelElementId _yHYiILZ8EeCZUu0W6cecrg
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
