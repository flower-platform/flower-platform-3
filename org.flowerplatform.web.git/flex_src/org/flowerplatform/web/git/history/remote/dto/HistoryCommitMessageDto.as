package org.flowerplatform.web.git.history.remote.dto {
	import mx.collections.ArrayCollection;
	
	/**
	 *	@author Cristina Constantinescu
	 */   
	[RemoteClass]
	public class HistoryCommitMessageDto {
		
		public var parents:ArrayCollection;
		
		public var children:ArrayCollection;
	}
}