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

import java.util.Collections;


/**
 * Parent node = Organization (i.e. File).<br/>
 * Child node is a virtual item, i.e. Pair<Org File, nodeType>.
 * 
 * @author Cristian Spiescu
 */
public class FileSystem_OrganizationChildrenProvider extends AbstractVirtualItemChildrenProvider {

	protected static final String NODE_TYPE_FILE_SYSTEM = "fileSystem";

	public FileSystem_OrganizationChildrenProvider() {
		super();
		childNodeTypes = Collections.singletonList(NODE_TYPE_FILE_SYSTEM);
	}

}