package org.flowerplatform.communication.sequential_execution {
	import org.flowerplatform.communication.sequential_execution.IServerCommandWrapper;

	/**
	 * Command that wrappes another command. After executing it on the server side it will send a signal
	 * to the client so that it can continue sendint the rest of the sequential commands from a queue.
	 * 
	 * @see SequentialExecutionQueue
	 * 
	 * @author Sorin
	 */ 
	[RemoteClass]
	public class SequentialExecutionServerCommand implements IServerCommandWrapper {
		
		public var callbackId:Number;
		
		private var _command:Object;
		
		/**
		 * When sending commands that on the server must be executed sequencially it is important
		 * to use the same <code>solicitor</code>. 
		 */ 
		public function SequentialExecutionServerCommand(command:Object, callbackId:Number) {
			this.command = command;
			this.callbackId = callbackId;
		}
		
		public function set command(value:Object):void {
			_command = value;
		}
		
		public function get command():Object {
			return _command;
		}
	}
}