package org.flowerplatform.web.git.explorer;

import java.io.File;
import java.util.List;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.explorer.RootChildrenProvider;
import org.flowerplatform.web.git.GitPlugin;
import org.flowerplatform.web.git.entity.GitNodeType;

/**
 * @author Cristina Constantienscu
 */
public class GitRootNodeDataProvider implements INodeDataProvider {
	
	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		destination.setLabel(GitPlugin.getInstance().getMessage("git.repositories"));
		destination.setIcon(GitPlugin.getInstance().getResourceUrl("images/eview16/repo_rep.gif"));
		return true;
	}

	@Override
	public Object getParent(Object node, String nodeType, GenericTreeContext context) {
		@SuppressWarnings("unchecked")
		File parentFile = ((Pair<File, String>) node).a;
		return parentFile;
	}

	@Override
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {
		//@SuppressWarnings("unchecked")
		//String nodeType1 = ((Pair<File, String>) node).b;
		return new PathFragment(nodeType, nodeType);
	}

	@Override
	public Object getNodeByPathFragment(Object parent, PathFragment pathFragment, GenericTreeContext context) {
		Pair<File, String> realChild;
		//Pair<Object, String> child;
		realChild = new Pair<File, String>((File) parent, GitNodeType.NODE_TYPE_GIT_REPOSITORIES);
		//child = new Pair<Object, String>(realChild, GitNodeType.NODE_TYPE_GIT_REPOSITORIES);
		return realChild;
	}

	@Override
	public Object getNodeByPath(List<PathFragment> fullPath, GenericTreeContext context) {
//		if (fullPath == null || fullPath.size() != 2 || !RootChildrenProvider.NODE_TYPE_ORGANIZATION.equals(fullPath.get(0).getType())) {
//			throw new IllegalArgumentException("We were expecting a path with 2 items (no 0 being an org), but we got: " + fullPath);
//		}
//		Pair<File, String> realChild;
//		Pair<Object, String> child;
//		realChild = new Pair<File, String>((File) RootChildrenProvider.getWorkspaceRoot(), GitNodeType.NODE_TYPE_GIT_REPOSITORIES);
//		child = new Pair<Object, String>(realChild, GitNodeType.NODE_TYPE_GIT_REPOSITORIES);
//		return child;
		return null;
	}

	@Override
	public String getLabelForLog(Object node, String nodeType) {
		@SuppressWarnings("unchecked")
		File file = ((Pair<File, String>) node).a;
		return file.getName();
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
