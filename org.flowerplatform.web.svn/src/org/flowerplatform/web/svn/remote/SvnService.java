package org.flowerplatform.web.svn.remote;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;

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
import org.flowerplatform.web.entity.SVNCommentEntity;
import org.flowerplatform.web.entity.SVNRepositoryURLEntity;
import org.flowerplatform.web.entity.User;
import org.flowerplatform.web.entity.WorkingDirectory;
import org.flowerplatform.web.projects.remote.ProjectsService;
import org.flowerplatform.web.security.service.UserService;
import org.flowerplatform.web.svn.SvnPlugin;
import org.flowerplatform.web.svn.operation.SvnOperationNotifyListener;
import org.flowerplatform.web.svn.remote.dto.FileDto;
import org.flowerplatform.web.svn.remote.dto.GetModifiedFilesDto;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tigris.subversion.subclipse.core.ISVNCoreConstants;
import org.tigris.subversion.subclipse.core.ISVNRemoteFolder;
import org.tigris.subversion.subclipse.core.ISVNRemoteResource;
import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.SVNProviderPlugin;
import org.tigris.subversion.subclipse.core.repo.SVNRepositoryLocation;
import org.tigris.subversion.subclipse.core.resources.RemoteFolder;
import org.tigris.subversion.subclipse.core.resources.RemoteResource;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.ISVNConflictResolver;
import org.tigris.subversion.svnclientadapter.ISVNInfo;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNRevision;
import org.tigris.subversion.svnclientadapter.SVNUrl;
import org.tigris.subversion.svnclientadapter.javahl.JhlClientAdapter;
import org.tigris.subversion.svnclientadapter.javahl.JhlStatus;
import org.tigris.subversion.svnclientadapter.utils.Depth;


/**
 * 
 * @author Victor Badila
 * 
 * @flowerModelElementId _bcYyQAMcEeOrJqcAep-lCg
 */
public class SvnService {

	public static final int MAX_SVN_COMMENTS = 10;

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
					SVNRepositoryLocation repository = SVNRepositoryLocation.fromString(url);
					if (repository.pathExists()) {
						// creates entry in database and links it to specified organization
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
	
	public String getCommitUrlPathForSingleSelection(ArrayList<PathFragment> path) {
		String workingDirectoryPath = getDirectoryFullPathFromPathFragments(path);
		return getSvnUrlForPath(workingDirectoryPath);		
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
	
	public Boolean resolve(ServiceInvocationContext context, ArrayList<String> filePaths, int choice) {
		CommunicationChannel channel = (CommunicationChannel) context.getCommunicationChannel();
		ProgressMonitor pm = ProgressMonitor.create(SvnPlugin.getInstance().getMessage("svn.service.resolve.markResolvedMonitor"), channel);
		SvnOperationNotifyListener opMng = new SvnOperationNotifyListener(CommunicationPlugin.tlCurrentChannel.get());		
		try {
			ISVNClientAdapter myClientAdapter = SVNProviderPlugin.getPlugin().getSVNClient();
			myClientAdapter.setProgressListener(opMng);			
			if (choice == 1) {
				choice = ISVNConflictResolver.Choice.chooseMine;
			} else if (choice == 2) {
				choice = ISVNConflictResolver.Choice.chooseTheirs;
			} else {
				choice = ISVNConflictResolver.Choice.chooseBase;
			}			
			for (int i=0; i<filePaths.size(); i++) {
				myClientAdapter.resolve(new File(filePaths.get(i)), choice);
			}
			
			
		} catch (SVNException | SVNClientException e) {
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand("Error", e.getMessage(), DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		} finally {
			pm.done();
		}		
		return true;
	}
	
	public boolean revert(ServiceInvocationContext context, ArrayList<FileDto> fileDtos) {
		CommunicationChannel channel = (CommunicationChannel) context.getCommunicationChannel();
		ProgressMonitor pm = ProgressMonitor.create(SvnPlugin.getInstance().getMessage("svn.service.revert.revertMonitor"), channel);
		SvnOperationNotifyListener opMng = new SvnOperationNotifyListener(CommunicationPlugin.tlCurrentChannel.get());
		Boolean recurse = true;
		try {
			ISVNClientAdapter myClientAdapter = SVNProviderPlugin.getPlugin().getSVNClient();
			myClientAdapter.setProgressListener(opMng);
			for (int i=0; i<fileDtos.size(); i++) {
				FileDto currentDto = fileDtos.get(i);
				if (currentDto.getStatus().equals("unversioned")) {
					File f = new File(currentDto.getPath());
					f.delete();
				} else {
					myClientAdapter.revert(new File(currentDto.getPath()), recurse);
				}
			}
		} catch (SVNException | SVNClientException e) {
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand("Error", e.getMessage(), DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		} finally {
			pm.done();
		}
		return true;
	}
		
	public boolean checkout(ServiceInvocationContext context, ArrayList<ArrayList<PathFragment>> folders, List<PathFragment> workingDirectoryPartialPath, 
							String workingDirectoryDestination, String revisionNumberAsString, 
							int depthIndex, Boolean headRevision, boolean force, boolean ignoreExternals) {		
		CommunicationChannel channel = (CommunicationChannel) context.getCommunicationChannel();
		ProgressMonitor pm = ProgressMonitor.create(SvnPlugin.getInstance().getMessage("svn.service.checkout.checkoutProgressMonitor"), channel);		
		workingDirectoryDestination = getDirectoryFullPathFromPathFragments(workingDirectoryPartialPath) + workingDirectoryDestination;
		SVNRevision revision;
		if (headRevision) {
			revision = SVNRevision.HEAD;
		}
		else {
			try {
				revision = SVNRevision.getRevision(revisionNumberAsString);
			} catch (ParseException e) {
				logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
				channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand("Error", e.getMessage(), DisplaySimpleMessageClientCommand.ICON_ERROR));
				return false;
			}			
		}
		RemoteResource[] remoteFolders = new RemoteResource[folders.size()];
		for(int i = 0; i < folders.size(); i++) {
			RemoteResource correspondingRemoteResource;
			Object treeNode = GenericTreeStatefulService.getNodeByPathFor(folders.get(i), null);
			if (treeNode instanceof SVNRepositoryLocation) {
				correspondingRemoteResource = (RemoteFolder) ((SVNRepositoryLocation)treeNode).getRootFolder();
			} else {				
				correspondingRemoteResource = (RemoteResource)treeNode;
			}
			remoteFolders[i] = correspondingRemoteResource;			
		}			
		pm.beginTask(null, 1000);
		ISVNClientAdapter myClient;
		SvnOperationNotifyListener opMng = new SvnOperationNotifyListener(CommunicationPlugin.tlCurrentChannel.get());
		try {
			myClient = SVNProviderPlugin.getPlugin().getSVNClient();			
			myClient.setProgressListener(opMng);
			opMng.beginOperation(pm, myClient, true);					
			for(int i = 0; i < folders.size(); i++) {
				File fileArgumentForCheckout = new File(workingDirectoryDestination + "/" + remoteFolders[i].getName());
				String fullPath = remoteFolders[i].getUrl().toString();				
				myClient.checkout(new SVNUrl(fullPath), fileArgumentForCheckout, 
						revision, getDepthValue(depthIndex), ignoreExternals, force);				
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
			opMng.endOperation();
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
	public String getDirectoryFullPathFromPathFragments(List<PathFragment> pathWithRoot) {		
		Object workingDirectory  = GenericTreeStatefulService.getNodeByPathFor(pathWithRoot, null);
		if (workingDirectory instanceof WorkingDirectory) {
			return "" + ProjectsService.getInstance().getOrganizationDir(((WorkingDirectory) workingDirectory).getOrganization().getLabel()) + "\\"+ ((WorkingDirectory) workingDirectory).getPathFromOrganization();
		}
		else return ((Pair<File, String>) workingDirectory).a.getAbsolutePath();			
	}
	
	public Boolean createFolderAndMarkAsWorkingDirectory (ServiceInvocationContext context, String path, TreeNode organization) {
		File targetFile = new File(ProjectsService.getInstance().getOrganizationDir(organization.getLabel()).getAbsolutePath() + "\\" +path);
			try {
				targetFile.getCanonicalPath();
			} catch (IOException e) {
				return false;
			}		
			if(targetFile.mkdirs()) {
				ProjectsService.getInstance().markAsWorkingDirectoryForFile(context, targetFile);
				return true;
			} else { 
				return false;
			}				
	}
	
	public String getSvnUrlForPath(String filePath) {
		ISVNClientAdapter myClientAdapter;
		try {
			myClientAdapter = SVNProviderPlugin.getPlugin().getSVNClient();
			ISVNInfo info = myClientAdapter.getInfo(new File(filePath));
			return info.getUrl().toString();
		} catch (SVNException | SVNClientException e) {
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);	
			return null;
		}		
	}	
	
	public List<PathFragment> getCommonParent(ArrayList<ArrayList<PathFragment>> selectionList) {
		int index = 0;
		boolean sem = true;
		int size = selectionList.get(0).size();

		for (int i = 0; i < size && sem; i++) {
			index = i;
			PathFragment element = selectionList.get(0).get(i);
			for (int j = 0; j < selectionList.size(); j++) {
				if (!(selectionList.get(j).get(i).equals(element))) {
					sem = false;
					index--;
					break;
				}
			}
		}
		return selectionList.get(0).subList(0, index + 1);
	}
	
	public ArrayList<String> setImageUrlForExtensionType(String fileType, String status) {
		ArrayList<String> result = new ArrayList<>();
		if (fileType.equals("dir")) {
			result.add("images/folder_pending.gif");
		} else {
			result.add("images/file.gif");
		}
		
		if (status.equals("unversioned")) {
			result.add("images/added_ov.gif");
		} else if (status.equals("missing")) {
			result.add("images/deleted_ov.gif");
		} else if (status.equals("conflicted")) {
			result.add("images/conflicted_ov.gif");
		} else {
			result.add("images/question_ov.gif");
		}		
		return result;
	}
	
	public ArrayList<String> getConflicted(ServiceInvocationContext context, ArrayList<ArrayList<PathFragment>> selectionList) {
		CommunicationChannel channel = (CommunicationChannel) context.getCommunicationChannel();
		JhlClientAdapter myClientAdapter = new JhlClientAdapter();
		ArrayList<String> result = new ArrayList<String>();		
		File[] filesToBeCompared = getFilesForSelectionList(selectionList);		
		for (File f : filesToBeCompared) {			
			try {
				JhlStatus[] jhlStatusArray = (JhlStatus[])myClientAdapter.getStatus(f, true, true);
				for (int i=0; i<jhlStatusArray.length; i++) {
					if (jhlStatusArray[i].getTextStatus().toString().equals("conflicted")) {						
						result.add(jhlStatusArray[i].getPath());
					}
				}
			} catch (SVNClientException e) {
				logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
				channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand("Error", e.getMessage(), DisplaySimpleMessageClientCommand.ICON_ERROR));
			}			
		}	
		return result;
	}
	
	
	public GetModifiedFilesDto getDifferences(ServiceInvocationContext context, ArrayList<ArrayList<PathFragment>> selectionList) {
		CommunicationChannel channel = (CommunicationChannel) context.getCommunicationChannel();
		JhlClientAdapter myClientAdapter = new JhlClientAdapter();
		ArrayList<FileDto> fileList = new ArrayList<FileDto>();		
		List<PathFragment> list = getCommonParent(selectionList);				
		String workingDirectoryFullPath = getDirectoryFullPathFromPathFragments(list);
		int workingDirectoryFullPathLength;
		if (workingDirectoryFullPath.contains("/")) {
			workingDirectoryFullPathLength = workingDirectoryFullPath.substring(0, workingDirectoryFullPath.lastIndexOf("/")).length();
		} else {
			 workingDirectoryFullPathLength = workingDirectoryFullPath.substring(0, workingDirectoryFullPath.lastIndexOf("\\")).length();			
		}		
		File[] filesToBeCompared = getFilesForSelectionList(selectionList);		
		for (File f : filesToBeCompared) {			
			try {
				ArrayList<String> conflictSpecificFiles = new ArrayList<String>();				
				
				
				JhlStatus[] jhlStatusArray = (JhlStatus[])myClientAdapter.getStatus(f, true, true);
				for (int i=0; i<jhlStatusArray.length; i++) {
					if (!jhlStatusArray[i].getTextStatus().toString().equals("normal")) {
						
						if (jhlStatusArray[i].getTextStatus().toString().equals("conflicted")) {
							conflictSpecificFiles.add(jhlStatusArray[i].getConflictNew().getAbsolutePath().replaceAll("\\\\", "/"));
							conflictSpecificFiles.add(jhlStatusArray[i].getConflictOld().getAbsolutePath().replaceAll("\\\\", "/"));
							conflictSpecificFiles.add(jhlStatusArray[i].getConflictWorking().getAbsolutePath().replaceAll("\\\\", "/"));							
						}
						
						Boolean continueSetter = false;
						if (jhlStatusArray[i].getTextStatus().toString().equals("unversioned")) {
							for (String s : conflictSpecificFiles) {
								if (s.equals(jhlStatusArray[i].getPath())) {
									continueSetter = true;
									break;
								}
							}
						}
						if (continueSetter) {
							continue;
						}						
						
						FileDto modifiedFileDto = new FileDto();
						JhlStatus currentTarget = jhlStatusArray[i];						
						modifiedFileDto.setPath(currentTarget.getFile().getAbsolutePath());
						modifiedFileDto.setLabel(currentTarget.getFile().getAbsolutePath().substring(workingDirectoryFullPathLength + 1));
						String status = currentTarget.getTextStatus().toString();
						modifiedFileDto.setStatus(status);						
						modifiedFileDto.setImageUrls(setImageUrlForExtensionType(currentTarget.getNodeKind().toString(), status));
												
						fileList.add(modifiedFileDto);
					}
				}
			} catch (SVNClientException e) {
				logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
				channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand("Error", e.getMessage(), DisplaySimpleMessageClientCommand.ICON_ERROR));
			}			
		}		
		// sort needs to be implemented manually:
		Collections.sort(fileList, new Comparator<FileDto>(){
			@Override
			public int compare(FileDto dto1, FileDto dto2) {
				return dto1.getPath().compareTo(dto2.getPath());
			}});		
		// duplicates are removed
		int currentIndex = 0;		
		int resultSize = fileList.size();
		while (currentIndex < resultSize-1) {
			if (fileList.get(currentIndex).getPath().equals(fileList.get(currentIndex+1).getPath())) {
				fileList.remove(currentIndex+1);
				resultSize--;
			} else {
				currentIndex++;
			}			
		}		
		GetModifiedFilesDto dto = new GetModifiedFilesDto();
		dto.setFiles(fileList);
		return dto;
	}
		
	public Boolean commit(ServiceInvocationContext context, ArrayList<FileDto> selectionList, String message, Boolean keepLocks) {
		CommunicationChannel channel = (CommunicationChannel) context.getCommunicationChannel();
		ProgressMonitor pm = ProgressMonitor.create(SvnPlugin.getInstance().getMessage("svn.service.commit.commitMonitor"), channel);
		SvnOperationNotifyListener opMng = new SvnOperationNotifyListener(CommunicationPlugin.tlCurrentChannel.get());
		
		// following 40 lines could be rewritten:		
		ArrayList<File> markedForAddition = new ArrayList<File>();
		ArrayList<File> markedForDeletion = new ArrayList<File>();
		ArrayList<File> normalCommitFiles = new ArrayList<File>();	
		
		for (FileDto dto : selectionList) {
			File file = new File(dto.getPath());
			switch (dto.getStatus()) {
			case "missing": // will be added to a queue ready to be marked for deletion, after which it would be safe to have it inside commit file list argument
				markedForDeletion.add(file);
				normalCommitFiles.add(file);
				break;
			case "modified": 
				normalCommitFiles.add(file);
				break;
			case "unversioned": // same as for "missing"
				markedForAddition.add(file);
				normalCommitFiles.add(file);
				break;
			case "added":
				normalCommitFiles.add(file);			
			}		
		}		
		// set File vectors for different types of operations:		
		File[] markedForDeletionFiles = new File[markedForDeletion.size()];
		for (int i=0; i<markedForDeletion.size(); i++) {
			markedForDeletionFiles[i] = markedForDeletion.get(i);
		}		
		File[] markedForAdditionFiles = new File[markedForAddition.size()];
		for (int i=0; i<markedForAddition.size(); i++) {
			markedForAdditionFiles[i] = markedForAddition.get(i);
		}
		File[] files = new File[normalCommitFiles.size()];
		for (int i=0; i<normalCommitFiles.size(); i++) {
			files[i] = normalCommitFiles.get(i);
		}		
		Boolean recurse = true;
		try {
			ISVNClientAdapter myClientAdapter = SVNProviderPlugin.getPlugin().getSVNClient();
			myClientAdapter.setProgressListener(opMng);			
			try {				
				for (int i=0; i<markedForAdditionFiles.length; i++) {
					myClientAdapter.addFile(markedForAdditionFiles[i]);					
				}			
				myClientAdapter.remove(markedForDeletionFiles, true);
				myClientAdapter.commit(files, message, recurse, keepLocks);
			} catch (SVNClientException e) {
				logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
				channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand("Error", e.getMessage(), DisplaySimpleMessageClientCommand.ICON_ERROR));
				return false;			
			}							
		} catch (SVNException e) {
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand("Error", e.getMessage(), DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		} finally {
			pm.done();
		}		
		return true;
	}	
	
	public File[] getFilesForSelectionList(ArrayList<ArrayList<PathFragment>> selectionList) {
		File[] result = new File[selectionList.size()];		
		for (int i=0; i<selectionList.size(); i++) {
			ArrayList<PathFragment> currentPathSelection = selectionList.get(i);
			String path = currentPathSelection.get(1).getName();			 
			path = ProjectsService.getInstance().getOrganizationDir(path).getAbsolutePath();
			for (int j=3; j<currentPathSelection.size();j++) {
				path = path.concat("\\" + currentPathSelection.get(j).getName());
			}
			result[i] = new File(path);			
		}
		return result;
	}
	
	public Boolean updateToHEAD(ServiceInvocationContext context, ArrayList<ArrayList<PathFragment>> selectionList) {
		return updateToVersion(context, selectionList, "head", Depth.infinity, false, false, true);
	}
	
	public Boolean updateToVersion(ServiceInvocationContext context, ArrayList<ArrayList<PathFragment>> selectionList, String revision,
								   int depth, Boolean changeWorkingCopyToSpecifiedDepth, Boolean ignoreExternals, Boolean allowUnversionedObstructions) {
		File[] fileMethodArgument = getFilesForSelectionList(selectionList);		
		CommunicationChannel channel = (CommunicationChannel) context.getCommunicationChannel();
		ProgressMonitor pm = ProgressMonitor.create(SvnPlugin.getInstance().getMessage("svn.service.update.updateToHeadMonitor"), channel);
		if (pm != null) {
			pm.beginTask(null, 1000);
		}
		SvnOperationNotifyListener opMng = new SvnOperationNotifyListener(CommunicationPlugin.tlCurrentChannel.get());
		Boolean operationSuccesful = true;		
		try {
			ISVNClientAdapter myClient = SVNProviderPlugin.getPlugin().getSVNClient();
			myClient.setProgressListener(opMng);
			SVNRevision revisionParameter = null;
			if (revision.equals("head")) {
				revisionParameter = SVNRevision.HEAD;				
			} else {
				try {
					revisionParameter = SVNRevision.getRevision(revision);
				} catch (ParseException e) {
					logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
					channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand("Error", e.getMessage(), DisplaySimpleMessageClientCommand.ICON_ERROR));
				}				
			}			
			long[] methodResult = myClient.update(fileMethodArgument, revisionParameter, Depth.infinity, changeWorkingCopyToSpecifiedDepth, 
												  ignoreExternals, allowUnversionedObstructions);
			if(methodResult[0] == -1)
				return false;				
		} catch (SVNException | SVNClientException e) {
			operationSuccesful = false;
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand("Error", e.getMessage(), DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		} finally {
			try {
				opMng.endOperation();
			} catch (SVNException e) {
				logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
				channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand("Error", e.getMessage(), DisplaySimpleMessageClientCommand.ICON_ERROR));
				pm.done();
				if(operationSuccesful) {
					return true;
				}
				return false;				
			}
			pm.done();
		}
		return true;
	}
	
	/**
	 * 
	 * @author Cristina Necula
	 * 
	 */

	public List<String> getPreviousComments(ServiceInvocationContext context) {
		CommunicationChannel cc = context.getCommunicationChannel();
		User user = (User) cc.getPrincipal().getUser();
		List<String> comments = new ArrayList<String>();
		for (SVNCommentEntity comment : UserService.getInstance()
				.getSVNCommentsOrderedByTimestamp(user, false)) {
			comments.add(comment.getBody());
		}
		return comments;
	}
	
	/**
	 * 
	 * @author Cristina Necula
	 * 
	 */
	private void addComment(final String iuser, final String comment) {
		
		DatabaseOperationWrapper wrapper = new DatabaseOperationWrapper(
				new DatabaseOperation() {
					
					@Override
					public void run() {

						User user = wrapper.findByField(User.class, "login", iuser).get(0);
						
						if (comment != null && comment.trim().length() > 0) {
							List<SVNCommentEntity> previousComments = UserService
									.getInstance()
									.getSVNCommentsOrderedByTimestamp(user,
											true);

							// verifies if the comment is already in the list
							int index = -1;
							for (int i = 0; i < previousComments.size(); i++) {
								if (previousComments.get(i).getBody()
										.equals(comment)) {
									index = i;
									break;
								}
							}
							if (index != -1) { // exists, remove it
								user.getSvnComments().remove(previousComments.get(index));
								wrapper.merge(user);
								wrapper.delete(previousComments.get(index));
								previousComments.remove(index);
							}

							// add it again so that it will be seen as the last
							// used comment
							
							SVNCommentEntity newComment = EntityFactory.eINSTANCE
									.createSVNCommentEntity();
							
							newComment = (SVNCommentEntity)wrapper.merge(newComment);
							
							newComment.setUser(user);
							newComment.setBody(comment);
							newComment.setTimestamp(System.currentTimeMillis());
							previousComments.add(newComment);
							
							newComment= (SVNCommentEntity)wrapper.merge(newComment);
							wrapper.setOperationResult(newComment);

							// delete old comments
							if (previousComments.size() > MAX_SVN_COMMENTS) {
								for (int i = 0; i < previousComments.size()
										- MAX_SVN_COMMENTS; i++) {
									wrapper.delete(previousComments.remove(i));
								}
							}

							// set new list of comments to user

							user.getSvnComments().add(newComment);
							for (int i = 0; i < previousComments.size(); i++) {
								user.getSvnComments().add(
										previousComments.get(i));
							}
							user = (User) wrapper.merge(user);

						}
					}

				});
	}
	
	/**
	 * 
	 * @author Cristina Necula
	 * 
	 */
	public boolean deleteSvnAction(ServiceInvocationContext context, List<List<PathFragment>> objectFullPaths, String comment){
		
		final CommunicationChannel cc = context.getCommunicationChannel();
		
		//list of remote resources
		List<ISVNRemoteResource> remoteObject = new ArrayList<ISVNRemoteResource>();
		
		//list of repository location
		final List<SVNRepositoryURLEntity> repositoryObject = new ArrayList<SVNRepositoryURLEntity>();
		
		for(List<PathFragment> fullPath : objectFullPaths){
			
			Object node = GenericTreeStatefulService.getNodeByPathFor(fullPath, null);
			
			//add in the list of repos
			if (node.getClass().equals(SVNRepositoryLocation.class)){
				
				SVNRepositoryLocation repo = (SVNRepositoryLocation) node;
				String organizationName = fullPath.get(1).getName();
				Organization org = EntityFactory.eINSTANCE.createOrganization();
				org.setName(organizationName);
				
				SVNRepositoryURLEntity urlEntity = EntityFactory.eINSTANCE
						.createSVNRepositoryURLEntity();
				urlEntity.setName(repo.getLocation());
				urlEntity.setOrganization(org);
				
				repositoryObject.add(urlEntity);
				
			}
			
			//add in the list of remote resources
			if (node.getClass().equals(RemoteFolder.class)){
				remoteObject.add((ISVNRemoteResource)node);
			}
		}
		
		ProgressMonitor monitor = ProgressMonitor.create(
				SvnPlugin.getInstance().getMessage("svn.deleteSvnAction.monitor.title"), cc);
		try {
			//save comment
			addComment(cc.getPrincipal().getUser().getLogin(), comment);

			//delete remote resource
			if(remoteObject.size() >= 1){
				SVNProviderPlugin.getPlugin().getRepositoryResourcesManager().
				deleteRemoteResources(remoteObject.toArray(
						new ISVNRemoteResource[remoteObject.size()]), comment, monitor);
			}
			
			//delete repository action
			if(repositoryObject.size() >= 1){
				
				//final List<SVNRepositoryURLEntity> repos;
				
				DatabaseOperationWrapper wrapper = new DatabaseOperationWrapper(
						new DatabaseOperation() {
					
					@Override
					public void run() {
						for(SVNRepositoryURLEntity url : repositoryObject){
							SVNRepositoryURLEntity toDelete = 
									wrapper.findByField(SVNRepositoryURLEntity.class, "name", url.getName()).get(0);
							if (toDelete != null){
								Organization org = toDelete.getOrganization();
								org.getSvnRepositoryURLs().remove(toDelete);
								wrapper.merge(org);
								wrapper.delete(toDelete);
							}
						}
					}
				});
			}
			
		} catch (SVNException e) {
			e.printStackTrace();
			logger.error("Exception thrown while deleting remote folders!", e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							SvnPlugin.getInstance().getMessage("svn.deleteSvnAction.error"), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		} finally {
			monitor.done();
		}
		return true;
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
