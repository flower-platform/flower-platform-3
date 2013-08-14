package org.flowerplatform.web.svn.explorer;

import java.util.ArrayList;
import java.util.Collection;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.tigris.subversion.subclipse.core.ISVNRemoteResource;
import org.tigris.subversion.subclipse.core.ISVNRepositoryLocation;
import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.repo.SVNRepositoryLocation;
import org.tigris.subversion.subclipse.core.resources.RemoteFile;
import org.tigris.subversion.subclipse.core.resources.RemoteFolder;

/**
 * Parent node = Svn Repository virtual node (i.e. Pair<File, String>).<br/>
 * Child node = svn file or svn folder
 * 
 * @author Victor Badila
 * 
 * @flowerModelElementId _UcYSoP3LEeKrJqcAep-lCg
 */
public class SvnFile_ChildrenProvider implements IChildrenProvider {

	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node,
			TreeNode treeNode, GenericTreeContext context) {
		
		
		Collection<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>();

		// we might want to consider getting rid of ISVNRepositoryLocation class and using only RemoteFolder
		if (node instanceof ISVNRepositoryLocation){
			try {
				
				ISVNRemoteResource[] children = ((SVNRepositoryLocation) node)
						.members(null);				
				for (ISVNRemoteResource child : children) {
					String nodeType = "svnFile";
					result.add(new Pair<Object, String>(child, nodeType));
				}	
				return result;	
			} catch (SVNException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		else if (node instanceof RemoteFolder){
			try {
				ISVNRemoteResource[] children = ((RemoteFolder) node).members(null);				
				for (ISVNRemoteResource child : children) {
					String nodeType = "svnFile";
					result.add(new Pair<Object, String>(child, nodeType));
				}	
				return result;	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		System.out.println("*** Problem while getting children: unknown node type ***");
		return null;	

	}

	/**
	 * @flowerModelElementId _z6dDEP3LEeKrJqcAep-lCg
	 */

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode,
			GenericTreeContext context) {		
	
		if(node instanceof RemoteFile)
			return false;
		
				return true;
//		if (node instanceof SVNRepositoryLocation)
//			try {				
//				if (((SVNRepositoryLocation) node).members(null) != null) // not sure if null in case of empty
//					return true;
//				else
//					return false;
//			} catch (SVNException e) {
//				e.printStackTrace();
//				return false;
//			}
//		
//		else if (node instanceof RemoteFolder) try {				
//				if (((RemoteFolder) node).members(null) != null) // not sure if null in case of empty
//					return true; 
//				else
//					return false;
//			} catch (TeamException e) {
//				e.printStackTrace();
//			} 
//			
//		return false;
	}

}