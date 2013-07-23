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
package com.crispico.flower.mp.codesync.wiki.dokuwiki;

import java.util.List;

import org.flowerplatform.common.regex.RegexWithAction;

import astcache.wiki.Page;
import astcache.wiki.WikiPackage;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;
import com.crispico.flower.mp.codesync.wiki.IConfigurationProvider;
import com.crispico.flower.mp.codesync.wiki.WikiPlugin;
import com.crispico.flower.mp.codesync.wiki.WikiRegexConfiguration;
import com.crispico.flower.mp.codesync.wiki.WikiTextBuilder;
import com.crispico.flower.mp.codesync.wiki.WikiTreeBuilder;

import static com.crispico.flower.mp.codesync.wiki.WikiRegexConfiguration.*;

/**
 * @author Mariana
 */
public class DokuWikiConfigurationProvider implements IConfigurationProvider {

	@Override
	public void buildConfiguration(WikiRegexConfiguration config, CodeSyncElement cse) {
		config
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(WikiPlugin.HEADLINE_LEVEL_1_CATEGORY, getHeadline(1), WikiPlugin.HEADLINE_LEVEL_1_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(WikiPlugin.HEADLINE_LEVEL_2_CATEGORY, getHeadline(2), WikiPlugin.HEADLINE_LEVEL_2_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(WikiPlugin.HEADLINE_LEVEL_3_CATEGORY, getHeadline(3), WikiPlugin.HEADLINE_LEVEL_3_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(WikiPlugin.HEADLINE_LEVEL_4_CATEGORY, getHeadline(4), WikiPlugin.HEADLINE_LEVEL_4_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(WikiPlugin.HEADLINE_LEVEL_5_CATEGORY, getHeadline(5), WikiPlugin.HEADLINE_LEVEL_5_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(WikiPlugin.PARAGRAPH_CATEGORY, PARAGRAPH_REGEX, WikiPlugin.PARAGRAPH_CATEGORY))
		.setUseUntilFoundThisIgnoreAll(false);
	}
	
	@Override
	public Class<? extends WikiTreeBuilder> getWikiTreeBuilderClass() {
		return WikiTreeBuilder.class;
	}
	
	@Override
	public WikiTextBuilder getWikiTextBuilder(CodeSyncElement cse) {
		return new DokuWikiTextBuilder();
	}
	
	private String getHeadline(int level) {
		return String.format("(={%s}", 7 - level) 
				+ ".*?" 
				+ String.format("={%s,}", 2) 		// at least 2 times
				+ "\\s*?"
				+ ")"
				+ LINE_TERMINATOR;
	}

	/**
	 * @param wiki list of {@link DokuWikiPage}s
	 */
	@Override
	public CodeSyncRoot getWikiTree(Object wiki) {
		List<DokuWikiPage> pages = (List<DokuWikiPage>) wiki;
		CodeSyncRoot root = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createCodeSyncRoot();
		root.setName(DokuWikiPlugin.getInstance().getWikiName());
		root.setType(WikiPlugin.FOLDER_CATEGORY);
		if (pages != null) {
			for (DokuWikiPage page : pages) {
				CodeSyncElement crtNode = root;
				String[] fragments = page.getId().split(":");
				for (int i = 0; i < fragments.length; i++) {
					String fragment = fragments[i];
					CodeSyncElement child = getChild(crtNode, fragment);
					if (child == null) {
						child = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createCodeSyncElement();
						child.setName(fragment);
						child.setType(WikiPlugin.FOLDER_CATEGORY);
						if (i == fragments.length - 1) {
							child.setType(WikiPlugin.PAGE_CATEGORY);
							Page wikiPage = WikiPackage.eINSTANCE.getWikiFactory().createPage();
							wikiPage.setInitialContent(page.getContent());
							wikiPage.setLineDelimiter(WikiPlugin.getInstance().getLineDelimiter(page.getContent()));
							child.setAstCacheElement(wikiPage);
						}
						crtNode.getChildren().add(child);
					}
					crtNode = child;
				}
			}
		}
		return root;
	}
	
	private CodeSyncElement getChild(CodeSyncElement parent, String name) {
		for (CodeSyncElement child : parent.getChildren()) {
			if (child.getName().equals(name)) {
				return child;
			}
		}
		return null;
	}

	@Override
	public void savePage(Page page) {
		DokuWikiPlugin.getInstance().savePage(page);
	}
	
}