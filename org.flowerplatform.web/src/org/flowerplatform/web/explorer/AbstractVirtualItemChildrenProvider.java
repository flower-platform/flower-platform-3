package org.flowerplatform.web.explorer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.TreeNode;

/**
 * The subclasses should populate {@link #childNodeTypes} in constructor.
 * 
 * @author Cristina Constantinescu
 */
public class AbstractVirtualItemChildrenProvider implements IChildrenProvider {

	protected List<String> childNodeTypes;
	
	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {
		Pair<Object, String> realChild;
		Pair<Object, String> child;
		
		Collection<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>();

		for (String childNodeType : childNodeTypes) {
			realChild = new Pair<Object, String>(node, childNodeType);
			child = new Pair<Object, String>(realChild, childNodeType);
			
			result.add(child);
		}
		return result;
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
		return true;
	}
	
}
