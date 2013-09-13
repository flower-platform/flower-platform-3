package org.flowerplatform.web.svn.remote;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.flowerplatform.common.CommonPlugin;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.CommunicationPlugin;

import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.progress_monitor.ProgressMonitor;

import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;

import org.flowerplatform.communication.service.ServiceInvocationContext;

import org.flowerplatform.communication.tree.GenericTreeContext;

import org.flowerplatform.communication.stateful_service.InvokeStatefulServiceMethodServerCommand;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;

import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;

import org.flowerplatform.communication.tree.remote.TreeNode;

import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;
import org.flowerplatform.web.entity.EntityFactory;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.SVNCommentEntity;
import org.flowerplatform.web.entity.SVNRepositoryURLEntity;

import org.flowerplatform.web.entity.WorkingDirectory;
import org.flowerplatform.web.projects.remote.ProjectsService;

import org.flowerplatform.web.entity.User;
import org.flowerplatform.web.entity.WorkingDirectory;
import org.flowerplatform.web.projects.remote.ProjectsService;
import org.flowerplatform.web.security.sandbox.FlowerWebPrincipal;
import org.flowerplatform.web.security.service.UserService;


import org.flowerplatform.web.entity.User;
import org.flowerplatform.web.security.service.OrganizationService;
import org.flowerplatform.web.security.service.UserService;

import org.flowerplatform.web.svn.SvnPlugin;
import org.flowerplatform.web.svn.SvnUtils;
import org.flowerplatform.web.svn.operation.SvnOperationNotifyListener;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.tigris.subversion.subclipse.core.ISVNCoreConstants;

import org.tigris.subversion.subclipse.core.ISVNRemoteFolder;
import org.tigris.subversion.subclipse.core.ISVNRemoteResource;

import org.tigris.subversion.subclipse.core.ISVNRepositoryLocation;

import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.SVNProviderPlugin;

import org.tigris.subversion.subclipse.core.SVNTeamProvider;

import org.tigris.subversion.subclipse.core.repo.SVNRepositoryLocation;

import org.tigris.subversion.subclipse.core.resources.RemoteFolder;
import org.tigris.subversion.subclipse.core.resources.RemoteResource;

import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.SVNRevision;
import org.tigris.subversion.svnclientadapter.SVNUrl;

import org.tigris.subversion.subclipse.core.resources.RemoteFolder;
import org.tigris.subversion.subclipse.core.resources.RemoteResource;
import org.tigris.subversion.svnclientadapter.ISVNStatus;
import org.tigris.subversion.svnclientadapter.SVNClientException;

import org.tigris.subversion.svnclientadapter.utils.Depth;

import org.tigris.subversion.subclipse.core.resources.RemoteFolder;

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

	/**
	 * @flowerModelElementId _yaKVkAMcEeOrJqcAep-lCg
	 */

	public static SvnService getInstance() {
		return INSTANCE;
	}


	/**
	 * @author Gabriela Murgoci
	 */
	public boolean createRemoteFolder(ServiceInvocationContext context,
			List<PathFragment> parentPath, String folderName, String comment) {

		Object selectedParent = GenericTreeStatefulService.getNodeByPathFor(
				parentPath, null);

		GenericTreeStatefulService explorerService = (GenericTreeStatefulService) GenericTreeStatefulService
				.getServiceFromPathWithRoot(parentPath);

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
			parentFolder.createRemoteFolder(folderName, comment,
					new NullProgressMonitor());
		} catch (SVNException e) { // something wrong happened
			logger.debug(SvnPlugin.getInstance().getMessage("error", e));
			CommunicationChannel channel = (CommunicationChannel) context
					.getCommunicationChannel();
			channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
					CommonPlugin.getInstance().getMessage("error"), e
							.getMessage(),
					DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		}

		// tree refresh
		parentFolder.refresh();
		explorerService.dispatchContentUpdate(parentFolder);
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

		CommunicationChannel chan = (CommunicationChannel) context
				.getCommunicationChannel();

		GenericTreeContext treeContext = GenericTreeStatefulService
				.getServiceFromPathWithRoot(destinationPath).getTreeContext(
						chan, remoteResourceName);

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
			ISVNClientAdapter svnClient = remoteResource.getRepository()
					.getSVNClient();
			SVNUrl destUrl = parentFolder.getUrl().appendPath(
					remoteResourceName);

			svnClient.move(remoteResource.getUrl(), destUrl, comment,
					SVNRevision.HEAD);
		} catch (Exception e) {
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			if (e instanceof SVNException) {
				CommunicationChannel channel = (CommunicationChannel) context
						.getCommunicationChannel();
				channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
						CommonPlugin.getInstance().getMessage("error"), e
								.getMessage(),
						DisplaySimpleMessageClientCommand.ICON_ERROR));
			}
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

		Object selection2 = GenericTreeStatefulService
				.getNodeByPathFor(
						destinationPath.subList(0, destinationPath.size()),
						treeContext);

		ISVNRemoteFolder destinationNode = null;

		if (selection2 instanceof ISVNRemoteFolder) {
			destinationNode = (ISVNRemoteFolder) selection2;
		} else if (selection2 instanceof IAdaptable) {
			// ISVNRepositoryLocation is adaptable to ISVNRemoteFolder
			IAdaptable a = (IAdaptable) selection2;
			Object adapter = a.getAdapter(ISVNRemoteFolder.class);
			destinationNode = (ISVNRemoteFolder) adapter;
		}

		destinationNode.refresh();
		explorerService.dispatchContentUpdate(destinationNode);

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

		try {
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
				}

				else {
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
			destinationFolder = repository.getRemoteFolder(destinationURL
					.substring(
							destinationURL.lastIndexOf(repository
									.getRootFolder().getUrl().toString())
									+ repository.getRootFolder().getUrl()
											.toString().length(),
							destinationURL.lastIndexOf("/")));

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
				CommunicationChannel channel = (CommunicationChannel) context
						.getCommunicationChannel();
				channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
						CommonPlugin.getInstance().getMessage("error"), e
								.getMessage(),
						DisplaySimpleMessageClientCommand.ICON_ERROR));
				return false;
			}

		} catch (Exception e) {
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			CommunicationChannel channel = (CommunicationChannel) context
					.getCommunicationChannel();
			channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
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

			item.setPath(selectedResource);
			item.setName(selectedResource.get(selectedResource.size() - 1)
					.getName());
			item.setPartialPath(partialPath);
			item.setImage("images/folder_pending.gif");

			branchResources.add(item);
		}


		if (actionType) {
			try {
				remoteResource = GenericTreeStatefulService.getNodeByPathFor(
					(List<PathFragment>) selected.get(0), null);
				listOfFiles = new File[] {(File) ((Pair<?, ?>) remoteResource).a};
				ISVNStatus[] listOfStatuses = SVNProviderPlugin
					.getPlugin().getSVNClient()
					.getStatus(listOfFiles);
				aux = listOfStatuses[0].getUrl();
				repository = SVNProviderPlugin.getPlugin()
					.getRepository(aux.toString());
			} catch (Exception e1) {}
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
			CommunicationChannel channel = (CommunicationChannel) context
					.getCommunicationChannel();
			channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
					CommonPlugin.getInstance().getMessage("error"), e
							.getMessage(),
					DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		}

		return true;
	}

	public boolean createSvnRepository(final ServiceInvocationContext context,
			final String url, final List<PathFragment> parentPath) {
		// had to use List due to limitations of altering final variables inside
		// runnable
		final List<String> operationSuccessful = new ArrayList<String>();

		new DatabaseOperationWrapper(new DatabaseOperation() {

			@SuppressWarnings({ "unchecked" })
			@Override
			public void run() {
				String organizationName = parentPath.get(1).getName();
				try {
					// check to see if repository with specified url address
					// exists following 2 lines might be merged
					SVNRepositoryLocation repository = SVNRepositoryLocation
							.fromString(url);
					if (repository.pathExists()) {
						// creates entry in database and links it to specified
						// organization
						Query q = wrapper
								.getSession()
								.createQuery(
										String.format(
												"SELECT e from %s e where e.name ='%s'",
												Organization.class
														.getSimpleName(),
												organizationName));
						Object organization = q.list().get(0);
						SVNRepositoryURLEntity urlEntity = EntityFactory.eINSTANCE
								.createSVNRepositoryURLEntity();
						urlEntity.setName(url);
						urlEntity.setOrganization((Organization) organization);
						// wrapper.merge(urlEntity);
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
					logger.debug(SvnPlugin.getInstance().getMessage("error", e));
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

		// tree refresh
		Object node = GenericTreeStatefulService.getNodeByPathFor(parentPath,
				null);
		((GenericTreeStatefulService) GenericTreeStatefulService
				.getServiceFromPathWithRoot(parentPath))
				.dispatchContentUpdate(node);

		return true;
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

	public boolean checkout(ServiceInvocationContext context,
			ArrayList<ArrayList<PathFragment>> folders,
			List<PathFragment> workingDirectoryPartialPath,
			String workingDirectoryDestination, String revisionNumberAsString,
			int depthIndex, Boolean headRevision, boolean force,
			boolean ignoreExternals) {
		CommunicationChannel channel = (CommunicationChannel) context
				.getCommunicationChannel();
		ProgressMonitor pm = ProgressMonitor.create(SvnPlugin.getInstance()
				.getMessage("svn.service.checkout.checkoutProgressMonitor"),
				channel);

		workingDirectoryDestination = getWorkingDirectoryFullPath(workingDirectoryPartialPath)
				+ workingDirectoryDestination;
		SVNRevision revision;
		if (headRevision) {
			revision = SVNRevision.HEAD;
		} else {
			try {
				revision = SVNRevision.getRevision(revisionNumberAsString);
			} catch (ParseException e) {
				logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
				channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
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

		pm.beginTask(null, 1000);
		ISVNClientAdapter myClient;
		SvnOperationNotifyListener opMng = new SvnOperationNotifyListener(
				CommunicationPlugin.tlCurrentChannel.get());
		try {
			myClient = SVNProviderPlugin.getPlugin().getSVNClient();
			myClient.setProgressListener(opMng);
			opMng.beginOperation(pm, myClient, true);
			for (int i = 0; i < folders.size(); i++) {
				File fileArgumentForCheckout = new File(
						workingDirectoryDestination + "/"
								+ remoteFolders[i].getName());
				String fullPath = remoteFolders[i].getUrl().toString();
				myClient.checkout(new SVNUrl(fullPath),
						fileArgumentForCheckout, revision,
						getDepthValue(depthIndex), ignoreExternals, force);

				try {
					ProjectsService.getInstance()
							.createOrImportProjectFromFile(context,
									fileArgumentForCheckout);
				} catch (URISyntaxException e) {
					logger.debug(
							CommonPlugin.getInstance().getMessage("error"), e);
					channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
							"Error", e.getMessage(),
							DisplaySimpleMessageClientCommand.ICON_ERROR));
				} catch (CoreException e) {
					logger.debug(
							CommonPlugin.getInstance().getMessage("error"), e);
					channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
							"Error", e.getMessage(),
							DisplaySimpleMessageClientCommand.ICON_ERROR));
				}
			}
			opMng.endOperation();
		} catch (SVNException | SVNClientException | MalformedURLException e) {
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

	public boolean workingDirectoryExists(ServiceInvocationContext context,
			String wdName, String organizationName) {
		ArrayList<String> existingWDs = getWorkingDirectoriesForOrganization(
				context, organizationName);
		for (String wd : existingWDs) {
			if (wd.equals(wdName))
				return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public String getWorkingDirectoryFullPath(List<PathFragment> pathWithRoot) {
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

	public Boolean createFolderAndMarkAsWorkingDirectory(
			ServiceInvocationContext context, String path, TreeNode organization) {
		File targetFile = new File(ProjectsService.getInstance()
				.getOrganizationDir(organization.getLabel()).getAbsolutePath()
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
			ArrayList<ArrayList<PathFragment>> selectionList) {
		return updateToVersion(context, selectionList, "head", Depth.infinity,
				false, false, true);
	}

	public Boolean updateToVersion(ServiceInvocationContext context,
			ArrayList<ArrayList<PathFragment>> selectionList, String revision,
			int depth, Boolean changeWorkingCopyToSpecifiedDepth,
			Boolean ignoreExternals, Boolean allowUnversionedObstructions) {
		File[] fileMethodArgument = new File[selectionList.size()];
		for (int i = 0; i < selectionList.size(); i++) {
			ArrayList<PathFragment> currentPathSelection = selectionList.get(i);
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
					return true;
				}
				return false;
			}
			pm.done();
		}
		return true;
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
	
	public boolean deleteSvnAction(final ServiceInvocationContext context, List<List<PathFragment>> objectFullPaths, String comment){
		
		final CommunicationChannel cc = context.getCommunicationChannel();
		
		//list of remote resources
		List<ISVNRemoteResource> remoteObject = new ArrayList<ISVNRemoteResource>();
		
		//list of repository location
		final List<SVNRepositoryURLEntity> repositoryObject = new ArrayList<SVNRepositoryURLEntity>();
		context.getCommand().getParameters().remove(0);
		tlCommand.set(context.getCommand());
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
			if (isAuthentificationException(e))
				return true;
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
	

//	public void openLoginWindow(ServiceInvocationContext context, String credentials) {
//		
//		InvokeServiceMethodServerCommand cmd = tlCommand.get();	
//		cmd.getParameters().remove(0);
//		new OpenSvnCredentialsWindowClientCommand(credentials, context.getCommand());
//	}
	
	@RemoteInvocation
	public List<String> getCredentials(ServiceInvocationContext context, List<PathFragment> path) {
		
			SVNRepositoryLocation remoteNode = (SVNRepositoryLocation) GenericTreeStatefulService.getNodeByPathFor(path, null);			
			if (remoteNode.getUrl() == null) {
				context.getCommunicationChannel().appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(
								CommonPlugin.getInstance().getMessage("error"), 
								"Cannot find repository for node " + remoteNode, 
								DisplaySimpleMessageClientCommand.ICON_ERROR));	
				return null;
			}
			
			String repository = "<" + remoteNode.getUrl().getProtocol() + "://" + 
									remoteNode.getUrl().getHost() + ":" + remoteNode.getUrl().getPort() +
									"> " + remoteNode.getUrl().getLastPathSegment();			
			if (((FlowerWebPrincipal) CommunicationPlugin.tlCurrentChannel.get().getPrincipal()).
					getUserSvnRepositories() == null){
				return null;
			}
			else {
				List<String> credentials = ((FlowerWebPrincipal) CommunicationPlugin.tlCurrentChannel.get().getPrincipal()).
						getUserSvnRepositories().get(repository);
				if(credentials != null){
					credentials.add(0, repository);
					return credentials;
				}
				else
					return null;
			}
	}
	
	@RemoteInvocation
	public void login(ServiceInvocationContext context, String uri, String username, String password, InvokeServiceMethodServerCommand command) {				
		
//		InvokeServiceMethodServerCommand command = tlCommand.get();
		tlCommand.remove();
		try {
			changeCredentials(context, uri, username, password);
			command.setCommunicationChannel(context.getCommunicationChannel());
			command.executeCommand();
		
		} catch (Exception e) {
			logger.error("Exception thrown while logging user!", e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							"Error while logging user!", 
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
		}
	}
	
	@RemoteInvocation
	public void changeCredentials(ServiceInvocationContext context, String uri, String username, String password) {				
		List<String> info = new ArrayList<String>();
		info.add(username);
		info.add(password);
		List<String> copyUri = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(uri, ":/<>"); 
		while(st.hasMoreTokens()) {
			copyUri.add(st.nextToken());
		}
		if (copyUri.size() == 3) {
			uri = "<" + copyUri.get(0) + "://" + copyUri.get(1) + ":3690> " + copyUri.get(2);
		}
		try {
			FlowerWebPrincipal principal = (FlowerWebPrincipal) CommunicationPlugin.tlCurrentChannel.get().getPrincipal();
			principal.getUserSvnRepositories().put(uri, info);
			
		} catch (Exception e) {
			logger.error("Exception thrown while changing credentials!", e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							"Error while changing credentials!", 
							DisplaySimpleMessageClientCommand.ICON_ERROR));
		}
	}
	
	public boolean isAuthentificationException(Throwable exception) {
		
		if (exception == null) {
			return false;
		}
		return SvnPlugin.getInstance().getUtils().isAuthenticationClientException(exception);
	}



	

}
