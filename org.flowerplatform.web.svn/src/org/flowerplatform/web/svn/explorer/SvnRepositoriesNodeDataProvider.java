package org.flowerplatform.web.svn.explorer;


import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.web.explorer.AbstractVirtualItemInOrganizationNodeDataProvider;
import org.flowerplatform.web.svn.SvnNodeType;
import org.flowerplatform.web.svn.SvnPlugin;

/**
 * 
 * @author Victor Badila
 * 
 * @flowerModelElementId _V-WRIP3KEeKrJqcAep-lCg
 */
public class SvnRepositoriesNodeDataProvider extends AbstractVirtualItemInOrganizationNodeDataProvider {
	/**
	 * @flowerModelElementId _YS4fkP3KEeKrJqcAep-lCg
	 */
	public SvnRepositoriesNodeDataProvider() {
		
		super();
		nodeInfo.put(SvnNodeType.NODE_TYPE_SVN_REPOSITORIES, 
				new String[] {
				SvnPlugin.getInstance().getMessage("svn.repositories"), 
				SvnPlugin.getInstance().getResourceUrl("images/repo_rep.gif")});
	}

	/**
	 * @flowerModelElementId _krrBMP3KEeKrJqcAep-lCg
	 */
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {		
		return new PathFragment(".svn-repositories", "svnRepositories");		
	}
}