package org.flowerplatform.web.security.dto {
	import org.flowerplatform.web.entity.dto.Dto;
	
	[RemoteClass(alias="com.crispico.flower.mp.web.security.dto.OrganizationUserAdminUIDto")]
	public class OrganizationUserAdminUIDto	extends Dto {
		public var organization:OrganizationAdminUIDto;
		public var status:String;
		
		public static const ADMIN:String = "ADMIN";
		public static const MEMBER:String = "MEMBER";
		public static const PENDING_MEMBERSHIP_APPROVAL = "PENDING_MEMBERSHIP_APPROVAL";
	}
}