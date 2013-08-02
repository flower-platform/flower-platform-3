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
package org.flowerplatform.blazeds {
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	import mx.messaging.events.MessageFaultEvent;
	
	/**
	 * Event dispatched by BlazeDSBridge class to announce some specific behavior.
	 * 
	 * @see BlazeDSBridge
	 * 
	 * @author Sorin
	 * 
	 */ 
	public class BridgeEvent extends Event {

		
		/**
		 * 
		 */
		public static const CONNECTED:String = "Connected";
		
		/**
		 * This is not dispatched by BlazeDSBridge. It is dispatched by WelcomeClientCommand.
		 */ 
		public static const WELCOME_RECEIVED_FROM_SERVER:String = "WelcomeReceivedFromServer";
		
		/**
		 * 
		 */
		public static const DISCONNECTED:String = "Disconnected";
		
		/**
		 * @see BlazeDSBridge#disconnectBecauseUserLoggedOut()
		 * @author Cristi
		 */ 
		public static const DISCONNECTED_BECAUSE_USER_LOGGED_OUT:String = "DisconnectedBecauseUserLoggedOut";
		public static const CONNECTING:String = "Connecting";
		public static const CONNECTING_CANCELED:String = "ConnectingCanceled";
		public static const AUTHENTICATION_NEEDED:String = "AuthenticationNeeded";
		public static const AUTHENTICATION_ACCEPTED:String = "AuthenticationAccepted";
		
		/**
		 * 
		 */
		public static const OBJECT_SENT:String="ObjectSent";
		
		/**
		 * 
		 */
		public static const OBJECT_RECEIVED:String = "ObjectReceived";
		public static const OBJECT_UNDELIVERED:String = "ObjectUndelivered";
		
		/**
		 * sentObject contains the undelivered object.
		 * 
		 * @see #dontExecuteTheDefaulFaultLogic
		 * @author Cristi
		 */
		public static const FAULT:String = MessageFaultEvent.FAULT;

		public var acceptedCredentials:Object;
		public var rejectedCredentials:Object;

		/**
		 * True when just to show credentials request form and not to inform
		 * the user that the previous credentials are wrong because they where not requested. 
		 */
		public var firstCredentialsRequest:Boolean;

		/**
		 * @see #firstBridgeConnection
		 */ 
		private static var firtBridgeConnectionHappened:Boolean = false;
		/**
		 * True only for the first successfull connection of the bridge.
		 * Intended to use for example to know if a module need to load everything from
		 * the server or only a delta.
		 */ 
		public var firstBridgeConnection:Boolean;

		public var sentObject:Object;
		public var receivedObject:Object;
		public var undeliveredObjects:ArrayCollection;

		public var disconnectedByServer:Boolean;
		
		/**
		 * The handler may set this to true, for a FAULT event.
		 * 
		 * @see #FAULT
		 * @author Cristi
		 */
		public var dontExecuteTheDefaulFaultLogic:Boolean;

		/**
		 * @param detail depending on type of the event, <code>detail</code> is recorded as a more 
		 * 		specific information in	a field of this class.
		 * @param firstCredentialsRequest stored only when the event is about authentication needed
		 * @author Sorin
		 * @author Cristi
		 */ 
		public function BridgeEvent(type:String, detail:Object = null, firstCredentialsRequest:Boolean = false) {
			super(type);
			
			if (type == AUTHENTICATION_NEEDED) {
				rejectedCredentials = detail;
				this.firstCredentialsRequest = firstCredentialsRequest;
			}
			if (type == AUTHENTICATION_ACCEPTED)
				acceptedCredentials = detail;
				
			if (type == CONNECTED) {
				firstBridgeConnection = !firtBridgeConnectionHappened;
				firtBridgeConnectionHappened = true;
			}
			if (type == DISCONNECTED)
				disconnectedByServer = detail == null ? false : detail;
			
			if (type == OBJECT_SENT)
				sentObject = detail;
			if (type == OBJECT_RECEIVED)
				receivedObject = detail;
			if (type == OBJECT_UNDELIVERED)
				undeliveredObjects = detail as ArrayCollection;
			if (type == FAULT) {
				sentObject = detail;
			}
		}
	}
}