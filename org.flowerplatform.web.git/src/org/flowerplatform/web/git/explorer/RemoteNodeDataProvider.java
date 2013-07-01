package org.flowerplatform.web.git.explorer;

import java.util.List;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.git.GitPlugin;

/**
 * @author Cristina Constantienscu
 */
public class RemoteNodeDataProvider implements INodeDataProvider {

	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		@SuppressWarnings("unchecked")
		String remote = ((Pair<String, String>) source).a;
		destination.setLabel(remote);
		destination.setIcon(GitPlugin.getInstance().getResourceUrl("images/full/obj16/remotespec.gif"));
		return true;
	}

	@Override
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {
		@SuppressWarnings("unchecked")
		String remote = ((Pair<String, String>) node).a;
		return new PathFragment(remote, nodeType);
	}

	@Override
	public String getLabelForLog(Object node, String nodeType) {
		@SuppressWarnings("unchecked")
		String remote = ((Pair<String, String>) node).a;
		return remote;
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
