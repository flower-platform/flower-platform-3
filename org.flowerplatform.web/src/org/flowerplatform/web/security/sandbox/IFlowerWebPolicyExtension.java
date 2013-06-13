package org.flowerplatform.web.security.sandbox;

import java.security.Permission;

public interface IFlowerWebPolicyExtension {

	boolean impliesWithoutTreePathCheck_isExecutable(Permission permission);
	
}
