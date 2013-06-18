package org.flowerplatform.web.git.dto;

import java.util.List;

/**
 *	@author Cristina Constantinescu
 */
public class ConfigPropertiesPageDto {

	private String repository;
	
	private List<ConfigEntryDto> entries;

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public List<ConfigEntryDto> getEntries() {
		return entries;
	}

	public void setEntries(List<ConfigEntryDto> entries) {
		this.entries = entries;
	}
		
}
