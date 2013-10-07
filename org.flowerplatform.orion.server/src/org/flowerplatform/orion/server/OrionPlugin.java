package org.flowerplatform.orion.server;

import org.flowerplatform.common.CommonPlugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class OrionPlugin implements BundleActivator {

	public OrionPlugin() {				
		CommonPlugin.getInstance().initializeProperties(this.getClass().getClassLoader()
				.getResourceAsStream("META-INF/flower-orion.properties"));	
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		
	}

}
