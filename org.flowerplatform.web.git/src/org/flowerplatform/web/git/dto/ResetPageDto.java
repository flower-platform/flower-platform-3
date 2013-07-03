package org.flowerplatform.web.git.dto;

import org.flowerplatform.web.git.remote.dto.CommitDto;
import org.flowerplatform.web.git.remote.dto.GitActionDto;
import org.flowerplatform.web.git.remote.dto.GitRef;

/**
 *	@author Cristina Constantinescu
 */
public class ResetPageDto extends GitActionDto {

	private String repoName;
	
	private GitRef current;
	
	private CommitDto currentCommit;
	
	private GitRef target;

	private CommitDto targetCommit;
	
	public String getRepoName() {
		return repoName;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	public GitRef getCurrent() {
		return current;
	}

	public void setCurrent(GitRef current) {
		this.current = current;
	}

	public GitRef getTarget() {
		return target;
	}

	public void setTarget(GitRef target) {
		this.target = target;
	}

	public CommitDto getCurrentCommit() {
		return currentCommit;
	}

	public void setCurrentCommit(CommitDto currentCommit) {
		this.currentCommit = currentCommit;
	}

	public CommitDto getTargetCommit() {
		return targetCommit;
	}

	public void setTargetCommit(CommitDto targetCommit) {
		this.targetCommit = targetCommit;
	}	
	
}
