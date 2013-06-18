package org.flowerplatform.web.git.dto {
	import mx.collections.ArrayCollection;

	/**
	 *	@author Cristina Constantinescu
	 */  
	[RemoteClass]
	public class ConfigFetchPushPageDto	{
				
		public var ref:GitRef;
		
		public var remoteConfigs:ArrayCollection;
		
	}	
}