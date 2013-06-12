package org.flowerplatform.communication.tree;

import java.util.List;

import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;

public interface INodeDataProvider {
	boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context);
	
	Object getParent(Object node, String nodeType, GenericTreeContext context);
	
	PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context);
	
	Object getNodeByPathFragment(Object parent, PathFragment pathFragment, GenericTreeContext context);
	
	Object getNodeByPath(List<PathFragment> fullPath, GenericTreeContext context);		
	
	String getLabelForLog(Object node, String nodeType);
	
	String getInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> fullPath);
	
	boolean setInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> path, String text);
}
