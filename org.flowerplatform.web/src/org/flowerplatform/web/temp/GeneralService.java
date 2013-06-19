package org.flowerplatform.web.temp;

import java.util.List;

import org.flowerplatform.web.database.DatabaseOperationWrapper;
import org.flowerplatform.web.entity.Group;
import org.flowerplatform.web.entity.GroupUser;
import org.flowerplatform.web.entity.ISecurityEntity;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.OrganizationMembershipStatus;
import org.flowerplatform.web.entity.EntityFactory;
import org.flowerplatform.web.entity.OrganizationUser;
import org.flowerplatform.web.entity.PermissionEntity;
import org.flowerplatform.web.entity.SVNCommentEntity;
import org.flowerplatform.web.entity.SVNRepositoryURLEntity;
import org.flowerplatform.web.entity.User;
import org.flowerplatform.web.security.service.Util;

/**
 * 
 * Temporary code used to populate the in-memory database when the application starts.
 * The entities return by these methods will be the one from cache, not the detached entities
 * created in the transaction scoped persistence context (i.e. created by entity manager)
 * 
 * @author Florin
 * @author Mariana
 *
 */
public class GeneralService {

	public Organization createOrganization(String name, DatabaseOperationWrapper wrapper) {
		Organization org = EntityFactory.eINSTANCE.createOrganization();
		org.setName(name);	
		org.setURL(name);
		org.setLabel(name + "Label");
		org.setActivated(true);
		org = wrapper.merge(org);		 
		return org;
	}
	
	public Group createGroup(String groupName, Organization organization, DatabaseOperationWrapper wrapper) {
		Group group = EntityFactory.eINSTANCE.createGroup();
		group.setName(groupName);
		group = wrapper.merge(group);
		if (organization != null) {
			organization.getGroups().add(group);
			group.setOrganization(wrapper.merge(organization));
		}
		return group;
	}
	
	public User createUserAndAddToGroups(String name, String password, List<Group> groups, DatabaseOperationWrapper wrapper) {
		User user = createUser(name, password, wrapper);
		for (Group g: groups) {
			addUserToGroup(user, g);
		}
		return user;
	}
	
	public User createUser(String name, String password, DatabaseOperationWrapper wrapper) {	
		User user = EntityFactory.eINSTANCE.createUser();
		user.setLogin(name);
		user.setName(name);
		user.setEmail("csp3@crispico.com");
		if (password != null) {
			user.setHashedPassword(Util.encrypt(password));
		} else {
			user.setHashedPassword(Util.encrypt("a"));
		}
		user.setActivationCode("12345");
		user.setActivated(true);
		return wrapper.merge(user);
	}
	
	public void createSVNCommentAndAddToUser(String body, User user) {
		SVNCommentEntity comment;
		comment = EntityFactory.eINSTANCE.createSVNCommentEntity();
		comment.setBody(body);
		comment.setTimestamp(System.currentTimeMillis());
		comment.setUser(user);
	}
	
	public void createSVNRepositoryURLAndAddToOrg(String name, Organization organization) {
		SVNRepositoryURLEntity url;
		url = EntityFactory.eINSTANCE.createSVNRepositoryURLEntity();
		url.setName(name);
		url.setOrganization(organization);
	}
	
	/**
	 * User and group are updated only if they are from cache.
	 * If they are not from cache they are not updated.
	 * 
	 * @param user
	 * @param group
	 */
	public void addUserToGroup(User user, Group group) {
		GroupUser gu = EntityFactory.eINSTANCE.createGroupUser();
		OrganizationUser organizationUser = EntityFactory.eINSTANCE.createOrganizationUser();
		gu.setUser(user);
		gu.setGroup(group);				
		
		// find operations will go to cache, not to the database
		// this will update the cache, so subsequent find operations will return proper results		
//		user.getGroupUsers().add(gu);
//		group.getGroupUsers().add(gu);
			
		Organization organization = group.getOrganization();
		boolean alreadyInOrg = organization == null;
		if (organization != null) {
			if (organization.getOrganizationUsers() != null) {
				for (OrganizationUser ou : organization.getOrganizationUsers()) {
					if (ou.getUser().getId() == user.getId()) {
						alreadyInOrg = true;
						break;
					}
				}
			}
		}
		if (!alreadyInOrg) {
			organizationUser.setUser(user);
			organizationUser.setOrganization(organization);
			if (group.getName().endsWith("admin"))
				organizationUser.setStatus(OrganizationMembershipStatus.ADMIN);
			else
				organizationUser.setStatus(OrganizationMembershipStatus.MEMBER);
			
//			user.getOrganizationUsers().add(organizationUser);
//			organization.getOrganizationUsers().add(organizationUser);
			
//			user = wrapper.merge(user);
//			organization = wrapper.merge(organization);
		}
	}
	
	public PermissionEntity createPermission(Class<?> implementedType, String resource, ISecurityEntity assignedTo, String parameters, DatabaseOperationWrapper wrapper) {
		PermissionEntity p = EntityFactory.eINSTANCE.createPermissionEntity();
		p.setName(resource);// path
		p.setActions(parameters);// actions
		p.setType(implementedType.getName());
		if (assignedTo instanceof User) {
			User u = (User) assignedTo;
			p.setAssignedTo("$" + u.getLogin());
		} else if (assignedTo instanceof Group){
			Group group = (Group) assignedTo;
			p.setAssignedTo("@" + group.getName());
		} else if (assignedTo instanceof Organization) {
			Organization org = (Organization) assignedTo;
			p.setAssignedTo("#" + org.getName());
		}
		return wrapper.merge(p);
	}
}
