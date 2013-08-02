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
	
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.core.mx_internal;
	
	use namespace mx_internal;
	
	/**
	 * 
	 * @author Cristi
	 * 
	 */
	public class StatefulClientRegistry {
		
		/**
		 * 
		 */
		protected var statefulClients:Dictionary = new Dictionary();
		
		/**
		 * We need the clients ordered, because after reconnection, we want to subscribe them
		 * in the order they were registered.
		 * 
		 * <p>
		 * This field is made available publicly (using <code>mx_internal</code> namespace) 
		 * to be able to iterate it. However, it shouldn't be modified from outside (because
		 * it's not write protected).
		 *   
		 * 
		 */
		// TODO CS/STFL am impresia ca cei care folosesc asta, legat de ER, pot sa foloseasca structura mai noua introdusa,
		// ca sa nu mai itereze pe SC
		/**
		 * 
		 */
		mx_internal  var statefulClientsList:ArrayCollection = new ArrayCollection();
		
		
		/**
		 * 
		 */
		public function getStatefulClientById(id:String):StatefulClient {
			return statefulClients[id];
		}
		
		/**
		 * 
		 */
		public function register(statefulClient:StatefulClient, dataFromRegistrator:Object):void {
			var existingStatefulClient:StatefulClient = statefulClients[statefulClient.getStatefulClientId()]; 
			if (existingStatefulClient != null && existingStatefulClient != statefulClient) {
				// sanity check; we don't allow overwriting an exisitng StatefulClient;
				// however if the existing instance == new instance, we continue (e.g. opening a
				// second editor with same editorInput
				throw "Error while registering StatefulClient for name = " + statefulClient.getStatefulClientId() + "; existing instance = " + existingStatefulClient + "; new instance = " + statefulClient;
			}
			if (existingStatefulClient != statefulClient) {
				statefulClients[statefulClient.getStatefulClientId()] = statefulClient;
				statefulClientsList.addItem(statefulClient);
				statefulClient.afterAddInStatefulClientRegistry();
			}
			statefulClient.subscribeToStatefulService(dataFromRegistrator);
		}
		
		
		/**
		 * 
		 */
		public function unregister(statefulClient:StatefulClient, dataFromUnregistrator:Object):void {
			var existingStatefulClient:StatefulClient = statefulClients[statefulClient.getStatefulClientId()]; 
			if (existingStatefulClient == null) {
				throw "Error while unregistering StatefulClient for name = " + statefulClient.getStatefulClientId() + "; if was not found in the registry";
			}
			var shouldRemoveFromMap:Boolean = statefulClient.unsubscribeFromStatefulService(dataFromUnregistrator);
			if (shouldRemoveFromMap) {
				delete statefulClients[statefulClient.getStatefulClientId()];
				statefulClientsList.removeItemAt(statefulClientsList.getItemIndex(statefulClient));
				statefulClient.afterRemoveFromStatefulClientRegistry();
			}
		}
		
		// TODO CS/STFL zicea mariana ca e un caz in care tr. definita o prioritate le suscribe
		public function subscribeToStatefulServices():void {		
			for each (var statefulClient:StatefulClient in statefulClientsList) {
				statefulClient.subscribeToStatefulService(null);
			}
		}
		
		public function allStatefulClientsUnsubscribedForcefully():void {
			// we iterate the inversely, because probably the code that we invoke
			// will remove the current position
			for (var i:int = statefulClientsList.length - 1; i >= 0; i--) {
				StatefulClient(statefulClientsList[i]).unsubscribedForcefully();
			}
		}
		
	}
}