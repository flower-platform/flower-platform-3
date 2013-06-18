package org.flowerplatform.web.git.explorer;

import java.util.List;

import org.eclipse.jgit.lib.Repository;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.git.GitPlugin;
import org.flowerplatform.web.git.entity.RemoteNode;
import org.flowerplatform.web.git.entity.SimpleNode;

/**
 * @author Cristina Constantienscu
 */
public class RemoteNodeDataProvider implements INodeDataProvider {

	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		RemoteNode remoteNode = (RemoteNode) source;
		destination.setLabel(remoteNode.getObject());
		destination.setIcon(GitPlugin.getInstance().getResourceUrl("images/full/obj16/remotespec.gif"));
		return true;
	}

	@Override
	public Object getParent(Object node, String nodeType, GenericTreeContext context) {
		RemoteNode remoteNode = (RemoteNode) node;
		return remoteNode.getParent();
	}

	@Override
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {
		RemoteNode remoteNode = (RemoteNode) node;
		return new PathFragment(remoteNode.getObject(), nodeType);
	}

	@Override
	public Object getNodeByPathFragment(Object parent, PathFragment pathFragment, GenericTreeContext context) {
		SimpleNode simpleNode = (SimpleNode) parent;
		Repository repo = simpleNode.getRepository();
		return new RemoteNode(simpleNode, repo, pathFragment.getName());
	}

	@Override
	public Object getNodeByPath(List<PathFragment> fullPath, GenericTreeContext context) {
		return null;
	}

	@Override
	public String getLabelForLog(Object node, String nodeType) {
		RemoteNode remoteNode = (RemoteNode) node;
		return remoteNode.getObject();
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
