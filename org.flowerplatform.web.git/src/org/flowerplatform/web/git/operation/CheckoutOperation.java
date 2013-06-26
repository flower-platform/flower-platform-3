package org.flowerplatform.web.git.operation;

import java.io.File;
import java.util.List;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CheckoutResult;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.progress_monitor.ProgressMonitor;
import org.flowerplatform.web.git.GitPlugin;
import org.flowerplatform.web.git.entity.RefNode;

/**
 * @author Cristina Constantinescu
 */ 
public class CheckoutOperation {

	private RefNode node;	
	private String name;
	private CommunicationChannel channel;
		
	public CheckoutOperation(String name, RefNode node, CommunicationChannel channel) {
		this.name = name;
		this.channel = channel;
		this.node = node;
	}

	public boolean execute() {
		ProgressMonitor monitor = ProgressMonitor.create(GitPlugin.getInstance().getMessage("git.checkout.monitor.title"), channel);
		
		try {	
			monitor.beginTask(GitPlugin.getInstance().getMessage("git.checkout.monitor.message", new Object[] {name}), 3);
			Git git = new Git(node.getRepository());
			
			git.branchCreate().
				setName(name).
				setStartPoint(node.getObject().getName()).
				setUpstreamMode(SetupUpstreamMode.TRACK).
				call();
		
			monitor.worked(1);
			
			File mainRepoFile = node.getRepository().getDirectory().getParentFile();		
			File wdirFile = new File(mainRepoFile.getParentFile(), "wd_" + name);
			if (wdirFile.exists()) {
				GitPlugin.getInstance().getUtils().delete(wdirFile);
			}
			
			GitPlugin.getInstance().getUtils().run_git_workdir_cmd(mainRepoFile.getAbsolutePath(), wdirFile.getAbsolutePath());
			
			monitor.worked(1);
			
			Repository wdirRepo = GitPlugin.getInstance().getUtils().getRepository(wdirFile);
			git = new Git(wdirRepo);
//			IProject[] validProjects = GitPlugin.getInstance().getGitUtils().getValidProjects(repository);
//
//			GitPlugin.getInstance().getGitUtils().backupProjectConfigFiles(null, validProjects);
								
			CheckoutCommand cc = git.checkout().setName(name).setForce(true);
						
			cc.call();				
			
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
//			GitPlugin.getInstance().getGitUtils().refreshValidProjects(validProjects, new SubProgressMonitor(monitor, 1));
			
			monitor.worked(1);
			
//			GitRepositoriesTreeStatefulService.getInstance().getUpdateDispatcher().dispatchContentUpdate(repository, GitTreeUpdateDispatcher.CHECKOUT, null);
								
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
//			GitPlugin.getInstance().getGitUtils().restoreProjectConfigFiles(repository, null);
		}
	}
	
}
