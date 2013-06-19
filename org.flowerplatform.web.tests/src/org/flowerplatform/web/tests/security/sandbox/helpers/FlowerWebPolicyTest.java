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
