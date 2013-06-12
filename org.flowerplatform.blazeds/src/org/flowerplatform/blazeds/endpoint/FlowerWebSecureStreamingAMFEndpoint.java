package org.flowerplatform.blazeds.endpoint;

import flex.messaging.endpoints.StreamingAMFEndpoint;


/**
 * As you can observe a custom serializer is not used because serialization
 * is done instantly for objects (commands) that need to reach client side.
 * 
 * @see FlowerWebSecureAMFEndpoint
 * @see ServerSnapshotClientCommand
 * @author Sorin
 * @flowerModelElementId _a6P8EG22EeGQ6LdvAwMt-w
 */
public class FlowerWebSecureStreamingAMFEndpoint extends StreamingAMFEndpoint {
	
	@Override
	protected String getDeserializerClassName() {
		return FlowerWebSecureAmfMessageDeserializer.class.getName();
	}
}