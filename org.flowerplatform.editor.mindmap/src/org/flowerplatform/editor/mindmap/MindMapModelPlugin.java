package org.flowerplatform.editor.mindmap;

import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Cristina Constantinescu
 */
public class MindMapModelPlugin extends AbstractFlowerJavaPlugin {

	protected static MindMapModelPlugin INSTANCE;
	
	public static MindMapModelPlugin getInstance() {
		return INSTANCE;
	}
	
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;		
		
	}
	
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}
	
	@Override
	public void registerMessageBundle() throws Exception {		
	}
	
}
