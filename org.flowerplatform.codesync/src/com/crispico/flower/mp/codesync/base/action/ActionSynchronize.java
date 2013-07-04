package com.crispico.flower.mp.codesync.base.action;

import com.crispico.flower.mp.codesync.base.Diff;
import com.crispico.flower.mp.codesync.base.Match;

public class ActionSynchronize {

	public static ActionSynchronize INSTANCE = new ActionSynchronize();
	
	/**
	 * @author Cristi
	 * @author Mariana
	 */
	public ActionResult[] execute(Match match) {
		if (match.isConflict() || match.isChildrenConflict())
			throw new IllegalArgumentException("The match (or one of its children) are conflicted.");
		boolean childrenConflict = false;
		boolean childrenModifiedLeft = false;
		boolean childrenModifiedRight = false;
		
		boolean diffsInConflict = false;
		boolean diffsModifiedLeft = false;
		boolean diffsModifiedRight = false;
		
		ActionResult[] result = new ActionResult[match.getDiffs().size()];
		
		int i = 0;
		for (Diff diff : match.getDiffs()) {
			int defaultAction = DiffActionRegistry.INSTANCE.getActionEntriesForUI(match, diff, true).defaultAction;
			if (defaultAction != -1) {
				result[i] = DiffActionRegistry.ActionType.values()[defaultAction].diffAction.execute(match, i);
//				diffsInConflict = diffsInConflict
			}
			i++;
		}
		return result;
	}
	
}
