package org.flowerplatform.web.git.remote.dto;

import java.util.List;

import org.flowerplatform.web.git.remote.dto.GitActionDto;

/**
 *	@author Cristina Constantinescu
 */
public class CommitPageDto extends GitActionDto {
	
	private String author;
	
	private String committer;
	
	private String message;
	
	private List<CommitResourceDto> commitResources;

	public List<CommitResourceDto> getCommitResources() {
		return commitResources;
	}

	public void setCommitResources(List<CommitResourceDto> commitResources) {
		this.commitResources = commitResources;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
