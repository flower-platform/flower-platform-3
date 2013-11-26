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

import java.util.List;

import org.flowerplatform.emf_model.regex.ParserRegex;

/**
 * @author Cristina Constantinescu
 */
public class RegexMatchDto extends RegexSubMatchDto {
	
	private ParserRegex parserRegex;
	
	private List<RegexSubMatchDto> subMatches;
			
	public ParserRegex getParserRegex() {
		return parserRegex;
	}

	public void setParserRegex(ParserRegex parserRegex) {
		this.parserRegex = parserRegex;
	}

	public List<RegexSubMatchDto> getSubMatches() {
		return subMatches;
	}

	public void setSubMatches(List<RegexSubMatchDto> subMatches) {
		this.subMatches = subMatches;
	}
	
	public RegexMatchDto() {
	}
	
	public RegexMatchDto(String value, RegexIndexDto start, RegexIndexDto end) {
		super(value, start, end);		
	}
	
}
