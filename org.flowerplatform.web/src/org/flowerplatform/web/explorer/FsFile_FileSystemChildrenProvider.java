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
package org.flowerplatform.web.explorer;


/**
 * Parent node = fsFile or Working Directories virtual node (i.e. Pair<File, String>).<br/>
 * Child node is a Pair<File, String>.
 * 
 * There was no need
 * to override {@link #getFile(Object)} to get the file from the "File System" node because
 * it is stored in the same format: <code>Pair<file, node_type>/code> where the file points
 * to the organization dir, which is exactly what we need.
 * 
 * @author Cristian Spiescu
 */
public class FsFile_FileSystemChildrenProvider extends AbstractFileWrapperChildrenProvider {

	public static final String NODE_TYPE_FS_FILE = "fsFile";
	
	@Override
	protected String getNodeType() {
		return NODE_TYPE_FS_FILE;
	}
	
}