package org.flowerplatform.web.git.common.remote.dto {
	import mx.collections.ArrayCollection;
	
	/**
	 *	@author Cristina Constantinescu
	 */ 
	[RemoteClass]
	public class ConfigBranchPageDto {
		
		public var ref:GitRef;
		
		public var refs:ArrayCollection;
		
		public var remotes:ArrayCollection;
		
		public var selectedRef:GitRef;
		
		public var selectedRemote:RemoteConfig;
		
		public var rebase:Boolean;
		
	}
}