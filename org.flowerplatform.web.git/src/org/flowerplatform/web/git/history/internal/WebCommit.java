package org.flowerplatform.web.git.history.internal;

import java.io.IOException;

import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.revplot.PlotCommit;
import org.eclipse.jgit.revwalk.RevWalk;

/**
 *	@author Cristina Constantinescu
 */
public class WebCommit extends PlotCommit<WebLane> {
	
	private RevWalk walk;
	
	WebCommit(final AnyObjectId id, RevWalk walk) {
		super(id);
		this.walk = walk;
	}

	@Override
	public void reset() {	
		walk = null;
		super.reset();
	}

	public void parseBody() throws IOException {
		if (getRawBuffer() == null) {
			walk.parseBody(this);
		}
	}

}
