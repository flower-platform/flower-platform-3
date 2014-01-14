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
package org.flowerplatform.web.git.history.remote;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringEscapeUtils;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CheckoutResult;
import org.eclipse.jgit.api.CherryPickCommand;
import org.eclipse.jgit.api.CherryPickResult;
import org.eclipse.jgit.api.CherryPickResult.CherryPickStatus;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.RevertCommand;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.BranchConfig;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectIdRef;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Ref.Storage;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.lib.RepositoryCache.FileKey;
import org.eclipse.jgit.lib.RepositoryState;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.RevWalkUtils;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.AndTreeFilter;
import org.eclipse.jgit.treewalk.filter.PathFilterGroup;
import org.eclipse.jgit.treewalk.filter.TreeFilter;
import org.eclipse.jgit.util.FS;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.channel.ICommunicationChannelLifecycleListener;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.progress_monitor.ProgressMonitor;
import org.flowerplatform.communication.stateful_service.IStatefulClientLocalState;
import org.flowerplatform.communication.stateful_service.IStatefulServiceMXBean;
import org.flowerplatform.communication.stateful_service.RegularStatefulService;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.web.git.GitPlugin;
import org.flowerplatform.web.git.GitUtils;
import org.flowerplatform.web.git.explorer.entity.RefNode;
import org.flowerplatform.web.git.history.internal.FileDiff;
import org.flowerplatform.web.git.history.internal.WebCommit;
import org.flowerplatform.web.git.history.internal.WebCommitList;
import org.flowerplatform.web.git.history.internal.WebCommitPlotRenderer;
import org.flowerplatform.web.git.history.internal.WebWalk;
import org.flowerplatform.web.git.history.remote.dto.HistoryEntryDto;
import org.flowerplatform.web.git.history.remote.dto.HistoryFileDiffEntryDto;
import org.flowerplatform.web.git.history.remote.dto.HistoryViewInfoDto;
import org.flowerplatform.web.git.operation.MergeOperation;
import org.flowerplatform.web.git.operation.RebaseOperation;
import org.flowerplatform.web.git.operation.ResetOperation;
import org.flowerplatform.web.git.remote.dto.CommitDto;
import org.flowerplatform.web.projects.remote.ProjectsService;

/**
 *	@author Cristina Constantinescu
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class GitHistoryStatefulService extends RegularStatefulService<CommunicationChannel, HistoryViewInfoDto> implements IStatefulServiceMXBean, ICommunicationChannelLifecycleListener {

	public static final String SERVICE_ID = "GitHistoryStatefulService";
	
	private static final String STATEFUL_CLIENT_ID = "git_history";
	
	public GitHistoryStatefulService() {
		super();
		clients = new HashMap<CommunicationChannel, HistoryViewInfoDto>();
	}

	///////////////////////////////////////////////////////////////
	// JMX Methods
	///////////////////////////////////////////////////////////////

	public Collection<String> getStatefulClientIdsForCommunicationChannel(CommunicationChannel communicationChannel) {
		return Collections.singleton(STATEFUL_CLIENT_ID);
	}
	
	///////////////////////////////////////////////////////////////
	// Normal methods
	///////////////////////////////////////////////////////////////
	
	@Override
	protected String getStatefulServiceId() {		
		return SERVICE_ID;
	}
	
	public static GitHistoryStatefulService getInstance() {		
		return (GitHistoryStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(SERVICE_ID);
	}
	
	@Override
	protected HistoryViewInfoDto getDataFromStatefulClientLocalState(StatefulServiceInvocationContext context, IStatefulClientLocalState statefulClientLocalState) {
		// channel destroyed
		if (statefulClientLocalState == null) {
			return null;
		}
		return ((GitHistoryStatefulClientLocalState) statefulClientLocalState).getInfo();
	}

	@Override
	public void communicationChannelCreated(CommunicationChannel webCommunicationChannel) {
		// do nothing
	}

	@Override
	public void communicationChannelDestroyed(CommunicationChannel webCommunicationChannel) {
		unsubscribe(new StatefulServiceInvocationContext(webCommunicationChannel, null, null), null);
	}
	
	private WebWalk getWebWalk(CommunicationChannel channel, HistoryViewInfoDto info) throws IOException {
		configObjectInfo(channel, info);
	
		String path = null;
		if (info.getFile() != null) {
			IPath workdirPath = new Path(info.getRepository().getWorkTree().getPath());
			int segmentCount = workdirPath.segmentCount();
			IPath filePath = null;
			if (info.getFilter() == HistoryViewInfoDto.SHOWALLFOLDER) {
				filePath = new Path(info.getFile().getParentFile().getPath());			
			} else  if (info.getFilter() == HistoryViewInfoDto.SHOWALLRESOURCE) {
				filePath = new Path(info.getFile().getPath());				
			} else if (info.getFilter() == HistoryViewInfoDto.SHOWALLPROJECT) {
				IResource res = ProjectsService.getInstance().getProjectWrapperResourceFromFile(info.getFile());
				if (res != null) {
					filePath = new Path(res.getProject().getFullPath().toFile().getPath());
				}
			}
			if (filePath != null) {
				path = filePath.removeFirstSegments(segmentCount).setDevice(null).toString();
			}			
		}									
		WebWalk walk = new WebWalk(info.getRepository());						
		setupWalk(walk, info.getRepository(), path);
		
		info.setPath(path);
		
		return walk;
	}
		
	private void setupWalk(WebWalk walk, Repository repo, String path) throws IOException {
		walk.addAdditionalRefs(repo.getRefDatabase().getAdditionalRefs());
		walk.addAdditionalRefs(repo.getRefDatabase().getRefs(Constants.R_NOTES).values());
	
		walk.sort(RevSort.COMMIT_TIME_DESC, true);
		walk.sort(RevSort.BOUNDARY, true);
		walk.setRetainBody(false);
			
		setWalkStartPoints(walk, repo, walk.parseCommit(repo.resolve(Constants.HEAD)));		
		
		createFileWalker(walk, repo, path);		
	}
	
	private TreeWalk createFileWalker(RevWalk walk, Repository db, String path) {
		TreeWalk fileWalker = new TreeWalk(db);
		fileWalker.setRecursive(true);
		fileWalker.setFilter(TreeFilter.ANY_DIFF);
		
		if (path == null || path.isEmpty()) {
			walk.setTreeFilter(TreeFilter.ALL);
		} else {
			List<String> stringPaths = new ArrayList<String>(1);
			stringPaths.add(path);
	
			walk.setTreeFilter(AndTreeFilter.create(PathFilterGroup
					.createFromStrings(stringPaths), TreeFilter.ANY_DIFF));
		}
		return fileWalker;
	}
	
	private String getCommitMessage(Repository repo, HistoryEntryDto entry, RevCommit commit) throws IOException {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int maxBranches = 20;
		
		StringBuilder d = new StringBuilder();			
		d.append("<p>");
		d.append(GitPlugin.getInstance().getMessage("git.history.commitMessage.commit"));
		d.append(" ");
		d.append(commit.getId().name());
		d.append("<br>");

		PersonIdent author = commit.getAuthorIdent();		
		if (author != null) {
			d.append(GitPlugin.getInstance().getMessage("git.history.commitMessage.author"));
			d.append(": ");
			d.append(author.getName());
			d.append(" &lt;");
			d.append(author.getEmailAddress());
			d.append("&gt; ");
			d.append(fmt.format(author.getWhen()));
			d.append("<br>");
		}
		
		PersonIdent committer = commit.getCommitterIdent();
		if (committer != null) {
			d.append(GitPlugin.getInstance().getMessage("git.history.commitMessage.committer"));
			d.append(": ");
			d.append(committer.getName());
			d.append(" &lt;");
			d.append(committer.getEmailAddress());
			d.append("&gt; ");
			d.append(fmt.format(committer.getWhen()));
			d.append("<br>");
		}

		for (CommitDto parent : entry.getCommitMessage().getParents()) {			
			d.append(GitPlugin.getInstance().getMessage("git.history.commitMessage.parent"));
			d.append(": ");
			d.append(String.format("<u><a href=''>%s</a></u>", parent.getId()));
			d.append(" (");
			d.append(parent.getLabel());
			d.append(")");
			d.append("<br>");
		}

		for (CommitDto child : entry.getCommitMessage().getChildren()) {			
			d.append(GitPlugin.getInstance().getMessage("git.history.commitMessage.child"));
			d.append(": ");
			d.append(String.format("<u><a href=''>%s</a></u>", child.getId()));
			d.append(" (");
			d.append(child.getLabel());
			d.append(")");
			d.append("<br>");
		}

					
		List<Ref> branches = getBranches(commit, getBranches(repo), repo);
		if (!branches.isEmpty()) {
			d.append(GitPlugin.getInstance().getMessage("git.history.commitMessage.branches"));
			d.append(": ");
			int count = 0;
			for (Iterator<Ref> i = branches.iterator(); i.hasNext();) {
				Ref head = i.next();
				d.append(formatHeadRef(head));
				if (i.hasNext()) {
					if (count++ <= maxBranches) {
						d.append(", ");
					} else {
						d.append(GitPlugin.getInstance().getMessage(
								"git.history.commitMessage.author", 
								new Object[] {Integer.valueOf(branches.size() - maxBranches)}));
						break;
					}
				}
			}
			d.append("<br>");
		}
		
		String tagsString = getTagsString(repo, commit);
		if (tagsString.length() > 0) {
			d.append(GitPlugin.getInstance().getMessage("git.history.commitMessage.tags"));
			d.append(": ");
			d.append(tagsString);
			d.append("<br>");
		}
		
		d.append("<br>");		
		d.append(String.format("<b>%s</b>", StringEscapeUtils.escapeHtml3(commit.getFullMessage()).replaceAll("\n", "<br>")));
		d.append("</p>");
		
		return d.toString();
	}
	
	private List<RefNode> getRefNodes(RevCommit commit, Repository repo, String refPrefix) {
		List<Ref> availableBranches = new ArrayList<Ref>();
		List<RefNode> nodes = new ArrayList<RefNode>();
		try {
			Map<String, Ref> branches = repo.getRefDatabase().getRefs(refPrefix);
			for (Ref branch : branches.values()) {
				if (branch.getLeaf().getObjectId().equals(commit.getId()))
					availableBranches.add(branch);
			}			
			for (Ref ref : availableBranches)
				nodes.add(new RefNode(repo, ref));

		} catch (IOException e) {
			// ignore here
		}
		return nodes;
	}
	
	private List<Ref> getBranches(RevCommit commit, Collection<Ref> allRefs, Repository db)
			throws MissingObjectException, IncorrectObjectTypeException,
			IOException {
		RevWalk revWalk = new RevWalk(db);
		try {
			revWalk.setRetainBody(false);
			return RevWalkUtils.findBranchesReachableFrom(commit, revWalk, allRefs);
		} finally {
			revWalk.dispose();
		}
	}
	
	private List<Ref> getBranches(Repository repo) throws IOException  {
		List<Ref> ref = new ArrayList<Ref>();
		
		ref.addAll(repo.getRefDatabase().getRefs(Constants.R_HEADS).values());
		ref.addAll(repo.getRefDatabase().getRefs(Constants.R_REMOTES).values());
		
		return ref;
	}
	
	private String formatHeadRef(Ref ref) {
		String name = ref.getName();
		if (name.startsWith(Constants.R_HEADS)) {
			return name.substring(Constants.R_HEADS.length());
		} else if (name.startsWith(Constants.R_REMOTES)) {
			return name.substring(Constants.R_REMOTES.length());
		}
		return name;
	}
	
	private String getTagsString(Repository repo, RevCommit commit) {
		StringBuilder sb = new StringBuilder();
		Map<String, Ref> tagsMap = repo.getTags();
		for (Entry<String, Ref> tagEntry : tagsMap.entrySet()) {
			ObjectId target = tagEntry.getValue().getPeeledObjectId();
			if (target == null) {
				target = tagEntry.getValue().getObjectId();
			}
			if (target != null && target.equals(commit)) {
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(tagEntry.getKey());
			}
		}
		return sb.toString();
	}
	
	private String getInfo(HistoryViewInfoDto objectInfo) {
		String repositoryName = GitPlugin.getInstance().getUtils().getRepositoryName(objectInfo.getRepository());
		
		if (objectInfo.getFile() == null) {
			return GitPlugin.getInstance().getMessage("git.history.repo.pattern", new Object[] {repositoryName});
		} else {
			// single file from Repository
			String path = objectInfo.getFile().getPath();
			
			String repoPath = objectInfo.getRepository().getDirectory().getParentFile().getPath();
			path = path.substring(path.indexOf(repoPath) + repoPath.length());
			
			return GitPlugin.getInstance().getMessage("git.history.file.pattern", new Object[] {path, repositoryName});
		}
	}
	
	private void setWalkStartPoints(RevWalk walk, Repository repo, AnyObjectId headId) throws IOException {	
		markStartAllRefs(repo, walk, Constants.R_HEADS);
		markStartAllRefs(repo, walk, Constants.R_REMOTES);
		markStartAllRefs(repo, walk, Constants.R_TAGS);
		
		markStartAdditionalRefs(repo, walk);
		markUninteresting(repo,  walk, Constants.R_NOTES);

		walk.markStart(walk.parseCommit(headId));		 
	}

	private void markStartAllRefs(Repository repo, RevWalk walk, String prefix)
			throws IOException, MissingObjectException,
			IncorrectObjectTypeException {
		for (Entry<String, Ref> refEntry : repo
				.getRefDatabase().getRefs(prefix).entrySet()) {
			Ref ref = refEntry.getValue();
			if (ref.isSymbolic())
				continue;
			markStartRef(repo, walk, ref);
		}
	}

	private void markStartAdditionalRefs(Repository repo, RevWalk walk) throws IOException {
		List<Ref> additionalRefs = repo.getRefDatabase()
				.getAdditionalRefs();
		for (Ref ref : additionalRefs)
			markStartRef(repo, walk, ref);
	}

	private void markStartRef(Repository repo, RevWalk walk, Ref ref) throws IOException,
			IncorrectObjectTypeException {
		try {
			Object refTarget = walk.parseAny(ref.getLeaf().getObjectId());
			if (refTarget instanceof RevCommit)
				walk.markStart((RevCommit) refTarget);
		} catch (MissingObjectException e) {
			// If there is a ref which points to Nirvana then we should simply
			// ignore this ref. We should not let a corrupt ref cause that the
			// history view is not filled at all
		}
	}

	private void markUninteresting(Repository repo, RevWalk walk, String prefix)
			throws IOException, MissingObjectException,
			IncorrectObjectTypeException {
		for (Entry<String, Ref> refEntry : repo
				.getRefDatabase().getRefs(prefix).entrySet()) {
			Ref ref = refEntry.getValue();
			if (ref.isSymbolic())
				continue;
			Object refTarget = walk.parseAny(ref.getLeaf().getObjectId());
			if (refTarget instanceof RevCommit)
				walk.markUninteresting((RevCommit) refTarget);
		}
	}

	private void configObjectInfo(CommunicationChannel channel, HistoryViewInfoDto info) throws IOException {		
		Object node = GenericTreeStatefulService.getNodeByPathFor((List<PathFragment>) info.getSelectedObject(), null);
		
		Repository repository = null;
		File file = null;
		
		if ((node instanceof Pair && ((Pair) node).a instanceof File)) {
			repository = GitPlugin.getInstance().getUtils().getRepository(((Pair<File, String>) node).a);
			file = ((Pair<File, String>) node).a;
		} else if (node instanceof RefNode) {
			repository = ((RefNode) node).getRepository();
			
			File mainRepoFile = repository.getDirectory().getParentFile();		
			String branchName = ((RefNode) node).getRef().getName().substring(Constants.R_HEADS.length());
			file = new File(mainRepoFile.getParentFile(), GitUtils.WORKING_DIRECTORY_PREFIX + branchName);			
		}
		if (repository == null) {
			throw new IOException();
		}
					
		info.setRepositoryAndFile(repository, file);
	}	
	
	private String mergeStatus(Repository repository) {
		String message = null;		
		try {
			Ref head = repository.getRef(Constants.HEAD);
			if (head == null || !head.isSymbolic()) {
				message = GitPlugin.getInstance().getMessage("git.merge.headIsNoBranch");
			}
			else if (!repository.getRepositoryState().equals(RepositoryState.SAFE))
				message = GitPlugin.getInstance().getMessage("git.merge.wrongRepositoryState", repository.getRepositoryState());
		} catch (IOException e) {
			message = e.getMessage();
		}
		return message;
	}
	
	///////////////////////////////////////////////////////////////
	// @RemoteInvocation methods
	///////////////////////////////////////////////////////////////
	
	@RemoteInvocation
	public HistoryViewInfoDto getObjectInfo(StatefulServiceInvocationContext context, HistoryViewInfoDto info) {
		try {
			configObjectInfo(context.getCommunicationChannel(), info);
			
			info.setRepositoryLocation(info.getRepository().getDirectory().getAbsolutePath());
			info.setInfo(getInfo(info));
			
			return info;
		} catch (IOException e) {			
			return null;
		}
	}
	
	@RemoteInvocation
	public List<Object> getLogEntries(StatefulServiceInvocationContext context, HistoryViewInfoDto info) {
		try {
			WebWalk walk = getWebWalk(context.getCommunicationChannel(), info);
		
			WebCommitList loadedCommits = new WebCommitList();
			loadedCommits.source(walk);
		
			loadedCommits.fillTo(10000);
			WebCommit[] commitsAsArray = new WebCommit[loadedCommits.size()];
			loadedCommits.toArray(commitsAsArray);
			
			List<HistoryEntryDto> result = new ArrayList<HistoryEntryDto>();
			for (WebCommit commit : commitsAsArray) {
				commit.parseBody();
				
				HistoryEntryDto entry = new HistoryEntryDto();
				
				entry.setId(commit.getId().name());
				entry.setShortId(commit.getId().abbreviate(7).name());
				entry.setMessage(commit.getShortMessage());
				
				PersonIdent person = commit.getAuthorIdent();
				entry.setAuthor(person.getName());
				entry.setAuthorEmail(person.getEmailAddress());
				entry.setAuthoredDate(person.getWhen());
				
				person = commit.getCommitterIdent();
				entry.setCommitter(person.getName());
				entry.setCommitterEmail(person.getEmailAddress());
				entry.setCommitteredDate(person.getWhen());
				
				WebCommitPlotRenderer renderer = new WebCommitPlotRenderer(commit);
				renderer.paint();
				
				entry.setSpecialMessage(renderer.getSpecialMessage());
				entry.setDrawings(renderer.getDrawings());
				
				for (int i = 0; i < commit.getParentCount(); i++) {
					WebCommit p = (WebCommit)commit.getParent(i);
					p.parseBody();
					
					CommitDto parent = new CommitDto();
					parent.setId(p.getId().name());
					parent.setLabel(p.getShortMessage());
					entry.getCommitMessage().getParents().add(parent);					
				}

				for (int i = 0; i < commit.getChildCount(); i++) {
					WebCommit p = (WebCommit)commit.getChild(i);
					p.parseBody();
					
					CommitDto child = new CommitDto();
					child.setId(p.getId().name());
					child.setLabel(p.getShortMessage());
					entry.getCommitMessage().getChildren().add(child);		
				}
				
				result.add(entry);
			}
			
			walk.dispose();
			
			return Arrays.asList(new Object[] {result, info});
		} catch (IOException e) {
			return null;
		}		
	}
	
	@RemoteInvocation
	public List<HistoryFileDiffEntryDto> getCommitFileDiffs(StatefulServiceInvocationContext context, HistoryEntryDto entry, HistoryViewInfoDto info) {
		List<HistoryFileDiffEntryDto> entries =new ArrayList<HistoryFileDiffEntryDto>();
		try {			
			Repository repo = RepositoryCache.open(FileKey.exact(new File(info.getRepositoryLocation()), FS.DETECTED));
			RevWalk walk = new RevWalk(repo);
			
			RevCommit commit = walk.parseCommit(repo.resolve(entry.getId()));		
			for (RevCommit parent : commit.getParents()) {
				walk.parseBody(parent);
			}
			FileDiff[] fileDiffs = FileDiff.compute(createFileWalker(new WebWalk(repo), repo, info.getPath()), commit, TreeFilter.ALL);
			for (FileDiff fd : fileDiffs) {
				HistoryFileDiffEntryDto fileEntry = new HistoryFileDiffEntryDto();
				fileEntry.setFile(fd.getLabel(fd));				
				//fileEntry.setImage(GitPlugin.getInstance().getGitUtils().getImageURL(fd.getImageDescriptor(fd)));	
				
				entries.add(fileEntry);
			}
			walk.dispose();
		} catch (Exception e) {
			context.getCommunicationChannel().sendCommandWithPush(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
			return null;
		} 
		return entries;		
	}
	
	@RemoteInvocation
	public String getCommitMessage(StatefulServiceInvocationContext context, HistoryEntryDto entry, String repositoryLocation) {
		try {
			Repository repo = RepositoryCache.open(FileKey.exact(new File(repositoryLocation), FS.DETECTED));
			RevCommit commit = new RevWalk(repo).parseCommit(repo.resolve(entry.getId()));
			
			return getCommitMessage(repo, entry, commit);
		} catch (Exception e) {
			context.getCommunicationChannel().sendCommandWithPush(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
			return null;
		}
	}
	
	@RemoteInvocation
	public boolean checkout(StatefulServiceInvocationContext context, HistoryEntryDto entry, String repositoryLocation) {
		try {
			Repository repo = RepositoryCache.open(FileKey.exact(new File(repositoryLocation), FS.DETECTED));
			RevCommit commit = new RevWalk(repo).parseCommit(repo.resolve(entry.getId()));
			
			List<RefNode> nodes = getRefNodes(commit, repo, Constants.R_HEADS);

			String target;
			if (nodes.isEmpty())
				target = commit.name();
			else {
				target = nodes.get(0).getRef().getName();
			}			
			// checkout local branch
			Git git = new Git(repo);
											
			CheckoutCommand cc = git.checkout().setName(target).setForce(true);						
			cc.call();				
						
			// show checkout result
			if (cc.getResult().getStatus() == CheckoutResult.Status.CONFLICTS)
				context.getCommunicationChannel().appendOrSendCommand(
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
					if (new File(repo.getWorkTree(), path1).exists()) {
						show = true;
						break;
					}
				}
				if (show) {
					context.getCommunicationChannel().appendOrSendCommand(
							new DisplaySimpleMessageClientCommand(
									GitPlugin.getInstance().getMessage("git.checkout.nonDeletedFiles.title"), 
									GitPlugin.getInstance().getMessage("git.checkout.nonDeletedFiles.message", Repository.shortenRefName(target)),
									cc.getResult().getUndeletedList().toString(),
									DisplaySimpleMessageClientCommand.ICON_ERROR));		
				}
			} else if (cc.getResult().getStatus() == CheckoutResult.Status.OK) {
				if (ObjectId.isId(repo.getFullBranch()))
					context.getCommunicationChannel().appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(
								GitPlugin.getInstance().getMessage("git.checkout.detachedHead.title"), 
								GitPlugin.getInstance().getMessage("git.checkout.detachedHead.message"),
								DisplaySimpleMessageClientCommand.ICON_ERROR));		
			}			
			return true;
		} catch (Exception e) {
			context.getCommunicationChannel().sendCommandWithPush(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
			return false;
		}
	}
	
	@RemoteInvocation
	public boolean cherryPick(StatefulServiceInvocationContext context, HistoryEntryDto entry, String repositoryLocation) {
		ProgressMonitor monitor = ProgressMonitor.create(
				GitPlugin.getInstance().getMessage("git.cherryPick.monitor.title", new Object[] {entry.getShortId()}), 
				context.getCommunicationChannel());
				
		try {
			Repository repo = RepositoryCache.open(FileKey.exact(new File(repositoryLocation), FS.DETECTED));
			RevCommit commit = new RevWalk(repo).parseCommit(repo.resolve(entry.getId()));
			
			CherryPickCommand command = new Git(repo).cherryPick().include(commit.getId());			
			CherryPickResult result = (CherryPickResult) GitPlugin.getInstance().getUtils().runGitCommandInUserRepoConfig(repo, command);
			
//			ProjectUtil.refreshValidProjects(
//					ProjectUtil.getValidOpenProjects(repo),
//					new SubProgressMonitor(monitor, 1));
			
			RevCommit newHead = result.getNewHead();
			if (newHead != null && result.getCherryPickedRefs().isEmpty()) {
				context.getCommunicationChannel().sendCommandWithPush(
						new DisplaySimpleMessageClientCommand(
								GitPlugin.getInstance().getMessage("git.cherryPick.noCherryPickPerformed.title"), 
								GitPlugin.getInstance().getMessage("git.cherryPick.noCherryPickPerformed.message"), 
								DisplaySimpleMessageClientCommand.ICON_ERROR));
				return false;
			}
			if (newHead == null) {
				CherryPickStatus status = result.getStatus();
				switch (status) {
				case CONFLICTING:
					context.getCommunicationChannel().sendCommandWithPush(
							new DisplaySimpleMessageClientCommand(
									GitPlugin.getInstance().getMessage("git.cherryPick.cherryPickConflicts.title"), 
									GitPlugin.getInstance().getMessage("git.cherryPick.cherryPickConflicts.message"), 
									DisplaySimpleMessageClientCommand.ICON_ERROR));	
					return false;
				case FAILED:
					context.getCommunicationChannel().sendCommandWithPush(
							new DisplaySimpleMessageClientCommand(
									CommonPlugin.getInstance().getMessage("error"), 
									GitPlugin.getInstance().getMessage("git.cherryPick.cherryPickFailed.message"), 
									DisplaySimpleMessageClientCommand.ICON_ERROR));	
					return false;
				case OK:
					break;
				}
			}			
			return true;
		} catch (Exception e) {		
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			context.getCommunicationChannel().sendCommandWithPush(
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
	public boolean revert(StatefulServiceInvocationContext context, HistoryEntryDto entry, String repositoryLocation) {
		ProgressMonitor monitor = ProgressMonitor.create(
				GitPlugin.getInstance().getMessage("git.revert.monitor.title", new Object[] {entry.getShortId()}), 
				context.getCommunicationChannel());
	
		try {
			Repository repo = RepositoryCache.open(FileKey.exact(new File(repositoryLocation), FS.DETECTED));
			RevCommit commit = new RevWalk(repo).parseCommit(repo.resolve(entry.getId()));
			
			RevertCommand command = new Git(repo).revert().include(commit);
			RevCommit newHead = (RevCommit) GitPlugin.getInstance().getUtils().runGitCommandInUserRepoConfig(repo, command);
			
//			ProjectUtil.refreshValidProjects(
//					ProjectUtil.getValidOpenProjects(repo),
//					new SubProgressMonitor(monitor, 1));
						
			List<Ref> revertedRefs = command.getRevertedRefs();
			if (newHead != null && revertedRefs.isEmpty()) {
				context.getCommunicationChannel().sendCommandWithPush(
						new DisplaySimpleMessageClientCommand(
								GitPlugin.getInstance().getMessage("git.revert.noRevert.title"), 
								GitPlugin.getInstance().getMessage("git.revert.alreadyReverted.message"), 
								DisplaySimpleMessageClientCommand.ICON_ERROR));	
				return false;
			}
			if (newHead == null) {
				context.getCommunicationChannel().sendCommandWithPush(
						new DisplaySimpleMessageClientCommand(
								GitPlugin.getInstance().getMessage("git.revert.failed"), 
								command.getFailingResult().toString(), 
								DisplaySimpleMessageClientCommand.ICON_ERROR));	
				return false;
			}
			return true;
		} catch (Exception e) {
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			context.getCommunicationChannel().sendCommandWithPush(
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
	public boolean reset(StatefulServiceInvocationContext context, HistoryEntryDto entry, String repositoryLocation, int resetType) {
		try {	
			Repository repo = RepositoryCache.open(FileKey.exact(new File(repositoryLocation), FS.DETECTED));
			RevCommit revCommit = new RevWalk(repo).parseCommit(repo.resolve(entry.getId()));
			
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
			
			new ResetOperation(repo, revCommit.getName(), type, context.getCommunicationChannel()).execute();
						
			return true;
		} catch (Exception e) {		
			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
			context.getCommunicationChannel().sendCommandWithPush(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));	
			return false;
		}
	}
	
	@RemoteInvocation
	public boolean merge(StatefulServiceInvocationContext context, HistoryEntryDto entry, String repositoryLocation) {		
		try {
			Repository repo = RepositoryCache.open(FileKey.exact(new File(repositoryLocation), FS.DETECTED));
			RevCommit commit = new RevWalk(repo).parseCommit(repo.resolve(entry.getId()));
					
			String status = mergeStatus(repo);
			if (status != null) {
				context.getCommunicationChannel().sendCommandWithPush(
						new DisplaySimpleMessageClientCommand(
								CommonPlugin.getInstance().getMessage("error"), 
								status, 
								DisplaySimpleMessageClientCommand.ICON_ERROR));
				return false;
			}
			
			List<RefNode> nodes = getRefNodes(commit, repo, Constants.R_HEADS);

			String target;
			if (nodes.isEmpty()) {
				target = commit.name();
			} else {
				target = nodes.get(0).getRef().getName();
			}
			
			MergeOperation op = new MergeOperation(repo, target, false, context.getCommunicationChannel());
			
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
			context.getCommunicationChannel().sendCommandWithPush(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		}
	}
	
	@RemoteInvocation
	public boolean rebase(StatefulServiceInvocationContext context, HistoryEntryDto entry, String repositoryLocation) {		
		try {
			Repository repo = RepositoryCache.open(FileKey.exact(new File(repositoryLocation), FS.DETECTED));	
						
			if (!repo.getFullBranch().startsWith(Constants.R_HEADS)) {
				context.getCommunicationChannel().sendCommandWithPush(
						new DisplaySimpleMessageClientCommand(
								CommonPlugin.getInstance().getMessage("error"), 
								GitPlugin.getInstance().getMessage("git.rebase.noLocalBranch"), 
								DisplaySimpleMessageClientCommand.ICON_ERROR));
				return false;
			}
			
			String currentBranch = repo.getBranch();
			RevCommit commit = new RevWalk(repo).parseCommit(repo.resolve(entry.getId()));
			
			Ref ref;
			List<RefNode> nodes = getRefNodes(commit, repo, Constants.R_HEADS);
			if (nodes.size() == 0)
				ref = new ObjectIdRef.Unpeeled(Storage.LOOSE, commit.getName(), commit);
			else if (nodes.size() == 1)
				ref = nodes.get(0).getRef();
			else {
				BranchConfig branchConfig = new BranchConfig(repo.getConfig(), currentBranch);
				String trackingBranch = branchConfig.getTrackingBranch();
				Ref remoteRef = null;

				for (int i = 0; i < nodes.size(); i++) {
					Ref obj = nodes.get(i).getRef();
					if (trackingBranch != null && trackingBranch.equals(obj.getName())) {
						ref = obj;
						break;
					}
					if (obj.getName().startsWith(Constants.R_REMOTES)) {
						remoteRef = obj;					
					}
				}

				if (remoteRef != null) {
					ref = remoteRef;
				} else {
					// We tried to pick a nice ref, just pick the first then
					ref = nodes.get(0).getRef();
				}
			}
			
			RebaseOperation op = new RebaseOperation(repo, ref.getName(), context.getCommunicationChannel());
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
			context.getCommunicationChannel().sendCommandWithPush(
					new DisplaySimpleMessageClientCommand(
							CommonPlugin.getInstance().getMessage("error"), 
							e.getMessage(), 
							DisplaySimpleMessageClientCommand.ICON_ERROR));
			return false;
		}
	}
			
}