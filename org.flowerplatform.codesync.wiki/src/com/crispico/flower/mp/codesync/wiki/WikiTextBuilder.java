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

import static com.crispico.flower.mp.codesync.wiki.WikiRegexConfiguration.*;

import org.flowerplatform.model.astcache.wiki.FlowerBlock;
import org.flowerplatform.model.astcache.wiki.Heading;
import org.flowerplatform.model.astcache.wiki.Page;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana Gheorghe
 */
public class WikiTextBuilder {

	private StringBuilder builder = new StringBuilder();
	
	protected String lineDelimiter = "\r\n";
	
	public String getWikiText(CodeSyncElement tree) {
		if (tree == null || tree.getAstCacheElement() == null) {
			return null;
		}
		if (WikiPlugin.PAGE_CATEGORY.equals(tree.getType())) {
			Page page = (Page) tree.getAstCacheElement();
			lineDelimiter = page.getLineDelimiter();
			generateWikiText(tree);
			int index = builder.lastIndexOf(lineDelimiter);
			if (index >= 0) {
				builder.delete(index, builder.length());
			}
			return builder.toString();
		} else {
			return null; // only pages have content
		}
	}

	private void generateWikiText(CodeSyncElement node) {
		builder.append(buildText(node));
		for (CodeSyncElement child : node.getChildren()) {
			generateWikiText(child);
		}
	}
	
	protected String buildText(CodeSyncElement node) {
		String text = "";
		String category = node.getType();
		if (WikiPlugin.PARAGRAPH_CATEGORY.equals(category)) {
			text = node.getName() + lineDelimiter;
		}
		int headingLevel = WikiPlugin.getInstance().getHeadingLevel(category);
		if (headingLevel > 0) {
			text = formatHeading((Heading) node, headingLevel) + lineDelimiter;
		}
		if (WikiPlugin.FLOWER_BLOCK_CATEGORY.equals(node.getType())) {
			text = formatFlowerBlock(node) + lineDelimiter;
		}
		return text;
	}
	
	protected String formatHeading(Heading node, int headingLevel) {
		return String.format(node.getOriginalFormat(), node.getName());
	}

	private String formatFlowerBlock(CodeSyncElement node) {
		if (node.getAstCacheElement() instanceof FlowerBlock) {
			FlowerBlock block = (FlowerBlock) node.getAstCacheElement();
			return String.format("%s %s%s%s %s", 
					FLOWER_BLOCK_START, node.getName(), lineDelimiter, block.getContent(), FLOWER_BLOCK_END);
		}
		return "";
	}
	
}