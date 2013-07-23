/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.web.git.operation;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RebaseCommand;
import org.eclipse.jgit.api.RebaseResult;
import org.eclipse.jgit.lib.Repository;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.progress_monitor.ProgressMonitor;
import org.flowerplatform.communication.progress_monitor.remote.ProgressMonitorStatefulService;
import org.flowerplatform.web.git.GitPlugin;
import org.flowerplatform.web.git.GitProgressMonitor;

/**
 * @author Cristina Constantinescu
 */
public class RebaseOperation {

	private Repository repository;
	private String refName;
	private CommunicationChannel channel;
	private RebaseResult rebaseResult;

	public RebaseOperation(Repository repository, String refName, CommunicationChannel channel) {
		this.repository = repository;
		this.refName = refName;
		this.channel = channel;
	}
	
	public String handleRebaseResult() {
		StringBuilder sb = new StringBuilder();		
		
		sb.append("Status: ");
		sb.append(rebaseResult.getStatus());
		sb.append("\n");
		
		if (rebaseResult.getConflicts() != null) {
			sb.append("\nConflicts: ");
			sb.append("\n");
			for (String conflict : rebaseResult.getConflicts()) {
				sb.append(conflict);
				sb.append("\n");
			}
		}
		
		if (rebaseResult.getFailingPaths() != null) {
			sb.append("\nFailing paths: ");
			sb.append("\n");
			for (String path : rebaseResult.getFailingPaths().keySet()) {
				sb.append(path);
				sb.append(" -> ");
				sb.append(rebaseResult.getFailingPaths().get(path).toString());
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	private boolean refreshNeeded() {
		if (rebaseResult == null)
			return true;
		if (rebaseResult.getStatus() == RebaseResult.Status.UP_TO_DATE)
			return false;
		return true;
	}
	
	public void execute() throws Exception {
		ProgressMonitor monitor = ProgressMonitor.create(GitPlugin.getInstance().getMessage("git.rebase"), channel);
			
		monitor.beginTask(GitPlugin.getInstance().getMessage("git.rebase.title", new Object[] {refName}), 2);
		
//		IProject[] validProjects = GitPlugin.getInstance().getGitUtils().getValidProjects(repository);
//		GitPlugin.getInstance().getGitUtils().backupProjectConfigFiles(null, validProjects);
		
		RebaseCommand cmd = new Git(repository).rebase().setProgressMonitor(new GitProgressMonitor(monitor));
		
		try {
			cmd.setUpstream(refName);
			rebaseResult = (RebaseResult) GitPlugin.getInstance().getUtils().runGitCommandInUserRepoConfig(repository, cmd);
			
		} catch (Exception e) {
			throw e;
		} finally {
//			if (refreshNeeded()) {
//				GitPlugin.getInstance().getGitUtils().refreshValidProjects(validProjects, new SubProgressMonitor(monitor, 1));
//			}
//			GitPlugin.getInstance().getGitUtils().restoreProjectConfigFiles(repository, null);
			monitor.done();
		}
	}
	
}