package org.flowerplatform.web.git.history.remote;

import org.flowerplatform.communication.stateful_service.IStatefulClientLocalState;
import org.flowerplatform.web.git.history.remote.dto.HistoryViewInfoDto;

/**
 *	@author Cristina Constantinescu
 */
public class GitHistoryStatefulClientLocalState implements IStatefulClientLocalState {

	private HistoryViewInfoDto info;

	public HistoryViewInfoDto getInfo() {
		return info;
	}

	public void setInfo(HistoryViewInfoDto info) {
		this.info = info;
	}
		
}
