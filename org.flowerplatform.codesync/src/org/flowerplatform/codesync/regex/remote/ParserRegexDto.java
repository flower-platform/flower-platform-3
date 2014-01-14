package org.flowerplatform.codesync.regex.remote;

public class ParserRegexDto extends MacroRegexDto {

	private RegexActionDto action;
	
	public RegexActionDto getAction() {
		return action;
	}

	public void setAction(RegexActionDto action) {
		this.action = action;
	}

	public ParserRegexDto() {	
		super();
	}
		
	public ParserRegexDto(String name, String regex) {
		super(name, regex);
	}
}
