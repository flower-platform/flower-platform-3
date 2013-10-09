package org.flowerplatform.orion.server;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.FlowerDispatcherServlet;
import org.flowerplatform.communication.ServletMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flex.messaging.MessageBrokerServlet;

@SuppressWarnings("serial")
public class OrionFlowerDispatcherServlet extends FlowerDispatcherServlet {

	private static Logger logger = LoggerFactory.getLogger(OrionFlowerDispatcherServlet.class);
	
	@Override
	public void init(ServletConfig arg0) throws ServletException {
		try {			
			logger.info("Starting Flower Platform Server, version {}...", CommonPlugin.VERSION);
			long time = System.currentTimeMillis();
		
			for (ServletMapping mapping : CommunicationPlugin.getInstance().getServletMappings()) {
				if (!(mapping.servlet instanceof MessageBrokerServlet)) {
					mapping.servlet.init(arg0);						
				}
			}
		
			logger.info("Started Flower Platform Server in {} ms", System.currentTimeMillis() - time);			
		} catch (Exception e) {
			logger.error("SERVER FAILED TO INITIALIZE!!! A critical error occured while initializing the FlowerDispatcherServlet. You need to repair the error and restart the server.", e);		
		}		
	}
}
