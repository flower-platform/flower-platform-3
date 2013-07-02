package com.crispico.flower.mp.codesync.base.action;


import java.util.ArrayList;
import java.util.List;

import com.crispico.flower.mp.codesync.base.Match;

/**
 * @flowerModelElementId _2BxrYLQcEeCPJvLYgj5VDQ
 */
public abstract class DiffAction {
	
	public abstract ActionResult execute(Match match, int diffIndex);
}