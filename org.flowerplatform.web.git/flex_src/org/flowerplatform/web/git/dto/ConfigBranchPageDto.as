package org.flowerplatform.web.git.dto {
	import mx.collections.ArrayCollection;
	
	/**
	 *	@author Cristina Constantinescu
	 */ 
	[RemoteClass]
	public class ConfigBranchPageDto {
		
		public var ref:GitRef;
		
		public var refs:ArrayCollection;
		
		public var remotes:ArrayCollection;
		
		public var selectedRef:String;
		
		public var selectedRemote:String;
		
		public var rebase:Boolean;
		
	}
}