package org.flowerplatform.web.git.explorer;

import java.util.List;

import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.git.GitPlugin;
import org.flowerplatform.web.git.explorer.entity.RemoteNode;

/**
 * @author Cristina Constantienscu
 */
public class RemoteNodeDataProvider implements INodeDataProvider {

	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		RemoteNode node = (RemoteNode) source;
		destination.setLabel(node.getRemote());
		destination.setIcon(GitPlugin.getInstance().getResourceUrl("images/full/obj16/remotespec.gif"));
		return true;
	}

	@Override
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {
		return new PathFragment(((RemoteNode) node).getRemote(), nodeType);
	}

	@Override
	public String getLabelForLog(Object node, String nodeType) {
		return ((RemoteNode) node).getRemote();
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
