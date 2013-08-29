package org.flowerplatform.web.svn.remote;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.progress_monitor.ProgressMonitor;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;
import org.flowerplatform.web.entity.EntityFactory;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.SVNRepositoryURLEntity;
import org.flowerplatform.web.entity.WorkingDirectory;
import org.flowerplatform.web.projects.remote.ProjectsService;
import org.flowerplatform.web.svn.SvnPlugin;
import org.flowerplatform.web.svn.operation.SVNOperationNotifyListener;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tigris.subversion.subclipse.core.ISVNCoreConstants;
import org.tigris.subversion.subclipse.core.ISVNRemoteFolder;
import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.SVNProviderPlugin;
import org.tigris.subversion.subclipse.core.repo.SVNRepositoryLocation;
import org.tigris.subversion.subclipse.core.resources.RemoteFolder;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNRevision;
import org.tigris.subversion.svnclientadapter.SVNUrl;

/**
 * 
 * @author Victor Badila
 * 
 * @flowerModelElementId _bcYyQAMcEeOrJqcAep-lCg
 */
public class SvnService {

	private static Logger logger = LoggerFactory.getLogger(SvnService.class);

	protected static SvnService INSTANCE = new SvnService();

	/**
	 * @flowerModelElementId _yaKVkAMcEeOrJqcAep-lCg
	 */

	public static SvnService getInstance() {
		return INSTANCE;
	}
	
	/**
	 * @author Gabriela Murgoci
	 */
	public boolean createRemoteFolder(ServiceInvocationContext context, List<PathFragment> parentPath, String folderName, String comment) {

		Object selectedParent = GenericTreeStatefulService.getNodeByPathFor(parentPath, null);

		ISVNRemoteFolder parentFolder = null;
		if (selectedParent instanceof ISVNRemoteFolder) {
			parentFolder = (ISVNRemoteFolder) selectedParent;
		} else if (selectedParent instanceof IAdaptable) {
			// ISVNRepositoryLocation is adaptable to ISVNRemoteFolder
			IAdaptable a = (IAdaptable) selectedParent;
			Object adapter = a.getAdapter(ISVNRemoteFolder.class);
			parentFolder = (ISVNRemoteFolder) adapter;
		}

		try {

			// create remote folder

			parentFolder.createRemoteFolder(folderName, comment, new NullProgressMonitor());
		} catch (SVNException e) { // something wrong happened
			CommunicationChannel channel = (CommunicationChannel) context.getCommunicationChannel();
			channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(CommonPlugin.getInstance().getMessage("error"), e.getMessage(),
					DisplaySimpleMessageClientCommand.ICON_ERROR));

			return false;
		}
		return true;
	}

	public boolean createSvnRepository(final ServiceInvocationContext context, final String url, final List<PathFragment> parentPath) {
		//had to use List due to limitations of altering final variables inside runnable
		final List<String> operationSuccessful = new ArrayList<String>();
		
		new DatabaseOperationWrapper(new DatabaseOperation() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				String organizationName = parentPath.get(1).getName();
				try {
					// check to see if repository with specified url address
					// exists following 2 lines might be merged
					SVNRepositoryLocation repository = SVNRepositoryLocation.fromString(url);
					if (repository.pathExists()) {
						// creates entry in database and links it to specified
						// organization
						Query q = wrapper.getSession().createQuery(String.format("SELECT e from %s e where e.name ='%s'", Organization.class.getSimpleName(), organizationName));
						ArrayList<Object> querryResult = (ArrayList<Object>) q.list();
						if(querryResult.isEmpty()) {
							CommunicationChannel channel = (CommunicationChannel) context.getCommunicationChannel();
							channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand("Error", SvnPlugin.getInstance().getMessage(
									"svn.remote.svnService.createSvnRepository.error.inexistentOrganizationError"), DisplaySimpleMessageClientCommand.ICON_ERROR));
							return;
						}
						Object organization = querryResult.get(0);						
						SVNRepositoryURLEntity urlEntity = EntityFactory.eINSTANCE.createSVNRepositoryURLEntity();
						urlEntity.setName(url);
						urlEntity.setOrganization((Organization) organization);
						operationSuccessful.add("success");
					} else {
						CommunicationChannel channel = (CommunicationChannel) context.getCommunicationChannel();
						channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand("Error", SvnPlugin.getInstance().getMessage(
								"svn.remote.svnService.createSvnRepository.error.invalidUrlError"), DisplaySimpleMessageClientCommand.ICON_ERROR));
					}
				} catch (SVNException e) {
					logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
					CommunicationChannel channel = (CommunicationChannel) context.getCommunicationChannel();
					channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand("Error", SvnPlugin.getInstance().getMessage(
							"svn.remote.svnService.createSvnRepository.error.svnExceptionError2"), DisplaySimpleMessageClientCommand.ICON_ERROR));
				}
			}
		});			
		// tree refresh
		if (operationSuccessful.contains("success")) {
			Object node = GenericTreeStatefulService.getNodeByPathFor(parentPath, null);
			GenericTreeStatefulService.getServiceFromPathWithRoot(parentPath).dispatchContentUpdate(node);
			return true;
		}		
		return false;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> getRepositoriesForOrganization(final String organizationName) {
		DatabaseOperationWrapper wrapper = new DatabaseOperationWrapper(new DatabaseOperation() {
			@Override
			public void run() {
				ArrayList<String> result = new ArrayList<String>();
				String myQuerry = String.format("SELECT e from %s e where e.organization.name ='%s'", SVNRepositoryURLEntity.class.getSimpleName(), organizationName);
				Query q = wrapper.getSession().createQuery(myQuerry);
				List<SVNRepositoryURLEntity> urlEntityList = q.list();
				for (SVNRepositoryURLEntity urlEntity : urlEntityList)
					result.add(urlEntity.getName());
				wrapper.setOperationResult(result);
			}
		});
		return (ArrayList<String>) wrapper.getOperationResult();
	}
	
	public void refresh(ServiceInvocationContext context, List<PathFragment> parentPath) {
		Object node = GenericTreeStatefulService.getNodeByPathFor(parentPath, null);
		if (node instanceof RemoteFolder)
			((RemoteFolder) node).refresh();
		else ((SVNRepositoryLocation) node).refreshRootFolder();
		GenericTreeStatefulService.getServiceFromPathWithRoot(parentPath).dispatchContentUpdate(node);		
	}
	
	public boolean checkout(ServiceInvocationContext context, ArrayList<ArrayList<PathFragment>> folders, List<PathFragment> workingDirectoryPartialPath, 
							String workingDirectoryDestination, String revisionNumberAsString, 
							int depthIndex, Boolean headRevision, boolean force, boolean ignoreExternals) {		
		workingDirectoryDestination = getWorkingDirectoryFullPath(workingDirectoryPartialPath) + workingDirectoryDestination;		
		SVNRevision revision = SVNRevision.HEAD;
		RemoteFolder[] remoteFolders = new RemoteFolder[folders.size()];			
		for(int i = 0; i < folders.size(); i++) {
			RemoteFolder correspondingRemoteFolder;
			Object treeNode = GenericTreeStatefulService.getNodeByPathFor(folders.get(i), null);
			if (treeNode instanceof SVNRepositoryLocation)
				correspondingRemoteFolder = (RemoteFolder) ((SVNRepositoryLocation)treeNode).getRootFolder();
			else correspondingRemoteFolder = (RemoteFolder) treeNode;
			remoteFolders[i] = correspondingRemoteFolder;			
		}		
		
		CommunicationChannel channel = (CommunicationChannel) context.getCommunicationChannel();
		final ProgressMonitor pm = ProgressMonitor.create(SvnPlugin.getInstance().getMessage("svn.service.checkout.checkoutProgressMonitor"), channel);
		if (pm != null) {
			pm.beginTask(null, 1000);
		}
		final ISVNClientAdapter myClient;
		SVNOperationNotifyListener opMng = new SVNOperationNotifyListener(CommunicationPlugin.tlCurrentChannel.get());
		try {
			myClient = SVNProviderPlugin.getPlugin().getSVNClient();			
			myClient.setProgressListener(opMng);
			opMng.beginOperation(pm, myClient, true);					
			for(int i = 0; i < folders.size(); i++) {
				File fileArgumentForCheckout = new File(workingDirectoryDestination + "/" + remoteFolders[i].getName());
				String fullPath = remoteFolders[i].getRepository().getLabel() + "/" + remoteFolders[i].getProjectRelativePath();				
				myClient.checkout(new SVNUrl(fullPath), fileArgumentForCheckout, 
						revision, getDepthValue(depthIndex), ignoreExternals, force);				
				opMng.endOperation();
				try {
					ProjectsService.getInstance().createOrImportProjectFromFile(context, fileArgumentForCheckout);
				} catch (URISyntaxException e) {
					logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
					channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand("Error", e.getMessage(), DisplaySimpleMessageClientCommand.ICON_ERROR));
				} catch (CoreException e) {
					logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
					channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand("Error", e.getMessage(), DisplaySimpleMessageClientCommand.ICON_ERROR));
				}
			}
		} catch (SVNException | MalformedURLException | SVNClientException e) {
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand("Error", e.getMessage(), DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		} finally {
			pm.done();
		}		
		return true;
	}
	
	public ArrayList<String> getWorkingDirectoriesForOrganization(ServiceInvocationContext context, String organizationName) {
		ArrayList<String> result = new ArrayList<String>();
		try {
			ProjectsService pj = new ProjectsService();
			List<WorkingDirectory> workingDirectoryList = pj.getWorkingDirectoriesForOrganizationName(organizationName);
			for (WorkingDirectory wd : workingDirectoryList) {
				result.add(wd.getPathFromOrganization());				
			}
		} catch (CoreException | URISyntaxException e) {			
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			CommunicationChannel channel = (CommunicationChannel) context.getCommunicationChannel();
			channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand("Error", SvnPlugin.getInstance().getMessage(
					"svn.service.checkout.error.problemsAtBrowseWorkDirs"), DisplaySimpleMessageClientCommand.ICON_ERROR));
		}	
		return result;		
	}
	
	public boolean workingDirectoryExists(ServiceInvocationContext context, String wdName, String organizationName) {
		ArrayList<String> existingWDs = getWorkingDirectoriesForOrganization(context, organizationName);
		for (String wd : existingWDs) {
			if(wd.equals(wdName))
				return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public String getWorkingDirectoryFullPath(List<PathFragment> pathWithRoot) {		
		Object workingDirectory  = GenericTreeStatefulService.getNodeByPathFor(pathWithRoot, null);
		if (workingDirectory instanceof WorkingDirectory) {
			return "" + ProjectsService.getInstance().getOrganizationDir(((WorkingDirectory) workingDirectory).getOrganization().getLabel()) + "/"+ ((WorkingDirectory) workingDirectory).getPathFromOrganization();
		}
		else return ((Pair<File, String>) workingDirectory).a.getAbsolutePath();			
	}
	
	public Boolean createFolderAndMarkAsWorkingDirectory (ServiceInvocationContext context, String path, TreeNode organization) {
		File targetFile = new File(ProjectsService.getInstance().getOrganizationDir(organization.getLabel()).getAbsolutePath() + "\\" +path);		
			if(targetFile.mkdirs()) {
				ProjectsService.getInstance().markAsWorkingDirectoryForFile(context, targetFile);
				return true;
			} else { 
				return false;
			}				
	}
	
	public int getDepthValue(int depth){
		switch (depth) {
			case 0: 
				return ISVNCoreConstants.DEPTH_INFINITY;
			case 1: 
				return ISVNCoreConstants.DEPTH_IMMEDIATES;
			case 2:
				return ISVNCoreConstants.DEPTH_FILES;
			case 3:
				return ISVNCoreConstants.DEPTH_EMPTY;			
		}
		return ISVNCoreConstants.DEPTH_UNKNOWN;
	}

}
