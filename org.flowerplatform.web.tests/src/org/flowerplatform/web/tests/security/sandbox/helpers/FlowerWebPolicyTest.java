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
package org.flowerplatform.web.tests.security.sandbox.helpers;

import java.security.PermissionCollection;
import java.security.Policy;
import java.util.Map;

import org.flowerplatform.web.security.permission.AbstractTreePermission;
import org.flowerplatform.web.security.sandbox.FlowerWebPolicy;
import org.flowerplatform.web.security.sandbox.TreePermissionCollection;

/**
 * This class is used only to have access to protected fields in superclass.
 * 
 * @author Mariana
 */
public class FlowerWebPolicyTest extends FlowerWebPolicy {

	public FlowerWebPolicyTest(Policy defaultPolicy) {
		super(defaultPolicy);
	}
	
	public Map<Class<? extends AbstractTreePermission>, TreePermissionCollection> getTreePermissionsCache() {
		return super.treePermissionsCache;
	}
	
	public Map<Long, PermissionCollection> getNormalPermissionsCache() {
		return super.normalPermissionsCache;
	}
}