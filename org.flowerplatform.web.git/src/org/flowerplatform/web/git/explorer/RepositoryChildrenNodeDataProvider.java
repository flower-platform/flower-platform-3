package org.flowerplatform.web.git.explorer;

import java.util.List;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.git.GitPlugin;
import org.flowerplatform.web.git.entity.GitNodeType;
import org.flowerplatform.web.git.entity.RepositoryNode;
import org.flowerplatform.web.git.entity.SimpleNode;

/**
 * @author Cristina Constantienscu
 */
public class RepositoryChildrenNodeDataProvider implements INodeDataProvider {
	
	private String getLabel(Object node) {		
		String nodeType = ((SimpleNode) node).getType();
		if (GitNodeType.NODE_TYPE_LOCAL_BRANCHES.equals(nodeType)) {
			return GitPlugin.getInstance().getMessage("git.localBranches");
		} else if (GitNodeType.NODE_TYPE_REMOTE_BRANCHES.equals(nodeType)) {
			return GitPlugin.getInstance().getMessage("git.remoteBranches");
		} else if (GitNodeType.NODE_TYPE_TAGS.equals(nodeType)) {
			return GitPlugin.getInstance().getMessage("git.tags");
		} else if (GitNodeType.NODE_TYPE_REMOTES.equals(nodeType)) {
			return GitPlugin.getInstance().getMessage("git.remotes");
		} else if (GitNodeType.NODE_TYPE_WDIRS.equals(nodeType)) {
			return GitPlugin.getInstance().getMessage("git.workingDirectories");
		} else {
			return null;
		}
	}
	
	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		destination.setLabel(getLabel(source));
		
		String nodeType = ((SimpleNode) source).getType();
		String icon = null;
		if (GitNodeType.NODE_TYPE_LOCAL_BRANCHES.equals(nodeType)) {
			icon = "images/full/obj16/branches_obj.gif";
		} else if (GitNodeType.NODE_TYPE_REMOTE_BRANCHES.equals(nodeType)) {
			icon = "images/full/obj16/branches_obj.gif";
		} else if (GitNodeType.NODE_TYPE_TAGS.equals(nodeType)) {
			icon = "images/full/obj16/tags.gif";
		} else if (GitNodeType.NODE_TYPE_REMOTES.equals(nodeType)) {
			icon = "images/full/obj16/remote_entry_tbl.gif";
		} else if (GitNodeType.NODE_TYPE_WDIRS.equals(nodeType)) {
			icon = "images/full/obj16/submodules.gif";
		} 
		destination.setIcon(GitPlugin.getInstance().getResourceUrl(icon));
		return true;
	}

	@Override
	public Object getParent(Object node, String nodeType, GenericTreeContext context) {
		return ((SimpleNode) node).getParent();
	}

	@Override
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {		
		return new PathFragment(((SimpleNode) node).getType(), ((SimpleNode) node).getType());
	}

	@Override
	public Object getNodeByPathFragment(Object parent, PathFragment pathFragment, GenericTreeContext context) {			
		RepositoryNode repoNode = (RepositoryNode) parent;
	
		Pair<SimpleNode, String> child;
		SimpleNode childNode = new SimpleNode(repoNode, repoNode.getRepository(), pathFragment.getType());
		child = new Pair<SimpleNode, String>(childNode, pathFragment.getType());
		return child;
	}

	@Override
	public Object getNodeByPath(List<PathFragment> fullPath, GenericTreeContext context) {
		return null;
	}

	@Override
	public String getLabelForLog(Object node, String nodeType) {
		return getLabel(node);
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
