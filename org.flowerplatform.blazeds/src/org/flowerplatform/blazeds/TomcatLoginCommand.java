package org.flowerplatform.blazeds;


import java.security.Principal;
import java.util.List;

import javax.security.auth.Subject;
import javax.servlet.ServletConfig;

import org.flowerplatform.communication.IPrincipal;
import org.flowerplatform.communication.IUser;

import flex.messaging.security.LoginCommand;


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
		String password = credentials.toString();
		
		if ("anonymous".equals(login) || "test".equals(login)) {
			final Subject subject = new Subject();		
			
			IPrincipal principal = new IPrincipal() {
				
				@Override
				public String getName() {
					return username;
				}

				@Override
				public Subject getSubject() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public IUser getUser() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public long getUserId() {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public void clearCachedUser() {
					// TODO Auto-generated method stub
					
				}
			};

			subject.getPrincipals().add(principal);
			subject.setReadOnly();
			return principal;
		}
//		
//		List<User> users = new Dao().findByField(User.class, "login", login);
//		
//		// No user found, or too many, or no password, or password not correct according to GeneralService#createUser 
//		if (users.size() != 1 || password == null || !Util.encrypt((String) password).equals( users.get(0).getHashedPassword())) { 
//			return null;
//		}
//		
//		User user = users.get(0);
//		
//		// if an activation code was sent, try activating the user
//		if (credentialsProps.length > 1) {
//			if (user.isActivated()) {
//				// if the user is already activated, throw an exception
//				SecurityException se = new SecurityException();
//				se.setCode(SecurityException.CLIENT_AUTHENTICATION_CODE + ".UserAlreadyActivated");
//				throw se;
//			} else {
//				UserService.getInstance().activateUser(login, credentialsProps[1]);
//			}
//		}
//	
//		// check if the user is activated; we throw this exception to differentiate from the case when username or password is incorrect
//		user = new Dao().find(User.class, user.getId());
//		if (!user.isActivated()) {
//			SecurityException se = new SecurityException();
//			se.setCode(SecurityException.CLIENT_AUTHENTICATION_CODE + ".NotActivated");
//			throw se;
//		}
//		
//		return new FlowerWebPrincipal(users.get(0).getId());
		return null;
	}

	/**
	 * @flowerModelElementId _7zYyk3dJEeGzz9ZUhe52dw
	 */
	@SuppressWarnings("unchecked")
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