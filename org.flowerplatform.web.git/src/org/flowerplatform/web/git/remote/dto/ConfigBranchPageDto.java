package org.flowerplatform.web.git.remote.dto;

import java.util.List;

import org.flowerplatform.web.git.remote.dto.GitRef;

/**
 *	@author Cristina Constantinescu
 */
public class ConfigBranchPageDto {

	private GitRef ref;
	
	private List<GitRef> refs;
	
	private List<RemoteConfig> remotes;

	private GitRef selectedRef;
	
	private RemoteConfig selectedRemote;
	
	private boolean rebase;
	
	public GitRef getRef() {
		return ref;
	}

	public void setRef(GitRef ref) {
		this.ref = ref;
	}

	public List<GitRef> getRefs() {
		return refs;
	}

	public void setRefs(List<GitRef> refs) {
		this.refs = refs;
	}

	public List<RemoteConfig> getRemotes() {
		return remotes;
	}

	public void setRemotes(List<RemoteConfig> remotes) {
		this.remotes = remotes;
	}

	public GitRef getSelectedRef() {
		return selectedRef;
	}

	public void setSelectedRef(GitRef selectedRef) {
		this.selectedRef = selectedRef;
	}

	public RemoteConfig getSelectedRemote() {
		return selectedRemote;
	}

	public void setSelectedRemote(RemoteConfig selectedRemote) {
		this.selectedRemote = selectedRemote;
	}

	public boolean isRebase() {
		return rebase;
	}

	public void setRebase(boolean rebase) {
		this.rebase = rebase;
	}	
	
}
