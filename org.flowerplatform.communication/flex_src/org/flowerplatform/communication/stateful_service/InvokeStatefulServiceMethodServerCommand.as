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
package org.flowerplatform.communication.stateful_service {
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	
	/**
	 * @author Cristi
	 */
	[RemoteClass]
	public class InvokeStatefulServiceMethodServerCommand extends org.flowerplatform.communication.service.InvokeServiceMethodServerCommand	{
		
		public var statefulClientId:String;
		
		public function InvokeStatefulServiceMethodServerCommand(statefulClientId:String = null, serviceId:String = null, methodName:String = null, parameters:Array = null, resultCallbackObject:Object=null, resultCallbackFunction:Function=null, exceptionCallbackFunction:Function=null) {
			super(serviceId, methodName, parameters, resultCallbackObject, resultCallbackFunction, exceptionCallbackFunction);
			this.statefulClientId = statefulClientId;
		}
	}
}