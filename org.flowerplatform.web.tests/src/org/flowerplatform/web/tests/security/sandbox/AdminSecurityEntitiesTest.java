package org.flowerplatform.web.tests.security.sandbox;

import static org.flowerplatform.web.tests.security.sandbox.helpers.Utils.getEntityByName;
import static org.flowerplatform.web.tests.security.sandbox.helpers.Utils.test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.flowerplatform.common.util.RunnableWithParam;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;
import org.flowerplatform.web.entity.EntityFactory;
import org.flowerplatform.web.entity.Group;
import org.flowerplatform.web.entity.GroupUser;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.OrganizationMembershipStatus;
import org.flowerplatform.web.entity.OrganizationUser;
import org.flowerplatform.web.entity.PermissionEntity;
import org.flowerplatform.web.entity.User;
import org.flowerplatform.web.security.dto.GroupAdminUIDto;
import org.flowerplatform.web.security.dto.OrganizationAdminUIDto;
import org.flowerplatform.web.security.dto.OrganizationUserAdminUIDto;
import org.flowerplatform.web.security.dto.UserAdminUIDto;
import org.flowerplatform.web.security.permission.AdminSecurityEntitiesPermission;
import org.flowerplatform.web.security.permission.FlowerWebFilePermission;
import org.flowerplatform.web.security.permission.ModifyTreePermissionsPermission;
import org.flowerplatform.web.security.sandbox.SecurityEntityAdaptor;
import org.flowerplatform.web.security.service.GroupService;
import org.flowerplatform.web.security.service.OrganizationService;
import org.flowerplatform.web.security.service.UserService;
import org.flowerplatform.web.temp.GeneralService;
import org.flowerplatform.web.tests.security.sandbox.helpers.Utils;

/**
 * @author Florin
 * @author Mariana
 */
public class AdminSecurityEntitiesTest {

	private static UserService userService;
	
	private static GroupService groupService;
	
	private static OrganizationService organizationService;
	
	private static User admin;
	
	private static Organization org1;

	private static Organization org2;

	private static Organization org3;
	
	private static User user1;
	
	private static User user2;
	
	private static User anonymous;
		
	@Before
	public void setup() {
		userService = (UserService) CommunicationPlugin.getInstance().getServiceRegistry().getService(UserService.SERVICE_ID);
		groupService = (GroupService) CommunicationPlugin.getInstance().getServiceRegistry().getService(GroupService.SERVICE_ID);
		organizationService = (OrganizationService) CommunicationPlugin.getInstance().getServiceRegistry().getService(OrganizationService.SERVICE_ID);
		
		final GeneralService service = new GeneralService();
		
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				admin = service.createUser("admin", null, wrapper);
				user1 = service.createUser("user1", null, wrapper);
				user2 = service.createUser("user2", null, wrapper);
				
				org1 = service.createOrganization("org1", wrapper);
				Group org1AdminGroup = service.createGroup("org1.admin", org1, wrapper);
				Group org1User = service.createGroup("org1.user", org1, wrapper);
				
				org2 = service.createOrganization("org2", wrapper);
				Group org2AdminGroup = service.createGroup("org2.admin", org2, wrapper);
				service.createGroup("org2.user", org2, wrapper);
				
				org3 = service.createOrganization("org3", wrapper);
				service.createGroup("org3.admin", org3, wrapper);
				service.createGroup("org3.user", org3, wrapper);
				org3.setActivated(false);
				wrapper.merge(org3);
				
				service.createGroup("noOrgGroup", null, wrapper);
				
				service.addUserToGroup(user1, org1AdminGroup);
				service.addUserToGroup(user1, org2AdminGroup);
				service.addUserToGroup(user2, org1User);
				
				anonymous = service.createUser(SecurityEntityAdaptor.ANONYMOUS, null, wrapper);
				
				service.createPermission(AdminSecurityEntitiesPermission.class, "", org1AdminGroup, "#org1", wrapper);
				service.createPermission(AdminSecurityEntitiesPermission.class, "", org2AdminGroup, "#org2", wrapper);
				service.createPermission(AdminSecurityEntitiesPermission.class, "", admin, PermissionEntity.ANY_ENTITY, wrapper);
				
				service.createPermission(FlowerWebFilePermission.class, "*", admin, FlowerWebFilePermission.READ_WRITE_DELETE, wrapper);
				service.createPermission(ModifyTreePermissionsPermission.class, "*", admin, "*", wrapper);
				
				admin = wrapper.find(User.class, admin.getId());
				user1 = wrapper.find(User.class, user1.getId());
				user2 = wrapper.find(User.class, user2.getId());
				org1 = wrapper.find(Organization.class, org1.getId());
				org2 = wrapper.find(Organization.class, org2.getId());
				org3 = wrapper.find(Organization.class, org3.getId());
			}
		});
		
	}
		
	@After
	public void tearDown() {
		Utils.deleteAllData();
	}
	
	private void testOrganizations(
			final User user, 
			final int numberOfOrganizationsVisibleInEditMode, 
			final int numberOfOrganizationsVisibleInRequestMode,
			final Organization organizationWhereAdmin,
			final Organization organizationWhereNotAdmin) {
		
		test(user, new RunnableWithParam<Void, ServiceInvocationContext>() {

			@Override
			public Void run(ServiceInvocationContext context) {
				///////////////////////////
				// Check result: visible organizations
				/////////////////////////////
				assertEquals("Number of organizations visible in edit mode", numberOfOrganizationsVisibleInEditMode, organizationService.findAllAsAdminUIDto(context, false).size());
				assertEquals("Number of organizations visible in request mode", numberOfOrganizationsVisibleInRequestMode, organizationService.findAllAsAdminUIDto(context, true).size());
				
				/////////////////////////////
				// Do action: add new organization
				///////////////////////////// 
				OrganizationAdminUIDto newOrgDto = new OrganizationAdminUIDto();
				String newOrgName = "newOrg" + user.getLogin();
				newOrgDto.setName(newOrgName);
				try {
					String errorMessage;
					errorMessage = organizationService.mergeAdminUIDto(context, newOrgDto);
					
					/////////////////////////////
					// Check result: add is only allowed for FDC admin
					/////////////////////////////
					if (user.isAdmin()) {
						assertNull("Add new organization failed with " + errorMessage, errorMessage);
						Organization newOrg = getEntityByName(Organization.class, newOrgName);
						assertNotNull("Organization was not created", newOrg);
					} else {
						fail("Add new organization for normal user is not allowed");
					}
				} catch (Exception e) {
					if (user.isAdmin())
						fail("Add new organization failed with " + e.getMessage());
				}
				

				if (organizationWhereAdmin != null) {
					/////////////////////////////
					// Do action: edit organization where user is admin
					///////////////////////////// 
					try {
						OrganizationAdminUIDto orgDto = Utils.convertOrganizationToOrganizationAdminUIDto(organizationWhereAdmin);
						String newUrl = organizationWhereAdmin.getURL() + "Modif";
						orgDto.setURL(newUrl);
						String errorMessage = organizationService.mergeAdminUIDto(context, orgDto);
						
						/////////////////////////////
						// Check result: edit successful
						/////////////////////////////
						assertNull("Edit organization where user is admin failed with " + errorMessage,	errorMessage);
						assertEquals("Edit was not commited in DB", getEntityByName(Organization.class, organizationWhereAdmin.getName()).getURL(), newUrl);
					} catch (Exception e) {
						fail("Edit organization where user is admin failed with " + e.getMessage());
					}
					
					/////////////////////////////
					// Do action: delete organization where user is admin
					///////////////////////////// 
					try {
						organizationService.delete(Collections.singletonList((int) organizationWhereAdmin.getId()));
						assertNull("Organization was not removed from DB", getEntityByName(Organization.class, organizationWhereAdmin.getName()));
					} catch (Exception e) {
						/////////////////////////////
						// Check result: delete successful
						/////////////////////////////
						fail("Delete organization where user is admin failed with " + e.getMessage());
					}
				}
				
				if (organizationWhereNotAdmin != null) {
					/////////////////////////////
					// Do action: edit organization where user is not admin
					///////////////////////////// 
					try {
						OrganizationAdminUIDto orgDto = Utils.convertOrganizationToOrganizationAdminUIDto(organizationWhereNotAdmin);
						String newUrl = organizationWhereNotAdmin.getURL() + "Modif";
						orgDto.setURL(newUrl);
						String errorMessage = organizationService.mergeAdminUIDto(context, orgDto);
						
						/////////////////////////////
						// Check result: edit is only allowed for FDC admin
						/////////////////////////////
						if (!user.isAdmin()) {
							assertNull("Edit organization where user is not admin failed with " + errorMessage,	errorMessage);
							fail("Edit organization where user is not admin is not allowed");
							assertEquals("Edit was not commited in DB", getEntityByName(Organization.class, organizationWhereNotAdmin.getName()).getURL(), newUrl);
						}
					} catch (Exception e) {
						if (user.isAdmin()) {
							fail("Edit organization failed for FDC admin with " + e.getMessage());
						}
					}
				
					/////////////////////////////
					// Do action: delete organization where user is not admin
					///////////////////////////// 
					try {
						organizationService.delete(Collections.singletonList((int) organizationWhereNotAdmin.getId()));
				
						/////////////////////////////
						// Check result: delete is only allowed for FDC admin
						/////////////////////////////
						if (!user.isAdmin()) {
							fail("Delete organization where user is not admin is not allowed");
						} else {
							assertNull("Organization was not removed from DB", getEntityByName(Organization.class, organizationWhereNotAdmin.getName()));
						}
					} catch (Exception e) {
						if (user.isAdmin()) {
							fail("Delete organization failed for FDC admin with " + e.getMessage());
						}
					}
				}
				
				return null;
			}
		});
	}
	
	/**
	 * Visible organizations: none. Cannot add/edit/delete.
	 */
	@Test
	public void testOrganizationsAsNormalUser() {
		testOrganizations(user2, 0, 1, null, org1);
	}
	
	/**
	 * Visible organizations: only those where user is admin. Cannot add. Can edit/delete only
	 * organizations where user is admin.
	 */
	@Test
	public void testOrganizationsAsOrganizationAdmin() {
		testOrganizations(user1, 2, 0, org2, org3);	
	}
	
	/**
	 * Visible organizations: all. Can add/edit/delete.
	 */
	@Test
	public void testOrganizationsAsFdcAdmin() {
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				testOrganizations(admin, wrapper.findAll(Organization.class).size(), 2, null, org3);
			}
		});
	}
	
	private void testGroups(
			final User user,
			final int groupsVisible,
			final Organization organizationWhereAdmin,
			final Organization organizationWhereNotAdmin) {
		test(user, new RunnableWithParam<Void, ServiceInvocationContext>() {

			@Override
			public Void run(ServiceInvocationContext context) {
				///////////////////////////
				// Check result: visible groups
				/////////////////////////////
				assertEquals("Number of visible groups", groupsVisible, groupService.findAllAsAdminUIDto().size());
				
				/////////////////////////////
				// Do action: add new group with no organization
				///////////////////////////// 
				GroupAdminUIDto newGroupDto = new GroupAdminUIDto();
				newGroupDto.setName("newGroup" + user.getName());
				try {
					groupService.mergeAdminUIDto(context, newGroupDto);
					
					///////////////////////////
					// Check result: add is allowed only for FDC admin
					/////////////////////////////
					if (!user.isAdmin()) {
						fail("New group was created for user " + user.getName());
					} else {
						assertNotNull("Group was not added to DB", getEntityByName(Group.class, newGroupDto.getName()));
					}
				} catch (Exception e) {
					if (user.isAdmin()) {
						fail("New group failed with " + e.getMessage());
					}
				}
				
				/////////////////////////////
				// Do action: add new group with organization where user is admin
				///////////////////////////// 
				if (organizationWhereAdmin != null) {
					try {
						newGroupDto = new GroupAdminUIDto();
						newGroupDto.setName(organizationWhereAdmin.getName() + user.getName());
						newGroupDto.setOrganization(Utils.convertOrganizationToOrganizationAdminUIDto(organizationWhereAdmin));
						String errorMessage = groupService.mergeAdminUIDto(context, newGroupDto);
						
						///////////////////////////
						// Check result: add is allowed only for organization admin
						/////////////////////////////
						assertNull("Add new group in organization where user is admin failed with " + errorMessage, errorMessage);
						assertNotNull("Group was not added to", getEntityByName(Group.class, newGroupDto.getName()));
					} catch (Exception e) {
						fail("New group failed in organization where user is admin " + e.getMessage());
					}
				}
				
				/////////////////////////////
				// Do action: add new group with organization where user is not admin
				///////////////////////////// 
				if (organizationWhereNotAdmin != null) {
					try {
						newGroupDto = new GroupAdminUIDto();
						newGroupDto.setName(organizationWhereNotAdmin.getName() + user.getName());
						newGroupDto.setOrganization(Utils.convertOrganizationToOrganizationAdminUIDto(organizationWhereNotAdmin));
						String errorMessage = groupService.mergeAdminUIDto(context, newGroupDto);
						
						///////////////////////////
						// Check result: add is allowed only for FDC admin
						/////////////////////////////
						if (user.isAdmin()) {
							assertNull("Add new group in organization failed with " + errorMessage, errorMessage);
							assertNotNull("Group was not added to", getEntityByName(Group.class, newGroupDto.getName()));
						} else {
							fail("New group was created for user " + user.getName());
						}
					} catch (Exception e) {
						if (user.isAdmin()) {
							fail("Add new group failed with " + e.getMessage());
						}
					}
				}
				
				if (organizationWhereAdmin != null) {
					/////////////////////////////
					// Do action: edit group from organization where user is admin to organization where user is not admin
					///////////////////////////// 
					try {
						Group group = getEntityByName(Group.class, organizationWhereAdmin.getName() + ".user");
						GroupAdminUIDto groupDto = Utils.convertGroupToGroupAdminUIDto(group);
						groupDto.setOrganization(organizationWhereNotAdmin == null ? null : Utils.convertOrganizationToOrganizationAdminUIDto(organizationWhereNotAdmin));
						String errorMessage = groupService.mergeAdminUIDto(context, groupDto);
						
						///////////////////////////
						// Check result: edit is allowed only for FDC admin
						/////////////////////////////
						if (!user.isAdmin()) {
							assertNull("Edit group failed with " + errorMessage, errorMessage);
						} else {
							fail("Edit group to organization where user is not admin is not allowed");
						}
					} catch (Exception e) {
						if (user.isAdmin()) {
							fail("Edit group failed with " + e.getMessage());
						}
					}
					
					/////////////////////////////
					// Do action: delete group from organization where user is admin
					///////////////////////////// 
					try {
						Group group = getEntityByName(Group.class, organizationWhereAdmin.getName() + ".user");
						groupService.delete(Collections.singletonList((int) group.getId()));
						
						///////////////////////////
						// Check result: delete successful
						/////////////////////////////
						assertNull("Groups was not removed from DB", getEntityByName(Group.class, group.getName()));
					} catch (Exception e) {
						fail("Delete group failed with " + e.getMessage());
					}
				}
				
				/////////////////////////////
				// Do action: edit group from organization where user is not admin to organization where user is admin
				///////////////////////////// 
				try {
					Group group = getEntityByName(Group.class, organizationWhereNotAdmin == null ? "noOrgGroup" : organizationWhereNotAdmin.getName() + ".user");
					GroupAdminUIDto groupDto = Utils.convertGroupToGroupAdminUIDto(group);
					groupDto.setOrganization(organizationWhereAdmin == null ? null : Utils.convertOrganizationToOrganizationAdminUIDto(organizationWhereAdmin));
					String errorMessage = groupService.mergeAdminUIDto(context, groupDto);
					
					///////////////////////////
					// Check result: edit is allowed only for FDC admin
					/////////////////////////////
					if (user.isAdmin()) {
						assertNull("Edit group failed with " + errorMessage, errorMessage);
					} else {
						fail("Edit group to organization where user is not admin is not allowed");
					}
				} catch (Exception e) {
					if (user.isAdmin()) {
						fail("Edit group failed with " + e.getMessage());
					}
				}
				
				/////////////////////////////
				// Do action: delete group from organization where user is not admin
				///////////////////////////// 
				try {
					Group group = getEntityByName(Group.class, organizationWhereNotAdmin == null ? "noOrgGroup" : organizationWhereNotAdmin.getName() + ".user");
					groupService.delete(Collections.singletonList((int) group.getId()));
					
					///////////////////////////
					// Check result: delete is allowed only for FDC admin
					/////////////////////////////
					if (user.isAdmin()) {
						assertNull("Groups was not removed from DB", getEntityByName(Group.class, group.getName()));
					} else {
						fail("Delete is not allowed for " + user.getName());
					}
				} catch (Exception e) {
					if (user.isAdmin()) {
						fail("Delete group failed with " + e.getMessage());
					}
				}
				return null;
			}
		});
	}
	
	/**
	 * Visible groups: none. Cannot add/edit/delete.
	 */
	@Test
	public void testGroupsAsNormalUser() {
		testGroups(user2, 0, null, org1);
	}
	
	/**
	 * Visible groups: only groups belonging to organization where user is admin. 
	 * Can add/edit/delete groups only belonging to organizations where user is admin.
	 */
	@Test
	public void testGroupsAsOrganizationAdmin() {
		testGroups(user1, 4, org1, org3);
	}
	
	/**
	 * Visible groups: all. Can add/edit/delete.
	 */
	@Test
	public void testGroupsAsAdmin() {
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				testGroups(admin, wrapper.findAll(Group.class).size(), null, org2);
			}
		});
	}
	
	private void testUsers(
			final User user,
			final int numberOfVisibleUsers,
			final Organization organizationWhereAdmin,
			final User userInOrganizationWhereAdmin,
			final User userInOrganizationWhereNotAdmin
			) {
		test(user, new RunnableWithParam<Void, ServiceInvocationContext>() {

			@Override
			public Void run(ServiceInvocationContext context) {
				///////////////////////////
				// Check result: visible users
				/////////////////////////////
				assertEquals(numberOfVisibleUsers, userService.findAllAsAdminUIDto(context, null).size());
				
				/////////////////////////////
				// Do action: add new user in no organization
				/////////////////////////////
				try {
					UserAdminUIDto newUserDto = new UserAdminUIDto();
					newUserDto.setLogin("newUserNoOrg" + user.getName());
					newUserDto.setEmail("newUser@userEmail");
					newUserDto.setName("newUser");
					newUserDto.setPassword("newUser");
					String errorMessage = userService.mergeAdminUIDto(newUserDto);
					
					///////////////////////////
					// Check result: add is only allowed for FDC admin
					/////////////////////////////
					if (user.isAdmin()) {
						assertNull("Add new user failed with " + errorMessage, errorMessage);
						assertNotNull("User was not added to DB", getEntityByName(User.class, newUserDto.getName()));
					} else {
						fail("User created for user " + user.getName());
					}
				} catch (Exception e) {
					if (user.isAdmin()) {
						fail("New user failed with " + e.getMessage());
					}
				}
				
				/////////////////////////////
				// Do action: add new user in organization where user is admin
				/////////////////////////////
				if (organizationWhereAdmin != null) {
					try {
						UserAdminUIDto newUserDto = new UserAdminUIDto();
						newUserDto.setLogin("newUser" + user.getName());
						newUserDto.setEmail("newUser@userEmail");
						newUserDto.setName("newUser");
						newUserDto.setPassword("newUser");
						OrganizationAdminUIDto orgDto = Utils.convertOrganizationToOrganizationAdminUIDto(organizationWhereAdmin);
						OrganizationUserAdminUIDto ouDto = new OrganizationUserAdminUIDto();
						ouDto.setOrganization(orgDto);
						ouDto.setStatus(OrganizationMembershipStatus.MEMBER);
						newUserDto.setOrganizationUsers(Collections.singletonList(ouDto));
						String errorMessage = userService.mergeAdminUIDto(newUserDto);
						
						///////////////////////////
						// Check result: add successful
						/////////////////////////////
						assertNull("Add new user failed with " + errorMessage, errorMessage);
						assertNotNull("User was not added to DB", getEntityByName(User.class, newUserDto.getName()));
					} catch (Exception e) {
						fail("New user failed with " + e.getMessage());
					}
				}
				
				if (userInOrganizationWhereAdmin != null) {
					/////////////////////////////
					// Do action: edit user in organization where user is admin
					/////////////////////////////
					try {
						UserAdminUIDto userDto = Utils.convertUserToUserAdminUIDto(userInOrganizationWhereAdmin);
						String newEmail = userDto.getEmail() + "Modif";
						userDto.setEmail(newEmail);
						String errorMessage = userService.mergeAdminUIDto(userDto);
						
						///////////////////////////
						// Check result: edit successful
						/////////////////////////////
						assertNull("Edit user failed with " + errorMessage, errorMessage);
						assertEquals("Edit not commited in DB", getEntityByName(User.class, userDto.getName()).getEmail(), newEmail);
					} catch (Exception e) {
						fail("Edit user failed with " + e.getMessage());
					}
					
					/////////////////////////////
					// Do action: delete user in organization where user is admin
					/////////////////////////////
					try {
						userService.delete(context, Collections.singletonList((int) userInOrganizationWhereAdmin.getId()));
						
						///////////////////////////
						// Check result: delete is allowed only for FDC admin
						/////////////////////////////
						if (user.isAdmin()) {
							assertNull(getEntityByName(User.class, userInOrganizationWhereAdmin.getName()));
						} else {
							fail("Delete user is not allowed for normal user");
						}
					} catch (Exception e) {
						if (user.isAdmin()) {
							fail("Delete user failed with " + e.getMessage());
						}
					}
				}
				
				if (userInOrganizationWhereNotAdmin != null) {
					/////////////////////////////
					// Do action: edit user in organization where user is not admin
					/////////////////////////////
					try {
						UserAdminUIDto userDto = Utils.convertUserToUserAdminUIDto(userInOrganizationWhereNotAdmin);
						String newEmail = userDto.getEmail() + "Modif";
						userDto.setEmail(newEmail);
						String errorMessage = userService.mergeAdminUIDto(userDto);
						
						///////////////////////////
						// Check result: edit is allowed only for FDC admin
						/////////////////////////////
						if (user.isAdmin()) {
							assertNull("Edit user failed with " + errorMessage, errorMessage);
							assertEquals("Edit not commited in DB", getEntityByName(User.class, userDto.getName()).getEmail(), newEmail);
						} else {
							fail("Edit user in organization where user is not admin");
						}
					} catch (Exception e) {
						if (user.isAdmin()) {
							fail("Edit user failed with " + e.getMessage());
						}
					}
					
					/////////////////////////////
					// Do action: delete user in organization where user is not admin
					/////////////////////////////
					try {
						userService.delete(context, Collections.singletonList((int) userInOrganizationWhereNotAdmin.getId()));
						
						///////////////////////////
						// Check result: delete is allowed only for FDC admin
						/////////////////////////////
						if (user.isAdmin()) {
							assertNull(getEntityByName(User.class, userInOrganizationWhereNotAdmin.getName()));
						} else {
							fail("Delete user is not allowed for normal user");
						}
					} catch (Exception e) {
						if (user.isAdmin()) {
							fail("Delete user failed with " + e.getMessage());
						}
					}
				}
				
				return null;
			}
		});
	}
	
	@Test
	public void testUsersAsNormalUser() {
		testUsers(user2, 1, null, null, user1);
	}
	
	@Test
	public void testUsersAsOrganizationAdmin() {
		testUsers(user1, 2, org1, user2, admin);
	}
	
	@Test
	public void testUsersAsAdmin() {
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				testUsers(admin, wrapper.findAll(User.class).size(), org1, null, user1);
			}
		});
	}
	
	private boolean userBelongsToGroup(User user, Group group) {
		for (Iterator<GroupUser> it = group.getGroupUsers().iterator(); it.hasNext(); ) {
			if (it.next().getUser().getId() == user.getId()) {
				return true;
			}
		}
		return false;
	}
	
	private OrganizationUser getOrganizationUser(final User u, final Organization o) {
		DatabaseOperationWrapper wrapper = new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				User user = wrapper.find(User.class, u.getId());
				for (Iterator<OrganizationUser> it = user.getOrganizationUsers().iterator(); it.hasNext(); ) {
					OrganizationUser ou = it.next();
					if (ou.getOrganization().getId() == o.getId()) {
						wrapper.setOperationResult(ou);
					}
				}
			}
		});
		return (OrganizationUser) wrapper.getOperationResult();
	}
	
	private void requestNewOrganization() {
		test(user2, new RunnableWithParam<Void, ServiceInvocationContext>() {

			@Override
			public Void run(ServiceInvocationContext context) {
				/////////////////////////////
				// Do action: request new organization
				///////////////////////////// 
				OrganizationAdminUIDto orgDto = new OrganizationAdminUIDto();
				orgDto.setName("newOrgRequested");
				userService.requestNewOrganization(context, orgDto, "Comment for Admin");
				
				new DatabaseOperationWrapper(new DatabaseOperation() {
					
					@Override
					public void run() {
						/////////////////////////////
					// Check result: organization created, user added as admin
					///////////////////////////// 
					Organization org = getEntityByName(Organization.class, "newOrgRequested", wrapper);
					user2 = getEntityByName(User.class, user2.getName());
					assertNotNull("Organization was not created", org);
					assertEquals(OrganizationMembershipStatus.ADMIN, organizationService.getOrganizationMembershipStatus(org, user2));
					}
				});
				
				return null;
			}
		});
	}
	
	private void testApproveDenyNewOrganizationRequest(final boolean approve) {
		test(admin, new RunnableWithParam<Void, ServiceInvocationContext>() {

			@Override
			public Void run(ServiceInvocationContext context) {
				/////////////////////////////
				// Do action: approve/deny new organization request
				///////////////////////////// 
				Organization org = getEntityByName(Organization.class, "newOrgRequested");
				OrganizationAdminUIDto orgDto = Utils.convertOrganizationToOrganizationAdminUIDto(org);
				userService.approveDenyNewOrganization(context, orgDto, approve, null);
				
				new DatabaseOperationWrapper(new DatabaseOperation() {
					
					@Override
					public void run() {
						if (approve) {
							/////////////////////////////
							// Check result: organization activated, groups created, anonymous user created, user added to groups 
							///////////////////////////// 
							Organization org = getEntityByName(Organization.class, "newOrgRequested", wrapper);
							
							assertTrue("Organization was not activated", org.isActivated());
							assertNotNull("User group was not created", getEntityByName(Group.class, "newOrgRequested.user", wrapper));
							assertNotNull("Admin group was not created", getEntityByName(Group.class, "newOrgRequested.admin", wrapper));
							Group adminGroup = getEntityByName(Group.class, "newOrgRequested.admin", wrapper);
							assertTrue("Organization owner was not added to admin group", userBelongsToGroup(user2, adminGroup));
//							IFolder dir = ResourcesPlugin.getWorkspace().getRoot().getFolder(new Path("root/newOrgRequested"));
//							assertTrue("Directory not created", dir.exists());
							
							
							List<PermissionEntity> permissions = wrapper.findByField(PermissionEntity.class, "assignedTo", "@newOrgRequested.admin");
							assertEquals("Permissions not created", 5, permissions.size());
							
							User anonymousUser = getEntityByName(User.class, SecurityEntityAdaptor.getAnonymousUserLogin(org), wrapper);
							assertNotNull("Anonymous user was not created", anonymousUser);
						} else {
							/////////////////////////////
							// Check result: organization removed
							///////////////////////////// 
							assertNull("Organization was not deleted", getEntityByName(Organization.class, "newOrgRequested"));
						}
					}
				});
				
				return null;
			}
			
		});
	}
	
	@Test
	public void testRequestNewOrganizationAndApprove() {
		requestNewOrganization();
		testApproveDenyNewOrganizationRequest(true);
	}
	
	@Test
	public void testRequestNewOrganizationAndDeny() {
		requestNewOrganization();
		testApproveDenyNewOrganizationRequest(false);
	}
	
	private void requestMembership() {
		test(user2, new RunnableWithParam<Void, ServiceInvocationContext>() {

			@Override
			public Void run(ServiceInvocationContext context) {
				/////////////////////////////
				// Do action: request membership
				///////////////////////////// 
				userService.requestMembership(context, user2.getId(), "org2", "Comment for Admin");
				new DatabaseOperationWrapper(new DatabaseOperation() {
					
					@Override
					public void run() {
						user2 = wrapper.find(User.class, user2.getId());
						org2 = wrapper.find(Organization.class, org2.getId());
						////////////////////////////
						// Check result: user added to org with PENDING status
						///////////////////////////// 
						assertEquals("User was not added with PENDING status", OrganizationMembershipStatus.PENDING_MEMBERSHIP_APPROVAL, organizationService.getOrganizationMembershipStatus(org2, user2));
					}
				});
				
				return null;
			}
		});
	}
	
	private void testApproveDenyMembershipRequest(final boolean approve) {
		test(user1, new RunnableWithParam<Void, ServiceInvocationContext>() {

			@Override
			public Void run(ServiceInvocationContext context) {
				/////////////////////////////
				// Do action: approve/deny membership
				///////////////////////////// 
				OrganizationUser organizationUser = getOrganizationUser(user2, org2);
				userService.approveDenyMembership(context, organizationUser.getId(), approve, null);
				
				if (approve) {
					new DatabaseOperationWrapper(new DatabaseOperation() {
						
						@Override
						public void run() {
							/////////////////////////////
							// Check result: user added to org with MEMBER status, user added to user group
							///////////////////////////// 
							user2 = getEntityByName(User.class, user2.getName(), wrapper);
							org2 = getEntityByName(Organization.class, org2.getName(), wrapper);
							assertEquals("User was not added with MEMBER status", OrganizationMembershipStatus.MEMBER, organizationService.getOrganizationMembershipStatus(org2, user2));
							assertTrue("User was not added to user group", userBelongsToGroup(user2, getEntityByName(Group.class, "org2.user", wrapper)));
						}
					});
					
				} else {
					new DatabaseOperationWrapper(new DatabaseOperation() {
						
						@Override
						public void run() {
							/////////////////////////////
							// Check result: user removed from organization
							///////////////////////////// 
							user2 = getEntityByName(User.class, user2.getName(), wrapper);
							org2 = getEntityByName(Organization.class, org2.getName(), wrapper);
							assertNull("User was not removed from organization", organizationService.getOrganizationMembershipStatus(org2, user2));
						}
					});
				}
				
				return null;
			}
		});
	}
	
	@Test
	public void testRequestMembershipAndApprove() {
		requestMembership();
		testApproveDenyMembershipRequest(true);
	}
	
	@Test
	public void testRequestMembershipAndDeny() {
		requestMembership();
		testApproveDenyMembershipRequest(false);
	}
	
	@Test
	public void updgradeDowngradeUser() {
		test(user1, new RunnableWithParam<Void, ServiceInvocationContext>() {

			@Override
			public Void run(ServiceInvocationContext param) {
				/////////////////////////////
				// Do action: upgrade user to admin
				///////////////////////////// 
				OrganizationUser organizationUser = getOrganizationUser(user2, org1);
				userService.upgradeDowngradeUser(Collections.singletonList((int) organizationUser.getId()), true);
				
				new DatabaseOperationWrapper(new DatabaseOperation() {
					
					@Override
					public void run() {
						/////////////////////////////
						// Check result: user removed from user group and added to admin group
						///////////////////////////// 
						assertFalse("User was not removed from user group", userBelongsToGroup(user2, getEntityByName(Group.class, "org1.user", wrapper)));
						assertTrue("User was not added to admin group", userBelongsToGroup(user2, getEntityByName(Group.class, "org1.admin", wrapper)));
					}
				});
				
				/////////////////////////////
				// Do action: downgrade user to member
				///////////////////////////// 
				userService.upgradeDowngradeUser(Collections.singletonList((int) organizationUser.getId()), false);
				
				new DatabaseOperationWrapper(new DatabaseOperation() {
					
					@Override
					public void run() {
						/////////////////////////////
						// Check result: user removed from admin group and added to user group
						///////////////////////////// 
						assertFalse("User was not removed from admin group", userBelongsToGroup(user2, getEntityByName(Group.class, "org1.admin", wrapper)));
						assertTrue("User was not added to user group", userBelongsToGroup(user2, getEntityByName(Group.class, "org1.user", wrapper)));
					}
				});
	
				return null;
			}
		});
	}
	
	@Test
	public void testLeaveOrganizations() {
		test(user1, new RunnableWithParam<Void, ServiceInvocationContext>() {

			@Override
			public Void run(ServiceInvocationContext context) {
				/////////////////////////////
				// Do action: leave organizations
				///////////////////////////// 
				userService.leaveOrganizations(context, Utils.convertUserToUserAdminUIDto(user1), Arrays.asList(
						Utils.convertOrganizationToOrganizationAdminUIDto(org1),
						Utils.convertOrganizationToOrganizationAdminUIDto(org2)), null);
				
				new DatabaseOperationWrapper(new DatabaseOperation() {
					
					@Override
					public void run() {
						/////////////////////////////
						// Check result: user removed from organizations and groups
						///////////////////////////// 
						user1 = getEntityByName(User.class, "user1", wrapper);
						org1 = getEntityByName(Organization.class, org1.getName(), wrapper);
						org2 = getEntityByName(Organization.class, org2.getName(), wrapper);
						assertNull("User was not removed from organization", organizationService.getOrganizationMembershipStatus(org1, user1));
						assertNull("User was not removed from organization", organizationService.getOrganizationMembershipStatus(org2, user1));
						assertFalse("User was not removed from group", userBelongsToGroup(user1, getEntityByName(Group.class, "org1.admin", wrapper)));
						assertFalse("User was not removed from group", userBelongsToGroup(user1, getEntityByName(Group.class, "org1.user", wrapper)));
						assertFalse("User was not removed from group", userBelongsToGroup(user1, getEntityByName(Group.class, "org2.admin", wrapper)));
						assertFalse("User was not removed from group", userBelongsToGroup(user1, getEntityByName(Group.class, "org2.user", wrapper)));
					}
				});
				
				return null;
			}
		
		});
	}
	
	@Test
	public void testCreateAndApproveAsOrganizationMember() {
		test(user1, new RunnableWithParam<Void, ServiceInvocationContext>() {

			@Override
			public Void run(ServiceInvocationContext context) {
				/////////////////////////////
				// Do action: create user and approve
				///////////////////////////// 
				UserAdminUIDto userDto = new UserAdminUIDto();
				userDto.setLogin("newUser");
				userDto.setName("newUser");
				userDto.setEmail("newUser@userEmail");
				userDto.setPassword("newUserPass");
				OrganizationUserAdminUIDto ouDto = new OrganizationUserAdminUIDto();
				ouDto.setOrganization(Utils.convertOrganizationToOrganizationAdminUIDto(org1));
				ouDto.setStatus(OrganizationMembershipStatus.MEMBER);
				userDto.setOrganizationUsers(Collections.singleton(ouDto));
				userService.createAndApproveAsOrganizationMember(context, userDto);
				
				new DatabaseOperationWrapper(new DatabaseOperation() {
					
					@Override
					public void run() {
						/////////////////////////////
						// Check result: user created and added as member of organization and added to user group
						///////////////////////////// 
						User newUser = getEntityByName(User.class, "newUser", wrapper);
						org1 = getEntityByName(Organization.class, org1.getName(), wrapper);
						assertNotNull("User was not created", newUser);
						assertEquals("User was not added to organization as member", OrganizationMembershipStatus.MEMBER, organizationService.getOrganizationMembershipStatus(org1, newUser));
						assertTrue("User was not added to user group", userBelongsToGroup(newUser, getEntityByName(Group.class, "org1.user", wrapper)));
					}
				});
				
				return null;
			}
			
		});
	}
	
	private void testAnonymousUser(final User user) {
		test(user, new RunnableWithParam<Void, ServiceInvocationContext>() {

			@Override
			public Void run(final ServiceInvocationContext context) {
				/////////////////////////////
				// Do action: add anonymous user
				///////////////////////////// 
				UserAdminUIDto userDto = new UserAdminUIDto();
				String name = SecurityEntityAdaptor.ANONYMOUS + ".org1";
				userDto.setLogin(name);
				userDto.setName(name);
				String errorMessage = userService.mergeAdminUIDto(userDto);
				
				/////////////////////////////
				// Check result: add anonymous user allowed/not allowed
				///////////////////////////// 
				if (user.isAdmin()) {
					assertNull("Anonymous user not added for " + user.getName(), errorMessage);
					assertNotNull("Anonymous user not added for " + user.getName(), getEntityByName(User.class, name));
				} else {
					assertEquals("Anonymous user added for " + user.getName(), "You cannot create an anonymous user!", errorMessage);
					assertNull("Anonymous user added for " + user.getName(), getEntityByName(User.class, name));
				}
				
				new DatabaseOperationWrapper(new DatabaseOperation() {
					
					@Override
					public void run() {
						/////////////////////////////
						// Do action: edit anonymous user
						///////////////////////////// 
						anonymous = getEntityByName(User.class, SecurityEntityAdaptor.ANONYMOUS + ".org1", wrapper);
						if (anonymous == null) {
							anonymous = EntityFactory.eINSTANCE.createUser();
							anonymous.setLogin(SecurityEntityAdaptor.ANONYMOUS + ".org1");
							anonymous.setName(anonymous.getLogin());
							anonymous.setEmail("anon@userEmail");
							anonymous.setActivated(true);
							
							anonymous = wrapper.merge(anonymous);
							
							// add to org1
							OrganizationUser ou = EntityFactory.eINSTANCE.createOrganizationUser();
							ou.setOrganization(org1);
							ou.setStatus(OrganizationMembershipStatus.MEMBER);
							ou.setUser(anonymous);
							anonymous.getOrganizationUsers().add(ou);
							anonymous = wrapper.merge(anonymous);
						}
					}
				});
				
				DatabaseOperationWrapper wrapper = new DatabaseOperationWrapper(new DatabaseOperation() {
					
					@Override
					public void run() {
						anonymous = getEntityByName(User.class, anonymous.getName(), wrapper);
						anonymous.setEmail(SecurityEntityAdaptor.ANONYMOUS + "Modif");
						wrapper.setOperationResult(userService.mergeAdminUIDto(Utils.convertUserToUserAdminUIDto(anonymous)));
					}
				});
				
				/////////////////////////////
				// Check result: edit anonymous user allowed
				///////////////////////////// 
				anonymous = getEntityByName(User.class, SecurityEntityAdaptor.ANONYMOUS + ".org1");
				assertNull("Anonymous user not edited for " + user.getName(), wrapper.getOperationResult());
				assertEquals("Anonymous user not edited for " + user.getName(), SecurityEntityAdaptor.ANONYMOUS + "Modif", anonymous.getEmail());
				
				wrapper = new DatabaseOperationWrapper(new DatabaseOperation() {
					
					@Override
					public void run() {
						/////////////////////////////
						// Do action: delete anonymous user
						///////////////////////////// 
						anonymous = getEntityByName(User.class, SecurityEntityAdaptor.ANONYMOUS + ".org1", wrapper);
						try {
							wrapper.setOperationResult(userService.delete(context, Collections.singletonList((int) anonymous.getId())));
						} catch (Exception e) {
							if (user.isAdmin())
								fail("Anonymous user not deleted for " + user.getName());
						}
					}
				});
				
				/////////////////////////////
				// Check result: delete anonymous user allowed/not allowed
				///////////////////////////// 
				if (user.isAdmin()) {
					assertNull("Anonymous user not deleted for " + user.getName(), errorMessage);
					assertNull("Anonymous user not deleted for " + user.getName(), getEntityByName(User.class, SecurityEntityAdaptor.ANONYMOUS + ".org1"));
				}
				return null;
			}
			
		});
	}
	
	@Test
	public void testAddEditDeleteAnonymousAsNormalUser() {
		testAnonymousUser(user1);
	}
	
	@Test
	public void testAddEditDeleteAnonymousAsAdmin() {
		testAnonymousUser(admin);
	}
	
	@Test
	public void testAsAnonymousUser() {
		test(anonymous, new RunnableWithParam<Void, ServiceInvocationContext>() {

			@Override
			public Void run(ServiceInvocationContext context) {
				/////////////////////////////
				// Do action: request organization
				/////////////////////////////
				OrganizationAdminUIDto orgDto = new OrganizationAdminUIDto();
				orgDto.setName("anonymousOrg");
				String errorMessage = userService.requestNewOrganization(context, orgDto, "");
				
				/////////////////////////////
				// Check result: anonymous cannot create new organization
				///////////////////////////// 
				assertEquals("Anonymous user cannot request a new organization!", errorMessage);
				assertNull("Organization was created", getEntityByName(Organization.class, "anonymousOrg"));
				
				/////////////////////////////
				// Do action: request membership
				/////////////////////////////
				errorMessage = userService.requestMembership(context, anonymous.getId(), "org1", "");
				
				/////////////////////////////
				// Check result: anonymous cannot request membership
				///////////////////////////// 
				assertEquals("Anonymous user cannot join organizations!", errorMessage);
				assertNull("Anonymous was added to organization", organizationService.getOrganizationMembershipStatus(org1, anonymous));
				
				/////////////////////////////
				// Do action: leave organization
				/////////////////////////////
				errorMessage = userService.leaveOrganizations(context, Utils.convertUserToUserAdminUIDto(anonymous), Collections.singletonList(Utils.convertOrganizationToOrganizationAdminUIDto(org1)), null);
				
				/////////////////////////////
				// Check result: anonymous cannot leave organization
				///////////////////////////// 
				assertEquals("Anonymous user cannot be removed from organizations!", errorMessage);
				
				return null;
			}
		});
	}
	
	@Test
	public void testAddUserToGroupFromOtherOrganization() {
		test(admin, new RunnableWithParam<Void, ServiceInvocationContext>() {

			@Override
			public Void run(ServiceInvocationContext context) {
				String errorMessage = (String) new DatabaseOperationWrapper(new DatabaseOperation() {
					
					@Override
					public void run() {
						/////////////////////////////
						// Do action: add user to group of an organization that he doesn't belong to
						///////////////////////////// 
						user1 = getEntityByName(User.class, user1.getName(), wrapper);
						GroupUser gu = EntityFactory.eINSTANCE.createGroupUser();
						gu.setGroup(getEntityByName(Group.class, "org3.user", wrapper));
						gu.setUser(user1);
						UserAdminUIDto userDto = Utils.convertUserToUserAdminUIDto(user1);
						wrapper.setOperationResult(userService.mergeAdminUIDto(userDto));
					}
				}).getOperationResult();
				
				
				/////////////////////////////
				// Check result: user cannot be added to group
				///////////////////////////// 
				assertEquals("User was added to a group of an organization that he doesn't belong to", "The user cannot be added to a group of an organization that he doesn't belong to!", errorMessage);
				return null;
			}
			
		});
	}
}
