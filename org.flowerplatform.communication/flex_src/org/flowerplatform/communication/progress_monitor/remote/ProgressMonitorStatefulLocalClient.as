package org.flowerplatform.communication.progress_monitor.remote {
	import org.flowerplatform.communication.stateful_service.IStatefulClientLocalState;
		
	[RemoteClass]
	public class ProgressMonitorStatefulLocalClient implements IStatefulClientLocalState {
		
		public var title:String;
		
		public var afterReconnect:Boolean;
		
		public function ProgressMonitorStatefulLocalClient(title:String, afterReconnect:Boolean) {
			this.title = title;
			this.afterReconnect = afterReconnect;
		}
		
	}
}