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