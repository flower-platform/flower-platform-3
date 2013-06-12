package org.flowerplatform.web.explorer;

import java.io.File;
import java.util.List;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.WebPlugin;

public class OrganizationChildrenNodeDataProvider implements INodeDataProvider {

	private String getLabel(Object node) {
		@SuppressWarnings("unchecked")
		String nodeType = ((Pair<File, String>) node).b;
		if (OrganizationChildrenProvider.NODE_TYPE_WORKSPACES.equals(nodeType)) {
			// TODO CS/FP2 msg
			return "Workspaces";
		} else if (OrganizationChildrenProvider.NODE_TYPE_REPOSITORIES.equals(nodeType)) {
			return "Src Repositories";
		} else if (OrganizationChildrenProvider.NODE_TYPE_ISSUES.equals(nodeType)) {
			return "Issues";
		} else if (OrganizationChildrenProvider.NODE_TYPE_WIKI.equals(nodeType)) {
			return "Wiki";
		} else {
			return null;
		}
	}
	
	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		destination.setLabel(getLabel(source));
		
		@SuppressWarnings("unchecked")
		String nodeType = ((Pair<File, String>) source).b;
		String icon = null;
		if (OrganizationChildrenProvider.NODE_TYPE_WORKSPACES.equals(nodeType)) {
			icon = "images/workset.gif";
		} else if (OrganizationChildrenProvider.NODE_TYPE_REPOSITORIES.equals(nodeType)) {
			icon = "images/repo_rep.gif";
		} else if (OrganizationChildrenProvider.NODE_TYPE_ISSUES.equals(nodeType)) {
			icon = "images/workset.gif";
		} else if (OrganizationChildrenProvider.NODE_TYPE_WIKI.equals(nodeType)) {
			icon = "images/repo_rep.gif";
		}
		destination.setIcon(WebPlugin.getInstance().getResourceUrl(icon));

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
		@SuppressWarnings("unchecked")
		String nodeType1 = ((Pair<File, String>) node).b;
		return new PathFragment(nodeType1, nodeType1);
	}

	@Override
	public Object getNodeByPathFragment(Object parent, PathFragment pathFragment, GenericTreeContext context) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getNodeByPath(List<PathFragment> fullPath, GenericTreeContext context) {
		if (fullPath == null || fullPath.size() != 2 || !RootChildrenProvider.NODE_TYPE_ORGANIZATION.equals(fullPath.get(0).getType())) {
			throw new IllegalArgumentException("We were expecting a path with 2 items (no 0 being an org), but we got: " + fullPath);
		}
		return new Pair<File, String>(new File(RootChildrenProvider.getWorkspaceRoot(), fullPath.get(0).getName()), fullPath.get(1).getType());
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

	public static void main(String[] args) {
		throw new UnsupportedOperationException();
	}

}
