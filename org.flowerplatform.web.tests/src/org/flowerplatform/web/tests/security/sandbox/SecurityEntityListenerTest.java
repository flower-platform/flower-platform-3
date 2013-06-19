package org.flowerplatform.web.tests.security.sandbox;

import java.io.FilePermission;
import java.security.Policy;
import java.util.Arrays;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;
import org.flowerplatform.web.entity.Group;
import org.flowerplatform.web.entity.GroupUser;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.PermissionEntity;
import org.flowerplatform.web.entity.User;
import org.flowerplatform.web.security.permission.AdminSecurityEntitiesPermission;
import org.flowerplatform.web.security.permission.FlowerWebFilePermission;
import org.flowerplatform.web.temp.GeneralService;
import org.flowerplatform.web.tests.security.sandbox.helpers.FlowerWebPolicyTest;
import org.flowerplatform.web.tests.security.sandbox.helpers.Utils;

/** 
 * 
 * @author Florin
 *
 */
public class SecurityEntityListenerTest {

	private static FlowerWebPolicyTest policy;
	
	private static Organization org1;
	
	private static Organization org2;
	
	private static Organization org3;
	
	private static Group org1AdminGroup;
	
	private static Group orgProj1MembersGroup;
	
	private static Group org2AdminGroup;
	
	private static Group org3AdminGroup;
	
	private static Group allGroup;
	
	private static User user1;
	
	private static User user2;
	
	private static User user3;
	
	private static PermissionEntity normalPermission;
	
	private static PermissionEntity treePermission;
	
	/**
	 * Setup database before each test, as each test modifies records.
	 */
	@Before
	public void beforeTest() {
		final GeneralService service = new GeneralService();
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				org1 = service.createOrganization("org1", wrapper);
				org2 = service.createOrganization("org2", wrapper);
				org3 = service.createOrganization("org3", wrapper);
				
				org1AdminGroup = service.createGroup("org1/admin", org1, wrapper);
				orgProj1MembersGroup = service.createGroup("org1/proj1_members", org1, wrapper);
				org2AdminGroup = service.createGroup("org2/admin", org2, wrapper);
				org3AdminGroup = service.createGroup("org3/admin", org3, wrapper);
				allGroup = service.createGroup("ALL", null, wrapper);
				
				user1 = service.createUserAndAddToGroups("user1", null, Arrays.asList(org1AdminGroup, orgProj1MembersGroup), wrapper);
				user2 = service.createUserAndAddToGroups("user2", null, Arrays.asList(org2AdminGroup), wrapper);
				user3 = service.createUserAndAddToGroups("user3", null, Arrays.asList(org3AdminGroup), wrapper);
				
				// create some normal permissions
				normalPermission = service.createPermission(AdminSecurityEntitiesPermission.class, "", user1, "#org1", wrapper);
				service.createPermission(AdminSecurityEntitiesPermission.class, "", user2, "#org2", wrapper);
				service.createPermission(AdminSecurityEntitiesPermission.class, "", user3, "#org3", wrapper);
				
				// create some tree permissions
				treePermission = service.createPermission(FlowerWebFilePermission.class, "org1/*", org1AdminGroup, FlowerWebFilePermission.READ_WRITE_DELETE, wrapper);
				service.createPermission(FlowerWebFilePermission.class, "org1/proj1/*", orgProj1MembersGroup, FlowerWebFilePermission.READ_WRITE_DELETE, wrapper);
				service.createPermission(FlowerWebFilePermission.class, "org1/proj1/activity_log.txt", orgProj1MembersGroup, FlowerWebFilePermission.READ, wrapper);
				service.createPermission(FlowerWebFilePermission.class, "org1/proj1/noadmin/*", org1AdminGroup, FlowerWebFilePermission.NONE, wrapper);
				service.createPermission(FlowerWebFilePermission.class, "org1/proj1/private/*", org3, FlowerWebFilePermission.READ_WRITE, wrapper);
				service.createPermission(FlowerWebFilePermission.class, "org1/proj1/private/*", allGroup, FlowerWebFilePermission.NONE, wrapper);
				service.createPermission(FlowerWebFilePermission.class, "org1/proj1/private/*", org2AdminGroup, FlowerWebFilePermission.READ, wrapper);
				service.createPermission(FlowerWebFilePermission.class, "org1/proj1/private/*", org1AdminGroup, FlowerWebFilePermission.READ_WRITE_DELETE, wrapper);
				service.createPermission(FlowerWebFilePermission.class, "org1/proj1/private/*", user1, FlowerWebFilePermission.NONE, wrapper);
			}
		});
		
		
		// install policy
		policy = (FlowerWebPolicyTest) Policy.getPolicy();
		
		// init caches
		Utils.hasPermission(user1, new AdminSecurityEntitiesPermission("", "#org1"));
		Utils.hasPermission(user2, new AdminSecurityEntitiesPermission("", "#org2"));
		Utils.hasPermission(user3, new AdminSecurityEntitiesPermission("", "#org3"));
		Utils.hasPermission(user1, new FilePermission("root/*", "read"));
	}
	
	@After
	public void afterTest() {
		Utils.deleteAllData();
	}
	
	@Test
	public void testDeleteUser() {		
		/////////////////////////////
		// Do action: delete user
		///////////////////////////// 
		deleteUser(user1);
		
		///////////////////////////
		// Check result: tree permissions cache cleared, normal permissions cache cleared for deleted user
		///////////////////////////
		Assert.assertEquals(0, policy.getTreePermissionsCache().keySet().size());
		Assert.assertEquals(null, policy.getNormalPermissionsCache().get(user1.getId()));
		Assert.assertTrue(policy.getNormalPermissionsCache().keySet().size() == 2);	
	}
	
	@Test
	public void testDeleteGroup() {
		/////////////////////////////
		// Do action: delete group
		///////////////////////////// 
		deleteGroup(org2AdminGroup);
		
		///////////////////////////
		// Check result: caches cleared
		///////////////////////////
		Assert.assertEquals(0, policy.getTreePermissionsCache().keySet().size());
		Assert.assertEquals(0, policy.getNormalPermissionsCache().keySet().size());
	}
	
	@Test
	public void testDeleteOrganization() {
		/////////////////////////////
		// Do action: delete organization
		///////////////////////////// 
		deleteOrganization(org1);
		
		///////////////////////////
		// Check result: caches cleared
		///////////////////////////
		Assert.assertEquals(0, policy.getTreePermissionsCache().keySet().size());
		Assert.assertEquals(0, policy.getNormalPermissionsCache().keySet().size());
	}
	
	@Test
	public void testDeleteNormalPermission() {
		/////////////////////////////
		// Do action: delete normal permission
		///////////////////////////// 
		deletePermission(normalPermission);
		
		///////////////////////////
		// Check result: normal permissions cache cleared
		///////////////////////////
		Assert.assertTrue(policy.getNormalPermissionsCache().size() == 0);
		Assert.assertTrue(policy.getTreePermissionsCache().size() == 1); // we have just one type of permissions: FlowerWebFilePermission
	}
	
	@Test
	public void testDeleteTreePermission() {
		/////////////////////////////
		// Do action: delete tree permission
		///////////////////////////// 
		deletePermission(treePermission);
		
		///////////////////////////
		// Check result: tree permissions cache cleared
		///////////////////////////
		Assert.assertTrue(policy.getNormalPermissionsCache().size() == 3); // we have 3 users
		Assert.assertTrue(policy.getTreePermissionsCache().size() == 0); 
	}
	
	@Test
	public void testUpdatePermission() {
		/////////////////////////////
		// Do action: update normal permission
		///////////////////////////// 
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				PermissionEntity permission = normalPermission;
				permission.setAssignedTo("@" + org3AdminGroup.getName());	
				wrapper.merge(permission);
			}
		});
					
		///////////////////////////
		// Check result: normal permissions cache cleared
		///////////////////////////
		Assert.assertTrue(policy.getNormalPermissionsCache().size() == 0);
		Assert.assertTrue(policy.getTreePermissionsCache().size() == 1); 
	}
	
	private void deleteUser(final User u) {
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				User user = (User) wrapper.merge(u);
				for (GroupUser gu: user.getGroupUsers()) {
					wrapper.delete(gu);
				}			
				wrapper.delete(user);
			}
		});
	}
	
	private void deleteGroup(final Group g) {
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				Group group = (Group) wrapper.merge(g);
				for (GroupUser gu: group.getGroupUsers()) {
					wrapper.delete(gu);
				}			
				wrapper.delete(group);
			}
		});
	}
	
	private void deleteOrganization(final Organization o) {
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				Organization org = (Organization) wrapper.merge(o);
				for (Group g: org.getGroups()) {
					g.setOrganization(null);				
				}
				wrapper.delete(org);	
			}
		});
	}
	
	private void deletePermission(final PermissionEntity permission) {
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				wrapper.delete(permission);	
			}
		});
	}
	
}
