package org.flowerplatform.web.explorer;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.jws.WebParam;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.WebPlugin;

public class WorkspacesNodeDataProvider implements INodeDataProvider {

	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		File file = (File) source;
		destination.setLabel(file.getName());
		if (file.isDirectory()) {
			destination.setIcon(WebPlugin.getInstance().getResourceUrl("images/folder.gif"));
		} else {
			destination.setIcon(WebPlugin.getInstance().getResourceUrl("images/file.gif"));
		}
		return true;
	}

	@Override
	public Object getParent(Object node, String nodeType, GenericTreeContext context) {
		File file = (File) node;
		if (RootChildrenProvider.getWorkspaceRoot().equals(file.getParentFile().getParentFile())) {
			// i.e. is a subnode of an organization node
			return new Pair<File, String>(file.getParentFile(), OrganizationChildrenProvider.NODE_TYPE_WORKSPACES);
		} else {
			return file.getParentFile();
		}
	}

	@Override
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {
		File file = (File) node;
		return new PathFragment(file.getName(), WorkspacesChildrenProvider.NODE_TYPE_FILE);
	}

	@Override
	public Object getNodeByPathFragment(Object parent, PathFragment pathFragment, GenericTreeContext context) {
		throw new UnsupportedOperationException("This should not be called, as we provide an implementation quicker implementation; i.e. for getNodeByPath()");
	}

	@Override
	public Object getNodeByPath(List<PathFragment> fullPath, GenericTreeContext context) {
		StringBuffer sb;
		try {
			sb = new StringBuffer(RootChildrenProvider.getWorkspaceRoot().getCanonicalPath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		for (PathFragment pf : fullPath) {
			if (RootChildrenProvider.NODE_TYPE_ORGANIZATION.equals(pf.getType()) ||
					WorkspacesChildrenProvider.NODE_TYPE_FILE.equals(pf.getType())) {
				sb.append(File.separatorChar);
				sb.append(pf.getName());
			} else if (OrganizationChildrenProvider.NODE_TYPE_WORKSPACES.equals(pf.getType())) {
				// we skip it
				continue;
			} else {
				throw new IllegalArgumentException(String.format("Unknwon node type = %s encountered while getting file by path for %s", pf.getType(), fullPath));
			}
		}
		return new File(sb.toString());
	}

	@Override
	public String getLabelForLog(Object node, String nodeType) {
		return ((File) node).getName();
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
