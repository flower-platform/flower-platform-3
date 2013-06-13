package org.flowerplatform.web;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServlet;

import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.web.database.DatabaseManager;
import org.flowerplatform.web.entity.dao.Dao;
import org.flowerplatform.web.security.service.GroupService;
import org.flowerplatform.web.security.service.OrganizationService;
import org.flowerplatform.web.security.service.UserService;
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
	
	private FlowerWebProperties flowerWebProperties;
	
	private DatabaseManager databaseManager;
	
	private Dao dao;
	
	public WebPlugin() {
		super();
		INSTANCE = this;
		flowerWebProperties = new FlowerWebProperties();
		dao = new Dao();
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

	/**
	 * @author Mariana
	 */
	public Dao getDao() {
		return dao;
	}

	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		invokeBridgeServletMethod("registerServletDelegate", eclipseDispatcherServlet);
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
