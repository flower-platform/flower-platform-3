package org.flowerplatform.editor.remote;

import java.util.ArrayList;
import java.util.List;

public class ContentTypeDescriptor {
	private int index;
	
	private String contentType;
	
	private List<String> compatibleEditors = new ArrayList<String>();
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getContentType() {
		return contentType;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public List<String> getCompatibleEditors() {
		return compatibleEditors;
	}
	
	public void setCompatibleEditors(List<String> compatibleEditors) {
		this.compatibleEditors = compatibleEditors;
	}

	@Override
	public String toString() {
		return String.format("[contentType = %s; index = %d; compatibleEditors = %s]", contentType, index, compatibleEditors);
	}
}
