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
package org.flowerplatform.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.ServletMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * All http requests that are handled by Tomcat (i.e. BridgeServlet, which "lives" in Tomcat's
 * class loader) are relayed to this class (which "lives" in Eclipse's class loader).
 * 
 * <p>
 * By default all requests are dispatched the embedded {@link MessageBrokerServlet}. In future 
 * other servlets (+ mappings) may exist.
 * 
 * @author Cristi
 * @flowerModelElementId _TOaI8E4oEeCAKrFO_vL8Gg
 */
@SuppressWarnings("serial")
public class EclipseDispatcherServlet extends HttpServlet {

	private static Logger logger = LoggerFactory.getLogger(EclipseDispatcherServlet.class);
	
	public ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<HttpServletRequest>();
	
	protected List<Pair<String, Servlet>> servlets = new ArrayList<Pair<String, Servlet>>();
	
	/**
	 * Delegates to the embedded servlet(s) as well.
	 * @flowerModelElementId _TObXE04oEeCAKrFO_vL8Gg
	 */
	@Override
	public void init(ServletConfig arg0) throws ServletException {
		Locale.setDefault(Locale.ROOT); // Hiding warning about flex/messaging/errors.properties and flex/data/errors.propertiesnot localized.
		try {
			String tomcatVersion = arg0.getServletContext().getServerInfo(); // "Apache Tomcat/7.0.27"
			tomcatVersion = tomcatVersion.substring(tomcatVersion.indexOf("/") + 1); // "7.0.27"
			if (tomcatVersion.startsWith("7") && !"7.0.27".equals(tomcatVersion)) // BlazeDS 4 streaming seems not to work with Tomcat 7.0.32 but works with Tomcat 7.0.27
				throw new IllegalStateException("When using Tomcat 7, Flower Platform should be deployed only on 7.0.27 version!");
			
			super.init(arg0);

			logger.info("Starting Flower Platform Server, version {}...", CommonPlugin.VERSION);
			long time = System.currentTimeMillis();

			for (ServletMapping mapping : CommunicationPlugin.getInstance().getServletMappings()) {
				mapping.servlet.init(arg0);
			}

			logger.info("Started Flower Platform Server in {} ms", System.currentTimeMillis() - time);
		} catch (Exception e) {
			logger.error("SERVER FAILED TO INITIALIZE!!! A critical error occured while initializing the server. You need to repair the error and restart the server.", e);		
		}
	}
	
	@Override
	public void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		try {
			requestThreadLocal.set(arg0);
			for (ServletMapping mapping : CommunicationPlugin.getInstance().getServletMappings()) {
				if (mapping.mappingEvaluator.run(arg0)) {
					mapping.servlet.service(arg0, arg1);
					break;
				}
			}

		} catch(Throwable e) {
			logger.error("SERVER FAILED WHEN SERVING", e);
		} finally {
			requestThreadLocal.set(null);
		}
	}
	
	/**
	 * Delegates to the embedded servlet(s) as well.
	 * @flowerModelElementId _TOdMQ04oEeCAKrFO_vL8Gg
	 */
	@Override
	public void destroy() { 
		try {
			super.destroy();
			for (ServletMapping mapping : CommunicationPlugin.getInstance().getServletMappings()) {
				mapping.servlet.destroy();
			}
		} catch(Throwable e) {
			logger.error("SERVER FAILED TO DESTROY ITSELF!", e);
		}
	}
		

}