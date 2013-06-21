package org.flowerplatform.web.security.sandbox;

import static org.flowerplatform.web.entity.PermissionEntity.GROUP_PREFIX;
import static org.flowerplatform.web.entity.PermissionEntity.ORGANIZATION_PREFIX;
import static org.flowerplatform.web.entity.PermissionEntity.USER_PREFIX;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;

import org.flowerplatform.web.entity.EntityFactory;
import org.flowerplatform.web.entity.Group;
import org.flowerplatform.web.entity.ISecurityEntity;
import org.flowerplatform.web.entity.NamedEntity;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.PermissionEntity;
import org.flowerplatform.web.entity.User;
import org.flowerplatform.web.entity.dto.NamedDto;

/**
 * The responsibility of this class is to translate between different representations
 * of a {@link ISecurityEntity}.
 * 
 * @author Florin
 * @flowerModelElementId _Z-wekHgGEeGtTo1wOb4S9A
 */
public class SecurityEntityAdaptor {

	/**
	 * <code>assignedTo</code> is a comma separated list of security entity names.
	 * Each name is prefix with one of the symbols:
	 * <ul>
	 * <li># for organization</li>
	 * <li>@ for group</li>
	 * <li>$ for user</li>
	 * </ul>
	 * 
	 * @param assignedTo - a comma separated list of security entity names. 
	 * @return
	 * @flowerModelElementId _Z-xFoXgGEeGtTo1wOb4S9A
	 */
	public static List<ISecurityEntity> csvStringToSecurityEntityList(String assignedTo) {
		List<ISecurityEntity> securityEntities = new ArrayList<ISecurityEntity>();				
		String[] entityNames = assignedTo.split(",");
		for (String name: entityNames) {
			name = name.trim();
			securityEntities.add(toSecurityEntity(name, false));
		}
		return securityEntities;
	}
	
	/**
	 * If <code>eager</code> is true, will also fetch the associations for the entity. This is
	 * to avoid lazy initialization exception when the entity is accessed from the permissions cache.
	 * 
	 * @author Florin
	 * @author Mariana
	 */
	public static ISecurityEntity toSecurityEntity(final String assignedTo, final boolean eager) {
		DatabaseOperationWrapper wrapper = new DatabaseOperationWrapper(new DatabaseOperation() {
					
			@Override
			public void run() {
				if (assignedTo.startsWith(ORGANIZATION_PREFIX)) {
					List<Organization> orgs = wrapper.findByField(Organization.class, "name", assignedTo.substring(1));
					if (orgs.size() > 0) {
						Organization organization = orgs.get(0);
						if (eager) {
							organization.getOrganizationUsers().size();
							organization.getGroups().size();
						}
						wrapper.setOperationResult(organization);
						return;
					}
				} else if (assignedTo.startsWith(GROUP_PREFIX)) {
					// if @ALL group exists in database this is ok, if not this case should be handled.
					List<Group> groups = wrapper.findByField(Group.class, "name", assignedTo.substring(1));
					if (groups.size() > 0) {
						Group group = groups.get(0);
						if (eager) {
							group.getGroupUsers().size();
						}
						wrapper.setOperationResult(group);
						return;
					} else {
						// @ALL does not exist in the DB
						if (assignedTo.equals("@ALL")) {
							Group all = EntityFactory.eINSTANCE.createGroup();
							all.setName("ALL");
							wrapper.setOperationResult(all);
							return;
						}
					}
				} else if (assignedTo.startsWith(USER_PREFIX)) {
					List<User> users = wrapper.findByField(User.class, "login", assignedTo.substring(1));
					if (users.size() > 0) {
						User user = users.get(0);
						if (eager) {
							user.getOrganizationUsers().size();
							user.getGroupUsers().size();
						}
						wrapper.setOperationResult(user);
						return;
					}
				}
			}
		});
		return (ISecurityEntity) wrapper.getOperationResult();
	}
	
	/**
	 * Return a csv list of entity names (with proper prefix).
	 * 
	 * @author Florin
	 * @author Mariana
	 *  
	 * @flowerModelElementId _Z-y60XgGEeGtTo1wOb4S9A
	 */
	public static String toCsvString(List<? extends NamedEntity> entities, Collection<? extends NamedDto> dtos, String prefix) {
		//TODO: bug - here should be a Collection<GroupAdminDto>. But this collection contains NamedDtos. 
		Set<String>names = new HashSet<String>();
		if (entities != null) {
			for (NamedEntity e: entities) {
				names.add(prefix + e.getName());
			}
		}
		if (dtos != null) {
			for (NamedDto dto: dtos) {
				names.add(prefix + dto.getName());
			}
		}
		
		return toCsvString(names);
	}
	
	/**
	 * @flowerModelElementId _Z-1-I3gGEeGtTo1wOb4S9A
	 */
	public static String toCsvString(Organization org, NamedDto orgDto) {
		Set<String> organizationNames = new HashSet<String>();
		if (org != null) {
			organizationNames.add(ORGANIZATION_PREFIX + org.getName());
		} 
		if (orgDto != null) {
			organizationNames.add(ORGANIZATION_PREFIX + orgDto.getName());
		} 
		return toCsvString(organizationNames);
	}
	
	public static String toCsvString(Set<String> names) {
		StringBuilder sb = new StringBuilder();
		for (String s: names) {
			sb.append(s);
			sb.append(",");
		}
		
		if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ',') {
			sb.replace(sb.length() - 1, sb.length(), "");
		}
		
		return sb.toString();
	}	
	
	/**
	 * @flowerModelElementId _Z-5BcHgGEeGtTo1wOb4S9A
	 */
	public static String getAssignedTo(ISecurityEntity securityEntity) {
		String assignedTo = "";
		if (securityEntity instanceof User) {			
			assignedTo = PermissionEntity.USER_PREFIX + ((User)securityEntity).getLogin();
		} else if (securityEntity instanceof Group) {
			assignedTo = PermissionEntity.GROUP_PREFIX + ((Group)securityEntity).getName();
		} else if (securityEntity instanceof Organization) {
			assignedTo = PermissionEntity.ORGANIZATION_PREFIX + ((Organization)securityEntity).getName();
		}
		return assignedTo;
	}
	
	public static final String ANONYMOUS = "anonymous";
	
	public static String getAnonymousUserLogin(Organization organization) {
		return ANONYMOUS + "." + organization.getName();
	}
}
