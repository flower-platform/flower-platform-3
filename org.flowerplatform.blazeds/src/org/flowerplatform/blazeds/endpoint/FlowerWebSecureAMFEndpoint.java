package org.flowerplatform.blazeds.endpoint;


import flex.messaging.endpoints.AMFEndpoint;


/**
 * This endpoint is used for polling communication.
 * 
 * <p>
 * As you can observe a custom serializer is not used because serialization
 * is done instantly for objects (commands) that need to reach client side.
 * 
 * @see FlowerWebSecureStreamingAMFEndpoint
 * @see ServerSnapshotClientCommand
 * @author Sorin
 * @flowerModelElementId _ez_00G22EeGQ6LdvAwMt-w
 */
public class FlowerWebSecureAMFEndpoint extends AMFEndpoint {
	
	@Override
	protected String getDeserializerClassName() {
		return FlowerWebSecureAmfMessageDeserializer.class.getName();
	}
}