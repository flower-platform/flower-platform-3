package org.flowerplatform.web.git.dto {
	import mx.collections.ArrayCollection;

	/**
	 *	@author Cristina Constantinescu
	 */ 
	[RemoteClass]
	public class CommitPageDto extends GitActionDto {
		
		public var author:String;
		
		public var committer:String;
		
		public var message:String;
				
		public var commitResources:ArrayCollection;
		
	}
}