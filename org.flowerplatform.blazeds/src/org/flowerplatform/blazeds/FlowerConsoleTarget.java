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
