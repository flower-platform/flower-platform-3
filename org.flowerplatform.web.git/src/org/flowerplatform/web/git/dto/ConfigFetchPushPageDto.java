package org.flowerplatform.web.git.dto;

import java.util.List;

/**
 *	@author Cristina Constantinescu
 */
public class ConfigFetchPushPageDto {

	private GitRef ref;
	
	private List<RemoteConfig> remoteConfigs;
	
	public GitRef getRef() {
		return ref;
	}

	public void setRef(GitRef ref) {
		this.ref = ref;
	}

	public List<RemoteConfig> getRemoteConfigs() {
		return remoteConfigs;
	}

	public void setRemoteConfigs(List<RemoteConfig> remoteConfigs) {
		this.remoteConfigs = remoteConfigs;
	}
		
}
