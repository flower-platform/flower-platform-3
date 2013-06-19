package org.flowerplatform.web.common.security.dto {
	import org.flowerplatform.web.common.entity.dto.Dto;
	
	
	[RemoteClass(alias="org.flowerplatform.web.security.dto.OrganizationUserAdminUIDto")]
	public class OrganizationUserAdminUIDto	extends Dto {
		public var organization:OrganizationAdminUIDto;
		public var status:String;
		
		public static const ADMIN:String = "ADMIN";
		public static const MEMBER:String = "MEMBER";
		public static const PENDING_MEMBERSHIP_APPROVAL = "PENDING_MEMBERSHIP_APPROVAL";
	}
}