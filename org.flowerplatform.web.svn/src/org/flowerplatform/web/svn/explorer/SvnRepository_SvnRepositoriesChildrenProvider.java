package org.flowerplatform.web.svn.explorer;

import java.util.ArrayList;
import java.util.Collection;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.svn.SvnNodeType;
import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.repo.SVNRepositoryLocation;

/**
 * * Parent node = Svn Repositories virtual node (i.e. Pair<File, String>).<br/>
 * Child node = svn repository
 * 
 * @author Victor Badila
 * 
 * @flowerModelElementId _UcYSoP3LEeKrJqcAep-lCg
 */
public class SvnRepository_SvnRepositoriesChildrenProvider implements
		IChildrenProvider {
	/**
	 * Intr-o prima faza, hardcodam la svn://csp1/flower2. Copiii sunt string
	 * 
	 * Apoi, se va lua din BD: get all svn repos for organization = crt. Copiii
	 * sunt SvnRepository
	 * 
	 * @flowerModelElementId _kWPuUP3LEeKrJqcAep-lCg
	 */

	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node,
			TreeNode treeNode, GenericTreeContext context) {

		Collection<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>();

		try {
			SVNRepositoryLocation rootLocation = SVNRepositoryLocation
					.fromString("svn://csp1/flower2");
			rootLocation.setLabel("svn://csp1/flower2");
			Pair<Object, String> hardcodedSvn = new Pair<Object, String>(
					rootLocation, SvnNodeType.NODE_TYPE_REPOSITORY);
			result.add(hardcodedSvn);
			return result;
		} catch (SVNException e) {
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * @flowerModelElementId _z6dDEP3LEeKrJqcAep-lCg
	 */

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode,
			GenericTreeContext context) {
		return true; // momentan
	}

}