package org.flowerplatform.web.git.operation;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jgit.api.errors.DetachedHeadException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.progress_monitor.ProgressMonitor;
import org.flowerplatform.web.git.GitPlugin;
import org.flowerplatform.web.git.GitProgressMonitor;
import org.flowerplatform.web.git.GitService;
import org.flowerplatform.web.git.operation.internal.PullCommand;
import org.flowerplatform.web.git.operation.internal.PullResult;

/**
 * @author Cristina Constantinescu
 */
public class PullOperation {
	
	private Repository repository;
	private Ref ref;
	private CommunicationChannel channel;
	
	private Map<Repository, Object> results = new LinkedHashMap<Repository, Object>();

	private int timeout = 30;

	public PullOperation(Ref ref, Repository repository, CommunicationChannel channel) {
		this.ref = ref;
		this.repository = repository;
		this.channel = channel;
	}

	public boolean execute() {		
		ProgressMonitor monitor = ProgressMonitor.create(GitPlugin.getInstance().getMessage("git.action.pull.label"), channel);
		
		try {			
			monitor.beginTask(GitPlugin.getInstance().getMessage("git.pull.monitor.message", 
					new Object[] {GitPlugin.getInstance().getUtils().getRepositoryName(repository)}), 2);
	
			PullCommand pull = new PullCommand(repository);
			PullResult pullResult = null;
			
			pull.setProgressMonitor(new GitProgressMonitor(new SubProgressMonitor(monitor, 1)));
			pull.setTimeout(timeout);		
			pullResult = pull.call();
			results.put(repository, pullResult);
			
			monitor.worked(1);
			
			String result = GitPlugin.getInstance().getUtils().handleFetchResult(pullResult.getFetchResult()) + "\n" 
								+ GitPlugin.getInstance().getUtils().handleMergeResult(pullResult.getMergeResult());
			
			channel.appendOrSendCommand(new DisplaySimpleMessageClientCommand(
					GitPlugin.getInstance().getMessage("git.pull.result"), 
					result,
					DisplaySimpleMessageClientCommand.ICON_INFORMATION));
			
			return true;
		} catch (DetachedHeadException e) {
			channel.appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							GitPlugin.getInstance().getMessage("git.pull.detachedHead"), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
			return false;
		} catch (InvalidConfigurationException e) {
			channel.appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							GitPlugin.getInstance().getMessage("git.pull.notConfigured"), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
			return false;
		} catch (Exception e) {
			if (GitPlugin.getInstance().getUtils().isAuthentificationException(e)) {
				((GitService) CommunicationPlugin.getInstance().getServiceRegistry().getService("gitService")).openLoginWindow();				
				return true;
			}
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
