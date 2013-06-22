package org.flowerplatform.web.git.dto;

import java.util.List;

/**
 *	@author Cristina Constantinescu
 */
public class RemoteConfig {
	
	private String name;
	
	private String uri;
	
	private List<String> fetchMappings;
		
	private List<String> pushMappings;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
}