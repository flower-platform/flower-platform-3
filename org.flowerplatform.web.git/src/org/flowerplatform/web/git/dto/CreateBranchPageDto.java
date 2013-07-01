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
