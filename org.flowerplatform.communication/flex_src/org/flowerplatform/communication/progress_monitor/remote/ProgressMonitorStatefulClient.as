package  org.flowerplatform.communication.progress_monitor.remote {
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.progress_monitor.ProgressMonitorDialog;
	import org.flowerplatform.communication.stateful_service.IStatefulClientLocalState;
	import org.flowerplatform.communication.stateful_service.StatefulClient;
		
	/**
	 * @author Sorin
	 * @flowerModelElementId _lW_MUBLsEeKIW4So6X04UQ
	 */
	public class ProgressMonitorStatefulClient extends StatefulClient  {
		
		/**
		 * @flowerModelElementId _pEupYBLsEeKIW4So6X04UQ
		 */
		private static const SERVICE_ID:String = "ProgressMonitorStatefulService";
	
		private static var lastUsedId:Number = 0;
		
		private var statefulClientId:String;
		
		private var title:String;
		
		private var createdByServer:Boolean;
		
		/**
		 * If <code>true</code>, a cancel button will be displayed on client side 
		 * to allow cancellation.
		 * 
		 * @author Cristina
		 */
		private var allowCancellation:Boolean;
		
		private var progressMonitorDialog:ProgressMonitorDialog;
	
		/**
		 * @flowerModelElementId _bE9ZEBN_EeK1ssFHNoNwQg
		 */
		public function ProgressMonitorStatefulClient(title:String, statefulClientId:String, createdByServer:Boolean, allowCancellation:Boolean=true) {
			this.title = title;
			this.statefulClientId = createdByServer ? statefulClientId : "ClientProgresMonitor" + (++ lastUsedId); // Use or generate new.
			this.createdByServer = createdByServer;
			this.allowCancellation = allowCancellation;
		}
		
		/**
		 * @flowerModelElementId _FoR1EBN_EeK1ssFHNoNwQg
		 */
		public override function getStatefulServiceId():String {
			return SERVICE_ID;
		}
		
		/**
		 * @flowerModelElementId _FoJ5QBN_EeK1ssFHNoNwQg
		 */
		public override function getStatefulClientId():String {
			return statefulClientId;
		}
		
		/**
		 * 
		 * TODO Sorin : de vorbit cu cristi sa spuna ca subscribe in in urma unui reconnect? 
		 * 
		 * See CreateProgressMonitorStatefulClient#execute() for register dataRegistrator != null.
		 * See #createAndShowProgressMonitor() for register dataRegistrator != null. 
		 * 
		 * @flowerModelElementId _Lt2sMBN_EeK1ssFHNoNwQg
		 */
		public override function getCurrentStatefulClientLocalState(dataFromRegistrator:Object = null):IStatefulClientLocalState {
			return new ProgressMonitorStatefulLocalClient(title, /* after reconnect */ dataFromRegistrator == null) ;
		}

		/**
		 * Package visibility because only the monitor dialog can call it.
		 * @flowerModelElementId _dn0rwBLtEeKIW4So6X04UQ
		 */
		public function attemptCancelProgressMonitor():void {
			invokeServiceMethod("attemptCancelProgressMonitor", null);
		}
		
		override public function afterAddInStatefulClientRegistry():void {
			// When added to the registy also show popup
			progressMonitorDialog = new ProgressMonitorDialog();
			progressMonitorDialog.progressMonitorStatefulClient = this;
			progressMonitorDialog.allowCancellation = allowCancellation;
			
			progressMonitorDialog.showPopup();
			
			progressMonitorDialog.title = this.title;
			progressMonitorDialog.progressLabel = this.title;
		}
		 
		override public function subscribeToStatefulService(dataFromRegistrator:Object):void {
			if (!createdByServer || dataFromRegistrator == null ) // Server already knows it, no need to subscribe, or reconnect
				super.subscribeToStatefulService(dataFromRegistrator);
		}
		
		override public function unsubscribeFromStatefulService(dataFromUnregistrator:Object):Boolean {
			return true; // Just unregister, the unsubscription was done already on the server.
		}
		
		override protected function removeUIAndRelatedElementsAndStatefulClientBecauseUnsubscribedForcefully():void {
			closeProgressMonitor();
		}
		
		/**
		 * Returns Progress monitor Stateful Client Id to be passed to the server.
		 * @see ProgressMonitorStatefulService#getProgressMonitor(progressMonitorStatefulClientId)
		 */ 
		public static function createAndShowProgressMonitor(title:String):ProgressMonitorStatefulClient {
			var progressMonitorStatefulClient:ProgressMonitorStatefulClient = new ProgressMonitorStatefulClient(title, null /* generate statefulClientId */, false /* created by client */);
			CommunicationPlugin.getInstance().statefulClientRegistry.register(progressMonitorStatefulClient, new Object() /* != null so not as reconnect */); 
			return progressMonitorStatefulClient;
		}
		
		override public function toString():String {
			return "ProgressMonitorStatefulClient statefulClientId = " + statefulClientId + " title = " + title;  
		}
		
		///////////////////////////////////////////////////////////////
		//@RemoteInvocation methods
		///////////////////////////////////////////////////////////////
		
		/**
		 * @flowerModelElementId _T7yEQBLtEeKIW4So6X04UQ
		 */
		[RemoteInvocation]
		public function beginProgressMonitor(name:String, totalWork:int):void {
			progressMonitorDialog.progressTotalWork = totalWork;
			progressMonitorDialog.progressLabel = name;
		}
		
		/**
		 * @flowerModelElementId _XAsmwBLtEeKIW4So6X04UQ
		 */
		[RemoteInvocation]
		public function updateProgressMonitor(name:String, workUntilNow:int):void {
			progressMonitorDialog.progressLabel = name;
			progressMonitorDialog.workUntilNow = workUntilNow;
		}
		
		/**
		 * Hides the monitor and unregisters it.
		 * @flowerModelElementId _ZacsYBLtEeKIW4So6X04UQ
		 */
		[RemoteInvocation]
		public function closeProgressMonitor():void {
			progressMonitorDialog.closeForm();
			CommunicationPlugin.getInstance().statefulClientRegistry.unregister(this, null);
		}
		
	}
}