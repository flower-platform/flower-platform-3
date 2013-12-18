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
package org.flowerplatform.idea;

import java.util.Dictionary;
import java.util.Hashtable;

import myPackage.BridgeUtilIdeaToEclipse;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.http.jetty.JettyConfigurator;
import org.eclipse.equinox.http.jetty.JettyConstants;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class FlowerJettyServer {

	private static final Logger logger = LoggerFactory.getLogger(FlowerJettyServer.class);
	
	private final String webappName = "org.flowerplatform.idea";
	
	private final String context = "/flower-platform";
	
	private final String host = "localhost";
	
	private String port;
	
	public void start() {
		logger.debug("Starting server");
		
		System.setProperty("org.mortbay.log.class", logger.getClass().getName());
		
		Dictionary<String, Object> d = new Hashtable<>();
		d.put(JettyConstants.HTTP_HOST, getHost()); // bind to localhost
		d.put(JettyConstants.HTTP_PORT, 0); 		// let Jetty chose an available port, see HttpServerManager.createHttpConnector()
		d.put(JettyConstants.CONTEXT_PATH, context);
		d.put(JettyConstants.OTHER_INFO, webappName);
		try {
			JettyConfigurator.startServer(webappName, d);
			checkBundle();
			
			BridgeUtilIdeaToEclipse.iIdeaToEclipseBridge = new IdeaToEclipseBirdge();
		} catch (Exception e) {
			logger.error("Server failed to start with error = {0}", e);
		}
	}
	
	public void stop() {
		logger.debug("Stoping server");
		
		try {
			JettyConfigurator.stopServer(webappName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Server failed to stop with error = {0}", e);
		}
	}
	
	/**
	 * @throws BundleException 
	 * @throws InvalidSyntaxException 
	 * @see JettyHelpServer#checkBundle()
	 */
	private void checkBundle() throws BundleException, InvalidSyntaxException {
		Bundle bundle = Platform.getBundle("org.eclipse.equinox.http.registry");
		if (bundle.getState() == Bundle.RESOLVED) {
			bundle.start(Bundle.START_TRANSIENT);
		}
		// Jetty selected a port number for us
		ServiceReference[] reference = bundle.getBundleContext().
				getServiceReferences("org.osgi.service.http.HttpService", "(" 
		+ JettyConstants.OTHER_INFO + "=" + webappName + ')');
		Object assignedPort = reference[0].getProperty(JettyConstants.HTTP_PORT);
		port = (String) assignedPort;
		
		logger.debug("Started server {}", host + ":" + port);
	}
	
	public String getHost() {
		return host;
	}
	
	public String getPort() throws BundleException {
		return port;
	}
	
	public String getUrl() {
		
		return "http://" + host + ":" + port + context + "/main.jsp";
		
	}
	
}
