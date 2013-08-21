package org.flowerplatform.web.svn.explorer;

import java.util.List;

import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.svn.SvnNodeType;
import org.tigris.subversion.subclipse.core.resources.RemoteFolder;
import org.tigris.subversion.subclipse.core.resources.RemoteResource;

/**
 * 
 * @author Victor Badila
 * 
 * @flowerModelElementId _MDgtwP3MEeKrJqcAep-lCg
 */
public class SvnFileNodeDataProvider implements INodeDataProvider {
	/**
	 * @flowerModelElementId _R5OFMP6CEeKrJqcAep-lCg
	 */
	public String getInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> fullPath) {
		return null;
	}

	/**
	 * @flowerModelElementId _R5nGwP6CEeKrJqcAep-lCg
	 */
	public String getLabelForLog(Object node, String nodeType) {
		return null;
	}

	/**
	 * @flowerModelElementId _R5724P6CEeKrJqcAep-lCg
	 */
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {
		String nodeSpecificPath = ((RemoteResource) node).getName();
		return new PathFragment(nodeSpecificPath, SvnNodeType.NODE_TYPE_FILE);
	}

	/**
	 * @flowerModelElementId _R6P_8P6CEeKrJqcAep-lCg
	 */
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		destination.setLabel(((RemoteResource) source).getName());
		if (source instanceof RemoteFolder) {
			destination.setIcon("servlet/public-resources/org.flowerplatform.web/images/folder.gif");
		} else {
			destination.setIcon("servlet/public-resources/org.flowerplatform.web/images/file.gif");
		}
		return true;
	}

	/**
	 * @flowerModelElementId _R6pBgP6CEeKrJqcAep-lCg
	 */
	public boolean setInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> path, String text) {
		return false;
	}
}