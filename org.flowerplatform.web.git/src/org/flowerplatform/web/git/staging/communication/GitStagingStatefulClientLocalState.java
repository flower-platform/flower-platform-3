package org.flowerplatform.web.git.staging.communication;

import org.flowerplatform.communication.stateful_service.IStatefulClientLocalState;

/**
 * @author Cristina
 */
public class GitStagingStatefulClientLocalState implements IStatefulClientLocalState {

	private String repositoryLocation;

	public String getRepositoryLocation() {
		return repositoryLocation;
	}

	public void setRepositoryLocation(String repositoryLocation) {
		this.repositoryLocation = repositoryLocation;
	}

		
}
