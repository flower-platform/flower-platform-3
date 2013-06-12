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