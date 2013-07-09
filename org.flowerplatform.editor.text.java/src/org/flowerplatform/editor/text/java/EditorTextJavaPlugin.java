package org.flowerplatform.editor.text.java;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Mariana
 */
public class EditorTextJavaPlugin extends AbstractFlowerJavaPlugin {
	
	protected static EditorTextJavaPlugin INSTANCE;
	
	public static EditorTextJavaPlugin getInstance() {
		return INSTANCE;
	}
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}
	
	@Override
	public void registerMessageBundle() throws Exception {
		// no messages
	}
}
