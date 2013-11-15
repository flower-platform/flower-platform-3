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
public class RegexSubMatchDto {

	private String value;
	
	private RegexIndexDto start;
	
	private RegexIndexDto end;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public RegexIndexDto getStart() {
		return start;
	}

	public void setStart(RegexIndexDto start) {
		this.start = start;
	}

	public RegexIndexDto getEnd() {
		return end;
	}

	public void setEnd(RegexIndexDto end) {
		this.end = end;
	}

	public RegexSubMatchDto() {		
	}	
	
	public RegexSubMatchDto(String value, RegexIndexDto start, RegexIndexDto end) {		
		this.value = value;
		this.start = start;
		this.end = end;
	}		
	
}
