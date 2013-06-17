package org.flowerplatform.web.git.staging.communication;

import static org.eclipse.jgit.lib.Constants.HEAD;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.RmCommand;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.channel.ICommunicationChannelLifecycleListener;
import org.flowerplatform.communication.service.ServiceRegistry;
import org.flowerplatform.communication.stateful_service.IStatefulClientLocalState;
import org.flowerplatform.communication.stateful_service.IStatefulServiceMXBean;
import org.flowerplatform.communication.stateful_service.RegularStatefulService;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.web.git.GitPlugin;
import org.flowerplatform.web.git.dto.CommitResourceDto;
import org.flowerplatform.web.git.history.communication.GitHistoryStatefulService;
import org.flowerplatform.web.git.staging.dto.StagingViewInfoDto;

/**
 * @author Cristina
 */
@SuppressWarnings({"unchecked", "restriction", "rawtypes" })
public class GitStagingStatefulService extends RegularStatefulService<CommunicationChannel, GitStagingData> implements IStatefulServiceMXBean, ICommunicationChannelLifecycleListener {

	public static final String SERVICE_ID = "GitStagingStatefulService";
	
	private static final String STATEFUL_CLIENT_ID = "Git Staging";
	
	public GitStagingStatefulService() {
		super();
		clients = new HashMap<CommunicationChannel, GitStagingData>();
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
	protected GitStagingData getDataFromStatefulClientLocalState(StatefulServiceInvocationContext context, IStatefulClientLocalState statefulClientLocalState) {
		// channel destroyed
		if (statefulClientLocalState == null) {
			return null;
		}
		return new GitStagingData(((GitStagingStatefulClientLocalState) statefulClientLocalState).getRepositoryLocation());
	}

	@Override
	public void communicationChannelCreated(CommunicationChannel webCommunicationChannel) {
		// do nothing
	}

	@Override
	public void communicationChannelDestroyed(CommunicationChannel webCommunicationChannel) {
		unsubscribe(new StatefulServiceInvocationContext(webCommunicationChannel, null, null), null);
	}
	
	private Repository getRepository(CommunicationChannel channel, StagingViewInfoDto info) throws IOException {
		if (info.getIsResource()) {
			IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember((String) info.getSelectedObject());			
			RepositoryMapping mapping = RepositoryMapping.getMapping(resource);
			
			if (mapping == null) {
				throw new IOException();
			}			
			return GitPlugin.getInstance().getGitUtils().getRepository(mapping);	
		} else {
			Object node = GitRepositoriesTreeStatefulService.getInstance().getNodeByPath(
					((List<PathFragment>) info.getSelectedObject()), 
					GitRepositoriesTreeStatefulService.getInstance().getTreeContext(channel, info.getStatefulClientId()));
			
			if (!(node instanceof RepositoryTreeNode)) {
				throw new IOException();
			}									
			return ((RepositoryTreeNode) node).getRepository();
		}
	}	
	
	private List<CommitResourceDto> getStagedChanges(Repository repository, IndexDiffData indexDiff) {				
		List<CommitResourceDto> stagedChanges = new ArrayList<CommitResourceDto>();
		for (String file : indexDiff.getAdded()) {
			addToList(stagedChanges, file, CommitResourceDto.ADDED, UIIcons.OVR_STAGED_ADD, false, repository.getDirectory());			
		}
		for (String file : indexDiff.getChanged()) {
			addToList(stagedChanges, file, CommitResourceDto.CHANGED, null, false, repository.getDirectory());		
		}
		for (String file : indexDiff.getRemoved()) {
			addToList(stagedChanges, file, CommitResourceDto.REMOVED, UIIcons.OVR_STAGED_REMOVE, false, repository.getDirectory());		
		}
		
		return stagedChanges;
	}
	
	private List<CommitResourceDto> getUnstagedChanges(Repository repository, IndexDiffData indexDiff) {				
		List<CommitResourceDto> unstagedChanges = new ArrayList<CommitResourceDto>();
		for (String file : indexDiff.getMissing()) {
			addToList(unstagedChanges, file, CommitResourceDto.MISSING, null, false, repository.getDirectory());			
		}
		for (String file : indexDiff.getModified()) {
			addToList(unstagedChanges, file, CommitResourceDto.MODIFIED, null, true, repository.getDirectory());			
		}
		for (String file : indexDiff.getUntracked()) {
			addToList(unstagedChanges, file, CommitResourceDto.UNTRACKED, UIIcons.OVR_UNTRACKED, false, repository.getDirectory());				
		}
		for (String file : indexDiff.getConflicting()) {
			addToList(unstagedChanges, file, CommitResourceDto.CONFLICTING, UIIcons.OVR_CONFLICT, false, repository.getDirectory());		
		}				
		return unstagedChanges;
	}
	
	private void addToList(List<CommitResourceDto> list, String file, int state, ImageDescriptor image, boolean addPrefix, File repoLocation) {
		IResource res = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI(new File(repoLocation, file).toURI())[0];
		if (GitPlugin.getInstance().getGitUtils().isIgnored(res)) {
			return;
		}
		
		CommitResourceDto dto = new CommitResourceDto();
		dto.setLabel((addPrefix ? "< " : "") + file);
		dto.setPath(file);
		dto.setState(state);
		setImage(dto, image);	
		list.add(dto);
	}
	
	private void setImage(CommitResourceDto dto, ImageDescriptor decorator) {	
		if (decorator != null) {
			org.eclipse.swt.graphics.Image image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
			image = new DecorationOverlayIcon(image, decorator, IDecoration.BOTTOM_RIGHT).createImage();
			dto.setImage(SWTImageUtil.INSTANCE.getImageURL(image));
			image.dispose();
		} else {
			dto.setImage(SWTImageUtil.INSTANCE.getRegisteredImageAliases().get("git_file"));
		}
	}
	
	private boolean hasHead(Repository repository) {
		try {
			Ref head = repository.getRef(HEAD);
			return head != null && head.getObjectId() != null;
		} catch (IOException e) {
			return false;
		}
	}
	
	///////////////////////////////////////////////////////////////
	// Remote methods
	///////////////////////////////////////////////////////////////
	
	@RemoteInvocation
	public StagingViewInfoDto getViewInfo(StatefulServiceInvocationContext context, StagingViewInfoDto info) {
		try {
			GitStagingData data = clients.get(context.getCommunicationChannel());
			
			Repository repo = getRepository(context.getCommunicationChannel(), info);
			data.setRepository(repo);
			
			info.setRepositoryLocation(repo.getDirectory().getAbsolutePath());
			info.setRepositoryName(GitPlugin.getInstance().getGitUtils().getRepositoryName(repo));
						
			IndexDiffData indexDiff = data.getIndexDiff();
			
			info.setStagedChanges(getStagedChanges(repo, indexDiff));
			info.setUnstagedChanges(getUnstagedChanges(repo, indexDiff));
			
			User user = (User) context.getCommunicationChannel().getPrincipal().getUser();
			
			info.setAuthor(user.getName() + " <" + user.getEmail() + ">");
			info.setCommitter(user.getName() + " <" + user.getEmail() + ">");	
			
			return info;
		} catch (IOException e) {
			return null;
		}
	}
	
	@RemoteInvocation
	public boolean deleteFiles(StatefulServiceInvocationContext context, List<CommitResourceDto> files) {
		return true;
	}
	
	@RemoteInvocation
	public boolean removeFromIndex(StatefulServiceInvocationContext context, List<CommitResourceDto> files) {
		try {
			GitStagingData data = clients.get(context.getCommunicationChannel());
			Git git = new Git(data.getRepository());
			if (hasHead(data.getRepository())) {
				ResetCommand resetCommand = git.reset();
				resetCommand.setRef(HEAD);
				for (CommitResourceDto file : files) {
					resetCommand.addPath(file.getPath());
				}
				resetCommand.call();
			} else {
				RmCommand rmCommand = git.rm();
				rmCommand.setCached(true);
				for (CommitResourceDto file : files) {
					rmCommand.addFilepattern(file.getPath());
				}
				rmCommand.call();
			}
			return true;
		} catch (Exception e) {			
			return false;
		}
	}
	
	@RemoteInvocation
	public boolean addToIndex(StatefulServiceInvocationContext context, List<CommitResourceDto> files) {
		try {
			GitStagingData data = clients.get(context.getCommunicationChannel());
			Git git = new Git(data.getRepository());
			RmCommand rm = null;
			
			Set<String> addPaths = new HashSet<String>();
			for (CommitResourceDto file : files) {
				switch (file.getState()) {
				case CommitResourceDto.ADDED:
				case CommitResourceDto.CHANGED:
				case CommitResourceDto.REMOVED:
					// already staged
					break;
				case CommitResourceDto.CONFLICTING:
				case CommitResourceDto.MODIFIED:				
				case CommitResourceDto.UNTRACKED:
					addPaths.add(file.getPath());
					break;
				case CommitResourceDto.MISSING:
					if (rm == null)
						rm = git.rm().setCached(true);
					rm.addFilepattern(file.getPath());
					break;
				}
			}
			
			if (!addPaths.isEmpty()) {
				AddCommand add = git.add();
				for (String addPath : addPaths) {
					add.addFilepattern(addPath);
				}
				add.call();	
			}
			if (rm != null) {
				rm.call();
			}
			return true;
		} catch (Exception e) {			
			return false;
		}				
	}
	
}
