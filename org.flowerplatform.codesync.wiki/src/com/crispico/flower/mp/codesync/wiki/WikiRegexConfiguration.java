package com.crispico.flower.mp.codesync.wiki;

import com.crispico.flower.mp.common.regex.RegexConfiguration;
import com.crispico.flower.mp.common.regex.RegexWithAction;

/**
 * @author Mariana
 */
public class WikiRegexConfiguration extends RegexConfiguration {

	public static final String FLOWER_BLOCK_CATEGORY = "Flower Block";
	
	public static final String FLOWER_BLOCK_START = "@flower-platform-type";
	
	public static final String FLOWER_BLOCK_END = "flower-platform-type-end";
	
	private final String FLOWER_BLOCK = FLOWER_BLOCK_START + "\\s+?(\\S*?)\\s+?(.*?)\\s+?" + FLOWER_BLOCK_END;
	
	private Class<? extends WikiTreeBuilder> sessionClass;
	
	public WikiRegexConfiguration() {
		add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(FLOWER_BLOCK_CATEGORY, FLOWER_BLOCK, FLOWER_BLOCK_CATEGORY));
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
