package org.flowerplatform.web.git.explorer;

import java.util.List;

import org.eclipse.jgit.lib.Repository;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.git.GitPlugin;

/**
 * @author Cristina Constantienscu
 */
public class RepositoryNodeDataProvider implements INodeDataProvider {
	
	public static final String REPOSITORY_KEY = "gitRepository";
	
	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		Repository repository = (Repository) source;

		destination.setLabel(repository.getDirectory().getParentFile().getParentFile().getName());
		destination.setIcon(GitPlugin.getInstance().getResourceUrl("images/full/obj16/repository_rep.gif"));
		return true;
	}

	@Override
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {
		Repository repository = (Repository) node;	
		return new PathFragment(repository.getDirectory().getParentFile().getParentFile().getName(), nodeType);
	}

	@Override
	public String getLabelForLog(Object node, String nodeType) {
		Repository repository = (Repository) node;
		return repository.getDirectory().getParentFile().getParentFile().getName();
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
