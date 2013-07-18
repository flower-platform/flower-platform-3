package com.crispico.flower.mp.codesync.wiki;

import static com.crispico.flower.mp.codesync.wiki.WikiRegexConfiguration.*;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;

import astcache.wiki.FlowerBlock;
import astcache.wiki.Page;

/**
 * @author Mariana
 */
public abstract class WikiTextBuilder {

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
		String category = node.getType();
		if (WikiPlugin.PARAGRAPH_CATEGORY.equals(category)) {
			return String.format("%s", node.getName() + lineDelimiter);
		}
		int headlineLevel = WikiPlugin.getInstance().getHeadlineLevel(category);
		if (headlineLevel > 0) {
			return formatHeadline(node, headlineLevel);
		}
		if (WikiPlugin.FLOWER_BLOCK_CATEGORY.equals(node.getType())) {
			builder.append(formatFlowerBlock(node));
		}
		return "";
	}
	
	protected abstract String formatHeadline(CodeSyncElement node, int headlineLevel);

	private String formatFlowerBlock(CodeSyncElement node) {
		if (node.getAstCacheElement() instanceof FlowerBlock) {
			FlowerBlock block = (FlowerBlock) node.getAstCacheElement();
			return String.format("%s %s%s%s %s", 
					FLOWER_BLOCK_START, node.getName(), lineDelimiter, block.getContent(), FLOWER_BLOCK_END);
		}
		return "";
	}
	
}
