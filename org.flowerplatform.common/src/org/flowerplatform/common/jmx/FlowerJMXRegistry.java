package org.flowerplatform.common.jmx;

import java.lang.management.ManagementFactory;

import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristina Constantinescu
 */
public class FlowerJMXRegistry {
	
	private static Logger logger = LoggerFactory.getLogger(FlowerJMXRegistry.class);
		
	private String commonMXBeanLocation;
	
	public FlowerJMXRegistry(String commonMXBeanLocation) {		
		this.commonMXBeanLocation = commonMXBeanLocation;
	}

	public void registerJMXBean(Object object, String type, String id) {
		if (!"true".equals(System.getProperty("com.sun.management.jmxremote"))) { // not true 
			return;
		}
		try {
			ManagementFactory.getPlatformMBeanServer().registerMBean(object, 
					new ObjectName(commonMXBeanLocation
									+ ":type=" + type + "," 
									+ "id=" + id));
		} catch (Throwable e) {
			logger.error("Could not register as an MBean location=" + commonMXBeanLocation + ", type=" + type + ", id=" + id, e);
		} 		
	}
	
}
