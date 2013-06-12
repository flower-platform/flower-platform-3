package org.flowerplatform.communication.stateful_service {
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.command.AbstractClientCommand;
	
	
	/**
	 * @author Cristi
	 */
	[RemoteClass]
	public class InvokeStatefulClientMethodClientCommand extends AbstractClientCommand {

		public var statefulClientId:String;
		
		public var methodName:String;
		
		public var parameters:Array;

		override public function execute():void	{
			var statefulClient:StatefulClient = CommunicationPlugin.getInstance().statefulClientRegistry.getStatefulClientById(statefulClientId);
			if (statefulClient == null) {
				// TODO CS/STFL aici trebuie sa facem dezabonarea; caci dintr-un motiv sau altul,
				// nu mai avem serviciul
				trace("Could not locate stateful client : " + statefulClientId);
			} else {
				// TODO CS/STFL de prins exceptie pentru cazul de metoda inexistenta, sau parametrii incorecti
				statefulClient[methodName].apply(statefulClient, parameters);
			}
		}
		
	}
}