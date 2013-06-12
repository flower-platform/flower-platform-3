package org.flowerplatform.communication.command {
	import org.flowerplatform.blazeds.BridgeEvent;
	import org.flowerplatform.communication.CommunicationPlugin;
	
	[RemoteClass]
	public class WelcomeClientCommand extends CompoundClientCommand {
		
		public var containsFirstTimeInitializations:Boolean;
		
		override public function execute():void {
			if (containsFirstTimeInitializations && CommunicationPlugin.getInstance().firstWelcomeWithInitializationsReceived) {
				throw new Error("WelcomeClientCommand arrived, but it seems another welcome was already received!");
			}
			super.execute();
			if (containsFirstTimeInitializations) {
				CommunicationPlugin.getInstance().firstWelcomeWithInitializationsReceived = true;
			}
			CommunicationPlugin.getInstance().bridge.dispatchEvent(new BridgeEvent(BridgeEvent.WELCOME_RECEIVED_FROM_SERVER));
		}
		
	}
}