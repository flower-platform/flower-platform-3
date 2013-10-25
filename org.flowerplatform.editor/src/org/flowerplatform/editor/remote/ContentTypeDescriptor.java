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