package org.flowerplatform.web.explorer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.TreeNode;

public class WorkspacesChildrenProvider implements IChildrenProvider {

	public static final String NODE_TYPE_FILE = "f";
	
	protected File getFile(Object node) {
		if (node instanceof Pair) {
			@SuppressWarnings("unchecked")
			Pair<File, String> realNode = (Pair<File, String>) node;
			return realNode.a;
		} else {
			// node is a file
			return (File) node;
		}
	}
	
	protected String getNodeType() {
		return NODE_TYPE_FILE;
	}
	
	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {
		File[] children = getFile(node).listFiles();
		Collection<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>(children.length);
		
		for (File child : children) {
			Pair<Object, String> pair = new Pair<Object, String>(child, getNodeType());
			result.add(pair);
		}
		
		return result;
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
		File file = getFile(node);
		return file.isDirectory() && file.list().length > 0;
	}

}
