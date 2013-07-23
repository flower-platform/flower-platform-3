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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.compare.internal.DocLineComparator;
import org.eclipse.compare.rangedifferencer.RangeDifference;
import org.eclipse.compare.rangedifferencer.RangeDifferencer;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;

import astcache.wiki.FlowerBlock;
import astcache.wiki.WikiPackage;

import com.crispico.flower.mp.codesync.base.Diff;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana
 */
public class WikiDiff extends Diff {

	public enum Direction {
		
		FROM_ANCESTOR_TO_LEFT,
		FROM_ANCESTOR_TO_RIGHT,
		FROM_LEFT_TO_RIGHT,
		FROM_RIGHT_TO_LEFT
		
	}
	
	private RangeDifference[] diffs;
	
	private boolean leftResolved, rightResolved;
	
	private IDocument ancestor, left, right;
	
	private TextEdit leftEdit = new MultiTextEdit(), rightEdit = new MultiTextEdit();
	
	private List<FlowerBlock> flowerBlocks = new ArrayList<FlowerBlock>();
	
	private String technology;
	
	public WikiDiff(String ancestor, String left, CodeSyncElement right, String technology) {
		this.ancestor = new Document(ancestor);
		this.left = new Document(left);
		this.right = new Document(WikiPlugin.getInstance().getWikiText(right, technology));
		
		long time = new Date().getTime();
		
		// compute range differences
		boolean ignoreWhiteSpace = false;
		diffs = RangeDifferencer.findRanges(
				new DocLineComparator(this.ancestor, null, ignoreWhiteSpace),
				new DocLineComparator(this.left, null, ignoreWhiteSpace),
				new DocLineComparator(this.right, null, ignoreWhiteSpace));
		
		setFeature(WikiPackage.eINSTANCE.getPage_InitialContent());
		
		this.technology = technology;
		
		// mark conflicted flower blocks
		if (right != null) {
			getFlowerBlocks(right, flowerBlocks);
		}
		for (RangeDifference diff : diffs) {
			switch (diff.kind()) {
			case RangeDifference.CONFLICT:
				// if the conflict is in a flower block, ignore it, but mark the node
				boolean conflictInFlowerBlock = false;
				for (FlowerBlock block : flowerBlocks) {
					if (overlap(block, diff)) {
						block.setConflict(true);
						conflictInFlowerBlock = true;
					}
				}
				if (!conflictInFlowerBlock) {
					setConflict(true);
					setLeftModified(true);
					setRightModified(true);
				}
				break;
			case RangeDifference.LEFT:
				setLeftModified(true);
				break;
			case RangeDifference.RIGHT:
				setRightModified(true);
				break;
			case RangeDifference.ANCESTOR:
				setLeftModified(true);
				setRightModified(true);
				break;
			}
		}
	}
	
	public WikiDiff(WikiDiff other) {
		this.ancestor = other.ancestor;
		this.left = other.left;
		this.right = other.right;
		this.flowerBlocks = other.flowerBlocks;
		this.technology = other.technology;
		this.diffs = other.diffs;
		this.leftEdit = other.leftEdit;
		this.rightEdit = other.rightEdit;
		this.leftResolved = other.leftResolved;
		this.rightResolved = other.rightResolved;
		this.setLeftModified(other.isLeftModified());
		this.setRightModified(other.isRightModified());
		this.setConflict(other.isConflict());
		this.setFeature(other.getFeature());
		this.setParentMatch(other.getParentMatch());
	}
	
	public RangeDifference[] getDiffs() {
		return diffs;
	}
	
	/**
	 * Returns the content of the left document, after being transformed to a wiki tree using the {@link #flowerBlocks}.
	 * This is to correctly synchronize any existing Flower blocks on the wiki page.
	 */
	public String getLeft() {
		save(true);
		String wikiText = left.get();
		CodeSyncElement root = (CodeSyncElement) getParentMatch().getLeft();
		WikiPlugin.getInstance().getWikiPageTree(wikiText, root, technology, flowerBlocks);
		return WikiPlugin.getInstance().getWikiText(root, technology);
	}

	/**
	 * Creates the page nodes corresponding to the content in the right document, rooted at the given
	 * {@link CodeSyncElement}.
	 */
	public void getRight(CodeSyncElement page) {
		save(false);
		WikiPlugin.getInstance().getWikiPageTree(right.get(), page, technology, flowerBlocks);
	}
	
	public boolean applyAll(CodeSyncElement page, boolean leftToRight) {
		if (isResolved(leftToRight)) {
			return true;
		}
		boolean rslt = true;
		for (RangeDifference diff : diffs) {
			if (diff.kind() != RangeDifference.NOCHANGE && diff.kind() != RangeDifference.CONFLICT) {
				Direction direction = getDefaultDirection(diff);
				if (allowDirection(direction, leftToRight)) {
					rslt = apply(diff, direction) && rslt;
				}
			}
		}
		markResolved(leftToRight);
		
		// trigger the creation of page children
		this.getRight(page);
		
		return rslt;
	}
	
	private boolean isResolved(boolean leftToRight) {
		if (leftToRight) {
			return rightResolved;
		} else {
			return leftResolved;
		}
	}

	private void markResolved(boolean leftToRight) {
		if (leftToRight) {
			rightResolved = true;
		} else {
			leftResolved = true;
		}
	}
	
	private boolean allowDirection(Direction direction, boolean leftToRight) {
		if (direction == null) {
			return false;
		}
		if (leftToRight) {
			return direction == Direction.FROM_ANCESTOR_TO_RIGHT || direction == Direction.FROM_LEFT_TO_RIGHT;
		} else {
			return direction == Direction.FROM_ANCESTOR_TO_LEFT || direction == Direction.FROM_RIGHT_TO_LEFT;
		}
	}

	public boolean apply(RangeDifference diff, Direction direction) {
		if (direction == null) {
			return true; // nothing to do
		}
		DocumentAndPosition from = getFromPosition(diff, direction);
		DocumentAndPosition to = getToPosition(diff, direction);
		createAndAddTextEdit(from, to);
		return true;
	}
	
	private void save(boolean onLeft) {
		try {
			if (onLeft) {
				leftEdit.apply(left);
				leftEdit = new MultiTextEdit();
			} else {
				rightEdit.apply(right);
				rightEdit = new MultiTextEdit();
			}
		} catch (Exception e) {
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < diffs.length; i++) {
			RangeDifference diff = diffs[i];
			builder.append(String.format("\n%s\n", diff));
			switch (diff.kind()) {
			case RangeDifference.NOCHANGE:
				builder.append(getText(ancestor, diff.ancestorStart(), diff.ancestorLength()));
				break;
			case RangeDifference.LEFT:
				builder.append(String.format("\n>>>>>>> start insert \n%s\n>>>>>>> end insert\n", getText(left, diff.leftStart(), diff.leftLength())));
				builder.append(String.format("\n<<<<<<< start delete \n%s\n<<<<<<< end delete\n", getText(right, diff.rightStart(), diff.rightLength())));
				break;
			case RangeDifference.RIGHT:
				builder.append(String.format("\n<<<<<<< start insert \n%s\n<<<<<<< end insert\n", getText(right, diff.rightStart(), diff.rightLength())));
				builder.append(String.format("\n>>>>>>> start delete \n%s\n>>>>>>> end delete\n", getText(left, diff.leftStart(), diff.leftLength())));
				break;
			case RangeDifference.ANCESTOR:
				builder.append(String.format("\n+++++++ start insert \n%s\n+++++++ end insert\n", getText(left, diff.leftStart(), diff.leftLength())));
				builder.append(String.format("\n------- start delete \n%s\n------- end delete\n", getText(ancestor, diff.ancestorStart(), diff.ancestorLength())));
				break;
			case RangeDifference.CONFLICT:
				String conflict = String.format("\n>>>>>>>\n%s\n>>>>>>>\n<<<<<<<\n%s\n<<<<<<<\n", getText(left, diff.leftStart(), diff.leftLength()), getText(right, diff.rightStart(), diff.rightLength()));
				builder.append(String.format("\n******* start conflict \n%s\n******* end conflict\n", conflict));
			}
		}
		return builder.toString();
	}
	
	private String getText(IDocument document, int start, int length) {
		try {
			Position position = getPosition(document, start, length);
			return document.get(position.getOffset(), position.getLength());
		} catch (BadLocationException e) {
			return "";
		}
	}
	
	private void createAndAddTextEdit(DocumentAndPosition from, DocumentAndPosition to) {
		try {
			if (to.position.length == 0) {
				to.edit.addChild(new InsertEdit(to.position.getOffset(), from.document.get(from.position.getOffset(), from.position.getLength())));
			} else {
				if (from.position.length == 0) {
					to.edit.addChild(new DeleteEdit(to.position.getOffset(), to.position.getLength()));
				} else {
					to.edit.addChild(new ReplaceEdit(to.position.getOffset(), to.position.getLength(), from.document.get(from.position.getOffset(), from.position.getLength())));
				}
			}
		} catch (BadLocationException e) {
		}
	}
	
	private DocumentAndPosition getFromPosition(RangeDifference diff, Direction direction) {
		if (Direction.FROM_ANCESTOR_TO_LEFT == direction || Direction.FROM_ANCESTOR_TO_RIGHT == direction) {
			return new DocumentAndPosition(ancestor, null, getPosition(ancestor, diff.ancestorStart(), diff.ancestorLength()));
		}
		if (Direction.FROM_LEFT_TO_RIGHT == direction) {
			return new DocumentAndPosition(left, leftEdit, getPosition(left, diff.leftStart(), diff.leftLength()));
		}
		if (Direction.FROM_RIGHT_TO_LEFT == direction) {
			return new DocumentAndPosition(right, rightEdit, getPosition(right, diff.rightStart(), diff.rightLength()));
		}
		return null;
	}
	
	private DocumentAndPosition getToPosition(RangeDifference diff, Direction direction) {
		if (Direction.FROM_ANCESTOR_TO_LEFT == direction || Direction.FROM_RIGHT_TO_LEFT == direction) {
			return new DocumentAndPosition(left, leftEdit, getPosition(left, diff.leftStart(), diff.leftLength()));
		}
		if (Direction.FROM_ANCESTOR_TO_RIGHT == direction || Direction.FROM_LEFT_TO_RIGHT == direction) {
			return new DocumentAndPosition(right, rightEdit, getPosition(right, diff.rightStart(), diff.rightLength()));
		}
		return null;
	}
	
	private Position getPosition(IDocument document, int start, int length) {
		try {
			int offset = document.getLineOffset(start);
			int end = offset;
			if (length > 0) {
				int lastLine = Math.min(start + length - 1, document.getNumberOfLines() - 1);
				end = document.getLineOffset(lastLine) + document.getLineLength(lastLine);
			}
			return new Position(offset, end - offset);
		} catch (BadLocationException e) {
			return new Position(0, 0);
		}
	}
	
	private Direction getDefaultDirection(RangeDifference diff) {
		switch (diff.kind()) {
		case RangeDifference.LEFT:
			return Direction.FROM_LEFT_TO_RIGHT;
		case RangeDifference.RIGHT:
		case RangeDifference.CONFLICT:
			return Direction.FROM_RIGHT_TO_LEFT;
		default:
			return null;
		}
	}
	
	private void getFlowerBlocks(CodeSyncElement node, List<FlowerBlock> blocks) {
		if (node.getAstCacheElement() instanceof FlowerBlock) {
			blocks.add((FlowerBlock) node.getAstCacheElement());
		}
		for (CodeSyncElement child : node.getChildren()) {
			getFlowerBlocks(child, blocks);
		}
	}
	
	private boolean overlap(FlowerBlock block, RangeDifference diff) {
		if (block.getLineStart() > diff.rightStart()) {
			return false;
		}
		if (block.getLineEnd() < diff.rightEnd()) {
			return false;
		}
		return true;
	}
	
	class DocumentAndPosition {
		
		public IDocument document;
		public TextEdit edit;
		public Position position;
		
		public DocumentAndPosition(IDocument document, TextEdit edit, Position position) {
			this.document = document;
			this.edit = edit;
			this.position = position;
		}
	}
	
}