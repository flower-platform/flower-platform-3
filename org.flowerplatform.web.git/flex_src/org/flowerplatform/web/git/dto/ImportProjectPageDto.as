package org.flowerplatform.web.git.dto {
	import mx.collections.ArrayCollection;
	
	/**
	 *	@author Cristina Constantinescu
	 */  
	[RemoteClass]
	public class ImportProjectPageDto {
		
		public var selectedFolders:ArrayCollection;
		
		public var existingProjects:ArrayCollection;
		
	}
}