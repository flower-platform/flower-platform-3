package org.flowerplatform.web.explorer;

import java.io.File;
import java.util.Collection;

import org.eclipse.core.resources.ResourcesPlugin;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.DelegatingGenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.TreeNode;

public class RootChildrenProvider extends WorkspacesChildrenProvider implements IChildrenProvider {

	public static final String NODE_TYPE_ORGANIZATION = "or";

	public static File getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot().getRawLocation().makeAbsolute().toFile();
	}

	@Override
	protected File getFile(Object node) {
		return getWorkspaceRoot();
	}

	@Override
	protected String getNodeType() {
		return NODE_TYPE_ORGANIZATION;
	}

	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {
		if (node != DelegatingGenericTreeStatefulService.ROOT_NODE_MARKER) {
			throw new IllegalArgumentException("Trying to get children of root, but the argument is not root: " + node);
		}

		return super.getChildrenForNode(node, treeNode, context);
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
		return true;
	}

}
