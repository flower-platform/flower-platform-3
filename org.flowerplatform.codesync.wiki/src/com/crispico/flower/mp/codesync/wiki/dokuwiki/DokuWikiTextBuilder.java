package com.crispico.flower.mp.codesync.wiki.dokuwiki;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.codesync.wiki.WikiTextBuilder;

/**
 * @author Mariana
 */
public class DokuWikiTextBuilder extends WikiTextBuilder {

	protected String formatHeadline(CodeSyncElement node, int headlineLevel) {
		return node.getName() + lineDelimiter;
	}

}
