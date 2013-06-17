package org.flowerplatform.web.git.dto;

import java.util.List;

/**
 *	@author Cristina Constantinescu
 */
public class ConfigBranchPageDto {

	private GitRef ref;
	
	private List<String> refs;
	
	private List<String> remotes;

	private String selectedRef;
	
	private String selectedRemote;
	
	private boolean rebase;
	
	public GitRef getRef() {
		return ref;
	}

	public void setRef(GitRef ref) {
		this.ref = ref;
	}

	public List<String> getRefs() {
		return refs;
	}

	public void setRefs(List<String> refs) {
		this.refs = refs;
	}

	public List<String> getRemotes() {
		return remotes;
	}

	public void setRemotes(List<String> remotes) {
		this.remotes = remotes;
	}

	public String getSelectedRef() {
		return selectedRef;
	}

	public void setSelectedRef(String selectedRef) {
		this.selectedRef = selectedRef;
	}

	public String getSelectedRemote() {
		return selectedRemote;
	}

	public void setSelectedRemote(String selectedRemote) {
		this.selectedRemote = selectedRemote;
	}

	public boolean isRebase() {
		return rebase;
	}

	public void setRebase(boolean rebase) {
		this.rebase = rebase;
	}	
	
}
