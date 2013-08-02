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
package org.flowerplatform.common.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristi
 * 
 */
public class RegexConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(RegexConfiguration.class); 

	protected List<RegexWithAction> regexes = new ArrayList<RegexWithAction>();
	
	protected RegexWithAction[] captureGroupToRegexMapping;
	
	protected Pattern pattern;
	
	protected int targetNestingForMatches;
	
	protected boolean useUntilFoundThisIgnoreAll = true;
	
	protected RegexProcessingSession createSessionInstance() {
		return new RegexProcessingSession();
	}
	
	public RegexConfiguration setTargetNestingForMatches(int targetNestingForMatches) {
		this.targetNestingForMatches = targetNestingForMatches;
		return this;
	}
	
	/**
	 * Option needed if the configuration does not need any filtering before returning output 
	 */
	public RegexConfiguration setUseUntilFoundThisIgnoreAll(boolean useUntilFoundThisIgnoreAll) {
		this.useUntilFoundThisIgnoreAll = useUntilFoundThisIgnoreAll;
		return this;
	}

	public RegexConfiguration add(RegexWithAction regex) {
		regexes.add(regex);
		return this;
	}
	
	/**
	 * Iterates on {@link #regexes} to find out how big the array should be. Then
	 * creates the array.
	 */
	protected void createCaptureGroupToRegexMappingArray() {
		int nextCaptureGroupIndex = 1;
		for (RegexWithAction regex : regexes) {
			nextCaptureGroupIndex += 1 + regex.numberOfCaptureGroups;
		}
		captureGroupToRegexMapping = new RegexWithAction[nextCaptureGroupIndex];
	}
	
	/**
	 * 
	 */
	public RegexConfiguration compile() {
		if (logger.isTraceEnabled()) {
			logger.trace("Compiling configuration...");
		}
		
		createCaptureGroupToRegexMappingArray();
		
		int nextCaptureGroupIndex = 1;
		StringBuilder composedRegex = new StringBuilder();
		for (int i = 0; i < regexes.size(); i++) {
			composedRegex.append('(');
		
			RegexWithAction regex = regexes.get(i);
			
			if (logger.isTraceEnabled()) {
				logger.trace("Adding to capture group = {} regex = {} having {} capture groups", new Object[] { nextCaptureGroupIndex, regex.regex, regex.numberOfCaptureGroups});
			}
			
			composedRegex.append(regex.regex);
			composedRegex.append(')');
			if (i != regexes.size() - 1) {
				composedRegex.append('|');
			}
			
			captureGroupToRegexMapping[nextCaptureGroupIndex] = regex;
			nextCaptureGroupIndex += 1 + regex.numberOfCaptureGroups;
		}
		
		if (logger.isTraceEnabled()) {
			logger.trace("Composed regex = {} having {} capture groups. Compiling pattern...", composedRegex.toString(), nextCaptureGroupIndex - 1);
		}
		
		pattern = Pattern.compile(composedRegex.toString(), Pattern.DOTALL);
		return this;
	}
	
	/**
	 * 
	 */
	public RegexProcessingSession startSession(CharSequence input) {
		RegexProcessingSession session = createSessionInstance();
		session.configuration = this;
		session.matcher = pattern.matcher(input);
		session.reset(false);
		return session;
	}
	
}