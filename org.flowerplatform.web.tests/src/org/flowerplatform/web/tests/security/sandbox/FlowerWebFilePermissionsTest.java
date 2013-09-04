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
package org.flowerplatform.web.tests.security.sandbox;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.security.Principal;
import java.security.PrivilegedAction;

import javax.security.auth.Subject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.RunnableWithParam;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.IPrincipal;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.web.communication.RecordingTestWebCommunicationChannel;
import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;
import org.flowerplatform.web.entity.User;
import org.flowerplatform.web.security.dto.PermissionAdminUIDto;
import org.flowerplatform.web.security.permission.FlowerWebFilePermission;
import org.flowerplatform.web.security.permission.ModifyTreePermissionsPermission;
import org.flowerplatform.web.security.sandbox.FlowerWebPrincipal;
import org.flowerplatform.web.security.sandbox.SecurityUtils;
import org.flowerplatform.web.security.service.PermissionService;
import org.flowerplatform.web.temp.GeneralService;
import org.flowerplatform.web.tests.security.sandbox.helpers.Utils;

import flex.messaging.FlexContext;
import flex.messaging.HttpFlexSession;

/**
 * @author Mariana
 */
public class FlowerWebFilePermissionsTest {

	private static User admin;
	
	private static User testUser;
	
	private static File testFile;
	
	@Before
	public void beforeTest() throws Exception {
		final GeneralService service = new GeneralService();
		
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				admin = service.createUser("admin", null, wrapper);
				
				testUser = service.createUser("testUser", null, wrapper);
				
				service.createPermission(FlowerWebFilePermission.class, "*", admin, FlowerWebFilePermission.READ_WRITE_DELETE, wrapper);
				service.createPermission(ModifyTreePermissionsPermission.class, "*", admin, "*", wrapper);
				
			}
		});
		
		testFile = CommonPlugin.getInstance().getWorkspaceRoot();
	}
	
	@After
	public void afterTest() {
		Utils.deleteAllData();
	}

	@Test
	public void testAsAdmin() {
		assertTrue("Admin should be allowed to see all files!", test(admin));
	}
	
	@Test
	public void testAsNormalUser() {
		assertFalse("Normal user should not be allowed to see files!", test(testUser));
	}
	
	@Test
	public void testWithPermissionForAll() {
		Utils.test(admin, new RunnableWithParam<Void, ServiceInvocationContext>() {
			
			@Override
			public Void run(ServiceInvocationContext context) {
				/////////////////////////////
				// Do action: create permission for @ALL
				///////////////////////////// 
				PermissionService permissionService = (PermissionService) CommunicationPlugin.getInstance().getServiceRegistry().getService(PermissionService.SERVICE_ID);
				PermissionAdminUIDto dto = new PermissionAdminUIDto();
				dto.setType(FlowerWebFilePermission.class.getName());
				dto.setName("*");
				dto.setAssignedTo("@ALL");
				dto.setActions(FlowerWebFilePermission.READ);
				try {
					permissionService.mergeAdminUIDto(context, dto);
				} catch (Exception e) {
					fail("Could not add permission for @ALL");
				}
				
				///////////////////////////
				// Check result: permission added successfully
				/////////////////////////////
				for (PermissionAdminUIDto permission : PermissionService.getInstance().findAllAsAdminUIDto()) {
					if (permission.getAssignedTo().equals("@ALL"))
						return null;
				}
				
				fail("Could not add permission for @ALL");
				
				return null;
			}
		});
		
		assertTrue("Normal user should be allowed to see all files!", test(testUser));
	}

	private boolean test(final User user) {
		Subject subject = new Subject();
		final Principal principal = new FlowerWebPrincipal(user.getId());
		subject.getPrincipals().add(principal);
		
		return Subject.doAsPrivileged(subject, new PrivilegedAction<Boolean>() {

			@Override
			public Boolean run() {
				FlexContext.setThreadLocalSession(new HttpFlexSession());
				FlexContext.setUserPrincipal(principal);
				RecordingTestWebCommunicationChannel cc = new RecordingTestWebCommunicationChannel();
				cc.setPrincipal((FlowerWebPrincipal) principal);
				CommunicationPlugin.tlCurrentChannel.set((RecordingTestWebCommunicationChannel) cc);
				
				return SecurityUtils.hasReadPermission(testFile);
			}
		}, null);
	}
}