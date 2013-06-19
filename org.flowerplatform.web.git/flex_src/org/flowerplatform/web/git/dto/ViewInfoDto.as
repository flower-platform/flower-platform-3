package org.flowerplatform.web.git.dto {
	
	/**
	 *	@author Cristina Constantinescu
	 */  
	[RemoteClass]
	public class ViewInfoDto {
		
		public var repositoryLocation:String;	
		
		public var repositoryName:String;
				
		public var selectedObject:Object;
		
		public var isResource:Boolean;
		
		public var statefulClientId:String;
	}
	
}