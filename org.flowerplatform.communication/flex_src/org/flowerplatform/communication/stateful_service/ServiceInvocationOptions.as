package org.flowerplatform.communication.stateful_service {
	import org.flowerplatform.communication.sequential_execution.SequentialExecutionQueue;
	
	/**
	 * Builder pattern for service method invocation.
	 * 
	 * @author Cristi
	 * @author Sorin
	 */
	public class ServiceInvocationOptions {
		
		public var resultCallbackObject:Object;
		
		public var resultCallbackFunction:Function; 
		
		public var exceptionCallbackFunction:Function;
		
		public var sequentialExecutionQueue:org.flowerplatform.communication.sequential_execution.SequentialExecutionQueue;
		
		public var returnCommandWithoutSending:Boolean;
		
		public function setResultCallbackObject(value:Object):ServiceInvocationOptions {
			this.resultCallbackObject = value;
			return this;
		}
		
		public function setResultCallbackFunction(value:Function):ServiceInvocationOptions {
			this.resultCallbackFunction = value;
			return this;
		} 
		
		public function setExceptionCallbackFunction(value:Function):ServiceInvocationOptions {
			this.exceptionCallbackFunction = value;
			return this;
		}
		
		public function setSequentialExecutionQueue(value:SequentialExecutionQueue):ServiceInvocationOptions {
			this.sequentialExecutionQueue = value;
			return this;
		}
		
		public function setReturnCommandWithoutSending(value:Boolean):ServiceInvocationOptions {
			this.returnCommandWithoutSending = value;
			return this;
		}

	}
}