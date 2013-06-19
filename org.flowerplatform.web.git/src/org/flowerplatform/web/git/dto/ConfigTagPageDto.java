package org.flowerplatform.web.git.dto;

import java.util.List;

/**
 *	@author Cristina Constantinescu
 */
public class ConfigTagPageDto {

	private List<CommitDto> commits;

	private int initialSelectedIndex;
	
	private boolean addEmptyLine;
	
	public List<CommitDto> getCommits() {
		return commits;
	}

	public void setCommits(List<CommitDto> commits) {
		this.commits = commits;
	}

	public int getInitialSelectedIndex() {
		return initialSelectedIndex;
	}

	public void setInitialSelectedIndex(int initialSelectedIndex) {
		this.initialSelectedIndex = initialSelectedIndex;
	}

	public boolean getAddEmptyLine() {
		return addEmptyLine;
	}

	public void setAddEmptyLine(boolean addEmptyLine) {
		this.addEmptyLine = addEmptyLine;
	}
		
}
