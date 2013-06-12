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
