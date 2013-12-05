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
package org.flowerplatform.codesync.regex.remote;

import org.flowerplatform.properties.remote.SelectedItem;

/**
 * @author Cristina Constantinescu
 */
public class RegexSelectedItem extends SelectedItem {

	private RegexMatchDto match;

	private MacroRegexDto regex;
	
	private String config;
	
	public RegexMatchDto getMatch() {
		return match;
	}

	public void setMatch(RegexMatchDto match) {
		this.match = match;
	}
	
	public MacroRegexDto getRegex() {
		return regex;
	}

	public void setRegex(MacroRegexDto regex) {
		this.regex = regex;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}
	
}
