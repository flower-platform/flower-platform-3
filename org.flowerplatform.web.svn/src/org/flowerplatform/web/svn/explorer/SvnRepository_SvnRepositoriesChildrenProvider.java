package org.flowerplatform.web.svn.explorer;


import java.util.Collection;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.TreeNode;

/**
 * @flowerModelElementId _UcYSoP3LEeKrJqcAep-lCg
 */
public class SvnRepository_SvnRepositoriesChildrenProvider implements
		IChildrenProvider {
	/**
	 * Intr-o prima faza, hardcodam la svn://csp1/flower2. Copiii sunt string
	 * 
	 * Apoi, se va lua din BD: get all svn repos for organization = crt. Copiii sunt SvnRepository
	 * 
	 * @flowerModelElementId _kWPuUP3LEeKrJqcAep-lCg
	 */
//	@Override
//	public Collection<Pair<Object, String>> getChildrenForNode(Object node,
//			String treeNode, GenericTreeContext context) {
//		// TODO implement
//		return null;
//	}
	
	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node,
			TreeNode treeNode, GenericTreeContext context) {
		// TODO implement
		return null;
	}

	/**
	 * @flowerModelElementId _z6dDEP3LEeKrJqcAep-lCg
	 */
//	@Override
//	public Boolean nodeHasChildren(Object node, String treeNode,
//			GenericTreeContext context) {
//		// TODO implement
//		return false;
//	}
	
	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode,
			GenericTreeContext context) {
		// TODO implement
		return false;
	}

}