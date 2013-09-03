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
import org.tigris.subversion.subclipse.core.ISVNRepositoryLocation;
import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.SVNProviderPlugin;
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

	/**
	 * 
	 * @author Gabriela Murgoci
	 */
	@SuppressWarnings("unchecked")
	public boolean branchTagResources(ServiceInvocationContext context,
			boolean resourceSelected, List<BranchResource> branchResources,
			String destinationURL, String comment, Number revision,
			boolean createMissingFolders, boolean preserveFolderStructure) {
		
		boolean createOnServer = true;
		boolean makeParents = true;
	
		try {			
			List<Object> remoteResources = new ArrayList<Object>();
			
			for (BranchResource item : branchResources) {
					remoteResources.add(GenericTreeStatefulService.getNodeByPathFor((List<PathFragment>)item.getPath(), null));
			}
			
			SVNUrl[] sourceUrls = null;
			
			if (remoteResources.size() > 1) {
				ArrayList<SVNUrl> urlArray = new ArrayList<SVNUrl>();
				
					for (int i = 0; i < remoteResources.size(); i++) {
						urlArray.add(((ISVNRemoteResource) remoteResources.get(i)).getUrl());
					}
				
				sourceUrls = new SVNUrl[urlArray.size()];
				urlArray.toArray(sourceUrls);
			} else {
					sourceUrls = new SVNUrl[1];
					sourceUrls[0] = ((ISVNRemoteResource) remoteResources.get(0)).getUrl();
			}				
							
			ISVNClientAdapter client = null;
			ISVNRepositoryLocation repository = SVNProviderPlugin.getPlugin().getRepository(sourceUrls[0].toString());
			if (repository != null)
				client = repository.getSVNClient();
			if (client == null)
				client = SVNProviderPlugin.getPlugin().getSVNClientManager().getSVNClient();

			try {
	          
	            if (createOnServer) {
	            	boolean copyAsChild = sourceUrls.length > 1;
	            	String commonRoot = null;
	            	if (copyAsChild) {
	            		commonRoot = getCommonRoot(sourceUrls);
	            	}
	            	if (!copyAsChild || destinationURL.toString().startsWith(commonRoot)) {
	            		System.out.println(sourceUrls.toString());
	            		client.copy(sourceUrls, new SVNUrl(destinationURL), comment, SVNRevision.HEAD, copyAsChild, makeParents);
	            		
	            	} else {
	            		for (int i = 0; i < sourceUrls.length; i++) {
	            			String fromUrl = sourceUrls[i].toString();
	            			String uncommonPortion = fromUrl.substring(commonRoot.length());
	            			String toUrl = destinationURL.toString() + uncommonPortion;
	            			SVNUrl destination = new SVNUrl(toUrl);
	            			SVNUrl[] source = { sourceUrls[i] };
	            			client.copy(source, destination, comment, SVNRevision.HEAD, copyAsChild, makeParents);
	            		}
	            	}
	            } 
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
			
		}	catch (Exception e) {
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
		// tree refresh
		 
		 List<PathFragment> partialPath = (List<PathFragment>)branchResources.get(0).getPath();
		 partialPath.remove(partialPath.size() - 1);
		
		 Object selection = GenericTreeStatefulService.getNodeByPathFor(partialPath, null);
		 
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

			return true;
	}
	
	private String getCommonRoot(SVNUrl[] sourceUrls) {
		String commonRoot = null;
		String urlString = sourceUrls[0].toString();
    	tag1:
        	for (int i = 0; i < urlString.length(); i++) {
        		String partialPath = urlString.substring(0, i+1);
        		if (partialPath.endsWith("/")) {
    	    		for (int j = 1; j < sourceUrls.length; j++) {
    	    			if (!sourceUrls[j].toString().startsWith(partialPath)) break tag1;
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
	 */
	public ArrayList<Object> populateBranchResourcesList (ServiceInvocationContext context, List<List<PathFragment>> selected) {

		ArrayList<Object> branchResources = new ArrayList<Object>();
		
		List<PathFragment> commonParent = selected.get(0);
		
		commonParent = getCommonParent(selected);
		
		for (List<PathFragment> selectedResource : selected) {
			BranchResource item = new BranchResource();
			String partialPath = "";
			List<PathFragment> partialPathList = selectedResource.subList(commonParent.size(), selectedResource.size());
			
			for (int i = 0; i < partialPathList.size(); i++) {
				partialPath += partialPathList.get(i).getName();
				if (i < partialPathList.size())
					partialPath += "/";
			}
			
			item.setPath(selectedResource);
			item.setName(selectedResource.get(selectedResource.size() - 1).getName());
			item.setPartialPath(partialPath);
			item.setImage("images/svn_persp.gif");
			
			branchResources.add(item);
		}
		
		branchResources.add(0, commonParent);
		return branchResources;
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
