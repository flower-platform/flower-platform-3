package org.flowerplatform.web.explorer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.TreeNode;

/**
 * Provides File wrappers, i.e. a <code>Pair</code> containg a File + a String = the node type
 * that must by provided by implementing the {@link #getNodeType()} abstract method.
 * 
 * <p>
 * {@link #getFile(Object)} may be overridden to provide the file corresponding to the parent
 * node, if it is not also a <code>Pair</code>.
 * 
 * @author Cristian Spiescu
 */
public abstract class AbstractFileWrapperChildrenProvider implements IChildrenProvider {
	
	protected abstract String getNodeType();

	protected File getFile(Object node) {
		if (node instanceof Pair) {
			@SuppressWarnings("unchecked")
			Pair<File, String> realNode = (Pair<File, String>) node;
			return realNode.a;
		} else {
			throw new IllegalArgumentException(String.format("Don't know how to get the File from class = %s, instance = %s. SUPER should have overridden and handled this type", node.getClass(), node));
		}
	}
	
	protected Object getChildToProvide(File realChild) {
		return new Pair<File, String>(realChild, getNodeType());
	}
	
	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {
		File[] children = getFile(node).listFiles();
		Collection<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>(children.length);
		
		for (File child : children) {
			Pair<Object, String> pair = new Pair<Object, String>(getChildToProvide(child), getNodeType());
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
