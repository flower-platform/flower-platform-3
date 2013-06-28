package org.flowerplatform.web.projects;

import java.util.List;

import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.WebPlugin;
import org.flowerplatform.web.entity.WorkingDirectory;

/**
 * Node is a {@link WorkingDirectory}.
 * 
 * @author Cristian Spiescu
 */
public class WorkingDirectoryNodeDataProvider implements INodeDataProvider {
	
	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		destination.setLabel(getLabelForLog(source, null));
		destination.setIcon(WebPlugin.getInstance().getResourceUrl("images/workset.gif"));
		return true;
	}

	@Override
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {
		return new PathFragment(((WorkingDirectory) node).getPathFromOrganization(), nodeType);
	}

	@Override
	public String getLabelForLog(Object node, String nodeType) {
		return ((WorkingDirectory) node).getPathFromOrganization();
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
