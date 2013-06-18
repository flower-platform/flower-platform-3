package org.flowerplatform.editor.remote;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Responsible with listening for resource changed notifications and reloading their corresponding 
 * {@link EditableResource}s.
 * 
 * @author Mariana
 * 
 * @flowerModelElementId _DgZ4ECJxEeKovflGC_YAeQ
 */
public abstract class FileBasedEditorStatefulService extends EditorStatefulService
//	implements IResourceChangeListener 
	{
	
	/**
	 * @flowerModelElementId _tC8U4Cm1EeKzq6x6tgMJRw
	 */
	private static final Logger logger = LoggerFactory.getLogger(FileBasedEditorStatefulService.class);
	
	/**
	 * @flowerModelElementId _B5SMcCJ1EeKovflGC_YAeQ
	 */
	public FileBasedEditorStatefulService() {
//		ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);
	}

//	/**
//	 * @flowerModelElementId _IIt1ECJzEeKovflGC_YAeQ
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
//	 * @flowerModelElementId _11e40CJzEeKovflGC_YAeQ
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
//	 * @flowerModelElementId _co7w0CJzEeKovflGC_YAeQ
//	 */
//	private void processResourceChanged(FileBasedEditableResource editableResource, IResource resource) {
//		logger.debug("Process resource changed = {} with modification stamp = {}", resource, resource.getModificationStamp());
//		reloadEditableResource(editableResource, true);
//		SingletonRefsInEditorPluginFromWebPlugin.INSTANCE_PROJECT_EXPLORER_TREE_STATEFUL_SERVICE.dispatchContentUpdate(resource);
//	}
//
//	/**
//	 * @flowerModelElementId _oSZHQCJzEeKovflGC_YAeQ
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
//	 * @flowerModelElementId _OC5J8CJyEeKovflGC_YAeQ
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