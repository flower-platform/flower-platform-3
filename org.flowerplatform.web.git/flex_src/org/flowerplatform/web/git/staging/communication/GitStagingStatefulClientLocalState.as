package org.flowerplatform.web.git.staging.communication {
	import org.flowerplatform.communication.stateful_service.IStatefulClientLocalState;
	
	/**
	 *	@author Cristina Constantinescu
	 */
	[RemoteClass]
	public class GitStagingStatefulClientLocalState implements IStatefulClientLocalState {
		
		public var repositoryLocation:String;
		
	}
}