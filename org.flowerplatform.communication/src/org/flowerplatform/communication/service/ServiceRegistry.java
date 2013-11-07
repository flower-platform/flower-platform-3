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
package org.flowerplatform.communication.service;


import java.util.HashMap;
import java.util.Map;

import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.jmx.FlowerJMXRegistry;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.ICommunicationChannelLifecycleListener;
import org.flowerplatform.communication.stateful_service.StatefulService;

/**
 * Central registry for application services, that may be invoked from
 * the Flex client, using {@link InvokeServiceMethodServerCommand}.
 * 
 * <p>
 * <strong>IMPORTANT NOTE:</strong> The service mechanism is a replacement/short hand of the 
 * original command system. The advantage is that there is less code to write (e.g. 3 methods
 * instead of 3 + 3 classes). The disadvantage is that there is no type checking (on the Flex side).
 * So <strong>please pay special attention when the signature of a service method changes</strong>,
 * because you won't get any compiler error. Look carefully in the whole workspace (using CTRL + H
 * and the name of the service or method), in order to modify ALL places that invokes that particular
 * method.
 * 
 * @see The class diagram, the mindmap and the wiki documentation.
 * @see InvokeServiceMethodServerCommand
 * 
 * @author Cristi
 * 
 */
public class ServiceRegistry {
	
	/**
	 * Contains the registered services.
	 * 
	 * 
	 */
	private Map<String, Object> map = new HashMap<String, Object>();

	/**
	 * Registers a new service.
	 * 
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */
	public void registerService(String id, Object serviceInstance) {
		map.put(id, serviceInstance);
		
		if (serviceInstance instanceof StatefulService) {				
			CommonPlugin.getInstance().getFlowerJMXRegistry().registerJMXBean(serviceInstance, "stateful_service", id);
		}		
	}

	/**
	 * Gets the service by its string id.
	 * 
	 * @return The registered service or <code>null</code> if nothing found.
	 * 
	 */
	public Object getService(String id) {
		return map.get(id);
	}
}