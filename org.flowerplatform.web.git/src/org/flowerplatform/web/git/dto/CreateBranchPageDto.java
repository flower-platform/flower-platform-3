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
package org.flowerplatform.web.git.dto;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.web.git.remote.dto.GitRef;

/**
 *	@author Cristina Constantinescu
 */
public class CreateBranchPageDto {

	private List<GitRef> refs;
	
	private int selectedIndex;
	
	private String prefixName;

	public List<GitRef> getRefs() {
		if (refs == null) {
			refs = new ArrayList<GitRef>();
		}
		return refs;
	}

	public void setRefs(List<GitRef> refs) {
		this.refs = refs;
	}
	
	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	public String getPrefixName() {
		return prefixName;
	}

	public void setPrefixName(String prefixName) {
		this.prefixName = prefixName;
	}
		
}