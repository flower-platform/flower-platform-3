package org.flowerplatform.web.git;

import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Cristina Constantienscu
 */
public class GitPlugin extends AbstractFlowerJavaPlugin {

	protected static GitPlugin INSTANCE;
	
	private GitUtils utils = new GitUtils();
	
	public static GitPlugin getInstance() {
		return INSTANCE;
	}

	public GitUtils getUtils() {
		return utils;
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		INSTANCE = this;		
	}
		
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		
		INSTANCE = null;		
	}
	
}
