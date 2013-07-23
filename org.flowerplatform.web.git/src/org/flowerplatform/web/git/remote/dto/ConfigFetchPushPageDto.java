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

import org.flowerplatform.web.git.remote.dto.GitRef;
import org.flowerplatform.web.git.remote.dto.RemoteConfig;

/**
 *	@author Cristina Constantinescu
 */
public class ConfigFetchPushPageDto {

	private GitRef ref;
	
	private List<RemoteConfig> remoteConfigs;
	
	public GitRef getRef() {
		return ref;
	}

	public void setRef(GitRef ref) {
		this.ref = ref;
	}

	public List<RemoteConfig> getRemoteConfigs() {
		return remoteConfigs;
	}

	public void setRemoteConfigs(List<RemoteConfig> remoteConfigs) {
		this.remoteConfigs = remoteConfigs;
	}
		
}