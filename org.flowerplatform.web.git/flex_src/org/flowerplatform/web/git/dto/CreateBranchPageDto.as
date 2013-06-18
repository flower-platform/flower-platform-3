package org.flowerplatform.web.git.dto {
	import mx.collections.ArrayCollection;
	
	/**
	 *	@author Cristina Constantinescu
	 */  
	[RemoteClass]
	public class CreateBranchPageDto {
		
		public var refs:ArrayCollection;
		
		public var selectedIndex:int;
		
		public var prefixName:String;
		
	}
}