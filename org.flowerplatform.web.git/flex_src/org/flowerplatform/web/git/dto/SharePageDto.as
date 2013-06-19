package org.flowerplatform.web.git.dto {
	import mx.collections.ArrayCollection;
	
	/**
	 *	@author Cristina Constantinescu
	 */  
	[RemoteClass]
	public class SharePageDto extends GitActionDto {
	
		public var repositories:ArrayCollection;
		
		public var projectPath:String;
		
	}
}