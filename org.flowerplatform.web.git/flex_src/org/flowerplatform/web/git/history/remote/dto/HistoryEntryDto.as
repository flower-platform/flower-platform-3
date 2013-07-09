package org.flowerplatform.web.git.history.remote.dto {
	import mx.collections.ArrayCollection;

	/**
	 *	@author Cristina Constantinescu
	 */ 
	[RemoteClass]
	public class HistoryEntryDto {
		
		public var id:String;
		public var shortId:String;
		
		public var message:String;
		
		public var author:String;		
		public var authorEmail:String;		
		public var authoredDate:Date;
		
		public var committer:String;
		public var committerEmail:String;
		public var committeredDate:Date;
		
		public var specialMessage:String;
				
		public var drawings:ArrayCollection;
		
		public var fileDiffs:ArrayCollection;
		
		public var commitMessage:HistoryCommitMessageDto;
		
	}
}