package org.flowerplatform.web.svn;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

<<<<<<< HEAD

import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.SVNProviderPlugin;

import org.apache.subversion.javahl.ClientException;
import org.tigris.subversion.subclipse.core.SVNException;

import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNInfoUnversioned;
=======
import org.apache.subversion.javahl.ClientException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.flowerplatform.communication.progress_monitor.ProgressMonitor;
import org.tigris.subversion.subclipse.core.ISVNRemoteResource;
import org.tigris.subversion.subclipse.core.ISVNRepositoryLocation;
import org.tigris.subversion.subclipse.core.Policy;
import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.repo.ISVNListener;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNInfoUnversioned;
import org.tigris.subversion.svnclientadapter.SVNUrl;
>>>>>>> origin/GH94-Merge
import org.tigris.subversion.svnclientadapter.javahl.JhlClientAdapter;

/**
 * 
 * @author Victor Badila 
 */
public class SvnUtils implements ISvnVersionHandler{
	
<<<<<<< HEAD
	public Boolean isRepository(File f) {				 
		try {
			if (SVNProviderPlugin.getPlugin().getSVNClient().getInfo(f) instanceof SVNInfoUnversioned) {
=======
	private List<ISVNListener> repositoryListeners = new ArrayList<ISVNListener>();
	
	public Boolean isRepository(File f) {
		JhlClientAdapter clientAdapter = new JhlClientAdapter();		 
		try {
			if (clientAdapter.getInfo(f) instanceof SVNInfoUnversioned) {
>>>>>>> origin/GH94-Merge
				return false;
			}
			return true;
		} catch (SVNClientException e) {
			return false;
		}			
	}

	/**
	 * 
	 * @author Cristina Necula
	 */
	@Override
	public boolean isAuthenticationClientException(Throwable exception) {
		if ((exception instanceof SVNException) &&
				(exception.getCause().getCause() instanceof ClientException)) {
			if ( ((ClientException)exception.getCause().getCause()).getAprError() == 170001) {
				return true;
			}
		}
<<<<<<< HEAD
=======
		
//	this code may be used later
		
//		try {
//			Class<?> clazz = Class.forName("org.tigris.subversion.svnclientadapter.SVNClientException");
//			if (exception.getCause() instanceof SVNClientException){
//				Method getAprErrorMethod = clazz.getMethod("getAprError");
//				getAprErrorMethod.setAccessible(true);
//				Object result = getAprErrorMethod.invoke(exception.getCause());
//				if (Integer.valueOf(String.valueOf(result)).intValue() == -1) {
//					return true;
//				}
//			}			
//		} catch (Exception e) {	// swallow it -> consider not authentication exception			
//		}
>>>>>>> origin/GH94-Merge
		return false;
	}
	/**
	 * 
	 * @author Cristina Necula
	 * @throws SVNException 
	 */
	public void deleteRemoteResources(ISVNRemoteResource[] remoteResources, String message, ProgressMonitor monitor) throws SVNException{
		IProgressMonitor progress = Policy.monitorFor(monitor);
        progress.beginTask(Policy.bind("RepositoryResourcesManager.deleteRemoteResources"), 100*remoteResources.length); //$NON-NLS-1$
        
        // the given remote resources can come from more than a repository and so needs
        // more than one svnClient
        // we associate each repository with the corresponding resources to delete
        HashMap<ISVNRepositoryLocation, List<ISVNRemoteResource>> mapRepositories = new HashMap<ISVNRepositoryLocation, List<ISVNRemoteResource>>();
        for (ISVNRemoteResource remoteResource : remoteResources) {
            ISVNRepositoryLocation repositoryLocation = remoteResource.getRepository();
            List<ISVNRemoteResource> resources = (List<ISVNRemoteResource>)mapRepositories.get(repositoryLocation);
            if (resources == null) {
                resources = new ArrayList<ISVNRemoteResource>();
                mapRepositories.put(repositoryLocation, resources);
            }
            resources.add(remoteResource);
        }
        ISVNClientAdapter svnClient = null;
        ISVNRepositoryLocation repository = null;
        try {        
        	for (List<ISVNRemoteResource> resources : mapRepositories.values()) {
                repository = (resources.get(0)).getRepository();
                svnClient = repository.getSVNClient();
                SVNUrl urls[] = new SVNUrl[resources.size()];
                for (int i = 0; i < resources.size();i++) {
                    ISVNRemoteResource resource = resources.get(i); 
                    urls[i] = resource.getUrl();
                }
                svnClient.remove(urls,message);
                repository.returnSVNClient(svnClient);
                svnClient = null;
                repository = null;
                
                for (ISVNRemoteResource resource : resources) {
                    remoteResourceDeleted(resource);
                }
                
                progress.worked(100*urls.length);
            }
        } catch (SVNClientException | SVNException e) {
            throw SVNException.wrapException(e);
        } finally {
        	if (repository != null) {
        		repository.returnSVNClient(svnClient);
        	}
            progress.done();
        }
	}
	
	public void remoteResourceDeleted(ISVNRemoteResource resource) {
    	for (ISVNListener listener : repositoryListeners) {
            listener.remoteResourceDeleted(resource);
        }    
    }
	
    public void addRepositoryListener(ISVNListener listener) {
        repositoryListeners.add(listener);
    }

    public void removeRepositoryListener(ISVNListener listener) {
        repositoryListeners.remove(listener);
    }


}
