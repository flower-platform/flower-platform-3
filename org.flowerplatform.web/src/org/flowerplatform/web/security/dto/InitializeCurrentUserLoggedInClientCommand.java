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
package org.flowerplatform.web.security.dto;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.command.AbstractClientCommand;
import org.flowerplatform.web.security.sandbox.FlowerWebPrincipal;
import org.flowerplatform.web.security.service.UserService;

/**
 * @author Mariana
 */
public class InitializeCurrentUserLoggedInClientCommand extends AbstractClientCommand {

	private User_CurrentUserLoggedInDto user;
	
	public InitializeCurrentUserLoggedInClientCommand() {
		user = UserService.getInstance().convertUserToUser_LoggedInDto(
				((FlowerWebPrincipal) CommunicationPlugin.tlCurrentPrincipal.get()).getUser());
	}

	public User_CurrentUserLoggedInDto getUser() {
		return user;
	}

	public void setUser(User_CurrentUserLoggedInDto user) {
		this.user = user;
	}

}