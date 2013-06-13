package org.flowerplatform.web.temp;

import java.util.List;

import org.flowerplatform.web.WebPlugin;
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
import org.flowerplatform.web.entity.dao.Dao;
import org.flowerplatform.web.security.service.Util;

/**
 * 
 * Temporary code used to populate the in-memory database when the application starts.
 * The entities return by these methods will be the one from cache, not the detached entities
 * created in the transaction scoped persistence context (i.e. created by entity manager)
 * 
 * @author Florin
 *
 */
public class GeneralService {

	private Dao getDao() {
		return WebPlugin.getInstance().getDao();
	}
	
	public Organization createOrganization(String name) {
		Organization org = EntityFactory.eINSTANCE.createOrganization();
		org.setName(name);	
		org.setURL(name);
		org.setLabel(name + "Label");
		org.setActivated(true);
		org = getDao().merge(org);		 
		return org;
	}
	
	public Group createGroup(String groupName, Organization organization) {
		Group group = EntityFactory.eINSTANCE.createGroup();
		group.setName(groupName);
		group.setOrganization(organization);		
		group = getDao().merge(group);
		if (organization != null) {
			organization.getGroups().add(group);
			organization = getDao().merge(organization);
		}
		return group;
	}
	
	public User createUserAndAddToGroups(String name, String password, List<Group> groups) {
		User user = createUser(name, password);
		for (Group g: groups) {
			addUserToGroup(user, g);
		}
		return user;
	}
	
	public User createUser(String name, String password) {		
		User user = EntityFactory.eINSTANCE.createUser();
		user.setLogin(name);
		user.setName(name);
//		user.setEmail(name + "@userEmail");
		user.setEmail("csp3@crispico.com");
		if (password != null) {
			user.setHashedPassword(Util.encrypt(password));
		} else {
			user.setHashedPassword(Util.encrypt("a"));
		}
		user.setActivationCode("12345");
		user.setActivated(true);
		user = getDao().merge(user);
		return user;
	}
	
	public void createSVNCommentAndAddToUser(String body, User user) {
		SVNCommentEntity comment;
		comment = EntityFactory.eINSTANCE.createSVNCommentEntity();
		comment.setBody(body);
		comment.setTimestamp(System.currentTimeMillis());
		comment.setUser(user);
		comment = getDao().merge(comment);
		user.getSvnComments().add(comment);	
		user = getDao().merge(user);
	}
	
	public void createSVNRepositoryURLAndAddToOrg(String name, Organization organization) {
		SVNRepositoryURLEntity url;
		url = EntityFactory.eINSTANCE.createSVNRepositoryURLEntity();
		url.setName(name);
		url.setOrganization(organization);
		url = getDao().merge(url);
		url = getDao().find(SVNRepositoryURLEntity.class, url.getId());
		organization.getSvnRepositoryURLs().add(url);		
		organization = getDao().merge(organization);
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
		
		gu = getDao().merge(gu); 	
		user = getDao().merge(user);
		group = getDao().merge(group);
		
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
			
			organizationUser = getDao().merge(organizationUser);
			
//			user.getOrganizationUsers().add(organizationUser);
//			organization.getOrganizationUsers().add(organizationUser);
			
			user = getDao().merge(user);
			organization = getDao().merge(organization);
		}
	}
	
	public PermissionEntity createPermission(Class<?> implementedType, String resource, ISecurityEntity assignedTo, String parameters) {
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
		return getDao().merge(p);
	}
}
