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
	import org.flowerplatform.communication.command.AbstractClientCommand;
	
	/**
	 * Invokes a <code>FlexCallback</code> that contains a function inside.
	 * This mechanism should be used when sending server commands that expect a feedback
	 * from the Java backend. 
	 * 
	 * <p>
	 * If this method has <code>disposeCallbackWithoutInvocation = true</code>, then
	 * the callback is not invoked, but it is disposed from the registry. 
	 * 
	 * <p>
	 * The commands (or other code) that send this command, should have a try/catch
	 * block. In case of problems, they should send this method, with 
	 * <code>disposeCallbackWithoutInvocation = true</code>.
	 * 
	 * @author Cristi
	 * @flowerModelElementId _vQgpfMbmEd6X47mKLkTdUQ
	 */ 
	[RemoteClass]
	public class InvokeCallbackClientCommand extends AbstractClientCommand {
		
		public var callbackId:Number;
		
		public var result:Object;
		
		public var disposeCallbackWithoutInvocation:Boolean;
		
		/**
		 * @flowerModelElementId _SVxIa8eWEd6ebePkcCLlJA
		 */
		public override function execute():void {
			var callback:FlexCallback = FlexCallback(CommunicationPlugin.getInstance().pendingCallbacks[callbackId]);
			if (callback != null) {
				try {
					if (!disposeCallbackWithoutInvocation) {
						callback.invokeCallback(result);
					}
				} finally {
					delete CommunicationPlugin.getInstance().pendingCallbacks[callbackId];
				}
			}
		}
	}
}