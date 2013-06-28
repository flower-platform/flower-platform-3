package org.flowerplatform.web.explorer2;

import java.io.File;
import java.util.List;

import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.INodeByPathRetriever;
import org.flowerplatform.communication.tree.INodeDataProvider2;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;

/**
 * The subclasses should populate {@link #nodeLabel} and {@link #nodeIcon} in constructor.
 * 
 * @author Cristian Spiescu
 */
public abstract class AbstractVirtualItemInOrganizationNodeDataProvider implements INodeDataProvider2, INodeByPathRetriever {

	protected String nodeLabel;
	
	protected String nodeIcon;
	
	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		destination.setLabel(nodeLabel);
		destination.setIcon(nodeIcon);

		return true;
	}

	@Override
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {
		@SuppressWarnings("unchecked")
		String nodeType1 = ((Pair<File, String>) node).b;
		return new PathFragment(nodeType1, nodeType1);
	}

	@Override
	public Object getNodeByPath(List<PathFragment> fullPath, GenericTreeContext context) {
		if (fullPath == null || fullPath.size() != 2 || !Organization_RootChildrenProvider.NODE_TYPE_ORGANIZATION.equals(fullPath.get(0).getType())) {
			throw new IllegalArgumentException("We were expecting a path with 2 items (no 0 being an org), but we got: " + fullPath);
		}
		return new Pair<File, String>(new File(CommonPlugin.getInstance().getWorkspaceRoot(), fullPath.get(0).getName()), fullPath.get(1).getType());
	}

	@Override
	public String getLabelForLog(Object node, String nodeType) {
		return nodeLabel;
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
