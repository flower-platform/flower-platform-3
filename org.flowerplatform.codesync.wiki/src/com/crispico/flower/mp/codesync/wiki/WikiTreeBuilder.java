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

import static com.crispico.flower.mp.codesync.wiki.WikiPlugin.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.flowerplatform.common.regex.RegexProcessingSession;
import org.flowerplatform.model.astcache.wiki.AstCacheWikiFactory;
import org.flowerplatform.model.astcache.wiki.FlowerBlock;
import org.flowerplatform.model.astcache.wiki.NodeWithOriginalFormat;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncFactory;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * @author Mariana
 */
public class WikiTreeBuilder extends RegexProcessingSession {

	/**
	 * Used for final levels (e.g. paragraphs).
	 */
	protected final int LEAF_LEVEL = 100;
	
	protected CodeSyncElement root;
	
	protected List<FlowerBlock> flowerBlocks;
	
	protected Resource resource;
	
	protected CodeSyncElement currentNode;
	
	protected IDocument document;
	
	public void setRoot(CodeSyncElement root) {
		if (root == null)
			throw new IllegalArgumentException("Root for session cannot be null");
		this.root = root;
		this.currentNode = root;
		for (Iterator it = root.getChildren().iterator(); it.hasNext();) {
			CodeSyncElement child = (CodeSyncElement) it.next();
			if (!PAGE_CATEGORY.equals(child.getType()) && !FOLDER_CATEGORY.equals(child.getType())) {
				it.remove();
			}
		}
	}
	
	public CodeSyncElement getRoot() {
		return root;
	}
	
	public void setInput(String input) {
		document = new Document(input);
	}
	
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	/**
	 * If not <code>null</code>, the content of a new {@link FlowerBlockNode} will be retrieved
	 * from this list, if there is a {@link FlowerBlockNode} with the same id.
	 */
	public void setFlowerBlocks(List<FlowerBlock> flowerBlocks) {
		this.flowerBlocks = flowerBlocks;
		if (flowerBlocks == null) {
			this.flowerBlocks = new ArrayList<FlowerBlock>();
		}
	}
	
	@Override
	public void candidateAnnounced(String category) {
		super.candidateAnnounced(category);
		CodeSyncElement node = createWikiNode(category);
		while (!acceptAsChild(node)) {
			currentNode = getParent(currentNode);
		}
		currentNode.getChildren().add(node);
		currentNode = node;
	}
	
	private CodeSyncElement getParent(CodeSyncElement node) {
		EObject eObj = node.eContainer();
		if (eObj instanceof CodeSyncElement) {
			return (CodeSyncElement) eObj;
		}
		return null;
	}
	
	/**
	 * Creates and populates a {@link CodeSyncElement} for the matched <code>category</code>.
	 */
	protected CodeSyncElement createWikiNode(String category) {
		if (FLOWER_BLOCK_CATEGORY.equals(category)) {
			return createFlowerBlockNode();
		}
		
		if (ORDERED_LIST_ITEM_CATEGORY.equals(category) || UNORDERED_LIST_ITEM_CATEGORY.equals(category)) {
			return createListItemNode(category);
		}
		
		if (BLOCKQUOTE_CHILD_CATEGORY.equals(category)) {
			return createBlockquoteChildNode(category);
		}
		
		if (CODE_LINE_CATEGORY.equals(category)) {
			return createCodeLineNode(category);
		}
		
		if (ORDERED_LIST_CATEGORY.equals(category) || UNORDERED_LIST_CATEGORY.equals(category) ||
				BLOCKQUOTE_CATEGORY.equals(category) ||
				CODE_CATEGORY.equals(category)) {
			return createParentNode(category);
		}
		
		if (WikiPlugin.getInstance().getHeadingLevel(category) > 0) {
			return createNodeWithOriginalFormat(category);
		}
		
		CodeSyncElement cse = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createCodeSyncElement();
		cse.setName(currentSubMatchesForCurrentRegex[0]);
		cse.setType(category);
		return cse;
	}
	
	/**
	 * If this is the first list item, create the parent list node.
	 */
	protected CodeSyncElement createListItemNode(String category) {
		CodeSyncElement item = createNodeWithOriginalFormat(category);
		if (!ORDERED_LIST_ITEM_CATEGORY.equals(currentNode.getType()) && !UNORDERED_LIST_ITEM_CATEGORY.equals(currentNode.getType())) {
			candidateAnnounced(ORDERED_LIST_ITEM_CATEGORY.equals(category) ? ORDERED_LIST_CATEGORY : UNORDERED_LIST_CATEGORY);
		}
		return item;
	}
	
	/**
	 * If this is the first blockquote child, create the parent blockquote node.
	 */
	protected CodeSyncElement createBlockquoteChildNode(String category) {
		CodeSyncElement child = createNodeWithOriginalFormat(category);
		if (!BLOCKQUOTE_CHILD_CATEGORY.equals(currentNode.getType())) {
			candidateAnnounced(BLOCKQUOTE_CATEGORY);
		}
		return child;
	}
	
	/**
	 * If this is the first code line, create the parent code node.
	 */
	protected CodeSyncElement createCodeLineNode(String category) {
		CodeSyncElement line = createNodeWithOriginalFormat(category);
		if (!CODE_LINE_CATEGORY.equals(currentNode.getType())) {
			candidateAnnounced(CODE_CATEGORY);
		}
		return line;
	}
	
	protected CodeSyncElement createParentNode(String category) {
		CodeSyncElement parent = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
		parent.setName(category);
		parent.setType(category);
		return parent;
	}
	
	protected CodeSyncElement createNodeWithOriginalFormat(String category) {
		NodeWithOriginalFormat node = AstCacheWikiFactory.eINSTANCE.createNodeWithOriginalFormat();
		String originalFormat = currentSubMatchesForCurrentRegex[0];
		String name = currentSubMatchesForCurrentRegex[1];
		if (name.length() > 0) {
			node.setOriginalFormat(originalFormat.replace(name, "%s"));
		} else {
			node.setOriginalFormat(originalFormat + "%s");
		}
		node.setName(name);
		node.setType(category);
		return node;
	}
	
	/**
	 * Retrieves the content of the new {@link FlowerBlockNode} from the {@link #flowerBlocks} list,
	 * if there is an existing block with the same id. Otherwise, creates the content from the current
	 * match.
	 */
	private CodeSyncElement createFlowerBlockNode() {
		int lineStart = -1, lineEnd = -1;
		try {
			lineStart = document.getLineOfOffset(matcher.start());
			lineEnd = document.getLineOfOffset(matcher.end()) + 1;
		} catch (Exception e) {
		}
		String name = currentSubMatchesForCurrentRegex[0];
		String content = currentSubMatchesForCurrentRegex[1];
		for (FlowerBlock block : flowerBlocks) {
			if (block.getCodeSyncElement().getName().equals(name)) {
				content = block.getContent();
				break;
			}
		}
		CodeSyncElement cse = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createCodeSyncElement();
		FlowerBlock flowerBlock = AstCacheWikiFactory.eINSTANCE.createFlowerBlock();
		cse.setName(name);
		cse.setType(FLOWER_BLOCK_CATEGORY);
		flowerBlock.setContent(content);
		flowerBlock.setLineStart(lineStart);
		flowerBlock.setLineEnd(lineEnd);
		cse.setAstCacheElement(flowerBlock);
		if (resource != null) {
			resource.getContents().add(flowerBlock);
		}
		return cse;
	}

	/**
	 * @return true if {@link #currentNode} can accept <code>candidate</code> as a child,
	 * false otherwise
	 */
	protected boolean acceptAsChild(CodeSyncElement candidate) {
		Level currentLevel = getLevelForCategory(currentNode.getType());
		Level candidateLevel = getLevelForCategory(candidate.getType());
		return currentLevel.acceptChild(candidateLevel);
	}
	
	/**
	 * @return the level of indentation for this category (e.g. heading level). Nodes with lower
	 * level will contain nodes with higher levels.
	 */
	protected Level getLevelForCategory(String category) {
		if (PARAGRAPH_CATEGORY.equals(category)) {
			return Level.PARAGRAPH; // cannot have children
		}
		
		if (ORDERED_LIST_ITEM_CATEGORY.equals(category) || UNORDERED_LIST_ITEM_CATEGORY.equals(category)) {
			return Level.LIST_ITEM;	// cannot have children
		}
		if (ORDERED_LIST_CATEGORY.equals(category) || UNORDERED_LIST_CATEGORY.equals(category)) {
			return Level.LIST;
		}
		
		if (BLOCKQUOTE_CHILD_CATEGORY.equals(category)) {
			return Level.BLOCKQUOTE_CHILD; // cannot have children
		}
		
		if (BLOCKQUOTE_CATEGORY.equals(category)) {
			return Level.BLOCKQUOTE;
		}
		
		if (CODE_LINE_CATEGORY.equals(category)) {
			return Level.CODE_LINE;
		}
		
		if (CODE_CATEGORY.equals(category)) {
			return Level.CODE;
		}
		
		int headingLevel = WikiPlugin.getInstance().getHeadingLevel(category);
		if (headingLevel > 0) {
			return Level.getHeading(headingLevel);
		}
		
		if (FOLDER_CATEGORY.equals(category)) {
			return Level.FOLDER;
		}
		if (PAGE_CATEGORY.equals(category)) {
			return Level.FILE;	// page can contain anything that is not a folder
		}
		if (FLOWER_BLOCK_CATEGORY.equals(category)) {
			return Level.FLOWER_BLOCK;	// cannot have children
		}
		throw new RuntimeException("Unknown category " + category);
	}
}