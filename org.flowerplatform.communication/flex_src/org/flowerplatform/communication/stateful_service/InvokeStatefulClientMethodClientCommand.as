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