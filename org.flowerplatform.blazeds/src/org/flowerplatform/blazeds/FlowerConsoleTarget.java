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
package org.flowerplatform.blazeds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flex.messaging.log.ConsoleTarget;

/**
 * At the moment just hides a warning from blazeds.
 * See services-config.xml, loggging section.
 * @author Sorin
 */
public class FlowerConsoleTarget extends ConsoleTarget {
	
	private static Logger logger = LoggerFactory.getLogger(FlowerConsoleTarget.class);
	
	@Override
	protected void internalLog(String message) {
		if (message.contains("because endpoint encountered a socket write error, possibly due to an unresponsive FlexClient.") ||
				message.contains("Software caused connection abort: socket write error")) {
			
			if (logger.isTraceEnabled())
				logger.trace("Hiding message :\n" + message);
		} else {
			super.internalLog(message);
		}
	}

}