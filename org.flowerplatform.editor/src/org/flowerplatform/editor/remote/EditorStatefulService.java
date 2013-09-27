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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.flowerplatform.common.log.AuditDetails;
import org.flowerplatform.common.log.LogUtil;
import org.flowerplatform.common.util.RunnableWithParam;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.channel.ICommunicationChannelLifecycleListener;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.stateful_service.IStatefulClientLocalState;
import org.flowerplatform.communication.stateful_service.NamedLockPool;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.communication.stateful_service.StatefulService;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.editor.UnlockEditableResourceRunnable;
import org.flowerplatform.editor.collaboration.CollaborativeFigureModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristi
 * 
 */
public abstract class EditorStatefulService extends StatefulService implements IEditorStatefulClientMXBean,  ICommunicationChannelLifecycleListener {
	
	private String editorName;
	
	/**
	 * 
	 */
	private static final Logger logger = LoggerFactory.getLogger(EditorStatefulService.class);
	
	public static final String SUBSCRIBE_ER_AUDIT_CATEGORY = "SUBSCRIBE_ER";
	public static final String SAVE_ER_AUDIT_CATEGORY = "SAVE_ER";
	
	/**
	 * The open {@link EditableResource}. They are mapped by <code>editableResourcePath</code>.
	 * 
	 * <p>
	 * We use {@link ConcurrentHashMap} as implementation. This allows access from multiple threads
	 * without locking. While this is good for performance, it may be bad for synchronization. However,
	 * the use of {@link NamedLockPool} overcomes this issue.
	 * 
	 * TODO CS/STFL cred ca dupa mutare runnable, putem pune inapoi protected
	 * 
	 */
	public Map<String, EditableResource> editableResources = new ConcurrentHashMap<String, EditableResource>();
	
	/**
	 * We use this instead of normal locking, because, during subscription there is a small
	 * time window where 2 threads subscribing for the same resource could create the {@link EditableResource}
	 * twice. Of course, we could have locked on the entire map, which would have solved this, but with
	 * a big performance impact. 
	 * 
	 * @see NamedLockPool
	 * @see #subscribe()
	 * TODO CS/STFL cred ca dupa mutare runnable, putem pune inapoi protected
	 * 
	 */
	public NamedLockPool namedLockPool = new NamedLockPool();
	
	/**
	 * Number of seconds until a lock expires. This property is transferred
	 * at startup to the client, by {@link InitializeEditorSupportClientCommand}.
	 * 
	 * 
	 */
	public int lockLeaseSeconds = 10;

	/**
	 * Used to schedule unlock operations to be executed when lock expires.
	 * 
	 * 
	 */
	private ScheduledExecutorService scheduler = CommunicationPlugin.getInstance().getScheduledExecutorServiceFactory().createScheduledExecutorService();
	
	protected AtomicInteger collaborativeFigureModelsIdFactory = new AtomicInteger(1);
	
	private RunnableWithParam<Void, EditableResource> standardRemoveEditableResourceRunnable = new RunnableWithParam<Void, EditableResource>() {
		@Override
		public Void run(EditableResource editableResource) {
			editableResources.remove(editableResource.getEditorInput());
			return null;
		}
	};

	private class WebCommunicationChannelAndEditableResources {
		private CommunicationChannel webCommunicationChannel;
		private List<EditableResource> editableResources = new ArrayList<EditableResource>();
	}

	///////////////////////////////////////////////////////////////
	// JMX Methods
	///////////////////////////////////////////////////////////////

	@Override
	public Collection<String> getStatefulClientIdsForCommunicationChannel(
			CommunicationChannel communicationChannel) {
		Collection<String> result = new HashSet<String>();
		// build the inverse hierarchy
		for (EditableResource er : editableResources.values()) {
			namedLockPool.lock(er.getEditableResourcePath());
			try {
				for (EditableResourceClient erc : er.getClients()) {
					if (communicationChannel.equals(erc.getCommunicationChannel())) {
						result.add(erc.getStatefulClientId());
					}
				}
			} finally {
				namedLockPool.unlock(er.getEditableResourcePath());
			}
		}
		
		return result;
	}
	
	/**
	 * 
	 */
	@Override
	public String printStatefulDataPerCommunicationChannel(String webCommunicationChannelIdFilter, String linePrefix) {
		// clean parameters
		if ("".equals(webCommunicationChannelIdFilter) || "String".equals(webCommunicationChannelIdFilter)) {
			webCommunicationChannelIdFilter = null;
		}
		if ("String".equals(linePrefix)) { 
			linePrefix = "";
		}
		
		StringBuffer sb = new StringBuffer();

		Map<Object, WebCommunicationChannelAndEditableResources> map = new HashMap<Object, EditorStatefulService.WebCommunicationChannelAndEditableResources>();
		
		// build the inverse hierarchy
		for (EditableResource er : editableResources.values()) {
			namedLockPool.lock(er.getEditableResourcePath());
			try {
				for (EditableResourceClient erc : er.getClients()) {
					// execute if no filter or if filter matches
					if (webCommunicationChannelIdFilter == null || webCommunicationChannelIdFilter.equals(erc.getCommunicationChannel().getId())) {
						
						// find or create entry
						WebCommunicationChannelAndEditableResources entry = map.get(erc.getCommunicationChannel().getId());
						if (entry == null) {
							entry = new WebCommunicationChannelAndEditableResources();
							entry.webCommunicationChannel = erc.getCommunicationChannel();
							map.put(erc.getCommunicationChannel().getId(), entry);
						}
						
						// add resource to the list
						entry.editableResources.add(er);
					}
				}
			} finally {
				namedLockPool.unlock(er.getEditableResourcePath());
			}
		}
		
		// print
		for (WebCommunicationChannelAndEditableResources entry : map.values()) {
			sb.append(linePrefix).append(entry.webCommunicationChannel).append("\n");
			for (EditableResource er : entry.editableResources) {
				sb.append(linePrefix).append("  ").append(er).append("\n");
			}
		}
		
		return sb.toString();
	}

	/**
	 * 
	 */
	@Override
	public String printEditableResources() {
		StringBuffer sb = new StringBuffer();
		
		for (EditableResource er : editableResources.values()) {
			// TODO CS/STFL de scos .toString()
			namedLockPool.lock(er.getEditorInput().toString());
			try {
				sb.append(er).append("\n");
				for (EditableResourceClient erc : er.getClients()) {
					sb.append("  ").append(erc).append("\n");
				}
			} finally {
				// TODO CS/STFL de scos .toString()
				namedLockPool.unlock(er.getEditorInput().toString());
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * @author Mariana
	 * 
	 */
	@Override
	public void reloadEditableResource(String editableResourcePath, boolean displayMessageToClient) { 
		EditableResource editableResource = editableResources.get(editableResourcePath);
		if (editableResource != null) {
			reloadEditableResource(editableResource, displayMessageToClient);
		} else {
			throw new RuntimeException(String.format("Path %s is not correct!", editableResourcePath));
		}
	}
	
	/**
	 * 
	 */
	public int getNumberOfRegisteredLocksForNamedLockPool() {
		return namedLockPool.getNumberOfRegisteredLocks();
	}

	

	/**
	 * 
	 */
	@Override
	public void unsubscribeClientForcefully(String communicationChannelId, String editableResourcePath) {
		EditableResource er = editableResources.get(editableResourcePath);
		if (er == null) {
			throw new RuntimeException("ER not found for path = " + editableResourcePath);
		}
		
		EditableResourceClient clientFound = null;
		for (EditableResourceClient client : er.getClients()) {
			if (communicationChannelId.equals(client.getCommunicationChannel().getId())) {
				clientFound = client;
				break;
			}
		}
		if (clientFound == null) {
			throw new RuntimeException("Client not found for this ER, for id = " + communicationChannelId);
		}
		
		unsubscribeClientForcefully(clientFound, editableResourcePath);
	}

	/**
	 * 
	 */
	@Override
	public void subscribeClientForcefully(String communicationChannelId, String editableResourcePath) {
		CommunicationChannel channel = CommunicationPlugin.getInstance().getCommunicationChannelManager().getCommunicationChannelById(communicationChannelId);
		if (channel == null) {
			throw new IllegalArgumentException("WebCommunicationChannel not found for id = " + communicationChannelId);
		}
		
		subscribeClientForcefully(channel, editableResourcePath);
	}
	
	///////////////////////////////////////////////////////////////
	// Normal methods
	///////////////////////////////////////////////////////////////
	
	/**
	 * 
	 */
	public String getEditorName() {
		return editorName;
	}

	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}

	/**
	 * Has the same behavior like the similar method from AS <code>BasicEditorDescriptor</code>. 
	 * 
	 */
	public String calculateStatefulClientId(String editableResourcePath) {
		// only one / because the path already contains a /
		return editorName + ":/" + editableResourcePath;
	}
	
//	/**
//	 * Should return the ID of this service (as registered
//	 * in {@link ServiceRegistry}.
//	 * 
//	 * 
//	 */
//	public abstract String getServiceId();
	
	/**
	 * Should return <code>true</code> if the client applies the modification
	 * immediately without waiting for server's response (e.g. text files). 
	 * <code>false</code> otherwise (e.g. diagrams).
	 * 
	 * @see method with same name from AS Class <code>EditorFrontendController</code>.
	 * 
	 * 
	 */
	protected abstract boolean areLocalUpdatesAppliedImmediately();

	/**
	 * Factory method. Should return a new (unpopulated) {@link EditableResource} instance
	 * handled by this class.
	 * 
	 * 
	 */
	protected abstract EditableResource createEditableResourceInstance();

	/**
	 * Invokes {@link #loadEditableResource()}, being exception proof. If the resource is slave => we add
	 * it to the master.
	 * 
	 * @return <code>false</code> if an error is thrown while loading.
	 * @see #disposeEditableResourceSafe()
	 * 
	 * @author Cristi
	 * @author Mariana
	 * 
	 */
	private boolean loadEditableResourceSafe(StatefulServiceInvocationContext context, EditableResource editableResource) {
		logger.debug("Loading Editable Resource = {}", editableResource.getEditableResourcePath());

		// TODO CS/STFL de reactivat logica de stergere lista cand nu mai avem slave: cand vom face lock si la Master cand operam pe slave
//		if (editableResource.getMasterEditableResource().getSlaveEditableResources() == null) {
//			editableResource.getMasterEditableResource().setSlaveEditableResources(new ArrayList<EditableResource>());
//		}
		
		try {
			loadEditableResource(context, editableResource);
		} catch (FileNotFoundException e) {
			// Mariana: context may be null if the resource was reloaded by the resource changed listener
			if (context != null) {
				context.getCommunicationChannel().appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
						"Resource not Found", String.format("The resource %s was not found", editableResource.getEditableResourcePath()), DisplaySimpleMessageClientCommand.ICON_WARNING));
			}
			// TODO CS/FP2 dezactivat act listener
//			SingletonRefsInEditorPluginFromWebPlugin.INSTANCE_ACTIVITY_LISTENER.removeFromRecentResources(getFriendlyEditableResourcePath(calculateStatefulClientId(editableResource.getEditableResourcePath())));
			logger.error(String.format("Error while loading resource %s", editableResource.getEditableResourcePath()), e);
			return false;
		} catch (Throwable e) {
			if (context != null) {
				context.getCommunicationChannel().appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
						"Error on Load Resource", String.format("The resource %s cannot be open; it may be corrupted. The following error was encountered: %s", editableResource.getEditableResourcePath(), e), DisplaySimpleMessageClientCommand.ICON_ERROR));
			}
			logger.error(String.format("Error while loading resource %s", editableResource.getEditableResourcePath()), e);
			return false;			
		}
		
		// Mariana: adding the new resource to the list of slaves here, since removing it is done at dispose()
		// this is to ensure that the slaves do not lose their master in case of reloading
		if (editableResource.getMasterEditableResource() != null) {
			editableResource.getMasterEditableResource().getSlaveEditableResources().add(editableResource);
		}
		return true;
	}
	
	/**
	 * Should load the content of the {@link EditableResource} (e.g. from the disk) and store
	 * it in the {@link EditableResource} object.
	 * 
	 * <p>
	 * Should throw {@link FileNotFoundException} if the resource (file, diagram, etc) is not found. 
	 * If it encounters issues while loading, it should throw another type of exception.
	 * 
	 * 
	 */
	protected abstract void loadEditableResource(StatefulServiceInvocationContext context, EditableResource editableResource) throws FileNotFoundException;
	
	/**
	 * Invokes {@link #disposeEditableResource()}, being exception proof. If the resource is slave => we remove
	 * it from the master.
	 * 
	 * @see #loadEditableResourceSafe()
	 * 
	 */
	private void disposeEditableResourceSafe(EditableResource editableResource) {
		logger.debug("Disposing Editable Resource = {}", editableResource.getEditableResourcePath());
		if (editableResource.getMasterEditableResource() != null) {
			editableResource.getMasterEditableResource().getSlaveEditableResources().remove(editableResource);
		}
		try {
			disposeEditableResource(editableResource);
		} catch (Throwable e) {
			// we catch it and we let the normal logic of the caller continue
			logger.error(String.format("Error while disposing resource %s. This may lead to memory leak.", editableResource.getEditableResourcePath()), e);
		}
	}

	/**
	 * Should do cleanup for the resource.
	 * 
	 */
	protected abstract void disposeEditableResource(EditableResource editableResource);
	
	/**
	 * Reloads an {@link EditableResource} by first disposing and then loading it. If the resource does not exist
	 * anymore (i.e. after reloading a model file, some diagrams might have been deleted), clients are unsubscribed
	 * forcefully from it.
	 * 
	 * <p>
	 * If <code>displayMessageToClient</code>, messages are sent to the subscribed clients, to notify about the resource 
	 * being reloaded or removed.
	 * 
	 * <p>
	 * <b>NOTE</b>: This method delegates to {@link #reloadEditableResource_disposeWithSlaves()} that disposes the master + slaves,
	 * and after that delegates to {@link #reloadEditableResource_loadWithSlaves()}, that loads the master + slaves. If we did otherwise
	 * (i.e. master dispose + load, for each slave: dispose + load), when processing slaves, they would be in a state where they still reference the old
	 * content, but their master references the new content. This might lead to subtle issues, difficult to track. 
	 * 
	 * @author Mariana
	 * @author Cristi
	 * 
	 */
	protected void reloadEditableResource(EditableResource editableResource, boolean displayMessageToClient) {
		namedLockPool.lock(editableResource.getEditableResourcePath());
		try {
			List<EditableResource> slaveEditableResourcesThatWereUnloaded = reloadEditableResource_disposeWithSlaves(editableResource);
			reloadEditableResource_loadWithSlaves(editableResource, slaveEditableResourcesThatWereUnloaded, displayMessageToClient);
		} finally {
			namedLockPool.unlock(editableResource.getEditableResourcePath());
		}
	}
	
	/**
	 * @see #reloadEditableResource()
	 * 
	 */
	protected List<EditableResource> reloadEditableResource_disposeWithSlaves(EditableResource editableResource) {
		List<EditableResource> slaveEditableResources = null;
		if (editableResource.getSlaveEditableResources() != null) {
			// create a copy here; the lines below will empty this list, so a copy is necessary
			slaveEditableResources = new ArrayList<EditableResource>(editableResource.getSlaveEditableResources());
		}
		
		for (EditableResourceClient client : editableResource.getClients()) {
			doOnClientUnsubscribed(editableResource, client);
		}		

		disposeEditableResourceSafe(editableResource);
		
		if (slaveEditableResources != null) {
			for (EditableResource slave : slaveEditableResources) {
				slave.getEditorStatefulService().reloadEditableResource_disposeWithSlaves(slave);
			}
		}
		return slaveEditableResources;
	}
	
	/**
	 * @see #reloadEditableResource()
	 * 
	 */
	protected void reloadEditableResource_loadWithSlaves(EditableResource editableResource, List<EditableResource> slaveEditableResources, boolean displayMessageToClient) {
		if (!loadEditableResourceSafe(null, editableResource)) {
			unsubscribeAllClientsForcefully(editableResource.getEditableResourcePath(), true);
		}
		
		for (EditableResourceClient client : editableResource.getClients()) {
			doOnClientSubscribed(editableResource, client);

			if (displayMessageToClient) {
				client.getCommunicationChannel().appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(
								"Info", 
								"Resource " + editableResource.getLabel() + " has been modified on the file system, thus the corresponding editor(s) have been refreshed.", 
								DisplaySimpleMessageClientCommand.ICON_INFORMATION));
			}
		}
		
		if (slaveEditableResources != null) {
			slaveEditableResources = new ArrayList<EditableResource>(slaveEditableResources);
			for (EditableResource slave : slaveEditableResources) {
				slave.getEditorStatefulService().reloadEditableResource_loadWithSlaves(slave, null, displayMessageToClient);
			}
		}
	}

	/**
	 * 
	 */
	public EditableResource getEditableResource(String editableResourcePath) {
		return editableResources.get(editableResourcePath);
	}

	/**
	 * Triggers the save custom logic (i.e. {@link #doSaveEditableResource(EditableResource)}). If
	 * the dirty state changes (i.e. normal behavior), the {@link EditableResource} will be dispatched
	 * towards the clients.
	 * 
	 * <p>
	 * If the {@link EditableResource} is slave, a WARNING is logged and we delegate to it's master. This is not
	 * normal, because the client always takes care to send the master (for optimization purposes).
	 * 
	 * 
	 */
	public void save(StatefulServiceInvocationContext context, String editableResourcePath) {
		AuditDetails auditDetails = new AuditDetails(logger, SAVE_ER_AUDIT_CATEGORY, getFriendlyEditableResourcePath(calculateStatefulClientId(editableResourcePath)));
		namedLockPool.lock(editableResourcePath);
		try {
			EditableResource editableResource = editableResources.get(editableResourcePath);
			if (editableResource == null) {
				logger.error("The Editable Resource with path = {} was not found", editableResourcePath);
				return;
			}
			
//			if (editableResource.getMasterEditableResource() != null) {
//				logger.warn("Save command arrived directly on slave editable resource = {}. Client should have called for the master editable resource. Delegating to master.", editableResource.getEditorInput());
//				EditorBackend masterEditorBackend = editableResource.getMasterEditableResource().getEditorBackend();
//				masterEditorBackend.saveEditableResource(context, editableResource.getMasterEditableResource().getEditorInput());
//			} else {
				boolean initialDirtyState = editableResource.isDirty();
				doSave(editableResource);	
				if (initialDirtyState != editableResource.isDirty() && !context.getCommunicationChannel().isDisposed()) {
					dispatchEditableResourceStatus(editableResource);
				}
//			}
				// TODO CS/FP2 dezact act listener
//				SingletonRefsInEditorPluginFromWebPlugin.INSTANCE_ACTIVITY_LISTENER.updateLastSave(getFriendlyEditableResourcePath(calculateStatefulClientId(editableResourcePath)));
		} finally {
			namedLockPool.unlock(editableResourcePath);
			LogUtil.audit(auditDetails);
		}
	}

	/**
	 * Must be implemented by extending classes. It is responsible with saving
	 * the {@link EditableResource}. Slave {@link EditableResource} should throw an
	 * {@link UnsupportedOperationException}.
	 * 
	 * 
	 */
	protected abstract void doSave(EditableResource editableResource);

	/**
	 * Sends the given content to the client (using an {@link UpdateContentClientCommand}).
	 * When the server logic needs to send content to the client, it should always use this method 
	 * (and not to send data directly to the client).
	 * 
	 * 
	 */
	public void sendContentUpdateToClient(EditableResource editableResource, EditableResourceClient client, Object content, boolean isFullContent) {
		if (logger.isTraceEnabled()) {
			logger.trace("For Editable Resource = {}, sending to client = {} content = {}; isFullContent = {}", new Object[] { editableResource.getEditableResourcePath(), client.getCommunicationChannel(), content, isFullContent });
		}
		invokeClientMethod(client.getCommunicationChannel(), client.getStatefulClientId(), "updateContent", new Object[] { content, isFullContent });
	}

	/**
	 * Should send the initial content to a client (i.e. when a new client is connected).
	 * Should use {@link #sendContentUpdateToClient()}. 
	 * 
	 * 
	 */
	protected abstract void sendFullContentToClient(EditableResource editableResource, EditableResourceClient client);

	/**
	 * Should apply the updates to the {@link EditableResource}, and dispatch them
	 * to the existing clients (using {@link #sendContentUpdateToClient()}.
	 * 
	 * 
	 */
	protected abstract void updateEditableResourceContentAndDispatchUpdates(StatefulServiceInvocationContext context, EditableResource editableResource, Object updatesToApply);

	@Override
	public void communicationChannelCreated(
			CommunicationChannel webCommunicationChannel) {
		// do nothing
	}

	/**
	 * 
	 */
	@Override
	public void communicationChannelDestroyed(
		CommunicationChannel webCommunicationChannel) {
		// because the map is a ConcurrentHashMap (and not a synchronized map), means that during
		// this iteration other threads might add and remove. 
		// If they add entries, we are not impacted here: for sure newly added EditableResources 
		// won't have this channel among the clients, because
		// this channel has just been destroyed
		for (final Iterator<EditableResource> iter = editableResources.values().iterator(); iter.hasNext(); ) {
			EditableResource er = iter.next();
			EditableResourceClient erc = er.getEditableResourceClientByCommunicationChannelThreadSafe(webCommunicationChannel);
			if (erc != null) {
				// now other threads may operate on the resource: add client, update content/dispatch, remove client.
				// But we are sure that the resource won't disappear from the editableResources map, because there
				// is at least this client/communication channel preventing its removal
				// no need to lock, because unsubscribe() locks anyway
				// TODO CS/STFL de scos .toString()
				unsubscribeInternal(webCommunicationChannel, er.getEditorInput().toString(), new RunnableWithParam<Void, EditableResource>() {
					@Override
					public Void run(EditableResource param) {
						// by using iterator.remove(), 
						// this implementation of "remove" makes sure that the map won't complain about
						// "concurrent modification exception" (i.e. because while iterating on it, we 
						// remove an element). However, from what I've read, ConcurrentHashMap doesn't throw
						// this. 
						// But anyway, we are safe with this version, and we'll be safe with another Map implementation.
						iter.remove();
						return null;
					}
				}, false);
			}
		}
	}

	/**
	 * This is NOT thread safe.
	 * 
	 * @param clientInvocationOptions TODO
	 */
	public void dispatchEditableResourceStatus(EditableResource editableResource) {
		for (EditableResourceClient client : editableResource.getClients()) {
			if (logger.isTraceEnabled()) {
				logger.trace(
						"For Editable Resource = {}, sending updated Editable Resource Status to client = {}",
						editableResource.getEditableResourcePath(),
						client.getCommunicationChannel());
			}
			invokeClientMethod(client.getCommunicationChannel(), client.getStatefulClientId(), "updateEditableResourceStatus", new Object[] { editableResource });
		}
	}	
	
	/**
	 * This is NOT thread safe. Should be called AFTER adding the new <code>client</code> in the list.
	 * 
	 * <p>
	 * Sends the full clients list to the <code>client</code> (all clients without itself). Sends <code>client</code> to the other
	 * existing clients.
	 * 
	 * 
	 */
	protected void dispatchClientSubscribedToEditableResource(EditableResource editableResource, EditableResourceClient client) {
		if (logger.isTraceEnabled()) {
			logger.trace("For Editable Resource = {}, dispatching client added notification; added client = {}", editableResource.getEditableResourcePath(), client.getCommunicationChannel());
		}
		
		if (editableResource.getClients().size() <= 1) {
			return;
		}

		// build the list without the calling client
		List<EditableResourceClient> existingClients = new ArrayList<EditableResourceClient>(editableResource.getClients().size() - 1);
		for (EditableResourceClient otherClient : editableResource.getClients()) {
			if (!client.equals(otherClient)) { // will skip the currentClient
				existingClients.add(otherClient);
			}
		}

		invokeClientMethod(client.getCommunicationChannel(), client.getStatefulClientId(), "newClientsAdded", new Object[] { existingClients, true });
		
		List<EditableResourceClient> addedClient = Collections.singletonList(client);
		
		for (EditableResourceClient otherClient : existingClients) {
			if (logger.isTraceEnabled()) {
				logger.trace(
						"For Editable Resource = {}, sending client added notification, to client = {}. Added client = {}",
						new Object[] {
							editableResource.getEditableResourcePath(),
							otherClient.getCommunicationChannel(),
							client.getCommunicationChannel()
						});
			}
			invokeClientMethod(otherClient.getCommunicationChannel(), otherClient.getStatefulClientId(), "newClientsAdded", new Object[] { addedClient });
		}
	}
	
	/**
	 * This is NOT thread safe. Should be called AFTER removing the <code>client</code> from the list.
	 * 
	 * <p>
	 * Sends <code>client</code> to the other existing clients.
	 * 
	 * 
	 */
	public void dispatchClientUnsubscribedFromEditableResource(EditableResource editableResource, EditableResourceClient client) {
		if (logger.isTraceEnabled()) {
			logger.trace("For Editable Resource = {}, dispatching client removed notification; removed client = {}", editableResource.getEditableResourcePath(), client.getCommunicationChannel());
		}

		for (EditableResourceClient otherClient : editableResource.getClients()) {
			if (logger.isTraceEnabled()) {
				logger.trace(
						"For Editable Resource = {}, sending client removed notification to client = {}. Removed client = {}",
						new Object[] {
							editableResource.getEditableResourcePath(),
							otherClient.getCommunicationChannel(),
							client.getCommunicationChannel()
						});
			}
			invokeClientMethod(otherClient.getCommunicationChannel(), otherClient.getStatefulClientId(), "clientRemoved", new Object[] { client.getCommunicationChannel().getId() });
		}

	}


	/**
	 * This is NOT thread safe.
	 * 
	 * <p>
	 * If the lock is successful (i.e. resource not locked, or locked by the same client), returns
	 * <code>true</code>. If the lock is acquired, then a notification is sent to the client; if the
	 * lock is extended, no notification is sent.
	 * 
	 * <p>
	 * If the lock is not successful, returns false and sends to the client a {@link DisplaySimpleMessageClientCommand}
	 * with a message.
	 * 
	 * 
	 */
	protected boolean tryLock(EditableResource editableResource, EditableResourceClient client) {
		if (!editableResource.isLocked() || client.equals(editableResource.getLockOwner())) {				
			Calendar calendar = Calendar.getInstance();
			editableResource.setLockUpdateTime(calendar.getTime());
			calendar.add(Calendar.SECOND, lockLeaseSeconds);
			editableResource.setLockExpireTime(calendar.getTime());	
			if (logger.isTraceEnabled()) {
				logger.trace("Lock acquired for Editable Resource = {}, by client = {} until {}", new Object[] { editableResource.getEditableResourcePath(), client.getCommunicationChannel(), editableResource.getLockExpireTime()});
			}
			if (!editableResource.isLocked()) {
				// first lock => schedule the runnable
				editableResource.setLocked(true);	
				editableResource.setLockOwner(client);
				scheduler.schedule(new UnlockEditableResourceRunnable(this, editableResource.getEditorInput(), client, scheduler), lockLeaseSeconds, TimeUnit.SECONDS);
			}
			// else we are in the case: renew the lock by the lock owner;
			// when scheduled runnable will be triggered, it will reschedule according to the new time
		}
		boolean lockByCurrentClient = client.equals(editableResource.getLockOwner()); 
		// TODO CS/STFL dezactivat pt. lansare caci face pb la model
//		if (!lockByCurrentClient) {
//			// locking operation did not succeed; sending a message to the user
//			if (logger.isTraceEnabled()) {
//				logger.debug("Lock failed for Editable Resource = {}, client = {}. Sending full content...", editableResource.getEditorInput(), client.getCommunicationChannel());
//			}
//			DisplaySimpleMessageCommand command = new DisplaySimpleMessageCommand("Warning", String.format("Locking '%s' has failed. A few changes done by you may be reverted.", editableResource.getLabel()), DisplaySimpleMessageCommand.ICON_WARNING);
//			client.getCommunicationChannel().sendObject(command);
//		}
		return lockByCurrentClient;
	}
	
	/**
	 * Delegate to {@link #subscribeClientForcefully(CommunicationChannel, String, boolean)} with <code>handleAsClientSubscription</code> <code>false</code>.
	 * 
	 * @author Mariana
	 */
	public EditableResource subscribeClientForcefully(CommunicationChannel communicationChannel, String editableResourcePath) {
		return subscribeClientForcefully(communicationChannel, editableResourcePath, false);
	}
	
	/**
	 * 
	 */
	public EditableResource subscribeClientForcefully(CommunicationChannel communicationChannel, String editableResourcePath, boolean handleAsClientSubscription) {
		EditorStatefulClientLocalState state = new EditorStatefulClientLocalState();
		state.setEditableResourcePath(editableResourcePath);
		state.setForcingSubscriptionFromServer(true);
		state.setHandleAsClientSubscription(handleAsClientSubscription);
		subscribe(new StatefulServiceInvocationContext(communicationChannel, null, null), state);
		return getEditableResource(editableResourcePath);
	}

	/**
	 * 
	 */
	public void unsubscribeClientForcefully(EditableResourceClient client, String editableResourcePath) {
		logger.debug("Unsubscribing forcefully from Editable Resource = {}, client = {}", editableResourcePath, client.getCommunicationChannel());

		namedLockPool.lock(editableResourcePath);
		try {
			if (!client.getCommunicationChannel().isDisposed()) { // e.g. the channel is disposed when a client leaves, that has a model + diagram open
				invokeClientMethod(client.getCommunicationChannel(), client.getStatefulClientId(), "unsubscribedForcefully", null);
			}
			unsubscribeInternal(client.getCommunicationChannel(), editableResourcePath, standardRemoveEditableResourceRunnable, false);
		} finally {
			namedLockPool.unlock(editableResourcePath);
		}
	}
	
	/**
	 * Unsubscribes all the clients from the {@link EditableResource}.
	 * 
	 * <p>
	 * If <code>displayMessageToClient</code>, notifies the clients that the resource has been removed.
	 * 
	 * @author Cristi
	 * @author Mariana
	 * 
	 */
	public void unsubscribeAllClientsForcefully(String editableResourcePath, boolean displayMessageToClient) {
		logger.debug("Unsubscribing all clients forcefully from Editable Resource = {}", editableResourcePath);
		
		namedLockPool.lock(editableResourcePath);
		try {
			EditableResource er = editableResources.get(editableResourcePath);
			if (er == null) {
				logger.warn("Editable Resource not found for path = {}", editableResourcePath);
				return;
			}
			
			// we use a copy to avoid ConcurrentModification...; it would have been too complicated to pass
			// a runnable to delete using this iterator, and given the fact that this operation is not very 
			// frequent, we chose to copy
			List<EditableResourceClient> existingClients = new ArrayList<EditableResourceClient>(er.getClients().size());
			existingClients.addAll(er.getClients());
			
			for (EditableResourceClient client : existingClients) {
				if (displayMessageToClient) {
					client.getCommunicationChannel().appendOrSendCommand(
							new DisplaySimpleMessageClientCommand(
									"Info", 
									"Resource " + er.getLabel() + " has been removed, thus you have been unsubscribed from it.", 
									DisplaySimpleMessageClientCommand.ICON_INFORMATION));
				}
				unsubscribeClientForcefully(client, editableResourcePath);
			}
		} finally {
			namedLockPool.unlock(editableResourcePath);
		}
	}

	/**
	 * 
	 */
	protected void doSubscribeForcefully(StatefulServiceInvocationContext context, EditorStatefulClientLocalState state, EditableResourceClient client) {
		if (context.getStatefulClientId() != null) {
			logger.warn("For Editable Resource = {} and client = {}, trying to subscribe forcefully, but the call seems to come from client", state.getEditableResourcePath(), client.getCommunicationChannel());
			return;
		}
		
		client.getCommunicationChannel().appendOrSendCommand(new CreateEditorStatefulClientCommand(
				state.getEditableResourcePath(), editorName, state.isHandleAsClientSubscription()));
		
		// set the new generated id
		String statefulClientId = calculateStatefulClientId(state.getEditableResourcePath());
		client.setStatefulClientId(statefulClientId);
		context.setStatefulClientId(statefulClientId);
	}
	
	/**
	 * This is NOT thread safe.
	 * 
	 * <p>
	 * Unlocks the resource (if <code>client</code> is the lock owner).
	 * 
	 * TODO CS/STFL cred ca dupa mutare runnable, putem pune inapoi protected
	 * 
	 */
	public boolean unlock(EditableResource editableResource, EditableResourceClient client) {
		boolean unlockedNow = false;
		if (editableResource.isLocked() && (client == null || client.equals(editableResource.getLockOwner()))) {
			editableResource.setLocked(false);
			editableResource.setLockExpireTime(null);
			unlockedNow = true;
			if (logger.isTraceEnabled()) {
				logger.trace("Unlocked Editable Resource = {}, client = {}", editableResource.getEditableResourcePath(), client != null ? client.getCommunicationChannel() : null);
			}
			// for resource in "editable" state we need to mention last user that modified the file and when
			// so we don't reset lockOwner and lockUpdateTime		
			
			// we don't shutdown the scheduled task (ScheduledFuture) because we don't have a reference towards it; when
			// the timer will trigger, the corresponding runnable won't do anything. If this is not enough, we might add
			// a reference from ER -> ScheduledFuture, to be able to cancel it quicker
		} else {
			// This shouldn't normally happen
			logger.error("Unlock failed for Editable Resource = {}, client = {}. The resource isLocked = {} by client = {}", 
					new Object[] { editableResource.getEditableResourcePath(), client != null ? client.getCommunicationChannel() : null, editableResource.isLocked(), editableResource.getLockOwner().getCommunicationChannel() } );
		}
		return unlockedNow;		
	}

//	//TODO : Temporary code (see #6777)
//	/**
//	 * 
//	 */
//	protected abstract File getEditableResourceFile(EditableResource editableResource);
	
	/**
	 * @return the <code>friendlName</code> encoded.
	 * @see EditorStatefulService#getFriendlyEditableResourcePath()
	 * 
	 * @author Cristina
	 */
	public String getFriendlyNameEncoded(String friendlyName) {
		return friendlyName;
	}
	
	/**
	 * @return the <code>friendlName</code> decoded.
	 * @see EditorStatefulService#getCanonicalEditableResourcePath()
	 * 
	 * @author Cristina
	 */
	public String getFriendlyNameDecoded(String friendlyName) {
		try {
			friendlyName = URLDecoder.decode(friendlyName, "UTF-8");		
		} catch (UnsupportedEncodingException e) {
			logger.error("Could not decode using UTF-8 charset : " + friendlyName);
		}
		return friendlyName;
	}
	
	///////////////////////////////////////////////////////////////
	// @RemoteInvocation methods
	///////////////////////////////////////////////////////////////
	
	/**
	 * Called from the client <code>EditorFrontendController</code> when a new resource is opening (e.g. from tree
	 * or at startup).<strong>NOTE:</strong> If a client has multiple editors for the same editorInput, it has only one subscription
	 * on the server.
	 * 
	 * <p>
	 * If the client is already subscribed to the {@link EditableResource} corresponding to <code>editorInput</code>,
	 * the {@link EditableResource} is returned.
	 * 
	 * <p>
	 * Otherwise, if the {@link EditableResource} exists (i.e. other clients are viewing it), the new client will
	 * subscribe to it (and we'll send the full content of the resource), and all clients will get notifications.
	 * 
	 * <p>
	 * Otherwise, if the {@link EditableResource} is not open it is loaded from disk and the flow from above
	 * is repeated.
	 * 
	 * <p>
	 * If the {@link EditableResource} is slave, we subscribe the client to its master {@link EditableResource} as well.
	 * 
	 * @author Cristi
	 * @author Mariana
	 * 
	 * 
	 */
	@Override
	@RemoteInvocation
	public void subscribe(StatefulServiceInvocationContext context, IStatefulClientLocalState statefulClientLocalState) {
		
		EditorStatefulClientLocalState state = (EditorStatefulClientLocalState) statefulClientLocalState;
		AuditDetails auditDetails = new AuditDetails(logger, SUBSCRIBE_ER_AUDIT_CATEGORY, getFriendlyEditableResourcePath(calculateStatefulClientId(state.getEditableResourcePath())));

		// lock by the path of the resource; any other thread with same path will wait
		namedLockPool.lock(state.getEditableResourcePath());

		try {
			// get the editable resource from the registry; if it doesn't exist, create the resource and register it
			// Here is the instruction that, without the synchronization on name, would have caused problems: 2 threads
			// see that there is no EditableResource for the given name => both create. Only one remains in map. This
			// may lead to memory leaks and other possibly unhealthy behaviors. 
			//
			// locking the whole list would solve the issue, but with big performance impact 
			EditableResource editableResource = editableResources.get(state.getEditableResourcePath());
			if (editableResource != null) {
				logger.debug("EditableResource found for path = {}; reusing it", state.getEditableResourcePath());
				if (editableResource.getEditableResourceClientByCommunicationChannel(context.getCommunicationChannel()) != null) {
					// client already subscribed
					// this scenario happens for tree nodes; each model that's opened => this method is called, but the client might be already subscribed
					logger.trace("For Editable Resource = {}, client = {} is already subscribed.", state.getEditableResourcePath(), context.getCommunicationChannel());
					return;
				}
			} else {
				logger.debug("EditableResource not found for path = {}; creating and registering a new one", state.getEditableResourcePath());
				editableResource = createEditableResourceInstance();
				editableResource.setEditorStatefulService(this);
				editableResource.setEditorInput(state.getEditableResourcePath());
				
				if (!loadEditableResourceSafe(context, editableResource)) {
					// i.e. load has thrown an error
					return;
				}

				editableResources.put(state.getEditableResourcePath(), editableResource);
			}
			
			if (logger.isTraceEnabled()) {
				logger.trace("For Editable Resource = {} adding client = {}. There were {} clients subscribed to this resource.", new Object[] { state.getEditableResourcePath(), context.getCommunicationChannel(), editableResource.getClients().size() });
			}
			
			EditableResourceClient client = new EditableResourceClient(context.getCommunicationChannel());
			client.setStatefulClientId(context.getStatefulClientId());

			// if this resource is a slave, subscribe to its master
			if (editableResource.getMasterEditableResource() != null) {
				editableResource.getMasterEditableResource().getEditorStatefulService().subscribeClientForcefully(client.getCommunicationChannel(), editableResource.getMasterEditableResource().getEditableResourcePath());
			}
			
			if (state.isForcingSubscriptionFromServer()) {
				// this method will populate context & client with statefulClientId
				doSubscribeForcefully(context, state, client);
			}
			
			editableResource.addEditableResourceClient(client);
			doOnClientSubscribed(editableResource, client);
			dispatchClientSubscribedToEditableResource(editableResource, client);
			
			if (editableResource.getCollaborativeFigureModels() != null) {
				invokeClientMethod(context.getCommunicationChannel(), context.getStatefulClientId(), "updateCollaborativeFigureModels", new Object[] { editableResource.getCollaborativeFigureModels(), true });
			}
				
			// TODO CS/FP2 dezact teste permisii
//			//TODO : Temporary code (see #6777)
//			// send notification to client in order to disable editing for client with no write permissions
//			if (!AbstractSecurityUtils.INSTANCE.hasWritePermission(getEditableResourceFile(editableResource))) {
//				invokeClientMethod(context.getCommunicationChannel(), context.getStatefulClientId(), "disableEditing", null);
//			}
			// TODO CS/FP2 dezact recent act 
//			SingletonRefsInEditorPluginFromWebPlugin.INSTANCE_ACTIVITY_LISTENER.updateLastAccess(editableResource);
		} finally {
			namedLockPool.unlock(state.getEditableResourcePath());
			LogUtil.audit(auditDetails);
		}
	}
	
	/**
	 * Used by {@link #subscribe()} and {@link #reloadEditableResource()}.
	 * 
	 * <p>
	 * Invokes <code>updateEditableResourceStatus()</code> and sends full content. 
	 * 
	 */
	protected void doOnClientSubscribed(EditableResource editableResource, EditableResourceClient client) {
		invokeClientMethod(client.getCommunicationChannel(), client.getStatefulClientId(), "updateEditableResourceStatus", new Object[] { editableResource });

		// send the data from this resource to the client
		sendFullContentToClient(editableResource, client);
	}

	/**
	 * 
	 */
	@RemoteInvocation
	public void unsubscribe(StatefulServiceInvocationContext context, IStatefulClientLocalState statefulClientLocalState) {
		unsubscribeInternal(
				context.getCommunicationChannel(), 
				((EditorStatefulClientLocalState) statefulClientLocalState).getEditableResourcePath(),
				standardRemoveEditableResourceRunnable, false);
	}
	
	/**
	 * Called by the client when the last editor closes. Unsubscribes the current client from the
	 * {@link EditableResource}. If no other clients use the resource, then it is disposed.
	 * 
	 * <p>
	 * Notifications are sent to all clients: the current client is told to remove the {@link EditableResource}
	 * from the system, and the other clients are informed that a client has left.
	 * 
	 * <p>
	 * If this client has the lock on the resource, the resource is unlocked.
	 * 
	 * <p>
	 * If subclasses need to add behavior here, they should use {@link #doUnsubscribeFromEditableResourceAdditionalLogic()}.
	 * 
	 * 
	 * @param saveResourceBeforeDisposing TODO
	 */
	protected void unsubscribeInternal(CommunicationChannel communicationChannel, String editableResourcePath, RunnableWithParam<Void, EditableResource> removeEditableResourceRunnable, boolean saveResourceBeforeDisposing) {
		AuditDetails auditDetails = new AuditDetails(logger, "UNSUBSCRIBE_ER", getFriendlyEditableResourcePath(calculateStatefulClientId(editableResourcePath)));

		namedLockPool.lock(editableResourcePath);
		try {
			EditableResource editableResource = editableResources.get(editableResourcePath);
			if (editableResource == null) {
				logger.error("The Editable Resource with path = {} was not found", editableResourcePath);
				return;
			}
	
			// remove the client 
			EditableResourceClient client = editableResource.removeEditableResourceClientByCommunicationChannel(communicationChannel);
			if (client == null) {
				logger.error("For the Editable Resource = {}, the client = {} is not subscribed", editableResourcePath, communicationChannel);
				return;
			}
			
			if (editableResource.isLocked() && editableResource.getLockOwner().equals(client)) {
				// if this client has lock => unlock
				unlock(editableResource, client);
				dispatchEditableResourceStatus(editableResource);
			}
			
			if (logger.isTraceEnabled())
				logger.trace("For the Editable Resource = {}, removing client = {}. Now there are {} clients subscribed to this resource.", new Object[] { editableResourcePath, communicationChannel, editableResource.getClients().size() });
			
			if (editableResource.getSlaveEditableResources() != null) {
				// force unsubscription for slave ERs
				
				// we do a copy to avoid ConcurrentModificationException. It would be complicated to pass the current iterator
				// and the performance penalty is not that big
				List<EditableResource> slaveEditableResources = new ArrayList<EditableResource>(editableResource.getSlaveEditableResources());
				for (EditableResource slaveER : slaveEditableResources) {
					EditableResourceClient slaveClient = slaveER.getEditableResourceClientByCommunicationChannelThreadSafe(communicationChannel);
					if (slaveClient != null) {
						// slave client may be null; e.g. client 1 = model, model/diagr1; client 2 = model, model/diagr2. Unsubscribing
						// client 1 from model, will iterate on diagr1, diagr2; but diagr2 is not associated with client 1, i.e. slaveClient == null
						slaveER.getEditorStatefulService().unsubscribeClientForcefully(slaveClient, slaveER.getEditableResourcePath());
					}
				}
			}

			doOnClientUnsubscribed(editableResource, client);
			dispatchClientUnsubscribedFromEditableResource(editableResource, client);

			if (editableResource.getClients().size() == 0) {
				if (editableResource.isDirty() && editableResource.getMasterEditableResource() == null && saveResourceBeforeDisposing) {
					logger.debug("Saving ER = {} automatically before dispose.", editableResource.getEditableResourcePath());
					// the save method needs only the cc; the statefulClientId is not needed; so we can generate a fake SSIC here,
					// to avoid to write another method that takes only CC, etc.
					save(new StatefulServiceInvocationContext(communicationChannel), editableResource.getEditableResourcePath());
				}
				disposeEditableResourceSafe(editableResource);
				removeEditableResourceRunnable.run(editableResource);
			}
		} finally {
			namedLockPool.unlock(editableResourcePath);
			LogUtil.audit(auditDetails);
		}
	}
	
	/**
	 * Used by {@link #unsubscribeInternal()} and {@link #reloadEditableResource()}.
	 * 
	 * <p>
	 * May be overridden if logic needs to be executed when a client disconnects from an
	 * {@link EditableResource}.
	 * 
	 * 
	 */
	protected void doOnClientUnsubscribed(EditableResource editableResource, EditableResourceClient client) {
		// do nothing by default
	}
	
	/**
	 * Called by the client when it has updates. 
	 * 
	 * <p>
	 * If the {@link EditableResource} is locked, then the attempt is unsuccessful, and if
	 * {@link #areLocalUpdatesAppliedImmediately()} then a full content is sent again to
	 * the server.
	 * 
	 * <p>
	 * Otherwise (i.e. not locked), then the resource is locked, the content update custom
	 * server logic is invoked (i.e. {@link #updateEditableResourceContentAndDispatchUpdates()}) and if
	 * the dirty state or lock has changed, the client is notified (using {@link UpdateEditableResourceClientCommand}).
	 * 
	 * <p>
	 * If the {@link EditableResource} is slave, and the dirty state changes, the notification is
	 * dispatched for the master {@link EditableResource}, not for the slave.
	 * 
	 * 
	 */
	@RemoteInvocation
	public void attemptUpdateEditableResourceContent(StatefulServiceInvocationContext context, String editableResourcePath, Object updatesToApply) {
		logger.trace("Attempting to update Editable Resource = {}, client = {}", editableResourcePath, context.getCommunicationChannel());
		namedLockPool.lock(editableResourcePath);
		try {
			EditableResource editableResource = editableResources.get(editableResourcePath);
			if (editableResource == null) {
				return;
			}
			
			// TODO CS/FP2 dezact permission check
//			//TODO : Temporary code (see #6777)
//			if (!AbstractSecurityUtils.INSTANCE.hasWritePermission(getEditableResourceFile(editableResource))) {
//				logger.error("The user cannot update content because he doesn't have write permissions!");
//				return;
//			}
			
			EditableResourceClient client = editableResource.getEditableResourceClientByCommunicationChannel(context.getCommunicationChannel());
			
			boolean initialDirtyState = editableResource.isDirty();
			boolean initialLockState = editableResource.isLocked();
	
			if (tryLock(editableResource, client)) {
				// lock acquired or renewed
				updateEditableResourceContentAndDispatchUpdates(context, editableResource, updatesToApply);
				
				// dispatch ER notification(s)
				if (editableResource.getMasterEditableResource() == null) {
					// normal Editable Resource
					if (initialDirtyState != editableResource.isDirty() || initialLockState != editableResource.isLocked()) {
						dispatchEditableResourceStatus(editableResource);
					}
				} else {
					// slave ER
					if (initialDirtyState != editableResource.isDirty()) {
						// dispatch dirty notification for the master ER
						editableResource.getMasterEditableResource().getEditorStatefulService().dispatchEditableResourceStatus(editableResource.getMasterEditableResource());
					}
					if (initialLockState != editableResource.isLocked()) {
						// dispatch lock notification for the slave ER
						dispatchEditableResourceStatus(editableResource);
					}
				}
			} else {
				// lock failed; someone else was quicker
				if (areLocalUpdatesAppliedImmediately()) {
					// reinitialize only in this case; otherwise, the client has still the content valid
					// because it waits for an update from the server
					sendFullContentToClient(editableResource, client);
				}
			}
		} finally {
			namedLockPool.unlock(editableResourcePath);
		}
	}
	
//	protected boolean lock(EditableResource editableResource, EditableResourceClient client) {
//		return EditorSupport.INSTANCE.lock(editableResource, client);
//	}

	/**
	 * 
	 */
	@RemoteInvocation
	public boolean tryLockFromButton(StatefulServiceInvocationContext context, String editableResourcePath) {
		namedLockPool.lock(editableResourcePath);
		try {
			EditableResource editableResource = editableResources.get(editableResourcePath);
			if (editableResource == null) {
				logger.error("The Editable Resource with path = {} was not found", editableResourcePath);
			}
		
			EditableResourceClient client = editableResource.getEditableResourceClientByCommunicationChannel(context.getCommunicationChannel());
			if (client == null) {
				logger.error("For the Editable Resource = {}, the client = {} is not subscribed", editableResourcePath, context.getCommunicationChannel());
			}
			
			if (tryLock(editableResource, client)) {
				dispatchEditableResourceStatus(editableResource);
				return true;
			} else {
				return false;
			}
		} finally {
			namedLockPool.unlock(editableResourcePath);
		}
	}
	
	/**
	 * 
	 */
	@RemoteInvocation
	public boolean unlockFromButton(StatefulServiceInvocationContext context, String editableResourcePath) {
		namedLockPool.lock(editableResourcePath);
		try {
			EditableResource editableResource = editableResources.get(editableResourcePath);
			if (editableResource == null) {
				logger.error("The Editable Resource with path = {} was not found", editableResourcePath);
			}
		
			EditableResourceClient client = editableResource.getEditableResourceClientByCommunicationChannel(context.getCommunicationChannel());
			if (client == null) {
				logger.error("For the Editable Resource = {}, the client = {} is not subscribed", editableResourcePath, context.getCommunicationChannel());
			}
			
			if (unlock(editableResource, client)) {
				dispatchEditableResourceStatus(editableResource);
				return true;
			} else {
				return false;
			}
		} finally {
			namedLockPool.unlock(editableResourcePath);
		}
	}
	
	/**
	 * 
	 */
	@RemoteInvocation
	public long addCollaborativeFigureModel(StatefulServiceInvocationContext context, String editableResourcePath, CollaborativeFigureModel model) {
		logger.trace("For Editable Resource = {}, client = {}, adding CollaborativeFigureModel", editableResourcePath, context.getCommunicationChannel());

		namedLockPool.lock(editableResourcePath);
		try {
			model.setId(collaborativeFigureModelsIdFactory.getAndIncrement());
			updateCollaborativeFigureModels(context, editableResourcePath, Collections.singleton(model));		
			return model.getId();
		} finally {
			namedLockPool.unlock(editableResourcePath);
		}
	}
	
	/**
	 * 
	 */
	@RemoteInvocation
	public void updateCollaborativeFigureModels(StatefulServiceInvocationContext context, String editableResourcePath, Collection<CollaborativeFigureModel> models) {
		logger.trace("For Editable Resource = {}, client = {}, updating CollaborativeFigureModel and dispatching updates", editableResourcePath, context.getCommunicationChannel());

		namedLockPool.lock(editableResourcePath);
		try {
			EditableResource editableResource = editableResources.get(editableResourcePath);
			if (editableResource == null) {
				logger.error("Editable Resource not found for path = {}", editableResourcePath);
				return;
			}
			
			if (editableResource.getCollaborativeFigureModels() == null) {
				// lazy init the collection
				editableResource.setCollaborativeFigureModels(new HashSet<CollaborativeFigureModel>());
			}
			
			for (CollaborativeFigureModel model : models) {
				// being a set, add a new instance with same id, will overwrite the existing instance
				editableResource.getCollaborativeFigureModels().add(model);
			}
			
			for (EditableResourceClient otherClient : editableResource.getClients()) {
				if (!context.getCommunicationChannel().equals(otherClient.getCommunicationChannel())) {
					// don't send to the initiator, because it has already applied the updates
					invokeClientMethod(otherClient.getCommunicationChannel(), otherClient.getStatefulClientId(), "updateCollaborativeFigureModels", new Object[] { models, false });
				}
			}
		} finally {
			namedLockPool.unlock(editableResourcePath);
		}
	}
	
	/**
	 * 
	 */
	@RemoteInvocation
	private void removeCollaborationFigureModelWithId(Collection<CollaborativeFigureModel> models, int id) {
		for (Iterator<CollaborativeFigureModel> iter = models.iterator(); iter.hasNext(); ) {
			CollaborativeFigureModel model = iter.next();
			if (model.getId() == id) {
				iter.remove();
				break;
			}
		}
	}
	
	/**
	 * 
	 */
	@RemoteInvocation
	public void removeCollaborativeFigureModels(StatefulServiceInvocationContext context, String editableResourcePath, Collection<Integer> modelIds) {
		logger.trace("For Editable Resource = {}, client = {}, removing CollaborativeFigureModel and dispatching updates", editableResourcePath, context.getCommunicationChannel());

		namedLockPool.lock(editableResourcePath);
		try {
			EditableResource editableResource = editableResources.get(editableResourcePath);
			if (editableResource == null) {
				logger.error("Editable Resource not found for path = {}", editableResourcePath);
				return;
			}
			
			if (editableResource.getCollaborativeFigureModels() == null) {
				logger.error("Trying to remove some CollaborativeFigureModels, but the list is null");
				return;
			}
			
			for (int id : modelIds) {
				removeCollaborationFigureModelWithId(editableResource.getCollaborativeFigureModels(), id);
			}
			
			if (editableResource.getCollaborativeFigureModels().size() == 0) {
				editableResource.setCollaborativeFigureModels(null);
			}
			
			for (EditableResourceClient otherClient : editableResource.getClients()) {
				if (!context.getCommunicationChannel().equals(otherClient.getCommunicationChannel())) {
					// don't send to the initiator, because it has already applied the updates
					invokeClientMethod(otherClient.getCommunicationChannel(), otherClient.getStatefulClientId(), "removeCollaborativeFigureModels", new Object[] { modelIds });
				}
			}
		} finally {
			namedLockPool.unlock(editableResourcePath);
		}
	}

	/**
	 * 
	 */
	public void revealEditor(CommunicationChannel channel, String editableResourcePath) {
		EditableResource editableResource = getEditableResource(editableResourcePath);
		if (editableResource == null)
			return; // Resource wasn't opened at all

		EditableResourceClient editableResourceClient = editableResource.getEditableResourceClientByCommunicationChannel(channel);
		if (editableResourceClient == null)
			return; // The client didn't manage to open the resource
		
		invokeClientMethod(channel, editableResourceClient.getStatefulClientId(), "revealEditor", new Object[] {});
	}

	/**
	 * Doesn't have a channel parameter because this method would always be called for a resource after loading it's master resource.
	 * <p>
	 * Doesn't have validationProblems parameter because this method is safely called with input created internally whereas the other method
	 * is called with input given by the user.
	 * 
	 * @see #getCanonicalEditableResourcePath(String, CommunicationChannel, StringBuffer)
	 *  
	 * 
	 * @author Sorin
	 * 
	 */
	public String getFriendlyEditableResourcePath(String canonicalEditableResourcePath) {
		return getFriendlyNameEncoded(canonicalEditableResourcePath);
	}

	/**
	 * @param channel needed because certain editors may present resource from a master resource that first must be opened for the current user (like diagram from model)
	 * @param validationProblems problems resulted from validating the path. This must be rigorously validated because it is called with input directly from user
	 * @return the canonical editableResourcePath or null if there were validation problems
	 * 
	 * 
	 * @author Sorin
	 * 
	 */
	public String getCanonicalEditableResourcePath(String friendlyEditableResourcePath, CommunicationChannel channel, StringBuffer validationProblems) {
		return getFriendlyNameDecoded(friendlyEditableResourcePath);
	}
	
	/**
	 * Method called to navigate to the given <code>fragment</code> by the external url feature.
	 * Each implementation should interpret <code>fragment</code> in it's own way.
	 *  
	 * @param editableResourcePath the resource that was already opened in an editor
	 * @param fragment a string used to located the desired fragment. It is recommended  to have a key=value
	 * @param channel the client in which the editableResourcePath is opened in an editor.
	 * 
	 * 
	 * @author Sorin
	 * 
	 */
	public void navigateToFragment(CommunicationChannel channel, String editableResourcePath, String fragment) {
	}
	
	/**
	 * @see ActivityService#getIconUrl(String)
	 * 
	 * @author Mariana
	 */
	public String getIconUrl() {
		return createEditableResourceInstance().getIconUrl();
	}
}