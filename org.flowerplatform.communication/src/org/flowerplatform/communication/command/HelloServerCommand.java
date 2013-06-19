package org.flowerplatform.communication.command;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sent by the client app when it becomes "alive". This includes a fresh
 * boot of the client app OR a reconnect.
 * 
 * @see #executeCommand()
 * @author Cristi
 * @flowerModelElementId _IR0rkM9iEeG9Xt8Mxhv6rg
 */
public class HelloServerCommand extends AbstractServerCommand {
	
	/**
	 * @flowerModelElementId _tIuKg9AVEeG3GYv-He135A
	 */
	private static final Logger logger = LoggerFactory.getLogger(HelloServerCommand.class);
	
	/**
	 * @see Getter doc.
	 * @flowerModelElementId _4mx5kM9iEeG9Xt8Mxhv6rg
	 */
	private String clientApplicationVersion;

	/**
	 * @see Getter doc.
	 * @flowerModelElementId _tIvYoNAVEeG3GYv-He135A
	 */
	private boolean firstWelcomeWithInitializationsReceived;
	
	/**
	 * The version of the client (statically linked within the SWF), which
	 * should match the version from the server.
	 * @flowerModelElementId _tIv_sNAVEeG3GYv-He135A
	 */
	public String getClientApplicationVersion() {
		return clientApplicationVersion;
	}

	/**
	 * @flowerModelElementId _tIwmwNAVEeG3GYv-He135A
	 */
	public void setClientApplicationVersion(String clientApplicationVersion) {
		this.clientApplicationVersion = clientApplicationVersion;
	}

	/**
	 * The initialization state of the client. <code>false</code> means it's
	 * the first connection => the server should send the initializations. 
	 * <code>true</code> means it is a reconnection, so no initializations
	 * need to be sent.
	 * @flowerModelElementId _tIx04NAVEeG3GYv-He135A
	 */
	public boolean isFirstWelcomeWithInitializationsReceived() {
		return firstWelcomeWithInitializationsReceived;
	}

	/**
	 * @flowerModelElementId _tIx049AVEeG3GYv-He135A
	 */
	public void setFirstWelcomeWithInitializationsReceived(boolean clientApplicationInitialized) {
		this.firstWelcomeWithInitializationsReceived = clientApplicationInitialized;
	}

	/**
	 * Performs the following:
	 * <ul>
	 * 	<li>Checks the client version vs. server version.
	 * 	<li>If the custom serialization descriptors have not been received
	 * 		(i.e. server freshly started) => requests them (using {@link RequestCustomSerializationDescriptorsClientCommand}).
	 * 	<li>Otherwise, if the client is not initialized, delegates to {@link CommunicationChannel#sendInitializationsForNewClient()}
	 * </ul>
	 * 
	 * @flowerModelElementId _yC7yEM9iEeG9Xt8Mxhv6rg
	 */
	public void executeCommand() {
		if (!CommonPlugin.VERSION.equals(getClientApplicationVersion())) {
			// problem; client/server app version mismatch
			logger.error("Client/Server Version Error; client = {}, server = {}", getClientApplicationVersion(), CommonPlugin.VERSION);
			getCommunicationChannel().appendCommandToCurrentHttpResponse(
					// TODO CS/FP2 msg
					new DisplaySimpleMessageClientCommand(
							"Client/Server Version Error", 
							String.format("A client application with version = %s tries to connect to a server application with version = %s.\nPlease try the following actions:\n\n" +
									" * Refresh this web page (i.e. using Refresh/Reload button of your browser)\n" +
									" * Clear the cache of your browser and refresh this page\n" +
									" * If the problem persists and you are behind a proxy that caches pages, please contact your network administrator.", getClientApplicationVersion(), CommonPlugin.VERSION), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));
		} else {
			WelcomeClientCommand welcome = new WelcomeClientCommand();
			// client/server app version check OK!
			if (!isFirstWelcomeWithInitializationsReceived()) {
				// client is newly connected
				// => send initial data
				logger.debug("Hello received from client = {}. Sending initializations.", getCommunicationChannel());
				welcome.setContainsFirstTimeInitializations(true);
				IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.flowerplatform.communication.welcomeInitializationsContributor");
				for (IConfigurationElement configurationElement : configurationElements) {
					try {
						AbstractClientCommand command = (AbstractClientCommand) configurationElement.createExecutableExtension("commandToAppendToWelcomeClientCommand");
						welcome.appendCommand(command);
					} catch (CoreException e) {
						logger.error("Error while adding welcome initialization command", e);
					}
				}
			} else {
				// client already initialized
				logger.debug("Hello received from client = {}. Client is initialized.", getCommunicationChannel());
			}
			getCommunicationChannel().appendCommandToCurrentHttpResponse(welcome);
		}
	}

}