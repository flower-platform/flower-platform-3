package org.flowerplatform.web.common.security.dto {
	import org.flowerplatform.web.common.entity.dto.NamedDto;
	
	
	/**
	 * @see Corresponding Java class.
	 * 
	 * @author Cristi
	 * @author Cristina
	 * @flowerModelElementId _Q9GhsF34EeGwLIVyv_iqEg
	 */ 
	[RemoteClass(alias="com.crispico.flower.mp.web.security.dto.PermissionAdminUIDto")]
	public class PermissionAdminUIDto extends NamedDto {
	
		/**
		 * @flowerModelElementId _Q9GhtV34EeGwLIVyv_iqEg
		 */
		public var type:String;
		
		/**
		 * @flowerModelElementId _Q9HIwV34EeGwLIVyv_iqEg
		 */
		public var actions:String;
		
		/**
		 * @flowerModelElementId _Q9HIwl34EeGwLIVyv_iqEg
		 */
		public var assignedTo:String;

		public var isEditable:Boolean;
		
		public function get simpleType():String { 
			return type.substring(type.lastIndexOf(".") + 1, type.length);
		}
	}
}