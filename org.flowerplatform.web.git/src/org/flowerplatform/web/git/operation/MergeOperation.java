package org.flowerplatform.web.git.operation;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.MergeCommand.FastForwardMode;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.progress_monitor.ProgressMonitor;
import org.flowerplatform.web.git.GitPlugin;

/**
 * @author Cristina Constantinescu
 */
public class MergeOperation {

	private Repository repository;
	private String refName;
	private boolean squash;	
	private CommunicationChannel channel;
	private MergeResult mergeResult;
	 
	public MergeOperation(Repository repository, String refName, boolean squash, CommunicationChannel channel) {
		this.repository = repository;
		this.refName = refName;
		this.squash = squash;
		this.channel = channel;
	}
		
	public MergeResult getMergeResult() {
		return mergeResult;
	}

	public void execute() {
		ProgressMonitor monitor = ProgressMonitor.create(GitPlugin.getInstance().getMessage("git.merge"), channel);
		
		try {
			monitor.beginTask(GitPlugin.getInstance().getMessage("git.merge.title", new Object[] {refName}), 3);
//			IProject[] validProjects = GitPlugin.getInstance().getUtils().getValidProjects(repository);
//			
//			GitPlugin.getInstance().getGitUtils().backupProjectConfigFiles(null, validProjects);
//						
			Git git = new Git(repository);
			monitor.worked(1);
			MergeCommand merge;
			
			FastForwardMode ffmode = FastForwardMode.FF;
			Ref ref = repository.getRef(refName);
			if (ref != null) {
				merge = git.merge().include(ref).setFastForward(ffmode);
			} else {
				merge = git.merge()
						.include(ObjectId.fromString(refName))
						.setFastForward(ffmode);
			}
			merge.setSquash(squash);
					
			mergeResult = (MergeResult) GitPlugin.getInstance().getUtils().runGitCommandInUserRepoConfig(repository, merge);
			monitor.worked(1);	
			
//			GitPlugin.getInstance().getUtils().refreshValidProjects(validProjects, new SubProgressMonitor(monitor, 1));		
		} catch (NoHeadException e) {
			channel.appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							GitPlugin.getInstance().getMessage("git.merge.mergeOperation.mergeFailedNoHead"), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));			
		} catch (ConcurrentRefUpdateException e) {
			channel.appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							GitPlugin.getInstance().getMessage("git.merge.mergeOperation.mergeFailedRefUpdate"), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));			
		} catch (CheckoutConflictException e) {
			mergeResult = new MergeResult(e.getConflictingPaths());
		} catch (GitAPIException e) {
			channel.appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getLocalizedMessage(), 
							e.getCause().getMessage(),
							DisplaySimpleMessageClientCommand.ICON_ERROR));			
		} catch (Exception e) {
			channel.appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));				
		} finally {			
			monitor.done();	
//			GitPlugin.getInstance().getUtils().restoreProjectConfigFiles(repository, null);
		}		
	}
	
}
