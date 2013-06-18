package org.flowerplatform.common;

import java.io.File;

import org.eclipse.core.resources.ResourcesPlugin;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;


/**
 * @author Cristi
 */
public class CommonPlugin extends AbstractFlowerJavaPlugin {

	protected static CommonPlugin INSTANCE;
	
	public static CommonPlugin getInstance() {
		return INSTANCE;
	}
	
	public static final String VERSION = "2.0.0.M2_2013-06-04";
	
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
	}

	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}

	public File getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot().getRawLocation().makeAbsolute().toFile();
	}


}
