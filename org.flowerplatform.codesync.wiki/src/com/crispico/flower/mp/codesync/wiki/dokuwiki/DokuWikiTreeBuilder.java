package com.crispico.flower.mp.codesync.wiki.dokuwiki;

import static com.crispico.flower.mp.codesync.wiki.dokuwiki.DokuWikiConfigurationProvider.PARAGRAPH_CATEGORY;

import com.crispico.flower.mp.codesync.wiki.WikiTreeBuilder;

/**
 * @author Mariana
 */
public class DokuWikiTreeBuilder extends WikiTreeBuilder {

	protected int getLevelForCategory(String category) {
		if (PARAGRAPH_CATEGORY.equals(category)) {
			return LEAF_LEVEL;
		}
		int headlineLevel = DokuWikiConfigurationProvider.getHeadlineLevel(category);
		if (headlineLevel > 0) {
			return headlineLevel;
		}
		return super.getLevelForCategory(category);
	}
}
