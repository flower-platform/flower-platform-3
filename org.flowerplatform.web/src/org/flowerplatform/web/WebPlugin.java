package org.flowerplatform.web;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServlet;

import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.web.database.DatabaseManager;
import org.flowerplatform.web.security.mail.SendMailService;
import org.flowerplatform.web.security.service.GroupService;
import org.flowerplatform.web.security.service.OrganizationService;
import org.flowerplatform.web.security.service.PermissionService;
import org.flowerplatform.web.security.service.UserService;
import org.osgi.framework.BundleContext;

/**
 * @author Cristi
 * @author Mariana
 */
public class WebPlugin extends AbstractFlowerJavaPlugin {

	protected static WebPlugin INSTANCE;
	
	public static WebPlugin getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Only set by plugin test project activator, to avoid {@link ClassNotFoundException}
	 * for <code>BridgeServlet</code>.
	 * 
	 * @author Mariana
	 */
	private String TESTING_FLAG = "testing";
	
	private EclipseDispatcherServlet eclipseDispatcherServlet = new EclipseDispatcherServlet();
	
	private FlowerWebProperties flowerWebProperties;
	
	private DatabaseManager databaseManager;
	
	public WebPlugin() {
		super();
		INSTANCE = this;
		
		flowerWebProperties = new FlowerWebProperties();
		databaseManager = new DatabaseManager();
	}
	
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
	
	/**
	 * @author Mariana
	 */
	public FlowerWebProperties getFlowerWebProperties() {
		return flowerWebProperties;
	}

	/**
	 * @author Mariana
	 */
	public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}

	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		UserService.getInstance().getObservable().addObserver(PermissionService.getInstance().getSecurityEntityObserver());
		GroupService.getInstance().getObservable().addObserver(PermissionService.getInstance().getSecurityEntityObserver());
		OrganizationService.getInstance().getObservable().addObserver(PermissionService.getInstance().getSecurityEntityObserver());
		OrganizationService.getInstance().getObservable().addObserver(OrganizationService.getInstance().getOrganizationObserver());	
		// do the initializations after the bundle is activated, because we need the resources bundle
		if (bundleContext.getProperty(TESTING_FLAG) == null) {
			databaseManager.initialize();
			invokeBridgeServletMethod("registerServletDelegate", eclipseDispatcherServlet);
		}
		SendMailService.getInstance().initializeProperties();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		if (bundleContext.getProperty(TESTING_FLAG) == null) {
			invokeBridgeServletMethod("unregisterServletDelegate", eclipseDispatcherServlet);
		}
		INSTANCE = null;
	}

}
