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
package org.flowerplatform.web.svn.explorer;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.INodePopulator;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.svn.SvnPlugin;
import org.tigris.subversion.subclipse.core.resources.RemoteFile;
import org.tigris.subversion.subclipse.core.resources.RemoteFolder;

/**
 * 
 * @author Victor Badila 
 */

public class SvnFile_NodePopulator implements INodePopulator {	
	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {		
		if(source instanceof RemoteFile) {			
			destination.getOrCreateCustomData().put(SvnPlugin.TREE_NODE_KEY_IS_FOLDER, false);
		} else if(source instanceof RemoteFolder) {
			destination.getOrCreateCustomData().put(SvnPlugin.TREE_NODE_KEY_IS_FOLDER, true);
		}		
		return false;
	}
}