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
package org.flowerplatform.web.communication;

import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;

/**
 * @author Cristi
 */
public class TestStatefulServiceInvocationContext extends StatefulServiceInvocationContext implements IRecordingTestWebCommunicationChannelProvider{

	public TestStatefulServiceInvocationContext(
			CommunicationChannel communicationChannel,
			InvokeServiceMethodServerCommand command, String statefulClientId) {
		super(communicationChannel, command, statefulClientId);
	}

	public TestStatefulServiceInvocationContext(
			CommunicationChannel communicationChannel) {
		super(communicationChannel);
	}

	/**
	 * @return A CC casted to {@link RecordingTestWebCommunicationChannel}, for convenience.
	 */
	public RecordingTestWebCommunicationChannel getRecordingTestWebCommunicationChannel() {
		return (RecordingTestWebCommunicationChannel) getCommunicationChannel();
	}

}