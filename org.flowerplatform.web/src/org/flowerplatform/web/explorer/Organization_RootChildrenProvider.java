package org.flowerplatform.web.explorer;

import java.io.File;
import java.util.Collection;

import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.TreeNode;

public class Organization_RootChildrenProvider extends AbstractFileWrapperChildrenProvider implements IChildrenProvider {

	public static final String NODE_TYPE_ORGANIZATION = "organization";

	@Override
	protected File getFile(Object node) {
		return CommonPlugin.getInstance().getWorkspaceRoot();
	}

	@Override
	protected String getNodeType() {
		return NODE_TYPE_ORGANIZATION;
	}

	@Override
	protected Object getChildToProvide(File realChild) {
		return realChild;
	}

	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {
		if (node != GenericTreeStatefulService.ROOT_NODE_MARKER) {
			throw new IllegalArgumentException("Trying to get children of root, but the argument is not root: " + node);
		}

		return super.getChildrenForNode(node, treeNode, context);
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
		return true;
	}

}
