package org.flowerplatform.web.explorer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.TreeNode;

public class OrganizationChildrenProvider implements IChildrenProvider {

	public static final String NODE_TYPE_WORKSPACES = "wo";
	
	public static final String NODE_TYPE_REPOSITORIES = "re"; 
	
	public static final String NODE_TYPE_ISSUES = "is";
	
	public static final String NODE_TYPE_WIKI = "wi";
	
	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {
		Collection<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>(4);
		
		Pair<File, String> realChild;
		Pair<Object, String> child;
		
		realChild = new Pair<File, String>((File) node, NODE_TYPE_WORKSPACES);
		child = new Pair<Object, String>(realChild, NODE_TYPE_WORKSPACES);
		result.add(child);
		
		realChild = new Pair<File, String>((File) node, NODE_TYPE_REPOSITORIES);
		child = new Pair<Object, String>(realChild, NODE_TYPE_REPOSITORIES);
		result.add(child);
		
		realChild = new Pair<File, String>((File) node, NODE_TYPE_ISSUES);
		child = new Pair<Object, String>(realChild, NODE_TYPE_ISSUES);
		result.add(child);
		
		realChild = new Pair<File, String>((File) node, NODE_TYPE_WIKI);
		child = new Pair<Object, String>(realChild, NODE_TYPE_WIKI);
		result.add(child);
		
		return result;
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
		return true;
	}

}
