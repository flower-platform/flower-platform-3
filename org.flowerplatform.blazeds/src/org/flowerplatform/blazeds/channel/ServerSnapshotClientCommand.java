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
package org.flowerplatform.blazeds.channel;

import java.io.ByteArrayOutputStream;

import org.flowerplatform.communication.channel.CommunicationChannelManager;
import org.flowerplatform.communication.command.AbstractClientCommand;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Output;

/**
 * Wraps a command by making an instant serialization.
 * Note : The command to be wrapped must ensure that it isn't modified during serialization,
 * albeit it may be modified afterwards. 
 * @author Sorin
 */
public class ServerSnapshotClientCommand extends AbstractClientCommand {

	private Object wrappedCommand;
	
	public byte[] serializedWrappedCommand;
	
	public ServerSnapshotClientCommand(Object wrappedCommand) {
		this.wrappedCommand = wrappedCommand;
		serialize();
	}
	
	private void serialize() {
		ByteArrayOutputStream serializedObjectInBytes = new ByteArrayOutputStream();
		try {
			SerializationContext serializationContext = SerializationContext.getSerializationContext(); 
			Amf3Output serializer = getSerializer(serializationContext);
			serializer.setOutputStream(serializedObjectInBytes);
			serializer.writeObject(wrappedCommand);
		} catch (Exception e) {
			CommunicationChannelManager.logger.error("Error making a snapshot of " + wrappedCommand, e);
			return;
		}
		serializedWrappedCommand = serializedObjectInBytes.toByteArray();
	}
	
	protected Amf3Output getSerializer(SerializationContext serializationContext) {
		return new Amf3Output(serializationContext);
	}

	/**
	 * @author Mariana
	 */
	public Object getWrappedCommand() {
		return wrappedCommand;
	}

	@Override
	public String toString() {
		return wrappedCommand.toString() + " wrapped in " + super.toString();
	}
}