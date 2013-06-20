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
