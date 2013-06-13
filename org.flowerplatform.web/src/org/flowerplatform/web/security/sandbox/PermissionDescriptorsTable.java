package org.flowerplatform.web.security.sandbox;

import java.security.Permission;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.web.security.permission.PermissionDescriptor;


/**
 * The descriptors of the permissions that {@link FlowerWebPolicy} enforces. 
 * 
 * @author Florin
 * 
 * @flowerModelElementId _xLTqkHQdEeGvmLPkt9AQrg
 */
class PermissionDescriptorsTable {

	/**
	 * The key of this map is handled permission type.
	 * @flowerModelElementId _xLVfwXQdEeGvmLPkt9AQrg
	 */
	private Map<Class<? extends Permission>, PermissionDescriptor> handledPermissionDescriptors = new LinkedHashMap<Class<? extends Permission>, PermissionDescriptor>();
		
	/**
	 * The key of this map is implemented permission type.
	 * @flowerModelElementId _xLWt4XQdEeGvmLPkt9AQrg
	 */
	private Map<Class<? extends Permission>, PermissionDescriptor> implementedPermissionDescriptors = new LinkedHashMap<Class<? extends Permission>, PermissionDescriptor>();
	
	/**
	 * @flowerModelElementId _Sdc3YIIZEeGPwv1h63g-uQ
	 */
	public void put(PermissionDescriptor descriptor) {
		handledPermissionDescriptors.put(descriptor.getHandledPermissionType(), descriptor);
		implementedPermissionDescriptors.put(descriptor.getImplementedPermissionType(), descriptor);
	}
	
	/**
	 * @flowerModelElementId _xLZKIXQdEeGvmLPkt9AQrg
	 */
	public PermissionDescriptor getByHandledPermissionType(Class<? extends Permission> type) {
		return handledPermissionDescriptors.get(type);
	}
	
	/**
	 * @flowerModelElementId _xLaYQXQdEeGvmLPkt9AQrg
	 */
	public PermissionDescriptor getByImplementedPermissionType(Class<? extends Permission> type) {
		return implementedPermissionDescriptors.get(type);
	}
	
	/**
	 * @flowerModelElementId _SdeFgIIZEeGPwv1h63g-uQ
	 */
	public List<PermissionDescriptor> getPermissionDescriptors() {
		return new ArrayList<PermissionDescriptor>(implementedPermissionDescriptors.values());
	}
}