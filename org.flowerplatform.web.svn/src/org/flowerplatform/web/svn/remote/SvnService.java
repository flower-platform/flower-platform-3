package org.flowerplatform.web.svn.remote;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.team.core.RepositoryProvider;
import org.eclipse.team.core.TeamException;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.progress_monitor.ProgressMonitor;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;
import org.flowerplatform.web.entity.EntityFactory;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.SVNCommentEntity;
import org.flowerplatform.web.entity.SVNRepositoryURLEntity;
import org.flowerplatform.web.entity.User;
import org.flowerplatform.web.entity.WorkingDirectory;
import org.flowerplatform.web.projects.remote.ProjectsService;
import org.flowerplatform.web.security.sandbox.FlowerWebPrincipal;
import org.flowerplatform.web.security.service.UserService;
import org.flowerplatform.web.svn.SvnPlugin;
import org.flowerplatform.web.svn.operation.SvnOperationNotifyListener;
import org.flowerplatform.web.svn.remote.dto.FileDto;
import org.flowerplatform.web.svn.remote.dto.GetModifiedFilesDto;
import org.hibernate.Query;
import org.hsqldb.lib.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tigris.subversion.subclipse.core.ISVNCoreConstants;
import org.tigris.subversion.subclipse.core.ISVNRemoteFolder;
import org.tigris.subversion.subclipse.core.ISVNRemoteResource;
import org.tigris.subversion.subclipse.core.ISVNRepositoryLocation;
import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.SVNProviderPlugin;
import org.tigris.subversion.subclipse.core.repo.SVNRepositoryLocation;
import org.tigris.subversion.subclipse.core.resources.RemoteFile;
import org.tigris.subversion.subclipse.core.resources.RemoteFolder;
import org.tigris.subversion.subclipse.core.resources.RemoteResource;
import org.tigris.subversion.subclipse.core.resources.SVNWorkspaceRoot;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.ISVNConflictResolver;
import org.tigris.subversion.svnclientadapter.ISVNInfo;
import org.tigris.subversion.svnclientadapter.ISVNStatus;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNRevision;
import org.tigris.subversion.svnclientadapter.SVNStatusKind;
import org.tigris.subversion.svnclientadapter.SVNUrl;
import org.tigris.subversion.svnclientadapter.javahl.JhlClientAdapter;
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

	public static final ThreadLocal<InvokeServiceMethodServerCommand> tlCommand = new ThreadLocal<InvokeServiceMethodServerCommand>();

	public static final ThreadLocal<String> tlURI = new ThreadLocal<String>();

	private boolean recurse = true;

	private String workspaceLocation = CommonPlugin.getInstance()
			.getWorkspaceRoot().getAbsolutePath();

	/**
	 * @flowerModelElementId _yaKVkAMcEeOrJqcAep-lCg
	 */

	public void setRecurse(boolean recurse) {
		this.recurse = recurse;
	}

	public static SvnService getInstance() {
		return INSTANCE;
	}

	/**
	 * @author Gabriela Murgoci
	 * @throws SVNException
	 */
	public boolean createRemoteFolder(ServiceInvocationContext context,
			List<PathFragment> parentPath, String folderName, String comment)
			throws SVNException {
		CommunicationChannel cc = (CommunicationChannel) context
				.getCommunicationChannel();
		Object selectedParent = GenericTreeStatefulService.getNodeByPathFor(
				parentPath, null);
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
			// save comment
			addComment(cc.getPrincipal().getUser().getLogin(), comment);
			// create remote folder
			parentFolder.createRemoteFolder(folderName, comment,
					new NullProgressMonitor());
		} catch (SVNException e) { // something wrong happened
			if (isAuthentificationException(e))
				return true;
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			cc.appendOrSendCommand(new DisplaySimpleMessageClientCommand(
					CommonPlugin.getInstance().getMessage("error"), e
							.getMessage(),
					DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		}
		// tree refresh
		ISVNRepositoryLocation repository = null;
		repository = parentFolder.getRepository();
		if (repository.getRootFolder().equals(parentFolder)) {
			repository.refreshRootFolder();
			((GenericTreeStatefulService) GenericTreeStatefulService
					.getServiceFromPathWithRoot(parentPath))
					.dispatchContentUpdate(repository);
		} else {
			parentFolder.refresh();
			((GenericTreeStatefulService) GenericTreeStatefulService
					.getServiceFromPathWithRoot(parentPath))
					.dispatchContentUpdate(parentFolder);
		}
		return true;
	}

	/**
	 * 
	 * @author Gabriela Murgoci
	 */
	public boolean renameMove(ServiceInvocationContext context,
			List<PathFragment> remoteResourcePath,
			List<PathFragment> destinationPath, String remoteResourceName,
			String comment) {
		CommunicationChannel cc = (CommunicationChannel) context
				.getCommunicationChannel();
		GenericTreeStatefulService explorerService = (GenericTreeStatefulService) GenericTreeStatefulService
				.getServiceFromPathWithRoot(remoteResourcePath);
		ISVNRemoteResource remoteResource = (ISVNRemoteResource) GenericTreeStatefulService
				.getNodeByPathFor(remoteResourcePath, null);
		Object selection = GenericTreeStatefulService.getNodeByPathFor(
				destinationPath, null);
		ISVNRemoteFolder parentFolder = null;
		if (selection instanceof ISVNRemoteFolder) {
			parentFolder = (ISVNRemoteFolder) selection;
		} else if (selection instanceof IAdaptable) {
			// ISVNRepositoryLocation is adaptable to ISVNRemoteFolder
			IAdaptable a = (IAdaptable) selection;
			Object adapter = a.getAdapter(ISVNRemoteFolder.class);
			parentFolder = (ISVNRemoteFolder) adapter;
		}
		try {
			// save comment
			addComment(cc.getPrincipal().getUser().getLogin(), comment);
			ISVNClientAdapter svnClient = remoteResource.getRepository()
					.getSVNClient();
			SVNUrl destUrl = parentFolder.getUrl().appendPath(
					remoteResourceName);
			svnClient.move(remoteResource.getUrl(), destUrl, comment,
					SVNRevision.HEAD);
		} catch (Exception e) {
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			cc.appendOrSendCommand(new DisplaySimpleMessageClientCommand(
					CommonPlugin.getInstance().getMessage("error"), e
							.getMessage(),
					DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		}
		List<PathFragment> sourcePath = remoteResourcePath.subList(0,
				remoteResourcePath.size() - 1);
		Object selection1 = GenericTreeStatefulService.getNodeByPathFor(
				sourcePath, null);
		ISVNRemoteFolder sourceNode = null;
		if (selection1 instanceof ISVNRemoteFolder) {
			sourceNode = (ISVNRemoteFolder) selection1;
		} else if (selection instanceof IAdaptable) {
			// ISVNRepositoryLocation is adaptable to ISVNRemoteFolder
			IAdaptable a = (IAdaptable) selection1;
			Object adapter = a.getAdapter(ISVNRemoteFolder.class);
			sourceNode = (ISVNRemoteFolder) adapter;
		}
		sourceNode.refresh();
		explorerService.dispatchContentUpdate(sourceNode);
		ISVNRepositoryLocation repository = null;
		// refresh for source
		repository = sourceNode.getRepository();
		if (repository.getRootFolder().equals(sourceNode)) {
			repository.refreshRootFolder();
			((GenericTreeStatefulService) GenericTreeStatefulService
					.getServiceFromPathWithRoot(sourcePath))
					.dispatchContentUpdate(repository);
		} else {
			parentFolder.refresh();
			((GenericTreeStatefulService) GenericTreeStatefulService
					.getServiceFromPathWithRoot(sourcePath))
					.dispatchContentUpdate(sourceNode);
		}
		// refresh for destination
		repository = parentFolder.getRepository();
		if (repository.getRootFolder().equals(parentFolder)) {
			repository.refreshRootFolder();
			((GenericTreeStatefulService) GenericTreeStatefulService
					.getServiceFromPathWithRoot(destinationPath))
					.dispatchContentUpdate(repository);
		} else {
			parentFolder.refresh();
			((GenericTreeStatefulService) GenericTreeStatefulService
					.getServiceFromPathWithRoot(destinationPath))
					.dispatchContentUpdate(parentFolder);
		}
		return true;
	}

	/**
	 * 
	 * @author Gabriela Murgoci
	 * @throws MalformedURLException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean branchTagResources(ServiceInvocationContext context,
			boolean resourceSelected, List<BranchResource> branchResources,
			String destinationURL, String comment, Number revision,
			boolean createMissingFolders, boolean preserveFolderStructure)
			throws MalformedURLException {
		boolean createOnServer = true;
		boolean makeParents = true;
		ISVNRemoteFolder destinationFolder;
		ISVNRepositoryLocation repository;
		CommunicationChannel cc = (CommunicationChannel) context
				.getCommunicationChannel();
		try {
			// save comment
			addComment(cc.getPrincipal().getUser().getLogin(), comment);
			List<Object> remoteResources = new ArrayList<Object>();
			for (BranchResource item : branchResources) {
				remoteResources.add(GenericTreeStatefulService
						.getNodeByPathFor((List<PathFragment>) item.getPath(),
								null));
			}
			SVNUrl[] sourceUrls = null;
			if (remoteResources.size() > 1) {
				ArrayList<SVNUrl> urlArray = new ArrayList<SVNUrl>();
				if (!resourceSelected) {
					for (int i = 0; i < remoteResources.size(); i++) {
						urlArray.add(((ISVNRemoteResource) remoteResources
								.get(i)).getUrl());
					}
				} else {
					for (int i = 0; i < remoteResources.size(); i++) {
						try {
							File[] listOfFiles = new File[1];
							listOfFiles[0] = (File) ((Pair) remoteResources
									.get(i)).a;
							ISVNStatus[] listOfStatuses = SVNProviderPlugin
									.getPlugin().getSVNClient()
									.getStatus(listOfFiles);
							urlArray.add(listOfStatuses[0].getUrl());
						} catch (Exception e) {
						}
					}
				}
				sourceUrls = new SVNUrl[urlArray.size()];
				urlArray.toArray(sourceUrls);
			} else {
				if (!resourceSelected) {
					sourceUrls = new SVNUrl[1];
					sourceUrls[0] = ((ISVNRemoteResource) (remoteResources
							.get(0))).getUrl();
				} else {
					try {
						sourceUrls = new SVNUrl[1];
						File[] listOfFiles = new File[1];
						listOfFiles[0] = (File) ((Pair) remoteResources.get(0)).a;
						ISVNStatus[] listOfStatuses = SVNProviderPlugin
								.getPlugin().getSVNClient()
								.getStatus(listOfFiles);
						sourceUrls[0] = listOfStatuses[0].getUrl();
					} catch (Exception e1) {
					}
				}
			}
			ISVNClientAdapter client = null;
			repository = SVNProviderPlugin.getPlugin().getRepository(
					sourceUrls[0].toString());
			if (!destinationURL.equals(repository.getUrl().toString())) {
				destinationFolder = repository.getRemoteFolder(destinationURL
						.substring(
								destinationURL.lastIndexOf(repository
										.getRootFolder().getUrl().toString())
										+ repository.getRootFolder().getUrl()
												.toString().length(),
								destinationURL.lastIndexOf("/")));
			}

			else {
				destinationFolder = repository.getRootFolder();
			}
			if (repository != null)
				client = repository.getSVNClient();
			if (client == null)
				client = SVNProviderPlugin.getPlugin().getSVNClientManager()
						.getSVNClient();
			try {
				if (createOnServer) {
					boolean copyAsChild = sourceUrls.length > 1;
					String commonRoot = null;
					if (copyAsChild) {
						commonRoot = getCommonRoot(sourceUrls);
					}
					if (!copyAsChild
							|| destinationURL.toString().startsWith(commonRoot)) {
						System.out.println(sourceUrls.toString());
						client.copy(sourceUrls, new SVNUrl(destinationURL),
								comment, SVNRevision.HEAD, copyAsChild,
								makeParents);
					} else {
						for (int i = 0; i < sourceUrls.length; i++) {
							String fromUrl = sourceUrls[i].toString();
							String uncommonPortion = fromUrl
									.substring(commonRoot.length());
							String toUrl = destinationURL.toString()
									+ uncommonPortion;
							SVNUrl destination = new SVNUrl(toUrl);
							SVNUrl[] source = { sourceUrls[i] };
							client.copy(source, destination, comment,
									SVNRevision.HEAD, copyAsChild, makeParents);
						}
					}
				}
			} catch (Exception e) {
				logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
				cc.appendOrSendCommand(new DisplaySimpleMessageClientCommand(
						CommonPlugin.getInstance().getMessage("error"), e
								.getMessage(),
						DisplaySimpleMessageClientCommand.ICON_ERROR));
				return false;
			}
		} catch (Exception e) {
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			cc.appendOrSendCommand(new DisplaySimpleMessageClientCommand(
					CommonPlugin.getInstance().getMessage("error"), e
							.getMessage(),
					DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		}
		// tree refresh
		if (!resourceSelected) {
			
			List<PathFragment> partialPath = (List<PathFragment>) branchResources
					.get(0).getPath();
			partialPath.remove(partialPath.size() - 1);
			Object selection = GenericTreeStatefulService.getNodeByPathFor(
					partialPath, null);
			destinationFolder.refresh();
			((GenericTreeStatefulService) GenericTreeStatefulService
					.getServiceFromPathWithRoot(partialPath))
					.dispatchContentUpdate(destinationFolder);
			ISVNRemoteFolder node = null;

			if (selection instanceof ISVNRemoteFolder) {
				node = (ISVNRemoteFolder) selection;
			} else if (selection instanceof IAdaptable) {
				// ISVNRepositoryLocation is adaptable to ISVNRemoteFolder
				IAdaptable a = (IAdaptable) selection;
				Object adapter = a.getAdapter(ISVNRemoteFolder.class);
				node = (ISVNRemoteFolder) adapter;
			}
			node.refresh();
			((GenericTreeStatefulService) GenericTreeStatefulService
					.getServiceFromPathWithRoot(partialPath))
					.dispatchContentUpdate(selection);
		} else {
			List<PathFragment> partialPath = (List<PathFragment>) branchResources
					.get(0).getPath();
			partialPath.remove(partialPath.size() - 1);

			if (repository.getRootFolder().equals(destinationFolder)) {
				repository.refreshRootFolder();
				((GenericTreeStatefulService) GenericTreeStatefulService
						.getServiceFromPathWithRoot(partialPath))
						.dispatchContentUpdate(repository);
			} else {
				destinationFolder.refresh();
				((GenericTreeStatefulService) GenericTreeStatefulService
						.getServiceFromPathWithRoot(partialPath))
						.dispatchContentUpdate(destinationFolder);
			}
		}
		return true;
	}

	/**
	 * 
	 * @author Gabriela Murgoci
	 */
	private String getCommonRoot(SVNUrl[] sourceUrls) {
		String commonRoot = null;
		String urlString = sourceUrls[0].toString();
		tag1: for (int i = 0; i < urlString.length(); i++) {
			String partialPath = urlString.substring(0, i + 1);
			if (partialPath.endsWith("/")) {
				for (int j = 1; j < sourceUrls.length; j++) {
					if (!sourceUrls[j].toString().startsWith(partialPath))
						break tag1;
				}
				commonRoot = partialPath.substring(0, i);
			}
		}
		return commonRoot;
	}

	/**
	 * 
	 * @author Gabriela Murgoci
	 */
	public List<PathFragment> getCommonParent(List<List<PathFragment>> selection) {
		int index = 0;
		boolean sem = true;
		int size = selection.get(0).size();

		for (int i = 0; i < size && sem; i++) {
			index = i;
			PathFragment element = selection.get(0).get(i);
			for (int j = 0; j < selection.size(); j++) {
				if (!(selection.get(j).get(i).equals(element))) {
					sem = false;
					index--;
					break;
				}
			}
		}
		return selection.get(0).subList(0, index + 1);
	}

	/**
	 * 
	 * @author Gabriela Murgoci
	 * @throws SVNException
	 */
	public ArrayList<Object> populateBranchResourcesList(
			ServiceInvocationContext context,
			List<List<PathFragment>> selected, boolean actionType)
			throws SVNException {
		ArrayList<Object> branchResources = new ArrayList<Object>();
		List<PathFragment> commonParent = selected.get(0);
		File[] listOfFiles;
		Object remoteResource;
		ISVNRepositoryLocation repository = null;
		SVNUrl aux;
		String commonParentPath = "";
		commonParent = getCommonParent(selected);
		for (List<PathFragment> selectedResource : selected) {
			BranchResource item = new BranchResource();
			String partialPath = "";
			List<PathFragment> partialPathList = selectedResource.subList(
					commonParent.size(), selectedResource.size());
			for (int i = 0; i < partialPathList.size(); i++) {
				partialPath += partialPathList.get(i).getName();
				if (i < partialPathList.size())
					partialPath += "/";
			}
			remoteResource = GenericTreeStatefulService.getNodeByPathFor(
					selectedResource, null);
			// /

			// /
			item.setPath(selectedResource);
			item.setName(selectedResource.get(selectedResource.size() - 1)
					.getName());
			item.setPartialPath(partialPath);
			if (actionType) {
				File file;
				file = (File) ((Pair) remoteResource).a;
				if (file.isDirectory())
					item.setImage("images/folder_pending.gif");
				else
					item.setImage("images/file.gif");
			} else {
				if (selectedResource.get(selectedResource.size() - 1).getType()
						.equals("svnFolder"))
					item.setImage("images/folder_pending.gif");
				else
					item.setImage("images/file.gif");
			}
			branchResources.add(item);
		}
		if (actionType) {
			try {
				remoteResource = GenericTreeStatefulService.getNodeByPathFor(
						(List<PathFragment>) selected.get(0), null);
				listOfFiles = new File[] { (File) ((Pair<?, ?>) remoteResource).a };
				ISVNStatus[] listOfStatuses = SVNProviderPlugin.getPlugin()
						.getSVNClient().getStatus(listOfFiles);
				aux = listOfStatuses[0].getUrl();
				repository = SVNProviderPlugin.getPlugin().getRepository(
						aux.toString());
			} catch (Exception e1) {
			}
		}
		for (int i = 3; i < commonParent.size(); i++) {
			commonParentPath += commonParent.get(i).getName();
			if (i < commonParent.size() - 1)
				commonParentPath += "/";
		}
		if (actionType)
			branchResources.add(0, (repository.getRootFolder().getUrl()
					.toString()
					+ "/" + commonParentPath));
		else
			branchResources.add(0, commonParentPath);
		branchResources.add(0, commonParent);
		return branchResources;
	}

	/**
	 * 
	 * @author Gabriela Murgoci
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean switchTo(ServiceInvocationContext context,
			List<BranchResource> branchResources, String location,
			long revision, int depth, boolean setDepth,
			boolean ignoreExternals, boolean force) {
		SVNUrl[] urls;
		try {
			List<Object> resources = new ArrayList<Object>();
			for (BranchResource item : branchResources) {
				resources.add(GenericTreeStatefulService.getNodeByPathFor(
						(List<PathFragment>) item.getPath(), null));
			}
			// computes the new URLs for selected resources
			if (branchResources.size() == 1) {
				urls = new SVNUrl[1];
				urls[0] = new SVNUrl(location);
			} else {
				urls = new SVNUrl[branchResources.size()];
				for (int i = 0; i < resources.size(); i++) {
					try {
						File[] listOfFiles = new File[1];
						listOfFiles[0] = (File) ((Pair) resources.get(i)).a;
						ISVNStatus[] listOfStatuses = SVNProviderPlugin
								.getPlugin().getSVNClient()
								.getStatus(listOfFiles);
						if (location.endsWith("/")) {
							urls[i] = new SVNUrl(location
									+ listOfStatuses[0].getUrl());
						} else {
							urls[i] = new SVNUrl(location + "/"
									+ listOfStatuses[0].getUrl());
						}
					} catch (Exception e) {
					}
				}
			}
			try {
				for (int i = 0; i < resources.size(); i++) {
					SVNUrl svnUrl = urls[i];
					ISVNRepositoryLocation repository = SVNProviderPlugin
							.getPlugin().getRepository(urls[0].toString());
					try {
						ISVNClientAdapter svnClient = null;
						if (repository != null)
							svnClient = repository.getSVNClient();
						if (svnClient == null)
							svnClient = SVNProviderPlugin.getPlugin()
									.getSVNClientManager().getSVNClient();
						File file = (File) ((Pair) resources.get(i)).a;
						svnClient.switchToUrl(file, svnUrl,
								revision == -1 ? SVNRevision.HEAD
										: new SVNRevision.Number(revision),
								SVNRevision.HEAD, depth, setDepth,
								ignoreExternals, force);
					} catch (SVNClientException e) {
						throw SVNException.wrapException(e);
					} finally {
					}
				}
			} finally {
			}
		} catch (Exception e) {
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			CommunicationChannel cc = (CommunicationChannel) context
					.getCommunicationChannel();
			cc.appendOrSendCommand(new DisplaySimpleMessageClientCommand(
					CommonPlugin.getInstance().getMessage("error"), e
							.getMessage(),
					DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		}
		return true;
	}

	/**
	 * @author Gabriela Murgoci
	 */
	@SuppressWarnings("rawtypes")
	public boolean cleanUp(ServiceInvocationContext context,
			List<List<PathFragment>> selection) {
		List<Object> resources = new ArrayList<Object>();
		ISVNRepositoryLocation repository = null;
		SVNUrl url = null;
		for (List<PathFragment> item : selection) {
			resources.add(GenericTreeStatefulService.getNodeByPathFor(
					(List<PathFragment>) item, null));
		}
		try {
			File[] listOfFiles = new File[1];
			listOfFiles[0] = (File) ((Pair) resources.get(0)).a;
			ISVNStatus[] listOfStatuses = SVNProviderPlugin.getPlugin()
					.getSVNClient().getStatus(listOfFiles);
			url = new SVNUrl(listOfStatuses[0].getUrl().toString());
		} catch (Exception e) {
		}
		try {
			repository = SVNProviderPlugin.getPlugin().getRepository(
					url.toString());
		} catch (SVNException e) {
		}
		for (int i = 0; i < resources.size(); i++) {
			try {
				ISVNClientAdapter svnClient = null;
				if (repository != null)
					svnClient = repository.getSVNClient();
				if (svnClient == null)
					svnClient = SVNProviderPlugin.getPlugin()
							.getSVNClientManager().getSVNClient();
				File file = (File) ((Pair) resources.get(i)).a;
				svnClient.cleanup(file);
			} catch (SVNException e) {
			} catch (SVNClientException e) {
				e.printStackTrace();
			} finally {
			}
		}
		return true;
	}

	/**
	 * @author Gabriela Murgoci
	 */
	public boolean canExecute(List<List<PathFragment>> selection) {
		for (int i = 0; i < selection.size(); i++) {
			IResource res = ProjectsService.getInstance()
					.getProjectWrapperResourceFromFile(selection.get(i));
			IProject project = res.getProject();
			if (!project.isAccessible()) {
				return false;
			}
			if (RepositoryProvider.isShared(project)) {
				return false;
			}
		}
		return selection.size() > 0;
	}

	/**
	 * @author Gabriela Murgoci
	 */
	public ArrayList<Object> populateRepositoriesInfo(
			ServiceInvocationContext context, List<List<PathFragment>> selection) {
		if (!canExecute(selection)) {
			return null;
		}
		ArrayList<Object> repositoriesInfo = new ArrayList<Object>();
		ArrayList<String> repositoriesUrls = getRepositoriesForOrganization(selection
				.get(0).get(1).getName());
		for (String repositoryUrl : repositoriesUrls) {
			ISVNRepositoryLocation repository;
			try {
				repository = SVNProviderPlugin.getPlugin().getRepository(
						repositoryUrl);
			} catch (SVNException e) {
				continue;
			}
			BranchResource item = new BranchResource();
			item.setPath(repository.getUrl().toString());
			item.setName(repositoryUrl);
			item.setImage("images/share.png");
			repositoriesInfo.add(item);
		}
		return repositoriesInfo;
	}

	/**
	 * @author Gabriela Murgoci
	 */
	public boolean shareProject(ServiceInvocationContext context,
			List<PathFragment> projectPath, String repositoryUrl,
			String directoryName, boolean create, String comment) {
		SVNUrl url;
		CommunicationChannel cc = (CommunicationChannel) context
				.getCommunicationChannel();
		try {
			// save comment
			addComment(cc.getPrincipal().getUser().getLogin(), comment);
			ISVNRepositoryLocation repository;
			if (create) {
				Properties properties = new Properties();
				properties.setProperty("url", repositoryUrl);

				repository = SVNProviderPlugin.getPlugin().getRepositories()
						.createRepository(properties);
			} else {
				repository = SVNProviderPlugin.getPlugin().getRepositories()
						.getRepository(repositoryUrl);
			}
			if (!SVNProviderPlugin.getPlugin().getRepositories()
					.isKnownRepository(repository.getLocation(), false)) {
				SVNProviderPlugin.getPlugin().getRepositories()
						.addOrUpdateRepository(repository);
			}
			IResource res = ProjectsService.getInstance()
					.getProjectWrapperResourceFromFile(projectPath);
			IProject project = res.getProject();
			// Purge any SVN folders that may exists in sub folders
			SVNWorkspaceRoot.getSVNFolderFor(project).unmanage(null);
			boolean alreadyExists = SVNProviderPlugin.getPlugin()
					.getRepositories()
					.isKnownRepository(repository.getLocation(), false);
			try {
				final ISVNClientAdapter svnClient = repository.getSVNClient();
				try {
					// create the remote dir
					if (directoryName != null)
						url = repository.getUrl().appendPath(directoryName);
					else
						url = repository.getUrl();
					boolean createDirectory = !repository.getRemoteFolder(
							directoryName).exists(null);
					if (createDirectory)
						svnClient.mkdir(url, true, comment);
					try {
						// checkout it so that we have .svn
						// If directory already existed in repository, do
						// recursive checkout.
						svnClient.checkout(url, project.getLocation().toFile(),
								SVNRevision.HEAD, !createDirectory);
					} finally {
					}
				} catch (SVNClientException e) {
					logger.error("Exception thrown while creating module!", e);
					cc.appendOrSendCommand(new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), e
									.getMessage(),
							DisplaySimpleMessageClientCommand.ICON_ERROR));
				}
				// SharingWizard.doesSVNDirectoryExist calls
				// getStatus on the folder which populates the
				// status cache
				// Need to clear the cache so we can get the new
				// hasRemote value
				SVNProviderPlugin.getPlugin().getStatusCacheManager()
						.refreshStatus(project, true);
				try {
					// Register it with Team.
					RepositoryProvider.map(project,
							SVNProviderPlugin.getTypeId());
				} catch (TeamException e) {
					throw new SVNException(
							"Cannot register project with svn provider", e);
				}
			} catch (SVNException e) {
				// The checkout may have triggered password caching
				// Therefore, if this is a newly created location, we want to
				// clear
				// its cache
				if (!alreadyExists)
					SVNProviderPlugin.getPlugin().getRepositories()
							.disposeRepository(repository);
				throw e;
			}
			// Add the repository if it didn't exist already
			if (!alreadyExists)
				SVNProviderPlugin.getPlugin().getRepositories()
						.addOrUpdateRepository(repository);
		} catch (Exception e) {
			if (isAuthentificationException(e)
					|| (e instanceof SVNException
							&& ((SVNException) e).getStatus() != null && ((SVNException) e)
							.getStatus().getCode() == TeamException.UNABLE)) {
				return true;
			}
			logger.error("Exception thrown while sharing project!", e);
			cc.appendOrSendCommand(new DisplaySimpleMessageClientCommand(
					CommonPlugin.getInstance().getMessage("error"), e
							.getMessage(),
					DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public String getRepositoryNameAndWorkingDirectory(
			ArrayList<PathFragment> pathFragments) {
		// if selected item is svn repository tree node, the "working directory"
		// will be the path from node.
		String result;
		Object workingDirectory = GenericTreeStatefulService.getNodeByPathFor(
				pathFragments, null);
		if (workingDirectory instanceof WorkingDirectory) {
			String pathFromOrganization = ((WorkingDirectory) workingDirectory)
					.getPathFromOrganization();
			String partialResult = ""
					+ ProjectsService.getInstance().getOrganizationDir(
							((WorkingDirectory) workingDirectory)
									.getOrganization().getLabel()) + "\\"
					+ pathFromOrganization;
			result = pathFromOrganization + " in "
					+ getSvnUrlForPath(partialResult, true);
		} else if (workingDirectory instanceof RemoteFolder) {
			RemoteFolder rf = (RemoteFolder) workingDirectory;
			result = rf.getUrl().toString()
					.substring(rf.getRepository().getUrl().toString().length())
					+ " in "
					+ ((RemoteFolder) workingDirectory).getRepository()
							.getUrl().toString();
		} else if (workingDirectory instanceof SVNRepositoryLocation) {
			result = "in "
					+ ((SVNRepositoryLocation) workingDirectory).getUrl()
							.toString();
		} else {
			String workingDirectoryPath = getDirectoryFullPathFromPathFragments(pathFragments);
			File f = ((Pair<File, String>) workingDirectory).a;
			result = (f.getAbsolutePath()).substring(f.getAbsolutePath()
					.lastIndexOf("\\"))
					+ " in "
					+ getSvnUrlForPath(workingDirectoryPath, true);
		}
		return result;
	}

	/**
	 * 
	 * @author Victor Badila
	 * 
	 */
	public String getUrlPathForSingleSelection(ArrayList<PathFragment> path,
			Boolean wantUrlForRepository) {
		String workingDirectoryPath = getDirectoryFullPathFromPathFragments(path);
		if (wantUrlForRepository) {
			return getSvnUrlForPath(workingDirectoryPath, true);
		}
		return getSvnUrlForPath(workingDirectoryPath, false);
	}

	public boolean createSvnRepository(final ServiceInvocationContext context,
			final String url, final List<PathFragment> parentPath) {
		// had to use List due to limitations of altering final variables inside
		// runnable
		final List<String> operationSuccessful = new ArrayList<String>();
		tlCommand.set(context.getCommand());
		try {
			new DatabaseOperationWrapper(new DatabaseOperation() {
				@SuppressWarnings("unchecked")
				@Override
				public void run() {
					String organizationName = parentPath.get(1).getName();
					try {
						SVNRepositoryLocation repository = SVNRepositoryLocation
								.fromString(url);
						if (repository.pathExists()) {
							// creates entry in database and links it to
							// specified organization
							Query q = wrapper
									.getSession()
									.createQuery(
											String.format(
													"SELECT e from %s e where e.name ='%s'",
													Organization.class
															.getSimpleName(),
													organizationName));
							ArrayList<Object> querryResult = (ArrayList<Object>) q
									.list();
							if (querryResult.isEmpty()) {
								CommunicationChannel channel = (CommunicationChannel) context
										.getCommunicationChannel();
								channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
										"Error",
										SvnPlugin
												.getInstance()
												.getMessage(
														"svn.remote.svnService.createSvnRepository.error.inexistentOrganizationError"),
										DisplaySimpleMessageClientCommand.ICON_ERROR));
								return;
							}
							Object organization = querryResult.get(0);
							SVNRepositoryURLEntity urlEntity = EntityFactory.eINSTANCE
									.createSVNRepositoryURLEntity();
							urlEntity.setName(url);
							urlEntity
									.setOrganization((Organization) organization);
							operationSuccessful.add("success");
						} else {
							CommunicationChannel channel = (CommunicationChannel) context
									.getCommunicationChannel();
							channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
									"Error",
									SvnPlugin
											.getInstance()
											.getMessage(
													"svn.remote.svnService.createSvnRepository.error.invalidUrlError"),
									DisplaySimpleMessageClientCommand.ICON_ERROR));
						}
					} catch (SVNException e) {
						logger.debug(
								CommonPlugin.getInstance().getMessage("error"),
								e);
						CommunicationChannel channel = (CommunicationChannel) context
								.getCommunicationChannel();
						channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
								"Error",
								SvnPlugin
										.getInstance()
										.getMessage(
												"svn.remote.svnService.createSvnRepository.error.svnExceptionError2"),
								DisplaySimpleMessageClientCommand.ICON_ERROR));
					}
				}
			});
		} catch (Exception e) {
			if (isAuthentificationException(e))
				return true;
		}
		// tree refresh
		if (operationSuccessful.contains("success")) {
			Object node = GenericTreeStatefulService.getNodeByPathFor(
					parentPath, null);
			GenericTreeStatefulService.getServiceFromPathWithRoot(parentPath)
					.dispatchContentUpdate(node);
			return true;
		}
		return false;
	}

	public String getCommitUrlPathForSingleSelection(
			ArrayList<PathFragment> path) {
		String workingDirectoryPath = getDirectoryFullPathFromPathFragments(path);
		return getSvnUrlForPath(workingDirectoryPath, true);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> getRepositoriesForOrganization(
			final String organizationName) {
		DatabaseOperationWrapper wrapper = new DatabaseOperationWrapper(
				new DatabaseOperation() {
					@Override
					public void run() {
						ArrayList<String> result = new ArrayList<String>();
						String myQuerry = String
								.format("SELECT e from %s e where e.organization.name ='%s'",
										SVNRepositoryURLEntity.class
												.getSimpleName(),
										organizationName);
						Query q = wrapper.getSession().createQuery(myQuerry);
						List<SVNRepositoryURLEntity> urlEntityList = q.list();
						for (SVNRepositoryURLEntity urlEntity : urlEntityList)
							result.add(urlEntity.getName());
						wrapper.setOperationResult(result);
					}
				});
		return (ArrayList<String>) wrapper.getOperationResult();
	}

	public void refresh(ServiceInvocationContext context,
			List<PathFragment> parentPath) {
		Object node = GenericTreeStatefulService.getNodeByPathFor(parentPath,
				null);
		if (node instanceof RemoteFolder)
			((RemoteFolder) node).refresh();
		else
			((SVNRepositoryLocation) node).refreshRootFolder();
		((GenericTreeStatefulService) GenericTreeStatefulService
				.getServiceFromPathWithRoot(parentPath))
				.dispatchContentUpdate(node);
	}

	public Boolean resolve(ServiceInvocationContext context,
			ArrayList<String> filePaths, int choice) {
		CommunicationChannel channel = (CommunicationChannel) context
				.getCommunicationChannel();
		ProgressMonitor pm = ProgressMonitor
				.create(SvnPlugin.getInstance().getMessage(
						"svn.service.resolve.markResolvedMonitor"), channel);
		SvnOperationNotifyListener opMng = new SvnOperationNotifyListener(
				CommunicationPlugin.tlCurrentChannel.get());
		try {
			ISVNClientAdapter myClientAdapter = SVNProviderPlugin.getPlugin()
					.getSVNClient();
			myClientAdapter.setProgressListener(opMng);
			if (choice == 1) {
				choice = ISVNConflictResolver.Choice.chooseMine;
			} else if (choice == 2) {
				choice = ISVNConflictResolver.Choice.chooseTheirs;
			} else {
				choice = ISVNConflictResolver.Choice.chooseBase;
			}
			for (int i = 0; i < filePaths.size(); i++) {

				myClientAdapter.resolve(
						new File(workspaceLocation + filePaths.get(i)), choice);
			}
		} catch (SVNException | SVNClientException e) {
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
					"Error", e.getMessage(),
					DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		} finally {
			pm.done();
		}
		return true;
	}

	public boolean revert(ServiceInvocationContext context,
			ArrayList<FileDto> fileDtos) {
		CommunicationChannel channel = (CommunicationChannel) context
				.getCommunicationChannel();
		ProgressMonitor pm = ProgressMonitor.create(SvnPlugin.getInstance()
				.getMessage("svn.service.revert.revertMonitor"), channel);
		SvnOperationNotifyListener opMng = new SvnOperationNotifyListener(
				CommunicationPlugin.tlCurrentChannel.get());
		Boolean recurse = true;
		try {
			ISVNClientAdapter myClientAdapter = SVNProviderPlugin.getPlugin()
					.getSVNClient();
			myClientAdapter.setProgressListener(opMng);
			for (int i = 0; i < fileDtos.size(); i++) {
				FileDto currentDto = fileDtos.get(i);
				if (currentDto.getStatus().equals("unversioned")) {

					File f = new File(workspaceLocation
							+ currentDto.getPathFromRoot());
					if (f.isDirectory()) {
						FileUtils.deleteDirectory(f);
					} else {
						f.delete();
					}

				} else {

					myClientAdapter.revert(
							new File(currentDto.getPathFromRoot()), recurse);
				}
			}
		} catch (SVNException | SVNClientException | IOException e) {
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
					"Error", e.getMessage(),
					DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		} finally {
			pm.done();
		}
		return true;
	}

	public boolean checkout(ServiceInvocationContext context, List<List<PathFragment>> folders, List<PathFragment> workingDirectoryPartialPath,
			String workingDirectoryDestination, String revisionNumberAsString, int depthIndex, Boolean headRevision, boolean force, boolean ignoreExternals, String newProjectName) {
		CommunicationChannel channel = (CommunicationChannel) context.getCommunicationChannel();
		ProgressMonitor pm = ProgressMonitor.create(SvnPlugin.getInstance().getMessage("svn.service.checkout.checkoutProgressMonitor"), channel);
		workingDirectoryDestination = getDirectoryFullPathFromPathFragments(workingDirectoryPartialPath) + workingDirectoryDestination;
		SVNRevision revision;
		if (headRevision) {
			revision = SVNRevision.HEAD;
		} else {
			try {
				revision = SVNRevision.getRevision(revisionNumberAsString);
			} catch (ParseException e) {
				logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
				channel.appendOrSendCommand(new DisplaySimpleMessageClientCommand(
						"Error", e.getMessage(),
						DisplaySimpleMessageClientCommand.ICON_ERROR));
				return false;
			}
		}
		RemoteResource[] remoteFolders = new RemoteResource[folders.size()];
		for (int i = 0; i < folders.size(); i++) {
			RemoteResource correspondingRemoteResource;
			Object treeNode = GenericTreeStatefulService.getNodeByPathFor(
					folders.get(i), null);
			if (treeNode instanceof SVNRepositoryLocation) {
				correspondingRemoteResource = (RemoteFolder) ((SVNRepositoryLocation) treeNode)
						.getRootFolder();
			} else {
				correspondingRemoteResource = (RemoteResource) treeNode;
			}
			remoteFolders[i] = correspondingRemoteResource;
		}		
		ISVNClientAdapter myClient;
		SvnOperationNotifyListener opMng = new SvnOperationNotifyListener(CommunicationPlugin.tlCurrentChannel.get());
		try {
			myClient = SVNProviderPlugin.getPlugin().getSVNClient();
			pm.beginTask(null, 1000);			
			opMng.beginOperation(pm, myClient, true);
			myClient.setProgressListener(opMng);
			for (int i = 0; i < folders.size(); i++) {
				File fileArgumentForCheckout;
				if (newProjectName != "" && newProjectName != null) {
					fileArgumentForCheckout = new File(
							workingDirectoryDestination + "\\" + newProjectName);
				} else {
					fileArgumentForCheckout = new File(
							workingDirectoryDestination + "\\"
									+ remoteFolders[i].getName());
				}
				String fullPath = remoteFolders[i].getUrl().toString();

				myClient.checkout(new SVNUrl(fullPath),
						fileArgumentForCheckout, revision,
						getDepthValue(depthIndex), ignoreExternals, force);
				ProjectsService.getInstance().createOrImportProjectFromFile(
						context, fileArgumentForCheckout);
			}
			opMng.endOperation();
		} catch (Exception e) {
			if (isAuthentificationException(e))
				return true;
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			channel.appendOrSendCommand(new DisplaySimpleMessageClientCommand(
					"Error", e.getMessage(),
					DisplaySimpleMessageClientCommand.ICON_ERROR));


			return false;
		} finally {
			pm.done();
		}
		return true;
	}

	public ArrayList<String> getWorkingDirectoriesForOrganization(
			ServiceInvocationContext context, String organizationName) {
		ArrayList<String> result = new ArrayList<String>();
		try {
			ProjectsService pj = new ProjectsService();
			List<WorkingDirectory> workingDirectoryList = pj
					.getWorkingDirectoriesForOrganizationName(organizationName);
			for (WorkingDirectory wd : workingDirectoryList) {
				result.add(wd.getPathFromOrganization());
			}
		} catch (CoreException | URISyntaxException e) {
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			CommunicationChannel channel = (CommunicationChannel) context
					.getCommunicationChannel();
			channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
					"Error",
					SvnPlugin
							.getInstance()
							.getMessage(
									"svn.service.checkout.error.problemsAtBrowseWorkDirs"),
					DisplaySimpleMessageClientCommand.ICON_ERROR));
		}
		return result;
	}

	public int workingDirectoryExistsAndProjectLocationIsValid(
			ServiceInvocationContext context, String wdName,
			String organizationName, String restOfThePath,
			ArrayList<PathFragment> workingDirectoryPartialPath) {
		Boolean wdExists = false;
		String partialPath = getDirectoryFullPathFromPathFragments(workingDirectoryPartialPath);
		ArrayList<String> existingWDs = getWorkingDirectoriesForOrganization(
				context, organizationName);
		for (String wd : existingWDs) {
			if (wd.equals(wdName)) {
				wdExists = true;
			}
		}
		/*
		 * values for return and significance: 0 - working directory exists and
		 * project location is valid 1 - working directory does not exist 2 - a
		 * file with specified location (working directory path + project
		 * location path) already exists
		 */
		if (!wdExists) {
			return 1;
		}
		if (new File(partialPath + "\\" + wdName + "\\" + restOfThePath)
				.exists()) {
			return 2;
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	public String getWorkingDirectoryFullPathFromPathFragments(
			List<PathFragment> pathWithRoot) {
		Object workingDirectory = GenericTreeStatefulService.getNodeByPathFor(
				pathWithRoot, null);

		if (workingDirectory instanceof WorkingDirectory) {
			return ""
					+ ProjectsService.getInstance().getOrganizationDir(
							((WorkingDirectory) workingDirectory)
									.getOrganization().getLabel())
					+ "\\"
					+ ((WorkingDirectory) workingDirectory)
							.getPathFromOrganization();
		} else
			return ((Pair<File, String>) workingDirectory).a.getAbsolutePath();
	}

	public String getDirectoryRelativePathFromPathFragments(
			List<PathFragment> pathWithRoot) {
		return getWorkingDirectoryFullPathFromPathFragments(pathWithRoot)
				.substring(workspaceLocation.length());
	}

	/**
	 * 
	 * @return repository url if parameter points to repository location or the
	 *         path from organization if parameter points to a working directory
	 *         tree node
	 */
	@SuppressWarnings("unchecked")
	public String getDirectoryFullPathFromPathFragments(
			List<PathFragment> pathWithRoot) {
		Object workingDirectory = GenericTreeStatefulService.getNodeByPathFor(
				pathWithRoot, null);
		if (workingDirectory instanceof WorkingDirectory) {
			return ""
					+ ProjectsService.getInstance().getOrganizationDir(
							((WorkingDirectory) workingDirectory)
									.getOrganization().getLabel())
					+ "\\"
					+ ((WorkingDirectory) workingDirectory)
							.getPathFromOrganization();
		} else if (workingDirectory instanceof RemoteFolder) {
			return ((RemoteFolder) workingDirectory).getUrl().toString();
		} else if (workingDirectory instanceof SVNRepositoryLocation) {
			return ((SVNRepositoryLocation) workingDirectory).getUrl()
					.toString();
		} else {
			return ((Pair<File, String>) workingDirectory).a.getAbsolutePath();
		}
	}

	public Boolean createFolderAndMarkAsWorkingDirectory(
			ServiceInvocationContext context, String path, String organization) {
		File targetFile = new File(ProjectsService.getInstance()
				.getOrganizationDir(organization).getAbsolutePath()
				+ "\\" + path);
		try {
			targetFile.getCanonicalPath();
		} catch (IOException e) {
			return false;
		}
		if (targetFile.mkdirs()) {
			ProjectsService.getInstance().markAsWorkingDirectoryForFile(
					context, targetFile);
			return true;
		} else {
			return false;
		}
	}

	public Boolean updateToHEAD(ServiceInvocationContext context,
			List<List<PathFragment>> selectionList) {
		return updateToVersion(context, selectionList, "head", Depth.infinity,
				false, false, true);
	}

	public Boolean updateToVersion(ServiceInvocationContext context,
			List<List<PathFragment>> selectionList, String revision,
			int depth, Boolean changeWorkingCopyToSpecifiedDepth,
			Boolean ignoreExternals, Boolean allowUnversionedObstructions) {
		File[] fileMethodArgument = new File[selectionList.size()];
		for (int i = 0; i < selectionList.size(); i++) {
			List<PathFragment> currentPathSelection = selectionList.get(i);
			String path = currentPathSelection.get(1).getName();
			path = ProjectsService.getInstance().getOrganizationDir(path)
					.getAbsolutePath();
			for (int j = 3; j < currentPathSelection.size(); j++) {
				path = path
						.concat("\\" + currentPathSelection.get(j).getName());
			}
			fileMethodArgument[i] = new File(path);
		}
		CommunicationChannel channel = (CommunicationChannel) context
				.getCommunicationChannel();
		ProgressMonitor pm = ProgressMonitor.create(SvnPlugin.getInstance()
				.getMessage("svn.service.update.updateToHeadMonitor"), channel);
		if (pm != null) {
			pm.beginTask(null, 1000);
		}
		SvnOperationNotifyListener opMng = new SvnOperationNotifyListener(
				CommunicationPlugin.tlCurrentChannel.get());
		Boolean operationSuccesful = true;
		try {
			ISVNClientAdapter myClient = SVNProviderPlugin.getPlugin()
					.getSVNClient();
			myClient.setProgressListener(opMng);
			SVNRevision revisionParameter = null;
			if (revision.equals("head")) {
				revisionParameter = SVNRevision.HEAD;
			} else {
				try {
					revisionParameter = SVNRevision.getRevision(revision);
				} catch (ParseException e) {
					logger.debug(
							CommonPlugin.getInstance().getMessage("error"), e);
					channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
							"Error", e.getMessage(),
							DisplaySimpleMessageClientCommand.ICON_ERROR));
				}
			}
			long[] methodResult = myClient.update(fileMethodArgument,
					revisionParameter, Depth.infinity,
					changeWorkingCopyToSpecifiedDepth, ignoreExternals,
					allowUnversionedObstructions);
			if (methodResult[0] == -1)
				return false;
		} catch (SVNException | SVNClientException e) {
			operationSuccesful = false;
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
					"Error", e.getMessage(),
					DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		} finally {
			try {
				opMng.endOperation();
			} catch (SVNException e) {
				logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
				channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
						"Error", e.getMessage(),
						DisplaySimpleMessageClientCommand.ICON_ERROR));
				pm.done();
				if (operationSuccesful) {
					// mass refresh on all tree nodes
					for (List<PathFragment> al : selectionList) {
						Object node = GenericTreeStatefulService
								.getNodeByPathFor(al, null);
						GenericTreeStatefulService.getServiceFromPathWithRoot(
								al).dispatchContentUpdate(node);
					}
					return true;
				}
				return false;
			}
			pm.done();
		}
		// mass refresh on all tree nodes
		for (List<PathFragment> al : selectionList) {
			Object node = GenericTreeStatefulService.getNodeByPathFor(al, null);
			GenericTreeStatefulService.getServiceFromPathWithRoot(al)
					.dispatchContentUpdate(node);
		}
		return true;
	}

	public String getSvnUrlForPath(String filePath, Boolean wantUrlForRepository) {
		ISVNClientAdapter myClientAdapter;
		try {
			myClientAdapter = SVNProviderPlugin.getPlugin().getSVNClient();
			ISVNInfo info = myClientAdapter.getInfo(new File(filePath));
			if (wantUrlForRepository) {
				return info.getRepository().toString();
			}
			return info.getUrlString();
		} catch (SVNException | SVNClientException e) {
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			return null;
		}
	}

	/**
	 * 
	 * @author Gabriela Murgoci
	 */
	public List<PathFragment> getCommonParent(
			ArrayList<ArrayList<PathFragment>> selectionList) {
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

	public ArrayList<String> setImageUrlForExtensionType(String fileType,
			String status) {
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

	public ArrayList<String> getConflicted(ServiceInvocationContext context,
			List<List<PathFragment>> selectionList) {
		CommunicationChannel channel = (CommunicationChannel) context
				.getCommunicationChannel();
		ArrayList<String> result = new ArrayList<String>();
		try {
			ISVNClientAdapter myClientAdapter = SVNProviderPlugin.getPlugin()
					.getSVNClient();
			File[] filesToBeCompared = getFilesForSelectionList(selectionList);
			for (File f : filesToBeCompared) {
				try {
					ISVNStatus[] jhlStatusArray = myClientAdapter.getStatus(f,
							true, true);
					for (int i = 0; i < jhlStatusArray.length; i++) {

						if (jhlStatusArray[i].getTextStatus().toString()
								.equals("conflicted")) {
							result.add(jhlStatusArray[i].getPath().substring(
									workspaceLocation.length()));

						}
					}
				} catch (SVNClientException e) {
					logger.debug(
							CommonPlugin.getInstance().getMessage("error"), e);
					channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
							"Error", e.getMessage(),
							DisplaySimpleMessageClientCommand.ICON_ERROR));
				}
			}
		} catch (SVNException e) {
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);

			channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
					"Error", e.getMessage(),
					DisplaySimpleMessageClientCommand.ICON_ERROR));
		}
		return result;
	}

	// TODO delete following method only after all tests are over
	public List<String> getRevisionsForFilesInSelection(
			List<List<PathFragment>> selection) {
		ArrayList<String> result = new ArrayList<>();
		File[] filesToBeCompared = getFilesForSelectionList(selection);
		ISVNClientAdapter myClientAdapter;
		try {
			myClientAdapter = SVNProviderPlugin.getPlugin().getSVNClient();
			for (File f : filesToBeCompared) {
				try {
					ISVNStatus[] jhlStatusArray = myClientAdapter.getStatus(f,
							true, true);
					for (int i = 0; i < jhlStatusArray.length; i++) {
						result.add(jhlStatusArray[i].getRevision().toString());
					}
				} catch (SVNClientException e) {
				}
			}
		} catch (SVNException e1) {
		}
		return result;
	}

	/**
	 * @param kinds
	 *            svn statuses for files to be returned by method
	 * @see SVNStatusKind
	 */
	public ArrayList<File> getFilesWithGivenStatusesForPathFragmentFiles(
			ServiceInvocationContext context,
			List<List<PathFragment>> selection,
			SVNStatusKind... kinds) {
		CommunicationChannel channel = (CommunicationChannel) context
				.getCommunicationChannel();
		ArrayList<File> files = new ArrayList<File>();
		try {
			ISVNClientAdapter myClientAdapter = SVNProviderPlugin.getPlugin()
					.getSVNClient();
			for (List<PathFragment> path : selection) {
				File currentFile = ((Pair<File, String>) GenericTreeStatefulService
						.getNodeByPathFor(path, null)).a;
				ISVNStatus[] jhlStatusArray = myClientAdapter.getStatus(
						currentFile, false, true);
				Boolean ok = false;
				for (SVNStatusKind kind : kinds) {
					if (jhlStatusArray[0].getTextStatus().equals(kind)) {
						ok = true;
						break;
					}
				}
				if (!ok) {
					channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
							"Warning",
							SvnPlugin
									.getInstance()
									.getMessage(
											"svnService.addToVersion.operationNotPossibleNotification"),
							DisplaySimpleMessageClientCommand.ICON_ERROR));
					return null;
				}
				files.add(currentFile);
			}
		} catch (SVNException | SVNClientException e) {
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
					"Error", e.getMessage(),
					DisplaySimpleMessageClientCommand.ICON_ERROR));
		}
		return files;
	}

	public Boolean checkForUnversionedAndIgnoredFilesInSelection(
			ServiceInvocationContext context,
			List<List<PathFragment>> selection) {
		CommunicationChannel channel = (CommunicationChannel) context
				.getCommunicationChannel();
		ArrayList<File> files = getFilesWithGivenStatusesForPathFragmentFiles(
				context, selection, SVNStatusKind.UNVERSIONED,
				SVNStatusKind.IGNORED);
		if (files == null) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public Boolean addToSvnIgnore(ServiceInvocationContext context,
			List<List<PathFragment>> paths, String pattern) {
		CommunicationChannel channel = (CommunicationChannel) context
				.getCommunicationChannel();
		ISVNClientAdapter myClientAdapter;
		try {
			myClientAdapter = SVNProviderPlugin.getPlugin().getSVNClient();
			for (List<PathFragment> element : paths) {
				File file = ((Pair<File, String>) GenericTreeStatefulService
						.getNodeByPathFor(element, null)).a;
				if (file.isFile()) {
					file = file.getParentFile();
				}
				myClientAdapter.addToIgnoredPatterns(file, pattern);
			}
			return true;
		} catch (SVNException | SVNClientException e) {
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
					"Error", e.getMessage(),
					DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public Boolean addToVersion(ServiceInvocationContext context,
			List<List<PathFragment>> selection) {
		CommunicationChannel channel = (CommunicationChannel) context
				.getCommunicationChannel();
		try {
			ISVNClientAdapter myClientAdapter = SVNProviderPlugin.getPlugin()
					.getSVNClient();
			ArrayList<File> files = getFilesWithGivenStatusesForPathFragmentFiles(
					context, selection, SVNStatusKind.UNVERSIONED);
			if (files == null) {
				return false;
			}
			for (File f : files) {
				if (f.isDirectory()) {
					myClientAdapter.addDirectory(f, true, true);
				} else {
					myClientAdapter.addFile(f);
				}
			}
			return true;
		} catch (SVNException | SVNClientException e) {
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
					"Error", e.getMessage(),
					DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		}
	}

	public GetModifiedFilesDto getDifferences(ServiceInvocationContext context,
			List<List<PathFragment>> selectionList) {
		CommunicationChannel channel = (CommunicationChannel) context
				.getCommunicationChannel();
		ArrayList<FileDto> fileList = new ArrayList<FileDto>();
		File[] filesToBeCompared = getFilesForSelectionList(selectionList);
		for (File f : filesToBeCompared) {
			try {
				ISVNClientAdapter myClientAdapter = SVNProviderPlugin
						.getPlugin().getSVNClient();
				ArrayList<String> conflictSpecificFiles = new ArrayList<String>();
				ISVNStatus[] jhlStatusArray = myClientAdapter.getStatus(f,
						true, true);
				for (int i = 0; i < jhlStatusArray.length; i++) {
					if (!jhlStatusArray[i].getTextStatus().toString()
							.equals("normal")) {
						if (jhlStatusArray[i].getTextStatus().toString()
								.equals("conflicted")) {
							conflictSpecificFiles.add(jhlStatusArray[i]
									.getConflictNew().getAbsolutePath()
									.replaceAll("\\\\", "/"));
							conflictSpecificFiles.add(jhlStatusArray[i]
									.getConflictOld().getAbsolutePath()
									.replaceAll("\\\\", "/"));
							conflictSpecificFiles.add(jhlStatusArray[i]
									.getConflictWorking().getAbsolutePath()
									.replaceAll("\\\\", "/"));
						}
						Boolean continueSetter = false;
						if (jhlStatusArray[i].getTextStatus().toString()
								.equals("unversioned")) {
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
						ISVNStatus currentTarget = jhlStatusArray[i];

						String absolutePath = currentTarget.getFile()
								.getAbsolutePath();
						modifiedFileDto.setPathFromRoot(absolutePath
								.substring(workspaceLocation.length()));
						modifiedFileDto.setLabel(absolutePath
								.substring(absolutePath.lastIndexOf("\\")));

						String status = currentTarget.getTextStatus()
								.toString();

						modifiedFileDto.setStatus(status);
						modifiedFileDto
								.setImageUrls(setImageUrlForExtensionType(
										currentTarget.getNodeKind().toString(),
										status));
						fileList.add(modifiedFileDto);
					}
				}
			} catch (SVNClientException | SVNException e) {
				logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
				channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
						"Error", e.getMessage(),
						DisplaySimpleMessageClientCommand.ICON_ERROR));
			}
		}
		// sort needed manual implementation:
		Collections.sort(fileList, new Comparator<FileDto>() {
			@Override
			public int compare(FileDto dto1, FileDto dto2) {
				return dto1.getPathFromRoot().compareTo(dto2.getPathFromRoot());
			}
		});
		// duplicates are removed
		int currentIndex = 0;
		int resultSize = fileList.size();
		while (currentIndex < resultSize - 1) {

			if (fileList.get(currentIndex).getPathFromRoot()
					.equals(fileList.get(currentIndex + 1).getPathFromRoot())) {

				fileList.remove(currentIndex + 1);
				resultSize--;
			} else {
				currentIndex++;
			}
		}
		GetModifiedFilesDto dto = new GetModifiedFilesDto();
		dto.setFiles(fileList);
		return dto;
	}

	public Boolean commit(ServiceInvocationContext context,
			ArrayList<FileDto> selectionList, String message, Boolean keepLocks) {
		CommunicationChannel channel = (CommunicationChannel) context
				.getCommunicationChannel();
		ProgressMonitor pm = ProgressMonitor.create(SvnPlugin.getInstance()
				.getMessage("svn.service.commit.commitMonitor"), channel);
		SvnOperationNotifyListener opMng = new SvnOperationNotifyListener(
				CommunicationPlugin.tlCurrentChannel.get());
		// following 40 lines could be rewritten:
		// replacement in D:\data\commit alternate block.txt
		ArrayList<File> markedForAddition = new ArrayList<File>();
		ArrayList<File> markedForDeletion = new ArrayList<File>();
		ArrayList<File> normalCommitFiles = new ArrayList<File>();

		for (FileDto dto : selectionList) {
			File file = new File(workspaceLocation + dto.getPathFromRoot());
			switch (dto.getStatus()) {
			case "missing": // will be added to a queue ready to be marked for
							// deletion, after which it would be safe to have it
							// inside commit file list argument
				markedForDeletion.add(file);
				normalCommitFiles.add(file);
				break;
			case "modified":
				normalCommitFiles.add(file);
				break;
			case "unversioned":
				markedForAddition.add(file);
				normalCommitFiles.add(file);
				break;
			case "added":
				normalCommitFiles.add(file);
			}
		}
		// set File vectors for different types of operations:
		File[] markedForDeletionFiles = new File[markedForDeletion.size()];
		for (int i = 0; i < markedForDeletion.size(); i++) {
			markedForDeletionFiles[i] = markedForDeletion.get(i);
		}
		File[] markedForAdditionFiles = new File[markedForAddition.size()];
		for (int i = 0; i < markedForAddition.size(); i++) {
			markedForAdditionFiles[i] = markedForAddition.get(i);
		}
		File[] files = new File[normalCommitFiles.size()];
		for (int i = 0; i < normalCommitFiles.size(); i++) {
			files[i] = normalCommitFiles.get(i);
		}
		Boolean recurse = true;
		try {
			ISVNClientAdapter myClientAdapter = SVNProviderPlugin.getPlugin()
					.getSVNClient();

			pm.beginTask(null, 1000);
			myClientAdapter.setProgressListener(opMng);
			try {
				for (int i = 0; i < markedForAdditionFiles.length; i++) {
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
			channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
					"Error", e.getMessage(),
					DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		} finally {
			pm.done();
		}
		return true;
	}

	public File[] getFilesForSelectionList(
			List<List<PathFragment>> selectionList) {
		File[] result = new File[selectionList.size()];
		for (int i = 0; i < selectionList.size(); i++) {
			List<PathFragment> currentPathSelection = selectionList.get(i);
			String path = currentPathSelection.get(1).getName();
			path = ProjectsService.getInstance().getOrganizationDir(path)
					.getAbsolutePath();
			for (int j = 3; j < currentPathSelection.size(); j++) {
				path = path
						.concat("\\" + currentPathSelection.get(j).getName());
			}
			result[i] = new File(path);
		}
		return result;
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
	public void addComment(final String iuser, final String comment) {

		DatabaseOperationWrapper wrapper = new DatabaseOperationWrapper(
				new DatabaseOperation() {

					@Override
					public void run() {

						User user = wrapper.findByField(User.class, "login",
								iuser).get(0);

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
								user.getSvnComments().remove(
										previousComments.get(index));
								wrapper.merge(user);
								wrapper.delete(previousComments.get(index));
								previousComments.remove(index);
							}

							// add it again so that it will be seen as the last
							// used comment

							SVNCommentEntity newComment = EntityFactory.eINSTANCE
									.createSVNCommentEntity();

							newComment = (SVNCommentEntity) wrapper
									.merge(newComment);

							newComment.setUser(user);
							newComment.setBody(comment);
							newComment.setTimestamp(System.currentTimeMillis());
							previousComments.add(newComment);

							newComment = (SVNCommentEntity) wrapper
									.merge(newComment);
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

	public boolean deleteSvnAction(ServiceInvocationContext context, List<List<PathFragment>> objectFullPaths, String comment) {

		final CommunicationChannel cc = context.getCommunicationChannel();

		// list of remote resources
		List<ISVNRemoteResource> remoteObject = new ArrayList<ISVNRemoteResource>();

		// list of repository location
		final List<SVNRepositoryURLEntity> repositoryObject = new ArrayList<SVNRepositoryURLEntity>();

		// list of remote files
		final List<RemoteFile> remoteFile = new ArrayList<RemoteFile>();

		// map used for refresh in the end
		HashMap<Object, List<PathFragment>> refreshMap = new HashMap<Object, List<PathFragment>>();

		for (List<PathFragment> fullPath : objectFullPaths) {

			Object node = GenericTreeStatefulService.getNodeByPathFor(fullPath,
					null);

			// add in the list of repos
			if (node.getClass().equals(SVNRepositoryLocation.class)) {

				SVNRepositoryLocation repo = (SVNRepositoryLocation) node;

				List<PathFragment> parent = new ArrayList<PathFragment>();
				parent.addAll(fullPath);
				parent.remove(parent.size() - 1);
				Object remote = GenericTreeStatefulService.getNodeByPathFor(
						parent, null);
				refreshMap.put(remote, parent);

				String organizationName = fullPath.get(1).getName();
				Organization org = EntityFactory.eINSTANCE.createOrganization();
				org.setName(organizationName);

				SVNRepositoryURLEntity urlEntity = EntityFactory.eINSTANCE
						.createSVNRepositoryURLEntity();
				urlEntity.setName(repo.getLocation());
				urlEntity.setOrganization(org);

				repositoryObject.add(urlEntity);
			}

			// add in the list of remote resources
			if (node.getClass().equals(RemoteFolder.class)) {
				remoteObject.add((ISVNRemoteResource) node);

				List<PathFragment> parent = new ArrayList<PathFragment>();
				parent.addAll(fullPath);
				parent.remove(parent.size() - 1);
				Object remote = GenericTreeStatefulService.getNodeByPathFor(
						parent, null);
				refreshMap.put(remote, parent);
			}

			// add in the list of remote files
			if (node.getClass().equals(RemoteFile.class)) {
				remoteFile.add((RemoteFile) node);

				List<PathFragment> parent = new ArrayList<PathFragment>();
				parent.addAll(fullPath);
				parent.remove(parent.size() - 1);
				Object remote = GenericTreeStatefulService.getNodeByPathFor(
						parent, null);
				refreshMap.put(remote, parent);
			}
		}

		ProgressMonitor monitor = ProgressMonitor.create(SvnPlugin
				.getInstance().getMessage("svn.deleteSvnAction.monitor.title"),
				cc);
		
		tlCommand.set(context.getCommand());

		try {
			// save comment
			addComment(cc.getPrincipal().getUser().getLogin(), comment);

			// delete remote resource
			if (remoteObject.size() >= 1) {
				SvnPlugin
						.getInstance()
						.getUtils()
						.deleteRemoteResources(
								remoteObject.toArray(new ISVNRemoteResource[remoteObject
										.size()]), comment, monitor);
			}

			// delete repository action
			if (repositoryObject.size() >= 1) {
				// final List<SVNRepositoryURLEntity> repos;
				DatabaseOperationWrapper wrapper = new DatabaseOperationWrapper(
						new DatabaseOperation() {

							@Override
							public void run() {
								for (SVNRepositoryURLEntity url : repositoryObject) {
									SVNRepositoryURLEntity toDelete = wrapper
											.findByField(
													SVNRepositoryURLEntity.class,
													"name", url.getName()).get(
													0);
									if (toDelete != null) {
										Organization org = toDelete
												.getOrganization();
										org.getSvnRepositoryURLs().remove(
												toDelete);
										wrapper.merge(org);
										wrapper.delete(toDelete);
									}
								}
							}
						});
			}

			// delete remote file
			if (remoteFile.size() >= 1) {
				remoteObject.addAll(remoteFile);
				SvnPlugin
						.getInstance()
						.getUtils()
						.deleteRemoteResources(
								remoteObject.toArray(new ISVNRemoteResource[remoteObject
										.size()]), comment, monitor);
			}
			for (Object root : refreshMap.keySet()) {
				if (root != null && refreshMap.get(root) != null)
					((GenericTreeStatefulService) GenericTreeStatefulService
							.getServiceFromPathWithRoot(refreshMap.get(root)))
							.dispatchContentUpdate(root);
			}

		} catch (SVNException e) {
			if (isAuthentificationException(e))
				return true;
			e.printStackTrace();
			logger.error("Exception thrown while deleting remote folders!", e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(CommonPlugin
							.getInstance().getMessage("error"), SvnPlugin
							.getInstance().getMessage(
									"svn.deleteSvnAction.error"),
							DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		} finally {
			monitor.done();
		}
		return true;
	}

	/**
	 * 
	 * @author Cristina Necula
	 * 
	 */
	@RemoteInvocation
	public List<String> getCredentials(ServiceInvocationContext context,
			List<PathFragment> path) {

		SVNRepositoryLocation remoteNode = (SVNRepositoryLocation) GenericTreeStatefulService
				.getNodeByPathFor(path, null);
		if (remoteNode.getUrl() == null) {
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(CommonPlugin
							.getInstance().getMessage("error"),
							"Cannot find repository for node " + remoteNode,
							DisplaySimpleMessageClientCommand.ICON_ERROR));
			return null;
		}
		String repository = "<" + remoteNode.getUrl().getProtocol() + "://"
				+ remoteNode.getUrl().getHost() + ":"
				+ remoteNode.getUrl().getPort() + "> "
				+ remoteNode.getUrl().getLastPathSegment();
		if (((FlowerWebPrincipal) CommunicationPlugin.tlCurrentChannel.get()
				.getPrincipal()).getUserSvnRepositories() == null) {
			return null;
		} else {
			List<String> credentials = ((FlowerWebPrincipal) CommunicationPlugin.tlCurrentChannel
					.get().getPrincipal()).getUserSvnRepositories().get(
					repository);
			if (credentials != null) {
				credentials.add(0, repository);
				return credentials;
			} else
				return null;
		}
	}

	/**
	 * 
	 * @author Cristina Necula
	 * 
	 */
	@RemoteInvocation
	public boolean login(ServiceInvocationContext context, String uri,
			String username, String password,
			InvokeServiceMethodServerCommand command) {
		tlCommand.remove();
		try {
			changeCredentials(context, uri, username, password);
			command.setCommunicationChannel(context.getCommunicationChannel());

			command.executeCommand();
		} catch (Exception e) {
			logger.error("Exception thrown while logging user!", e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(CommonPlugin
							.getInstance().getMessage("error"),
							"Error while logging user!",
							DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		} finally {
			if (tlCommand != null) {
				tlCommand.remove();
			}
		}
		return true;
	}

	/**
	 * 
	 * @author Cristina Necula
	 * 
	 */
	@RemoteInvocation
	public void changeCredentials(ServiceInvocationContext context, String uri,
			String username, String password) {
		List<String> info = new ArrayList<String>();
		info.add(username);
		info.add(password);
		List<String> copyUri = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(uri, ":/<>");
		while (st.hasMoreTokens()) {
			copyUri.add(st.nextToken());
		}
		if (copyUri.size() == 3) {
			uri = "<" + copyUri.get(0) + "://" + copyUri.get(1) + ":3690> "
					+ copyUri.get(2);
		}
		try {
			FlowerWebPrincipal principal = (FlowerWebPrincipal) CommunicationPlugin.tlCurrentChannel
					.get().getPrincipal();
			principal.getUserSvnRepositories().put(uri, info);

		} catch (Exception e) {
			logger.error("Exception thrown while changing credentials!", e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(CommonPlugin
							.getInstance().getMessage("error"),
							"Error while changing credentials!",
							DisplaySimpleMessageClientCommand.ICON_ERROR));
		}
	}

	/**
	 * 
	 * @author Cristina Necula
	 * 
	 */

	public boolean isAuthentificationException(Throwable exception) {

		if (exception == null) {
			return false;
		}
		return SvnPlugin.getInstance().getUtils()
				.isAuthenticationClientException(exception);

	}

	/**
	 * 
	 * @author Cristina Necula
	 * 
	 */
	public boolean merge(ServiceInvocationContext context, String resourcePath,
			List<List<PathFragment>> selectionList, String url1,
			long revision1, String url2, long revision2, boolean force,
			boolean ignoreAncestry) throws SVNException {

		boolean checkForConflict = false;

		SvnOperationNotifyListener opMng = new SvnOperationNotifyListener(
				context.getCommunicationChannel());

		ProgressMonitor monitor = ProgressMonitor.create(SvnPlugin
				.getInstance().getMessage("svn.action.mergeAction.label"),
				context.getCommunicationChannel());
		try {
			monitor.beginTask(null, 100);

			SVNUrl svnUrl1 = new SVNUrl(url1);
			SVNUrl svnUrl2 = new SVNUrl(url2);

			ISVNClientAdapter client = SVNProviderPlugin.getPlugin()
					.getSVNClient();
			client.setProgressListener(opMng);
			opMng.beginOperation(monitor, client, true);
			monitor.subTask(resourcePath);

			File[] files = getFilesForSelectionList(selectionList);
			SVNRevision svnRevision1 = revision1 == -1 ? SVNRevision.HEAD
					: new SVNRevision.Number(revision1);
			SVNRevision svnRevision2 = revision2 == -1 ? SVNRevision.HEAD
					: new SVNRevision.Number(revision2);

			client.merge(svnUrl1, svnRevision1, svnUrl2, svnRevision2,
					files[0], force, recurse, false, ignoreAncestry);
			monitor.worked(100);
		} catch (SVNClientException e) {
			if (isAuthentificationException(e))
				return true;
			logger.error("Error during Merge operation!", e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(CommonPlugin
							.getInstance().getMessage("error"), e.getMessage(),
							DisplaySimpleMessageClientCommand.ICON_ERROR));
			checkForConflict = true;
			throw SVNException.wrapException(e);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		} finally {
			opMng.endOperation();
			monitor.done();
			if (checkForConflict)
				return false;
		}
		return true;
	}

	/**
	 * 
	 * @author Cristina Necula
	 * 
	 */
	public List<String> getMergeSpecs(ServiceInvocationContext context,
			List<List<PathFragment>> selectionList,

			List<PathFragment> path) {

		tlCommand.set(context.getCommand());

		List<String> specs = new ArrayList<String>();
		File[] files = getFilesForSelectionList(selectionList);
		String workingDirectoryPath = getDirectoryFullPathFromPathFragments(path);
		try {
			ISVNClientAdapter myClientAdapter;
			myClientAdapter = SVNProviderPlugin.getPlugin().getSVNClient();
			ISVNInfo info = myClientAdapter.getInfo(new File(
					workingDirectoryPath));
			specs.add(info.getUrlString());
			specs.add(files[0].getAbsolutePath());
		} catch (Exception e) {
			if (isAuthentificationException(e)) {
				specs.add("loginmessage");
			}
		}
		return specs;
	}

	public int getDepthValue(int depth) {
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
