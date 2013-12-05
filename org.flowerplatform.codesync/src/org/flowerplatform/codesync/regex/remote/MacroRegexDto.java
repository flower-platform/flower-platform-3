package org.flowerplatform.codesync.regex.remote;

public class MacroRegexDto {

	protected String id;
	
	protected String name;
	
	protected String regex;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}
		
	public MacroRegexDto() {
	}
	
	public MacroRegexDto(String name, String regex) {
		this.name = name;
		this.regex = regex;
	}
	
}
