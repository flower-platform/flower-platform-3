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

import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.file_event.FileEvent;
import org.flowerplatform.file_event.IFileEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Responsible with listening for resource changed notifications and reloading their corresponding 
 * {@link EditableResource}s.
 * 
 * @author Mariana
 * 
 * 
 */
public abstract class FileBasedEditorStatefulService extends EditorStatefulService implements IFileEventListener
//	implements IResourceChangeListener 
	{
	
	/**
	 * 
	 */
	private static final Logger logger = LoggerFactory.getLogger(FileBasedEditorStatefulService.class);
	
	/**
	 * Registers the listener
	 */
	public FileBasedEditorStatefulService() {
		CommonPlugin.getInstance().getFileEventDispatcher().addFileEventListener(this);
	}

	@Override
	protected void loadEditableResource(StatefulServiceInvocationContext context, EditableResource editableResource) throws FileNotFoundException {
		FileBasedEditableResource er = (FileBasedEditableResource) editableResource;
		File file = new File(CommonPlugin.getInstance().getWorkspaceRoot(), er.getEditableResourcePath());
		er.setFile(file);
		if (!file.exists()) {
			throw new FileNotFoundException(editableResource.getEditableResourcePath());
		}
	}

	@Override
	public void notify(FileEvent event) {
		File file = (event.getEvent() == FileEvent.FILE_RENAMED) ? event.getOldFile() : event.getFile();
		String filePathRelativeToWorkspace = "/" + CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(file);
		EditableResource editableResource = getEditableResource(filePathRelativeToWorkspace);
		if(editableResource != null) {
			switch (event.getEvent()) {
				case FileEvent.FILE_REFRESHED : {
					if(!editableResource.isSynchronized()) {
						processResourceChanged(editableResource);
					}
					break;
				}
				case FileEvent.FILE_MODIFIED : {
					processResourceChanged(editableResource);
					break;
				}	
				case FileEvent.FILE_DELETED : {
					processResourceRemoved(editableResource);
					break;
				}	
				case FileEvent.FILE_RENAMED : {
					// for now just remove the file from editor
					processResourceRemoved(editableResource);
					break;
				}	
				default:
					break;
				}
		} else {
			switch (event.getEvent()) {
				case FileEvent.FILE_REFRESHED : {
					processResourcesRefreshed(editableResource);
					break;
				}	
				case FileEvent.FILE_DELETED : {
					for(EditableResource edRed : editableResources.values()) {
						if(edRed.getEditableResourcePath().contains(filePathRelativeToWorkspace)) {
							processResourceRemoved(edRed);
						}
					}
					break;
				}	
				case FileEvent.FILE_RENAMED : {
					for(EditableResource edRed : editableResources.values()) {
						if(edRed.getEditableResourcePath().contains(filePathRelativeToWorkspace)) {
							processResourceRemoved(edRed);
						}
					}
					break;
				}	
				default:
					break;
			}
		}
	}

	private void processResourcesRefreshed(EditableResource editableResource) {
		for(EditableResource edRes : editableResources.values()) {
			if(!edRes.isSynchronized()) {
				processResourceChanged(edRes);
			}
		}
	}

	private void processResourceChanged(EditableResource editableResource) {
		logger.debug("Process resource changed = {} with modification stamp = {}", editableResource.getEditableResourcePath(), ((FileBasedEditableResource) editableResource).getFile().lastModified());
		reloadEditableResource(editableResource, true);
		// TODO check if refresh handles this
	}
	
	private void processResourceRemoved(EditableResource editableResource) {
		logger.debug("Process resource removed = {} with modification stamp = {}", editableResource.getEditableResourcePath(), ((FileBasedEditableResource) editableResource).getFile().lastModified());
		unsubscribeAllClientsForcefully(editableResource.getEditableResourcePath(), true);
	}
	
//	/**
//	 * 
//	 */
//	@Override
//	public void resourceChanged(IResourceChangeEvent event) {
//		processDelta(event.getDelta());
//	}
//
//	/**
//	 * Recursive method that goes through the children of the {@link IResourceDelta} tree and processes the delta 
//	 * accordingly for each resource.
//	 * 
//	 * 
//	 */
//	private void processDelta(IResourceDelta deltaToProcess) {
//		// recursively call this method for all the deltas
//		for (IResourceDelta delta : deltaToProcess.getAffectedChildren()) {
//			processDelta(delta);
//		}
//		
//		IResource resource = deltaToProcess.getResource();
//		String editableResourcePath = resource.getFullPath().toString();
//		FileBasedEditableResource er = (FileBasedEditableResource) getEditableResource(editableResourcePath);
//		if (er != null && !er.isSynchronized() && !er.isIgnoreResourceChangedNotification()) {
//			switch (deltaToProcess.getKind()) {
//				case IResourceDelta.REMOVED : {
//					processResourceRemoved(er, resource);
//					break;
//				}
//				case IResourceDelta.CHANGED : {
//					processResourceChanged(er, resource);
//					break;
//				}
//				default : {
//					break;
//				}
//			}
//		}
//	}
//
//	/**
//	 * 
//	 */
//	private void processResourceChanged(FileBasedEditableResource editableResource, IResource resource) {
//		logger.debug("Process resource changed = {} with modification stamp = {}", resource, resource.getModificationStamp());
//		reloadEditableResource(editableResource, true);
//		SingletonRefsInEditorPluginFromWebPlugin.INSTANCE_PROJECT_EXPLORER_TREE_STATEFUL_SERVICE.dispatchContentUpdate(resource);
//	}
//
//	/**
//	 * 
//	 */
//	private void processResourceRemoved(FileBasedEditableResource editableResource, IResource resource) {
//		logger.debug("Process resource removed = {} with modification stamp = {}", resource, resource.getModificationStamp());
//		unsubscribeAllClientsForcefully(editableResource.getEditableResourcePath(), true);
//	}
//
//	/**
//	 * Save logic must be executed while ignoring the resource to be saved. This is to ensure that updates are not sent to clients
//	 * during saving. After saving, update the synchronization stamp saved on the file, so any notifications sent for the resource
//	 * after the change (e.g. from the <code>AutoBuildJob</code>) will also be ignored.
//	 * 
//	 * <p>
//	 * Note: ignoring notifications during save is important; otherwise, the file would be reloaded while it is already locked by the
//	 * save logic, which could cause deadlocks, because the reload logic also attempts to lock the file.
//	 * 
//	 * 
//	 */
//	@Override
//	public void save(StatefulServiceInvocationContext context, String editableResourcePath) {
//		namedLockPool.lock(editableResourcePath);
//		try {
//			FileBasedEditableResource er = (FileBasedEditableResource) getEditableResource(editableResourcePath);
//			er.setIgnoreResourceChangedNotification(true);
//			try {
//				super.save(context, editableResourcePath);
//				er.updateSynchronizationStamp();
//			} finally {
//				er.setIgnoreResourceChangedNotification(false);
//			}
//		} finally {
//			namedLockPool.unlock(editableResourcePath);
//		}
//	}
//	
//	/**
//	 * Sets the <code>ignoreResourceChangedNotification</code> for the {@link FileBasedEditableResource} corresponding to the given path.
//	 */
//	public void setIgnoreResourceChangedNotificationForEditableResourcePath(String editableResourcePath, boolean ignore) {
//		namedLockPool.lock(editableResourcePath);
//		try {
//			FileBasedEditableResource er = (FileBasedEditableResource) getEditableResource(editableResourcePath);
//			if (er != null) {
//				er.setIgnoreResourceChangedNotification(ignore);
//			}
//		} finally {
//			namedLockPool.unlock(editableResourcePath);
//		}
//	}
//	
//	/**
//	 * Iterates through the {@link EditorStatefulService} and delegates to each {@link FileBasedEditorStatefulService}s.
//	 * 
//	 * @author Mariana
//	 */
//	public static void setIgnoreResourceChangedNotificationForAllServices(String editableResourcePath, boolean ignore) {
//		for (EditorStatefulService service : EditorSupport.INSTANCE.getEditorStatefulServices()) {
//			if (service instanceof FileBasedEditorStatefulService) {
//				((FileBasedEditorStatefulService) service).setIgnoreResourceChangedNotificationForEditableResourcePath(editableResourcePath, ignore);
//			}
//		}
//	}
//	
//	@Override
//	protected File getEditableResourceFile(EditableResource editableResource) {		
//		return ((FileBasedEditableResource) editableResource).getFile().getLocation().toFile();
//	}

}