package org.flowerplatform.web.git.dto;

import java.util.List;

/**
 *	@author Cristina Constantinescu
 */
public class RemoteConfig {
	
	private String remoteName;
	
	private String fetchUri;
	
	private List<String> fetchMappings;
	
	private String pushUri;
	
	private List<String> pushMappings;

	public String getRemoteName() {
		return remoteName;
	}

	public void setRemoteName(String remoteName) {
		this.remoteName = remoteName;
	}

	public List<String> getFetchMappings() {
		return fetchMappings;
	}

	public void setFetchMappings(List<String> fetchMappings) {
		this.fetchMappings = fetchMappings;
	}

	public List<String> getPushMappings() {
		return pushMappings;
	}

	public void setPushMappings(List<String> pushMappings) {
		this.pushMappings = pushMappings;
	}

	public String getFetchUri() {
		return fetchUri;
	}

	public void setFetchUri(String fetchUri) {
		this.fetchUri = fetchUri;
	}

	public String getPushUri() {
		return pushUri;
	}

	public void setPushUri(String pushUri) {
		this.pushUri = pushUri;
	}
		
}