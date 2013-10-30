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
package org.flowerplatform.codesync.properties.remote;

import org.flowerplatform.properties.remote.SelectedItem;

/**
 * Loaded descriptor selection.
 * 
 * @author Mircea Negreanu
 */
public class StringSelectedItem extends SelectedItem {

	private String descriptorName;
	
	private Boolean isRelation;
	
	public StringSelectedItem(String descriptorName, Boolean isRelation) {
		super();
		
		this.descriptorName = descriptorName;
		this.isRelation = isRelation;
	}
	
	public StringSelectedItem() {
		
	}

	public String getDescriptorName() {
		return descriptorName;
	}

	public void setDescriptorName(String descriptorName) {
		this.descriptorName = descriptorName;
	}

	public Boolean getIsRelation() {
		return isRelation;
	}

	public void setIsRelation(Boolean isRelation) {
		this.isRelation = isRelation;
	}
}
