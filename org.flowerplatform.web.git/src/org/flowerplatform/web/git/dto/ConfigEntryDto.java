package org.flowerplatform.web.git.dto;

/**
 *	@author Cristina Constantinescu
 */
public class ConfigEntryDto {

	private String section;
	
	private String subsection;
	
	private String name;
	
	private String value;
	
	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getSubsection() {
		return subsection;
	}

	public void setSubsection(String subsection) {
		this.subsection = subsection;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ConfigEntryDto() {	
	}
	
	public ConfigEntryDto(String section, String subsection, String name, String value) {		
		this.section = section;
		this.subsection = subsection;
		this.name = name;
		this.value = value;
	}
		
}
