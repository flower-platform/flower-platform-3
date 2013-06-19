package org.flowerplatform.web.tests.security.sandbox;

import static org.flowerplatform.web.tests.security.sandbox.helpers.Utils.getEntityByName;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.flowerplatform.blazeds.TomcatLoginCommand;
import org.flowerplatform.web.entity.User;
import org.flowerplatform.web.security.dto.UserAdminUIDto;
import org.flowerplatform.web.security.service.RegisterUserService;
import org.flowerplatform.web.tests.security.sandbox.helpers.Utils;

import flex.messaging.security.SecurityException;

public class RegistrationAndActivationTest {
	
	private RegisterUserService service = new RegisterUserService();
	
	private String username = "newUser";

	@Before
	public void register() {
		UserAdminUIDto newUserDto = new UserAdminUIDto();
		newUserDto.setName(username);
		newUserDto.setLogin(username);
		newUserDto.setPassword(username);
		newUserDto.setEmail(username + "@userEmail");
		service.register(newUserDto, null);
	}
	
	@Test
	public void testRegistration() {
		///////////////////////////
		// Check result: user created and not activated
		///////////////////////////
		User newUser = getEntityByName(User.class, username);
		assertNotNull("User was not registered", newUser);
		assertFalse(newUser.isActivated());
	}
	
	@Test
	public void testLoginNotActivated() {
		/////////////////////////////
		// Do action: login as not activated, without activation code
		///////////////////////////// 
		TomcatLoginCommand cmd = new TomcatLoginCommand();
		try {
			cmd.doAuthentication(username, username);
			
			///////////////////////////
			// Check result: login failed
			///////////////////////////
			fail("User cannot login if not activated");
		} catch (SecurityException e) {
			// do nothing
		}
	}
	
	@Test
	public void testLoginAndActivateWithWrongActivationCode() {
		/////////////////////////////
		// Do action: login as not activated, with wrong activation code
		///////////////////////////// 
		TomcatLoginCommand cmd = new TomcatLoginCommand();
		try {
			cmd.doAuthentication(username + "|wrongActivationCode", username);
			
			///////////////////////////
			// Check result: login failed
			///////////////////////////
			fail("Wrong activation code");
		} catch (SecurityException e) {
			// do nothing
		}
	}
	
	@Test
	public void testLoginAndActivateWithCorrectActivationCode() {
		/////////////////////////////
		// Do action: login and activate
		///////////////////////////// 
		TomcatLoginCommand cmd = new TomcatLoginCommand();
		String activationCode = getEntityByName(User.class, username).getActivationCode();
		try {
			cmd.doAuthentication(username, username + "," + activationCode);
		} catch (SecurityException e) {
			///////////////////////////
			// Check result: activation + login successful
			///////////////////////////
			fail("User was not activated");
		}
	}
	
	@After
	public void afterTest() {
		Utils.deleteAllData();
	}
}
