package org.flowerplatform.web.security.dto {
	import org.flowerplatform.web.entity.dto.NamedDto;
	
	/**
	 * @author Cristi
	 */
	[RemoteClass(alias="com.crispico.flower.mp.web.security.dto.User_CurrentUserLoggedInDto")]
	public class User_CurrentUserLoggedInDto extends NamedDto {
		public var login:String;
		
		public var email:String;
		
		public var hasAdminSecurityEntitiesPermissions:Boolean;
		
		public var isAdmin:Boolean;
	}
}