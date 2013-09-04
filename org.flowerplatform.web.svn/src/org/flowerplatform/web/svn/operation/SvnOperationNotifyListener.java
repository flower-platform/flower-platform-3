package org.flowerplatform.web.svn.operation;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.tigris.subversion.subclipse.core.Policy;
import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.SVNProviderPlugin;
import org.tigris.subversion.subclipse.core.client.OperationProgressNotifyListener;
import org.tigris.subversion.subclipse.core.resources.SVNWorkspaceRoot;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.ISVNNotifyListener;
import org.tigris.subversion.svnclientadapter.ISVNProgressListener;
import org.tigris.subversion.svnclientadapter.SVNNodeKind;
import org.tigris.subversion.svnclientadapter.SVNProgressEvent;

/**
 * Class inspired from {@link OperationProgressNotifyListener}.
 * 
 * <p>
 * Modifications were made to work in a multithreading environment.
 * 
 * @author Victor Badila
 */
public class SvnOperationNotifyListener implements ISVNNotifyListener, ISVNProgressListener {
	
	protected CommunicationChannel channel;

	private Set<IResource> changedResources = new LinkedHashSet<IResource>();

	private SvnOperationProgressNotifyListener operationProgressListener = null;
		
	public CommunicationChannel getChannel() {
		return channel;
	}
	
	public SvnOperationNotifyListener(CommunicationChannel channel) {
		this.channel = channel;		
	}
	
	public void beginOperation(IProgressMonitor monitor, ISVNClientAdapter svnClient, boolean addProgressListener) {
		if (addProgressListener) {			
			this.operationProgressListener = new SvnOperationProgressNotifyListener(monitor, svnClient, channel);
			SvnOperationManager.INSTANCE.add(operationProgressListener);						
		}
		SvnOperationManager.INSTANCE.add(this);	
	}

	public void endOperation() throws SVNException {
		endOperation(true);
	}
	
	/**
	 * Ends a batch of operations. Pending changes are committed only when the
	 * number of calls to endOperation() balances those to beginOperation().
	 */
	public void endOperation(boolean refresh) throws SVNException {
		if (operationProgressListener != null) {
			SvnOperationManager.INSTANCE.remove(operationProgressListener);
		}
		SvnOperationManager.INSTANCE.remove(this);
		
		for (Iterator<IResource> it = changedResources.iterator(); it.hasNext();) {
			IResource resource = (IResource) it.next();
			// Ensure the .svn has the team private flag set before refresh.
			if (refresh && resource instanceof IContainer)
				handleSVNDir((IContainer) resource);
			try {
				// .svn directory will be refreshed so all files in the
				// directory including resource will
				// be refreshed later (@see SyncFileChangeListener)
				if (refresh) {
					resource.refreshLocal(IResource.DEPTH_INFINITE,	new NullProgressMonitor());
				}				
				// Refreshing the root directory at this point will
				// avoid problems with linked source folders.
				if (refresh	&& resource.getParent().getType() == IResource.PROJECT) {
					resource.getParent().refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor());
				}
			} catch (CoreException e) {
				throw SVNException.wrapException(e);
			}
		}		
	}

	@Override
	public void logMessage(String message) {
		if (operationProgressListener != null && CommunicationPlugin.tlCurrentChannel.get().equals(channel)) {
			operationProgressListener.logMessage(message);
		}
	}
	
	@Override
	public void logCompleted(String message) {
		if (operationProgressListener != null && CommunicationPlugin.tlCurrentChannel.get().equals(channel)) {
			operationProgressListener.logMessage(message);
		}
	}

	@Override
	public void onNotify(File path, SVNNodeKind kind) {
		if (!CommunicationPlugin.tlCurrentChannel.get().equals(channel)) {
			return;
		}
		IPath pathEclipse = new Path(path.getAbsolutePath());

        if (kind == SVNNodeKind.UNKNOWN)  { // delete, revert 
            IPath pathEntries = pathEclipse.removeLastSegments(1).append(
            		SVNProviderPlugin.getPlugin().getAdminDirectoryName());
            changedResources.addAll(Arrays.asList(SVNWorkspaceRoot.getResourcesFor(pathEntries, false)));
            
            if (path.isDirectory()) {
            	IResource[] resources = SVNWorkspaceRoot.getResourcesFor(pathEclipse, false);
            	for (int i = 0; i < resources.length; i++) {
            		IResource resource = resources[i];
					if (resource.getType() != IResource.ROOT) {
						IResource svnDir = ((IContainer) resource).getFolder(new Path(SVNProviderPlugin.getPlugin().getAdminDirectoryName()));
	                    changedResources.add(svnDir);
					}
            	}
            }
        } else {
			IResource svnDir = null;
			if (kind == SVNNodeKind.DIR) {
				// when the resource is a directory, two .svn directories can
				// potentially
				// be modified
				IResource[] resources = SVNWorkspaceRoot.getResourcesFor(pathEclipse, false);
				for (int i = 0; i < resources.length; i++) {
					IResource resource = resources[i];
					if (resource.getType() != IResource.ROOT) {
						if (resource.getProject() != resource) {
							// if resource is a project. We can't refresh ../.svn
							svnDir = resource.getParent().getFolder(new Path(SVNProviderPlugin.getPlugin().getAdminDirectoryName()));
							changedResources.add(svnDir);
						}
						if (resource instanceof IContainer) {
		                    svnDir = ((IContainer) resource).getFolder(new Path(SVNProviderPlugin.getPlugin().getAdminDirectoryName()));
		                    changedResources.add(svnDir);
						}
					}
				}
			} else if (kind == SVNNodeKind.FILE) {
				IResource[] resources = SVNWorkspaceRoot.getResourcesFor(pathEclipse, false);

				for (int i = 0; i < resources.length; i++) {
					svnDir = resources[i].getParent().getFolder(new Path(SVNProviderPlugin.getPlugin().getAdminDirectoryName()));
					changedResources.add(svnDir);
				}
			}
		}
        
        if (operationProgressListener != null) {
        	operationProgressListener.onNotify(path, kind);    		
        }		
	}
	
	protected boolean handleSVNDir(final IContainer svnDir) {
		if (!svnDir.exists() || !svnDir.isTeamPrivateMember()) {
			try {
				// important to have both the refresh and setting of team-private in the
				// same runnable so that the team-private flag is set before other delta listeners 
				// sees the SVN folder creation.
				ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
					public void run(IProgressMonitor monitor) throws CoreException {
						if (!svnDir.exists()) {
							svnDir.refreshLocal(IResource.DEPTH_ZERO,new NullProgressMonitor());
						}
						if (svnDir.exists())
							svnDir.setTeamPrivateMember(true);
					} 
				}, svnDir.getParent(), IWorkspace.AVOID_UPDATE, null);
			} catch(CoreException e) {
				SVNProviderPlugin.log(SVNException.wrapException(svnDir, Policy.bind("OperationManager.errorSettingTeamPrivateFlag"), e)); //$NON-NLS-1$
			}
		}
		return svnDir.isTeamPrivateMember();
	}
	
	@Override
	public void setCommand(int command) {		
	}

	@Override
	public void logCommandLine(String commandLine) {		
	}
	
	@Override
	public void logError(String message) {		
	}

	@Override
	public void logRevision(long revision, String path) {		
	}

	@Override
	public void onProgress(SVNProgressEvent progressEvent) {
		if (operationProgressListener != null) {
			operationProgressListener.onProgress(progressEvent);
		}
	}	

}

