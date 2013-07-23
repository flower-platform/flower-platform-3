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
package org.flowerplatform.web.projects;

import org.flowerplatform.web.explorer.AbstractFileWrapperChildrenProvider;

/**
 * Parent node = projFile or project (both of them Pair<File, nodeType>).<br/>
 * Child node = projFile, i.e. Pair<File, nodeType>
 * 
 * There was no need to override {@link #getFile(Object)}.
 * 
 * @author Cristian Spiescu
 */
public class ProjFile_ProjectChildrenProvider extends AbstractFileWrapperChildrenProvider {

	protected static final String NODE_TYPE_PROJ_FILE = "projFile";
	
	@Override
	protected String getNodeType() {
		return NODE_TYPE_PROJ_FILE;
	}
	
}