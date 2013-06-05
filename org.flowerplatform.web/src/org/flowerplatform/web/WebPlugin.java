package org.flowerplatform.web;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServlet;

import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Cristi
 */
public class WebPlugin extends AbstractFlowerJavaPlugin {

	protected static WebPlugin INSTANCE;
	
	public static WebPlugin getInstance() {
		return INSTANCE;
	}
	
	private EclipseDispatcherServlet eclipseDispatcherServlet = new EclipseDispatcherServlet();

	/**
	 * We use reflection because there is no compile time dependency,
	 * which would generate a circular project dependency (or would imply
	 * exotic projects setup.
	 * @flowerModelElementId _TOSNIk4oEeCAKrFO_vL8Gg
	 */
	private void invokeBridgeServletMethod(String methodName, Object parameter) {
		try {
			@SuppressWarnings("rawtypes")
			Class bridgeServletClass = Class.forName("org.eclipse.equinox.servletbridge.BridgeServlet");
			@SuppressWarnings("unchecked")
			Method registerServletDelegateMethod = bridgeServletClass.getDeclaredMethod(methodName, new Class[] {HttpServlet.class});
			registerServletDelegateMethod.invoke(null, parameter);
		} catch (Exception e) {
			throw new RuntimeException("Error registering/unregistering to webapp bridge servlet.", e);
		}
	}
	
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		invokeBridgeServletMethod("registerServletDelegate", eclipseDispatcherServlet);
		INSTANCE = this;
	}

	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		invokeBridgeServletMethod("unregisterServletDelegate", eclipseDispatcherServlet);
		INSTANCE = null;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// do nothing, because we don't have messages (yet)
	}

}
