package org.flowerplatform.web.git.remote.dto {
	import mx.collections.ArrayCollection;
	
	/**
	 *	@author Cristina Constantinescu
	 */  
	[RemoteClass]
	public class CommitDto {
		
		public var id:String;
		
		public var shortId:String;
		
		public var label:String;
		
		public var image:String;
	}
}