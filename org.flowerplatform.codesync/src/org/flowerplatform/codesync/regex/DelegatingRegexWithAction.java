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
package org.flowerplatform.codesync.regex;

import java.util.regex.Pattern;

import org.flowerplatform.common.regex.AbstractRegexWithAction;
import org.flowerplatform.common.regex.RegexAction;
import org.flowerplatform.common.regex.RegexProcessingSession;
import org.flowerplatform.emf_model.regex.ParserRegex;

/**
 * @author Cristina Constantinescu
 */
public class DelegatingRegexWithAction extends AbstractRegexWithAction {

	protected String config;
	
	protected ParserRegex parserRegex;
	
	protected RegexAction action;
	
	protected String regex;
		
	protected int numberOfCaptureGroups = -1;
	
	public DelegatingRegexWithAction setConfig(String config) {
		this.config = config;
		return this;
	}

	@Override
	public String getRegex() {
		if (regex == null) {
			regex = RegexService.getInstance().getFullRegex(config, parserRegex.getRegex());
		}
		return regex;
	} 

	@Override
	public int getNumberOfCaptureGroups() {
		if (numberOfCaptureGroups == -1) {
			numberOfCaptureGroups = Pattern.compile(getRegex()).matcher("").groupCount();
		}
		return numberOfCaptureGroups;
	}
	
	@Override
	public String getName() {
		return parserRegex.getName();
	}
	
	public String getRegexWithMacros() {
		return parserRegex.getRegex();
	}
	
	@Override
	public RegexAction getRegexAction() {
		if (action == null) {
			action = RegexService.getInstance().getActions().get(parserRegex.getAction());
		}
		return action;
	}
	
	public DelegatingRegexWithAction setRegexAction(RegexAction action) {
		this.action = action;
		return this;
	}

	public DelegatingRegexWithAction setParserRegex(ParserRegex parserRegex) {
		this.parserRegex = parserRegex;
		return this;
	}

	@Override
	public void executeAction(RegexProcessingSession session) {
		getRegexAction().executeAction(session);
	}
	
}
