package org.flowerplatform.web.svn.explorer;

import java.io.File;

import org.eclipse.core.resources.IResource;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.INodePopulator;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.projects.remote.ProjectsService;
import org.flowerplatform.web.svn.SvnPlugin;
import org.tigris.subversion.subclipse.core.resources.SVNWorkspaceRoot;


/**
 * 
 * @author Victor Badila 
 */
public class FSFile_SvnNodePopulator implements INodePopulator{
	
	private final String TREE_NODE_SVN_FILE_TYPE = "svnFileType";

	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		if (!(source instanceof Pair<?, ?> && ((Pair<?, ?>) source).a instanceof File)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		File file = ((Pair<File, String>) source).a;
		boolean isFileFromSvnRepository = SvnPlugin.getInstance().getUtils().isRepository(file);
//		boolean isFileFromSvnRepository; 
//		IResource res = (IResource) ProjectsService.getInstance().getProjectWrapperResourceFromFile((File)( (Pair) source).a); 
//		if (SVNWorkspaceRoot.getSVNResourceFor(res) != null)
//			isFileFromSvnRepository = true;
//		else 
//			isFileFromSvnRepository = false;
//		
		if(isFileFromSvnRepository) {
			destination.getOrCreateCustomData().put(TREE_NODE_SVN_FILE_TYPE, true);
		}
		return false;		
	}

}
