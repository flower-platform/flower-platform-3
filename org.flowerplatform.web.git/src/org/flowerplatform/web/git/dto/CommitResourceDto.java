package org.flowerplatform.web.git.dto;

/**
 *	@author Cristina Constantinescu
 */
public class CommitResourceDto {
	
	public static final int ADDED = 0;
	public static final int CHANGED = 1;
	public static final int REMOVED = 2;
	public static final int CONFLICTING = 3;
	public static final int MODIFIED = 4;
	public static final int UNTRACKED = 5;
	public static final int MISSING = 6;
	
	private String path;
	
	private String label;
	
	private String image;
	
	private int state;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
}