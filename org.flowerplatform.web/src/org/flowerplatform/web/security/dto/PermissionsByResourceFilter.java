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
package org.flowerplatform.web.security.dto;

import org.flowerplatform.web.entity.PermissionEntity;

/**
 * @flowerModelElementId _ssrPkFczEeG6S8FiFZ8nVA
 */
public class PermissionsByResourceFilter {
	
	/**
	 * Resource - also known as name of {@link PermissionEntity}
	 * 
	 * @flowerModelElementId _wF7DUFczEeG6S8FiFZ8nVA
	 */
	private String resource;

	/**
	 * @flowerModelElementId _0W8MkFczEeG6S8FiFZ8nVA
	 */
	public String getResource() {
		return resource;
	}

	/**
	 * @flowerModelElementId _0W-BwFczEeG6S8FiFZ8nVA
	 */
	public void setResource(String resource) {
		this.resource = resource;
	}
	
}