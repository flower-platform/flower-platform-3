package org.flowerplatform.web.git.explorer;

import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.git.GitPlugin;
import org.flowerplatform.web.git.entity.GitNodeType;
import org.flowerplatform.web.git.entity.RefNode;
import org.flowerplatform.web.git.entity.SimpleNode;

/**
 * @author Cristina Constantienscu
 */
public class RefNodeDataProvider implements INodeDataProvider {

	@Override
	public boolean populateTreeNode(Object source, TreeNode destination,	GenericTreeContext context) {
		RefNode node = (RefNode) source;
		destination.setLabel(Repository.shortenRefName(((Ref)node.getObject()).getName()));
		String icon;
		if (GitNodeType.NODE_TYPE_TAG.equals(node.getType())) {
			icon = "images/full/obj16/annotated-tag.gif";
		} else {
			icon = "images/full/obj16/branch_obj.gif";
		}
		destination.setIcon(GitPlugin.getInstance().getResourceUrl(icon));
		return true;
	}

	@Override
	public Object getParent(Object node, String nodeType, GenericTreeContext context) {
		return ((RefNode) node).getParent();
	}

	@Override
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {
		RefNode refNode = (RefNode) node;
		return new PathFragment(((Ref) refNode.getObject()).getName(), refNode.getType());
	}

	@Override
	public Object getNodeByPathFragment(Object parent, PathFragment pathFragment, GenericTreeContext context) {
		SimpleNode parentNode = (SimpleNode) parent;		
		try {
			Ref ref = parentNode.getRepository().getRef(pathFragment.getName());
			return new RefNode(parentNode, pathFragment.getType(), parentNode.getRepository(), ref);
		} catch (IOException e) {
			return null;
		} 		
	}

	@Override
	public Object getNodeByPath(List<PathFragment> fullPath, GenericTreeContext context) {		//
		return null;
	}

	@Override
	public String getLabelForLog(Object node, String nodeType) {
		RefNode refNode = (RefNode) node;
		return ((Ref) refNode.getObject()).getName();
	}

	@Override
	public String getInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> fullPath) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> path, 	String text) {
		throw new UnsupportedOperationException();
	}

}
