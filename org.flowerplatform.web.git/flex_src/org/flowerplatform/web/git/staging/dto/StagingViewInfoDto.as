package org.flowerplatform.web.git.staging.dto {
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.web.git.dto.ViewInfoDto;

	/**
	 *	@author Cristina Constantinescu
	 */
	[RemoteClass]
	public class StagingViewInfoDto extends ViewInfoDto {
				
		public var unstagedChanges:ArrayCollection;
		
		public var stagedChanges:ArrayCollection;
		
		public var author:String;
		
		public var committer:String;
		
	}
}