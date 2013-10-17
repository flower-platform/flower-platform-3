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
package org.flowerplatform.codesync.remote;

import java.util.List;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncElementDescriptor {

	private String codeSyncType;
	
	private String iconUrl;
	
	private List<String> codeSyncTypeCategories;
	
	private List<String> features;
	
	private List<String> childrenCodeSyncTypeCategories;

	public String getCodeSyncType() {
		return codeSyncType;
	}

	public void setCodeSyncType(String codeSyncType) {
		this.codeSyncType = codeSyncType;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public List<String> getCodeSyncTypeCategories() {
		return codeSyncTypeCategories;
	}

	public void setCodeSyncTypeCategories(List<String> codeSyncTypeCategories) {
		this.codeSyncTypeCategories = codeSyncTypeCategories;
	}

	public List<String> getFeatures() {
		return features;
	}

	public void setFeatures(List<String> features) {
		this.features = features;
	}

	public List<String> getChildrenCodeSyncTypeCategories() {
		return childrenCodeSyncTypeCategories;
	}

	public void setChildrenCodeSyncTypeCategories(
			List<String> childrenCodeSyncTypeCategories) {
		this.childrenCodeSyncTypeCategories = childrenCodeSyncTypeCategories;
	}
}
