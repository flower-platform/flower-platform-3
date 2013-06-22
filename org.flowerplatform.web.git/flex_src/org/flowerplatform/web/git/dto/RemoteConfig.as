package  org.flowerplatform.web.git.dto {
	import mx.collections.ArrayCollection;
	
	/**
	 *	@author Cristina Constantinescu
	 */  
	[RemoteClass]
	[Bindable]
	public class RemoteConfig {
		
		public var name:String;
				
		public var uri:String;
		
		public var fetchMappings:ArrayCollection;
		
		public var pushMappings:ArrayCollection;		
		
	}
}