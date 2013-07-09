package org.flowerplatform.web.git.common.remote.dto {
	import mx.collections.ArrayCollection;
	
	/**
	 *	@author Cristina Constantinescu
	 */  
	[RemoteClass]
	[Bindable]
	public class ImportProjectPageDto {
		
		public var selectedFolders:ArrayCollection;
		
		public var existingProjects:ArrayCollection;
		
	}
}