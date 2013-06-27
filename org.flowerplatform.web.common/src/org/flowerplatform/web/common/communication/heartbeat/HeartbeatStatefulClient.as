package org.flowerplatform.web.common.communication.heartbeat {
	
	import flash.events.TimerEvent;
	import flash.utils.Timer;
	
	import org.flowerplatform.blazeds.BridgeEvent;
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.stateful_service.IStatefulClientLocalState;
	import org.flowerplatform.communication.stateful_service.StatefulClient;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.web.common.WebCommonPlugin;
	import org.flowerplatform.web.common.communication.heartbeat.WarnAboutNoActivityDialog;

	/**
	 * StatefulClient that helps the server monitoring the traveling data 
	 * and the activity of a client. 
	 * 
	 * @see HeartbeatStatefulService
	 * @see #warnAboutNoActivity(seconds)
	 * @see #signalHeartbeat()
	 * @see #signalActivity()
	 * 
	 * @author Sorin
	 * @flowerModelElementId _1PxqIAlNEeK1a-Ic5xjg1Q
	 */
	public class HeartbeatStatefulClient extends StatefulClient  {
		
		public static const SERVICE_ID:String = "heartbeatStatefulService";
		
		public static const CLIENT_ID:String = "heartbeatStatefulClient";
		
		private static const CLIENT_HEARTBEAT_PERIOD:String = "client.heartbeat.period";

		private static var HEARTBEAT_PERIOD:int = 60 * 1000 / 4; // milliseconds
		
		private var warnAboutNoActivityDialog:WarnAboutNoActivityDialog;

		/**
		 * Records time for last object sent or received to/from the server.
		 * Involved in determining if a signal must be send to the server 
		 * to notify that the client is still alive.		 
		 */
		private var lastTravelingDataTimestamp:Number;
		
		/**
		 * Timer which periodically announces, using #signalHeartbeat() handler
		 * that bridge activity was recorded on the client side. 
		 */
		private var heart:Timer;
		
		
		/**
		 * @flowerModelElementId _2ct2AAlbEeK1a-Ic5xjg1Q
		 */
		public function HeartbeatStatefulClient() {
			heart = new Timer(HEARTBEAT_PERIOD, 0); // infinite
			heart.addEventListener(TimerEvent.TIMER, signalHeartbeat);
			
			WebCommonPlugin.getInstance().authenticationManager.bridge.addEventListener(BridgeEvent.CONNECTED, registerStatefulClient);
			
			WebCommonPlugin.getInstance().authenticationManager.bridge.addEventListener(BridgeEvent.CONNECTED, handleBridgeEvent);
			WebCommonPlugin.getInstance().authenticationManager.bridge.addEventListener(BridgeEvent.CONNECTING, handleBridgeEvent);
			WebCommonPlugin.getInstance().authenticationManager.bridge.addEventListener(BridgeEvent.DISCONNECTED, handleBridgeEvent);
			
			WebCommonPlugin.getInstance().authenticationManager.bridge.addEventListener(BridgeEvent.OBJECT_SENT, handleBridgeEvent);
			WebCommonPlugin.getInstance().authenticationManager.bridge.addEventListener(BridgeEvent.OBJECT_RECEIVED, handleBridgeEvent);
		}
		
		public override function getStatefulServiceId():String {
			return SERVICE_ID
		}
		
		public override function getStatefulClientId():String {
			return CLIENT_ID;
		}
		
		private function registerStatefulClient(event:BridgeEvent):void {
			CommunicationPlugin.getInstance().statefulClientRegistry.register(this, this);
			// Remove listener because the registry already listens the event to subscribe again.
//			WebCommonPlugin.getInstance().authenticationManager.bridge.removeEventListener(BridgeEvent.CONNECTED, registerStatefulClient);
		}

		public override function getCurrentStatefulClientLocalState(dataFromRegistrator:Object = null):IStatefulClientLocalState {	
			return null; // Doesn't maintain a state.
		}
		
		/**
		 * @flowerModelElementId _JMquAAlcEeK1a-Ic5xjg1Q
		 */
		private function handleBridgeEvent(event:BridgeEvent):void {
			if (event.type == BridgeEvent.CONNECTED) { // New connection
				lastTravelingDataTimestamp = new Date().time;
				heart.start();
				
			} else if (event.type == BridgeEvent.CONNECTING || event.type == BridgeEvent.DISCONNECTED) { // Connection lost or disconnected
				heart.stop();
				FlexUtilGlobals.getInstance().popupHandlerFactory.removePopup(warnAboutNoActivityDialog);
				warnAboutNoActivityDialog = null;
			} else if (event.type == BridgeEvent.OBJECT_SENT || event.type == BridgeEvent.OBJECT_RECEIVED) { // Object sent or received
				lastTravelingDataTimestamp = new Date().time;
				heart.stop();
				heart.start();
			}
		}
		
		public function updateProperties(properties:Object):void {
			if (properties[CLIENT_HEARTBEAT_PERIOD] != null)
				HEARTBEAT_PERIOD = int(properties[CLIENT_HEARTBEAT_PERIOD]);
			
			heart.delay = HEARTBEAT_PERIOD;
			signalHeartbeat();
		}

		///////////////////////////////////////////////////////////////
		// Wrappers for service methods
		///////////////////////////////////////////////////////////////
		
		private function signalHeartbeat(event:TimerEvent = null):void { /* I am alive! don't kill me!!! */
			invokeServiceMethod("signalHeartbeat", null);
		}
		
		public function signalActivity():void {
			invokeServiceMethod("signalActivity", null);
		}

		///////////////////////////////////////////////////////////////
		//@RemoteInvocation methods
		///////////////////////////////////////////////////////////////
		
		/**
		 * @flowerModelElementId _dQMRQAldEeK1a-Ic5xjg1Q
		 */
		[RemoteInvocation]
		public function warnAboutNoActivity(secondsUntilDisconnect:Number):void {
			warnAboutNoActivityDialog = new WarnAboutNoActivityDialog();
			warnAboutNoActivityDialog.secondsUntilDisconnect = secondsUntilDisconnect;
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setPopupContent(warnAboutNoActivityDialog)
				.show();
		}
		
		[RemoteInvocation]
		override public function unsubscribedForcefully():void {
			// refuzes to leave
		}
	}
}