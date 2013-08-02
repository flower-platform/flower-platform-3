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
package org.flowerplatform.blazeds.heartbeat;

import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.FlowerWebProperties.AddIntegerProperty;

public class HeartbeatProperties {
	
	/**
	 * Period for checking if clients are still alive.
	 * 
	 */
	public static final String SERVER_HEARTBEAT_PERIOD = "server.heartbeat.period"; // milliseconds
	
	/**
	 * Period for which if a client did not have any activity, even though it was connected,
	 * then we kick it out.
	 */
	public static final String CLIENT_NO_ACTIVITY_PERIOD = "client.no.activity.period"; // milliseconds
	
	public static final String WARN_ABOUT_NO_ACTIVITY_INTERVAL = "warn.about.no.activity.interval"; // milliseconds
	
	/**
	 * Periodicity in milliseconds for the client to send signals to the server that it is still alive.
	 * Used on client.
	 */
	public static final String CLIENT_HEARTBEAT_PERIOD = "client.heartbeat.period";
	
	/**
	 * Periodicity in milliseconds for trying to establish the connection again
	 * Used on client.
	 */
	public static final String CLIENT_CONNECTING_RETRY_PERIOD = "client.connecting.retry.period";
	
	/**
	 * Time in milliseconds to wait in case of switching user. Helps for avoiding certain blazeDS problems. 
	 * Used on client.
	 */
	public static final String CLIENT_SWITCH_USER_INTERVAL = "client.switch.user.interval";
	
	/**
	 * Time in milliseconds to wait in case that the connection did not fail and there were problems sending the objects.
	 * Used on client.
	 */
	public static final String CLIENT_DISPATCH_UNDELIVERED_OBJECTS_INTERVAL = "client.dispatch.undelivered.objects.interval";
	
	// TODO problema, nu merg schimbate proprietatile
	public static void initializeCommunicationProperties() {
		CommonPlugin.getInstance().getFlowerWebProperties().addProperty(new AddIntegerProperty(SERVER_HEARTBEAT_PERIOD, "60000"));
		CommonPlugin.getInstance().getFlowerWebProperties().addProperty(new AddIntegerProperty(CLIENT_NO_ACTIVITY_PERIOD, "900000"));
		CommonPlugin.getInstance().getFlowerWebProperties().addProperty(new AddIntegerProperty(WARN_ABOUT_NO_ACTIVITY_INTERVAL, "60000"));
		
		CommonPlugin.getInstance().getFlowerWebProperties().addProperty(new AddIntegerProperty(CLIENT_HEARTBEAT_PERIOD, "15000")); 
		CommonPlugin.getInstance().getFlowerWebProperties().addProperty(new AddIntegerProperty(CLIENT_CONNECTING_RETRY_PERIOD, "3000"));
		CommonPlugin.getInstance().getFlowerWebProperties().addProperty(new AddIntegerProperty(CLIENT_SWITCH_USER_INTERVAL, "300"));
		CommonPlugin.getInstance().getFlowerWebProperties().addProperty(new AddIntegerProperty(CLIENT_DISPATCH_UNDELIVERED_OBJECTS_INTERVAL, "2000"));
	}
}