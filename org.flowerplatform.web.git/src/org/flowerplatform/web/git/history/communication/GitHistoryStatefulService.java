package org.flowerplatform.web.git.history.communication;

import org.apache.commons.lang3.StringEscapeUtils;
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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
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
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.channel.ICommunicationChannelLifecycleListener;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.stateful_service.IStatefulClientLocalState;
import org.flowerplatform.communication.stateful_service.IStatefulServiceMXBean;
import org.flowerplatform.communication.stateful_service.RegularStatefulService;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.web.git.GitPlugin;
import org.flowerplatform.web.git.dto.CommitDto;
import org.flowerplatform.web.git.entity.GitNode;
import org.flowerplatform.web.git.entity.GitNodeType;
import org.flowerplatform.web.git.entity.RepositoryNode;
import org.flowerplatform.web.git.history.dto.HistoryEntryDto;
import org.flowerplatform.web.git.history.dto.HistoryFileDiffEntryDto;
import org.flowerplatform.web.git.history.dto.HistoryViewInfoDto;
import org.flowerplatform.web.git.history.internal.FileDiff;
import org.flowerplatform.web.git.history.internal.WebCommit;
import org.flowerplatform.web.git.history.internal.WebCommitList;
import org.flowerplatform.web.git.history.internal.WebCommitPlotRenderer;
import org.flowerplatform.web.git.history.internal.WebWalk;

/**
 *	@author Cristina Constantinescu
 */
@SuppressWarnings({ "restriction", "rawtypes", "unchecked" })
public class GitHistoryStatefulService extends RegularStatefulService<CommunicationChannel, HistoryViewInfoDto> implements IStatefulServiceMXBean, ICommunicationChannelLifecycleListener {

	public static final String SERVICE_ID = "GitHistoryStatefulService";
	
	private static final String STATEFUL_CLIENT_ID = "Git History";
	
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
			
//		if (!info.getIsResource()) {
			String path = null;
			if (info.getFile() != null) {
				IPath workdirPath = new Path(info.getRepository().getWorkTree().getPath());
				int segmentCount = workdirPath.segmentCount();
				IPath filePath = null;
				if (info.getFilter() == HistoryViewInfoDto.SHOWALLFOLDER) {
					filePath = new Path(info.getFile().getParentFile().getPath());			
				} else /* if (showAllFilter == ShowFilter.SHOWALLRESOURCE) */{
					filePath = new Path(info.getFile().getPath());				
				}
				if (filePath != null) {
					path = filePath.removeFirstSegments(segmentCount).setDevice(null).toString();
				}			
			}									
			WebWalk walk = new WebWalk(info.getRepository());						
			setupWalk(walk, info.getRepository(), path);
			
			info.setPath(path);
			
			return walk;
//		} else {			
//			RepositoryMapping mapping = RepositoryMapping.getMapping(info.getResource());
//			
//			String path = null;
//			if (info.getFilter() == HistoryViewInfoDto.SHOWALLFOLDER) {				
//				// if the resource's parent is the workspace root, we will
//				// get nonsense from map.getRepoRelativePath(), so we
//				// check here and use the project instead
//				if (info.getResource().getParent() instanceof IWorkspaceRoot) {
//					path = mapping.getRepoRelativePath(info.getResource().getProject());
//				} else {
//					path = mapping.getRepoRelativePath(info.getResource().getParent());
//				}				
//			} else if (info.getFilter() == HistoryViewInfoDto.SHOWALLPROJECT) {
//				path = mapping.getRepoRelativePath(info.getResource().getProject());				
//			} else if (info.getFilter() == HistoryViewInfoDto.SHOWALLREPO) {
//				// nothing
//			} else /* if (showAllFilter == ShowFilter.SHOWALLRESOURCE) */{
//				path = mapping.getRepoRelativePath(info.getResource());				
//			}
//						
//			WebWalk walk = new WebWalk(info.getRepository());
//			setupWalk(walk, info.getRepository(), path);
//					
//			info.setPath(path);
//			
//			return walk;
//		}		
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
	
//	private List<RefNode> getRefNodes(RevCommit commit, Repository repo, String refPrefix) {
//		List<Ref> availableBranches = new ArrayList<Ref>();
//		List<RefNode> nodes = new ArrayList<RefNode>();
//		try {
//			Map<String, Ref> branches = repo.getRefDatabase().getRefs(refPrefix);
//			for (Ref branch : branches.values()) {
//				if (branch.getLeaf().getObjectId().equals(commit.getId()))
//					availableBranches.add(branch);
//			}
//			RepositoryNode repoNode = new RepositoryNode(null, repo);
//			for (Ref ref : availableBranches)
//				nodes.add(new RefNode(repoNode, repo, ref));
//
//		} catch (IOException e) {
//			// ignore here
//		}
//		return nodes;
//	}
	
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
		
		if (objectInfo.getResource() == null && objectInfo.getFile() == null) {
			return GitPlugin.getInstance().getMessage("git.history.repo.pattern", new Object[] {repositoryName});
		} else if (objectInfo.getResource() != null) {		
			String type;
			switch (objectInfo.getResource().getType()) {
			case IResource.PROJECT:
				type = GitPlugin.getInstance().getMessage("git.history.projectType");
				break;
			case IResource.FILE:
				type = GitPlugin.getInstance().getMessage("git.history.fileType");
				break;		
			default:
				type = GitPlugin.getInstance().getMessage("git.history.folderType");
				break;
			}
			String path = objectInfo.getResource().getFullPath().makeRelative().toString();
			if (objectInfo.getResource().getType() == IResource.FOLDER) {
				path = path + '/';
			}
			return GitPlugin.getInstance().getMessage("git.history.file.pattern", new Object[] {type, path, repositoryName});
		} else {
			// single file from Repository
			String path;
			String type;
			if (objectInfo.getFile().isDirectory()) {
				type = GitPlugin.getInstance().getMessage("git.history.folderType");
				path = objectInfo.getFile().getPath() ;
			} else {
				type = GitPlugin.getInstance().getMessage("git.history.fileType");
				path = objectInfo.getFile().getPath();
			}
			String repoPath = objectInfo.getRepository().getDirectory().getPath();
			path = path.substring(path.indexOf(repoPath) + repoPath.length());
			
			return GitPlugin.getInstance().getMessage("git.history.file.pattern", new Object[] {type, path, repositoryName});
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
		if (info.getIsResource()) {
//			IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember((String) info.getSelectedObject());			
//			RepositoryMapping mapping = RepositoryMapping.getMapping(resource);
//			
//			if (mapping == null) {
//				throw new IOException();
//			}
//			info.setResource1(resource);
//			info.setRepository1(GitPlugin.getInstance().getGitUtils().getRepository(mapping));	
		} else {
			Object node = GenericTreeStatefulService.getNodeByPathFor((List<PathFragment>) info.getSelectedObject(), null);
			if (!(node instanceof GitNode<?>)) {
				throw new IOException();
			}
			
			info.setFile1(null);
			info.setRepository1(((GitNode<?>) node).getRepository());
		}
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
	public void subscribe(StatefulServiceInvocationContext context, IStatefulClientLocalState statefulClientLocalState) {
		super.subscribe(context, statefulClientLocalState);
		
		//invokeClientMethod(context.getCommunicationChannel(), context.getStatefulClientId(), "refresh", new Object[] {((GitHistoryStatefulClientLocalState) statefulClientLocalState).getInfo()});
	}
	
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
			Repository repo = RepositoryCache.open(FileKey.exact(new File(repositoryLocation), FS.DETECTED));;
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
	
//	@RemoteInvocation
//	public boolean checkout(StatefulServiceInvocationContext context, HistoryEntryDto entry, String repositoryLocation) {
//		try {
//			Repository repo = RepositoryCache.open(FileKey.exact(new File(repositoryLocation), FS.DETECTED));;
//			RevCommit commit = new RevWalk(repo).parseCommit(repo.resolve(entry.getId()));
//			
//			List<RefNode> nodes = getRefNodes(commit, repo, Constants.R_HEADS);
//
//			String target;
//			if (nodes.isEmpty())
//				target = commit.name();
//			else {
//				target = nodes.get(0).getObject().getName();
//			}
//			
//			return new CheckoutOperation(repo, target, context.getCommunicationChannel()).execute();
//			
//		} catch (Exception e) {
//			context.getCommunicationChannel().sendObject(
//					new DisplaySimpleMessageClientCommand(
//							CommonPlugin.getInstance().getMessage("error"), 
//							e.getMessage(), 
//							DisplaySimpleMessageClientCommand.ICON_ERROR));	
//			return false;
//		}
//	}
//	
//	@RemoteInvocation
//	public boolean cherryPick(StatefulServiceInvocationContext context, HistoryEntryDto entry, String repositoryLocation) {
//		WebProgressMonitor monitor = ProgressMonitorStatefulService.getInstance().
//				createProgressMonitor(GitPlugin.getInstance().getMessage("git.cherryPick.monitor.title", new Object[] {entry.getShortId()}));
//		
//		try {
//			Repository repo = RepositoryCache.open(FileKey.exact(new File(repositoryLocation), FS.DETECTED));;
//			RevCommit commit = new RevWalk(repo).parseCommit(repo.resolve(entry.getId()));
//			
//			CherryPickCommand command = new Git(repo).cherryPick().include(commit.getId());			
//			CherryPickResult result = (CherryPickResult) GitPlugin.getInstance().getGitUtils().runGitCommandInUserRepoConfig(repo, command);
//			
//			ProjectUtil.refreshValidProjects(
//					ProjectUtil.getValidOpenProjects(repo),
//					new SubProgressMonitor(monitor, 1));
//			
//			RevCommit newHead = result.getNewHead();
//			if (newHead != null && result.getCherryPickedRefs().isEmpty()) {
//				context.getCommunicationChannel().sendObject(
//						new DisplaySimpleMessageClientCommand(
//								GitPlugin.getInstance().getMessage("git.cherryPick.noCherryPickPerformed.title"), 
//								GitPlugin.getInstance().getMessage("git.cherryPick.noCherryPickPerformed.message"), 
//								DisplaySimpleMessageClientCommand.ICON_ERROR));
//				return false;
//			}
//			if (newHead == null) {
//				CherryPickStatus status = result.getStatus();
//				switch (status) {
//				case CONFLICTING:
//					context.getCommunicationChannel().sendObject(
//							new DisplaySimpleMessageClientCommand(
//									GitPlugin.getInstance().getMessage("git.cherryPick.cherryPickConflicts.title"), 
//									GitPlugin.getInstance().getMessage("git.cherryPick.cherryPickConflicts.message"), 
//									DisplaySimpleMessageClientCommand.ICON_ERROR));	
//					return false;
//				case FAILED:
//					context.getCommunicationChannel().sendObject(
//							new DisplaySimpleMessageClientCommand(
//									CommonPlugin.getInstance().getMessage("error"), 
//									GitPlugin.getInstance().getMessage("git.cherryPick.cherryPickFailed.message"), 
//									DisplaySimpleMessageClientCommand.ICON_ERROR));	
//					return false;
//				case OK:
//					break;
//				}
//			}			
//			return true;
//		} catch (Exception e) {		
//			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
//			context.getCommunicationChannel().sendObject(
//					new DisplaySimpleMessageClientCommand(
//							CommonPlugin.getInstance().getMessage("error"), 
//							e.getMessage(), 
//							DisplaySimpleMessageClientCommand.ICON_ERROR));	
//			return false;
//		} finally {
//			monitor.done();
//		}	
//	}
//	
//	@RemoteInvocation
//	public boolean revert(StatefulServiceInvocationContext context, HistoryEntryDto entry, String repositoryLocation) {
//		WebProgressMonitor monitor = ProgressMonitorStatefulService.getInstance().
//				createProgressMonitor(GitPlugin.getInstance().getMessage("git.revert.monitor.title", new Object[] {entry.getShortId()}));
//		
//		try {
//			Repository repo = RepositoryCache.open(FileKey.exact(new File(repositoryLocation), FS.DETECTED));;
//			RevCommit commit = new RevWalk(repo).parseCommit(repo.resolve(entry.getId()));
//			
//			RevertCommand command = new Git(repo).revert().include(commit);
//			RevCommit newHead = (RevCommit) GitPlugin.getInstance().getGitUtils().runGitCommandInUserRepoConfig(repo, command);
//			
//			ProjectUtil.refreshValidProjects(
//					ProjectUtil.getValidOpenProjects(repo),
//					new SubProgressMonitor(monitor, 1));
//						
//			List<Ref> revertedRefs = command.getRevertedRefs();
//			if (newHead != null && revertedRefs.isEmpty()) {
//				context.getCommunicationChannel().sendObject(
//						new DisplaySimpleMessageClientCommand(
//								GitPlugin.getInstance().getMessage("git.revert.noRevert.title"), 
//								GitPlugin.getInstance().getMessage("git.revert.alreadyReverted.message"), 
//								DisplaySimpleMessageClientCommand.ICON_ERROR));	
//				return false;
//			}
//			if (newHead == null) {
//				context.getCommunicationChannel().sendObject(
//						new DisplaySimpleMessageClientCommand(
//								GitPlugin.getInstance().getMessage("git.revert.failed"), 
//								command.getFailingResult().toString(), 
//								DisplaySimpleMessageClientCommand.ICON_ERROR));	
//				return false;
//			}
//			return true;
//		} catch (Exception e) {
//			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
//			context.getCommunicationChannel().sendObject(
//					new DisplaySimpleMessageClientCommand(
//							CommonPlugin.getInstance().getMessage("error"), 
//							e.getMessage(), 
//							DisplaySimpleMessageClientCommand.ICON_ERROR));	
//			return false;
//		} finally {
//			monitor.done();
//		}	
//	}
//	
//	@RemoteInvocation
//	public CreateBranchPageDto populate_createBranchPage(StatefulServiceInvocationContext context, HistoryEntryDto entry, String repositoryLocation) {
//		try {
//			Repository repo = RepositoryCache.open(FileKey.exact(new File(repositoryLocation), FS.DETECTED));;
//			RevCommit commit = new RevWalk(repo).parseCommit(repo.resolve(entry.getId()));
//			
//			if (repo.isBare()) {
//				context.getCommunicationChannel().sendObject(
//						new DisplaySimpleMessageClientCommand(
//								CommonPlugin.getInstance().getMessage("error"), 
//								GitPlugin.getInstance().getMessage("git.repo.bare"), 
//								DisplaySimpleMessageClientCommand.ICON_ERROR));	
//				return null;
//			}
//			
//			CreateBranchPageDto result = new CreateBranchPageDto();		
//			result.setPrefixName(Constants.R_HEADS);						
//			result.getRefs().add(new GitRef(commit.getId().getName(), entry.getShortId()));
//			
//			return result;
//		} catch (IOException e) {
//			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
//			context.getCommunicationChannel().sendObject(
//					new DisplaySimpleMessageClientCommand(
//							CommonPlugin.getInstance().getMessage("error"), 
//							GitPlugin.getInstance().getMessage("git.page.populate.error"), 
//							DisplaySimpleMessageClientCommand.ICON_ERROR));	
//			return null;
//		}
//	}
//	
//	@RemoteInvocation
//	public boolean createBranch(StatefulServiceInvocationContext context, HistoryEntryDto entry, String repositoryLocation, String name, GitRef baseOn, boolean checkout) {
//		try {
//			Repository repo = RepositoryCache.open(FileKey.exact(new File(repositoryLocation), FS.DETECTED));;
//			
//			Git git = new Git(repo);
//		
//			Ref newBranch = git.branchCreate().
//				setName(name).
//				setStartPoint(baseOn.getName()).
//				setUpstreamMode(SetupUpstreamMode.TRACK).
//				call();
//		
//			// notify clients about changes
//			RepositoryTreeNode parent = new LocalNode(new BranchesNode(new RepositoryNode(null, repo), repo), repo);	
//			RepositoryTreeNode newNode = new RefNode(parent, repo, newBranch);
//			if (checkout) {			
//				return new CheckoutOperation(repo, ((Ref)newNode.getObject()).getName(), context.getCommunicationChannel()).execute();
//			} else {					
//				GitRepositoriesTreeStatefulService.getInstance().dispatchContentUpdate(parent);
//			}
//						
//			return true;
//		} catch (Exception e) {	
//			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
//			context.getCommunicationChannel().sendObject(
//					new DisplaySimpleMessageClientCommand(
//							CommonPlugin.getInstance().getMessage("error"), 
//							e.getMessage(), 
//							DisplaySimpleMessageClientCommand.ICON_ERROR));	
//			return false;
//		}
//	}
//	
//	@RemoteInvocation
//	public ConfigTagPageDto populate_createTagPage(StatefulServiceInvocationContext context, HistoryEntryDto entry, String repositoryLocation) {
//		try {
//			Repository repo = RepositoryCache.open(FileKey.exact(new File(repositoryLocation), FS.DETECTED));;
//			RevCommit revCommit = new RevWalk(repo).parseCommit(repo.resolve(entry.getId()));
//						
//			List<RevCommit> revCommits = new  ArrayList<RevCommit>();
//			revCommits.add(revCommit);
//			
//			List<CommitDto> commits = new ArrayList<CommitDto>();
//			for (RevCommit commit : revCommits) {				
//				CommitDto commitDto = new CommitDto();
//				commitDto.setId(commit.name());
//				commitDto.setShortId(commit.abbreviate(7).name());
//				commitDto.setLabel(commit.abbreviate(7).name() + ": " + commit.getShortMessage());
//				commits.add(commitDto);
//			}
//			
//			ConfigTagPageDto result = new ConfigTagPageDto();
//			result.setCommits(commits);
//			result.setAddEmptyLine(false);
//			result.setInitialSelectedIndex(0);
//			
//			return result;
//		} catch (IOException e) {
//			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
//			context.getCommunicationChannel().sendObject(
//					new DisplaySimpleMessageClientCommand(
//							CommonPlugin.getInstance().getMessage("error"), 
//							GitPlugin.getInstance().getMessage("git.page.populate.error"), 
//							DisplaySimpleMessageClientCommand.ICON_ERROR));	
//			return null;
//		}
//	}
//
//	@RemoteInvocation
//	public boolean createTag(StatefulServiceInvocationContext context, HistoryEntryDto entry, String repositoryLocation, String name, String message, CommitDto commit) {
//		try {
//			Repository repo = RepositoryCache.open(FileKey.exact(new File(repositoryLocation), FS.DETECTED));;								
//			Git git = new Git(repo);
//		
//			TagCommand tag = git.tag().setName(name).setMessage(message);
//			if (commit != null) {
//				ObjectId objectId = repo.resolve(commit.getId());
//				RevCommit revCommit = new RevWalk(repo).lookupCommit(objectId);
//				
//				tag.setObjectId(revCommit);
//			}
//			tag.call();
//			
//			RepositoryTreeNode parent = new TagsNode(new RepositoryNode(null, repo), repo);			
//			GitRepositoriesTreeStatefulService.getInstance().dispatchContentUpdate(parent);
//
//			return true;
//		} catch (GitAPIException e) {		
//			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
//			context.getCommunicationChannel().sendObject(
//					new DisplaySimpleMessageClientCommand(
//							CommonPlugin.getInstance().getMessage("error"), 
//							e.getMessage(), 
//							DisplaySimpleMessageClientCommand.ICON_ERROR));	
//			return false;
//		} catch (IOException e1) {
//			logger.debug(CommonPlugin.getInstance().getMessage("error"), e1);
//			context.getCommunicationChannel().sendObject(
//					new DisplaySimpleMessageClientCommand(
//							CommonPlugin.getInstance().getMessage("error"), 
//							GitPlugin.getInstance().getMessage("git.createTagPage.error"), 
//							DisplaySimpleMessageClientCommand.ICON_ERROR));	
//			return false;
//		}	
//	}
//	
//	@RemoteInvocation
//	public boolean reset(StatefulServiceInvocationContext context, HistoryEntryDto entry, String repositoryLocation, int resetType) {
//		try {	
//			Repository repo = RepositoryCache.open(FileKey.exact(new File(repositoryLocation), FS.DETECTED));;
//			RevCommit revCommit = new RevWalk(repo).parseCommit(repo.resolve(entry.getId()));
//			
//			ResetType type;
//			switch (resetType) {
//				case 0:
//					type = ResetType.SOFT;
//					break;
//				case 1:
//					type = ResetType.MIXED;
//					break;
//				default:
//					type = ResetType.HARD;
//			}
//			
//			new ResetOperation(repo, revCommit.getName(), type).execute();
//						
//			return true;
//		} catch (Exception e) {		
//			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
//			context.getCommunicationChannel().sendObject(
//					new DisplaySimpleMessageClientCommand(
//							CommonPlugin.getInstance().getMessage("error"), 
//							e.getMessage(), 
//							DisplaySimpleMessageClientCommand.ICON_ERROR));	
//			return false;
//		}
//	}
//	
//	@RemoteInvocation
//	public ConfigFetchPushPageDto populate_pushCommitPage(StatefulServiceInvocationContext context, String repositoryLocation) {
//		try {
//			Repository repo = RepositoryCache.open(FileKey.exact(new File(repositoryLocation), FS.DETECTED));;
//					
//			return GitPlugin.getInstance().getGitUtils().getConfigFetchPushPageDto(repo, false);
//		} catch (Exception e) {	
//			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
//			context.getCommunicationChannel().sendObject(
//					new DisplaySimpleMessageClientCommand(
//							CommonPlugin.getInstance().getMessage("error"), 
//							e.getMessage(), 
//							DisplaySimpleMessageClientCommand.ICON_ERROR));	
//			return null;
//		}
//	}
//	
//	@RemoteInvocation
//	public boolean pushCommit(StatefulServiceInvocationContext context, HistoryEntryDto entry, String repositoryLocation, RemoteConfig pushConfig, String target, boolean isForceUpdate) {
//		GitService.tlCommand.set((InvokeServiceMethodServerCommand) context.getCommand());
//		
//		WebProgressMonitor monitor = ProgressMonitorStatefulService.getInstance().
//				createProgressMonitor(GitPlugin.getInstance().getMessage("git.push.monitor.title"));
//				
//		try {
//			Repository repo = RepositoryCache.open(FileKey.exact(new File(repositoryLocation), FS.DETECTED));;
//			RevCommit revCommit = new RevWalk(repo).parseCommit(repo.resolve(entry.getId()));
//			
//		
//			RefSpec refSpec = new RefSpec().
//					setSourceDestination(revCommit.name(), target).
//					setForceUpdate(isForceUpdate);
//
//			org.eclipse.jgit.transport.RemoteConfig remoteConfig = null;
//			List<org.eclipse.jgit.transport.RemoteConfig> remotes = org.eclipse.jgit.transport.RemoteConfig.getAllRemoteConfigs(repo.getConfig());
//			for (org.eclipse.jgit.transport.RemoteConfig remote : remotes) {
//				if (remote.getName().equals(pushConfig.getRemoteName())) {
//					remoteConfig = remote;
//					break;
//				}
//			}		
//			List<RefSpec> fetchSpecs = remoteConfig != null ? remoteConfig.getFetchRefSpecs() : null;
//	
//			Collection<RemoteRefUpdate> remoteRefUpdates = Transport
//					.findRemoteRefUpdatesFor(repo,
//							Collections.singleton(refSpec), fetchSpecs);
//			
//			EclipseGitProgressTransformer gitMonitor = new EclipseGitProgressTransformer(monitor);
//			Transport transport = Transport.open(repo, new URIish(pushConfig.getPushUri()).toPrivateString());
//			transport.setCredentialsProvider(new GitUsernamePasswordCredentialsProvider());
//			PushResult result = transport.push(gitMonitor, remoteRefUpdates);
//
//			context.getCommunicationChannel().sendObject(new OpenOperationResultWindowClientCommand(
//					GitPlugin.getInstance().getMessage("git.push.result"), 
//					GitPlugin.getInstance().getGitUtils().handlePushResult(result)));
//			
//			return true;
//		} catch (Exception e) {		
//			if (GitPlugin.getInstance().getGitUtils().isAuthentificationException(e)) {
//				GitService.getInstance().openLoginWindow();				
//				return true;
//			}
//			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
//			context.getCommunicationChannel().sendObject(
//					new DisplaySimpleMessageClientCommand(
//							CommonPlugin.getInstance().getMessage("error"), 
//							e.getMessage(), 
//							DisplaySimpleMessageClientCommand.ICON_ERROR));	
//			return false;
//		} finally {
//			monitor.done();
//		}
//	}	
//	
//	@RemoteInvocation
//	public boolean merge(StatefulServiceInvocationContext context, HistoryEntryDto entry, String repositoryLocation) {		
//		try {
//			Repository repo = RepositoryCache.open(FileKey.exact(new File(repositoryLocation), FS.DETECTED));
//			RevCommit commit = new RevWalk(repo).parseCommit(repo.resolve(entry.getId()));
//					
//			String status = mergeStatus(repo);
//			if (status != null) {
//				context.getCommunicationChannel().sendObject(
//						new DisplaySimpleMessageClientCommand(
//								CommonPlugin.getInstance().getMessage("error"), 
//								status, 
//								DisplaySimpleMessageClientCommand.ICON_ERROR));
//				return false;
//			}
//			
//			List<RefNode> nodes = getRefNodes(commit, repo, Constants.R_HEADS);
//
//			String target;
//			if (nodes.isEmpty())
//				target = commit.name();
//			else {
//				target = nodes.get(0).getObject().getName();
//			}
//			
//			MergeOperation op = new MergeOperation(repo, target, false, context.getCommunicationChannel());
//			
//			return op.execute();
//		} catch (Exception e) {
//			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
//			context.getCommunicationChannel().sendObject(
//					new DisplaySimpleMessageClientCommand(
//							CommonPlugin.getInstance().getMessage("error"), 
//							e.getMessage(), 
//							DisplaySimpleMessageClientCommand.ICON_ERROR));
//			return false;
//		}
//	}
//	
//	@RemoteInvocation
//	public boolean rebase(StatefulServiceInvocationContext context, HistoryEntryDto entry, String repositoryLocation) {		
//		try {
//			Repository repo = RepositoryCache.open(FileKey.exact(new File(repositoryLocation), FS.DETECTED));	
//						
//			if (!repo.getFullBranch().startsWith(Constants.R_HEADS)) {
//				context.getCommunicationChannel().sendObject(
//						new DisplaySimpleMessageClientCommand(
//								CommonPlugin.getInstance().getMessage("error"), 
//								GitPlugin.getInstance().getMessage("git.rebase.noLocalBranch"), 
//								DisplaySimpleMessageClientCommand.ICON_ERROR));
//				return false;
//			}
//			
//			String currentBranch = repo.getBranch();
//			RevCommit commit = new RevWalk(repo).parseCommit(repo.resolve(entry.getId()));
//			
//			Ref ref;
//			List<RefNode> nodes = getRefNodes(commit, repo, Constants.R_HEADS);
//			if (nodes.size() == 0)
//				ref = new ObjectIdRef.Unpeeled(Storage.LOOSE, commit.getName(), commit);
//			else if (nodes.size() == 1)
//				ref = nodes.get(0).getObject();
//			else {
//				BranchConfig branchConfig = new BranchConfig(repo.getConfig(), currentBranch);
//				String trackingBranch = branchConfig.getTrackingBranch();
//				Ref remoteRef = null;
//
//				for (int i = 0; i < nodes.size(); i++) {
//					Ref obj = nodes.get(i).getObject();
//					if (trackingBranch != null && trackingBranch.equals(obj.getName())) {
//						ref = obj;
//						break;
//					}
//					if (obj.getName().startsWith(Constants.R_REMOTES)) {
//						remoteRef = obj;					
//					}
//				}
//
//				if (remoteRef != null) {
//					ref = remoteRef;
//				} else {
//					// We tried to pick a nice ref, just pick the first then
//					ref = nodes.get(0).getObject();
//				}
//			}
//			
//			RebaseOperation op = new RebaseOperation(repo, ref.getName());
//			op.execute();
//			
//			context.getCommunicationChannel().sendObject(new OpenOperationResultWindowClientCommand(
//					GitPlugin.getInstance().getMessage("git.rebase.result"), 
//					op.handleRebaseResult()));
//			
//			return true;		
//		} catch (Exception e) {
//			logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
//			context.getCommunicationChannel().sendObject(
//					new DisplaySimpleMessageClientCommand(
//							CommonPlugin.getInstance().getMessage("error"), 
//							e.getMessage(), 
//							DisplaySimpleMessageClientCommand.ICON_ERROR));
//			return false;
//		}
//	}
			
}
