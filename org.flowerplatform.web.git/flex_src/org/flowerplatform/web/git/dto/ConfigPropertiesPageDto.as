package org.flowerplatform.web.git.dto {
	import mx.collections.ArrayCollection;
	
	/**
	 *	@author Cristina Constantinescu
	 */ 
	[RemoteClass]
	public class ConfigPropertiesPageDto {
		
		public var repository:String;
		
		public var entries:ArrayCollection;
		
	}
}