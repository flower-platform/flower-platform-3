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
import java.io.IOException;

import org.eclipse.jgit.lib.Repository;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.explorer.AbstractFileWrapperNodeDataProvider;
import org.flowerplatform.web.git.GitNodeType;
import org.flowerplatform.web.git.GitPlugin;
import org.flowerplatform.web.git.GitUtils;

/**
 * @author Cristina Constantienscu
 */
public class GitFileNodeDataProvider extends AbstractFileWrapperNodeDataProvider {

	protected File getFile(Object node) {
		if (node instanceof File) {			
			return (File) node;
		} 
		return super.getFile(node);		
	}
	
	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		super.populateTreeNode(source, destination, context);
	
		if (GitNodeType.NODE_TYPE_WDIR.equals(destination.getPathFragment().getType())) {			
			try {
				@SuppressWarnings("unchecked")
				Pair<File, String> pair = (Pair<File, String>) source;
				File wdirFile = pair.a;
				if (!GitUtils.MAIN_REPOSITORY.equals(wdirFile.getName())) {
					Repository repository = GitPlugin.getInstance().getUtils().getRepository(wdirFile);
					destination.setLabel(String.format("%s [%s]", destination.getLabel(), repository.getBranch()));
				}
			} catch (IOException e) {				
			}
		}
		return true;
	}
	
}