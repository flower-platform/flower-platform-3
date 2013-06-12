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