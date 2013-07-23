/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
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