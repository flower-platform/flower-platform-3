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
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.web.git.command.client.OpenOperationResultWindowClientCommand;
import org.flowerplatform.web.git.dto.CommitPageDto;
import org.flowerplatform.web.git.dto.ConfigFetchPushPageDto;
import org.flowerplatform.web.git.dto.ImportProjectPageDto;
import org.flowerplatform.web.git.dto.ProjectDto;
import org.flowerplatform.web.git.entity.GitNode;
import org.flowerplatform.web.git.entity.GitNodeType;
import org.flowerplatform.web.git.entity.RefNode;
import org.flowerplatform.web.git.entity.RemoteNode;
import org.flowerplatform.web.git.entity.RepositoryNode;
import org.flowerplatform.web.git.entity.SimpleNode;
import org.flowerplatform.web.git.operation.CheckoutOperation;
import org.flowerplatform.web.git.operation.CommitOperation;
import org.flowerplatform.web.git.operation.MergeOperation;
import org.flowerplatform.web.git.operation.PullOperation;
import org.flowerplatform.web.git.operation.RebaseOperation;
import org.flowerplatform.web.git.operation.ResetOperation;
import org.flowerplatform.web.git.remote.OpenGitCredentialsWindowClientCommand;
import org.flowerplatform.web.git.remote.dto.GitActionDto;
import org.flowerplatform.web.git.remote.dto.GitRef;
import org.flowerplatform.web.git.remote.dto.RemoteConfig;
import org.flowerplatform.web.security.sandbox.FlowerWebPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristina Constantinescu
 */
public class GitService {

	private static Logger logger = LoggerFactory.getLogger(GitService.class);
	
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
	
	private void dispatchContentUpdate(Object node) {
		for (GenericTreeStatefulService service : GitPlugin.getInstance().getTreeStatefulServicesDisplayingGitContent()) {
			service.dispatchContentUpdate(node);
		}
	}
			
	/**
	 * Sends command to client to open the login window 
	 * based on the info stored in {@link #tlCommand}.
	 */
	public void openLoginWindow() {
		InvokeServiceMethodServerCommand cmd = tlCommand.get();	
		
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
		RepositoryNode node = (RepositoryNode) GenericTreeStatefulService.getNodeByPathFor(selectedNode, null);
				
		ProgressMonitor monitor = ProgressMonitor.create(
				GitPlugin.getInstance().getMessage("git.deleteRepo.monitor.title"), context.getCommunicationChannel());
		
		try {						
			monitor.beginTask(GitPlugin.getInstance().getMessage("git.deleteRepo.monitor.message", 
					new Object[] {GitPlugin.getInstance().getUtils().getRepositoryName(node.getRepository())}), 2);
			
			node.getRepository().getObjectDatabase().close();
			node.getRepository().getRefDatabase().close();			
			monitor.worked(1);		
			
			node.getRepository().close();	
			File repoFile = node.getRepository().getDirectory().getParentFile().getParentFile();
			if (GitUtils.GIT_REPOSITORIES_NAME.equals(repoFile.getParentFile().getName())) {
				GitPlugin.getInstance().getUtils().delete(repoFile);
			}
			monitor.worked(1);		
			
			dispatchContentUpdate(node.getParent());
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
			RefNode node = (RefNode) GenericTreeStatefulService.getNodeByPathFor(path, null);			
			Repository repo = node.getRepository();

			GitActionDto data = new GitActionDto();		
			data.setRepository(repo.getDirectory().getAbsolutePath());		
			data.setBranch(repo.getBranch());		
			
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
					new OpenOperationResultWindowClientCommand(GitPlugin.getInstance().getMessage("git.merge.result"), result));
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
						new OpenOperationResultWindowClientCommand(GitPlugin.getInstance().getMessage("git.rebase.result"), result));
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
			SimpleNode node = (SimpleNode) GenericTreeStatefulService.getNodeByPathFor(path, null);			
			Repository repo = node.getRepository();
					
			org.eclipse.jgit.transport.RemoteConfig repoConfig = new org.eclipse.jgit.transport.RemoteConfig(repo.getConfig(), remoteConfig.getName());
			
			while (repoConfig.getURIs().size() > 0) {
				URIish uri = repoConfig.getURIs().get(0);
				repoConfig.removeURI(uri);
			}		
			repoConfig.addURI(new URIish(remoteConfig.getUri().trim()));
						
			repoConfig.update(repo.getConfig());			
			repo.getConfig().save();
			
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
			RemoteNode node = (RemoteNode) GenericTreeStatefulService.getNodeByPathFor(path, null);			
			Repository repo = node.getRepository();
			
			String remoteName = node.getObject();
			
			org.eclipse.jgit.transport.RemoteConfig repoConfig = new org.eclipse.jgit.transport.RemoteConfig(repo.getConfig(), remoteName);
			
			RemoteConfig result = new RemoteConfig();
			result.setName(remoteName);
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
			RemoteNode node = (RemoteNode) GenericTreeStatefulService.getNodeByPathFor(path, null);			
				
			StoredConfig config = node.getRepository().getConfig();
			config.unsetSection("remote", (String) node.getObject());
			config.save();
				
			dispatchContentUpdate(node.getParent());
			
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
			RepositoryNode node = (RepositoryNode) GenericTreeStatefulService.getNodeByPathFor(path, null);	
			Repository repo = node.getRepository();
					
			ConfigFetchPushPageDto result = new ConfigFetchPushPageDto();
			List<RemoteConfig> list = new ArrayList<RemoteConfig>();
			List<org.eclipse.jgit.transport.RemoteConfig> remotes = org.eclipse.jgit.transport.RemoteConfig.getAllRemoteConfigs(repo.getConfig());
			
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
	public boolean fetch(ServiceInvocationContext context, List<PathFragment> path, RemoteConfig fetchConfig, int tagOpt, boolean saveConfig) {		
		tlCommand.set((InvokeServiceMethodServerCommand) context.getCommand());
		
		ProgressMonitor monitor = ProgressMonitor.create(GitPlugin.getInstance().getMessage("git.fetch.monitor.title"), context.getCommunicationChannel());
		
		try {
			GitNode<?> node = (GitNode<?>) GenericTreeStatefulService.getNodeByPathFor(path, null);
			Repository repo = node.getRepository();
			
			RemoteNode remote = null;
			if (node.getType().equals(GitNodeType.NODE_TYPE_REMOTE)) {
				remote = (RemoteNode) node;
			}
			
			GitProgressMonitor gitMonitor = new GitProgressMonitor(monitor);
			FetchCommand command;
			
			if (remote != null) {
				command = new Git(repo).fetch().setRemote(remote.getObject());
			} else {
				List<RefSpec> specs = new ArrayList<RefSpec>();
				for (String refMapping : fetchConfig.getFetchMappings()) {
					specs.add(new RefSpec(refMapping));
				}			
				command = new Git(repo).fetch().setRemote(new URIish(fetchConfig.getUri()).toPrivateString()).setRefSpecs(specs);
			}
			command.setProgressMonitor(gitMonitor);
			
			TagOpt tagOption = null;
			switch (tagOpt) {
			case 0:
				tagOption = TagOpt.AUTO_FOLLOW;
				break;
			case 1:
				tagOption = TagOpt.FETCH_TAGS;
				break;
			case 2:
				tagOption = TagOpt.NO_TAGS;
				break;
			default:
				break;
			}			
			if (tagOption != null) {
				command.setTagOpt(tagOption);
			}
			
			FetchResult fetchResult = command.call();
			
			dispatchContentUpdate(node);
			
			context.getCommunicationChannel().appendOrSendCommand(new OpenOperationResultWindowClientCommand(
					GitPlugin.getInstance().getMessage("git.fetch.result"), 
					GitPlugin.getInstance().getUtils().handleFetchResult(fetchResult)));
			
			if (saveConfig) {
				org.eclipse.jgit.transport.RemoteConfig repoConfig = new org.eclipse.jgit.transport.RemoteConfig(repo.getConfig(), fetchConfig.getName());
				
				while (repoConfig.getFetchRefSpecs().size() > 0) {
					RefSpec refspec = repoConfig.getFetchRefSpecs().get(0);
					repoConfig.removeFetchRefSpec(refspec);
				}		
				for (String refMapping : fetchConfig.getFetchMappings()) {
					repoConfig.addFetchRefSpec(new RefSpec(refMapping));
				}
				
				repoConfig.update(repo.getConfig());			
				repo.getConfig().save();
			}
			
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
	
	@RemoteInvocation
	public boolean push(ServiceInvocationContext context, List<PathFragment> path, RemoteConfig pushConfig, boolean saveConfig) {
		tlCommand.set((InvokeServiceMethodServerCommand) context.getCommand());
		
		ProgressMonitor monitor = ProgressMonitor.create(GitPlugin.getInstance().getMessage("git.push.monitor.title"), context.getCommunicationChannel());
		
		try {
			GitNode<?> node = (GitNode<?>) GenericTreeStatefulService.getNodeByPathFor(path, null);
			Repository repo = node.getRepository();
			
			RemoteNode remote = null;
			if (node.getType().equals(GitNodeType.NODE_TYPE_REMOTE)) {
				remote = (RemoteNode) node;
			}
			
			GitProgressMonitor gitMonitor = new GitProgressMonitor(monitor);
			PushCommand command;
			
			if (remote != null) {
				command = new Git(repo).push().setRemote(remote.getObject());
			} else {
				List<RefSpec> specs = new ArrayList<RefSpec>();
				for (String refMapping : pushConfig.getPushMappings()) {
					specs.add(new RefSpec(refMapping));
				}			
				command = new Git(repo).push().setRemote(new URIish(pushConfig.getUri()).toPrivateString()).setRefSpecs(specs);
			}
			command.setProgressMonitor(gitMonitor);
			command.setCredentialsProvider(new GitUsernamePasswordCredentialsProvider());
			Iterable<PushResult> resultIterable = command.call();
			PushResult pushResult = resultIterable.iterator().next();

			context.getCommunicationChannel().appendOrSendCommand(new OpenOperationResultWindowClientCommand(
					GitPlugin.getInstance().getMessage("git.push.result"), 
					GitPlugin.getInstance().getUtils().handlePushResult(pushResult)));
			
			if (saveConfig) {
				org.eclipse.jgit.transport.RemoteConfig repoConfig = new org.eclipse.jgit.transport.RemoteConfig(repo.getConfig(), pushConfig.getName());
			
				while (repoConfig.getPushRefSpecs().size() > 0) {
					RefSpec refspec = repoConfig.getPushRefSpecs().get(0);
					repoConfig.removePushRefSpec(refspec);
				}
				for (String refMapping : pushConfig.getPushMappings()) {
					repoConfig.addPushRefSpec(new RefSpec(refMapping));
				}

				repoConfig.update(repo.getConfig());			
				repo.getConfig().save();
			}
			
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
	public boolean checkout(ServiceInvocationContext context, List<PathFragment> path, 
			String name, GitRef upstreamBranch, RemoteConfig remote, boolean rebase) {
		
		GitNode<?> node = (GitNode<?>) GenericTreeStatefulService.getNodeByPathFor(path, null);	
		Repository repo = node.getRepository();
		
		boolean result = new CheckoutOperation(node, name, remote, upstreamBranch, rebase, context.getCommunicationChannel()).execute();
		
		if (result) {
			RepositoryNode repoNode;
			if (GitNodeType.NODE_TYPE_TAG.equals(node.getType())) {
				repoNode = (RepositoryNode) ((SimpleNode) node.getParent()).getParent();
			} else {
				repoNode = (RepositoryNode) node.getParent();
			}			
			dispatchContentUpdate(new SimpleNode(repoNode, repo, GitNodeType.NODE_TYPE_LOCAL_BRANCHES));
			dispatchContentUpdate(new SimpleNode(repoNode, repo, GitNodeType.NODE_TYPE_WDIRS));
			dispatchContentUpdate(new SimpleNode(repoNode, repo, GitNodeType.NODE_TYPE_REMOTE_BRANCHES));
		}
		return result;
	}
	
	@RemoteInvocation
	public ImportProjectPageDto getProjects(ServiceInvocationContext context, List<List<PathFragment>> paths) {
		try {
			Repository repo = null;
			List<String> directories = new ArrayList<String>();
			List<File> files = new ArrayList<File>();
			for (List<PathFragment> path : paths) {
				GitNode<?> node = (GitNode<?>) GenericTreeStatefulService.getNodeByPathFor(path, null);				
				if (repo == null) {
					repo = node.getRepository();
				}
				File directory = null;
				switch (node.getType()) {
					case GitNodeType.NODE_TYPE_REPOSITORY:
					case GitNodeType.NODE_TYPE_WDIR:
						directory = repo.getWorkTree();				
						break;
					case GitNodeType.NODE_TYPE_FOLDER:
						directory = (File) node.getObject();					
						break;
				}		
				directories.add(directory.getCanonicalPath());
				GitPlugin.getInstance().getUtils().findProjectFiles(files, directory, null);
			}
			
			List<ProjectDto> projects = new ArrayList<ProjectDto>();
			
			for (File projectSystemFile : files) {
				// project path
				IPath path = new Path(projectSystemFile.getParentFile().getCanonicalPath());
				String projectName = path.lastSegment();
				File repoFile = repo.getDirectory().getParentFile().getParentFile();
				String projectRelativeLocation = path.makeRelativeTo(new Path(repoFile.getAbsolutePath())).toString();
				ProjectDto dto = new ProjectDto();
				dto.setName(projectName + " (" + projectRelativeLocation + ")");
				dto.setLocation(path.toString());
												
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
	
	@RemoteInvocation
	public boolean pull(ServiceInvocationContext context, List<PathFragment> path) {
		tlCommand.set((InvokeServiceMethodServerCommand) context.getCommand());
			
		try {			
			RefNode node = (RefNode) GenericTreeStatefulService.getNodeByPathFor(path, null);
				
			File mainRepoFile = node.getRepository().getDirectory().getParentFile();		
			File wdirFile = new File(mainRepoFile.getParentFile(), GitUtils.WORKING_DIRECTORY_PREFIX + Repository.shortenRefName(node.getObject().getName()));
			if (!wdirFile.exists()) {
				return false;
			}			
			new PullOperation(node.getObject(), GitPlugin.getInstance().getUtils().getRepository(wdirFile), context.getCommunicationChannel()).execute();
			
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
	public boolean importExistingProjects(ServiceInvocationContext context, List<ProjectDto> projects) {
		for (ProjectDto project : projects) {
			
		}
		return true;
	}
	
	@RemoteInvocation
	public boolean importAsProjects(ServiceInvocationContext context, List<String> folders) {
		for (String folder : folders) {
				
		}
		return true;
	}
	
	@RemoteInvocation
	public CommitPageDto getCommitData(ServiceInvocationContext context, List<List<PathFragment>> paths) {
		List<File> files = new ArrayList<File>();
		for (List<PathFragment> path : paths) {
			File file = (File) GenericTreeStatefulService.getNodeByPathFor(path, null);
			files.add(file);
		}
		return new CommitOperation(context.getCommunicationChannel(), files).getPageDto();
	}
	
	@RemoteInvocation
	public Object getAllRemotes(ServiceInvocationContext context, List<PathFragment> path) {
		try {
			SimpleNode node = (SimpleNode) GenericTreeStatefulService.getNodeByPathFor(path, null);
			Repository repo = node.getRepository();
			
			List<RemoteConfig> list = new ArrayList<RemoteConfig>();
			List<org.eclipse.jgit.transport.RemoteConfig> remotes = org.eclipse.jgit.transport.RemoteConfig.getAllRemoteConfigs(repo.getConfig());
			
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
	public boolean openCredentials(ServiceInvocationContext context, List<PathFragment> path) {
		try {
			RemoteNode node = (RemoteNode) GenericTreeStatefulService.getNodeByPathFor(path, null);			
			Repository repo = node.getRepository();
			
			org.eclipse.jgit.transport.RemoteConfig repoConfig = new org.eclipse.jgit.transport.RemoteConfig(repo.getConfig(), node.getObject());
						
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
			RemoteNode node = (RemoteNode) GenericTreeStatefulService.getNodeByPathFor(path, null);			
			Repository repo = node.getRepository();
			
			org.eclipse.jgit.transport.RemoteConfig repoConfig = new org.eclipse.jgit.transport.RemoteConfig(repo.getConfig(), node.getObject());
			
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
