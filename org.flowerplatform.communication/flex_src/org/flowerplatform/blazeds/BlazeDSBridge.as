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
package  org.flowerplatform.blazeds {
	import flash.events.EventDispatcher;
	import flash.events.HTTPStatusEvent;
	import flash.events.TimerEvent;
	import flash.utils.Timer;
	
	import mx.collections.ArrayCollection;
	import mx.core.FlexGlobals;
	import mx.core.mx_internal;
	import mx.events.PropertyChangeEvent;
	import mx.messaging.Channel;
	import mx.messaging.ChannelSet;
	import mx.messaging.Consumer;
	import mx.messaging.FlexClient;
	import mx.messaging.Producer;
	import mx.messaging.channels.AMFChannel;
	import mx.messaging.channels.StreamingAMFChannel;
	import mx.messaging.config.ServerConfig;
	import mx.messaging.events.ChannelEvent;
	import mx.messaging.events.MessageAckEvent;
	import mx.messaging.events.MessageEvent;
	import mx.messaging.events.MessageFaultEvent;
	import mx.messaging.messages.AsyncMessage;
	import mx.rpc.AsyncResponder;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.utils.UIDUtil;
	import mx.utils.URLUtil;
	
	import org.flowerplatform.common.log.Logger;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	
	use namespace mx_internal;
	
	// NOTE : DEBUG sections contain usefull code for debugging this class. There are some at the end of the file.
	/**
	 * Bridge that contains the following behaviour :
	 * - sending and receiving objects;
	 * - using authentication;
	 * 
	 * Other less important behaviours are:
	 * - automatic (re)connection;
	 * 
	 * The establishing of a connection is done each time a network failure appears or any other type 
	 * of anomaly. The connections are done delayed so that the server isn't flood by clients.
	 * 
	 * @author Sorin
	 * 
	 */
	public class BlazeDSBridge extends EventDispatcher {
		
		public const logger:Logger = new Logger(BlazeDSBridge, true  /* log enabled */, false /* debug enabled */, false /* stack trace enabled */); 

		public static const BLAZEDS_DESTINATION:String = "blazedsSecuredDestination";
		
		/**
		 * @author Cristi
		 */
		public static const BLAZEDS_ALL_CHANNELS:String = "allChannels";
		
		/**
		 * @author Cristi
		 */
		public static const BLAZEDS_STREAMING_CHANNEL:String = "blazedsStreamingAMFChannel";
		
		/**
		 * @author Cristi
		 */
		public static const BLAZEDS_POLLING_CHANNEL:String = "blazedsLongPollingAMFChannel";
		
		private static var RETRY_RECONNECTING_INTERVAL:int = 3000; // milliseconds
		
		private static var SWITCH_RECONNECTING_INTERVAL:int = 300; // milliseconds
		
		private static var DISPATCH_UNDELIVERED_OBJECTS_INTERVAL:int = 2000; // miliseconds
		
		private var channelSet:ChannelSet;
		private var producer:Producer;
		private var consumer:Consumer;
		
		/**
		 * Flag kept in order to know if the bridge is connected.
		 * This is set even if the bridge is in the process of reconnecting.
		 */
		public var connectionEstablished:Boolean;
		
		/**
		 * Flag kept in order to know if this bridge is trying to establish a connection or a reconnection.
		 */
		private var establishingConnection:Boolean;
		
		/**
		 * Flag kept so that disconnect and connect notifications are supressed, 
		 * like when the bridge needs to be reinitialized due to difficulties in establishing or using the connection.
		 */
		private var internalReconnect:Boolean;

		/**
		 * When the bridge is internally reconnecting and no credentials were passed, info from this field is used 
		 * so that the user isn't enoyed to enter credentials again when the connection is lost for a short time.
		 * The credentials are kept from the moment of starting the authentication until the bridge was disconnected.
		 */ 		
		private var lastLoginCredentials:Object = {username : null, password : null};
		
		/**
		 * Timer to notify that a certain amount of time has been passed 
		 * and we should #connect() again.
		 * 
		 * @see #connectLaterCredentials
		 */
		private var connectLaterTimer:Timer;
		
		/**
		 * When a later #connect() is scheduled, credentials are remembered here.
		 */ 
		private var connectLaterCredentials:Object;
		
		/**
		 * Timer to notify later about the undelivered objects from client's fauld, 
		 * in case that a disconnect did not happened.
		 */ 
		private var laterUnderliveredObjectsTimer:Timer;
		
		/**
		 * Keeps a list of undelivered object for which to notify later.
		 */ 
		private var laterUnderliveredObjects:ArrayCollection = new ArrayCollection();		
		
		/**
		 * Initializes ChannelSet, Producer, Consumer, assigns their properties and their listeners.
		 * 
		 * @author Sorin
		 * @auhtor Cristi 
		 */
		public function BlazeDSBridge(explicitEndpointURI:String = null):void {
			if (logger.isDebugEnabled())
				ServerConfig.channelSetFactory = DebugChannelSet;
			
			producer = !logger.isDebugEnabled() ? new CustomProducer() : new DebugCustomProducer(); 
			producer.destination = BLAZEDS_DESTINATION;
			
			consumer = !logger.isDebugEnabled() ? new Consumer() : new DebugConsumer();
			consumer.destination = BLAZEDS_DESTINATION;

			// this info may come from the params in the application
			var channelToUse:String = FlexGlobals.topLevelApplication.parameters.channelToUse;
			initializeChannelSet(channelToUse);			
			
			consumer.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, subscribeSuccessHandler); 
			consumer.addEventListener(MessageFaultEvent.FAULT, subscribeFaultHandler);
			consumer.addEventListener(ChannelEvent.DISCONNECT, unsubscribeHandler);
			consumer.addEventListener(MessageEvent.MESSAGE, messageArrivedHandler); 
			producer.addEventListener(MessageAckEvent.ACKNOWLEDGE, messageArrivedHandler);
			producer.addEventListener(MessageFaultEvent.FAULT, producerFaultHandler);
		}

		/**
		 * Called without username and password for connecting without authentication.
		 * In case that connection can not be done without credentials, 
		 * the client will be notified that it needs to authenticate.
		 * 
		 * @see #loginSuccessHandler, #loginFailHandler
		 * 
		 * In case that the connection can be done without passing credetials,
		 * the client will automatically establish a connection.
		 * 
		 * @see #subscribeSuccessHandler, #subscribeFaultHandler
		 * 
		 * Called also when reinitializing bridge internally which supresses connecting notifications.
		 * 
		 * @see #disconnectAndConnect(), #connectLaterHandler()
		 * 
		 * @author Sorin
 		 * @author Mariana
		 * 
		 */
		public function connect(username : String = null, password : String = null) : void {
			if (password == null || password.length == 0) // Due to a bug in blazeDS, password must not be empty so we consider it same as username.
				password = username;

			if (logger.isLogEnabled())
				logger.log("connecting , username = [" + username + "] , using internal reconnection = " + internalReconnect);

			if (connectionEstablished) {
				logger.log("connecting , Incorrect usage of bridge. You must disconnect() before you connect() again to this bridge!");
				throw "Incorrect usage of bridge. You must disconnect() before you connect() again to this bridge!";
			}
			
			if (!establishingConnection && !internalReconnect)  // first connect and don't supress notification.
				dispatchEvent(new BridgeEvent(BridgeEvent.CONNECTING));
			
			establishingConnection = true;
			clearConnectLater(); // If a connect later was scheduled stop it.

			// If no username was supplied try the simple subscribing which may be the case of connecting after the authentication has been succeeded
			// or in the future the flash player cached thesession id was retrievd and it was still valid. 
			if (username == null) {
				consumer.subscribe(UIDUtil.createUID()); // Avoids server problems about duplicated subscription.
			} else {
				var index:int = username.indexOf("|"); 
				lastLoginCredentials = {username : index == -1 ? username : username.substring(0, index), password : password}; // Store credentials for internal reconnect use. 
				try {
					channelSet.login(username, password).addResponder(
							new AsyncResponder(loginSuccessHandler, loginFailHandler, 
													{username : username, password : password})); // Pass the credentials.
				} catch (e:Error) {
					// Weird behavior. It seems that the servers responds twice that authentication is needed
					// but blazeDS authenticationAgent does not process the first request(or maybe doesn't finish it) so we are given incorrect 
					// instructions and we try to login again which triggers a problem. The solution is to reinitialize the bridge.
					if (e.message != "ChannelSet is in the process of logging in or logging out.") {
						logger.log("connecting , " + e.message);
						throw e;
					} else {
						logger.log("connecting , " + e.message + " , reinitializing"); // TODO pe viitor de sters, nu e nevoie
						disconnectAndConnect(username, password, true /* reinitialize */);
					}
				}
				
			}
		}

		/**
		 * Intended to be called by the user to stop trying to establish a connection.
		 * If a connection was established it notifies about a disconnect happened.
		 * 
		 * @see disconnect()
		 * 
		 * 
		 */
		public function cancelConnecting():void {
			if (logger.isLogEnabled())
				logger.log("cancel connecting, using internal reconnection = " + internalReconnect);
			
			dispatchEvent(new BridgeEvent(BridgeEvent.CONNECTING_CANCELED));
			
			establishingConnection = false;
			clearConnectLater(); // If a connect later was scheduled stop it.
			disconnect(); // by client, acts as a bridge clean out.
		}
		
		/**
		 * Called by the user when it wants to disconnect,
		 * or internally after the user requested to stop trying to connect. 
		 * Called internaly when the servers kicks out the flex client and  
		 * dispatces a disconnect event that it happened from the server.
		 * Called when reinitializing bridge internally which supresses deconnected notifications.
		 * 
		 * 
		 */
		public function disconnect(triggeredByServer:Boolean=false):void {
			if (logger.isLogEnabled())
				logger.log("disconnect , using internal reconnection = " + internalReconnect);

			if (!internalReconnect && connectionEstablished) // don't supress notification and was once connected.
				dispatchEvent(new BridgeEvent(BridgeEvent.DISCONNECTED, triggeredByServer));

			connectionEstablished = false; 
			lastLoginCredentials = {username : null, password : null};
			
			try {
				channelSet.logout();
			} catch (e:Error) {
				// Problem logging out while logging out durring internal reconnect. Probably a 
				// loggout answer from the server could not be obtain because the netword/server died.
				if (e.message != "ChannelSet is in the process of logging in or logging out.")
					throw e; 
			}
			channelSet.disconnectAll();			
		}
		
		/**
		 * Invokes <code>disconnect()</code> but dispatches an additional Event. 
		 * The application needs to distinguish among various reasons for disconnect, this
		 * one, when the user has explicitly pressed logout.
		 * 
		 * @author Cristi
		 */
		public function disconnectBecauseUserLoggedOut():void {
			disconnect();
			dispatchEvent(new BridgeEvent(BridgeEvent.DISCONNECTED_BECAUSE_USER_LOGGED_OUT));
		}
		
		/**
		 * Method indended to be called by user for switching the current user.
		 * Internally called for bridge reinitialization when problems exist due to network problems.
		 * Schedules a connect with the purpose of not flooding the server with requests.
		 * Depending on the <code>internalReconnect</code> parameter it schedules to connect 
		 * sooner when switching and later when reconnecting.
		 * 
		 * @see #connectLaterHandler()
		 * @see #clearConnectLater()
		 */ 
		public function disconnectAndConnect(username:String, password:String, internalReconnect:Boolean = false):void {
			if (logger.isLogEnabled())
				logger.log("disconnect and connect , username = [" + username + "] , as internal reconnection = " + internalReconnect);

			if (connectLaterTimer != null) {
				logger.log("disconnect and connect , " + "Already in the process of waiting to connect later !"); 
				throw "Already in the process of waiting to connect later !";
			}
			
			this.internalReconnect = internalReconnect; // stop dispatching connecting and disconnected notifications.
			connectLaterCredentials = (username != null) ? {username : username, password : password} : lastLoginCredentials; // Store credentials to access them later. If none were passed use the last credentials for login.

			disconnect(); // by client, acts as a bridge clean out.	
			connectLaterTimer = new Timer(internalReconnect ? RETRY_RECONNECTING_INTERVAL : SWITCH_RECONNECTING_INTERVAL, 1); // When switching user wait less that internal reconnecting.
			connectLaterTimer.addEventListener(TimerEvent.TIMER, connectLaterHandler);
			connectLaterTimer.start();
		}
		
		/**
		 * Calls the delayed connect using the stored credentials.
		 */
		private function connectLaterHandler(event:TimerEvent):void {
			FlexClient.getInstance().id = null; // This helps avoiding HTTP-duplicated session problems.
			connect(connectLaterCredentials.username, connectLaterCredentials.password);
			clearConnectLater(); // after connecting, clear internal reconnecting state and stored credentials.
		}

		/**
		 * Stops later connect (if one did not ocurred yet), clears credentials and internal reconnect state.
		 */ 
		private function clearConnectLater():void {
			if (connectLaterTimer == null) 
				return;
			connectLaterTimer.removeEventListener(TimerEvent.TIMER, connectLaterHandler);
			connectLaterTimer.stop();
			connectLaterTimer = null;

			connectLaterCredentials = null;
			internalReconnect = false;			
		}
		
		/**
		 * Sends an object to the server.
		 * If the bridge is not connected it will issue in error.
		 * 
		 * @author Sorin
		 * @author Cristi
		 * 
		 */
		public function sendObject(object:Object):void {
			if (!connectionEstablished) {
				logger.log("Trying to send messages to server while the connection is not established in the bridge. object = " + object); 
				throw "Trying to send messages to server while the connection is not established in the bridge.";
			}
			
			var event:BridgeEvent = new BridgeEvent(BridgeEvent.OBJECT_SENT, object); 
			
			dispatchEvent(event);
			
			if (event.sentObject != null) {
				// a) this means, either the object was originally null, 
				// b) or a handler has set to null, preventing the sending
				// a) shouldn't happen; b) wasn't needed (yet) but maybe in the future will be useful
				producer.send(new AsyncMessage(object));
			}
		}
		
		/**
		 * When the credentials sent to the server where accepted, notifies about the accepted credentials,
		 * and trigger the subscription of the bridge.
		 */ 
		private function loginSuccessHandler(event:ResultEvent, credentials:Object=null):void {
			if (logger.isLogEnabled())
				logger.log("login sucessfully , username = [" + credentials.username + "], event.type = " + event.type + " , event.result = " + event.result);

			dispatchEvent(new BridgeEvent(BridgeEvent.AUTHENTICATION_ACCEPTED, credentials)); // Notifies with accepted credentials.
			clearConnectLater(); // If a connect later was scheduled stop it.
			connect(); // After authenticating, subscribe.
		}
		
		/**
		 * When the credentials sent to the server where not accepted,
		 * notify that authentication is still needed and provides the incorrect credentials.
		 * If any other type of errors happen then a bridge reinitialization is done. 
		 * 
		 * @author Sorin	
		 * @author Mariana
		 */  
		private function loginFailHandler(event:FaultEvent, credentials:Object=null):void {
			if (logger.isLogEnabled())
				logger.log("login FAULT , event.type = " + event.type + ", event.fault.code = " + event.fault.faultCode);

			establishingConnection = false;
			
			if (Utils.beginsWith(event.fault.faultCode, "Client.Authentication")) {
				credentials.faultCode = event.fault.faultCode;
				dispatchEvent(new BridgeEvent(BridgeEvent.AUTHENTICATION_NEEDED, credentials, false)); // Notifies about the rejected credentials. 
			} else {
				disconnectAndConnect(credentials.username, credentials.password, true /* reconnecting state, no notifications about disconnect and connect*/);
			}
		} 

		/**
		 * When a connection has been established or a reconnection has been succeeded, notifications are dispatched. 
		 * 
		 */
		private function subscribeSuccessHandler(event:PropertyChangeEvent):void {
			if (event.property == "subscribed" && event.newValue == true) {
				if (logger.isLogEnabled())
					logger.log("connection successfull");

				establishingConnection = false;
				clearConnectLater(); // If a connect later was scheduled stop it.
				connectionEstablished = true;
				
				dispatchEvent(new BridgeEvent(BridgeEvent.CONNECTED));
			} 
		}
		
		/**
		 * If subscription can not be done due to missing credentials, they are requested.
		 * If other type of errors happened a bridge reinitialization is commanded.
		 */
		private function subscribeFaultHandler(event:MessageFaultEvent):void {
			if (logger.isLogEnabled())
				logger.log("subscription FAULT , event.type = " + event.type + " , event.fault.code = " + event.faultCode);
			
			establishingConnection = false;
			
			if (event.faultCode == "Client.Authentication" && !channelSet.authenticated) {
				dispatchEvent(new BridgeEvent(BridgeEvent.AUTHENTICATION_NEEDED, null, true));
			} else { // If channelSet is authenticated and there are problems with the credentials 
				 // then some weird behaviour happened on the server so we better reinitialize the bridge.  
				disconnectAndConnect(null, null, true /* reconnecting state, no notifications about disconnect and connect*/ );
			}
		}
		
		/**
		 * When a channel disconnect is detected, depending on the reconnecting property of the event :
		 * - either a notification about reconnecting is sent, 
		 * - or a disconnect notification is sent along with the information if the disconnect happened on the server.
		 * 
		 * @author Sorin
		 * @author Mariana
		 */
		private function unsubscribeHandler(event:ChannelEvent):void {
			if (logger.isLogEnabled())
				logger.log("disconnected , event.type = " + event.type + " , event.reconnecting = " + event.reconnecting + " , event.rejected = " + event.rejected);
			
			// Events for undelivered objects are dispatched later if the channel did not disconnect. Because disconnect happened supress dispatching events.
			unscheduleLaterUndeliveredObjects();
			
			if (event.reconnecting) { // Connection problem, but reconnecting.
				// The blazeds disconnected event is received even thought if a connection was not established in the first place
				// Also because the specification is to automatically reconnect, a notification about bridge disconnected is not 
				// dispatched in this handler, but it is dispatched in the #cancelConnecting() method, when it wants to cancel.
				if (connectionEstablished) { // Processing only reconnection
					dispatchEvent(new BridgeEvent(BridgeEvent.CONNECTING));
					disconnectAndConnect(null, null, true);
				} else {
					// Retrying when the connection was not established at least once is 
					// not treated here because a notification about connecting was already
					// sent inside #connect() method when the user called it in the first place.
				}
			} else if (event.rejected) { // Server disconnect, maybe timeout.
				// Trrigered by server. Will call automaticaly logic for logout, disconnecting and dispatching notifications.
				disconnect(true); 
			} else {
				// If not reconnecting and not disconnected from server then disconnect from client.
				// A disconnect from client always is done either by #disconnect method or by #cancelConnection() which calls #disconnect method. 
			} 
		}
		
		/**
		 * Notifies about an undelivered object.
		 * Ignores all errors because other mechanism intepret them for reconnecting.
		 * 
		 * @author Sorin
		 * @author Cristi
		 */ 
		private function producerFaultHandler(event:MessageFaultEvent):void {
			if (logger.isLogEnabled())
				logger.log("producer FAULT , event.type = " + event.type + " , event.fault.code = " + event.faultCode + " , object = " + event.message.extendedData);
			
			var undeliveredObject:Object = event.message.extendedData;

			var bridgeEvent:BridgeEvent = new BridgeEvent(BridgeEvent.FAULT, undeliveredObject); 
			dispatchEvent(bridgeEvent);
			
			if ((undeliveredObject == null) || (bridgeEvent.dontExecuteTheDefaulFaultLogic) || (event.faultCode.indexOf("Server") >= 0)) // Server exception or problem, we ignore it. 
				return;
			
			/**
			 * When an object is detected as being undelivered following cases may happen:
			 * - client did not immediately detect that the connection is unplugged so sending it triggered an fault.
			 * This is most common case and usually a disconnect happenes. For this case we decided not to show the undelivered object.
			 * - client tries to send an object but for unforseen motives at that time the object could not be delivered.
			 * This case was not seen but we expect to happen some time. That's why for this case if a disconnect did not happen soon
			 * notification for undelivred objects are sent.
			 */ 
			scheduleLaterUndeliveredObject(undeliveredObject); // Schedule to dispatch events later in case that disconnect did not happened.
		}
		
		private function scheduleLaterUndeliveredObject(undeliveredObject:Object):void {
			laterUnderliveredObjects.addItem(undeliveredObject);
			if (laterUnderliveredObjectsTimer != null) 
				return;
			laterUnderliveredObjectsTimer = new Timer(DISPATCH_UNDELIVERED_OBJECTS_INTERVAL, 1); // once
			laterUnderliveredObjectsTimer.addEventListener(TimerEvent.TIMER, laterUndeliveredObjectsHandler);
			laterUnderliveredObjectsTimer.start();
		}
		
		private function laterUndeliveredObjectsHandler(event:TimerEvent):void {
			var copy:ArrayCollection = new ArrayCollection();
			copy.addAll(laterUnderliveredObjects);
			dispatchEvent(new BridgeEvent(BridgeEvent.OBJECT_UNDELIVERED, copy));
			unscheduleLaterUndeliveredObjects();
		}
		
		private function unscheduleLaterUndeliveredObjects():void {
			if (laterUnderliveredObjectsTimer == null) 
				return;
			laterUnderliveredObjects.removeAll();
			laterUnderliveredObjectsTimer.stop();
			laterUnderliveredObjectsTimer.removeEventListener(TimerEvent.TIMER, laterUndeliveredObjectsHandler);
			laterUnderliveredObjectsTimer = null;
		}
	
		/**
		 * When a message is received from the server a notification with the object is dispatched.
		 * This also handles http responses from the server.
		 * @see WebCommunicationChannelManager.java#invoke()
		 * 
		 */
		private function messageArrivedHandler(event:MessageEvent):void {
			dispatchEvent(new BridgeEvent(BridgeEvent.OBJECT_RECEIVED, event.message.body));
		}
		
		public function get channelType() : String {
			return channelSet.currentChannel.id;
		}
		
		/**
		 * It's the Flex Client ID. Don't mistake this with message client id.
		 */
		public function get communicationChannelId() : String {
			return FlexClient.getInstance().id;
		}
		
		public function getLastLoginUserName():String {
			return lastLoginCredentials.username;
		}

		/**
		 * Used by the debug UI to propose entries in the "Connect using channels:" combo box.
		 * 
		 * @author Cristi
		 */
		public function getAvaiableChannels():ArrayCollection {
			return new ArrayCollection([BLAZEDS_ALL_CHANNELS, BLAZEDS_STREAMING_CHANNEL, BLAZEDS_POLLING_CHANNEL]);
		}
		
		/**
		 * Creates the channel set (containing 1 or 2 channels, depending on parameter) and assigns it to the
		 * producer and consumer.
		 * 
		 * @author Cristi
		 */
		public function initializeChannelSet(channelToUse:String):void {
			if (connectionEstablished) {
				throw new Error("Cannot initialize the ChannelSet while the connection to server is established.");
			}
			
			if (channelToUse == null) {
				channelToUse = BLAZEDS_ALL_CHANNELS;
			}
			
			channelSet = new ChannelSet();
			
			if (channelToUse == BLAZEDS_STREAMING_CHANNEL || channelToUse == BLAZEDS_ALL_CHANNELS) {
				var streamingChannel:StreamingAMFChannel = new StreamingAMFChannel("blazedsStreamingAMFChannel", FlexUtilGlobals.getInstance().createAbsoluteUrl("messagebroker/streamingamf"));
				channelSet.addChannel(streamingChannel);
			}
			
			if (channelToUse == BLAZEDS_POLLING_CHANNEL || channelToUse == BLAZEDS_ALL_CHANNELS) {
				var pollingChannel:AMFChannel = new AMFChannel("blazedsLongPollingAMFChannel", FlexUtilGlobals.getInstance().createAbsoluteUrl("messagebroker/amflongpolling"));
				pollingChannel.pollingEnabled = true;
				pollingChannel.pollingInterval = 3000;
				channelSet.addChannel(pollingChannel);
			}
			
			producer.channelSet = channelSet;
			consumer.channelSet = channelSet;
		}
		
		private static const CLIENT_CONNECTING_RETRY_PERIOD:String = "client.connecting.retry.period";
		
		private static const CLIENT_SWITCH_USER_INTERVAL:String = "client.switch.user.interval";
		
		private static const CLIENT_DISPATCH_UNDELIVERED_OBJECTS_INTERVAL:String = "client.dispatch.undelivered.objects.interval";
		
		public function updateProperties(properties:Object):void {
			if (properties[CLIENT_CONNECTING_RETRY_PERIOD] != null)
				RETRY_RECONNECTING_INTERVAL = int(properties[CLIENT_CONNECTING_RETRY_PERIOD]);
			
			if (properties[CLIENT_SWITCH_USER_INTERVAL] != null)
				SWITCH_RECONNECTING_INTERVAL = int(properties[CLIENT_SWITCH_USER_INTERVAL]);
			
			if (properties[CLIENT_DISPATCH_UNDELIVERED_OBJECTS_INTERVAL] != null)
				DISPATCH_UNDELIVERED_OBJECTS_INTERVAL = int(properties[CLIENT_DISPATCH_UNDELIVERED_OBJECTS_INTERVAL]);
			
			producer.reconnectInterval = RETRY_RECONNECTING_INTERVAL; 
			consumer.resubscribeInterval = RETRY_RECONNECTING_INTERVAL;
		}
	}
}

import mx.messaging.Producer;
import mx.messaging.messages.AsyncMessage;
import mx.messaging.messages.ErrorMessage;
import mx.messaging.messages.IMessage;

class CustomProducer extends Producer {
	
	/**
	 * Enhances the fault error message with the undelivered message.
	 * Notice that it tests that the undelivered message is of certain instance,
	 * the one in BlazeDSSecureBridge#send method.
	 */		
	override public function fault(errMsg:ErrorMessage, msg:IMessage):void {
		// keep inside extended data, the orginal object, but only if the message is our AsyncMessage
		if (Object(msg).constructor == AsyncMessage) {
			errMsg.extendedData = msg.body;
		}
		super.fault(errMsg, msg);
		
	}
}	

// START DEBUG SECTION ///////////////////////////////////////
// Enchances some classes to print their dispatched events.

import flash.events.Event;

import mx.events.PropertyChangeEvent;
import mx.messaging.ChannelSet;
import mx.messaging.Consumer;

class EventPrinterUtil {
	
	public static function printEvent(caller:String, event:Event):void {
		var message : String = "Debug BlazeDSBridge : " + caller + " type = " + event.type + " ";
		if (event is PropertyChangeEvent)
			message += "property = " + PropertyChangeEvent(event).property + " new value = " + PropertyChangeEvent(event).newValue + " old value = " + PropertyChangeEvent(event).oldValue;
		else
			message += event.toString();
		trace(message);
	}
}

class DebugConsumer extends Consumer {
	
	override public function dispatchEvent(event:Event):Boolean {
		EventPrinterUtil.printEvent("Consumer", event);
		return super.dispatchEvent(event);
	}
}

class DebugCustomProducer extends CustomProducer {
	
	override public function dispatchEvent(event:Event):Boolean {
		EventPrinterUtil.printEvent("Producer", event);
		return super.dispatchEvent(event);
	}
}

class DebugChannelSet extends ChannelSet {
	
	public function DebugChannelSet(channelIds:Array = null, clusteredWithURLLoadBalancing:Boolean = false) {
		super(channelIds, clusteredWithURLLoadBalancing);
	}
	
	override public function dispatchEvent(event:Event):Boolean {
		EventPrinterUtil.printEvent("ChannelSet", event);
		return super.dispatchEvent(event);
	}
}
// END DEBUG SECTION ///////////////////////////////////////