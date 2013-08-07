package org.flowerplatform.web.svn.explorer;


import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;

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
	public Collection<Par<Object, String>> getChildrenForNode(Object node,
			String treeNode, GenericTreeContext context) {
		// TODO implement
		return null;
	}

	/**
	 * @flowerModelElementId _z6dDEP3LEeKrJqcAep-lCg
	 */
	public boolean nodeHasChildren(Object node, String treeNode,
			GenericTreeContext context) {
		// TODO implement
		return 0;
	}
}