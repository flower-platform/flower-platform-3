/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
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
		
//			Repository repository = GitPlugin.getInstance().getUtils().getRepository(file);			
//			try {
//				destination.setLabel(String.format("%s [%s]", 
//						destination.getLabel(),
//						repository.getBranch()));
//			} catch (IOException e) {
//			}
		}
		return false;
	}

}