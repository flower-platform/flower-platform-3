package org.flowerplatform.web.git.explorer;

import java.io.File;
import java.util.List;

import org.eclipse.jgit.lib.Repository;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.git.GitPlugin;
import org.flowerplatform.web.git.entity.RepositoryNode;

/**
 * @author Cristina Constantienscu
 */
public class RepositoryNodeDataProvider implements INodeDataProvider {
	
	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		RepositoryNode node = (RepositoryNode) source;
		destination.setLabel(((File)node.getObject()).getName());
		destination.setIcon(GitPlugin.getInstance().getResourceUrl("images/full/obj16/repository_rep.gif"));
		return true;
	}

	@Override
	public Object getParent(Object node, String nodeType, GenericTreeContext context) {
		return ((RepositoryNode) node).getParent();
	}

	@Override
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {
		RepositoryNode repoNode = (RepositoryNode) node;
		return new PathFragment(((File) repoNode.getObject()).getName(), nodeType);
	}

	@Override
	public Object getNodeByPathFragment(Object parent, PathFragment pathFragment, GenericTreeContext context) {
		File repoFile = new File(GitRootChildrenProvider.getGitRepositoriesFile(), pathFragment.getName());
		Repository repo = GitPlugin.getInstance().getUtils().getMainRepository(repoFile);
		return new RepositoryNode(parent, repo, repoFile);
	}

	@Override
	public Object getNodeByPath(List<PathFragment> fullPath, GenericTreeContext context) {
		return null;
	}

	@Override
	public String getLabelForLog(Object node, String nodeType) {
		RepositoryNode repoNode = (RepositoryNode) node;
		return ((File) repoNode.getObject()).getName();
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
