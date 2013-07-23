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