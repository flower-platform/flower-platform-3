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
package org.flowerplatform.codesync.regex.ide.remote;

/**
 * @author Cristina Constantinescu
 */
public class RegexDto {

	private String name;
	
	private String regex;
	
	private String regexWithMacro;
		
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegexWithMacro() {
		return regexWithMacro;
	}

	public void setRegexWithMacro(String regexWithMacro) {
		this.regexWithMacro = regexWithMacro;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public RegexDto() {		
	}
	
	public RegexDto(String name, String regex, String regexWithMacro) {
		super();
		this.name = name;
		this.regex = regex;
		this.regexWithMacro = regexWithMacro;
	}

}
