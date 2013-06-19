package org.flowerplatform.web.tests.security.sandbox.helpers;

import java.security.Permission;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.security.auth.Subject;

import junit.framework.Assert;

import org.flowerplatform.common.util.RunnableWithParam;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.IPrincipal;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.web.communication.RecordingTestWebCommunicationChannel;
import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;
import org.flowerplatform.web.entity.Group;
import org.flowerplatform.web.entity.GroupUser;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.OrganizationUser;
import org.flowerplatform.web.entity.User;
import org.flowerplatform.web.entity.dto.NamedDto;
import org.flowerplatform.web.security.dto.GroupAdminUIDto;
import org.flowerplatform.web.security.dto.OrganizationAdminUIDto;
import org.flowerplatform.web.security.dto.OrganizationUserAdminUIDto;
import org.flowerplatform.web.security.dto.UserAdminUIDto;
import org.flowerplatform.web.security.sandbox.FlowerWebPrincipal;
import org.flowerplatform.web.security.sandbox.FlowerWebSecurityException;
import org.flowerplatform.web.security.service.OrganizationService;
import flex.messaging.FlexContext;
import flex.messaging.HttpFlexSession;

/**
 * 
 * @author Florin
 * @author Mariana
 */
public class Utils {
	
	public static boolean hasPermission(User user, final Permission p) {
		Subject subject = new Subject();
		final Principal principal = new FlowerWebPrincipal(user.getId());
		subject.getPrincipals().add(principal);
		return Subject.doAsPrivileged(subject, new PrivilegedAction<Boolean>() {

			@Override
			public Boolean run() {
				try {
					System.getSecurityManager().checkPermission(p);
					return true;
				} catch (Exception e) {
					if (!(e instanceof FlowerWebSecurityException)) {
						e.printStackTrace();
						Assert.fail();
					}
					return false;
				}
			}
		}, null);
	}
	
	public static <T> T getEntityByName(Class<T> cls, String name, DatabaseOperationWrapper wrapper) {
		List<T> list = wrapper.findByField(cls, "name", name);
		if (list.size() > 0)
			return list.get(0);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getEntityByName(final Class<T> cls, final String name) {
		return (T) new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				wrapper.setOperationResult(getEntityByName(cls, name, wrapper));
				
			}
		}).getOperationResult();
	}
	
	public static void test(final User user, final RunnableWithParam<Void, ServiceInvocationContext> runnable) {
		Subject subject = new Subject();
		final Principal principal = new FlowerWebPrincipal(user.getId());
		subject.getPrincipals().add(principal);
		Subject.doAsPrivileged(subject, new PrivilegedAction<Void>() {

			@Override
			public Void run() {
				FlexContext.setThreadLocalSession(new HttpFlexSession());
				FlexContext.setUserPrincipal(principal);
				RecordingTestWebCommunicationChannel cc = new RecordingTestWebCommunicationChannel();
				cc.setPrincipal((FlowerWebPrincipal) principal);
				CommunicationPlugin.tlCurrentPrincipal.set((IPrincipal) principal);
				ServiceInvocationContext context = new ServiceInvocationContext(cc);
				
				runnable.run(context);
				
				return null;
			}
			
		}, null);
	}
	
	public static UserAdminUIDto convertUserToUserAdminUIDto(User user) {
		UserAdminUIDto dto = new UserAdminUIDto();
		
		dto.setId(user.getId());
		dto.setName(user.getName());
		dto.setEmail(user.getEmail());
		dto.setIsActivated(user.isActivated());
		dto.setLogin(user.getLogin());
		
		HashSet<GroupAdminUIDto> groups = new HashSet<GroupAdminUIDto>();
		if (user.getGroupUsers() != null) {
			for (GroupUser groupUser : user.getGroupUsers()) {
				Group group = groupUser.getGroup();
				NamedDto orgDto = null;
				if (group.getOrganization() != null) {
					orgDto = new NamedDto(group.getOrganization().getId(), group.getOrganization().getName());					
				}			
				groups.add(new GroupAdminUIDto(group.getId(), group.getName(), orgDto));
			}
			dto.setGroups(groups);
		}
		
		HashSet<OrganizationUserAdminUIDto> organizations = new HashSet<OrganizationUserAdminUIDto>();
		if (user.getOrganizationUsers() != null) {
			for (OrganizationUser organizationUser : user.getOrganizationUsers()) {
				OrganizationUserAdminUIDto ouDto = new OrganizationUserAdminUIDto();
				ouDto.setId(organizationUser.getId());
				ouDto.setOrganization(OrganizationService.getInstance().convertOrganizationToOrganizationAdminUIDto(organizationUser.getOrganization(), user));
				ouDto.setStatus(organizationUser.getStatus());
				organizations.add(ouDto);
			}
			dto.setOrganizationUsers(organizations);
		}
		return dto;
	}
	
	public static GroupAdminUIDto convertGroupToGroupAdminUIDto(Group group) {
		GroupAdminUIDto dto = new GroupAdminUIDto();
		
		dto.setId(group.getId());
		dto.setName(group.getName());
		if (group.getOrganization() != null) {
			dto.setOrganization(new NamedDto(group.getOrganization().getId(), group.getOrganization().getName()));
		}
		return dto;
	}
	
	public static OrganizationAdminUIDto convertOrganizationToOrganizationAdminUIDto(Organization organization) {
		OrganizationAdminUIDto dto = new OrganizationAdminUIDto();
		dto.setId(organization.getId());
		dto.setName(organization.getName());
		return dto;
	}
	
	public static void deleteAllData() {
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				List<String> list = Arrays.asList(
						"SVNCommentEntity", "SVNRepositoryURLEntity", 
						"GroupUser", "OrganizationUser", "User", "Group", "Organization", "PermissionEntity");
				for (String table : list) {
					wrapper.createQuery(String.format("delete from %s", table)).executeUpdate();
				}
			}
		});
	}
}
