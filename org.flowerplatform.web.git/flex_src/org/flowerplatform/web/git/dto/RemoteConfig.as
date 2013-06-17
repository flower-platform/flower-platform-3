package  org.flowerplatform.web.git.dto {
	import mx.collections.ArrayCollection;
	
	/**
	 *	@author Cristina Constantinescu
	 */  
	[RemoteClass]
	public class RemoteConfig {
		
		public var remoteName:String;
		
		public var fetchUri:String;
		
		public var fetchMappings:ArrayCollection;
		
		public var pushUri:String;
		
		public var pushMappings:ArrayCollection;
		
	}
}