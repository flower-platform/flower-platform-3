package org.flowerplatform.communication.stateful_service {
	import flash.events.EventDispatcher;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;

	/**
	 * Extends <code>EventDispatcher</code> for the convenience of the sub classes. This is not
	 * used within this concrete class.
	 * 
	 * @author Cristi
	 * @author Sorin
	 * @flowerModelElementId _Vrk98P2kEeGIGtkQKYU-mw
	 */
	public class StatefulClient extends EventDispatcher {
		
		/**
		 * If true => a call to <code>unsubscribedForcefully()</code> has
		 * been made. No communication with the service is allowed any more.
		 */
		protected var hasBeenUnsubscribedForcefully:Boolean = false;
		
		/**
		 * @flowerModelElementId _xo8cAgJrEeKxZ4GAUYwMrg
		 */
		public function getStatefulClientId():String {
			throw "Abstract method. Should be implemented!";
		}
		
		/**
		 * @flowerModelElementId _xo9DEQJrEeKxZ4GAUYwMrg
		 */
		public function getStatefulServiceId():String {
			throw "Abstract method. Should be implemented!";
		}
		/**
		 * @flowerModelElementId _xo9qIAJrEeKxZ4GAUYwMrg
		 */
		public function getCurrentStatefulClientLocalState(dataFromRegistrator:Object = null):IStatefulClientLocalState {
			throw "Abstract method. Should be implemented!";
		}
		
		/**
		 * This method has protected visibility to avoid other objects calling it directly. If there is such a need, the 
		 * <code>StatefulClient</code> should add and expose wrapper methods that expose the desired server
		 * methods.
		 * 
		 * <p>
		 * If <code>hasBeenUnsubscribedForcefully</code> and someone invokes accidentally this method: it doens't do anything.
		 * 
		 * @flowerModelElementId _xo9qIgJrEeKxZ4GAUYwMrg
		 */
		protected function invokeServiceMethod(methodName:String, parameters:Array, serviceInvocationOptions:ServiceInvocationOptions=null):Object {
			if (hasBeenUnsubscribedForcefully) {
				return null;
			}
			
			if (serviceInvocationOptions == null) // Provide default options.
				serviceInvocationOptions = new ServiceInvocationOptions(); 
			
			var invokeServiceMethodCommand:InvokeServiceMethodServerCommand =
				new InvokeStatefulServiceMethodServerCommand(
					getStatefulClientId(), 
					getStatefulServiceId(), 
					methodName, parameters, 
					(serviceInvocationOptions.resultCallbackObject != null ? serviceInvocationOptions.resultCallbackObject : this), 
					serviceInvocationOptions.resultCallbackFunction, 
					serviceInvocationOptions.exceptionCallbackFunction);
			
			if (serviceInvocationOptions.returnCommandWithoutSending)  // Just create the command.
				return invokeServiceMethodCommand;
			
			if (serviceInvocationOptions.sequentialExecutionQueue != null) // Send the command to execute sequentially
				serviceInvocationOptions.sequentialExecutionQueue.send(invokeServiceMethodCommand);
			else  // Execute normally 
				CommunicationPlugin.getInstance().bridge.sendObject(invokeServiceMethodCommand);
			return null;
		}
		
		public function afterAddInStatefulClientRegistry():void {
			// do nothing
		}
		
		public function afterRemoveFromStatefulClientRegistry():void {
			// do nothing
		}
		
		/**
		 * Should trigger the "close" flow for this StatefulClient and associated UI (+
		 * other data, if present). E.g. close the view and unregister the StatefulClient.
		 * 
		 * <p>
		 * This method SHOULD NOT trigger (directly or indirectly) the unregistration of a
		 * StatefulClient other than the current one (because we are iterating a list that
		 * may be modified in a nasty way). 
		 */ 
		protected function removeUIAndRelatedElementsAndStatefulClientBecauseUnsubscribedForcefully():void {
//			throw "This method should be implemented";
		}
		
		///////////////////////////////////////////////////////////////
		//@RemoteInvocation methods
		///////////////////////////////////////////////////////////////

		/**
		 * @flowerModelElementId _xpCiogJrEeKxZ4GAUYwMrg
		 */
		[RemoteInvocation]
		public function subscribeToStatefulService(dataFromRegistrator:Object):void {
			invokeServiceMethod("subscribe", [getCurrentStatefulClientLocalState(dataFromRegistrator)]);
		}
		
		[RemoteInvocation]
		public function unsubscribeFromStatefulService(dataFromUnregistrator:Object):Boolean {
			if (!hasBeenUnsubscribedForcefully) {
				invokeServiceMethod("unsubscribe", [getCurrentStatefulClientLocalState(dataFromUnregistrator)]);
			}
			return true;
		}

		/**
		 * When this method is called, the server has unsubscribed this client; i.e. it has completely
		 * (and forcefully) forgoten about this client. 
		 * 
		 * <p>
		 * This method sets <code>hasBeenUnsubscribedForcefully</code> to true and invokes <code>
		 * removeUIAndRelatedElementsAndStatefulClientBecauseUnsubscribedForcefully()</code> for which
		 * the concrete subclass should have a proper implementation.
		 */ 
		[RemoteInvocation]
		public function unsubscribedForcefully():void {
			// TODO CS/FP2: reactivate this debug switch + untie it from the debug window + move the debug window (StatefulClientRegistryDebugWindow.mxml.disabled) someplace else
//			if (StatefulClientRegistryDebugWindow.INSTANCE != null && StatefulClientRegistryDebugWindow.INSTANCE.preventUnsubscribedForcefullySignalFromServer) {
//				return;
//			}
			hasBeenUnsubscribedForcefully = true;
			removeUIAndRelatedElementsAndStatefulClientBecauseUnsubscribedForcefully();
		}

	}
}