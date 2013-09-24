package org.flowerplatform.web.svn.explorer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.svn.SvnNodeType;
import org.flowerplatform.web.svn.remote.SvnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.repo.SVNRepositoryLocation;

/**
 * Parent node = Svn Repositories virtual node (i.e. Pair<File, String>).<br/>
 * Child node = svn repository
 * 
 * @author Victor Badila
 * 
 * @flowerModelElementId _UcYSoP3LEeKrJqcAep-lCg
 */
public class SvnRepository_SvnRepositoriesChildrenProvider implements IChildrenProvider {
	
	private static Logger logger = LoggerFactory.getLogger(SvnRepository_SvnRepositoriesChildrenProvider.class);
	
	/**
	 * 
	 * @flowerModelElementId _kWPuUP3LEeKrJqcAep-lCg
	 */

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {
		Collection<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>();
		String organizationName = ((Pair<File, String>) node).a.getAbsolutePath();
		organizationName = organizationName.substring(organizationName.lastIndexOf("\\") + 1);
		ArrayList<String> repositories = SvnService.getInstance().getRepositoriesForOrganization(organizationName);
		for (String repositoryLocationString : repositories) {
			try {
				SVNRepositoryLocation repositoryToBeAdded = SVNRepositoryLocation.fromString(repositoryLocationString);
				repositoryToBeAdded.setLabel(repositoryLocationString);
				Pair<Object, String> pair = new Pair<Object, String>(repositoryToBeAdded, SvnNodeType.NODE_TYPE_REPOSITORY);
				result.add(pair);
			} catch (SVNException e) {
				logger.debug(CommonPlugin.getInstance().getMessage("error", e));
			}
		}
		return result;
	}

	/**
	 * @flowerModelElementId _z6dDEP3LEeKrJqcAep-lCg
	 */

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
		return true; // for the moment
	}

}