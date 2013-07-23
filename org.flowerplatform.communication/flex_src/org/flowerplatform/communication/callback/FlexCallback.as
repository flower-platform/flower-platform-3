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
package org.flowerplatform.communication.callback {
	import org.flowerplatform.communication.CommunicationPlugin;

	/**
	 * This class wraps an AS function and it is registered globally with an
	 * unique ID. The Java backend invokes it by using an <code>
	 * InvokeFlexCallbackCommand</code>.
	 * 
	 * <p>
	 * The constructor registers it.
	 * 
	 * @author Cristi
	 * @flowerModelElementId _vQgpZ8bmEd6X47mKLkTdUQ
	 */ 
	public class FlexCallback {
		
		public var callbackId:int;
		
		private var callbackFunction:Function;
		
		private var callbackObject:Object;
		
		public function FlexCallback(callbackFunction:Function, callbackObject:Object) {
			this.callbackFunction = callbackFunction;
			this.callbackObject = callbackObject;
			callbackId = ++CommunicationPlugin.getInstance().lastCallbackId;
			CommunicationPlugin.getInstance().pendingCallbacks[callbackId] = this;
		}
		
		/**
		 * @flowerModelElementId _vQgpd8bmEd6X47mKLkTdUQ
		 */
		public function invokeCallback(result:Object):void {
			callbackFunction.call(callbackObject, result);
		}
		
	}
}