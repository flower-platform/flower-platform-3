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
package com.crispico.flower.mp.codesync.wiki;

import org.flowerplatform.common.regex.RegexConfiguration;
import org.flowerplatform.common.regex.RegexWithAction;

/**
 * @author Mariana
 */
public class WikiRegexConfiguration extends RegexConfiguration {
	

	///////////////////////////
	// Helper formats and regex
	///////////////////////////
	
	public static final String MULTILINE_MATCH_FORMAT = "^%s$";
	
	public static final String CAPTURE_GROUP_FORMAT = "(%s)";
	
	public static final String NON_CAPTURE_GROUP_FORMAT = "(?:%s)";
	
	public static final String ZERO_OR_MORE_TIMES_FORMAT = "%s*";
	
	public static final String ONE_OR_MORE_TIMES_FORMAT = "%s+"; 
	
	public static final String CLASS = "[%s]";
	
	public static final String ANY_CHARACTER = ".";
	
	public static final String WHITESPACE = "\\s";
	
	public static final String DIGIT = "\\d";
	
	public static final String LINE_DELIMITER = "\r\n|[\r\n]";
	
	public static final String CAPTURE_ANY = String.format(CAPTURE_GROUP_FORMAT, String.format(ZERO_OR_MORE_TIMES_FORMAT, ANY_CHARACTER));
	
	public static final String NON_CAPTURE_LINE_DELIMITER = String.format(NON_CAPTURE_GROUP_FORMAT, LINE_DELIMITER);
	
	///////////////////////////
	// Default wiki syntax
	///////////////////////////
	
	public static final String PARAGRAPH_REGEX = String.format(MULTILINE_MATCH_FORMAT, CAPTURE_ANY);
		
	public static final String FLOWER_BLOCK_START = "@flower-platform-type";
	
	public static final String FLOWER_BLOCK_END = "flower-platform-type-end";
	
	private final String FLOWER_BLOCK = FLOWER_BLOCK_START + "\\s+?(\\S*?)\\s+?(.*?)\\s+?" + FLOWER_BLOCK_END;
	
	private Class<? extends WikiTreeBuilder> sessionClass;
	
	public WikiRegexConfiguration() {
		add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(WikiPlugin.FLOWER_BLOCK_CATEGORY, FLOWER_BLOCK, WikiPlugin.FLOWER_BLOCK_CATEGORY));
	}
	
	public WikiRegexConfiguration setSessionClass(Class<? extends WikiTreeBuilder> sessionClass) {
		this.sessionClass = sessionClass;
		return this;
	}

	@Override
	protected WikiTreeBuilder createSessionInstance() {
		try {
			return sessionClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("The session class cannot be instantiated!", e);
		} 
	}

	@Override
	public WikiTreeBuilder startSession(CharSequence input) {
		WikiTreeBuilder session = (WikiTreeBuilder) super.startSession(input);
		session.setInput(input.toString());
		return session;
	}
	
}