package com.crispico.flower.mp.codesync.wiki.dokuwiki;

import static com.crispico.flower.mp.codesync.wiki.dokuwiki.DokuWikiConfigurationProvider.*;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.codesync.wiki.WikiTextBuilder;

/**
 * @author Mariana
 */
public class DokuWikiTextBuilder extends WikiTextBuilder {

	@Override
	protected String buildText(CodeSyncElement node) {
		String category = node.getType();
		if (PARAGRAPH_CATEGORY.equals(category)) {
			return String.format("%s", node.getName());
		}
		int headlineLevel = DokuWikiConfigurationProvider.getHeadlineLevel(category);
		if (headlineLevel > 0) {
			return formatHeadline(node, headlineLevel);
		}
		return super.buildText(node);
	}

	private String formatHeadline(CodeSyncElement node, int headlineLevel) {
		String delimiter = "";
		for (int i = 0; i < 7 - headlineLevel; i++) {
			delimiter += "=";
		}
		return String.format("%s%s%s", delimiter, node.getName(), delimiter);
	}

}
