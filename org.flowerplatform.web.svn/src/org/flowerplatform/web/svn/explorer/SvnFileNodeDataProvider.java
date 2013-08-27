package org.flowerplatform.web.svn.explorer;

import java.io.File;
import java.util.List;

import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.INodeByPathRetriever;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.projects.remote.ProjectsService;
import org.flowerplatform.web.svn.SvnNodeType;
import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.repo.SVNRepositoryLocation;
import org.tigris.subversion.subclipse.core.resources.RemoteFolder;
import org.tigris.subversion.subclipse.core.resources.RemoteResource;

/**
 * 
 * @author Victor Badila
 * 
 * @flowerModelElementId _MDgtwP3MEeKrJqcAep-lCg
 */
public class SvnFileNodeDataProvider implements INodeDataProvider, INodeByPathRetriever {
	/**
	 * @flowerModelElementId _R5OFMP6CEeKrJqcAep-lCg
	 */
	public String getInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> fullPath) {
		return null;
	}

	/**
	 * @flowerModelElementId _R5nGwP6CEeKrJqcAep-lCg
	 */
	public String getLabelForLog(Object node, String nodeType) {
		return null;
	}

	/**
	 * @flowerModelElementId _R5724P6CEeKrJqcAep-lCg
	 */
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {
		String nodeSpecificPath = ((RemoteResource) node).getName();
		return new PathFragment(nodeSpecificPath, SvnNodeType.NODE_TYPE_FILE);
	}

	/**
	 * @flowerModelElementId _R6P_8P6CEeKrJqcAep-lCg
	 */
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		destination.setLabel(((RemoteResource) source).getName());
		if (source instanceof RemoteFolder) {
			destination.setIcon("servlet/public-resources/org.flowerplatform.web/images/folder.gif");
		} else {
			destination.setIcon("servlet/public-resources/org.flowerplatform.web/images/file.gif");
		}
		return true;
	}

	/**
	 * @flowerModelElementId _R6pBgP6CEeKrJqcAep-lCg
	 */
	public boolean setInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> path, String text) {
		return false;
	}
	

	@Override
	public Object getNodeByPath(List<PathFragment> fullPath, GenericTreeContext context) {
//		if (fullPath == null || fullPath.size() < ) {
//			throw new IllegalArgumentException("We were expecting a path with 1 item, but we got: " + fullPath);
//		}
		
		String remoteFilePath = new String();		
		String repositoryURL = fullPath.get(2).getName();
		
//		String remoteFilePath = fullPath.subList(3, fullPath.size());
		try {
			SVNRepositoryLocation repository = SVNRepositoryLocation.fromString(repositoryURL);
			
			for(int i = 3; i < fullPath.size(); i++) {			
				remoteFilePath += fullPath.get(i).getName();
				if (i < fullPath.size() - 1) {
					remoteFilePath += "/";		
				}
			}
			
//			remoteFilePath.concat(fullPath.get(num).getName());
			
			//remoteFilePath = fullPath.subList(3, fullPath.size());
			
			//remoteFilePath.concat(fullPath.get(fullPath.size() - 1));
			return repository.getRemoteFolder(remoteFilePath.toString());
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return null;		
	}
}