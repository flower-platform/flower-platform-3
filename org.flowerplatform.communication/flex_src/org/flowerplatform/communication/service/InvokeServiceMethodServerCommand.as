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
package org.flowerplatform.communication.service {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.callback.FlexCallback;
	
	/**
	 * @see Corresponding Java class for documentation and important remarks.
	 * 
	 * @author Cristi
	 * @flowerModelElementId _MF8PAFZkEeGL3vi-zPhopA
	 */
	[RemoteClass]
	public class InvokeServiceMethodServerCommand {
	
		/**
		 * @flowerModelElementId _RFddAFZkEeGL3vi-zPhopA
		 */
		public var serviceId:String;
	
		/**
		 * @flowerModelElementId _ukajAFbgEeGL3vi-zPhopA
		 */
		public var methodName:String;

		/**
		 * @flowerModelElementId _TASyAFZkEeGL3vi-zPhopA
		 */
		public var parameters:ArrayCollection;
		
		/**
		 * @flowerModelElementId _ukeNYFbgEeGL3vi-zPhopA
		 */
		public var callbackId:Number = 0;
		
		public var exceptionCallbackId:Number = 0;
				
		/**
		 * @flowerModelElementId _ukh3w1bgEeGL3vi-zPhopA
		 */
		public function InvokeServiceMethodServerCommand(serviceId:String = null, methodName:String = null, parameters:Array = null, resultCallbackObject:Object = null, resultCallbackFunction:Function = null, exceptionCallbackFunction:Function = null) {
			this.serviceId = serviceId;
			this.methodName = methodName;
			this.parameters = new ArrayCollection(parameters);
			
			if (resultCallbackFunction != null) {
				var flexCallback:FlexCallback = new FlexCallback(resultCallbackFunction, resultCallbackObject);
				callbackId = flexCallback.callbackId;
			}
			if (exceptionCallbackFunction != null) {
				flexCallback = new FlexCallback(exceptionCallbackFunction, resultCallbackObject);
				exceptionCallbackId = flexCallback.callbackId;
			}
		}
	}
}