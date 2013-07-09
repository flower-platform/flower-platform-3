package org.flowerplatform.web.git.history.remote {
	import org.flowerplatform.communication.stateful_service.IStatefulClientLocalState;
	import org.flowerplatform.web.git.history.remote.dto.HistoryViewInfoDto;
	
	/**
	 *	@author Cristina Constantinescu
	 */   
	[RemoteClass]
	public class GitHistoryStatefulClientLocalState implements IStatefulClientLocalState {
		
		public var info:HistoryViewInfoDto;
		
	}
}