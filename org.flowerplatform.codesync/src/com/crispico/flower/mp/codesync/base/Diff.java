package com.crispico.flower.mp.codesync.base;

/**
 * @flowerModelElementId _rQSIQK75EeCDbs_ALiXk1Q
 */
public class Diff {

	private Object feature;
	
	private Match parentMatch;
	
	private boolean isLeftModified;
	
	private boolean isRightModified;
	
	/**
	 * @flowerModelElementId _rQZdAK75EeCDbs_ALiXk1Q
	 */
	private boolean isConflict;

	public Object getFeature() {
		return feature;
	}

	public void setFeature(Object feature) {
		this.feature = feature;
	}

	public Match getParentMatch() {
		return parentMatch;
	}

	public boolean isLeftModified() {
		return isLeftModified;
	}

	public void setLeftModified(boolean isLeftModified) {
		this.isLeftModified = isLeftModified;
	}

	public boolean isRightModified() {
		return isRightModified;
	}

	public void setRightModified(boolean isRightModified) {
		this.isRightModified = isRightModified;
	}

	public boolean isConflict() {
		return isConflict;
	}

	public void setConflict(boolean isConflict) {
		this.isConflict = isConflict;
	}

	public void setParentMatch(Match parentMatch) {
		this.parentMatch = parentMatch;
	}
	
}
