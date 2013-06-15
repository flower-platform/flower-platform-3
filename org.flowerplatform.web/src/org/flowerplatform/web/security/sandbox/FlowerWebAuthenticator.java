package org.flowerplatform.web.security.sandbox;

import java.util.List;

import org.flowerplatform.communication.IAuthenticator;
import org.flowerplatform.communication.IPrincipal;
import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;
import org.flowerplatform.web.entity.User;
import org.flowerplatform.web.security.service.UserService;
import org.flowerplatform.web.security.service.Util;

/**
 * @author Mariana
 */
public class FlowerWebAuthenticator implements IAuthenticator {

	@Override
	public AuthenticationResult authenticate(final String login, final String password, final String activationCode) {
		final AuthenticationResult[] result = new AuthenticationResult[1];
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				List<User> users = wrapper.findByField(User.class, "login", login);
				
				// No user found, or too many, or no password, or password not correct according to GeneralService#createUser 
				if (users.size() != 1 || password == null || !Util.encrypt((String) password).equals( users.get(0).getHashedPassword())) { 
					result[0] = AuthenticationResult.INCORRECT_CREDENTIALS;
				}
				
				User user = users.get(0);
				
				// if an activation code was sent, try activating the user
				if (activationCode != null) {
					if (user.isActivated()) {
						// the user is already activated
						result[0] = getResult(AuthenticationResult.ALREADY_ACTIVATED, user.getId());
					} else {
						UserService.getInstance().activateUser(login, activationCode);
					}
				}
			
				// check if the user is activated
				user = wrapper.find(User.class, user.getId());
				if (!user.isActivated()) {
					result[0] = getResult(AuthenticationResult.NOT_ACTIVATED, user.getId());
				}
				
				result[0] = getResult(AuthenticationResult.OK, user.getId());
			}
		});
		
		return result[0];
	}

	@Override
	public IPrincipal getPrincipal(long id) {
		return new FlowerWebPrincipal(id);
	}
	
	private AuthenticationResult getResult(AuthenticationResult result, long id) {
		result.setId(id);
		return result;
	}

}
