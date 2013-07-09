package org.flowerplatform.web.git.history.remote.dto;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.web.git.remote.dto.CommitDto;

/**
 *	@author Cristina Constantinescu
 */
public class HistoryCommitMessageDto {

	private List<CommitDto> parents;
	
	private List<CommitDto> children;

	public List<CommitDto> getParents() {
		if (parents == null) {
			parents = new ArrayList<CommitDto>();
		}	
		return parents;
	}

	public void setParents(List<CommitDto> parents) {
		this.parents = parents;
	}

	public List<CommitDto> getChildren() {
		if (children == null) {
			children = new ArrayList<CommitDto>();
		}	
		return children;
	}

	public void setChildren(List<CommitDto> children) {		
		this.children = children;
	}
		
}
