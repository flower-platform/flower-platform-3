package org.flowerplatform.web.svn.explorer;

import java.io.File;
import java.util.List;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.INodeByPathRetriever;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.projects.remote.ProjectsService;
import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.repo.SVNRepositoryLocation;

/**
 * 
 * @author Victor Badila
 * 
 * @flowerModelElementId _aF3AMP5mEeKrJqcAep-lCg
 */
public class SvnRepositoryNodeDataProvider implements INodeDataProvider, INodeByPathRetriever {

	/**
	 * @flowerModelElementId _iQKIIP5mEeKrJqcAep-lCg
	 */
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {		
		return new PathFragment(((SVNRepositoryLocation) node).getUrl().toString(), "svnRepository");
	}

	/**
	 * @flowerModelElementId _-KimAP5_EeKrJqcAep-lCg
	 */
	public String getInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> fullPath) {
		return null;
	}

	/**
	 * @flowerModelElementId _-K4kQP5_EeKrJqcAep-lCg
	 */
	public String getLabelForLog(Object node, String nodeType) {
		return null;
	}

	/**
	 * @flowerModelElementId _-LKREP5_EeKrJqcAep-lCg
	 */
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		destination.setLabel(((SVNRepositoryLocation) source).getLabel());
		// will use git specific icon for the moment
		destination.setIcon("servlet/public-resources/org.flowerplatform.web.svn/images/repository_rep.gif");
		return true;
	}

	/**
	 * @flowerModelElementId _-Lb94P5_EeKrJqcAep-lCg
	 */
	public boolean setInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> path, String text) {
		return false;
	}

	@Override
	public Object getNodeByPath(List<PathFragment> fullPath, GenericTreeContext context) {		
		String repositoryURL = fullPath.get(2).getName();
		try {
			return SVNRepositoryLocation.fromString(repositoryURL);		
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return null;		
	}

}