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
		}
		
		/**
		 * @flowerModelElementId _vQgpd8bmEd6X47mKLkTdUQ
		 */
		public function invokeCallback(result:Object):void {
			callbackFunction.call(callbackObject, result);
		}
		
	}
}