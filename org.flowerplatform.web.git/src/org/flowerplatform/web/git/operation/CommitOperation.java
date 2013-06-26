package org.flowerplatform.web.git.operation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryState;
import org.eclipse.jgit.revwalk.RevWalk;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.progress_monitor.ProgressMonitor;
import org.flowerplatform.web.entity.User;
import org.flowerplatform.web.git.GitPlugin;
import org.flowerplatform.web.git.dto.CommitPageDto;
import org.flowerplatform.web.git.dto.CommitResourceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristina Constantinescu
 */ 
public class CommitOperation {

	private static Logger logger = LoggerFactory.getLogger(CommitOperation.class);
	
	private CommunicationChannel channel;
	private List<File> selection;
	private Repository repository;
		
	public CommitOperation(CommunicationChannel channel, List<File> selection) {
		this.channel = channel;
		this.selection = selection;
	}

	private void includeList(Set<String> added, Set<String> files) {
		for (String filename : added) {
			if (!files.contains(filename)) {
				files.add(filename);		
			}
		}
	}
	
	private String getMergeResolveMessage(Repository mergeRepository) {
		File mergeMsg = new File(mergeRepository.getDirectory(),
				Constants.MERGE_MSG);
		FileReader reader;
		try {
			reader = new FileReader(mergeMsg);
			BufferedReader br = new BufferedReader(reader);
			try {
				StringBuilder message = new StringBuilder();
				String s;
				String newLine = newLine();
				while ((s = br.readLine()) != null)
					message.append(s).append(newLine);
				return message.toString();
			} catch (IOException e) {
				throw new IllegalStateException(e);
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					// Empty
				}
			}
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	private String newLine() {
		return System.getProperty("line.separator");
	}
	
	private String getCherryPickOriginalAuthor(Repository mergeRepository) {
		try {
			ObjectId cherryPickHead = mergeRepository.readCherryPickHead();
			PersonIdent author = new RevWalk(mergeRepository).parseCommit(cherryPickHead).getAuthorIdent();
			return author.getName() + " <" + author.getEmailAddress() + ">";
		} catch (IOException e) {
			return null;
		}
	}
	
	private Repository getRepository(List<File> files) { 
		Repository repo = null;
		for (File file : files) {			
			if (repo == null) {
				repo = GitPlugin.getInstance().getUtils().getRepository(file);
			} else if (!repo.equals(GitPlugin.getInstance().getUtils().getRepository(file))) {
				return null;
			}
		}
		return repo;
	}
	
	public CommitPageDto getPageDto() {		
		ProgressMonitor monitor = ProgressMonitor.create(GitPlugin.getInstance().getMessage("git.precommit.monitor.title"), channel);
			
		try {
			monitor.beginTask(GitPlugin.getInstance().getMessage("git.precommit.monitor.message"), 4);
			
			repository = getRepository(selection);
			if (repository == null) {
				channel.appendOrSendCommand(new DisplaySimpleMessageClientCommand(
						CommonPlugin.getInstance().getMessage("error"), 
						GitPlugin.getInstance().getMessage("git.commit.errorDifferentRepositories"), 
						DisplaySimpleMessageClientCommand.ICON_ERROR));
				return null;
			}
					
			RepositoryState state = repository.getRepositoryState();			
			if (!state.canCommit()) {
				channel.appendOrSendCommand(new DisplaySimpleMessageClientCommand(
						CommonPlugin.getInstance().getMessage("error"), 
						GitPlugin.getInstance().getMessage("git.commit.repositoryState", new Object[] {state.getDescription()}), 
						DisplaySimpleMessageClientCommand.ICON_ERROR));			
				return null;
			}
			
			boolean isMergedResolved = false;
			boolean isCherryPickResolved = false;
			
			if (state.equals(RepositoryState.MERGING_RESOLVED)) {
				isMergedResolved = true;			
			} else if (state.equals(RepositoryState.CHERRY_PICKING_RESOLVED)) {
				isCherryPickResolved = true;				
			}
					
			User user = (User) CommunicationPlugin.tlCurrentPrincipal.get().getUser();
													
			String committer = user.getName() + " <" + user.getEmail() + ">";
			String author = user.getName() + " <" + user.getEmail() + ">";	
			if (isCherryPickResolved) {
				author = getCherryPickOriginalAuthor(repository);
			}			
			if (author == null) {		
				author = user.getName() + " <" + user.getEmail() + ">";		
			}				
			String message = null;
			if (isMergedResolved || isCherryPickResolved) {
				message = getMergeResolveMessage(repository);
			}
			
			org.eclipse.jgit.api.Status repoStatus = new Git(repository).status().call();
			monitor.worked(1);
					
			if (monitor.isCanceled()) {
				 return null;
			}
			
			Set<String> files = new HashSet<String>();
			includeList(repoStatus.getAdded(), files);
			includeList(repoStatus.getChanged(), files);
			includeList(repoStatus.getRemoved(), files);
			includeList(repoStatus.getMissing(), files);
			includeList(repoStatus.getModified(), files);
			includeList(repoStatus.getUntracked(), files);
			
			List<CommitResourceDto> commitResources = new ArrayList<CommitResourceDto>();	
			for (String path : files) {					
				CommitResourceDto commitDto = new CommitResourceDto();
				commitDto.setLabel(path);
				commitDto.setPath(path);
				
				commitResources.add(commitDto);
			}
			monitor.worked(1);
			
			CommitPageDto dto = new CommitPageDto();			
			dto.setCommitResources(commitResources);
			dto.setAuthor(author);
			dto.setCommitter(committer);
			dto.setMessage(message);
			dto.setRepository(repository.getDirectory().getAbsolutePath());
			
			monitor.worked(1);

			return dto;
		} catch (Exception e) {
			logger.debug(GitPlugin.getInstance().getMessage("git.commit.error"), e);
			channel.appendOrSendCommand(new DisplaySimpleMessageClientCommand(
					CommonPlugin.getInstance().getMessage("error"), 
					GitPlugin.getInstance().getMessage("git.commit.error"),
					e.getMessage(),
					DisplaySimpleMessageClientCommand.ICON_ERROR));
			return null;
		} finally {
			monitor.done();
		}
	}
}
