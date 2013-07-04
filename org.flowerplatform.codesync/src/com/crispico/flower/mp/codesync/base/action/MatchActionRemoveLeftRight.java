package com.crispico.flower.mp.codesync.base.action;

import com.crispico.flower.mp.codesync.base.Match;

public class MatchActionRemoveLeftRight extends DiffAction {

	@Override
	public ActionResult execute(Match match, int diffIndex) {
		new MatchActionRemoveLeft().execute(match, diffIndex);
		return new MatchActionRemoveRight().execute(match, diffIndex); 
	}

}
