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
package org.flowerplatform.web.security.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;
import org.flowerplatform.web.entity.Group;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.PermissionEntity;
import org.flowerplatform.web.entity.dto.NamedDto;
import org.flowerplatform.web.security.dto.GroupAdminUIDto;
import org.flowerplatform.web.security.dto.OrganizationAdminUIDto;
import org.flowerplatform.web.security.sandbox.SecurityEntityAdaptor;
import org.flowerplatform.web.security.sandbox.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service used to make CRUD operations on <code>Group</code> entity.
 * 
 * @author Cristi
 * @author Cristina
 * @author Mariana
 * 
 * @flowerModelElementId _QTrxQF34EeGwLIVyv_iqEg
 */
public class GroupService extends ServiceObservable {
	
	public static final String SERVICE_ID = "groupService";
	
	private static final Logger logger = LoggerFactory.getLogger(GroupService.class);
	
	public static GroupService getInstance() {	
		return (GroupService) CommunicationPlugin.getInstance().getServiceRegistry().getService(SERVICE_ID);
	}
	
	/**
	 * Converts a {@link Group} to {@link GroupAdminUIDto}.
	 * 
	 * @see #findAllAsAdminUIDto()
	 * @see #findByIdAsAdminUIDto(long)
	 * @flowerModelElementId _QTsYUl34EeGwLIVyv_iqEg
	 */
	private GroupAdminUIDto convertGroupToGroupAdminUIDto(Group group) {
		GroupAdminUIDto dto = new GroupAdminUIDto();
		
		dto.setId(group.getId());
		dto.setName(group.getName());
		if (group.getOrganization() != null) {
			dto.setOrganization(new NamedDto(group.getOrganization().getId(), group.getOrganization().getName()));
			dto.setOrganizationLabel(group.getOrganization().getLabel());
		} else {
			dto.setOrganizationLabel(""); // this is to avoid problems on flex
		}
		return dto;
	}
	
	/**
	 * Finds the {@link Group group} given by its id and returns a {@link GroupAdminUIDto}. 
	 * @flowerModelElementId _QTsYVl34EeGwLIVyv_iqEg
	 */
	public GroupAdminUIDto findByIdAsAdminUIDto(final long id) {
		logger.debug("Find group with id = {}", id);
		DatabaseOperationWrapper wrapper = new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				Group group = wrapper.find(Group.class, id);		
				if (group == null)
					throw new RuntimeException(String.format("Group with id=%s was not found in the DB.", id));		
				try {
					SecurityUtils.checkAdminSecurityEntitiesPermission(PermissionEntity.GROUP_PREFIX + group.getName());
				} catch (SecurityException e) {
					throw new RuntimeException(String.format("Group with id=%s is not available.", id));
				}
				wrapper.setOperationResult(convertGroupToGroupAdminUIDto(group));
			}
		});
		return (GroupAdminUIDto) wrapper.getOperationResult();
	}
	
	/**
	 * Finds all {@link Group}s and returns a list of their corresponding {@link GroupAdminUIDto}.
	 * @flowerModelElementId _QTs_YV34EeGwLIVyv_iqEg
	 */
	public List<GroupAdminUIDto> findAllAsAdminUIDto() {
		logger.debug("Find all groups");
		final List<GroupAdminUIDto> list = new ArrayList<GroupAdminUIDto>();
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				for (Group group : wrapper.findAll(Group.class)) {
					try { 
						SecurityUtils.checkAdminSecurityEntitiesPermission(PermissionEntity.GROUP_PREFIX + group.getName());
					} catch (SecurityException e) {
						// we don't have permissions
						continue;
					}
					list.add(convertGroupToGroupAdminUIDto(group));
				}
			}
		});
		return list;	
	}
	
	/**
	 * Finds all {@link Group}s and returns a list of their corresponding {@link NamedDto}.
	 * 
	 * <p>
	 * Used for listboxes.
	 * 
	 * @flowerModelElementId _QTs_ZF34EeGwLIVyv_iqEg
	 */
	public List<NamedDto> findAllAsNamedDto(final List<OrganizationAdminUIDto> filter) {
		final List<NamedDto> list = new ArrayList<NamedDto>();
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				for (Group group : wrapper.findAll(Group.class)) {
					try { 
						SecurityUtils.checkAdminSecurityEntitiesPermission(PermissionEntity.GROUP_PREFIX + group.getName());
					} catch (SecurityException e) {
						// we don't have permissions
						continue;
					}
					
					// return only groups without organizations or in the organizations in the filter list
					if (group.getOrganization() != null) {
						for (OrganizationAdminUIDto organization : filter) {
							if (organization.getId() == group.getOrganization().getId()) {
								list.add(new NamedDto(group.getId(), group.getName()));
								break;
							}
						}
					} else {
						list.add(new NamedDto(group.getId(), group.getName()));
					}
				}
			}
		});
		
		return list;	
	}
	
	/**
	 * Creates/Updates the {@link Group} based on {@link GroupAdminUIDto} stored information.
	 * @flowerModelElementId _QTtmcl34EeGwLIVyv_iqEg
	 */
	public String mergeAdminUIDto(ServiceInvocationContext context, final GroupAdminUIDto dto) {
		logger.debug("Merge group = {}", dto.getName());
		
		Group initialGroup = (Group) new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				wrapper.setOperationResult(wrapper.find(Group.class, dto.getId()));
			}
		}).getOperationResult();
		
		DatabaseOperationWrapper wrapper = new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				Group group = wrapper.mergeDto(Group.class, dto);
				
				// check initial organization and new organization separately
				// reason: if the user has permissions over only one of them, the check will go through
				// and then the user can either change the organization to one where the user does not have permissions,
				// or he can change a group of another organization
				
				// check initial organization, if this is an existing group
				String ownerEntities = SecurityEntityAdaptor.toCsvString(group.getOrganization(), null);
				if (group.getId() != 0) {
					if (group.getOrganization() == null) {
						ownerEntities = PermissionEntity.ANY_ENTITY;
					}
					if (ownerEntities.trim().equals("")) {
						ownerEntities = PermissionEntity.ANY_ENTITY;
					}
					SecurityUtils.checkAdminSecurityEntitiesPermission(ownerEntities);
				}
				
				// check new organization
				ownerEntities = SecurityEntityAdaptor.toCsvString(null, dto.getOrganization());
				if (dto.getOrganization() == null) {
					ownerEntities = PermissionEntity.ANY_ENTITY;
				}
				if (ownerEntities.trim().equals("")) {
					ownerEntities = PermissionEntity.ANY_ENTITY;
				}
				SecurityUtils.checkAdminSecurityEntitiesPermission(ownerEntities);
				
				// check if there is already a group with the same name
				List<Group> groupsWithSameName = wrapper.findByField(Group.class, "name", dto.getName());
				if (dto.getId() == 0 && groupsWithSameName != null && groupsWithSameName.size() > 0) {
					wrapper.setOperationResult("There is already a group with the same name!");
					return;
				}
				
				group = (Group) wrapper.merge(group);
				// set organization
				Organization org = group.getOrganization();
				if (dto.getOrganization() != null) {
					org = wrapper.find(Organization.class, dto.getOrganization().getId());			
					group.setOrganization(org);			
				} else {			
					group.setOrganization(null);
				}
			}
		});
		
		Group newGroup = (Group) new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				wrapper.setOperationResult(wrapper.find(Group.class, dto.getId()));
			}
		}).getOperationResult();
		observable.notifyObservers(Arrays.asList(UPDATE, initialGroup, newGroup));
		return (String) wrapper.getOperationResult();
	}
	
	/**
	 * Deletes all {@link Group}s based on the list of their ids.
	 * @flowerModelElementId _QTuNgl34EeGwLIVyv_iqEg
	 */
	public void delete(final List<Integer> ids) {
		for (final Integer id : ids) {
			DatabaseOperationWrapper wrapper = new DatabaseOperationWrapper(new DatabaseOperation() {
				
				@Override
				public void run() {
					Group group = wrapper.find(Group.class, Long.valueOf(id));
					
					logger.debug("Delete {}", group);
					
					String owners;
					if (group.getOrganization() == null) {
						owners = PermissionEntity.ANY_ENTITY;
					} else {
						owners = SecurityEntityAdaptor.toCsvString(group.getOrganization(), null);
					}
					SecurityUtils.checkAdminSecurityEntitiesPermission(owners);
					// update organization list of groups
					if (group.getOrganization() != null) {
						group.getOrganization().getGroups().remove(group);
					}
					// all GroupUsers will be deleted automatically 
					wrapper.delete(group);
					wrapper.setOperationResult(group);
				} 
			});
			observable.notifyObservers(Arrays.asList(DELETE, wrapper.getOperationResult()));
		}
	}	
	
}