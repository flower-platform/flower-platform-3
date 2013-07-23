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
package org.flowerplatform.web.git.explorer.entity;

import org.eclipse.jgit.lib.Repository;

/**
 * @author Cristina Constantinescu
 */
public class RemoteNode {

	private Repository repository;
	
	private String remote;

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	public String getRemote() {
		return remote;
	}

	public void setRemote(String remote) {
		this.remote = remote;
	}

	public RemoteNode(Repository repository, String remote) {
		this.repository = repository;
		this.remote = remote;
	}

	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		
		result = prime * result + ((remote == null) ? 0 : remote.hashCode());
		result = prime * result + ((repository == null) ? 0 : repository.getDirectory().hashCode());
		
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		RemoteNode other = (RemoteNode) obj;

		return ((repository == null && other.getRepository() == null) || (repository != null && repository.getDirectory().equals(other.getRepository().getDirectory()))) &&
				((remote == null && other.getRemote() == null) || (remote != null && remote.equals(other.getRemote())));
	}
	
}