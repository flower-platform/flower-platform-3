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