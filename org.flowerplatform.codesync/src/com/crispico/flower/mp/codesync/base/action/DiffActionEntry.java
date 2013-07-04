package com.crispico.flower.mp.codesync.base.action;


/**
 * @flowerModelElementId _a99hMLOXEeCa6MHp-4L_Cw
 */
public class DiffActionEntry {
	private int actionType = -1;
	
	private String label;
	
	private int diffIndex = -1;
	
	private boolean enabled;
	
	private boolean default1;

	public int getActionType() {
		return actionType;
	}

	public void setActionType(int actionId) {
		this.actionType = actionId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getDiffIndex() {
		return diffIndex;
	}

	public void setDiffIndex(int diffIndex) {
		this.diffIndex = diffIndex;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isDefault1() {
		return default1;
	}

	public void setDefault1(boolean default1) {
		this.default1 = default1;
	}
}