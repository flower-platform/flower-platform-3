/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
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