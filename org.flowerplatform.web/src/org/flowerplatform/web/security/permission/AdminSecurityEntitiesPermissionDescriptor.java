package org.flowerplatform.web.security.permission;

import java.security.Permission;
import java.util.HashMap;
import java.util.Map;

import org.flowerplatform.web.security.sandbox.SecurityEntityAdaptor;

import org.flowerplatform.web.WebPlugin;
import org.flowerplatform.web.entity.Group;
import org.flowerplatform.web.entity.ISecurityEntity;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.PermissionEntity;


/**
 * Descriptor for {@link ModifyTreePermissionsPermission}. See the doc of this
 * class for additional info.
 * 
 * @author Florin
 * @flowerModelElementId _Z9XXcHgGEeGtTo1wOb4S9A
 */
public class AdminSecurityEntitiesPermissionDescriptor extends PermissionDescriptor {

	/**
	 * @flowerModelElementId _Z9X-gXgGEeGtTo1wOb4S9A
	 */
	@Override
	public Class<? extends Permission> getHandledPermissionType() {
		return AdminSecurityEntitiesPermission.class;
	}

	/**
	 * @flowerModelElementId _Z9aawHgGEeGtTo1wOb4S9A
	 */
	@Override
	public Class<? extends Permission> getImplementedPermissionType() {
		return AdminSecurityEntitiesPermission.class;
	} 

	@Override
	public String getSimpleName() {
		return WebPlugin.getInstance().getMessage("entity.permission.simpleName.adminSecurityEntitiesPermission");
	}

	@Override
	public int getOrder() {
		return 20;
	}

	@Override
	public Map<String, String> getHints() {
		Map<String, String> map = super.getHints();
		map.put(TYPE_FIELD, WebPlugin.getInstance().getMessage("entity.permission.adminSecurityEntitiesPermission.type.hint"));
		map.put(NAME_FIELD, WebPlugin.getInstance().getMessage("entity.permission.adminSecurityEntitiesPermission.resource.hint"));
		map.put(ACTIONS_FIELD, WebPlugin.getInstance().getMessage("entity.permission.adminSecurityEntitiesPermission.actions.hint", getActions()));
		return map;
	}

	/**
	 * @flowerModelElementId _Z9bo4XgGEeGtTo1wOb4S9A
	 */
	@Override
	public boolean isTreePermission() {
		return false;
	}

	/**
	 * @flowerModelElementId _ScsCYIIZEeGPwv1h63g-uQ
	 */
	@Override
	public Map<String, String> validate(Permission permission) {
		if (!(permission instanceof AdminSecurityEntitiesPermission)) {
			throw new RuntimeException("Can handle only AdminSecurityEntitiesPermission");
		}
		Map<String, String> validationResults = new HashMap<String, String>();
		
		if (!permission.getName().equals("")) {
			validationResults.put(NAME_FIELD, WebPlugin.getInstance().getMessage("entity.permission.validation.mustBeEmpty"));
		}

		// check actions
		StringBuilder noPrefix = new StringBuilder();
		StringBuilder notFoundOrganizations = new StringBuilder();
		StringBuilder notFoundGroups = new StringBuilder();
		if (!permission.getActions().equals(PermissionEntity.ANY_ENTITY)) {		
			String[] entityNames = permission.getActions().split(",");
			for (String name: entityNames) {				
				ISecurityEntity entity = SecurityEntityAdaptor.toSecurityEntity(name.trim());
				if (entity != null) {
					// only groups and organizations are allowed here
					if (!(entity instanceof Organization) && !(entity instanceof Group)) {
						appendToMessage(noPrefix, name);
					}
				} else {
					// organization doesn't exist
					if (name.startsWith(PermissionEntity.ORGANIZATION_PREFIX)) {
						appendToMessage(notFoundOrganizations, name);
					} else {
						// group doesn't exist
						if (name.startsWith(PermissionEntity.GROUP_PREFIX)) {
							appendToMessage(notFoundGroups, name);
						} else {
							// prefix is not correct
							appendToMessage(noPrefix, name);
						}
					}
				}
			}
		}
		
		String validationError = "";
		if (noPrefix.length() > 0) {
			validationError = WebPlugin.getInstance().getMessage("entity.permission.validation.invalidPrefix", noPrefix);
		}
		if (notFoundOrganizations.length() > 0) {
			validationError = WebPlugin.getInstance().getMessage("entity.permission.validation.organizationsNotFound", notFoundOrganizations);
		}
		if (notFoundGroups.length() > 0) {
			validationError = WebPlugin.getInstance().getMessage("entity.permission.validation.groupsNotFound", notFoundGroups);
		}
		
		if (validationError.length() > 0) {
			validationResults.put(ACTIONS_FIELD, validationError);
		}
		
		return validationResults;
	}
	
	private void appendToMessage(StringBuilder message, String string) {
		if (message.length() > 0) {
			message.append(", ");
		}
		message.append(string);
	}

}
