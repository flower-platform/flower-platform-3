/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.communication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.common.util.RunnableWithParam;
import org.flowerplatform.communication.channel.CommunicationChannelManager;
import org.flowerplatform.communication.service.ServiceRegistry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristi
 */
public class CommunicationPlugin extends AbstractFlowerJavaPlugin {

	private Logger logger = LoggerFactory.getLogger(CommunicationPlugin.class);
	
	protected static CommunicationPlugin INSTANCE;
	
	public static CommunicationPlugin getInstance() {
		return INSTANCE;
	}
	
	public static final String SERVLET_EXTENSION_POINT = "org.flowerplatform.communication.servlet";
	
	public static final String SERVICE_EXTENSION_POINT = "org.flowerplatform.communication.service";
	
	/**
	 * @author Mariana
	 */
	public static final String AUTHENTICATOR_EXTENSION_POINT = "org.flowerplatform.communication.authenticator";
	
	private List<ServletMapping> servletMappings;
	
	private CommunicationChannelManager communicationChannelManager = new CommunicationChannelManager();
	
	private ServiceRegistry serviceRegistry = new ServiceRegistry();
	
	private List<Runnable> allServicesStartedListeners = new ArrayList<Runnable>();
	
	/**
	 * @author Mariana
	 */
	private IAuthenticator authenticator;
	
	private ScheduledExecutorServiceFactory scheduledExecutorServiceFactory = new ScheduledExecutorServiceFactory();

	public List<ServletMapping> getServletMappings() {
		return servletMappings;
	}
	
	public CommunicationChannelManager getCommunicationChannelManager() {
		return communicationChannelManager;
	}

	public ServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}
	
	/**
	 * @author Mariana
	 */
	public IAuthenticator getAuthenticator() {
		return authenticator;
	}

	public ScheduledExecutorServiceFactory getScheduledExecutorServiceFactory() {
		return scheduledExecutorServiceFactory;
	}
	
	public List<Runnable> getAllServicesStartedListeners() {
		return allServicesStartedListeners;
	}

	public static final ThreadLocal<IPrincipal> tlCurrentPrincipal = new ThreadLocal<IPrincipal>();

	/**
	 * @author Cristian Spiescu
	 * @author Mariana Gheorghe
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;

		initExtensionPoint_servlet();
		initExtensionPoint_authenticator();
		
		if (bundleContext.getBundle(0).getState() == Bundle.ACTIVE) {
			// If Eclipse is already started before this plugin is activated (happens during testing or
			// running the app from the IDE), it's safe to start the services.
			initializeServicesAndNotifyServicesStartedListeners();
		} else {
			// Wait until Eclipse is started (see comment below).
			bundleContext.addFrameworkListener(new FrameworkListener() {
			@Override
			public void frameworkEvent(FrameworkEvent event) {
				if (event.getType() != FrameworkEvent.STARTLEVEL_CHANGED) {
					return;
				}
				
				CommunicationPlugin.this.initializeServicesAndNotifyServicesStartedListeners();
			}
		});
		}
	}
	
	protected void initializeServicesAndNotifyServicesStartedListeners() {
		try {
			// When STARTLEVEL_CHANGED event happens, Eclipse seems to have finished starting.
			// It is now that we start the services. If we didn't do this, e.g. of thing that
			// would go wrong: this plugin is activated because FlowerDispatcherServlet is referenced
			// (is superclass) of EclipseDispatcherServlet, which is referenced somewhere in the
			// initializer or constructor of WebPlugin. => the services would be instantiated, including
			// some services from .web, which would want to use the resourceBundle, which hasn't been
			// yet initialized because WebPlugin.start() hasn't been called yet.
			initExtensionPoint_service();
			
			// notify the listeners that the services have been started
			for (Runnable listener : allServicesStartedListeners) {
				listener.run();
			}
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	protected void initExtensionPoint_servlet() throws CoreException {
		servletMappings = new ArrayList<ServletMapping>();
		IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor(SERVLET_EXTENSION_POINT);
		for (IConfigurationElement configurationElement : configurationElements) {
			ServletMapping servletMapping = new ServletMapping();
			servletMapping.priority = Integer.parseInt(configurationElement.getAttribute("priority"));
			servletMapping.mappingEvaluator = (RunnableWithParam<Boolean, HttpServletRequest>) configurationElement.createExecutableExtension("mappingEvaluator");
			servletMapping.servlet = (Servlet) configurationElement.createExecutableExtension("servlet");
			servletMappings.add(servletMapping);
			logger.debug("Added servlet mapping with priority = {} for servlet class = {}", servletMapping.priority, servletMapping.servlet.getClass());
		}
		Collections.sort(servletMappings, new Comparator<ServletMapping>() {
			@Override
			public int compare(ServletMapping arg0, ServletMapping arg1) {
				return Integer.compare(arg0.priority, arg1.priority);
			}
		});
	}
	
	protected void initExtensionPoint_service() throws CoreException {
		IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor(SERVICE_EXTENSION_POINT);
		for (IConfigurationElement configurationElement : configurationElements) {
			String id = configurationElement.getAttribute("id");
			Object serviceInstance = configurationElement.createExecutableExtension("serviceClass");
			getServiceRegistry().registerService(id, serviceInstance);
			logger.debug("Added service with id = {} with class = {}", id, serviceInstance.getClass());
		}
		
	}

	/**
	 * @author Mariana
	 */
	protected void initExtensionPoint_authenticator() throws CoreException {
		IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor(AUTHENTICATOR_EXTENSION_POINT);
		for (IConfigurationElement configurationElement : configurationElements) {
			String id = configurationElement.getAttribute("id");
			Object authenticatorInstance = configurationElement.createExecutableExtension("authenticatorClass");
			authenticator = (IAuthenticator) authenticatorInstance;
			logger.debug("Added authenticator with id = {} with class = {}", id, authenticatorInstance.getClass());
		}
	}
	
	public void stop(BundleContext bundleContext) throws Exception {
		scheduledExecutorServiceFactory.dispose();
		super.stop(bundleContext);
		INSTANCE = null;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// do nothing, because we don't have messages (yet)
	}
	
}