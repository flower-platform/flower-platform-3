package org.flowerplatform.web.git;

import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Cristina Constantinescu
 */
public class GitCommonPlugin extends AbstractFlowerJavaPlugin {
	
	protected static GitCommonPlugin INSTANCE;
	
	private GitUtils utils = new GitUtils();
	
	public static GitCommonPlugin getInstance() {
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
