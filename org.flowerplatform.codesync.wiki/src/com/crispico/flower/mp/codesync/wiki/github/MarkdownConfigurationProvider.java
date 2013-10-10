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
import static com.crispico.flower.mp.codesync.wiki.WikiPlugin.*;

import org.flowerplatform.common.regex.RegexWithAction;
import org.flowerplatform.model.astcache.wiki.Page;

import com.crispico.flower.mp.codesync.wiki.IConfigurationProvider;
import com.crispico.flower.mp.codesync.wiki.WikiRegexConfiguration;
import com.crispico.flower.mp.codesync.wiki.WikiTextBuilder;
import com.crispico.flower.mp.codesync.wiki.WikiTreeBuilder;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;

/**
 * @author Mariana
 */
public class MarkdownConfigurationProvider implements IConfigurationProvider {

	private final String HEADING_LEVEL_1_UNDERLINE = "=";
	private final String HEADING_LEVEL_2_UNDERLINE = "-";
	
	@Override
	public void buildConfiguration(WikiRegexConfiguration config, CodeSyncElement cse) {
		config
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(HEADING_LEVEL_6_CATEGORY, getHeadingAtx(6), HEADING_LEVEL_6_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(HEADING_LEVEL_5_CATEGORY, getHeadingAtx(5), HEADING_LEVEL_5_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(HEADING_LEVEL_4_CATEGORY, getHeadingAtx(4), HEADING_LEVEL_4_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(HEADING_LEVEL_3_CATEGORY, getHeadingAtx(3), HEADING_LEVEL_3_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(HEADING_LEVEL_2_CATEGORY, getHeadingAtx(2), HEADING_LEVEL_2_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(HEADING_LEVEL_1_CATEGORY, getHeadingAtx(1), HEADING_LEVEL_1_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(HEADING_LEVEL_1_CATEGORY, getHeadingSetext(HEADING_LEVEL_1_UNDERLINE), HEADING_LEVEL_1_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(HEADING_LEVEL_2_CATEGORY, getHeadingSetext(HEADING_LEVEL_2_UNDERLINE), HEADING_LEVEL_2_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(ORDERED_LIST_ITEM_CATEGORY, getListItem(String.format(ONE_OR_MORE_TIMES_FORMAT, "\\d") + "\\."), ORDERED_LIST_ITEM_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(UNORDERED_LIST_ITEM_CATEGORY, getListItem(String.format(CLASS, "\\*\\+-")), UNORDERED_LIST_ITEM_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(CODE_LINE_CATEGORY, getCode(), CODE_LINE_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(BLOCKQUOTE_CHILD_CATEGORY, getBlockquote(), BLOCKQUOTE_CHILD_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(PARAGRAPH_CATEGORY, PARAGRAPH_REGEX, PARAGRAPH_CATEGORY))
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

	/**
	 * ## My Header
	 */
	private String getHeadingAtx(int level) {
		String delim = String.format("#{%s}", level);
		return String.format(MULTILINE_MATCH_FORMAT, 
				String.format(CAPTURE_GROUP_FORMAT, 					// capture full heading definition
				delim + 												// delimiter
				CAPTURE_ANY + 											// capture heading title
				String.format(ZERO_OR_MORE_TIMES_FORMAT, "#")));		// trailing delimiter
	}

	/**
	 * My Header
	 * =========
	 */
	private String getHeadingSetext(String sign) {
		return String.format(MULTILINE_MATCH_FORMAT, 
				String.format(CAPTURE_GROUP_FORMAT, 					// capture full heading definition
				CAPTURE_ANY +											// capture heading title
				NON_CAPTURE_LINE_DELIMITER +							// ignore line delimiters
				String.format(ONE_OR_MORE_TIMES_FORMAT, sign) +			// underline
				String.format(ZERO_OR_MORE_TIMES_FORMAT, 
						String.format(CLASS, WHITESPACE + 				// trailing whitespaces
						"&&[^\r\n]"))));								// except line delimiters
	}
	
	private String getListItem(String marker) {
		return String.format(MULTILINE_MATCH_FORMAT, 
				String.format(CAPTURE_GROUP_FORMAT, 					// capture full list item definition
				marker +												// marker (e.g. number or *)
				String.format(ONE_OR_MORE_TIMES_FORMAT, WHITESPACE) +	// trailing whitespaces
				CAPTURE_ANY));											// capture list item
	}
	
	private String getBlockquote() {
		String gt = ">";
		return String.format(MULTILINE_MATCH_FORMAT, 
				String.format(CAPTURE_GROUP_FORMAT, 
				String.format(ZERO_OR_MORE_TIMES_FORMAT, 
						String.format(CLASS, gt + WHITESPACE + 			// > and whitespaces
								"&&[^\r\n]")) +							// except line terminators
				gt +													// must contain a >
				CAPTURE_ANY));											// capture blockquote text
	}
	
	private String getCode() {
		return String.format(MULTILINE_MATCH_FORMAT, 			
				String.format(CAPTURE_GROUP_FORMAT, 					// capture full definition
				String.format(NON_CAPTURE_GROUP_FORMAT, "    |\t") + 	// starts with 4 spaces or a tab
				CAPTURE_ANY));											// capture code text
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