package org.flowerplatform.web.git.explorer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jgit.lib.Repository;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.git.GitNodeType;
import org.flowerplatform.web.git.GitPlugin;

/**
 * Parent node = Virtual node (Working Directories) (i.e. Pair<Repository, nodeType>).<br/>
 * Child node = File, i.e. Pair<File, nodeType>.
 * 
 * @author Cristina Constantinescu
 */
public class WorkingDirectory_VirtualItemChildrenProvider implements IChildrenProvider {
	
	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {	
		@SuppressWarnings("unchecked")
		Repository repository = ((Pair<Repository, String>) node).a;	
		File[] children = repository.getDirectory().getParentFile().getParentFile().listFiles();
		
		Collection<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>(children.length);
		for (File child : children) {		
			Repository repo = GitPlugin.getInstance().getUtils().getRepository(child);
			
			if (repo != null) {
				Pair<Object, String> pair = new Pair<Object, String>(child, GitNodeType.NODE_TYPE_WDIR);
				result.add(pair);
			}
		}		
		return result;
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
		return true;
	}

}
