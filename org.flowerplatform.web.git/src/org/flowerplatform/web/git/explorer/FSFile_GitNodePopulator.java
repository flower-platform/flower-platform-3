package org.flowerplatform.web.git.explorer;

import java.io.File;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.INodePopulator;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.git.GitPlugin;

public class FSFile_GitNodePopulator implements INodePopulator {

	private static final String TREE_NODE_GIT_FILE_TYPE = "gitFileType";
	
	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		if (!(source instanceof Pair<?, ?> && ((Pair<?, ?>) source).a instanceof File)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		File file = ((Pair<File, String>) source).a;
		boolean isFileFromGitRepository = GitPlugin.getInstance().getUtils().isRepository(file);
		if (isFileFromGitRepository) {
			destination.getOrCreateCustomData().put(TREE_NODE_GIT_FILE_TYPE, true);
		}
		return false;
	}

}
