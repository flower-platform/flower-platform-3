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

import org.flowerplatform.web.explorer.AbstractFileWrapperChildrenProvider;
import org.flowerplatform.web.git.GitNodeType;

/**
 * Parent node = gitFile or git Working Directories virtual node (i.e. Pair<File, String>).<br/>
 * Child node is a Pair<File, String>.
 * 
 * @author Cristina Constantinescu
 */
public class GitFile_WorkingDirectoryChildrenProvider extends AbstractFileWrapperChildrenProvider {

	@Override
	protected String getNodeType() {
		return GitNodeType.NODE_TYPE_FILE;
	}

	protected File getFile(Object node) {
		if (node instanceof File) {			
			return (File) node;
		} 
		return super.getFile(node);		
	}
	
}