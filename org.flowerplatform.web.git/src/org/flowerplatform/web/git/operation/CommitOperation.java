package org.flowerplatform.web.git.operation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.lib.RepositoryCache.FileKey;
import org.eclipse.jgit.lib.RepositoryState;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.util.FS;
import org.eclipse.jgit.util.RawParseUtils;
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
		
	private void addUntracked(Collection<String> notTracked, Repository repo) throws Exception {
		if (notTracked == null || notTracked.size() == 0)
			return;
		AddCommand addCommand = new Git(repo).add();		
		for (String path : notTracked) {
			addCommand.addFilepattern(path);
		}	
		addCommand.call();			
	}
	
	public boolean commit(String repositoryLocation, List<CommitResourceDto> files, String author, String committer, String message, boolean amending) {		
		ProgressMonitor monitor = ProgressMonitor.create(GitPlugin.getInstance().getMessage("git.commit.monitor.title"), channel);
			
		try {
			Repository repo = RepositoryCache.open(FileKey.exact(new File(repositoryLocation), FS.DETECTED));
			
			Collection<String> notTracked = new HashSet<String>(); 	
			Collection<String> resources = new HashSet<String>();
			
			for (CommitResourceDto file : files) {
				resources.add(file.getPath());
				if (file.getState() == CommitResourceDto.UNTRACKED) {
					notTracked.add(file.getPath());
				}
			}
							
			monitor.beginTask(GitPlugin.getInstance().getMessage("git.commit.monitor.message"), 10);
			addUntracked(notTracked, repo);
			monitor.worked(1);
			
			CommitCommand commitCommand = new Git(repo).commit();			
			commitCommand.setAmend(amending).setMessage(message);						
						
			for (String path : resources) {
				commitCommand.setOnly(path);
			}
			
			Date commitDate = new Date();
			TimeZone timeZone = TimeZone.getDefault();

			PersonIdent enteredAuthor = RawParseUtils.parsePersonIdent(author);
			PersonIdent enteredCommitter = RawParseUtils.parsePersonIdent(committer);
			if (enteredAuthor == null) {
				channel.appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(
								CommonPlugin.getInstance().getMessage("error"), 
								GitPlugin.getInstance().getMessage("git.commit.errorParsingPersonIdent", new Object[] {author}), 
								DisplaySimpleMessageClientCommand.ICON_ERROR));	
				return false;
			}				
			if (enteredCommitter == null) {
				channel.appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(
								CommonPlugin.getInstance().getMessage("error"), 
								GitPlugin.getInstance().getMessage("git.commit.errorParsingPersonIdent", new Object[] {committer}), 
								DisplaySimpleMessageClientCommand.ICON_ERROR));	
				return false;
			}
		
			PersonIdent authorIdent = new PersonIdent(enteredAuthor, commitDate, timeZone);
			PersonIdent committerIdent = new PersonIdent(enteredCommitter, commitDate, timeZone);

			if (amending) {				
				RevCommit headCommit = GitPlugin.getInstance().getUtils().getHeadCommit(repo);
				if (headCommit != null) {
					PersonIdent headAuthor = headCommit.getAuthorIdent();
					authorIdent = new PersonIdent(enteredAuthor, headAuthor.getWhen(), headAuthor.getTimeZone());
				}
			}
			commitCommand.setAuthor(authorIdent);
			commitCommand.setCommitter(committerIdent);
			
			monitor.worked(1);			
			commitCommand.call();
			if (monitor.isCanceled()) {
				return false;
			}
			monitor.worked(8);
			
//			GitLightweightDecorator.refresh();
//			
//			updateDispatcher.dispatchContentUpdate(null, repo, GitTreeUpdateDispatcher.COMMIT, null);
		
			return true;
		} catch (Exception e) {
			logger.debug(GitPlugin.getInstance().getMessage("git.commit.error"), e);
			channel.appendOrSendCommand(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							GitPlugin.getInstance().getMessage("git.commit.error"),
							e.getMessage(),
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
			return false;
		} finally {			
			monitor.done();
		}
	}
}
