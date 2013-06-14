package org.flowerplatform.web.git.explorer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.explorer.RootChildrenProvider;

public class GitWorkingDirsChildrenProvider implements IChildrenProvider {

	public static final String GIT_REPOSITORIES_NAME = ".git-repositories";
	
	public static final String MAIN_WORKING_DIRECTORY_NAME = "main-working-directory";
	public static final String VIRTUAL_WORKING_DIRECTORIES_NAME = "virtual-working-directories";
		
	public File getOrganizationFile() {
		return new File(RootChildrenProvider.getWorkspaceRoot(), "org1/.gitRepositories/");
	}
	
	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {	
//		File[] children;
//		if (GitExplorerTreeStatefulService.ROOT_NODE_MARKER.equals(node)) {
//			children = getOrganizationFile().listFiles();
//		} else {
//			children =((File) node).listFiles();		
//		 }
//		Collection<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>(children.length);
//		for (File child : children) {
//			Pair<Object, String> pair = new Pair<Object, String>(child, GitExplorerTreeStatefulService.NODE_TYPE_FILE);
//			result.add(pair);
//		}
//	
		return null;
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
//		if (GitExplorerTreeStatefulService.ROOT_NODE_MARKER.equals(node)) {
//			return true;
//		}
//		File file = (File) node;
//		return file.isDirectory() && file.list().length > 0;
		return false;
	}

}
