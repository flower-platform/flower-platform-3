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
package com.crispico.flower.mp.codesync.wiki.github;

import static com.crispico.flower.mp.codesync.wiki.WikiRegexConfiguration.*;

import org.flowerplatform.common.regex.RegexWithAction;
import org.flowerplatform.model.astcache.wiki.Page;

import com.crispico.flower.mp.codesync.wiki.IConfigurationProvider;
import com.crispico.flower.mp.codesync.wiki.WikiPlugin;
import com.crispico.flower.mp.codesync.wiki.WikiRegexConfiguration;
import com.crispico.flower.mp.codesync.wiki.WikiTextBuilder;
import com.crispico.flower.mp.codesync.wiki.WikiTreeBuilder;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;

/**
 * @author Mariana
 */
public class MarkdownConfigurationProvider implements IConfigurationProvider {

	private final String HEADING_LEVEL_1_UNDERLINE = "(\\S.*?)[\r\n]=+[\r\n]";
	private final String HEADING_LEVEL_2_UNDERLINE = "(\\S.*?)[\r\n]-+[\r\n]";
	
	@Override
	public void buildConfiguration(WikiRegexConfiguration config, CodeSyncElement cse) {
		config
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(WikiPlugin.HEADING_LEVEL_6_CATEGORY, getHeading(6), WikiPlugin.HEADING_LEVEL_6_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(WikiPlugin.HEADING_LEVEL_5_CATEGORY, getHeading(5), WikiPlugin.HEADING_LEVEL_5_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(WikiPlugin.HEADING_LEVEL_4_CATEGORY, getHeading(4), WikiPlugin.HEADING_LEVEL_4_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(WikiPlugin.HEADING_LEVEL_3_CATEGORY, getHeading(3), WikiPlugin.HEADING_LEVEL_3_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(WikiPlugin.HEADING_LEVEL_2_CATEGORY, getHeading(2), WikiPlugin.HEADING_LEVEL_2_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(WikiPlugin.HEADING_LEVEL_1_CATEGORY, getHeading(1), WikiPlugin.HEADING_LEVEL_1_CATEGORY))
//		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(HEADLINE_LEVEL_1_CATEGORY, HEADLINE_LEVEL_1_UNDERLINE, HEADLINE_LEVEL_1_CATEGORY))
//		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(HEADLINE_LEVEL_2_CATEGORY, HEADLINE_LEVEL_2_UNDERLINE, HEADLINE_LEVEL_2_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(WikiPlugin.PARAGRAPH_CATEGORY, PARAGRAPH_REGEX, WikiPlugin.PARAGRAPH_CATEGORY))
		.setUseUntilFoundThisIgnoreAll(false);
	}

	@Override
	public Class<? extends WikiTreeBuilder> getWikiTreeBuilderClass() {
		return WikiTreeBuilder.class;
	}

	@Override
	public WikiTextBuilder getWikiTextBuilder(CodeSyncElement cse) {
		return new WikiTextBuilder();
	}

	private String getHeading(int level) {
		String delim = String.format("#{%s}", level);
		return String.format(MULTILINE_MATCH_FORMAT, String.format(CAPTURE_GROUP_FORMAT, delim + CAPTURE_ANY));
	}

	@Override
	public CodeSyncRoot getWikiTree(Object wiki) {
		throw new UnsupportedOperationException("A markup configuration provider cannot be used to build a wiki structure!");
	}

	@Override
	public void savePage(Page page) {
		throw new UnsupportedOperationException("A markup configuration provider cannot be used to save a wiki structure!");
	}
	
}