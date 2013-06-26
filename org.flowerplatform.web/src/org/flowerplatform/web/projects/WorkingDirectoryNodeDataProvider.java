package org.flowerplatform.web.projects;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildNodeAdjusterBeforeProcessing;
import org.flowerplatform.communication.tree.IGenericTreeStatefulServiceAware;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.NodeInfo;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.WebPlugin;
import org.flowerplatform.web.entity.WorkingDirectory;
import org.flowerplatform.web.explorer.RootChildrenProvider;

/**
 * This provider handles all children found under "/org/Working Directories" virtual node. All of them have
 * the same node type: {@link WorkingDirectory_WorkingDirectoriesChildrenProvider#NODE_TYPE_WORKING_DIRECTORY}.
 * 
 * <p>
 * The actual type of the node is <code>Pair<File, String></code>: <file for current node, the nodeType constant cf. above>.
 * We put like this (and not directly <code>File</code>) in order to allow a file being added by multiple providers/subtrees
 * (e.g. git, svn, etc). And for this, we need different instances.
 * 
 * <p>
 * There are 2 special kinds of nodes: working directory and project. They have the same node type and actual type of node
 * cf. above, but the methods that handle them, look in the {@link NodeInfo#getCustomData()} to see a) if they are these
 * kind of special nodes and b) to populate/process them slightly differently. For them, the children provider passes
 * the special data which we store in the map + we "adjust" the nodes (i.e. change the actual type, so that they respect
 * the convention (<code>Pair<File, String></code>).
 * 
 * @author Cristian Spiescu
 */
public class WorkingDirectoryNodeDataProvider implements INodeDataProvider, IChildNodeAdjusterBeforeProcessing, IGenericTreeStatefulServiceAware {
	
	protected static final String WORKING_DIRECTORY = "workingDirectory";
	
	protected static final String IPROJECT = "iProject";
	
	protected GenericTreeStatefulService genericTreeStatefulService;
	
	@Override
	public void setGenericTreeStatefulService(GenericTreeStatefulService genericTreeStatefulService) {
		this.genericTreeStatefulService = genericTreeStatefulService;
	}

	@Override
	public Object adjustChild(Object originalChild, String nodeType, NodeInfo nodeInfo) {
		// the children provider has passed the File pointing to the working directory, and the WorkingDirectory
		@SuppressWarnings("unchecked")
		Pair<File, WorkingDirectory> childToBeAdjusted = (Pair<File, WorkingDirectory>) originalChild;
		
		// save the WorkingDirectory in the map
		nodeInfo.getCustomData().put(WORKING_DIRECTORY, childToBeAdjusted.b);
		
		// the actual child is not a plain file, to allow other providers (i.e. subtrees) put files; e.g. git repo, etc
		return new Pair<File, String>(childToBeAdjusted.a, nodeType);
	}

	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		@SuppressWarnings("unchecked")
		Pair<File, String> pair = (Pair<File, String>) source;
		File file = pair.a;
		destination.setLabel(file.getName());
		
		NodeInfo nodeInfo = genericTreeStatefulService.getNodeInfoForObject(source, pair.b);
		
		WorkingDirectory wd = (WorkingDirectory) nodeInfo.getCustomData().get(WORKING_DIRECTORY);
		if (wd != null) {
			// it's a WD
			destination.setLabel("[WD]" + destination.getLabel());
		}
		
		if (file.isDirectory()) {
			destination.setIcon(WebPlugin.getInstance().getResourceUrl("images/folder.gif"));
		} else {
			destination.setIcon(WebPlugin.getInstance().getResourceUrl("images/file.gif"));
		}
		return true;
	}

	@Override
	public Object getParent(Object node, String nodeType, GenericTreeContext context) {
		@SuppressWarnings("unchecked")
		Pair<File, String> pair = (Pair<File, String>) node;
		File file = pair.a;

//		NodeInfo nodeInfo = genericTreeStatefulService.getNodeInfoForObject(node, pair.b);
//		
//		WorkingDirectory wd = (WorkingDirectory) nodeInfo.getCustomData().get(WORKING_DIRECTORY);
//		if (wd != null) {
			// it's a WD; so the parent is a pair of the ORG file + nodeType
			return new Pair<File, String>(file.getParentFile().getParentFile(), WorkingDirectories_OrganizationChildrenProvider.NODE_TYPE_WORKING_DIRECTORIES);
//		}

		// normal case
//		return new Pair<File, String>(file.getParentFile(), nodeType);
	}

	@Override
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {
		@SuppressWarnings("unchecked")
		Pair<File, String> pair = (Pair<File, String>) node;
		File file = pair.a;

		NodeInfo nodeInfo = genericTreeStatefulService.getNodeInfoForObject(node, pair.b);
		
		String pathName;
		WorkingDirectory wd = (WorkingDirectory) nodeInfo.getCustomData().get(WORKING_DIRECTORY);
		if (wd != null) {
			// it's a WD; so we need more than the name of file; e.g. git_repos/repo1/wd1
			pathName = wd.getPathFromOrganization();
		} else {
			// normal case
			pathName = file.getName();
		}
		return new PathFragment(pathName, nodeType);
	}

	@Override
	public Object getNodeByPathFragment(Object parent, PathFragment pathFragment, GenericTreeContext context) {
		throw new UnsupportedOperationException("This should not be called, as we provide an implementation quicker implementation; i.e. for getNodeByPath()");
	}

	@Override
	public Object getNodeByPath(List<PathFragment> fullPath, GenericTreeContext context) {
		StringBuffer sb;
		try {
			sb = new StringBuffer(RootChildrenProvider.getWorkspaceRoot().getCanonicalPath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		for (PathFragment pf : fullPath) {
			if (RootChildrenProvider.NODE_TYPE_ORGANIZATION.equals(pf.getType()) ||
					WorkingDirectory_WorkingDirectoriesChildrenProvider.NODE_TYPE_WORKING_DIRECTORY.equals(pf.getType())) {
				sb.append(File.separatorChar);
				sb.append(pf.getName());
			} else if (WorkingDirectories_OrganizationChildrenProvider.NODE_TYPE_WORKING_DIRECTORIES.equals(pf.getType())) {
				// we skip it
				continue;
			} else {
				throw new IllegalArgumentException(String.format("Unknwon node type = %s encountered while getting file by path for %s", pf.getType(), fullPath));
			}
		}
		return new Pair<File, String>(new File(sb.toString()), WorkingDirectory_WorkingDirectoriesChildrenProvider.NODE_TYPE_WORKING_DIRECTORY);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getLabelForLog(Object node, String nodeType) {
		// TODO CS/FP2: use the same as for population
		return ((Pair<File, String>) node).a.getName();
	}

	@Override
	public String getInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> fullPath) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> path, String text) {
		throw new UnsupportedOperationException();
	}

}
