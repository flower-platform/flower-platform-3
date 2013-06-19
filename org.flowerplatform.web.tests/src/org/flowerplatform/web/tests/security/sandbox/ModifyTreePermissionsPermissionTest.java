package org.flowerplatform.web.tests.security.sandbox;

import static org.flowerplatform.web.tests.security.sandbox.helpers.Utils.test;

import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import org.flowerplatform.common.util.RunnableWithParam;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;
import org.flowerplatform.web.entity.Group;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.PermissionEntity;
import org.flowerplatform.web.entity.User;
import org.flowerplatform.web.security.dto.PermissionAdminUIDto;
import org.flowerplatform.web.security.permission.FlowerWebFilePermission;
import org.flowerplatform.web.security.permission.ModifyTreePermissionsPermission;
import org.flowerplatform.web.security.service.PermissionService;
import org.flowerplatform.web.temp.GeneralService;
import org.flowerplatform.web.tests.security.sandbox.helpers.Utils;

/**
 * Tests ModifyTreePermissionsPermission for creating/updating/deleting PermissionEntities
 * for ModifyTreePermissionsPermission and FlowerWebFilePermission
 * 
 * @author Florin
 * @author Mariana
 */
public class ModifyTreePermissionsPermissionTest {

	private static PermissionService permissionService;
	
	private static Group org1AdminGroup;
	
	private static Group org2AdminGroup;
	
	private static User user1;
	
	private static PermissionEntity filePermission;
	
	private static PermissionEntity filePermission_2;
	
	private static String filePath = "root/*";
	
	@BeforeClass
	public static void setup() {
		
		final GeneralService service = new GeneralService();
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				Organization org1 = service.createOrganization("org1", wrapper);
				Organization org2 = service.createOrganization("org2", wrapper);
				
				org1AdminGroup = service.createGroup("org1/admin", org1, wrapper);
				Group orgProj1MembersGroup = service.createGroup("org1/proj1_members", org1, wrapper);
				org2AdminGroup = service.createGroup("org2/admin", org2, wrapper);
				
				user1 = service.createUserAndAddToGroups("user1", null, Arrays.asList(org1AdminGroup, orgProj1MembersGroup), wrapper);
				service.createUserAndAddToGroups("user2", null, Arrays.asList(org2AdminGroup), wrapper);
				
				service.createPermission(ModifyTreePermissionsPermission.class, "folder_1", user1, "#org1, $user2", wrapper);
				filePermission = service.createPermission(FlowerWebFilePermission.class, "folder_1", org1AdminGroup, FlowerWebFilePermission.READ_WRITE, wrapper);
				filePermission_2 = service.createPermission(FlowerWebFilePermission.class, "folder_1", org2AdminGroup, FlowerWebFilePermission.READ_WRITE, wrapper);
				
				service.createPermission(ModifyTreePermissionsPermission.class, filePath, user1, "#org1, $user2", wrapper);
				filePermission = service.createPermission(FlowerWebFilePermission.class, filePath, org1AdminGroup, FlowerWebFilePermission.READ_WRITE, wrapper);
				filePermission_2 = service.createPermission(FlowerWebFilePermission.class, filePath, org2AdminGroup, FlowerWebFilePermission.READ_WRITE, wrapper);
			}
		});
		
		permissionService = (PermissionService) CommunicationPlugin.getInstance().getServiceRegistry().getService(PermissionService.SERVICE_ID);
	}
	
	@Test
	public void createFlowerWebFilePermissionTest() {
		test(user1, new RunnableWithParam<Void, ServiceInvocationContext>() {

			@Override
			public Void run(ServiceInvocationContext context) {
				
				/////////////////////////////
				// Do action: create permission for organization where user has ModifyTreePerm
				///////////////////////////// 
				PermissionAdminUIDto dto = new PermissionAdminUIDto();
				dto.setType(FlowerWebFilePermission.class.getName());
				dto.setName(filePath);
				dto.setAssignedTo("#org1");
				dto.setActions(FlowerWebFilePermission.READ);
				try {
					permissionService.mergeAdminUIDto(context, dto);
				} catch (SecurityException e) {					
					///////////////////////////
					// Check result: permission created successfully
					///////////////////////////
					Assert.fail("Add permissions failed with " + e.getMessage());
				}
				
				/////////////////////////////
				// Do action: create permission for user where user has ModifyTreePerm
				///////////////////////////// 
				dto = new PermissionAdminUIDto();
				dto.setType(FlowerWebFilePermission.class.getName());
				dto.setName(filePath);
				dto.setAssignedTo("$user2");
				dto.setActions(FlowerWebFilePermission.READ);
				
				try {
					permissionService.mergeAdminUIDto(context, dto);
				} catch (SecurityException e) {
					///////////////////////////
					// Check result: permission created successfully
					///////////////////////////
					Assert.fail("Add permissions failed with " + e.getMessage());
				}
				
				/////////////////////////////
				// Do action: create permission for organization where user does not have ModifyTreePerm
				///////////////////////////// 
				dto = new PermissionAdminUIDto();
				dto.setType(FlowerWebFilePermission.class.getName());
				dto.setName(filePath);
				dto.setAssignedTo("#org2");
				dto.setActions(FlowerWebFilePermission.READ);
				try {
					permissionService.mergeAdminUIDto(context, dto);
					///////////////////////////
					// Check result: not allowed
					///////////////////////////
					Assert.fail();
				} catch (SecurityException e) {		
					// do nothing
				}
				return null;
			}
		});
	}
	
	@Test
	public void createModifyTreePermissionsPermissionTest() {
		test(user1, new RunnableWithParam<Void, ServiceInvocationContext>() {

			@Override
			public Void run(ServiceInvocationContext context) {
				
				/////////////////////////////
				// Do action: create permission for organization where user has ModifyTreePerm
				///////////////////////////// 
				PermissionAdminUIDto dto = new PermissionAdminUIDto();
				dto.setType(ModifyTreePermissionsPermission.class.getName());
				dto.setName(filePath);
				dto.setAssignedTo("#org1");
				dto.setActions("#org1");
				
				try {
					permissionService.mergeAdminUIDto(context, dto);
				} catch (SecurityException e) {			
					///////////////////////////
					// Check result: permission created successfully
					///////////////////////////
					Assert.fail("Add permissions failed with " + e.getMessage());
				}
				
				/////////////////////////////
				// Do action: create permission for organization where user does not have ModifyTreePerm
				///////////////////////////// 
				dto = new PermissionAdminUIDto();
				dto.setType(ModifyTreePermissionsPermission.class.getName());
				dto.setName(filePath);
				dto.setAssignedTo("#org1");
				dto.setActions("#org1,#org2");
				try {
					permissionService.mergeAdminUIDto(context, dto);
					///////////////////////////
					// Check result: not allowed
					///////////////////////////
					Assert.fail();
				} catch (SecurityException e) {	
					// do nothing
				}
				return null;
			}
		});
	}
	
	@Test
	public void updateTest() {
		test(user1, new RunnableWithParam<Void, ServiceInvocationContext>() {

			@Override
			public Void run(ServiceInvocationContext context) {
				
				/////////////////////////////
				// Do action: update permission for organization where user has ModifyTreePerm
				///////////////////////////// 
				PermissionAdminUIDto dto = new PermissionAdminUIDto();
				dto.setId(filePermission.getId());
				dto.setType(FlowerWebFilePermission.class.getName());
				dto.setName(filePath);
				dto.setAssignedTo("#org1");
				dto.setActions("read-write-delete");
				try {
					permissionService.mergeAdminUIDto(context, dto);					
				} catch (SecurityException e) {						
					///////////////////////////
					// Check result: permission updated successfully
					///////////////////////////
					Assert.fail("Update permissions failed with " + e.getMessage());
				}
				
				/////////////////////////////
				// Do action: update permission for organization where user does not have ModifyTreePerm
				///////////////////////////// 
				dto = new PermissionAdminUIDto();
				dto.setId(filePermission_2.getId());
				dto.setType(FlowerWebFilePermission.class.getName());
				dto.setName(filePath);
				dto.setAssignedTo("#org1");
				dto.setActions("read-write-delete");
				try {
					permissionService.mergeAdminUIDto(context, dto);
					///////////////////////////
					// Check result: not allowed
					///////////////////////////
					Assert.fail();
				} catch (SecurityException e) {											
				}
				
				return null;
			}
		});
	}
	
	@Test
	public void deleteTest() {
		test(user1, new RunnableWithParam<Void, ServiceInvocationContext>() {

			@Override
			public Void run(ServiceInvocationContext param) {
				try {
					/////////////////////////////
					// Do action: delete permission for organization where user has ModifyTreePerm
					///////////////////////////// 
					permissionService.delete(Arrays.asList((int)filePermission.getId()));
				} catch (SecurityException e) {
					///////////////////////////
					// Check result: permission deleted successfully
					///////////////////////////
					Assert.fail("Delete permissions failed with " + e.getMessage());
				}			
				
				try {
					/////////////////////////////
					// Do action: delete permission for organization where user does not have ModifyTreePerm
					///////////////////////////// 
					permissionService.delete(Arrays.asList((int)filePermission_2.getId()));
					///////////////////////////
					// Check result: not allowed
					///////////////////////////
					Assert.fail();
				} catch (SecurityException e) {
					
				}

				return null;
			}
			
		});
	}
	
	@AfterClass
	public static void tearDown() {
		Utils.deleteAllData();
	}
}
