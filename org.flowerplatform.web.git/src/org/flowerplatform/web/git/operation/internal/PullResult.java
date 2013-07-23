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
package org.flowerplatform.web.git.operation.internal;

import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.RebaseResult;
import org.eclipse.jgit.transport.FetchResult;

/**
 * @author Cristina Constantinescu
 */
public class PullResult {
	private final FetchResult fetchResult;

	private final MergeResult mergeResult;

	private final RebaseResult rebaseResult;

	private final String fetchedFrom;

	PullResult(FetchResult fetchResult, String fetchedFrom,
			MergeResult mergeResult) {
		this.fetchResult = fetchResult;
		this.fetchedFrom = fetchedFrom;
		this.mergeResult = mergeResult;
		this.rebaseResult = null;
	}

	PullResult(FetchResult fetchResult, String fetchedFrom,
			RebaseResult rebaseResult) {
		this.fetchResult = fetchResult;
		this.fetchedFrom = fetchedFrom;
		this.mergeResult = null;
		this.rebaseResult = rebaseResult;
	}

	/**
	 * @return the fetch result, or <code>null</code>
	 */
	public FetchResult getFetchResult() {
		return this.fetchResult;
	}

	/**
	 * @return the merge result, or <code>null</code>
	 */
	public MergeResult getMergeResult() {
		return this.mergeResult;
	}

	/**
	 * @return the rebase result, or <code>null</code>
	 */
	public RebaseResult getRebaseResult() {
		return this.rebaseResult;
	}

	/**
	 * @return the name of the remote configuration from which fetch was tried,
	 *         or <code>null</code>
	 */
	public String getFetchedFrom() {
		return this.fetchedFrom;
	}

	/**
	 * @return whether the pull was successful
	 */
	public boolean isSuccessful() {
		if (mergeResult != null)
			return mergeResult.getMergeStatus().isSuccessful();
		else if (rebaseResult != null)
			return rebaseResult.getStatus().isSuccessful();
		return true;
	}

	@SuppressWarnings("nls")
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (fetchResult != null)
			sb.append(fetchResult.toString());
		else
			sb.append("No fetch result");
		sb.append("\n");
		if (mergeResult != null)
			sb.append(mergeResult.toString());
		else if (rebaseResult != null)
			sb.append(rebaseResult.toString());
		else
			sb.append("No update result");
		return sb.toString();
	}
}