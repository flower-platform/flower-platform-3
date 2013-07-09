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
