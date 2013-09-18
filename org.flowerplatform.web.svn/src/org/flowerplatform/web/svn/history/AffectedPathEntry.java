package org.flowerplatform.web.svn.history;

/**
 * Holds data to be displayed as an entry in the SVN affected paths table.
 * 
 * @author Victor Badila
 * 
 * Corresponds to a {@link LogEntryChangePath}.
 */
public class AffectedPathEntry {
	
	private String action;
	
	private String affectedPath;
	
	private String description;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAffectedPath() {
		return affectedPath;
	}

	public void setAffectedPath(String affectedPath) {
		this.affectedPath = affectedPath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}