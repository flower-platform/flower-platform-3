package org.flowerplatform.web.git.staging.dto;

import java.util.List;

import org.flowerplatform.web.git.dto.CommitResourceDto;
import org.flowerplatform.web.git.dto.ViewInfoDto;

/**
 *	@author Cristina Constantinescu
 */
public class StagingViewInfoDto extends ViewInfoDto {
	
	private List<CommitResourceDto> unstagedChanges;
	
	private List<CommitResourceDto> stagedChanges;
	
	private String author;
	
	private String committer;
		
	public List<CommitResourceDto> getUnstagedChanges() {
		return unstagedChanges;
	}

	public void setUnstagedChanges(List<CommitResourceDto> unstagedChanges) {
		this.unstagedChanges = unstagedChanges;
	}

	public List<CommitResourceDto> getStagedChanges() {
		return stagedChanges;
	}

	public void setStagedChanges(List<CommitResourceDto> stagedChanges) {
		this.stagedChanges = stagedChanges;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCommitter() {
		return committer;
	}

	public void setCommitter(String committer) {
		this.committer = committer;
	}
		
}
