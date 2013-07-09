package org.flowerplatform.editor.java;

import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaEditorPlugin extends AbstractFlowerJavaPlugin {

	private static final Logger logger = LoggerFactory.getLogger(JavaEditorPlugin.class);
	
	protected static JavaEditorPlugin INSTANCE;
	
	public static JavaEditorPlugin getInstance() {
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

}
