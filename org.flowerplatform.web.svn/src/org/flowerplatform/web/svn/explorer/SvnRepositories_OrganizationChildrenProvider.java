package org.flowerplatform.web.svn.explorer;

import java.util.Collections;

import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IGenericTreeStatefulServiceAware;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.explorer.AbstractVirtualItemChildrenProvider;
import org.flowerplatform.web.svn.SvnNodeType;
import org.flowerplatform.web.svn.SvnPlugin;
import org.tigris.subversion.subclipse.core.SVNException;

/**
 * Parent node = Organization (i.e. File).<br/>
 * Child node is a virtual item, i.e. Pair<Org File, nodeType>.
 * 
 * @author Victor Badila
 * 
 * @flowerModelElementId _UEfo0P2kEeKrJqcAep-lCg
 */

public class SvnRepositories_OrganizationChildrenProvider extends AbstractVirtualItemChildrenProvider implements IGenericTreeStatefulServiceAware {
	/**
	 * @throws SVNException
	 * @flowerModelElementId _DjxSYP2tEeKrJqcAep-lCg
	 */

	public SvnRepositories_OrganizationChildrenProvider() throws SVNException {
		super();
		childNodeTypes = Collections.singletonList(SvnNodeType.NODE_TYPE_SVN_REPOSITORIES);
	}

	/**
	 * @flowerModelElementId _Upao4P2mEeKrJqcAep-lCg
	 */

	@Override
	public void setGenericTreeStatefulService(GenericTreeStatefulService genericTreeStatefulService) {
		SvnPlugin.getInstance().getTreeStatefulServicesDisplayingSvnContent().add(genericTreeStatefulService);
	}

	/**
	 * @flowerModelElementId _RA3y0P6AEeKrJqcAep-lCg
	 */
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
		return true;
	}
}