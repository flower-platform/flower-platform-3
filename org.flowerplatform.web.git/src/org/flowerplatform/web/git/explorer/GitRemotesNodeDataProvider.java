package org.flowerplatform.web.git.explorer;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.explorer.RootChildrenProvider;

public class GitRemotesNodeDataProvider implements INodeDataProvider {
	
	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
//		if (GitExplorerTreeStatefulService.ROOT_NODE_MARKER .equals(source)) {
//			return false;
//		}
		File file = (File) source;
		destination.setLabel(file.getName());
		
		return true;
	}

	@Override
	public Object getParent(Object node, String nodeType, GenericTreeContext context) {
		if (GenericTreeStatefulService.NODE_TYPE_ROOT.equals(nodeType)) {
			return null;
		}
		return ((File) node).getParentFile();
	}

	@Override
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {
		return new PathFragment(((File) node).getName(), nodeType);
	}

	@Override
	public Object getNodeByPathFragment(Object parent, PathFragment pathFragment, GenericTreeContext context) {
//		if (GitExplorerTreeStatefulService.NODE_TYPE_FILE.equals(pathFragment.getType())) {
//			return new File((File) parent, pathFragment.getName());
//		}			
		return null;
	}

	@Override
	public Object getNodeByPath(List<PathFragment> fullPath, GenericTreeContext context) {
		return null;
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
