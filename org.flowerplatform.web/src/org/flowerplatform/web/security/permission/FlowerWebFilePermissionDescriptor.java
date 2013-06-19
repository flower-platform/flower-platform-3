package org.flowerplatform.web.security.permission;


import java.security.Permission;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.web.WebPlugin;


/**
 * Descriptor for {@link FlowerWebFilePermission}. See the doc of this class
 * for additional info.
 * 
 * @author Cristi
 * @author Florin
 * 
 * @flowerModelElementId _f1tOYGR_EeGyd4yTk74SKw
 */
public class FlowerWebFilePermissionDescriptor extends PermissionDescriptor {

	/**
	 * @flowerModelElementId _f0zL0GnXEeGiEKNiPvCvPw
	 */
	@Override
	public Class<? extends Permission> getHandledPermissionType() {
		return java.io.FilePermission.class;
	}

	/**
	 * @flowerModelElementId _GsnvwGzzEeGBsfNm1ipRfw
	 */
	@Override
	public Class<? extends Permission> getImplementedPermissionType() {
		return org.flowerplatform.web.security.permission.FlowerWebFilePermission.class;
	}

	@Override
	public String getSimpleName() {
		return WebPlugin.getInstance().getMessage("entity.permission.simpleName.flowerWebFilePermission");
	}
	
	@Override
	public int getOrder() {
		return 10;
	}

	@Override
	public Map<String, String> getHints() {
		Map<String, String> map = super.getHints();
		map.put(TYPE_FIELD, WebPlugin.getInstance().getMessage("entity.permission.filePermission.type.hint"));
		map.put(NAME_FIELD, WebPlugin.getInstance().getMessage("entity.permission.filePermission.resource.hint"));
		map.put(ACTIONS_FIELD, WebPlugin.getInstance().getMessage("entity.permission.filePermission.actions.hint", getActions()));
		return map;
	}

	/**
	 * @flowerModelElementId _7p8NIWaqEeGOeOE1u9CeQw
	 */
	@Override
	public boolean isTreePermission() {
		return true;
	}

	/**
	 * @flowerModelElementId _Sc1MUIIZEeGPwv1h63g-uQ
	 */
	@Override
	public List<String> getActions() {
		return Arrays.asList(FlowerWebFilePermission.READ, FlowerWebFilePermission.READ_WRITE, FlowerWebFilePermission.READ_WRITE_DELETE, FlowerWebFilePermission.NONE);
	}
	
	/**
	 * @flowerModelElementId _Sc1MU4IZEeGPwv1h63g-uQ
	 */
	@Override
	public Map<String, String> validate(Permission permission) {
		if (!(permission instanceof FlowerWebFilePermission)) {
			throw new RuntimeException("Can handle only FlowerWebFilePermission");
		}
		Map<String, String> validationResults = new HashMap<String, String>();
		
		if (!(pathIsRelativeToWorkspace(permission.getName()))) {
			validationResults.put(NAME_FIELD, WebPlugin.getInstance().getMessage("entity.permission.validation.invalidPath"));
		}
		if (!getActions().contains(permission.getActions())) {
			validationResults.put(ACTIONS_FIELD, WebPlugin.getInstance().getMessage("entity.permission.validation.invalidAction", getActions()));
		}
		
		return validationResults;
	}

}