package org.flowerplatform.web.security.dto
{
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.web.entity.dto.NamedDto;
	
	/**
	 * @see Corresponding Java class.
	 * 
	 * @author Cristi
	 * @author Cristina
	 * @flowerModelElementId _Q9EsgF34EeGwLIVyv_iqEg
	 */ 
	[RemoteClass(alias="com.crispico.flower.mp.web.security.dto.OrganizationAdminUIDto")]
	public class OrganizationAdminUIDto	extends NamedDto {
		
		public var label:String;
		
		public var URL:String;
		
		public var logoURL:String;
		
		public var iconURL:String;
		
		public var activated:Boolean;
		
		public var projectsCount:int;
		
		public var filesCount:int;

		public var modelsCount:int;
		
		public var diagramsCount:int;
		
		public var pinned:Array;
		
		public var status:Object;
		
		/**
		 * @flowerModelElementId _Q9FTkl34EeGwLIVyv_iqEg
		 */
		public var groups:ArrayCollection;		
		
		public var SVNRepositoryURLs:ArrayCollection;
		
		override public function toString():String {
			return label;
		}
	}
	
}