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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.URIish;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.progress_monitor.ProgressMonitor;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.InvokeStatefulServiceMethodServerCommand;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.web.git.command.client.OpenGitCredentialsWindowClientCommand;
import org.flowerplatform.web.git.dto.GitRef;
import org.flowerplatform.web.git.entity.RepositoryNode;
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
			db = new GitFileRepository(new File("/tmp"));		
			
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
			return Arrays.asList(new Object[] {branches, headRef, Constants.DEFAULT_REMOTE_NAME});			
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
			String repositoryUrl, final List<String> selectedBranches, final String initialBranch, final String remoteName, final boolean cloneAllBranches) {		
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
								
					if (initialBranch != null)
						cloneRepository.setBranch(initialBranch);
					else {
						cloneRepository.setNoCheckout(true);
					}
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
					// reveal and select the main working directory
//					revealNode(context, new RepositoryNode(null, repository));
					monitor.worked(1);
					
					context.getCommunicationChannel().sendCommandWithPush(
							new DisplaySimpleMessageClientCommand(
									CommonPlugin.getInstance().getMessage("info"), 
									GitPlugin.getInstance().getMessage("git.cloneWizard.importProjects.info"),							
									DisplaySimpleMessageClientCommand.ICON_INFORMATION));	
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
			GitPlugin.getInstance().getUtils().delete(node.getRepository().getDirectory().getParentFile().getParentFile());
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
		//((FlowerWebPrincipal) ((WebCommunicationChannel) context.getCommunicationChannel()).getPrincipal()).getUserGitRepositories().put(uri, info);
	}
	
}
