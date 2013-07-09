package com.crispico.flower.mp.codesync.base.communication;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.communication.tree.remote.TreeNode;

import com.crispico.flower.mp.codesync.base.action.DiffContextMenuEntry;

/**
 * @flowerModelElementId _YHtzgK1aEeCJde5_hCYUsw
 */
public class DiffTreeNode extends TreeNode {

	private int topLeftColor = 0xFFFFFF;
	
	private int topRightColor = 0xFFFFFF;
	
	private int bottomLeftColor = 0xFFFFFF;
	
	private int bottomRightColor = 0xFFFFFF;
	
	private int crossColor = 0xFFFFFF;
	
	private String toolTip;

	private List<DiffContextMenuEntry> contextMenuEntries = new ArrayList<DiffContextMenuEntry>();

	public int getTopLeftColor() {
		return topLeftColor;
	}

	public void setTopLeftColor(int topLeftColor) {
		this.topLeftColor = topLeftColor;
	}

	public int getTopRightColor() {
		return topRightColor;
	}

	public void setTopRightColor(int topRightColor) {
		this.topRightColor = topRightColor;
	}

	public int getBottomLeftColor() {
		return bottomLeftColor;
	}

	public void setBottomLeftColor(int bottomLeftColor) {
		this.bottomLeftColor = bottomLeftColor;
	}

	public int getBottomRightColor() {
		return bottomRightColor;
	}

	public void setBottomRightColor(int bottomRightColor) {
		this.bottomRightColor = bottomRightColor;
	}

	public String getToolTip() {
		return toolTip;
	}

	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	public int getCrossColor() {
		return crossColor;
	}

	public void setCrossColor(int crossColor) {
		this.crossColor = crossColor;
	}
	
	public void setRightColor(int rightColor) {
		this.topRightColor = rightColor;
		this.bottomRightColor = rightColor;
	}
	
	public void setLeftColor(int leftColor) {
		this.topLeftColor = leftColor;
		this.bottomLeftColor = leftColor;
	}

	public List<DiffContextMenuEntry> getContextMenuEntries() {
		return contextMenuEntries;
	}

	public void setContextMenuEntries(List<DiffContextMenuEntry> contextMenuEntries) {
		this.contextMenuEntries = contextMenuEntries;
	}

}
