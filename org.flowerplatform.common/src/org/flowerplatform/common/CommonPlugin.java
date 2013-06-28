package org.flowerplatform.common;

import java.io.File;
import java.io.InputStream;

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
	
	/**
	 * @author Mariana
	 */
	private FlowerWebProperties flowerWebProperties;
	
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

	public String getPathRelativeToWorkspaceRoot(File file) {
		return getPathRelativeToFile(file, getWorkspaceRoot());
	}
	
	public String getPathRelativeToFile(File file, File relativeTo) {
		String relative = relativeTo.toURI().relativize(file.toURI()).getPath();
		if (relative.length() > 0 && relative.endsWith("/")) {
			relative = relative.substring(0, relative.length() - 1);
		}
		return relative;
	}
	
	/**
	 * Initialized from the web plugin.
	 * 
	 * @author Mariana
	 */
	public FlowerWebProperties getFlowerWebProperties() {
		return flowerWebProperties;
	}

	/**
	 * @author Mariana
	 */
	public void initializeProperties(InputStream inputStream) {
		flowerWebProperties = new FlowerWebProperties(inputStream);
	}
}
