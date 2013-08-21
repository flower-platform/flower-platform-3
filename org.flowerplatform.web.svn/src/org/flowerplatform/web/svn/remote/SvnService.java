package org.flowerplatform.web.svn.remote;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;

import org.flowerplatform.common.util.Pair;

import org.flowerplatform.common.CommonPlugin;

import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
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
import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.repo.SVNRepositoryLocation;

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

}
