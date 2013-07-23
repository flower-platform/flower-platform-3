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