package org.flowerplatform.web.security.service;

import java.util.List;

import org.flowerplatform.web.security.dto.OrganizationAdminUIDto;
import org.flowerplatform.web.security.dto.UserAdminUIDto;
import org.flowerplatform.web.security.mail.SendMailService;
import org.flowerplatform.web.security.sandbox.SecurityEntityAdaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.flowerplatform.web.WebPlugin;
import org.flowerplatform.web.entity.User;

/**
 * @author Mariana
 */
public class RegisterUserService extends UserService {
	
	private static final Logger logger = LoggerFactory.getLogger(RegisterUserService.class);
	
	/**
	 * Check if there is already an existing user with the same login, or if the
	 * email address is valid before registering this new user.
	 */
	public String register(UserAdminUIDto dto, String organizationPrefix) {
		logger.debug("Register new user with login = {}", dto.getLogin());
		
		dto.setIsActivated(false);
		String message = mergeAdminUIDto(dto);
		
		if (message == null) {
			User user = getDao().findByField(User.class, "login", dto.getLogin()).get(0);
			sendActivationCodeForUser(user, organizationPrefix);
		}
		
		logger.debug("Resistration successful");
		return message;
	}
	
	/**
	 * Check if the user exists and is activated; otherwise, send the appropriate message. 
	 * If user is not yet activated, resend the activation code. Returns a message that will
	 * be displayed to the client.
	 */
	public String resendActivationCode(String login, String organizationFilter) {
		logger.debug("Resend activation code for user with login = {}", login);
		List<User> existingUsers = getDao().findByField(User.class, "login", login);
		if (existingUsers.size() == 1) {
			if (!existingUsers.get(0).isActivated()) {
				sendActivationCodeForUser(existingUsers.get(0), organizationFilter);
				return "The activation code was sent to your mail box.";
			} else {
				return "This user is already activated!";
			}
		} else {
			return "This user does not exist!";
		}
	}
	
	private final String RECOVER_PASSWORD_SUBJECT = "mail.template.recover.password.subject";
	private final String RECOVER_PASSWORD_BODY = "mail.template.recover.password.body";
	private final String RECOVER_USERNAME_SUBJECT = "mail.template.recover.username.subject";
	private final String RECOVER_USERNAME_BODY = "mail.template.recover.username.body";
	
	/**
	 * Resets the password for the user with the given <code>login</code> and sends it
	 * by email. Returns <code>true</code> if the password was reset and sent, and 
	 * <code>false</code> if the user does not exist.
	 */
	public boolean forgotPassword(String login) {
		logger.debug("Forgot password request for user with login = {}", login);
		
		List<User> existingUsers = getDao().findByField(User.class, "login", login);
		if (existingUsers.size() == 0)
			return false;
		
		User user = existingUsers.get(0);
		
		if (user.getLogin().startsWith(SecurityEntityAdaptor.ANONYMOUS)) {
			return false; // do not allow reset for anonymous
		}
		
		String newPassword = generateRandomString();
		user.setHashedPassword(Util.encrypt(newPassword));
		getDao().merge(user);
		
		String subject = WebPlugin.getInstance().getMessage(RECOVER_PASSWORD_SUBJECT);
		String content = WebPlugin.getInstance().getMessage(RECOVER_PASSWORD_BODY,
				new Object[] { 
					user.getLogin(),
					user.getName(),
					user.getEmail(),
					SendMailService.getInstance().getServerUrl(),
					newPassword 
				});
		
		SendMailService.getInstance().send(user.getEmail(), subject, content);
		return true;
	}
	
	/**
	 * Checks if there is a registered user with the given <code>email</code>.
	 * Resets the password and sends the username and new password to the user.
	 * Returns <code>false</code> if the user does not exist, <code>true</code>
	 * otherwise.
	 */
	public boolean forgotUsername(String email) {
		logger.debug("Forgot username request for user with email = {}", email);
		
		List<User> existingUsers = getDao().findByField(User.class, "email", email);
		if (existingUsers.size() == 0)
			return false;
		
		User user = existingUsers.get(0);
		
		if (user.getLogin().startsWith(SecurityEntityAdaptor.ANONYMOUS)) {
			return false; // do not allow reset for anonymous
		}
		
		String newPassword = generateRandomString();
		user.setHashedPassword(Util.encrypt(newPassword));
		getDao().merge(user);
		
		String subject = WebPlugin.getInstance().getMessage(RECOVER_USERNAME_SUBJECT);
		String content = WebPlugin.getInstance().getMessage(RECOVER_USERNAME_BODY,
				new Object[] { 
					user.getLogin(),
					user.getName(),
					user.getEmail(),
					SendMailService.getInstance().getServerUrl(),
					newPassword 
				});
		
		SendMailService.getInstance().send(user.getEmail(), subject, content);
		
		return true;
	}
	
	public OrganizationAdminUIDto getOrganizationFilter(String organizationName) {
		return OrganizationService.getInstance().findByNameAsAdminUIDto(organizationName);
	}
}
