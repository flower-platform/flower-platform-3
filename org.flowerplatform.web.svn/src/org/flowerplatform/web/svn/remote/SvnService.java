package org.flowerplatform.web.svn.remote;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;
import org.flowerplatform.web.entity.EntityFactory;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.SVNRepositoryURLEntity;
import org.flowerplatform.web.svn.SvnPlugin;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tigris.subversion.subclipse.core.ISVNRemoteFolder;
import org.tigris.subversion.subclipse.core.ISVNRemoteResource;
import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.repo.SVNRepositoryLocation;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
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
	 * 
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

	public boolean createSvnRepository(final ServiceInvocationContext context,
			final String url, final List<PathFragment> parentPath) {
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

}
