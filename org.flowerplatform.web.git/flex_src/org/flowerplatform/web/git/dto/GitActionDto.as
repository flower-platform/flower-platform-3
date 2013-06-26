package org.flowerplatform.web.git.dto {
	import org.flowerplatform.communication.tree.remote.TreeNode;
	
	/**
	 *	@author Cristina Constantinescu
	 */   
	[RemoteClass]
	[Bindable]
	public class GitActionDto {
		
		public var repository:String;
	
		public var branch:String;
		
		public var repositoryNode:TreeNode;
	}
}