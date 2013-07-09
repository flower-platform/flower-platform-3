package org.flowerplatform.web.git.operation;

import java.io.File;
import java.util.List;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CheckoutResult;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.URIish;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.progress_monitor.ProgressMonitor;
import org.flowerplatform.web.git.GitPlugin;
import org.flowerplatform.web.git.GitUtils;
import org.flowerplatform.web.git.remote.dto.GitRef;
import org.flowerplatform.web.git.remote.dto.RemoteConfig;

/**
 * @author Cristina Constantinescu
 */ 
public class CheckoutOperation {

	private Object node;	
	private Repository repository;	
	private String name;
	private RemoteConfig remote;
	private GitRef upstreamBranch;
	private boolean rebase;
	private CommunicationChannel channel;
			

	public CheckoutOperation(Object node, Repository repository, String name, RemoteConfig remote,
			GitRef upstreamBranch, boolean rebase, CommunicationChannel channel) {	
		this.node = node;
		this.repository = repository;
		this.name = name;
		this.remote = remote;
		this.upstreamBranch = upstreamBranch;
		this.rebase = rebase;
		this.channel = channel;
	}
	
	public boolean execute() {
		ProgressMonitor monitor = ProgressMonitor.create(GitPlugin.getInstance().getMessage("git.checkout.monitor.title"), channel);
		
		try {	
			monitor.beginTask(GitPlugin.getInstance().getMessage("git.checkout.monitor.message", new Object[] {name}), 4);			
			monitor.setTaskName("Getting remote branch...");
			Git git = new Git(repository);
			Ref ref;
			if (node instanceof Ref) {
				ref = (Ref) node;
			} else {
				// get remote branch
				String dst = Constants.R_REMOTES + remote.getName();
				String remoteRefName = dst + "/" + upstreamBranch.getShortName();
				ref = repository.getRef(remoteRefName);
				if (ref == null) { // doesn't exist, fetch it
					RefSpec refSpec = new RefSpec();
					refSpec = refSpec.setForceUpdate(true);
					refSpec = refSpec.setSourceDestination(upstreamBranch.getName(), remoteRefName);
		
					git.fetch()
						.setRemote(new URIish(remote.getUri()).toPrivateString())
						.setRefSpecs(refSpec)
						.call();
					
					ref = repository.getRef(remoteRefName);
				}
			}					
			monitor.worked(1);
			monitor.setTaskName("Creating local branch...");
			
			// create local branch
			git.branchCreate().
				setName(name).
				setStartPoint(ref.getName()).
				setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM).
				call();
		
			if (!(node instanceof Ref)) {
				// save upstream configuration
				StoredConfig config = repository.getConfig();			
				
				config.setString(ConfigConstants.CONFIG_BRANCH_SECTION,
							name, ConfigConstants.CONFIG_KEY_MERGE,
							upstreamBranch.getName());
							
				config.setString(ConfigConstants.CONFIG_BRANCH_SECTION,
						name, ConfigConstants.CONFIG_KEY_REMOTE,
						remote.getName());
				
				if (rebase) {
					config.setBoolean(ConfigConstants.CONFIG_BRANCH_SECTION,
							name, ConfigConstants.CONFIG_KEY_REBASE,
							true);
				} else {
					config.unset(ConfigConstants.CONFIG_BRANCH_SECTION,
							name, ConfigConstants.CONFIG_KEY_REBASE);
				}			
				config.save();				
			}			
			monitor.worked(1);
			monitor.setTaskName("Creating working directory");
			
			// create working directory for local branch
			File mainRepoFile = repository.getDirectory().getParentFile();		
			File wdirFile = new File(mainRepoFile.getParentFile(), GitUtils.WORKING_DIRECTORY_PREFIX + name);
			if (wdirFile.exists()) {
				GitPlugin.getInstance().getUtils().delete(wdirFile);
			}			
			GitPlugin.getInstance().getUtils().run_git_workdir_cmd(mainRepoFile.getAbsolutePath(), wdirFile.getAbsolutePath());			
			monitor.worked(1);
			monitor.setTaskName("Checkout branch");
			
			// checkout local branch
			Repository wdirRepo = GitPlugin.getInstance().getUtils().getRepository(wdirFile);
			git = new Git(wdirRepo);
								
			CheckoutCommand cc = git.checkout().setName(name).setForce(true);						
			cc.call();				
			
			// show checkout result
			if (cc.getResult().getStatus() == CheckoutResult.Status.CONFLICTS)
				channel.appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(
								GitPlugin.getInstance().getMessage("git.checkout.checkoutConflicts.title"), 
								GitPlugin.getInstance().getMessage("git.checkout.checkoutConflicts.message"),
								cc.getResult().getConflictList().toString(),
								DisplaySimpleMessageClientCommand.ICON_INFORMATION));	
				
			else if (cc.getResult().getStatus() == CheckoutResult.Status.NONDELETED) {
				// double-check if the files are still there
				boolean show = false;
				List<String> pathList = cc.getResult().getUndeletedList();
				for (String path1 : pathList) {
					if (new File(wdirRepo.getWorkTree(), path1).exists()) {
						show = true;
						break;
					}
				}
				if (show) {
					channel.appendOrSendCommand(
							new DisplaySimpleMessageClientCommand(
									GitPlugin.getInstance().getMessage("git.checkout.nonDeletedFiles.title"), 
									GitPlugin.getInstance().getMessage("git.checkout.nonDeletedFiles.message", Repository.shortenRefName(name)),
									cc.getResult().getUndeletedList().toString(),
									DisplaySimpleMessageClientCommand.ICON_ERROR));		
				}
			} else if (cc.getResult().getStatus() == CheckoutResult.Status.OK) {
				if (ObjectId.isId(wdirRepo.getFullBranch()))
					channel.appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(
								GitPlugin.getInstance().getMessage("git.checkout.detachedHead.title"), 
								GitPlugin.getInstance().getMessage("git.checkout.detachedHead.message"),
								DisplaySimpleMessageClientCommand.ICON_ERROR));		
			}			
			monitor.worked(1);				
			return true;
		} catch (Exception e) {		
			channel.appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
			return false;
		} finally {
			monitor.done();
		}
	}
	
}
