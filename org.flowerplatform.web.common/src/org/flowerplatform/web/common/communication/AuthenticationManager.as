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
package  org.flowerplatform.web.common.communication {
	
	import mx.core.FlexGlobals;
	
	import org.flowerplatform.blazeds.BlazeDSBridge;
	import org.flowerplatform.blazeds.BridgeEvent;
	import org.flowerplatform.common.CommonPlugin;
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.command.HelloServerCommand;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.layout.IViewProvider;
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.flexutil.resources.ResourceUpdatedEvent;
	import org.flowerplatform.web.common.WebCommonPlugin;
	import org.flowerplatform.web.common.security.dto.User_CurrentUserLoggedInDto;
	
	/**
	 * @author Sorin
	 * 
	 */
	public class AuthenticationManager {
		
		public var bridge:BlazeDSBridge;
		
		/**
		 * Keep the authenticated user, even if anonymous, and it is used by the menu bar to show 
		 * the current user.
		 */ 
		private var authenticatedUser:String;
		
		/**
		 * Remembers the last authenticating user provided in the authentication popup.
		 * In case of automatically retrying to connect as anonymous and it failed,
		 * the popup should not show the anonymous user, and instead the previously 
		 * typed user. So this will never hold an anonymous user.
		 */ 
		public var lastProvidedUsername:String;
		
		/**
		 * Flag needed to know if to enable or not the authenticate as anonymous option
		 * in the authentication popup.
		 * 
		 * Updated when anonymous authentication is failed in #notifyAuthenticationNeede mehtod
		 * and cleared when establishing a connection #notifyConnected, when giving up to this 
		 * establishing of connection #notifyConnectingCanceled. 
		 * It is not updated when disconnecting or canceling reconnecting because the case
		 * of disconnecting it was already cleared by connected or cancel connecting, and
		 * in the case of canceling reconnecting it is never about an reauthentication.
		 */
		private var anonymousAuthenticationRejected:Boolean;

		/**
		 * @author Mariana
		 */
		public var authenticationView:AuthenticationView;
		
		/**
		 * @author Cristi
	 	 */
		public var currentUserLoggedIn:User_CurrentUserLoggedInDto;
		
		/**
		 * Keeps the value for "Keep layout structure" checkbox from auth dialog.
		 * It will be used later to switch or not the current user perspective layout.
		 * @see SwitchPerspectiveClientCommand
		 */ 
		public var keepLayoutStructure:Boolean;
		
		private var connectingView:ConnectingView;
		
		/**
		 * 
		 * 
		 * @author Sorin
		 * @author Cristi
		 */
		public function AuthenticationManager() {
			bridge = CommunicationPlugin.getInstance().bridge;
			
			bridge.addEventListener(BridgeEvent.CONNECTING, handleConnecting);
			bridge.addEventListener(BridgeEvent.CONNECTED, handleConnected);
			// we want this listener to be the first one notified, to remove the spinner
			bridge.addEventListener(BridgeEvent.WELCOME_RECEIVED_FROM_SERVER, handleWelcomeReceivedFromServer, false, 10);

			bridge.addEventListener(BridgeEvent.CONNECTING_CANCELED, handleConnectingCanceled);
			
			bridge.addEventListener(BridgeEvent.AUTHENTICATION_NEEDED, handleAuthenticationNeeded);
			bridge.addEventListener(BridgeEvent.AUTHENTICATION_ACCEPTED, handleAuthenticationAccepted);
			
			bridge.addEventListener(BridgeEvent.DISCONNECTED, handleDisconnected);
			
			bridge.addEventListener(BridgeEvent.FAULT, handleFault);
			bridge.addEventListener(BridgeEvent.OBJECT_UNDELIVERED, handleObjectUndelivered);
			
			showAuthenticationView();
			
//			bridge.connect("admin", "a");
		}
		
		/**
		 * @author Cristi
		 * @author Mariana
		 */
		public function showAuthenticationView(switchUserMode:Boolean = false, providedUsername:String = null, anonymousFailed:Boolean = false, showActivationCodeField:Boolean = false):void {
			if (authenticationView == null) {
				var authenticationViewProvider:IViewProvider = FlexUtilGlobals.getInstance().composedViewProvider.getViewProvider(AuthenticationViewProvider.ID);
				authenticationView = IPopupContent(authenticationViewProvider.createView(null)) as AuthenticationView;
				FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
					.setPopupContent(authenticationView)
					.show();
			}
			authenticationView.addEventListener(ResourceUpdatedEvent.RESOURCE_UPDATED, authenticationView.resourceUpdated);
			authenticationView.switchUserMode = switchUserMode;
			authenticationView.providedUsername = providedUsername;
			authenticationView.showActivationCodeField = showActivationCodeField;
		}
		
		public function handleConnecting(event:BridgeEvent):void {
//			ModalSpinner.addGlobalModalSpinner(CommonPlugin.getInstance().getMessage("spinner.connecting"), new ReconnectingSpinner());
			connectingView = new ConnectingView()
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setPopupContent(connectingView)
				.showModalOverAllApplication();
		}
		
		/**
		 * 
		 */
		public function handleConnected(event:BridgeEvent):void {
			if (connectingView != null) {
				connectingView.currentState = "initializing";
			}
			
			var hello:HelloServerCommand = new HelloServerCommand();
			hello.clientApplicationVersion = CommonPlugin.VERSION;
			hello.firstWelcomeWithInitializationsReceived = CommunicationPlugin.getInstance().firstWelcomeWithInitializationsReceived;
			CommunicationPlugin.getInstance().bridge.sendObject(hello);
			
//			// We have established a connection for the first time so we clear this flag, in order
//			// for the user to be able to login anonymously.
			anonymousAuthenticationRejected = false; 
//		
//			ModalSpinner.removeGlobalModalSpinner();
		}

		public function handleWelcomeReceivedFromServer(event:BridgeEvent):void {
			if (connectingView != null) {
				FlexUtilGlobals.getInstance().popupHandlerFactory.removePopup(connectingView);
				connectingView = null;
			}
		}
		
		public function handleConnectingCanceled(event:BridgeEvent):void {
			if (connectingView != null) {
				FlexUtilGlobals.getInstance().popupHandlerFactory.removePopup(connectingView);
				connectingView = null;
			}
//			// We have established a connection for the first time so we clear this flag, in order
//			// for the user to be able to login anonymously.
			anonymousAuthenticationRejected = false;
//
//			ModalSpinner.removeGlobalModalSpinner();
//			AuthenticationPopup.showPopup(false, lastProvidedUsername, anonymousAuthenticationRejected); // login mode
		}
		
		/**
		 * @author Sorin
		 * @author Mariana
		 * 
		 * 
		 */
		public function handleAuthenticationNeeded(event:BridgeEvent):void {
			var failedUsername:String = event.rejectedCredentials ? event.rejectedCredentials.username : null;
			var failedIsAnonymous:Boolean = failedUsername == getAnonymousUser();
			
			FlexUtilGlobals.getInstance().popupHandlerFactory.removePopup(connectingView);
			connectingView = null;
			
			if (event.firstCredentialsRequest) {
				// If user provided login, show the login popup pre-filled with the login
				if (FlexGlobals.topLevelApplication.parameters.login) {
					showAuthenticationView(false, FlexGlobals.topLevelApplication.parameters.login, anonymousAuthenticationRejected);
				} else {
					// Try automatically the anonymous user. 
					bridge.connect(getAnonymousUser());
				}
				// No need to save last authenticating user because first time correspond to a server request 
				// for authentication and not for a server faild for authentication.
			} else {
				var activationFailed:Boolean = false;
				if (failedIsAnonymous) {
					// From now on, we are not allowed anymore to present to the user the popup with 
					// anonymous login being activated. It can be seen disabled if the ststem tried 
					// to login automatically, or if the user tried to enter anonymous and it was rejected.  
					anonymousAuthenticationRejected = true;
				} else {
					// Updates the last provided username in order to show it again in the popup  
					lastProvidedUsername = failedUsername;
					if (event.rejectedCredentials) {
						var faultCode:String = event.rejectedCredentials.faultCode;
						if (faultCode == "Client.Authentication.NotActivated") {
							// user is not activated
							handleActivationNeeded(event);
							return;
						} else { 
							if (faultCode == "Client.Authentication.UserAlreadyActivated") {
								// user already activated
//								AuthenticationPopup.activationNeeded = false; // don't show the activation code field again
								activationFailed = true;
							}
						}
					}
				}
				// Shows the popup with the last provided username by the user itself (can not be the anonymous),
				// and with the entering anonymously enabled or not.
				showAuthenticationView(false, lastProvidedUsername, anonymousAuthenticationRejected);
				if (activationFailed) {
					FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
						.setText("This user is already activated!")
						.setTitle("Error")
						.showMessageBox();
					return;
				}
			} 
			// When being notified the first time it means that no explicit login was done, 
			// no credentials were provided so there is no sense to report that the credentials are wrong.
			// If the current user that failed it's credentials is anonymous then again there
			// is no need to show warning that credentials were not correct because no user inputed them.
			if (!event.firstCredentialsRequest && !failedIsAnonymous)
				FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
					.setText(WebCommonPlugin.getInstance().getMessage("authentication.error.wrongLoginOrPassword"))
					.setText(CommonPlugin.getInstance().getMessage("error"))
					.showMessageBox();
		}
		
		public function handleAuthenticationAccepted(event:BridgeEvent):void {
			// Keep the accepted user.
			authenticatedUser = event.acceptedCredentials.username;
			// Keep the accepted user only if it is not anonymous. If it is then keep the old one.
			if (authenticatedUser != getAnonymousUser()) {
				lastProvidedUsername = authenticatedUser;	
			}
			FlexUtilGlobals.getInstance().popupHandlerFactory.removePopup(authenticationView);
			authenticationView = null;
		}
		
		/**
		 * Handles the case when the client tried to login with an unactivated user without providing an activation code or with an incorrect 
		 * activation code.
		 * 
		 * @author Mariana
		 */ 
		public function handleActivationNeeded(event:BridgeEvent):void {
			lastProvidedUsername = event.rejectedCredentials ? event.rejectedCredentials.username : null;
			anonymousAuthenticationRejected = lastProvidedUsername == getAnonymousUser();
//			AuthenticationPopup.activationNeeded = true;
			showAuthenticationView(false, lastProvidedUsername, anonymousAuthenticationRejected, true);
			FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
				.setText(WebCommonPlugin.getInstance().getMessage("authentication.error.noActivationCode"))
				.setTitle(CommonPlugin.getInstance().getMessage("error"))
				.showMessageBox();
		}
		
		public function handleDisconnected(event:BridgeEvent):void {
			// After disconnecting we invalidate the local kept authenticated user.
			authenticatedUser = null;
			
//			WebPlugin.getInstance().authenticationMenuBar.updateMenuItems(false); // disconnected
			// When disconnecting we automatically show the login popup until the user reacts and provides credentials.
			// Until the no connection establishing is in progress (no spinner).
//			AuthenticationPopup.showPopup(false, lastProvidedUsername, anonymousAuthenticationRejected); // login mode
			showAuthenticationView(false, lastProvidedUsername, anonymousAuthenticationRejected);
		}

		/**
		 * @author Cristi
		 */
		public function handleFault(event:BridgeEvent):void {
//			if (event.sentObject is InvokeStatefulServiceMethodServerCommand && 
//					InvokeStatefulServiceMethodServerCommand(event.sentObject).serviceId == ChannelObserverStatefulClient.SERVICE_ID) {
//				// the undelivered is a signal of heartbeat or activity which triggers the reconnecting behavior.
//				event.dontExecuteTheDefaulFaultLogic = true;
//			}
		}
		
		public function handleObjectUndelivered(event:BridgeEvent):void {
//			Alert.show("The following command have not been delivered : [ " + event.undeliveredObjects.source.join(", ") + " ] \n" +
//					   "Please contact your administrator.");
		}
		
		/**
		 * If the request to load the swf file came with a the <code>organization</code> parameter
		 * return it.
		 */ 
		public function getAnonymousUser():String {
//			var organization:String = ApplicationParametersProvider.INSTANCE.getOrganization();
//			if (organization == null)
				return "anonymous";
//			else
//				return "anonymous" + "." + organization;
		}
	}
}