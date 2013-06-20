package org.flowerplatform.web.common.security.dto {
	
	/**
	 * @see Corresponding Java class.
	 * 
	 * @author Cristi
	 * @author Cristina
	 * @flowerModelElementId _Q9I98F34EeGwLIVyv_iqEg
	 */ 
	[RemoteClass(alias="org.flowerplatform.web.security.dto.PermissionsByResourceFilter")]
	public class PermissionsByResourceFilter {
		
		
		/**
		 * @flowerModelElementId _Q9I99F34EeGwLIVyv_iqEg
		 */
		public var resource:String;
		
		/**
		 * @author Mariana
		 * 
		 * @flowerModelElementId _Q9I99V34EeGwLIVyv_iqEg
		 */
		public function PermissionsByResourceFilter(resource:String) {
			this.resource = resource;
		}
	}
	
}