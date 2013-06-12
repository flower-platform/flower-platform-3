package org.flowerplatform.communication.tree.remote;

import java.util.List;
import java.util.Map;

import org.flowerplatform.communication.stateful_service.IStatefulClientLocalState;

/**
 * @author Cristina
 * @flowerModelElementId _1qt2cA7vEeKbvNML8mcTuA
 */
public class GenericTreeStatefulClientLocalState implements IStatefulClientLocalState {
	
	/**
	 * @flowerModelElementId _mPg6cA70EeKbvNML8mcTuA
	 */
	private List<List<PathFragment>> openNodes;
	
	/**
	 * @flowerModelElementId _VQghoBEjEeKYjqFAQECmkA
	 */
	private Map<Object, Object> clientContext;

	private Map<Object, Object> statefulContext;
	
	/**
	 * @flowerModelElementId _eLYkMBEeEeKYjqFAQECmkA
	 */
	public List<List<PathFragment>> getOpenNodes() {
		return openNodes;
	}

	/**
	 * @flowerModelElementId _eLlYgBEeEeKYjqFAQECmkA
	 */
	public void setOpenNodes(List<List<PathFragment>> openNodes) {
		this.openNodes = openNodes;
	}

	public Map<Object, Object> getClientContext() {
		return clientContext;
	}

	public void setClientContext(Map<Object, Object> clientContext) {
		this.clientContext = clientContext;
	}

	public Map<Object, Object> getStatefulContext() {
		return statefulContext;
	}

	public void setStatefulContext(Map<Object, Object> statefulContext) {
		this.statefulContext = statefulContext;
	}
	
}