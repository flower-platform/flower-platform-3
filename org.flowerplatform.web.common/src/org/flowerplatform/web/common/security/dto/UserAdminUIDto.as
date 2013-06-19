package org.flowerplatform.web.common.security.dto {
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.web.common.entity.dto.NamedDto;
	
	/**
	 * @see Corresponding Java class.
	 * 
	 * @author Cristi
	 * @author Cristina
	 * 	
	 * @flowerModelElementId _37klIFfcEeG3xJSMfQ3HWg
	 */
	[RemoteClass(alias="org.flowerplatform.web.security.dto.UserAdminUIDto")]
	public class UserAdminUIDto extends NamedDto  {
		
		/**
		 * @flowerModelElementId _5d-MAFfcEeG3xJSMfQ3HWg
		 */
		public var login:String;
		
		/**
		 * @flowerModelElementId _6bAcAFfcEeG3xJSMfQ3HWg
		 */
		public var password:String;
		
		/**
		 * @flowerModelElementId _7rd-UFfcEeG3xJSMfQ3HWg
		 */
		public var email:String;
		
		public var isActivated:Boolean;
		
		/**
		 * @flowerModelElementId _8fQLsFfcEeG3xJSMfQ3HWg
		 */
		public var groups:ArrayCollection;
		
		/**
		 * @flowerModelElementId _-67j8FfcEeG3xJSMfQ3HWg
		 */
		public var organizationUsers:ArrayCollection;
	}
	
}