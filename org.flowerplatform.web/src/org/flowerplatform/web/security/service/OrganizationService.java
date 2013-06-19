package org.flowerplatform.web.security.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;
import org.flowerplatform.web.security.dto.OrganizationAdminUIDto;
import org.flowerplatform.web.security.sandbox.SecurityEntityAdaptor;
import org.flowerplatform.web.security.sandbox.SecurityUtils;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.service.ServiceRegistry;
import org.flowerplatform.web.WebPlugin;
import org.flowerplatform.web.entity.FavoriteItem;
import org.flowerplatform.web.entity.Group;
import org.flowerplatform.web.entity.NamedEntity;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.OrganizationMembershipStatus;
import org.flowerplatform.web.entity.OrganizationUser;
import org.flowerplatform.web.entity.RecentResource;
import org.flowerplatform.web.entity.SVNRepositoryURLEntity;
import org.flowerplatform.web.entity.User;
import org.flowerplatform.web.entity.dto.NamedDto;
import org.flowerplatform.web.explorer.RootChildrenProvider;

/**
 * Service used to make CRUD operations on <code>Organization</code> entity.
 * 
 * @see BootstrapService#initialize()
 * @see ServiceRegistry
 * 
 * @author Cristi
 * @author Cristina
 * @author Mariana
 * 
 * @flowerModelElementId _Yyg9UFcqEeG6S8FiFZ8nVA
 */
public class OrganizationService extends ServiceObservable {
	
	public static final String SERVICE_ID = "organizationService";
	
	private static final Logger logger = LoggerFactory.getLogger(OrganizationService.class);
	
	public static OrganizationService getInstance() {	
		return (OrganizationService) CommunicationPlugin.getInstance().getServiceRegistry().getService(SERVICE_ID);
	}
	
	/**
	 * Converts an {@link Organization} to {@link OrganizationAdminUIDto}.
	 * 
	 * @see #findByIdAsAdminUIDto()
	 * 
	 * @flowerModelElementId __rSEEFcyEeG6S8FiFZ8nVA
	 */
	public OrganizationAdminUIDto convertOrganizationToOrganizationAdminUIDto(Organization organization, User user) {	
		OrganizationAdminUIDto orgAdminUIDto = new OrganizationAdminUIDto();
		
		orgAdminUIDto.setId(organization.getId());
		orgAdminUIDto.setName(organization.getName());
		orgAdminUIDto.setURL(organization.getURL());
		orgAdminUIDto.setLabel(organization.getLabel());
		orgAdminUIDto.setLogoURL(organization.getLogoURL());
		orgAdminUIDto.setIconURL(organization.getIconURL());
		orgAdminUIDto.setActivated(organization.isActivated());
		orgAdminUIDto.setProjectsCount(organization.getProjectsCount());
		orgAdminUIDto.setFilesCount(organization.getFilesCount());
		orgAdminUIDto.setModelsCount(organization.getModelsCount());
		orgAdminUIDto.setDiagramsCount(organization.getDiagramsCount());
		if (user != null) {
			orgAdminUIDto.setStatus(getOrganizationMembershipStatus(organization, user));
		}
		List<NamedDto> list = new ArrayList<NamedDto>();
		for (NamedEntity url : organization.getSvnRepositoryURLs()) {
			list.add(new NamedDto(url.getId(), url.getName()));
		}
		orgAdminUIDto.setSVNRepositoryURLs(list);
		
		list = new ArrayList<NamedDto>();
		for (Group group : organization.getGroups()) {
			list.add(new NamedDto(group.getId(), group.getName()));
		}
		orgAdminUIDto.setGroups(list);
	
		return orgAdminUIDto;
	}

	public OrganizationMembershipStatus getOrganizationMembershipStatus(Organization organization, User user) {
		if (organization.getOrganizationUsers() != null) {
			for (OrganizationUser ou : organization.getOrganizationUsers()) {
				if (ou.getUser().getId() == user.getId()) {
					return ou.getStatus();
				}
			}
		}
		// not a member of organization
		return null;
	}

	/**
	 * Finds the {@link Organization} given by its id and returns an {@link OrganizationAdminUIDto}. 
	 * @flowerModelElementId _QTxQ0134EeGwLIVyv_iqEg
	 */
	public OrganizationAdminUIDto findByIdAsAdminUIDto(final ServiceInvocationContext context, final long id) {
		logger.debug("Find organization with id = {}", id);
		DatabaseOperationWrapper wrapper = new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				Organization org = wrapper.find(Organization.class, id);
				if (org == null)
					throw new RuntimeException(String.format("Organization with id=%s was not found in the DB.", id));
				try {
					SecurityUtils.checkAdminSecurityEntitiesPermission(SecurityEntityAdaptor.toCsvString(org, null));
				} catch (SecurityException e) {
					throw new RuntimeException(String.format("Organization with id=%s was not available.", id));
				}
				User user = (User) context.getCommunicationChannel().getPrincipal().getUser();
				wrapper.setOperationResult(convertOrganizationToOrganizationAdminUIDto(org, user));
			}
		});
		return (OrganizationAdminUIDto) wrapper.getOperationResult();
	}
	
	/**
	 * Called from client side to request the current organization filter.
	 */
	public OrganizationAdminUIDto findByNameAsAdminUIDto(final String name) {
		logger.debug("Find organization with name = {}", name);
		DatabaseOperationWrapper wrapper = new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				List<Organization> rslt = wrapper.findByField(Organization.class, "name", name);
				if (rslt.size() == 1) {
					wrapper.setOperationResult(convertOrganizationToOrganizationAdminUIDto(rslt.get(0), null));
				}
			}
		});
		return (OrganizationAdminUIDto) wrapper.getOperationResult();
	}
	
	/**
	 * Finds all {@link Organization}s and returns a list of their corresponding {@link OrganizationAdminUIDto}.
	 * 	
	 * @flowerModelElementId _OsC04FcwEeG6S8FiFZ8nVA
	 */
	public List<OrganizationAdminUIDto> findAllAsAdminUIDto(final ServiceInvocationContext context, final boolean getAll) {
		logger.debug("Find all organizations");
		final List<OrganizationAdminUIDto> list = new ArrayList<OrganizationAdminUIDto>();
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				User user = (User) context.getCommunicationChannel().getPrincipal().getUser();
				for (Organization org : wrapper.findAll(Organization.class)) {
					if (!getAll) {
						try {
							SecurityUtils.checkAdminSecurityEntitiesPermission(SecurityEntityAdaptor.toCsvString(org, null));
						} catch (SecurityException e) {
							continue;
						}
					} else {
						if (!org.isActivated()) {
							continue; // do not return organizations that are not activated
						}
						if (getOrganizationMembershipStatus(org, user) != null) {
							continue; // do not return organizations where the user is already a member
						}
					}
					
					list.add(convertOrganizationToOrganizationAdminUIDto(org, user));
				}
			}
		});
		return list;		
	}
	
	public List<OrganizationAdminUIDto> findMyOrganizationsAsADminUIDto(final long userId) {
		final List<OrganizationAdminUIDto> list = new ArrayList<OrganizationAdminUIDto>();
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				if (userId != 0) {
					User user =  wrapper.findByField(User.class, "id", userId).get(0);
					
					logger.debug("Find organizations where {} belongs", user);
					
					for (OrganizationUser ou : user.getOrganizationUsers()) {
						try {
							if (user.getId() != CommunicationPlugin.tlCurrentPrincipal.get().getUserId())
								SecurityUtils.checkAdminSecurityEntitiesPermission(SecurityEntityAdaptor.toCsvString(ou.getOrganization(), null));
							list.add(convertOrganizationToOrganizationAdminUIDto(ou.getOrganization(), user));
						} catch (Exception e) {
							// do nothing
						}
					}
				}
			}
		});
		return list;	
	}

	/**
	 * Finds all {@link Organization}s and returns a list of their corresponding {@link NamedDto}.
	 * 
	 * <p>
	 * Used for listboxes.
	 * 
	 * @flowerModelElementId _QTye8F34EeGwLIVyv_iqEg
	 */
	public List<NamedDto> findAllAsNamedDto() {
		final List<NamedDto> list = new ArrayList<NamedDto>();
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				for (Organization org : wrapper.findAll(Organization.class)) {
					try {
						SecurityUtils.checkAdminSecurityEntitiesPermission(SecurityEntityAdaptor.toCsvString(org, null));
					} catch (SecurityException e) {
						continue;
					}
					list.add(new NamedDto(org.getId(), org.getName()));
				}
			}
		});
		
		return list;	
	}
	
	/**
	 * Creates/Updates the {@link Organization} based on {@link OrganizationAdminUIDto} stored information.
	 * Returns an error message (if there is already an organization with the same name, or <code>null</code>
	 * if there were no errors.
	 * 
	 * @flowerModelElementId _QTye8134EeGwLIVyv_iqEg
	 */
	public String mergeAdminUIDto(ServiceInvocationContext context, OrganizationAdminUIDto dto) {
		return mergeAdminUIDto(context, dto, false);
	}
	
	String mergeAdminUIDto(ServiceInvocationContext context, final OrganizationAdminUIDto dto, final boolean requestMode) {
		logger.debug("Merge organization = {}", dto.getName());
		DatabaseOperationWrapper wrapper = new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				// first check if there is already an organization with the same name
				List<Organization> orgsWithSameName = wrapper.findByField(Organization.class, "name", dto.getName());
				if (dto.getId() == 0 && orgsWithSameName != null && orgsWithSameName.size() > 0) {
					wrapper.setOperationResult("There is already an organization with this name!");
					return;
				}
				
				Organization initialOrg = wrapper.find(Organization.class, dto.getId());
				
				// normal users are only allowed to add new organizations in request mode
				if (!requestMode)
					SecurityUtils.checkAdminSecurityEntitiesPermission(SecurityEntityAdaptor.toCsvString(initialOrg, dto));
				
				Organization org = wrapper.mergeDto(Organization.class, dto);
				org.setName(dto.getName());
				org.setURL(dto.getURL());
				org.setLabel(dto.getLabel());
				org.setLogoURL(dto.getLogoURL());
				org.setIconURL(dto.getIconURL());
				org.setActivated(dto.isActivated());
				
				org = wrapper.merge(org);
				observable.notifyObservers(Arrays.asList(UPDATE, initialOrg, org));
			}
		});
		
		return (String) wrapper.getOperationResult();
	}
	
	/**
	 * Deletes all {@link Organization}s based on the list of their ids.
	 * @flowerModelElementId _QTzGAl34EeGwLIVyv_iqEg
	 */
	public void delete(final List<Integer> ids) {
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				for (Integer id : ids) {
					Organization org = wrapper.find(Organization.class, Long.valueOf(id));
					SecurityUtils.checkAdminSecurityEntitiesPermission(SecurityEntityAdaptor.toCsvString(org, null));
					
					logger.debug("Delete {}", org);
					
					// this will also remove the Organization from the Group
					org.getGroups().clear(); 
					
					for (Iterator<OrganizationUser> it = org.getOrganizationUsers().iterator(); it.hasNext();) {
						OrganizationUser ou = it.next();
						// manually remove the OrganizationUser from the User
						ou.getUser().getOrganizationUsers().remove(ou);
						it.remove();
						// and delete the OrganizationUser
						wrapper.delete(ou);
					}
					
					Query recentResources = wrapper.createQuery("SELECT e FROM RecentResource e WHERE e.organization = :organization");
					recentResources.setParameter("organization", org);
					for (Object object : recentResources.list()) {
						RecentResource rr = (RecentResource) object;
						rr.setOrganization(null);
					}
					
					Query favoriteItems = wrapper.createQuery("SELECT e FROM FavoriteItem e WHERE e.organization = :organization");
					favoriteItems.setParameter("organization", org);
					for (Object object : favoriteItems.list()) {
						FavoriteItem fav = (FavoriteItem) object;
						wrapper.delete(fav);
					}
					
					wrapper.delete(org);
					observable.notifyObservers(Arrays.asList(DELETE, org));
					
				}
			}
		});
	}
	
	/**
	 * Returns the default path for one of the {@link Organization}s where the current user belongs.
	 * 
	 * @author Mariana
	 */
	public String getOrganizationDefaultPath(List<String> organizationsFilter) {
		List<OrganizationAdminUIDto> organizations = findMyOrganizationsAsADminUIDto(CommunicationPlugin.tlCurrentPrincipal.get().getUserId());
		for (OrganizationAdminUIDto organization : organizations) {
			if (organizationsFilter.contains(organization.getName()) || organizationsFilter.size() == 0) {
				String organizationDir = WebPlugin.getInstance().getFlowerWebProperties().getProperty(UserService.ORGANIZATION_DIRECTORIES).split(",\\s*")[0];
				organizationDir = MessageFormat.format(organizationDir, organization.getName());
				organizationDir += "/" + WebPlugin.getInstance().getFlowerWebProperties().getProperty(UserService.ORGANIZATION_DEFAULT_DIRECTORY);
				return organizationDir;
			}
		}
		return null;
	}
	
	public Organization getOrganizationForResource(String resourcePath) {
		String[] organizationDirectories = WebPlugin.getInstance().getFlowerWebProperties().getProperty(UserService.ORGANIZATION_DIRECTORIES).split(",\\s*");
		for (String dir : organizationDirectories) {
			dir = RootChildrenProvider.getWorkspaceRoot().getPath() + "/" + dir;
			// append a / if the dir doesn't end in /
			if (dir.lastIndexOf("/") != dir.length() - 1) {
				dir += "/";
			}
			dir = dir.replace(Matcher.quoteReplacement("{0}"), Matcher.quoteReplacement("(.*?)"));
			Matcher matcher = Pattern.compile(dir).matcher(resourcePath);
			matcher.find();
			try {
				final String organizationName = matcher.group(1);
				DatabaseOperationWrapper wrapper = new DatabaseOperationWrapper(new DatabaseOperation() {
					
					@Override
					public void run() {
						List<Organization> orgs = wrapper.findByField(Organization.class, "name", organizationName);
						if (orgs.size() > 0) {
							wrapper.setOperationResult(orgs.get(0));
						}
					}
				});
				return (Organization) wrapper.getOperationResult();
			} catch (IllegalStateException e) {
				// do nothing
			}
		}
		
		return null;
	}
	
	public SVNRepositoryURLEntity findSVNRepositoryURLByName(Organization org, String name) {
		for (SVNRepositoryURLEntity url : org.getSvnRepositoryURLs()) {
			if (url.getName().endsWith(name)) {
				return url;
			}
		}
		return null;
	}
	
	/**
	 * Rename the anonymous user and groups belonging to the organization.
	 */
	private void onOrganizationUpdate(final Organization initialOrganization, final Organization newOrganization) {
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				String initialName = initialOrganization.getName();
				String newName  = newOrganization.getName();
				if (!initialName.equals(newName)) {
					List<User> list = wrapper.findByField(User.class, "login", SecurityEntityAdaptor.getAnonymousUserLogin(initialOrganization));
					if (list.size() > 0) {
						User user = list.get(0);
						User initialUser = wrapper.find(User.class, user.getId());
						user.setLogin(user.getLogin().replace(initialName, newName));
						user.setHashedPassword(Util.encrypt(user.getLogin()));
						user = wrapper.merge(user);
						observable.notifyObservers(Arrays.asList(UPDATE, initialUser, user));
					}
					
					for (Group group : wrapper.findAll(Group.class)) {
						if (group.getOrganization() != null && group.getOrganization().getId() == initialOrganization.getId()) {
							Group initialGroup = wrapper.find(Group.class, group.getId());
							group.setName(group.getName().replace(initialName, newName));
							group = wrapper.merge(group);
							observable.notifyObservers(Arrays.asList(UPDATE, initialGroup, group));
						}
					}
				}
			}
		});
		
	}
	
	/**
	 * Delete the anonymous user belonging to the organization.
	 */
	private void onOrganizationDelete(final Organization organization) {
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				List<User> list = wrapper.findByField(User.class, "login", SecurityEntityAdaptor.getAnonymousUserLogin(organization));
				if (list.size() > 0) {
					wrapper.delete(list.get(0));
					observable.notifyObservers(Arrays.asList(DELETE, list.get(0)));
				}
			}
		});
	}
	
	private Observer organizationObserver = new Observer() {
		
		@Override
		public void update(Observable o, Object arg) {
			List<?> list = (List<?>) arg;
			if (list.get(0).equals(ServiceObservable.UPDATE) && list.get(1) instanceof Organization && list.get(2) instanceof Organization) {
				onOrganizationUpdate((Organization) list.get(1), (Organization) list.get(2));
			} else {
				if (list.get(0).equals(ServiceObservable.DELETE) && list.get(1) instanceof Organization) {
					onOrganizationDelete((Organization) list.get(1));
				}
			}
		}
	};
	
	public Observer getOrganizationObserver() {
		return organizationObserver; 
	}
}