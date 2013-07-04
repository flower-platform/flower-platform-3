package com.crispico.flower.mp.codesync.wiki;

import static com.crispico.flower.mp.codesync.wiki.WikiRegexConfiguration.FLOWER_BLOCK_CATEGORY;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;

import astcache.wiki.FlowerBlock;
import astcache.wiki.WikiPackage;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.common.regex.RegexProcessingSession;

/**
 * @author Mariana
 */
public abstract class WikiTreeBuilder extends RegexProcessingSession {

	public static final String FOLDER_CATEGORY = "Folder";
	public static final String PAGE_CATEGORY = "Page";
	
	/**
	 * Used for final levels (e.g. paragraphs).
	 */
	protected final int LEAF_LEVEL = 100;
	
	/**
	 * Used for top levels that can contain nodes of the same level (e.g. folders).
	 */
	protected final int FOLDER_LEVEL = -1;
	
	/**
	 * Used for file level.
	 */
	protected final int FILE_LEVEL = 0;
	
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
		CodeSyncElement cse = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createCodeSyncElement();
		cse.setName(currentSubMatchesForCurrentRegex[0]);
		cse.setType(category);
		return cse;
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
		FlowerBlock flowerBlock = WikiPackage.eINSTANCE.getWikiFactory().createFlowerBlock();
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
		int currentLevel = getLevelForCategory(currentNode.getType());
		if (currentLevel == LEAF_LEVEL) {
			return false;	// leaves cannot have children
		}
		int candidateLevel = getLevelForCategory(candidate.getType());
		if (currentLevel == -1) {
			// folders can only contain other folders or files
			return candidateLevel <= FILE_LEVEL;
		}
		return currentLevel < candidateLevel;
	}
	
	/**
	 * @return the level of indentation for this category (e.g. headline level). Nodes with lower
	 * level will contain nodes with higher levels.
	 */
	protected int getLevelForCategory(String category) {
		if (FOLDER_CATEGORY.equals(category)) {
			return -1;
		}
		if (PAGE_CATEGORY.equals(category)) {
			return FILE_LEVEL;	// page can contain anything that is not a folder
		}
		if (FLOWER_BLOCK_CATEGORY.equals(category)) {
			return LEAF_LEVEL;	// cannot have children
		}
		return -1;
	}
}
