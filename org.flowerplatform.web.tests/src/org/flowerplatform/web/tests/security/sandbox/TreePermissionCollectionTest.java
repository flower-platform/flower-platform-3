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

import java.io.File;
import java.io.FilePermission;
import java.util.Arrays;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;
import org.flowerplatform.web.entity.Group;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.User;
import org.flowerplatform.web.security.permission.FlowerWebFilePermission;
import org.flowerplatform.web.security.sandbox.FlowerWebPrincipal;
import org.flowerplatform.web.security.sandbox.TreePermissionCollection;
import org.flowerplatform.web.security.sandbox.TreePermissionCollectionEntry;
import org.flowerplatform.web.temp.GeneralService;
import org.flowerplatform.web.tests.security.sandbox.helpers.Utils;

/**
 * Test for TreePermissionCollection from (W-SEC-SANDBOX) Security Sandbox functionality.
 * 
 * @author Florin
 */
public class TreePermissionCollectionTest {
	
	private static TreePermissionCollection permissionCollection;
	
	private static Organization org1;
	
	private static Organization org2;
	
	private static Organization org3;
	
	private static Organization org4;
	
	private static Group org1AdminGroup;
	
	private static Group orgProj1MembersGroup;
	
	private static Group org2AdminGroup;
	
	private static Group org3AdminGroup;
	
	private static Group org4AdminGroup;
	
	private static Group allGroup;
	
	private static User user1;
	
	private static User user2;
	
	private static User user3;
	
	private static User user4;
	
	private static User userEverywhere;
	
	/**
	 *	Tabel de permisiuni:
	 *
	 * -									@ALL	 				r
	 * /org1/-								@org1/admin				rwd
	 * /org1/proj1/-						@org1/proj1_members		rwd
	 * /org1/proj1/activity_log.txt			@org1/proj1_members		rw
	 * /org1/proj1/noadmin/-				@org1/admin				none
	 * /org1/proj1/private/-				#org3					rw
	 * /org1/proj1/private/-				@ALL      				none
	 * /org1/proj1/private/-				@org2/admin				r
	 * /org1/proj1/private/-				@org1/admin				rwd
	 * /org1/proj1/private/-				$org1/admin/user1		none
	 * /r-demo								@ALL					rwd
	 */
	@BeforeClass
	public static void beforeClass() {	
		permissionCollection = new TreePermissionCollection(FlowerWebFilePermission.class, new File("C:"));
		
		final GeneralService service = new GeneralService();
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				org1 = service.createOrganization("org1", wrapper);
				org2 = service.createOrganization("org2", wrapper);
				org3 = service.createOrganization("org3", wrapper);
				org4 = service.createOrganization("org4", wrapper);
				
				org1AdminGroup = service.createGroup("org1/admin", org1, wrapper);
				orgProj1MembersGroup = service.createGroup("org1/proj1_members", org1, wrapper);
				org2AdminGroup = service.createGroup("org2/admin", org2, wrapper);
				org3AdminGroup = service.createGroup("org3/admin", org3, wrapper);
				org4AdminGroup = service.createGroup("org4/admin", org4, wrapper);
				allGroup = service.createGroup("ALL", null, wrapper);
				
				user1 = service.createUserAndAddToGroups("user1", null, Arrays.asList(org1AdminGroup), wrapper);
				user2 = service.createUserAndAddToGroups("user2", null, Arrays.asList(org2AdminGroup), wrapper);
				user3 = service.createUserAndAddToGroups("user3", null, Arrays.asList(org3AdminGroup), wrapper);
				user4 = service.createUserAndAddToGroups("user4", null, Arrays.asList(org4AdminGroup), wrapper);
				userEverywhere = service.createUserAndAddToGroups("userEverywhere", null,
						Arrays.asList(org1AdminGroup, orgProj1MembersGroup, org2AdminGroup, org3AdminGroup, org4AdminGroup), wrapper);
			}
		});
		
					
		permissionCollection.addPermission(allGroup, new FlowerWebFilePermission("*", FlowerWebFilePermission.READ));
		permissionCollection.addPermission(org1AdminGroup, new FlowerWebFilePermission("org1/*", FlowerWebFilePermission.READ_WRITE_DELETE));
		permissionCollection.addPermission(user4, new FlowerWebFilePermission("org1/*", FlowerWebFilePermission.READ_WRITE_DELETE));
		permissionCollection.addPermission(orgProj1MembersGroup, new FlowerWebFilePermission("org1/proj1/*", FlowerWebFilePermission.READ_WRITE_DELETE));
		permissionCollection.addPermission(orgProj1MembersGroup, new FlowerWebFilePermission("org1/proj1/activity_log.txt", FlowerWebFilePermission.READ_WRITE));
		permissionCollection.addPermission(org1AdminGroup, new FlowerWebFilePermission("org1/proj1/noadmin/*", FlowerWebFilePermission.NONE));
		permissionCollection.addPermission(org3, new FlowerWebFilePermission("org1/proj1/private/*", FlowerWebFilePermission.READ_WRITE));
		permissionCollection.addPermission(allGroup, new FlowerWebFilePermission("org1/proj1/private/*", FlowerWebFilePermission.NONE));
		permissionCollection.addPermission(org2AdminGroup, new FlowerWebFilePermission("org1/proj1/private/*", FlowerWebFilePermission.READ));
		permissionCollection.addPermission(org1AdminGroup, new FlowerWebFilePermission("org1/proj1/private/*", FlowerWebFilePermission.READ_WRITE_DELETE));
		permissionCollection.addPermission(user1, new FlowerWebFilePermission("org1/proj1/private/*", FlowerWebFilePermission.NONE));
		permissionCollection.addPermission(allGroup, new FlowerWebFilePermission("r-demo/*", FlowerWebFilePermission.READ_WRITE_DELETE));
	}
	
	/**
	 * Test we do not stop on first deny permission (first we encounter a deny, but later we get permissions).
	 */
	@Test
	public void test1() {
		java.io.FilePermission permission = new java.io.FilePermission("C:/org1/proj1/private/*", "read, write");
		FlowerWebPrincipal principal = new FlowerWebPrincipal(user1.getId());
		boolean implies = permissionCollection.implies(principal, permission);
		Assert.assertTrue(implies);
	}
	
	/**
	 * Test that we stop when path changes (after the path changes there is an allow). 
	 */
	@Test
	public void test2() {
		java.io.FilePermission permission = new java.io.FilePermission("C:/org1/proj1/private/*", "read, write");
		FlowerWebPrincipal principal = new FlowerWebPrincipal(user4.getId());
		boolean implies = permissionCollection.implies(principal, permission);
		Assert.assertFalse(implies);
	}
	
	@Test
	public void test3() {
		java.io.FilePermission permission = new java.io.FilePermission("C:/org1/undeva/a.txt", "read");
		FlowerWebPrincipal principal = new FlowerWebPrincipal(user4.getId());
		boolean implies = permissionCollection.implies(principal, permission);
		Assert.assertTrue(implies);
		
		permission = new java.io.FilePermission("/tmp/undeva/a.txt", "read, write");
		principal = new FlowerWebPrincipal(user1.getId());
		implies = permissionCollection.implies(principal, permission);
		Assert.assertFalse(implies);
	}
	
	/**
	 * Test that an user has read permissions, but not wider permissions
	 */
	@Test
	public void test4() {
		FlowerWebPrincipal principal = new FlowerWebPrincipal(user2.getId());
		
		java.io.FilePermission permission = new java.io.FilePermission("C:/org1/proj1/private/file.txt", "read");		
		boolean implies = permissionCollection.implies(principal, permission);
		Assert.assertTrue(implies);
		
		permission = new java.io.FilePermission("C:/org1/proj1/private/file.txt", "write");
		implies = permissionCollection.implies(principal, permission);
		Assert.assertFalse(implies);
		
		permission = new java.io.FilePermission("C:/org1/proj1/private/file.txt", "delete");
		implies = permissionCollection.implies(principal, permission);
		Assert.assertFalse(implies);
		
		permission = new java.io.FilePermission("C:/org1/proj1/private/file.txt", "read,write");
		implies = permissionCollection.implies(principal, permission);
		Assert.assertFalse(implies);
		
		permission = new java.io.FilePermission("C:/org1/proj1/private/file.txt", "read,write,delete");
		implies = permissionCollection.implies(principal, permission);
		Assert.assertFalse(implies);
	}
	
	@Test
	public void pathMatchTest() {
		TreePermissionCollection collection = new TreePermissionCollection(FlowerWebFilePermission.class, new File("C:"));
		
		Assert.assertTrue(collection.pathMatch("C:/a/b/c.txt", "*"));
		Assert.assertTrue(collection.pathMatch("C:/a/b/c.txt", "/*"));
		Assert.assertTrue(collection.pathMatch("C:/a/b/c.txt", "/a/b/*"));
		Assert.assertTrue(collection.pathMatch("C:/a/b/c.txt", "/a/b/c.txt"));
		Assert.assertFalse(collection.pathMatch("C:/a/b/c.tx", "/a/b/c.txt"));		
		Assert.assertFalse(collection.pathMatch("C:/a/b/c.txt", "/aa/*"));		
	}
	
	@Test
	public void testUserBelongsToUser() {
		Assert.assertTrue(user1.contains(user1));
		Assert.assertFalse(user1.contains(user2));
	}
	
	@Test
	public void testUserBelongsToGroup() {
		Assert.assertTrue(org1AdminGroup.contains(user1));
		Assert.assertTrue(org2AdminGroup.contains(user2));
		Assert.assertTrue(org3AdminGroup.contains(user3));
		Assert.assertTrue(org4AdminGroup.contains(user4));
		
		Assert.assertFalse(org1AdminGroup.contains(user2));
		Assert.assertFalse(org1AdminGroup.contains(user3));
		Assert.assertFalse(org1AdminGroup.contains(user4));

		Assert.assertTrue(allGroup.contains(user1));
		Assert.assertTrue(allGroup.contains(user2));
		Assert.assertTrue(allGroup.contains(user3));
		Assert.assertTrue(allGroup.contains(user4));		
		
		Assert.assertTrue(org1AdminGroup.contains(userEverywhere));
		Assert.assertTrue(orgProj1MembersGroup.contains(userEverywhere));
		Assert.assertTrue(org2AdminGroup.contains(userEverywhere));
		Assert.assertTrue(org3AdminGroup.contains(userEverywhere));
		Assert.assertTrue(org4AdminGroup.contains(userEverywhere));
	}
	
	@Test
	public void testUserBelongsToOrganization() {
		Assert.assertTrue(org1.contains(user1));
		Assert.assertTrue(org2.contains(user2));
		Assert.assertTrue(org3.contains(user3));
		Assert.assertTrue(org4.contains(user4));
		
		Assert.assertTrue(org1.contains(userEverywhere));
		Assert.assertTrue(org2.contains(userEverywhere));
		Assert.assertTrue(org3.contains(userEverywhere));
		Assert.assertTrue(org4.contains(userEverywhere));		
	}
	
	@Test
	public void sortTest() {
		TreePermissionCollection col = new TreePermissionCollection(FlowerWebFilePermission.class, new File("/"));
		
		col.addPermission(user1, new FlowerWebFilePermission("/z_folder/*", "read")); // 2
		col.addPermission(user1, new FlowerWebFilePermission("/*", "read")); // 1
		col.addPermission(user1, new FlowerWebFilePermission("/z_folder/b.txt", "read")); // 4
		col.addPermission(user1, new FlowerWebFilePermission("/z_folder/a.txt", "read")); // 3		
		col.addPermission(user1, new FlowerWebFilePermission("/", "read")); // 0
		
		col.implies(new FlowerWebPrincipal(user1.getId()), new FilePermission("/", "read"));
		
		Assert.assertTrue(equal(col.entries.get(0), new FlowerWebFilePermission("/", "read")));
		Assert.assertTrue(equal(col.entries.get(1), new FlowerWebFilePermission("/*", "read")));
		Assert.assertTrue(equal(col.entries.get(2), new FlowerWebFilePermission("/z_folder/*", "read")));
		Assert.assertTrue(equal(col.entries.get(3), new FlowerWebFilePermission("/z_folder/a.txt", "read")));
		Assert.assertTrue(equal(col.entries.get(4),new FlowerWebFilePermission("/z_folder/b.txt", "read")));		
	}
	
	private static boolean equal(TreePermissionCollectionEntry entry, FlowerWebFilePermission p2 ) {
		return entry.getPermission().getName().equals(p2.getName()) && entry.getPermission().getActions().equals(p2.getActions());
	}

	@AfterClass
	public static void tearDown() {
		Utils.deleteAllData();
	}
}