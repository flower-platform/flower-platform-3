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
package org.flowerplatform.editor.remote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.editor.collaboration.CollaborativeFigureModel;

/**
 * The corresponding ActionScript class has a special mapping. Some fields exist only
 * in Java, or only in AS, or both in AS and Java but they are not serialized, or both
 * in AS and Java and they are serialized. The comment of each getter specifies this.
 * 
 * <p>
 * Represents a resource that can be edited on the client side (in an editor or not).
 * E.g. a text file, a model file, a diagram.
 * 
 * <p>
 * It contains fields related to the state of the resource (i.e. fields existent
 * in this class) and fields related to the content of the resource (which are provided
 * by the subclass, according to the data it holds).
 * 
 * <p>
 * On the server side, an {@link EditableResource} means state + content, and there
 * is a specialized class for each type of {@link EditableResource}. On the client side,
 * the {@link EditableResource} holds only the state (and there are no {@link EditableResource}
 * subclasses). The content is held by <code>EditorFrontend</code>s.
 * 
 * @author Cristi
 * 
 */
public abstract class EditableResource {

	private EditorStatefulService editorStatefulService;
	
	/**
	 * @see Getter
	 * 
	 */
	private Object editorInput;
	
	/**
	 * This is not synchronized on purpose.
	 * 
	 * @see Getter
	 * 
	 */
	private List<EditableResourceClient> clients = new ArrayList<EditableResourceClient>();
	
	private List<EditableResourceClient> clientsReadOnly = Collections.unmodifiableList(clients);
	
	/**
	 * @see Getter.
	 * 
	 */
	private boolean locked;
	
	/**
	 * @see Getter.
	 * 
	 */
	private Date lockExpireTime;
	
	/**
	 * @see Getter.
	 * 
	 */
	private Date lockUpdateTime;

	/**
	 * @see Getter.
	 * 
	 */
	private EditableResourceClient lockOwner;
	
	private Collection<CollaborativeFigureModel> collaborativeFigureModels;
	
	public EditorStatefulService getEditorStatefulService() {
		return editorStatefulService;
	}

	public void setEditorStatefulService(EditorStatefulService editorStatefulService) {
		this.editorStatefulService = editorStatefulService;
	}

	public String getEditorStatefulClientId() {
		return getEditorStatefulService().calculateStatefulClientId(getEditableResourcePath());
	}
	
	public String getMasterEditorStatefulClientId() {
		if (getMasterEditableResource() == null) {
			return null;
		} else {
			return getMasterEditableResource().getEditorStatefulClientId();
		}
	}
	
	/**
	 * This property is transferred to the client.
	 * 
	 * The editorInput is an unique identifier of an {@link EditableResource},
	 * which is valid regardless of the state of the server and the {@link EditableResource}
	 * (i.e. it is not a transient id).
	 * 
	 * <p>
	 * For the moment, the convention is to use a string that represents the path
	 * of the resource (if it is a file, e.g. /org/ws/proj.txt) or a the path of the
	 * resource + the internal path (e.g. for a model + diagram can be /org/ws/model.flower/!xmi_id_of_element).
	 * 
	 * <p>
	 * The editor backend (server)/editor frontend (client) pair interpret (or unpack)
	 * the editorInput. They are the only ones allowed to cast the value to something meaningful
	 * (probably a <code>String</code>). Everyone else, should treat this as an opaque <code>Object</code>, and 
	 * should not have logic related to "what's in" the editorInput". It can be of course used for 
	 * comparison. 
	 * 
	 * 
	 */
	public Object getEditorInput() {
		return editorInput;
	}
	
	public String getEditableResourcePath() {
		return editorInput.toString();
	}

	/**
	 * @see Getter
	 * 
	 */
	public void setEditorInput(Object editorInput) {
		this.editorInput = editorInput;
	}

	/**
	 * This property is NOT transferred to the client.
	 * 
	 * By default returns <code>null</code> and should be overridden by classes
	 * that are slave {@link EditableResource}s (i.e. there is another {@link EditableResource}
	 * that represents the physical file (e.g. a model file) and they are are sub-units
	 * (e.g. a diagram) that cannot be separately saved).
	 * 
	 * <p>
	 * In a master-slave relationship, a slave {@link EditableResource} cannot be saved
	 * separately. Saving makes sense only on maste {@link EditableResource}s.
	 * 
	 * <p>
	 * The field is not declared in this class to save some memory (there are a lot of
	 * {@link EditableResource}s that are not master/slave and they don't need this field).
	 * 
	 * 
	 */
	public EditableResource getMasterEditableResource() {
		return null;
	}
	
	/**
	 * This property is NOT transferred to the client, although it exists there, maintained
	 * by client logic.
	 * 
	 * <p>
	 * By default returns <code>null</code> and should be overridden by classes
	 * that are master {@link EditableResource}s.
	 * 
	 * <p>
	 * The field is not declared in this class to save some memory (there are a lot of
	 * {@link EditableResource}s that are not master/slave and they don't need this field).
	 */
	public List<EditableResource> getSlaveEditableResources() {
		return null;
	}
	
	/**
	 * By default does nothing  and should be overridden by classes
	 * that are master {@link EditableResource}s. The method should set 
	 * the underlying field. This method exists, because this list
	 * is lazy initialized, to save some memory.
	 */
	public void setSlaveEditableResources(List<EditableResource> value) {
		// do nothing
	}

	/**
	 * This property is transferred to the client.
	 * 
	 * Should return the label (displayed in the Open Resources View
	 * and when the Save dialog pops up).
	 * 
	 * 
	 */
	public abstract String getLabel();
	
	/**
	 * This property is transferred to the client.
	 * 
	 * Should return the URL for the icon (displayed in the Open Resources View
	 * and when the Save dialog pops up).
	 * 
	 * 
	 */
	public abstract String getIconUrl();

	/**
	 * This property is transferred to the client.
	 * 
	 * Should return the dirty state of the {@link EditableResource}
	 * (i.e. <code>true</code> if it needs to be saved and <code>false</code>
	 * if it doesn't have unsaved changes).
	 * 
	 * 
	 */
	public abstract boolean isDirty();

	/**
	 * The lock status of the current resource.
	 * 
	 * 
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * 
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	/**
	 * The time when the lock will expire.
	 * 
	 * 
	 */
	public Date getLockExpireTime() {
		return lockExpireTime;
	}

	/**
	 * 
	 */
	public void setLockExpireTime(Date lockExpireTime) {
		this.lockExpireTime = lockExpireTime;
	}

	/**
	 * The time when the lock info has been updated.
	 * 
	 * 
	 */
	public Date getLockUpdateTime() {
		return lockUpdateTime;
	}

	/**
	 * 
	 */
	public void setLockUpdateTime(Date lockUpdateTime) {
		this.lockUpdateTime = lockUpdateTime;
	}

	/**
	 * The owner of the lock.
	 * 
	 * 
	 */
	public EditableResourceClient getLockOwner() {
		return lockOwner;
	}

	/**
	 * 
	 */
	public void setLockOwner(EditableResourceClient lockOwner) {
		this.lockOwner = lockOwner;
	}

	/**
	 * This property exists on the client, but is not transferred via serialization
	 * (i.e. is Transient on the client). 
	 * 
	 * <p>
	 * Contains the clients that are currently viewing this {@link EditableResource}. The list
	 * is read-only. This class has methods for adding and removing elements in it.
	 * 
	 * <p>
	 * Please make sure that the code that uses methods related to this list (including this one,
	 * used for iteration), uses a named lock, from {@link EditorStatefulService#namedLockPool}.
	 * Unless explicitly specified otherwise in the method's doc.
	 * 
	 * TODO CS/STFL adaugat @see cu metodele respective, cand le definitivam
	 * TODO CS/STFL de verificat ca toate apelurile sunt cu lock catre ac. metode
	 */
	public List<EditableResourceClient> getClients() {
		return clientsReadOnly;
	}

	/**
	 * Iterates the clients and returns the one having the given
	 * {@link CommunicationChannel} (or <code>null</code> if none found).
	 * 
	 * 
	 */
	public EditableResourceClient getEditableResourceClientByCommunicationChannel(
			final CommunicationChannel communicationChannel) {
//		synchronized (clients) {
			for (EditableResourceClient client : clients) {
				if (client.getCommunicationChannel().equals(communicationChannel)) {
					return client;
				}
			}
			return null;
//		}
	}

	/**
	 * Used by {@link #removeEditableResourceClientByCommunicationChannel(CommunicationChannel)} and 
	 * {@link #getEditableResourceClientByCommunicationChannelThreadSafe(CommunicationChannel)} to ensure that
	 * no removal can be done from the list while the iteration is in progress.
	 * 
	 * @see #getEditableResourceClientByCommunicationChannelThreadSafe(CommunicationChannel)
	 * @see #removeEditableResourceClientByCommunicationChannel(CommunicationChannel)
	 */
	private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	
	/**
	 * Iterates the clients and returns the one having the given
	 * {@link CommunicationChannel}, or <code>null</code> if not found.
	 * 
	 * <p>
	 * This method is synchronized and shares a read/write lock only with {@link #removeEditableResourceClientByCommunicationChannel(CommunicationChannel)}.
	 * That means that while a iterating this list, a removal cannot take place. However, an 
	 * addition can happen, and in this case the iteration won't take the newly added element into consideration.
	 * 
	 * <p>
	 * This method is intended to be used within {@link EditorStatefulService#webCommunicationChannelDestroyed()}. For 
	 * this usage, the fact that new items can be added to the list, is not disturbing. The fact that the addition is
	 * not synchronized improves the performance (at least in theory).
	 * 
	 * <p>
	 * Because we are using a Read/Write lock, multiple threads can execute this method (having a Read lock) at the same time. 
	 */
	public EditableResourceClient getEditableResourceClientByCommunicationChannelThreadSafe(
			final CommunicationChannel communicationChannel) {
		readWriteLock.readLock().lock();
		try {
			// we use iteration with for instead of for each, because new elements might
			// be added to the list, and the iterator would complain. Cf. this method comment,
			// this kind of concurrent read/write access is valid. However, we wouldn't have liked
			// the opposite: to have elements removed. That's why we use the read/write lock
			for (int i = 0; i < clients.size(); i++) {
				EditableResourceClient client = clients.get(i);
				if (client.getCommunicationChannel().equals(communicationChannel)) {
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
	 * Iterates the clients and removes the one having the given
	 * {@link CommunicationChannel}. Returns it (or <code>null</code> if none found).
	 * 
	 * <p>
	 * This method is synchronized and shares the read/write lock only with {@link #getEditableResourceClientByCommunicationChannelThreadSafe(CommunicationChannel)}.
	 * So a list removal cannot take place when the former method iterates on the list.
	 * 
	 * @see #getEditableResourceClientByCommunicationChannelThreadSafe(CommunicationChannel)
	 * 
	 */
	public EditableResourceClient removeEditableResourceClientByCommunicationChannel(
			CommunicationChannel communicationChannel) {
		readWriteLock.writeLock().lock();
		try {
			for (Iterator<EditableResourceClient> iter = clients.iterator(); iter.hasNext(); ) {
				EditableResourceClient client = iter.next();
				if (client.getCommunicationChannel().equals(communicationChannel)) {
					// found
					iter.remove();
					return client;
				}
			}
			return null;
		} finally {
			readWriteLock.writeLock().unlock();
		}
	}
	
	public void addEditableResourceClient(EditableResourceClient client) {
		clients.add(client);
	}
	
	@Override
	public String toString() {
		if (getSlaveEditableResources() != null) {
			return String.format("%s[path=%s, slaveERs=%s]", getClass().getSimpleName(), getEditorInput(), getSlaveEditableResources());
		} else {
			return String.format("%s[path=%s]", getClass().getSimpleName(), getEditorInput());
		}
	}

	public Collection<CollaborativeFigureModel> getCollaborativeFigureModels() {
		return collaborativeFigureModels;
	}

	public void setCollaborativeFigureModels(
			Collection<CollaborativeFigureModel> collaborativeFigureModels) {
		this.collaborativeFigureModels = collaborativeFigureModels;
	}

}