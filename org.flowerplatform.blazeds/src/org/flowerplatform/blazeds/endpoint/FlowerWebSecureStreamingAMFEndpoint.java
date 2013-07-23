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