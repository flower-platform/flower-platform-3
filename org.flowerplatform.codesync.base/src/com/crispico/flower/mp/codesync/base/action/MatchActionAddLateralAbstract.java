package com.crispico.flower.mp.codesync.base.action;

import com.crispico.flower.mp.codesync.base.Diff;
import com.crispico.flower.mp.codesync.base.Match;

/**
 * @flowerModelElementId _eBiKALaDEeCZUu0W6cecrg
 */
public abstract class MatchActionAddLateralAbstract extends MatchActionAddAbstract {

	public MatchActionAddLateralAbstract(boolean processDiffs) {
		super(processDiffs);
	}

	@Override
	protected void processDiffs(Match match) {
		for (Diff diff : match.getDiffs()) {
			diff.setLeftModified(true);
			diff.setRightModified(true);
			diff.setConflict(false);
		}
		match.setDiffsModifiedLeft(true);
		match.setDiffsModifiedRight(true);
		match.setDiffsConflict(false);
	}
}