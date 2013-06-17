package org.flowerplatform.web.git.dto {
	import mx.collections.ArrayCollection;
	
	/**
	 *	@author Cristina Constantinescu
	 */  
	[RemoteClass]
	public class ConfigTagPageDto {
		
		public var commits:ArrayCollection;
	
		public var initialSelectedIndex:int;
		
		public var addEmptyLine:Boolean;
		
	}
}