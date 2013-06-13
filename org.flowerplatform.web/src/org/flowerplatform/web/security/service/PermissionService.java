package org.flowerplatform.web.security.service;

import java.lang.reflect.Constructor;
import java.security.Permission;
import java.security.Policy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.regex.Matcher;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.service.ServiceRegistry;
import org.flowerplatform.web.FlowerWebProperties.AddBooleanProperty;
import org.flowerplatform.web.WebPlugin;
import org.flowerplatform.web.entity.Entity;
import org.flowerplatform.web.entity.EntityFactory;
import org.flowerplatform.web.entity.Group;
import org.flowerplatform.web.entity.ISecurityEntity;
import org.flowerplatform.web.entity.PermissionEntity;
import org.flowerplatform.web.entity.dao.Dao;
import org.flowerplatform.web.security.dto.PermissionAdminUIDto;
import org.flowerplatform.web.security.dto.PermissionsByResourceFilter;
import org.flowerplatform.web.security.permission.AdminSecurityEntitiesPermission;
import org.flowerplatform.web.security.permission.PermissionDescriptor;
import org.flowerplatform.web.security.sandbox.FlowerWebPolicy;
import org.flowerplatform.web.security.sandbox.SecurityEntityAdaptor;
import org.flowerplatform.web.security.sandbox.SecurityUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service used to make CRUD operations on <code>Permission</code> entity.
 * 
 * @see BootstrapService#initialize()
 * @see ServiceRegistry
 * 
 * @author Cristi
 * @author Cristina
 * @author Mariana
 
 * @flowerModelElementId _fLaqAFczEeG6S8FiFZ8nVA
 */
public class PermissionService {
	
	public static final String SERVICE_ID = "permissionService";
	
	private static final Logger logger = LoggerFactory.getLogger(PermissionService.class);
	
	private static final String SHOW_ALL_APPLICABLE_PERMISSIONS_PER_FILTERED_RESOURCE = "users.permissions.showAllApplicablePermissionsPerFilteredResource";
	
	static {
		WebPlugin.getInstance().getFlowerWebProperties().addProperty(new AddBooleanProperty(SHOW_ALL_APPLICABLE_PERMISSIONS_PER_FILTERED_RESOURCE, "true"));
	}
	
	public static PermissionService getInstance() {	
		return (PermissionService) CommunicationPlugin.getInstance().getServiceRegistry().getService(SERVICE_ID);
	}
	
	/**
	 * @flowerModelElementId _QT2JUF34EeGwLIVyv_iqEg
	 */
	public Dao getDao() {
		return WebPlugin.getInstance().getDao();
	}
	
	/**
	 * Converts a {@link Group} to {@link PermissionAdminUIDto}.
	 * 
	 * @see #findAllAsAdminUIDto()
	 * @see #findByIdAsAdminUIDto()
	 * @flowerModelElementId _QT2JUl34EeGwLIVyv_iqEg
	 */
	private PermissionAdminUIDto convertPermissionToPermissionAdminUIDto(PermissionEntity permission) {
		PermissionAdminUIDto dto = new PermissionAdminUIDto();
				
		dto.setId(permission.getId());
		dto.setName(permission.getName());
		dto.setActions(permission.getActions());
		dto.setAssignedTo(permission.getAssignedTo());
		dto.setType(permission.getType());
		dto.setIsEditable(true);
		
		return dto;
	}
	
	/**
	 * Finds the {@link PermissionEntity permission} given by its id and returns a {@link PermissionAdminUIDto}. 
	 * @flowerModelElementId _QT2JVl34EeGwLIVyv_iqEg
	 */
	public PermissionAdminUIDto findByIdAsAdminUIDto(long id) {
		logger.debug("Find permission with id = {}", id);
		
		PermissionEntity permission = getDao().find(PermissionEntity.class, id);
		if (permission == null)
			throw new RuntimeException(String.format("Permission with id=%s was not found in the DB.", id));
		return convertPermissionToPermissionAdminUIDto(permission);
	}
	
	/**
	 * Finds all {@link PermissionEntity}s and returns a list of their corresponding {@link PermissionAdminUIDto}.
	 * @flowerModelElementId _QT2wYl34EeGwLIVyv_iqEg
	 */
	public List<PermissionAdminUIDto> findAllAsAdminUIDto() { 
		logger.debug("Find all permissions");
		
		List<PermissionAdminUIDto> list = new ArrayList<PermissionAdminUIDto>();
		for (PermissionEntity permission : getDao().findAll(PermissionEntity.class)) {
			try {
				SecurityUtils.checkModifyTreePermission(permission);
				if (Class.forName(permission.getType()).equals(AdminSecurityEntitiesPermission.class))
					SecurityUtils.checkCurrentUserIsAdmin(null);
				list.add(convertPermissionToPermissionAdminUIDto(permission));
			} catch (Exception e) {}
		}
		return list;
	}
	
	/**
	 * Finds {@link PermissionEntity}s that correspond to given {@link PermissionsByResourceFilter} 
	 * and returns a list of their corresponding {@link PermissionAdminUIDto}.
	 * @flowerModelElementId _QT3Xcl34EeGwLIVyv_iqEg
	 */
	@SuppressWarnings("unchecked")
	public List<PermissionAdminUIDto> findAsAdminUIDtoFilterByResource(PermissionsByResourceFilter resourceFilter) {
		if (resourceFilter == null) {
			return findAllAsAdminUIDto();
		}				
		List<String> patterns = new ArrayList<String>();
		patterns.add(resourceFilter.getResource());
		// add filter by root/dir/* for root/dir
		if (!resourceFilter.getResource().endsWith("/*"))
			patterns.add(resourceFilter.getResource() + (resourceFilter.getResource().endsWith("/") ? "*" : "/*"));
		// add filter by *
		patterns.add("*");
		int fromIndex = 0;
		int index = resourceFilter.getResource().indexOf("/", fromIndex);
		while (index != -1) {			
			patterns.add(resourceFilter.getResource().substring(0, index) + "/*");
			fromIndex = index + 1;
			index = resourceFilter.getResource().indexOf("/", fromIndex);
		}
		
		Session session = getDao().getFactory().openSession();
		session.beginTransaction();
		List<PermissionEntity> permissionEntities;
		try {
			Query q = session.createQuery("SELECT e FROM PermissionEntity e WHERE e.name in :names ORDER by e.type, e.name");
			q.setParameter("names", patterns);
			q.setReadOnly(true);					
			permissionEntities = q.list();
		} finally {
			session.close();
		}
		
		boolean showAllApplicablePermissions = Boolean.valueOf(WebPlugin.getInstance().getFlowerWebProperties().getProperty(SHOW_ALL_APPLICABLE_PERMISSIONS_PER_FILTERED_RESOURCE));
		
		List<PermissionAdminUIDto> listDtos = new ArrayList<PermissionAdminUIDto>();
		for (PermissionEntity permission : permissionEntities) {
			boolean isEditable = false;
			try { 
				SecurityUtils.checkModifyTreePermission(permission);
				// permission check did not throw a security exception => permission is editable by the current user
				isEditable = true;
			} catch (SecurityException e) {
				// do nothing
			}
			
			// return this permission if it is editable OR if all applicable permissions must be displayed (even if they are not editable by the current user)
			if (isEditable || showAllApplicablePermissions) {
				PermissionAdminUIDto dto = convertPermissionToPermissionAdminUIDto(permission);
				dto.setIsEditable(isEditable);
				listDtos.add(dto);
			}
		}
		return listDtos;		
	}
	
	/**
	 * Validates the information found in {@link PermissionAdminUIDto dto} and
	 * creates/updates the {@link PermissionEntity}.
	 * 
	 * <p>
	 * The validation with return messages to client if data is invalid.
	 * 
	 * <p>
	 * Also return a special value in the map if the permission is a tree permission
	 * and the resource is a folder. The client will then ask the user if similar
	 * permissions (e.g. for root/org1 -> root/org1/*) should be modified as well.
	 * 
	 * @see #validDto()
	 * @flowerModelElementId _QT3-gl34EeGwLIVyv_iqEg
	 */
	public Map<String, String> mergeAdminUIDto(ServiceInvocationContext context, PermissionAdminUIDto dto) {
		logger.debug("Merge permission = {}", dto.getName());
		
		PermissionEntity permissionEntity;
		if (dto.getId() == 0) {
			permissionEntity = EntityFactory.eINSTANCE.createPermissionEntity();
		} else {
			permissionEntity = getDao().find(PermissionEntity.class, (long)dto.getId());
			SecurityUtils.checkModifyTreePermission(permissionEntity);
		}
		try {
			if (Class.forName(dto.getType()).equals(AdminSecurityEntitiesPermission.class)) {
				SecurityUtils.checkCurrentUserIsAdmin(String.format("Current user can not modify %s because he is not admin.", AdminSecurityEntitiesPermission.class.getSimpleName()));
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		permissionEntity.setName(dto.getName());
		permissionEntity.setAssignedTo(dto.getAssignedTo());
		permissionEntity.setActions(dto.getActions());
		permissionEntity.setType(dto.getType());				

		PermissionDescriptor descriptor = ((FlowerWebPolicy)Policy.getPolicy()).getPermissionDescriptor(permissionEntity.getType());
		Map<String, String> validationResults = descriptor.validate(createPermission(permissionEntity));
		String assignedTo = permissionEntity.getAssignedTo();
		if (!assignedTo.equals(PermissionEntity.ANY_ENTITY)) {
			String message = SecurityUtils.validateSecurityEntity(assignedTo);
			if (message != null) {
				validationResults.put(PermissionDescriptor.ASSIGNED_TO_FIELD, message);
			}
		}
		
		if (!validationResults.isEmpty()) {
			return validationResults;
		}		
		SecurityUtils.checkModifyTreePermission(permissionEntity);		
		getDao().merge(permissionEntity);
		
		Map<String, String> result = new HashMap<String, String>();
		
		// check if tree permission on folder
		if (descriptor.isTreePermission()) {
			if (isFolder(permissionEntity.getName())) {
				result.put("modifySimilarPermission", "");
			}
		}
		
		return result;
	}
	
	/**
	 * Deletes all {@link PermissionEntity}s based on the list of their ids.
	 * 
	 * <p>
	 * Returns a list of boolean values: for each deleted permission, return
	 * <code>true</code> if the permission is a tree permission and the resource
	 * is a folder. The client will then ask the user if similar permissions 
	 * (e.g. for root/org1 -> root/org1/*) should be deleted as well.
	 * 
	 * @flowerModelElementId _QUEy0V34EeGwLIVyv_iqEg
	 */
	public List<Boolean> delete(List<Integer> ids) {
		List<Boolean> result = new ArrayList<Boolean>();
		for (Integer id : ids) {
			PermissionEntity permissionEntity = getDao().find(PermissionEntity.class, Long.valueOf(id));
			SecurityUtils.checkModifyTreePermission(permissionEntity);
			try {
				if (Class.forName(permissionEntity.getType()).equals(AdminSecurityEntitiesPermission.class)) {
					SecurityUtils.checkCurrentUserIsAdmin(String.format("Current user can not delete %s because he is not admin.", AdminSecurityEntitiesPermission.class.getSimpleName()));
				}
				PermissionDescriptor descriptor = ((FlowerWebPolicy)Policy.getPolicy()).getPermissionDescriptor(permissionEntity.getType());
				if (descriptor.isTreePermission()) {
					result.add(isFolder(permissionEntity.getName()));
				}
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
			
			logger.debug("Delete {}", permissionEntity);
			
			getDao().delete(permissionEntity);
		}		
		return result;
	}
	
	private boolean isFolder(String path) {
		path = path.split("/\\*")[0];
		IResource resource;
		try {
			resource = ResourcesPlugin.getWorkspace().getRoot().getFolder(new Path(path));
		} catch (IllegalArgumentException e) {
			return false;
		}
		return resource.exists();
	}
	
	public List<PermissionEntity> findPermissionsByType(String type) {
		Session session = getDao().getFactory().openSession();
		try {
			session.beginTransaction();
			Query q = session.createQuery("SELECT p FROM PermissionEntity p WHERE p.type = :type ORDER BY p.name");
			q.setParameter("type", type);
			return q.list();
		} finally {
			session.close();
		}
	}
	
	public void onSecurityEntityDelete(ISecurityEntity securityEntity) {
		Session session = getDao().getFactory().openSession();
		try {
			session.getTransaction().begin();	
			
			// delete permissions assigned to deleted entity
			String assignedTo = SecurityEntityAdaptor.getAssignedTo(securityEntity);
			Query query = session.createQuery("DELETE FROM PermissionEntity p WHERE p.assignedTo = :assigned_to");
			query.setParameter("assigned_to", assignedTo);
			query.executeUpdate();
			
			// delete/edit permissions where actions contain deleted entity
			String actions = SecurityEntityAdaptor.getAssignedTo(securityEntity);
			Query q = session.createQuery("SELECT p FROM PermissionEntity p WHERE p.actions LIKE :actions");
			q.setParameter("actions", "%" + actions + "%");
			List<PermissionEntity> list = q.list();
				
			for (PermissionEntity permission : list) {
				if (permission.getActions().equals(actions)) {
					// delete permission
					session.delete(permission);
				} else {
					// update actions
					List<ISecurityEntity> assignableEntities = SecurityEntityAdaptor.csvStringToSecurityEntityList(permission.getActions());
					for (Iterator<ISecurityEntity> it = assignableEntities.iterator(); it.hasNext();) {
						Entity entity = (Entity) it.next();
						if (entity != null) {
							if (entity.getId() == ((Entity) securityEntity).getId()) {
								it.remove();
								break;
							}
						} else {
							it.remove();
						}
					}
					Set<String> names = new HashSet<String>();
					for (ISecurityEntity entity : assignableEntities) {
						names.add(SecurityEntityAdaptor.getAssignedTo(entity));
					}
					permission.setActions(SecurityEntityAdaptor.toCsvString(names));
					
					session.merge(permission);
				}
			}
			
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw new RuntimeException(e);
		} finally {
			session.close();
		}
	}
	
	public void onSecurityEntityUpdate(ISecurityEntity initialEntity, ISecurityEntity newEntity) {
		String initialAssignedTo = SecurityEntityAdaptor.getAssignedTo(initialEntity);
		String newAssignedTo = SecurityEntityAdaptor.getAssignedTo(newEntity);
		
		if (!initialAssignedTo.equals(newAssignedTo)) {
			Session session = getDao().getFactory().openSession();
			try {
				session.getTransaction().begin();
				
				// update permissions assigned to entity
				Query query = session.createQuery("UPDATE PermissionEntity " +
											 "SET assignedTo = :newAssignedTo " + 
											 "WHERE assignedTo = :initialAssignedTo");
				query.setParameter("newAssignedTo", newAssignedTo);
				query.setParameter("initialAssignedTo", initialAssignedTo);
				query.executeUpdate();
				
				// update permissions where actions contain entity
				Query q = session.createQuery("SELECT p FROM PermissionEntity p WHERE p.actions LIKE :actions");
				q.setParameter("actions", "%" + initialAssignedTo + "%");
				List<PermissionEntity> list = q.list();
				
				for (PermissionEntity permission : list) {
					permission.setActions(permission.getActions().replaceAll(Matcher.quoteReplacement(initialAssignedTo), Matcher.quoteReplacement(newAssignedTo)));
					session.merge(permission);
				}
				session.getTransaction().commit();
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw new RuntimeException(e);
			} finally {
				session.close();
			}
		}
	}
	
	public List<PermissionDescriptor> getPermissionDescriptors() {
		FlowerWebPolicy policy = (FlowerWebPolicy) Policy.getPolicy();
		return policy.getPermissionDescriptors();
	}
	
	/**
	 * Instantiates a {@link Permission} from the record saved in the database,
	 * i.e. in the {@link org.flowerplatform.web.entity.PermissionEntity}.
	 * 
	 */	
	@SuppressWarnings("unchecked")
	public Permission createPermission(org.flowerplatform.web.entity.PermissionEntity permission) {
		Permission instance = null;
		try {			
			Class<? extends Permission> permissionClass = (Class<? extends Permission>) Class.forName(permission.getType());
			Constructor<? extends Permission> constructor = permissionClass.getConstructor(String.class, String.class);
			instance = constructor.newInstance(permission.getName(), permission.getActions());			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
		return instance;
	}
	
	private Observer securityEntityObserver = new Observer() {
		@Override
		public void update(Observable o, Object arg) {				
			List<?> list = (List<?>) arg;
			if (list.get(0).equals(ServiceObservable.DELETE) && list.get(1) instanceof ISecurityEntity) {
				ISecurityEntity securityEntity = (ISecurityEntity) list.get(1);
				onSecurityEntityDelete(securityEntity);
			} else {
				if (list.get(0).equals(ServiceObservable.UPDATE) && list.get(1) instanceof ISecurityEntity && list.get(2) instanceof ISecurityEntity) {
					onSecurityEntityUpdate((ISecurityEntity) list.get(1), (ISecurityEntity) list.get(2));
				}
			}
		}
	}; 
	
	public Observer getSecurityEntityObserver() {
		return securityEntityObserver; 
	}
}