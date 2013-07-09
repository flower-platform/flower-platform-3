package com.crispico.flower.mp.codesync.wiki.github;

import com.crispico.flower.mp.codesync.wiki.WikiTextBuilder;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana
 */
public class MarkdownTextBuilder extends WikiTextBuilder {

	@Override
	protected String formatHeadline(CodeSyncElement node, int headlineLevel) {
		String delimiter = "";
		for (int i = 0; i < headlineLevel; i++) {
			delimiter += "#";
		}
		return delimiter + node.getName() + delimiter + lineDelimiter;
	}

}
