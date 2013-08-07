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
package org.flowerplatform.communication.tree;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;

/**
 * Holds information on server regarding a tree node.
 * 
 * <p>
 * It's build as a tree structure.
 * 
 * @author Cristi
 * @author Cristina
 * 
 */
public class NodeInfo {
	
	/**
	 * @see Getter doc.
	 * 
	 */
	private Object node;

	/**
	 * @see Getter doc.
	 * 
	 */
	private NodeInfo parent;

	/**
	 * @see Getter doc.
	 * 
	 */
	private PathFragment pathFragment;
	
	/**
	 * @see Getter doc.
	 * 
	 */
	private List<NodeInfo> children = new ArrayList<NodeInfo>();
	
	/**
	 * @see Getter doc.
	 * 
	 */
	private List<NodeInfoClient> clients = new ArrayList<NodeInfoClient>();
		
	/**
	 * 
	 */
	private List<NodeInfoClient> clientsReadOnly = Collections.unmodifiableList(clients);
	
	private Map<String, Object> customData;
	/**
	 * The server object representing the node on client.
	 * 
	 */
	public Object getNode() {
		return node;
	}

	/**
	 * 
	 */
	public void setNode(Object node) {
		this.node = node;
	}

	/**
	 * The child nodes of the current node. Needed
	 * for cleanup, when a node is removed from the server.
	 * 
	 * @see GenericTreeStatefulService#dispatchContentUpdate()
	 * 
	 */
	public List<NodeInfo> getChildren() {
		return children;
	}

	/**
	 * 
	 */
	public void setChildren(List<NodeInfo> children) {
		this.children = children;
	}

	/**
	 * The parent {@link NodeInfo}.
	 * Used to create this node's structure.
	 * 
	 */
	public NodeInfo getParent() {
		return parent;
	}

	/**
	 * 
	 */
	public void setParent(NodeInfo parent) {
		this.parent = parent;
	}

	/**
	 * The path for the node.
	 * Used to communicate with clients.
	 * 
	 */
	public PathFragment getPathFragment() {
		return pathFragment;
	}

	/**
	 * 
	 */
	public void setPathFragment(PathFragment pathFragment) {
		this.pathFragment = pathFragment;
	}

	/**
	 * 
	 */
	public List<NodeInfoClient> getClients() {
		return clientsReadOnly;
	}
	
	/**
	 * 
	 */
	private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	/**
	 * 
	 */	
	public ReentrantReadWriteLock getReadWriteLock() {
		return readWriteLock;
	}

	/**
	 * 
	 */
	public NodeInfoClient getNodeInfoClientByCommunicationChannelThreadSafe(CommunicationChannel communicationChannel, String statefulClientId, GenericTreeStatefulService service) {
		readWriteLock.readLock().lock();
		try {
			// we use iteration with for instead of for each, because new elements might
			// be added to the list, and the iterator would complain. Cf. this method comment,
			// this kind of concurrent read/write access is valid. However, we wouldn't have liked
			// the opposite: to have elements removed. That's why we use the read/write lock
			for (int i = 0; i < clients.size(); i++) {
				NodeInfoClient client = clients.get(i);
				if (client.getCommunicationChannel().equals(communicationChannel) &&
					(statefulClientId == null || client.getStatefulClientId(service).equals(statefulClientId))) {
					return client;
				}
			}
			
			// no client found
			return null;
		} finally {
			readWriteLock.readLock().unlock();
		}
	}

	/**
	 * 
	 */
	public NodeInfoClient removeNodeInfoClientByCommunicationChannel(CommunicationChannel communicationChannel, String statefulClientId, GenericTreeStatefulService service) {
		readWriteLock.writeLock().lock();
		try {
			for (Iterator<NodeInfoClient> iter = clients.iterator(); iter.hasNext(); ) {
				NodeInfoClient client = iter.next();
				if (client.getCommunicationChannel().equals(communicationChannel) &&
					(statefulClientId == null || client.getStatefulClientId(service).equals(statefulClientId))) {
					// found
					iter.remove();
					if (statefulClientId != null) { // only this node must be removed, so return
						return client;
					}
				}
			}
			return null;
		} finally {
			readWriteLock.writeLock().unlock();
		}
	}

	/**
	 * 
	 */
	public void addNodeInfoClient(NodeInfoClient client) {
		clients.add(client);
	}

	/**
	 * 
	 */
	public String toString() {
		return String.format("%s[path=%s]", getClass().getSimpleName(), getPathFragment());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof NodeInfo)) {
			return false;
		}
		return node.equals(((NodeInfo) obj).node);
	}

	public Map<String, Object> getCustomData() {
		if (customData == null) {
			customData = new HashMap<String, Object>();
		}
		return customData;
	}

}