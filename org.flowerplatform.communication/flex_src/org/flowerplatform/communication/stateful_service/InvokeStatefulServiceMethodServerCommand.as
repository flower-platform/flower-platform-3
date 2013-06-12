package org.flowerplatform.communication.stateful_service {
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	
	/**
	 * @author Cristi
	 */
	[RemoteClass]
	public class InvokeStatefulServiceMethodServerCommand extends org.flowerplatform.communication.service.InvokeServiceMethodServerCommand	{
		
		public var statefulClientId:String;
		
		public function InvokeStatefulServiceMethodServerCommand(statefulClientId:String, serviceId:String, methodName:String, parameters:Array, resultCallbackObject:Object=null, resultCallbackFunction:Function=null, exceptionCallbackFunction:Function=null) {
			super(serviceId, methodName, parameters, resultCallbackObject, resultCallbackFunction, exceptionCallbackFunction);
			this.statefulClientId = statefulClientId;
		}
	}
}