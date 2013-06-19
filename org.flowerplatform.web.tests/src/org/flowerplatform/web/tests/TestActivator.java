package org.flowerplatform.web.tests;

import static org.flowerplatform.web.tests.TestUtil.createDirectoriesIfNeeded;
import static org.flowerplatform.web.tests.TestUtil.getCanonicalPath;

import org.eclipse.osgi.framework.internal.core.FrameworkProperties;
import org.flowerplatform.web.WebPlugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * @author Mariana
 * @author Sorin
 */
@SuppressWarnings("restriction")
public class TestActivator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		System.setProperty("testing", "true");
		
		// FlowerFrameworkLauncher properties & Tomcat properties
		createDirectoriesIfNeeded("temp");
		createDirectoriesIfNeeded("temp/tomcat");
		System.setProperty("catalina.base", getCanonicalPath("temp/tomcat"));
		
		FrameworkProperties.setProperty("flower.server.app.location", "temp/");
		FrameworkProperties.setProperty("flower.server.app.context", "flower-dev-center");
		FrameworkProperties.setProperty("flower.linux.subversion.configuration.location", "../org.flowerplatform.web.app/WebContent/WEB-INF/eclipse/subversion_config/");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// do nothing
	}

}
