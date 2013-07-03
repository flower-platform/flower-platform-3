package org.flowerplatform.web.git.common.remote.dto {
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