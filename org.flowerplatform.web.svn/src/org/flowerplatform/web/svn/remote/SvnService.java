package org.flowerplatform.web.svn.remote;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.SVNRepositoryURLEntity;
import org.flowerplatform.web.temp.GeneralService;
import org.hibernate.Query;
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
	/**
	 * @flowerModelElementId _yaKVkAMcEeOrJqcAep-lCg
	 */

	public boolean createRemoteFolder(ServiceInvocationContext context, List<PathFragment> parentPath, String folderName, String comment) {

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

			// create remote folder

			parentFolder.createRemoteFolder(folderName, comment,
					new NullProgressMonitor());

		} catch (SVNException e) {

			CommunicationChannel channel = (CommunicationChannel) context
					.getCommunicationChannel();

			channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
					"Error", e.getMessage(),
					DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		}

		return true;

	}

	
	public boolean createSvnRepository(final ServiceInvocationContext context, final String url, final List<PathFragment> parentPath) {
		
		DatabaseOperationWrapper wrapper = new DatabaseOperationWrapper(new DatabaseOperation() {
			@Override
			public void run() {				
				String organizationName = parentPath.get(1).getName();				
				try {
					// check to see if repository with specified url address exists
					// following 2 lines might be merged
					SVNRepositoryLocation repository = SVNRepositoryLocation.fromString(url);
					if(repository.pathExists()) {						
						// creates entry in database and links it to specified organization
						Query q = wrapper.getSession().createQuery(String.format("SELECT e from Organization e where e.name ='%s'", organizationName));
						Object organization = q.list().get(0);	
						GeneralService service = new GeneralService();
						service.createSVNRepositoryURLAndAddToOrg(url, (Organization)organization );
						// TODO tree refresh (is implemented in original SVN as automatically closing then opening the specific part of the tree)
					}
					else {						
						CommunicationChannel channel = (CommunicationChannel) context
								.getCommunicationChannel();
						channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
								"Error", "Url does not point to an existing Svn repository",
								DisplaySimpleMessageClientCommand.ICON_ERROR));							
					}
				} catch (SVNException e) {
					System.out.println(e.toString());
					CommunicationChannel channel = (CommunicationChannel) context
							.getCommunicationChannel();
					channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(
							"Error", e.getMessage(),
							DisplaySimpleMessageClientCommand.ICON_ERROR));							
				}	
			}
		});	
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getRepositoriesForOrganization(final String organizationName) {
			
			DatabaseOperationWrapper wrapper = new DatabaseOperationWrapper(new DatabaseOperation() {
				@Override
				public void run() {
					ArrayList<String> result = new ArrayList<String>();				
					String myQuerry = String.format("SELECT e from SVNRepositoryURLEntity e where e.organization.name ='%s'", organizationName);
					Query q = wrapper.getSession().createQuery(myQuerry);
					List<SVNRepositoryURLEntity> urlEntityList = q.list();				
					for(SVNRepositoryURLEntity urlEntity : urlEntityList)
							result.add(urlEntity.getName());
						wrapper.setOperationResult(result);	
				}
			});		
			return (ArrayList<String>) wrapper.getOperationResult();
		}
	
}