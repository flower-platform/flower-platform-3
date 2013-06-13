package org.flowerplatform.web.git;

import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Cristina Constantienscu
 */
public class GitPlugin extends AbstractFlowerJavaPlugin {

	protected static GitPlugin INSTANCE;
	
	protected GitCommonPlugin gitCommonPlugin = new GitCommonPlugin();
		
	public static GitPlugin getInstance() {
		return INSTANCE;
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		INSTANCE = this;		
		gitCommonPlugin.start(context);
	}
		
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		
		gitCommonPlugin.stop(context);
		INSTANCE = null;		
	}
	
}
