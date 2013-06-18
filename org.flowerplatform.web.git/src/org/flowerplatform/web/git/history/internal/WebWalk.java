package org.flowerplatform.web.git.history.internal;

import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revplot.PlotWalk;
import org.eclipse.jgit.revwalk.RevCommit;

/**
 *	@author Cristina Constantinescu
 */
public class WebWalk extends PlotWalk {

	public WebWalk(Repository repo) {
		super(repo);
	}

	@Override
	protected RevCommit createCommit(final AnyObjectId id) {
		return new WebCommit(id, this);
	}
	
}
