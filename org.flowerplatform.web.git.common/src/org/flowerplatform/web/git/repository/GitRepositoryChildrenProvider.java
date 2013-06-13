package org.flowerplatform.web.git.repository;

import java.util.Collection;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.TreeNode;

public class GitRepositoryChildrenProvider implements IChildrenProvider {

	public static final String GIT_REPOSITORIES_NAME = ".git-repositories";
	
	public static final String MAIN_WORKING_DIRECTORY_NAME = "main-working-directory";
	public static final String VIRTUAL_WORKING_DIRECTORIES_NAME = "virtual-working-directories";
	
	public static final String NODE_TYPE_REPOSITORY = "repo";
	public static final String NODE_TYPE_LOCAL_BRANCHES = "lbr";
	public static final String NODE_TYPE_REMOTE_BRANCHES = "rbr";
	public static final String NODE_TYPE_BRANCH = "br";
	
	public static final String NODE_TYPE_TAGS = "tags";
	public static final String NODE_TYPE_TAG = "tag";
	
	public static final String NODE_TYPE_REMOTES = "rmts";
	public static final String NODE_TYPE_REMOTE = "rmt";
	
	public static final String NODE_TYPE_WDIRS = "wdirs";
	public static final String NODE_TYPE_FILE = "file";
	public static final String NODE_TYPE_FOLDER = "fld";
		
	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {
		
		return null;
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
		// TODO Auto-generated method stub
		return null;
	}

}
