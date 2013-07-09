package org.flowerplatform.web.git;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.lib.RepositoryCache.FileKey;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.storage.file.FileRepository;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.TagOpt;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.util.FS;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.progress_monitor.ProgressMonitor;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.InvokeStatefulServiceMethodServerCommand;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.communication.tree.NodeInfo;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.web.git.explorer.entity.RefNode;
import org.flowerplatform.web.git.explorer.entity.RemoteNode;
import org.flowerplatform.web.git.operation.CheckoutOperation;
import org.flowerplatform.web.git.operation.CommitOperation;
import org.flowerplatform.web.git.operation.MergeOperation;
import org.flowerplatform.web.git.operation.PullOperation;
import org.flowerplatform.web.git.operation.RebaseOperation;
import org.flowerplatform.web.git.operation.ResetOperation;
import org.flowerplatform.web.git.remote.OpenGitCredentialsWindowClientCommand;
import org.flowerplatform.web.git.remote.dto.CommitPageDto;
import org.flowerplatform.web.git.remote.dto.CommitResourceDto;
import org.flowerplatform.web.git.remote.dto.ConfigBranchPageDto;
import org.flowerplatform.web.git.remote.dto.ConfigFetchPushPageDto;
import org.flowerplatform.web.git.remote.dto.GitActionDto;
import org.flowerplatform.web.git.remote.dto.GitRef;
import org.flowerplatform.web.git.remote.dto.ImportProjectPageDto;
import org.flowerplatform.web.git.remote.dto.ProjectDto;
import org.flowerplatform.web.git.remote.dto.RemoteConfig;
import org.flowerplatform.web.projects.remote.ProjectsService;
import org.flowerplatform.web.security.sandbox.FlowerWebPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristina Constantinescu
 */
public class GitService {

	private static Logger logger = LoggerFactory.getLogger(GitService.class);
	
	private static final String SERVICE_ID = "gitService";
		
	/**
	 * Keeps the {@link InvokeStatefulServiceMethodServerCommand command} that needs authentication data before
	 * in order to be executed.
	 * 
	 * <p>
	 * The GIT operations that need authentications are clone/fetch/push.
	 * 
	 * @see #openLoginWindow()	
	 */
	public static final ThreadLocal<InvokeServiceMethodServerCommand> tlCommand = new ThreadLocal<InvokeServiceMethodServerCommand>();
	
	/**
	 * Keeps the repository URI for GIT authentication.
	 * 
	 * @see GitUsernamePasswordCredentialsProvider#get()
	 * @see #openLoginWindow()
	 */
	public static final ThreadLocal<String> tlURI = new ThreadLocal<String>();
	
	public static GitService getInstance() {		
		return (GitService) CommunicationPlugin.getInstance().getServiceRegistry().getService(SERVICE_ID);
	}
	
	private void dispatchContentUpdate(Object node) {
		for (GenericTreeStatefulService service : GitPlugin.getInstance().getTreeStatefulServicesDisplayingGitContent()) {
			service.dispatchContentUpdate(node);
		}
	}
			
	private void dispatchLabelUpdate(Object node, String nodeType) {
		for (GenericTreeStatefulService service : GitPlugin.getInstance().getTreeStatefulServicesDisplayingGitContent()) {
			service.dispatchLabelUpdate(node, nodeType);
		}
	}
	
	public Repository getRepository(NodeInfo nodeInfo) {
		if (nodeInfo == null) {
			return null;
		}
		if (nodeInfo.getNode() instanceof File && GitUtils.GIT_REPOSITORIES_NAME.equals(((File) nodeInfo.getNode()).getName())) {
			return null;
		}
		if (nodeInfo.getNode() instanceof Repository) {
			return (Repository) nodeInfo.getNode();
		}
		return getRepository(nodeInfo.getParent());		
	}
	
	/**
	 * Sends command to client to open the login window 
	 * based on the info stored in {@link #tlCommand}.
	 */
	public void openLoginWindow() {
		InvokeServiceMethodServerCommand cmd = tlCommand.get();	
		cmd.getParameters().remove(0);
		
		cmd.getCommunicationChannel().appendOrSendCommand(
				new OpenGitCredentialsWindowClientCommand(	tlURI.get().toString(), cmd));
	}
	
	@RemoteInvocation
	public List<Object> getBranches(ServiceInvocationContext context, String repositoryUrl) {
		tlCommand.set((InvokeServiceMethodServerCommand) context.getCommand());		
		Repository db = null;
		try {			
			URIish uri = new URIish(repositoryUrl.trim());
			db = new FileRepository(new File("/tmp"));		
			
			Git git = new Git(db);
			LsRemoteCommand rc = git.lsRemote();
			rc.setRemote(uri.toString()).setTimeout(30);
			
			Collection<Ref> remoteRefs = rc.call();
			List<GitRef> branches = new ArrayList<GitRef>();

			Ref idHEAD = null;
			for (Ref r: remoteRefs) {
				if (r.getName().equals(Constants.HEAD)) {
					idHEAD = r;
				}
			}			
			Ref head = null;
			boolean headIsMaster = false;
			String masterBranchRef = Constants.R_HEADS + Constants.MASTER;
			for (Ref r : remoteRefs) {
				String n = r.getName();
				if (!n.startsWith(Constants.R_HEADS))
					continue;
				branches.add(new GitRef(n, Repository.shortenRefName(n)));
				if (idHEAD == null || headIsMaster)
					continue;
				if (r.getObjectId().equals(idHEAD.getObjectId())) {
					headIsMaster = masterBranchRef.equals(r.getName());
					if (head == null || headIsMaster)
						head = r;
				}
			}
			Collections.sort(branches, new Comparator<GitRef>() {
				public int compare(GitRef r1, GitRef r2) {
					return r1.getShortName().compareTo(r2.getShortName());
				}
			});
			if (idHEAD != null && head == null) {
				head = idHEAD;
				branches.add(0, new GitRef(idHEAD.getName(), Repository.shortenRefName(idHEAD.getName())));
			}
			
			GitRef headRef = head != null ? new GitRef(head.getName(), Repository.shortenRefName(head.getName())) : null;			
			return Arrays.asList(new Object[] {branches, headRef});			
		} catch (JGitInternalException | GitAPIException e) {			
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							GitPlugin.getInstance().getMessage("git.cloneWizard.branch.cannotListBranches") + "\n" + e.getCause().getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));			
		} catch (IOException e) {
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							GitPlugin.getInstance().getMessage("git.cloneWizard.branch.cannotCreateTempRepo"), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));					
		} catch (URISyntaxException e) {
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getReason(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));
		} catch (Exception e) {
			if (GitPlugin.getInstance().getUtils().isAuthentificationException(e)) {
				openLoginWindow();
				return null;
			}
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return null;
	}
	
	/**
	 * Verifies if repository URL is valid.
	 * @return
	 * <ul>
	 * 	<li>  1  -> repository already exists
	 * 	<li> -1 -> repository not OK
	 *  <li>  0  -> repository OK
	 * </ul>
	 */
	@RemoteInvocation
	public Object validateRepositoryURL(ServiceInvocationContext context, List<PathFragment> selectedPath, String url) {
		try {
			@SuppressWarnings("unchecked")
			Pair<File, Object> node = (Pair<File, Object>) GenericTreeStatefulService.getNodeByPathFor(selectedPath, null);
			String repoName = new URIish(url.trim()).getHumanishName();
			
			File gitReposFile = GitPlugin.getInstance().getUtils().getGitRepositoriesFile(node.a);
			
			if (new File(gitReposFile, repoName).exists()) {
				return 1;
			}
		} catch (URISyntaxException e) {
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getReason(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));
			return -1;
		}
		return 0;
	}

	@RemoteInvocation
	public boolean cloneRepository(final ServiceInvocationContext context, List<PathFragment> selectedPath, 
			String repositoryUrl, final List<String> selectedBranches, final String remoteName, final boolean cloneAllBranches) {		
		tlCommand.set((InvokeServiceMethodServerCommand) context.getCommand());
		
		final URIish uri;
		try {
			uri = new URIish(repositoryUrl.trim());
		} catch (URISyntaxException e) {
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getReason(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));		
			return false;
		}		
		
		@SuppressWarnings("unchecked")
		final	Pair<File, Object> node = (Pair<File, Object>) GenericTreeStatefulService.getNodeByPathFor(selectedPath, null);
		
		File gitReposFile = GitPlugin.getInstance().getUtils().getGitRepositoriesFile(node.a);
		final File mainRepo = GitPlugin.getInstance().getUtils().getMainRepositoryFile(new File(gitReposFile, uri.getHumanishName()), true);
		
		final ProgressMonitor monitor =ProgressMonitor.create(
				GitPlugin.getInstance().getMessage("git.clone.monitor.title", uri), context.getCommunicationChannel());		
		monitor.beginTask(GitPlugin.getInstance().getMessage("git.clone.monitor.title", uri), 2);
		
		Job job = new Job(MessageFormat.format(GitPlugin.getInstance().getMessage("git.clone.monitor.title", uri), uri))	{
			@Override
			protected IStatus run(IProgressMonitor m) {														
				Repository repository = null;
				try {			
					CloneCommand cloneRepository = Git.cloneRepository();
					
					cloneRepository.setNoCheckout(true);					
					cloneRepository.setDirectory(mainRepo);
					cloneRepository.setProgressMonitor(new GitProgressMonitor(new SubProgressMonitor(monitor, 1)));
					cloneRepository.setRemote(remoteName);
					cloneRepository.setURI(uri.toString());
					cloneRepository.setTimeout(30);
					cloneRepository.setCloneAllBranches(cloneAllBranches);
					cloneRepository.setCloneSubmodules(false);
					if (selectedBranches.size() > 0) {		
						cloneRepository.setBranchesToClone(selectedBranches);
					}
					
					Git git = cloneRepository.call();
					repository = git.getRepository();
					
					// notify clients about changes
					dispatchContentUpdate(node);
					
					monitor.worked(1);	
				} catch (Exception e) {			
					if (repository != null)
						repository.close();
					GitPlugin.getInstance().getUtils().delete(mainRepo.getParentFile());
					
					if (monitor.isCanceled()) {
						return Status.OK_STATUS;
					}
					if (GitPlugin.getInstance().getUtils().isAuthentificationException(e)) {
						openLoginWindow();
						return Status.OK_STATUS;
					}
					logger.debug(GitPlugin.getInstance().getMessage("git.cloneWizard.error", new Object[] {mainRepo.getName()}), e);
					context.getCommunicationChannel().appendOrSendCommand(
							new DisplaySimpleMessageClientCommand(
									CommonPlugin.getInstance().getMessage("error"), 
									GitPlugin.getInstance().getMessage("git.cloneWizard.error", new Object[] {mainRepo.getName()}),							
									DisplaySimpleMessageClientCommand.ICON_ERROR));	
					
					return Status.CANCEL_STATUS;
				} finally {
					monitor.done();					
					if (repository != null) {
						repository.close();
					}
				}
				return Status.OK_STATUS;
			}
		};		
		job.schedule();
		return true;
	}
	
	public void deleteRepository(ServiceInvocationContext context, List<PathFragment> selectedNode) {
		Repository repository = (Repository) GenericTreeStatefulService.getNodeByPathFor(selectedNode, null);			
		GenericTreeStatefulService service = GenericTreeStatefulService.getServiceFromPathWithRoot(selectedNode);
		NodeInfo repositoryNodeInfo = service.getVisibleNodes().get(repository);
		
		ProgressMonitor monitor = ProgressMonitor.create(
				GitPlugin.getInstance().getMessage("git.deleteRepo.monitor.title"), context.getCommunicationChannel());
		
		try {						
			monitor.beginTask(GitPlugin.getInstance().getMessage("git.deleteRepo.monitor.message", 
					new Object[] {GitPlugin.getInstance().getUtils().getRepositoryName(repository)}), 2);
			
			repository.getObjectDatabase().close();
			repository.getRefDatabase().close();	
			
			// delete repositories from cache
			File[] children = repository.getDirectory().getParentFile().getParentFile().listFiles();	
			for (File child : children) {		
				Repository repo = GitPlugin.getInstance().getUtils().getRepository(child);
				RepositoryCache.close(repo);
			}		
			monitor.worked(1);		
			
			// delete repository files
			File repoFile = repository.getDirectory().getParentFile().getParentFile();
			if (GitUtils.GIT_REPOSITORIES_NAME.equals(repoFile.getParentFile().getName())) {
				GitPlugin.getInstance().getUtils().delete(repoFile);
			}
			monitor.worked(1);		
			
			dispatchContentUpdate(repositoryNodeInfo.getParent().getNode());
		} catch (Exception e) {
			logger.debug(GitPlugin.getInstance().getMessage("git.deleteRepo.error"), e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							GitPlugin.getInstance().getMessage("git.deleteRepo.error"), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
		} finally {
			monitor.done();
		}
	}
		
	@RemoteInvocation
	public GitActionDto getNodeAdditionalData(ServiceInvocationContext context, List<PathFragment> path) {
		try {
			RefNode refNode = (RefNode) GenericTreeStatefulService.getNodeByPathFor(path, null);			
			GenericTreeStatefulService service = GenericTreeStatefulService.getServiceFromPathWithRoot(path);
			NodeInfo refNodeInfo = service.getVisibleNodes().get(refNode);
			Repository repository = getRepository(refNodeInfo);
			if (repository == null) {
				context.getCommunicationChannel().appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(
								CommonPlugin.getInstance().getMessage("error"), 
								"Cannot find repository for node " + refNode, 
								DisplaySimpleMessageClientCommand.ICON_ERROR));	
				return null;
			}
			GitActionDto data = new GitActionDto();		
			data.setRepository(repository.getDirectory().getAbsolutePath());		
			data.setBranch(repository.getBranch());		
			
			return data;			
		} catch (Exception e) {	
			logger.debug(CommonPlugin.getInstance().getMessage("error"), path, e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
		}
		return null;
	}
	
	public boolean merge(ServiceInvocationContext context, String repositoryLocation, String refName, boolean squash) {		
		try {
			Repository repo = RepositoryCache.open(FileKey.exact(new File(repositoryLocation), FS.DETECTED));	
			
			MergeOperation op = new MergeOperation(repo, refName, squash, context.getCommunicationChannel());
			op.execute();
			
			String result = GitPlugin.getInstance().getUtils().handleMergeResult(op.getMergeResult());
			if (result != null) {
				context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							GitPlugin.getInstance().getMessage("git.merge.result"), 
							result, 
							DisplaySimpleMessageClientCommand.ICON_INFORMATION));
			}
			return true;
		} catch (Exception e) {
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		}
	}
	
	public boolean rebase(ServiceInvocationContext context, String repositoryLocation, String refName) {		
		try {
			Repository repo = RepositoryCache.open(FileKey.exact(new File(repositoryLocation), FS.DETECTED));	
						
			if (!repo.getFullBranch().startsWith(Constants.R_HEADS)) {
				context.getCommunicationChannel().appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(
								CommonPlugin.getInstance().getMessage("error"), 
								GitPlugin.getInstance().getMessage("git.rebase.noLocalBranch"), 
								DisplaySimpleMessageClientCommand.ICON_ERROR));
				return false;
			}
			RebaseOperation op = new RebaseOperation(repo, refName, context.getCommunicationChannel());
			op.execute();
			
			String result = op.handleRebaseResult();
			if (result != null) {
				context.getCommunicationChannel().appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(
								GitPlugin.getInstance().getMessage("git.rebase.result"), 
								result,
								DisplaySimpleMessageClientCommand.ICON_INFORMATION));
			}
			return true;		
		} catch (Exception e) {
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		}
	}
	
	@RemoteInvocation
	public boolean reset(ServiceInvocationContext context, String repositoryLocation, String targetName, int resetType) {		
		try {
			Repository repo = RepositoryCache.open(FileKey.exact(new File(repositoryLocation), FS.DETECTED));	
			
			ResetType type;
			switch (resetType) {
				case 0:
					type = ResetType.SOFT;
					break;
				case 1:
					type = ResetType.MIXED;
					break;
				default:
					type = ResetType.HARD;
			}
				
			new ResetOperation(repo, targetName, type, context.getCommunicationChannel()).execute();			
		} catch (Exception e) {		
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
			return false;
		}		
		return true;
	}
	
	@RemoteInvocation
	public boolean configRemote(ServiceInvocationContext context, List<PathFragment> path,
			RemoteConfig remoteConfig) {
		try {
			Object node = GenericTreeStatefulService.getNodeByPathFor(path, null);		
			GenericTreeStatefulService service = GenericTreeStatefulService.getServiceFromPathWithRoot(path);
			NodeInfo nodeInfo = service.getVisibleNodes().get(node);
			Repository repository = getRepository(nodeInfo);
										
			org.eclipse.jgit.transport.RemoteConfig repoConfig = new org.eclipse.jgit.transport.RemoteConfig(repository.getConfig(), remoteConfig.getName());
			
			while (repoConfig.getURIs().size() > 0) {
				URIish uri = repoConfig.getURIs().get(0);
				repoConfig.removeURI(uri);
			}		
			repoConfig.addURI(new URIish(remoteConfig.getUri().trim()));
						
			repoConfig.update(repository.getConfig());			
			repository.getConfig().save();
			
			// notify clients about changes			
			dispatchContentUpdate(node);
			
			return true;
		} catch (URISyntaxException e) {
			logger.debug(CommonPlugin.getInstance().getMessage("error"), path, e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getReason(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
			return false;
		} catch (Exception e) {	
			logger.debug(CommonPlugin.getInstance().getMessage("error"), path, e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
			return false;
		}
	}
		
	@RemoteInvocation
	public RemoteConfig getRemoteConfigData(ServiceInvocationContext context, List<PathFragment> path) {
		try {			
			RemoteNode remoteNode = (RemoteNode) GenericTreeStatefulService.getNodeByPathFor(path, null);			
			Repository repository = remoteNode.getRepository();
			if (repository == null) {
				context.getCommunicationChannel().appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(
								CommonPlugin.getInstance().getMessage("error"), 
								"Cannot find repository for node " + remoteNode, 
								DisplaySimpleMessageClientCommand.ICON_ERROR));	
				return null;
			}
			
			org.eclipse.jgit.transport.RemoteConfig repoConfig = new org.eclipse.jgit.transport.RemoteConfig(repository.getConfig(), remoteNode.getRemote());
			
			RemoteConfig result = new RemoteConfig();
			result.setName(remoteNode.getRemote());
			result.setUri(repoConfig.getURIs().get(0).toString());
			
			List<String> fetchSpecs = new ArrayList<String>();
			for (RefSpec refspec : repoConfig.getFetchRefSpecs()) {
				fetchSpecs.add(refspec.toString());
			}
			result.setFetchMappings(fetchSpecs);
			
			List<String> pushSpecs = new ArrayList<String>();
			for (RefSpec refspec : repoConfig.getPushRefSpecs()) {
				pushSpecs.add(refspec.toString());
			}
			result.setPushMappings(pushSpecs);
					
			return result;
		} catch (Exception e) {		
			logger.debug(CommonPlugin.getInstance().getMessage("error"), path, e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
			return null;
		}
	}
	
	@RemoteInvocation
	public boolean deleteRemote(ServiceInvocationContext context, List<PathFragment> path) {
		try {
			RemoteNode remoteNode = (RemoteNode) GenericTreeStatefulService.getNodeByPathFor(path, null);			
			Repository repository = remoteNode.getRepository();	
			GenericTreeStatefulService service = GenericTreeStatefulService.getServiceFromPathWithRoot(path);
			NodeInfo remoteNodeInfo = service.getVisibleNodes().get(remoteNode);
			
			if (repository == null) {
				context.getCommunicationChannel().appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(
								CommonPlugin.getInstance().getMessage("error"), 
								"Cannot find repository for node " + remoteNode, 
								DisplaySimpleMessageClientCommand.ICON_ERROR));	
				return false;
			}
							
			StoredConfig config = repository.getConfig();
			config.unsetSection("remote", remoteNode.getRemote());
			config.save();
				
			dispatchContentUpdate(remoteNodeInfo.getParent().getNode());
			
			return true;
		} catch (Exception e) {		
			logger.debug(GitPlugin.getInstance().getMessage("git.deleteRemote.error"), e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							GitPlugin.getInstance().getMessage("git.deleteRemote.error"),
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
			return false;
		}
	}
	
	@RemoteInvocation
	public ConfigFetchPushPageDto getFetchPushConfigData(ServiceInvocationContext context, List<PathFragment> path, boolean getFetchData) {
		try {
			Repository repository = (Repository) GenericTreeStatefulService.getNodeByPathFor(path, null);			
							
			ConfigFetchPushPageDto result = new ConfigFetchPushPageDto();
			List<RemoteConfig> list = new ArrayList<RemoteConfig>();
			List<org.eclipse.jgit.transport.RemoteConfig> remotes = org.eclipse.jgit.transport.RemoteConfig.getAllRemoteConfigs(repository.getConfig());
			
			for (org.eclipse.jgit.transport.RemoteConfig remote : remotes) {
				RemoteConfig remoteConfig = new RemoteConfig();
				remoteConfig.setName(remote.getName());
				remoteConfig.setUri(remote.getURIs().get(0).toString());
				if (getFetchData) {					
					List<String> fetchSpecs = new ArrayList<String>();
					for (RefSpec refspec : remote.getFetchRefSpecs()) {
						fetchSpecs.add(refspec.toString());
					}
					remoteConfig.setFetchMappings(fetchSpecs);
				} else {					
					List<String> pushSpecs = new ArrayList<String>();
					for (RefSpec refspec : remote.getPushRefSpecs()) {
						pushSpecs.add(refspec.toString());
					}
					remoteConfig.setPushMappings(pushSpecs);
				}
				list.add(remoteConfig);
			}
			result.setRemoteConfigs(list);
			
			return result;
		} catch (Exception e) {		
			logger.debug(CommonPlugin.getInstance().getMessage("error"), path, e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
			return null;
		}
	}
		
	@RemoteInvocation
	public boolean fetch(ServiceInvocationContext context, List<PathFragment> path, RemoteConfig fetchConfig) {		
		tlCommand.set((InvokeServiceMethodServerCommand) context.getCommand());
		
		ProgressMonitor monitor = ProgressMonitor.create(GitPlugin.getInstance().getMessage("git.fetch.monitor.title"), context.getCommunicationChannel());
		
		try {
			Object node = GenericTreeStatefulService.getNodeByPathFor(path, null);
			GenericTreeStatefulService service = GenericTreeStatefulService.getServiceFromPathWithRoot(path);
			NodeInfo nodeInfo = service.getVisibleNodes().get(node);
			Repository repository = getRepository(nodeInfo);
			if (repository == null) {
				context.getCommunicationChannel().appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(
								CommonPlugin.getInstance().getMessage("error"), 
								"Cannot find repository for node " + node, 
								DisplaySimpleMessageClientCommand.ICON_ERROR));	
				return false;
			}
						
			String remote = null;
			if (GitNodeType.NODE_TYPE_REMOTE.equals(nodeInfo.getPathFragment().getType())) {				
				remote = ((RemoteNode) node).getRemote();
			}
			
			GitProgressMonitor gitMonitor = new GitProgressMonitor(monitor);
			FetchCommand command;
			
			if (remote != null) {
				command = new Git(repository).fetch().setRemote(remote);
			} else {
				List<RefSpec> specs = new ArrayList<RefSpec>();
				for (String refMapping : fetchConfig.getFetchMappings()) {
					specs.add(new RefSpec(refMapping));
				}			
				command = new Git(repository).fetch().setRemote(new URIish(fetchConfig.getUri()).toPrivateString()).setRefSpecs(specs);
			}
			command.setProgressMonitor(gitMonitor);
			command.setTagOpt(TagOpt.FETCH_TAGS);
						
			FetchResult fetchResult = command.call();
			
			dispatchContentUpdate(node);
			
			context.getCommunicationChannel().appendOrSendCommand(new DisplaySimpleMessageClientCommand(
					GitPlugin.getInstance().getMessage("git.fetch.result"), 
					GitPlugin.getInstance().getUtils().handleFetchResult(fetchResult),
					DisplaySimpleMessageClientCommand.ICON_INFORMATION));
						
			return true;			
		} catch (Exception e) {		
			if (GitPlugin.getInstance().getUtils().isAuthentificationException(e)) {
				openLoginWindow();				
				return true;
			}
			logger.debug(GitPlugin.getInstance().getMessage("git.fetch.error"), e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
			return false;
		} finally {
			monitor.done();			
		}
	}
	
	public boolean pushToUpstreamInternal(ServiceInvocationContext context, Repository repository, RemoteConfig pushConfig, String remote) {
		ProgressMonitor monitor = ProgressMonitor.create(GitPlugin.getInstance().getMessage("git.push.monitor.title"), context.getCommunicationChannel());
		
		try {			
			if (repository == null) {
				context.getCommunicationChannel().appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(
								CommonPlugin.getInstance().getMessage("error"), 
								"Cannot find repository " + repository, 
								DisplaySimpleMessageClientCommand.ICON_ERROR));	
				return false;
			}
						
			GitProgressMonitor gitMonitor = new GitProgressMonitor(monitor);
			PushCommand command;
			
			if (remote != null) {
				command = new Git(repository).push().setRemote(remote);
			} else {
				List<RefSpec> specs = new ArrayList<RefSpec>();
				for (String refMapping : pushConfig.getPushMappings()) {
					specs.add(new RefSpec(refMapping));
				}			
				command = new Git(repository).push().setRemote(new URIish(pushConfig.getUri()).toPrivateString()).setRefSpecs(specs);
			}
			command.setProgressMonitor(gitMonitor);
			command.setCredentialsProvider(new GitUsernamePasswordCredentialsProvider());
			Iterable<PushResult> resultIterable = command.call();
			PushResult pushResult = resultIterable.iterator().next();

			context.getCommunicationChannel().appendOrSendCommand(new DisplaySimpleMessageClientCommand(
					GitPlugin.getInstance().getMessage("git.push.result"), 
					GitPlugin.getInstance().getUtils().handlePushResult(pushResult),
					DisplaySimpleMessageClientCommand.ICON_INFORMATION));
						
			return true;
			
		} catch (Exception e) {		
			if (GitPlugin.getInstance().getUtils().isAuthentificationException(e)) {
				openLoginWindow();				
				return true;
			}
			logger.debug(GitPlugin.getInstance().getMessage("git.push.error"), e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
			return false;
		} finally {
			monitor.done();			
		}
	}
	
	@RemoteInvocation
	public boolean push(ServiceInvocationContext context, List<PathFragment> path, RemoteConfig pushConfig) {	
		tlCommand.set((InvokeServiceMethodServerCommand) context.getCommand());
		
		Object node = GenericTreeStatefulService.getNodeByPathFor(path, null);
		GenericTreeStatefulService service = GenericTreeStatefulService.getServiceFromPathWithRoot(path);
		NodeInfo nodeInfo = service.getVisibleNodes().get(node);
		Repository repository = getRepository(nodeInfo);
									
		String remote = null;
		if (GitNodeType.NODE_TYPE_REMOTE.equals(nodeInfo.getPathFragment().getType())) {				
			remote = ((RemoteNode) node).getRemote();
		}
			
		return pushToUpstreamInternal(context, repository, pushConfig, remote);			
	}
	
	@RemoteInvocation
	public boolean pushToUpstream(ServiceInvocationContext context, String repositoryLocation) {	
		tlCommand.set((InvokeServiceMethodServerCommand) context.getCommand());
		
		// push
		Repository repository = GitPlugin.getInstance().getUtils().getRepository(new File(repositoryLocation));
				
		if (repository == null) {
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							"Cannot find repository " + repository, 
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
			return false;
		}		
				
		try {
			Object[] data = GitPlugin.getInstance().getUtils().getFetchPushUpstreamDataRefSpecAndRemote(repository);
					
			RemoteConfig pushConfig = new RemoteConfig();
			
			List<String>refSpecs = new ArrayList<String>();
			refSpecs.add(String.format("+%s:%s", data[0], data[1]));
			pushConfig.setPushMappings(refSpecs);
			pushConfig.setUri((String) data[2]);
					
			return pushToUpstreamInternal(context, repository, pushConfig, null);
		} catch (Exception e) {		
			logger.debug(GitPlugin.getInstance().getMessage("git.push.error"), e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));
		}
		return false;		
	}
	
	@RemoteInvocation
	public boolean checkout(ServiceInvocationContext context, List<PathFragment> path, 
			String name, GitRef upstreamBranch, RemoteConfig remote, boolean rebase) {
		
		Object node =GenericTreeStatefulService.getNodeByPathFor(path, null);			
		GenericTreeStatefulService service = GenericTreeStatefulService.getServiceFromPathWithRoot(path);
		NodeInfo nodeInfo = service.getVisibleNodes().get(node);
		Repository repository = getRepository(nodeInfo);
		
		boolean result = new CheckoutOperation(node, repository, name, remote, upstreamBranch, rebase, context.getCommunicationChannel()).execute();
		
		if (result) {		
			for (NodeInfo repoChildNodeInfo : service.getVisibleNodes().get(repository).getChildren()) {
				if (GitNodeType.NODE_TYPE_LOCAL_BRANCHES.equals(repoChildNodeInfo.getPathFragment().getType())
					|| GitNodeType.NODE_TYPE_WDIRS.equals(repoChildNodeInfo.getPathFragment().getType())
					|| GitNodeType.NODE_TYPE_REMOTE_BRANCHES.equals(repoChildNodeInfo.getPathFragment().getType())) {
					dispatchContentUpdate(repoChildNodeInfo.getNode());
				}
			}			
		}
		return result;
	}
	
	@RemoteInvocation
	public ImportProjectPageDto getProjects(ServiceInvocationContext context, List<PathFragment> path) {
		try {			
			List<String> directories = new ArrayList<String>();
			List<File> files = new ArrayList<File>();
			
			@SuppressWarnings("unchecked")
			Pair<File, String> node = (Pair<File, String>) GenericTreeStatefulService.getNodeByPathFor(path, null);				
			Repository repo = GitPlugin.getInstance().getUtils().getRepository(node.a);

			GitPlugin.getInstance().getUtils().findProjectFiles(files, repo.getWorkTree(), null);
					
			List<ProjectDto> projects = new ArrayList<ProjectDto>();
			
			for (File projectSystemFile : files) {
				// project path
				IPath projPath = new Path(projectSystemFile.getParentFile().getCanonicalPath());
				String projectName = projPath.lastSegment();
				File repoFile = repo.getDirectory().getParentFile().getParentFile();
				String projectRelativeLocation = projPath.makeRelativeTo(new Path(repoFile.getAbsolutePath())).toString();
				ProjectDto dto = new ProjectDto();
				dto.setName(projectName + " (" + projectRelativeLocation + ")");
				dto.setLocation(projPath.toString());
												
				projects.add(dto);
			}
			ImportProjectPageDto result = new ImportProjectPageDto();
			result.setExistingProjects(projects);
			result.setSelectedFolders(directories);
			
			return result;
		} catch (Exception e) {
			logger.debug(GitPlugin.getInstance().getMessage("git.page.populate.error"), e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							GitPlugin.getInstance().getMessage("git.page.populate.error"), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
			return null;
		}
	}
	
	public boolean importProjects(ServiceInvocationContext context, List<ProjectDto> projects) {
		try {			
			for (ProjectDto dto : projects) {
				File file = new File(dto.getLocation());
				ProjectsService.getInstance().createOrImportProjectFromFile(context, file);
			}
			return true;
		} catch (Exception e) {		
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		}
	}
	
	@RemoteInvocation
	public boolean pull(ServiceInvocationContext context, List<PathFragment> path) {
		tlCommand.set((InvokeServiceMethodServerCommand) context.getCommand());
			
		try {			
			RefNode refNode = (RefNode) GenericTreeStatefulService.getNodeByPathFor(path, null);			
			GenericTreeStatefulService service = GenericTreeStatefulService.getServiceFromPathWithRoot(path);
			NodeInfo refNodeInfo = service.getVisibleNodes().get(refNode);
			Repository repository = getRepository(refNodeInfo);
			if (repository == null) {
				context.getCommunicationChannel().appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(
								CommonPlugin.getInstance().getMessage("error"), 
								"Cannot find repository for node " + refNode, 
								DisplaySimpleMessageClientCommand.ICON_ERROR));	
				return false;
			}
				
			File mainRepoFile = repository.getDirectory().getParentFile();		
			File wdirFile = new File(mainRepoFile.getParentFile(), GitUtils.WORKING_DIRECTORY_PREFIX + Repository.shortenRefName(refNode.getRef().getName()));
			if (!wdirFile.exists()) {
				return false;
			}			
			new PullOperation(refNode.getRef(), GitPlugin.getInstance().getUtils().getRepository(wdirFile), context.getCommunicationChannel()).execute();
			
			return true;
		} catch (Exception e) {		
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		}
	}
		
	@RemoteInvocation
	public CommitPageDto getCommitData(ServiceInvocationContext context, List<List<PathFragment>> paths) {
		List<File> files = new ArrayList<File>();
		for (List<PathFragment> path : paths) {
			@SuppressWarnings("unchecked")
			Pair<File, String> node = (Pair<File, String>) GenericTreeStatefulService.getNodeByPathFor(path, null);
			files.add(node.a);
		}
		return new CommitOperation(context.getCommunicationChannel(), files).getPageDto();
	}
	
	@RemoteInvocation
	public boolean commit(ServiceInvocationContext context, String repositoryLocation, List<CommitResourceDto> files, String author, String committer, String message, boolean amending) {		
		return new CommitOperation(context.getCommunicationChannel()).commit(repositoryLocation, files, author, committer, message, amending);		
	}
	
	@RemoteInvocation
	public List<RemoteConfig> getAllRemotes(ServiceInvocationContext context, List<PathFragment> path) {
		try {
			Object node = GenericTreeStatefulService.getNodeByPathFor(path, null);		
			GenericTreeStatefulService service = GenericTreeStatefulService.getServiceFromPathWithRoot(path);
			NodeInfo nodeInfo = service.getVisibleNodes().get(node);
			Repository repository = getRepository(nodeInfo);
			
			List<RemoteConfig> list = new ArrayList<RemoteConfig>();
			List<org.eclipse.jgit.transport.RemoteConfig> remotes = org.eclipse.jgit.transport.RemoteConfig.getAllRemoteConfigs(repository.getConfig());
			
			for (org.eclipse.jgit.transport.RemoteConfig remote : remotes) {
				RemoteConfig remoteConfig = new RemoteConfig();
				remoteConfig.setName(remote.getName());
				remoteConfig.setUri(remote.getURIs().get(0).toString());			
				list.add(remoteConfig);
			}
			return list;
		} catch (Exception e) {		
			logger.debug(CommonPlugin.getInstance().getMessage("error"), path, e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
			return null;
		}
	}
	
	@RemoteInvocation
	public ConfigBranchPageDto getConfigBranchData(ServiceInvocationContext context, List<PathFragment> path) {
		try {
			RefNode node = (RefNode) GenericTreeStatefulService.getNodeByPathFor(path, null);			
			Repository repository = node.getRepository();
			
			StoredConfig config = repository.getConfig();
			
			ConfigBranchPageDto dto = new ConfigBranchPageDto();
			
			Ref branch = (Ref) node.getRef();
			dto.setRef(new GitRef(branch.getName(), Repository.shortenRefName(branch.getName())));
			
			List<RemoteConfig> remotes = getAllRemotes(context, path);
			
			String branchName = branch.getName().substring(Constants.R_HEADS.length());
			String branchConfig = config.getString(
					ConfigConstants.CONFIG_BRANCH_SECTION, branchName,
					ConfigConstants.CONFIG_KEY_MERGE);
			

			String remoteConfig = config.getString(
					ConfigConstants.CONFIG_BRANCH_SECTION, branchName,
					ConfigConstants.CONFIG_KEY_REMOTE);
			if (remoteConfig == null) {
				remoteConfig = "";
			}
			if (remotes != null) {			
				dto.setRemotes(remotes);
			
				for (RemoteConfig remote : remotes) {
					if (remote.getName().equals(remoteConfig)) {
						List<Object> branches = getBranches(context, remote.getUri());
						if (branches != null) {
							@SuppressWarnings("unchecked")
							List<GitRef> refs = (List<GitRef>) branches.get(0);
							for (GitRef ref : refs) {
								if (ref.getName().equals(branchConfig)) {
									dto.setSelectedRef(ref);
									break;
								}
							}
							dto.setRefs(refs);
						}
						dto.setSelectedRemote(remote);
						break;
					}
				}
			}
		
			boolean rebaseFlag = config.getBoolean(
					ConfigConstants.CONFIG_BRANCH_SECTION, branchName,
					ConfigConstants.CONFIG_KEY_REBASE, false);
			dto.setRebase(rebaseFlag);
								
			return dto;
		} catch (Exception e) {
			logger.debug(CommonPlugin.getInstance().getMessage("error"), path, e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
			return null;
		}
	}
	
	@RemoteInvocation
	public boolean configBranch(ServiceInvocationContext context, List<PathFragment> path, GitRef upstreamBranch, RemoteConfig remote, boolean rebase) {
		try {
			RefNode node = (RefNode) GenericTreeStatefulService.getNodeByPathFor(path, null);				
			Repository repository = node.getRepository();
			StoredConfig config = repository.getConfig();
						
			Ref ref;
			if (node instanceof Ref) {
				ref = node.getRef();
			} else {
				// get remote branch
				String dst = Constants.R_REMOTES + remote.getName();
				String remoteRefName = dst + "/" + upstreamBranch.getShortName();
				ref = repository.getRef(remoteRefName);
				if (ref == null) { // doesn't exist, fetch it
					RefSpec refSpec = new RefSpec();
					refSpec = refSpec.setForceUpdate(true);
					refSpec = refSpec.setSourceDestination(upstreamBranch.getName(), remoteRefName);
		
					new Git(repository).fetch()
						.setRemote(new URIish(remote.getUri()).toPrivateString())
						.setRefSpecs(refSpec)
						.call();
					
					ref = repository.getRef(remoteRefName);
				}
			}				
			
			String branchName = node.getRef().getName().substring(Constants.R_HEADS.length());
			if (upstreamBranch.getName().length() > 0) {
				config.setString(ConfigConstants.CONFIG_BRANCH_SECTION,
						branchName, ConfigConstants.CONFIG_KEY_MERGE,
						upstreamBranch.getName());
			} else {
				config.unset(ConfigConstants.CONFIG_BRANCH_SECTION,
						branchName, ConfigConstants.CONFIG_KEY_MERGE);
			}
			if (remote.getName().length() > 0) {
				config.setString(ConfigConstants.CONFIG_BRANCH_SECTION,
						branchName, ConfigConstants.CONFIG_KEY_REMOTE,
						remote.getName());
			} else {
				config.unset(ConfigConstants.CONFIG_BRANCH_SECTION,
						branchName, ConfigConstants.CONFIG_KEY_REMOTE);
			}
			if (rebase) {
				config.setBoolean(ConfigConstants.CONFIG_BRANCH_SECTION,
						branchName, ConfigConstants.CONFIG_KEY_REBASE,
						true);
			} else {
				config.unset(ConfigConstants.CONFIG_BRANCH_SECTION,
						branchName, ConfigConstants.CONFIG_KEY_REBASE);
			}
			
			config.save();
			
			return true;
		} catch (Exception e) {	
			logger.debug(CommonPlugin.getInstance().getMessage("error"), path, e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		}
	}
	
	@RemoteInvocation
	public boolean deleteBranch(ServiceInvocationContext context, List<PathFragment> path) {
		try {
			RefNode node = (RefNode) GenericTreeStatefulService.getNodeByPathFor(path, null);		
			GenericTreeStatefulService service = GenericTreeStatefulService.getServiceFromPathWithRoot(path);
			NodeInfo nodeInfo = service.getVisibleNodes().get(node);
			Repository repository = node.getRepository();
			
			Git git = new Git(repository);
		
			git.branchDelete().
				setBranchNames(node.getRef().getName()).
				setForce(true).
				call();
			
			// delete working directory
			String branchName = node.getRef().getName().substring(Constants.R_HEADS.length());
			File mainRepoFile = repository.getDirectory().getParentFile();		
			File wdirFile = new File(mainRepoFile.getParentFile(), GitUtils.WORKING_DIRECTORY_PREFIX + branchName);
			if (wdirFile.exists()) {
				GitPlugin.getInstance().getUtils().delete(wdirFile);
			}			
			
			dispatchContentUpdate(nodeInfo.getParent().getNode());
			return true;
		} catch (GitAPIException e) {
			logger.debug(CommonPlugin.getInstance().getMessage("error"), path, e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
			return false;
		}	
	}
	
	@RemoteInvocation
	public boolean openCredentials(ServiceInvocationContext context, List<PathFragment> path) {
		try {
			RemoteNode remoteNode = (RemoteNode) GenericTreeStatefulService.getNodeByPathFor(path, null);			
			Repository repository = remoteNode.getRepository();
			if (repository == null) {
				context.getCommunicationChannel().appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(
								CommonPlugin.getInstance().getMessage("error"), 
								"Cannot find repository for node " + remoteNode, 
								DisplaySimpleMessageClientCommand.ICON_ERROR));	
				return false;
			}
						
			org.eclipse.jgit.transport.RemoteConfig repoConfig = new org.eclipse.jgit.transport.RemoteConfig(repository.getConfig(), remoteNode.getRemote());
						
			URIish uri = repoConfig.getURIs().get(0);		
			List<String> credentials = ((FlowerWebPrincipal) CommunicationPlugin.tlCurrentPrincipal.get()).getUserGitRepositories().get(uri.toString());
			
			context.getCommunicationChannel().appendOrSendCommand(
					new OpenGitCredentialsWindowClientCommand(uri.toString(), credentials != null ? credentials.get(0) : ""));
			
			return true;
		} catch (Exception e) {		
			logger.debug(GitPlugin.getInstance().getMessage("git.changeCredentials.error"), e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							GitPlugin.getInstance().getMessage("git.changeCredentials.error"),
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
			return false;
		}	
	}
	
	@RemoteInvocation
	public boolean clearCredentials(ServiceInvocationContext context, List<PathFragment> path) {
		try {
			RemoteNode remoteNode = (RemoteNode) GenericTreeStatefulService.getNodeByPathFor(path, null);			
			Repository repository = remoteNode.getRepository();
			if (repository == null) {
				context.getCommunicationChannel().appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(
								CommonPlugin.getInstance().getMessage("error"), 
								"Cannot find repository for node " + remoteNode, 
								DisplaySimpleMessageClientCommand.ICON_ERROR));	
				return false;
			}
			org.eclipse.jgit.transport.RemoteConfig repoConfig = new org.eclipse.jgit.transport.RemoteConfig(repository.getConfig(), remoteNode.getRemote());
			
			URIish uri = repoConfig.getURIs().get(0);			
			((FlowerWebPrincipal) CommunicationPlugin.tlCurrentPrincipal.get()).getUserGitRepositories().remove(uri.toString());
	
			return true;
		} catch (Exception e) {		
			logger.debug(GitPlugin.getInstance().getMessage("git.clearCredentials.error"), e);
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							GitPlugin.getInstance().getMessage("git.clearCredentials.error"),
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
			return false;
		}	
	}
	
	/**
	 * Stores login information in principal and re-executes the interrupted operation.
	 */
	@RemoteInvocation
	public void login(ServiceInvocationContext context, String uri, String username, String password, InvokeServiceMethodServerCommand command) {				
		changeCredentials(context, uri, username, password);
		
		command.setCommunicationChannel(context.getCommunicationChannel());
		command.executeCommand();		
	}
	
	@RemoteInvocation
	public void changeCredentials(ServiceInvocationContext context, String uri, String username, String password) {				
		List<String> info = new ArrayList<String>();
		info.add(username);
		info.add(password);
		FlowerWebPrincipal principal = (FlowerWebPrincipal) CommunicationPlugin.tlCurrentPrincipal.get();
		principal.getUserGitRepositories().put(uri, info);
	}
	
}
