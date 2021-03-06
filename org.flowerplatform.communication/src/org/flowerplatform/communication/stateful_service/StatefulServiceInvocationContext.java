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
package org.flowerplatform.communication.stateful_service;

import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;

public class StatefulServiceInvocationContext extends ServiceInvocationContext {

	private String statefulClientId;

	public StatefulServiceInvocationContext(CommunicationChannel communicationChannel) {
		this(communicationChannel, null);
	}

	public StatefulServiceInvocationContext(CommunicationChannel communicationChannel, InvokeServiceMethodServerCommand command, String statefulClientId) {
		super(communicationChannel, command);
		this.statefulClientId = statefulClientId;
	}
	
	public StatefulServiceInvocationContext(CommunicationChannel communicationChannel, String statefulClientId) {
		this(communicationChannel, null, statefulClientId);
	}

	public String getStatefulClientId() {
		return statefulClientId;
	}

	public void setStatefulClientId(String statefulClientId) {
		this.statefulClientId = statefulClientId;
	}
}