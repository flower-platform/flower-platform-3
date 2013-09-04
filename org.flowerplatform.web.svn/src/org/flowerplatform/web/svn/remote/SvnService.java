package org.flowerplatform.web.svn.remote;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.command.AbstractClientCommand;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.progress_monitor.ProgressMonitor;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.web.WebPlugin;
import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;
import org.flowerplatform.web.entity.EntityFactory;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.SVNCommentEntity;
import org.flowerplatform.web.entity.SVNRepositoryURLEntity;
import org.flowerplatform.web.entity.User;
import org.flowerplatform.web.security.sandbox.FlowerWebPrincipal;
import org.flowerplatform.web.security.service.UserService;
import org.flowerplatform.web.svn.SvnPlugin;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tigris.subversion.subclipse.core.ISVNRemoteFolder;
import org.tigris.subversion.subclipse.core.ISVNRemoteResource;
import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.SVNProviderPlugin;
import org.tigris.subversion.subclipse.core.repo.SVNRepositoryLocation;
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
	
	
	public boolean createRemoteFolder(ServiceInvocationContext context,
			List<PathFragment> parentPath, String folderName, String comment) {

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
		} catch (SVNException e) { // something wrong happened
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
						ArrayList<Object> querryResult = (ArrayList<Object>) q.list();
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
						urlEntity.setOrganization((Organization) organization);
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
							CommonPlugin.getInstance().getMessage("error"), e);
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
		if (operationSuccessful.contains("success")) {
			Object node = GenericTreeStatefulService.getNodeByPathFor(
					parentPath, null);
			GenericTreeStatefulService.getServiceFromPathWithRoot(parentPath)
					.dispatchContentUpdate(node);
			return true;
		}
		return false;
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
	

	public void openLoginWindow(ServiceInvocationContext context, String credentials) {
		
//		InvokeServiceMethodServerCommand cmd = tlCommand.get();	
//		cmd.getParameters().remove(0);
		
		new OpenSvnCredentialsWindowClientCommand(credentials, context.getCommand());
	}
	
	@RemoteInvocation
	public List<String> getCredentials(ServiceInvocationContext context, List<PathFragment> path) {
		
			SVNRepositoryLocation remoteNode = (SVNRepositoryLocation) GenericTreeStatefulService.getNodeByPathFor(path, null);			
			String repository = remoteNode.getLocation();
			if (repository == null) {
				context.getCommunicationChannel().appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(
								CommonPlugin.getInstance().getMessage("error"), 
								"Cannot find repository for node " + remoteNode, 
								DisplaySimpleMessageClientCommand.ICON_ERROR));	
				return null;
			}			
			if (((FlowerWebPrincipal) CommunicationPlugin.tlCurrentPrincipal.get()).
					getUserSvnRepositories() == null){
				return null;
			}
			else {
				List<String> credentials = ((FlowerWebPrincipal) CommunicationPlugin.tlCurrentPrincipal.get()).
						getUserSvnRepositories().get(repository);
//				context.getCommunicationChannel().appendOrSendCommand(
//						(AbstractClientCommand)new OpenSvnCredentialsWindowClientCommand(remoteNode.getUrl().toString(),
//								credentials != null ? credentials.get(0) : ""));
				if(credentials != null){
					credentials.add(0, repository);
					return credentials;
				}
				else
					return null;
			}
	}
	
	@RemoteInvocation
	public void login(ServiceInvocationContext context, String uri, String username, String password) {				
		
		try {
			changeCredentials(context, uri, username, password);
			InvokeServiceMethodServerCommand command = context.getCommand();
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
		
		try {
			FlowerWebPrincipal principal = (FlowerWebPrincipal) CommunicationPlugin.tlCurrentPrincipal.get();
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

}
