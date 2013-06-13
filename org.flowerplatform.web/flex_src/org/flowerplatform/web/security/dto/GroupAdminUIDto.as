package org.flowerplatform.web.security.dto
{
	import org.flowerplatform.web.entity.dto.NamedDto;
	
	/**
	 * @see Corresponding Java class.
	 * 
	 * @author Cristi
	 * @author Cristina
	 * @flowerModelElementId _Q9C3UF34EeGwLIVyv_iqEg
	 */ 
	[RemoteClass(alias="com.crispico.flower.mp.web.security.dto.GroupAdminUIDto")]
	public class GroupAdminUIDto extends NamedDto {
		
		/**
		 * @flowerModelElementId _Q9DeZF34EeGwLIVyv_iqEg
		 */
		public var organization:NamedDto;
		
		/**
		 * This is needed because the organization is only NamedDto, so we can't access its label.
		 */ 
		public var organizationLabel:String;
		
	}
}