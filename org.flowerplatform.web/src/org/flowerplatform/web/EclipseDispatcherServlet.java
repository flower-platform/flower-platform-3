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

import java.util.Locale;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.flowerplatform.communication.FlowerDispatcherServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristian Spiescu
 * @author Cristina Constantinescu
 */
@SuppressWarnings("serial")
public class EclipseDispatcherServlet extends FlowerDispatcherServlet {

	private static Logger logger = LoggerFactory.getLogger(EclipseDispatcherServlet.class);
	
	@Override
	public void init(ServletConfig arg0) throws ServletException {
		Locale.setDefault(Locale.ROOT); // Hiding warning about flex/messaging/errors.properties and flex/data/errors.propertiesnot localized.
		try {
			String tomcatVersion = arg0.getServletContext().getServerInfo(); // "Apache Tomcat/7.0.27"
			tomcatVersion = tomcatVersion.substring(tomcatVersion.indexOf("/") + 1); // "7.0.27"
			if (tomcatVersion.startsWith("7") && !"7.0.27".equals(tomcatVersion)) // BlazeDS 4 streaming seems not to work with Tomcat 7.0.32 but works with Tomcat 7.0.27
				throw new IllegalStateException("When using Tomcat 7, Flower Platform should be deployed only on 7.0.27 version!");
		} catch (Exception e) {
			logger.error("SERVER FAILED TO INITIALIZE!!! A critical error occured while initializing the server. You need to repair the error and restart the server.", e);		
		}
		super.init(arg0);		
	}

}