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
package org.flowerplatform.communication.command
{
	import flash.utils.ByteArray;
	
	/**
	 * Command that wrappes another server command that was obtaind by instant serialization.
	 * See server side command.
	 * @author Sorin
	 */ 
	[RemoteClass(alias="org.flowerplatform.blazeds.channel.ServerSnapshotClientCommand")]	
	public class ServerSnapshotClientCommand extends AbstractClientCommand {
		
		private var wrappedCommand:Object;
		
		private var _serializedWrappedCommand:ByteArray;
		
		public function set serializedWrappedCommand(value:ByteArray):void {
			_serializedWrappedCommand = value;
//			if (MiscDebugProperties.deserializeServerSnapshotImmediately)
//				deserialize();
		}
		
		public function get serializedWrappedCommand():ByteArray {
			return _serializedWrappedCommand;
		}
		
		override public function execute():void {
			deserialize();
			AbstractClientCommand(wrappedCommand).execute();
		}
		
		private function deserialize():void {
			if (wrappedCommand != null)
				return;
			wrappedCommand = serializedWrappedCommand.readObject();
			serializedWrappedCommand = null;
		}
	}
}