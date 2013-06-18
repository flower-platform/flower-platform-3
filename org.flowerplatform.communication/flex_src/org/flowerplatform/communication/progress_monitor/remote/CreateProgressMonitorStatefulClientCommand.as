package org.flowerplatform.communication.progress_monitor.remote {
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.command.AbstractClientCommand;
	
	[RemoteClass]
	public class CreateProgressMonitorStatefulClientCommand extends AbstractClientCommand {
		
		public var title:String;
		
		public var statefulClientId:String;
		
		/**
		 * If <code>true</code>, a cancel button will be displayed on client side 
		 * to allow cancellation.
		 */
		public var allowCancellation:Boolean;
		
		override public function execute():void {
			CommunicationPlugin.getInstance().statefulClientRegistry.register(new ProgressMonitorStatefulClient(title, statefulClientId, true /* created by server */, allowCancellation), new Object() /* != null so not as reconnect */);
		}
	}
}