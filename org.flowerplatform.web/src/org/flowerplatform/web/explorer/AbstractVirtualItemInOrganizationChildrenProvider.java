package org.flowerplatform.web.explorer;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.TreeNode;

public class AbstractVirtualItemInOrganizationChildrenProvider implements IChildrenProvider {

	protected String childNodeType;
	
	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {
		Pair<File, String> realChild;
		Pair<Object, String> child;
		
		realChild = new Pair<File, String>((File) node, childNodeType);
		child = new Pair<Object, String>(realChild, childNodeType);
		return Collections.singleton(child);
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
		return true;
	}

}
