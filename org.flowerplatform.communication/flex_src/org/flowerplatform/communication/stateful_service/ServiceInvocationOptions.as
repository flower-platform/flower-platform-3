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