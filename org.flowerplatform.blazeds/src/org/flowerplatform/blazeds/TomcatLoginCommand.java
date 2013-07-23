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
package org.flowerplatform.blazeds;


import java.security.Principal;
import java.util.List;

import javax.servlet.ServletConfig;

import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.IAuthenticator.AuthenticationResult;
import flex.messaging.security.LoginCommand;
import flex.messaging.security.SecurityException;

/**
 * @author Sorin
 * @flowerModelElementId _dGJ9gG23EeGQ6LdvAwMt-w
 */
public class TomcatLoginCommand implements LoginCommand {

	/**
	 * @param username contains the username and an optional activation code,
	 * formatted as <code>username|activation_code</code>
	 * @param credentials password
	 * 
	 * @author Sorin
	 * @author Mariana
	 * 
	 * @flowerModelElementId _7zXkcHdJEeGzz9ZUhe52dw
	 */
	@Override
	public Principal doAuthentication(final String username, Object credentials) {
		String[] credentialsProps = username.toString().split("\\|");
		String login = credentialsProps[0];
		String activationCode = credentialsProps.length > 1 ? credentialsProps[1] : null;
		String password = credentials.toString();
		
		AuthenticationResult result = CommunicationPlugin.getInstance().getAuthenticator().authenticate(login, password, activationCode);
		
		switch (result) {
		case OK:
			return CommunicationPlugin.getInstance().getAuthenticator().getPrincipal(result.getId());
		
		// we throw an exception to differentiate from the case when username or password is incorrect
		case ALREADY_ACTIVATED:
			SecurityException seAlreadyActivated = new SecurityException();
			seAlreadyActivated.setCode(SecurityException.CLIENT_AUTHENTICATION_CODE + ".UserAlreadyActivated");
			throw seAlreadyActivated;
		case NOT_ACTIVATED:
			SecurityException seNotActivated = new SecurityException();
			seNotActivated.setCode(SecurityException.CLIENT_AUTHENTICATION_CODE + ".NotActivated");
			throw seNotActivated;
		
		default:
			return null;
		}
	}

	/**
	 * @flowerModelElementId _7zYyk3dJEeGzz9ZUhe52dw
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean doAuthorization(Principal principal, List roles) {
		return true;
	}

	/**
	 * @flowerModelElementId _7zanw3dJEeGzz9ZUhe52dw
	 */
	@Override
	public boolean logout(Principal principal) {
		// No problem logging out
		return true;
	}

	/**
	 * @flowerModelElementId _7zb14XdJEeGzz9ZUhe52dw
	 */
	@Override
	public void start(ServletConfig servletConfig) {
		// nothing
		// This could be used maybe to establish connection to the DB.
	}

	/**
	 * @flowerModelElementId _7zcc83dJEeGzz9ZUhe52dw
	 */
	@Override
	public void stop() {
		// nothing
		// This could be used to stop the connection to the DB.
	}

}