package org.flowerplatform.web.tests.security.sandbox;

import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.flowerplatform.common.util.RunnableWithParam;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;
import org.flowerplatform.web.entity.Group;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.PermissionEntity;
import org.flowerplatform.web.entity.User;
import org.flowerplatform.web.security.dto.GroupAdminUIDto;
import org.flowerplatform.web.security.dto.OrganizationAdminUIDto;
import org.flowerplatform.web.security.dto.UserAdminUIDto;
import org.flowerplatform.web.security.permission.AdminSecurityEntitiesPermission;
import org.flowerplatform.web.security.permission.FlowerWebFilePermission;
import org.flowerplatform.web.security.sandbox.SecurityEntityAdaptor;
import org.flowerplatform.web.security.service.GroupService;
import org.flowerplatform.web.security.service.OrganizationService;
import org.flowerplatform.web.security.service.UserService;
import org.flowerplatform.web.temp.GeneralService;
import org.flowerplatform.web.tests.security.sandbox.helpers.Utils;

public class ServiceObserverTest {

	private static GeneralService service = new GeneralService();
	
	private static User admin;
	private static User user1;
	private static Organization org1;
	private static Group org1Admin;
	private static Group org1User;
	
	private PermissionEntity permOverUser;
	private PermissionEntity permOverUsers;
	private PermissionEntity permToUser;
	
	private PermissionEntity permOverGroup;
	private PermissionEntity permOverGroups;
	private PermissionEntity permToGroup;
	
	private PermissionEntity permOverOrg;
	private PermissionEntity permOverOrgs;
	private PermissionEntity permToOrg;
	
	@Before
	public void setUp() {
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				admin = service.createUser("admin", null, wrapper);
				user1 = service.createUser("user1", null, wrapper);
				service.createUser("user2", null, wrapper);
				org1 = service.createOrganization("org1", wrapper);
				service.createOrganization("org2", wrapper);
				org1Admin = service.createGroup("org1.admin", org1, wrapper);
				service.addUserToGroup(user1, org1Admin);
				org1User = service.createGroup("org1.user", org1, wrapper);
				
				service.createUserAndAddToGroups("anonymous.org1", null, Collections.singletonList(org1User), wrapper);
				
				service.createPermission(AdminSecurityEntitiesPermission.class, "", admin, "*", wrapper);
			}
		});
	}
	
	@After
	public void tearDown() {
		Utils.deleteAllData();
	}
	
	private void createUsersPermissions() {
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				permOverUser = service.createPermission(AdminSecurityEntitiesPermission.class, "", admin, "$user1", wrapper);
				permOverUsers = service.createPermission(AdminSecurityEntitiesPermission.class, "", admin, "$user1,$user2", wrapper);
				permToUser = service.createPermission(FlowerWebFilePermission.class, "*", user1, "read", wrapper);
			}
		});
	}
	
	private void createGroupsPermissions() {
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				permOverGroup = service.createPermission(AdminSecurityEntitiesPermission.class, "", user1, "@org1.user", wrapper);
				permOverGroups = service.createPermission(AdminSecurityEntitiesPermission.class, "", admin, "@org1.user,@ALL,@org1.admin", wrapper);
				permToGroup = service.createPermission(FlowerWebFilePermission.class, "*", org1User, "read", wrapper);
			}
		});
	}

	private void createOrganizationsPermissions() {
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				permOverOrg = service.createPermission(AdminSecurityEntitiesPermission.class, "", admin, "#org1", wrapper);
				permOverOrgs = service.createPermission(AdminSecurityEntitiesPermission.class, "", admin, "#org2,#org1", wrapper);
				permToOrg = service.createPermission(FlowerWebFilePermission.class, "*", org1, "read", wrapper);
			}
		});
	}
	
	private void assertPermissionUpdated(final PermissionEntity p, final Class<?> type, final String resource, final String assignedTo, final String actions) {
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				PermissionEntity permission = wrapper.find(PermissionEntity.class, p.getId());
				
				Assert.assertEquals("Different permission type", type.getName(), permission.getType());
				Assert.assertEquals("Different resource/name", resource, permission.getName());
				Assert.assertEquals("Different assigned entity", assignedTo, permission.getAssignedTo());
				Assert.assertEquals("Different actions", actions, permission.getActions());
			}
		});
	}
	
	private void assertPermissionDeleted(final PermissionEntity permission) {
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				Assert.assertNull("Permission was not deleted", wrapper.find(PermissionEntity.class, permission.getId()));
			}
		});
	}
	
	@Test
	public void testDeleteUser() {
		/////////////////////////////
		// Do action: create permissions and delete user
		/////////////////////////////
		createUsersPermissions();
		Utils.test(admin, new RunnableWithParam<Void, ServiceInvocationContext>() {
			
			@Override
			public Void run(ServiceInvocationContext context) {
				UserService.getInstance().delete(context, Collections.singletonList((int) user1.getId()));
				return null;
			}
		});
		
		/////////////////////////////
		// Check result: permissions updated/deleted
		/////////////////////////////
		assertPermissionDeleted(permToUser);
		assertPermissionDeleted(permOverUser);
		assertPermissionUpdated(permOverUsers, AdminSecurityEntitiesPermission.class, "", "$admin", "$user2");
	}
	
	@Test
	public void testUpdateUser() {
		/////////////////////////////
		// Do action: create permissions and update user
		/////////////////////////////
		createUsersPermissions();
		Utils.test(admin, new RunnableWithParam<Void, ServiceInvocationContext>() {
			
			@Override
			public Void run(ServiceInvocationContext context) {
				new DatabaseOperationWrapper(new DatabaseOperation() {
					
					@Override
					public void run() {
						user1 = wrapper.find(User.class, user1.getId());
						UserAdminUIDto dto = Utils.convertUserToUserAdminUIDto(user1);
						dto.setLogin("user1Modif");
						UserService.getInstance().mergeAdminUIDto(dto);
					}
				});
				
				return null;
			}
		});
		
		/////////////////////////////
		// Check result: permissions updated
		/////////////////////////////
		assertPermissionUpdated(permToUser, FlowerWebFilePermission.class, "*", "$user1Modif", "read");
		assertPermissionUpdated(permOverUser, AdminSecurityEntitiesPermission.class, "", "$admin", "$user1Modif");
		assertPermissionUpdated(permOverUsers, AdminSecurityEntitiesPermission.class, "", "$admin", "$user1Modif,$user2");
	}
	
	@Test
	public void testDeleteGroup() {
		/////////////////////////////
		// Do action: create permissions and delete group
		/////////////////////////////
		createGroupsPermissions();
		Utils.test(admin, new RunnableWithParam<Void, ServiceInvocationContext>() {

			@Override
			public Void run(ServiceInvocationContext context) {
				GroupService.getInstance().delete(Collections.singletonList((int) org1User.getId()));
				return null;
			}
		});
		
		/////////////////////////////
		// Check result: permissions updated/deleted
		/////////////////////////////
		assertPermissionDeleted(permToGroup);
		assertPermissionDeleted(permOverGroup);
		assertPermissionUpdated(permOverGroups, AdminSecurityEntitiesPermission.class, "", "$admin", "@ALL,@org1.admin");
	}
	
	@Test
	public void testUpdateGroup() {
		/////////////////////////////
		// Do action: create permissions and update group
		/////////////////////////////
		createGroupsPermissions();
		Utils.test(admin, new RunnableWithParam<Void, ServiceInvocationContext>() {

			@Override
			public Void run(final ServiceInvocationContext context) {
				new DatabaseOperationWrapper(new DatabaseOperation() {
					
					@Override
					public void run() {
						org1User = wrapper.find(Group.class, org1User.getId());
						GroupAdminUIDto dto = Utils.convertGroupToGroupAdminUIDto(org1User);
						dto.setName("org1UserModif");
						GroupService.getInstance().mergeAdminUIDto(context, dto);
					}
				});
				
				return null;
			}
		});
		
		/////////////////////////////
		// Check result: permissions updated
		/////////////////////////////
		assertPermissionUpdated(permToGroup, FlowerWebFilePermission.class, "*", "@org1UserModif", "read");
		assertPermissionUpdated(permOverGroup, AdminSecurityEntitiesPermission.class, "", "$user1", "@org1UserModif");
		assertPermissionUpdated(permOverGroups, AdminSecurityEntitiesPermission.class, "", "$admin", "@org1UserModif,@ALL,@org1.admin");
	}
	
	@Test
	public void testDeleteOrganization() {
		/////////////////////////////
		// Do action: create permissions and delete organization
		/////////////////////////////
		createOrganizationsPermissions();
		Utils.test(admin, new RunnableWithParam<Void, ServiceInvocationContext>() {

			@Override
			public Void run(ServiceInvocationContext context) {
				OrganizationService.getInstance().delete(Collections.singletonList((int) org1.getId()));
				return null;
			}
		});
		
		/////////////////////////////
		// Check result: permissions updated/deleted, anonymous user deleted
		/////////////////////////////
		assertPermissionDeleted(permToOrg);
		assertPermissionDeleted(permOverOrg);
		assertPermissionUpdated(permOverOrgs, AdminSecurityEntitiesPermission.class, "", "$admin", "#org2");

		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				List<User> list = wrapper.findByField(User.class, "login", SecurityEntityAdaptor.getAnonymousUserLogin(org1));
				Assert.assertEquals("Anonymous user was not deleted", 0, list.size());
			}
		});
	}
	
	@Test
	public void testUpdateOrganization() {
		/////////////////////////////
		// Do action: create permissions and update organization
		/////////////////////////////
		createOrganizationsPermissions();
		Utils.test(admin, new RunnableWithParam<Void, ServiceInvocationContext>() {
			
			@Override
			public Void run(final ServiceInvocationContext context) {
				new DatabaseOperationWrapper(new DatabaseOperation() {
					
					@Override
					public void run() {
						org1 = wrapper.find(Organization.class, org1.getId());
						OrganizationAdminUIDto dto = Utils.convertOrganizationToOrganizationAdminUIDto(org1);
						dto.setName("org1Modif");
						OrganizationService.getInstance().mergeAdminUIDto(context, dto);
					}
				});
				
				return null;
			}
		});
		
		/////////////////////////////
		// Check result: permissions updated, anonymous user updated
		/////////////////////////////
		assertPermissionUpdated(permToOrg, FlowerWebFilePermission.class, "*", "#org1Modif", "read");
		assertPermissionUpdated(permOverOrg, AdminSecurityEntitiesPermission.class, "", "$admin", "#org1Modif");
		assertPermissionUpdated(permOverOrgs, AdminSecurityEntitiesPermission.class, "", "$admin", "#org2,#org1Modif");
	
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				Organization org = Utils.getEntityByName(Organization.class, "org1Modif");
				List<User> list = wrapper.findByField(User.class, "login", SecurityEntityAdaptor.getAnonymousUserLogin(org));
				Assert.assertEquals("Anonymous user was not updated", 1, list.size());
			}
		});
	}
}
