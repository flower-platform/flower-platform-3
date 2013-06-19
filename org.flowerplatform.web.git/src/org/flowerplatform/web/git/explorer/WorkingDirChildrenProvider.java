package org.flowerplatform.web.git.explorer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.git.entity.FileNode;
import org.flowerplatform.web.git.entity.FolderNode;
import org.flowerplatform.web.git.entity.GitNode;
import org.flowerplatform.web.git.entity.GitNodeType;

/**
 * @author Cristina Constantienscu
 */
public class WorkingDirChildrenProvider implements IChildrenProvider {

	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {
		@SuppressWarnings("rawtypes")
		GitNode gitNode = (GitNode) node;
		File file = (File) gitNode.getObject();
		
		File[] children = file.listFiles();
		Collection<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>(children.length);
		for (File child : children) {
			if (child.isDirectory()) {
				FolderNode childNode = new FolderNode(gitNode, gitNode.getRepository(), child);
				
				Pair<Object, String> pair = new Pair<Object, String>(childNode, GitNodeType.NODE_TYPE_FOLDER);
				result.add(pair);
			} else {
				FileNode childNode = new FileNode(gitNode, gitNode.getRepository(), child);
				
				Pair<Object, String> pair = new Pair<Object, String>(childNode, GitNodeType.NODE_TYPE_FILE);
				result.add(pair);
			}		
		}		
		return result;
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
		@SuppressWarnings("rawtypes")
		GitNode gitNode = (GitNode) node;
		File file = (File) gitNode.getObject();
		
		return file.isDirectory() && file.list().length > 0;
	}

}
