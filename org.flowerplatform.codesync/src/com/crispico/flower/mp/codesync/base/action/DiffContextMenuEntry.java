package com.crispico.flower.mp.codesync.base.action;

import java.util.List;


/**
 * @flowerModelElementId _oTKSoLOVEeCa6MHp-4L_Cw
 */
public class DiffContextMenuEntry {

	private String label;
	
	private List<DiffActionEntry> actionEntries;

	private boolean isRight;
	
	private int color = 0xFFFFFF;
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<DiffActionEntry> getActionEntries() {
		return actionEntries;
	}

	public void setActionEntries(List<DiffActionEntry> actionEntries) {
		this.actionEntries = actionEntries;
	}

	public boolean isRight() {
		return isRight;
	}

	public void setRight(boolean isRight) {
		this.isRight = isRight;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

}