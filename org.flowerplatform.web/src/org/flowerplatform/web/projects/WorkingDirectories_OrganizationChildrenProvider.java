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

import java.util.Collections;

import org.flowerplatform.web.explorer.AbstractVirtualItemChildrenProvider;

/**
 * Parent node = Organization (i.e. File).<br/>
 * Child node is a virtual node, i.e. Pair<Org File, nodeType>.
 * 
 * @author Cristian Spiescu
 */
public class WorkingDirectories_OrganizationChildrenProvider extends AbstractVirtualItemChildrenProvider {

	protected static final String NODE_TYPE_WORKING_DIRECTORIES = "workingDirectories";

	public WorkingDirectories_OrganizationChildrenProvider() {
		super();
		childNodeTypes = Collections.singletonList(NODE_TYPE_WORKING_DIRECTORIES);
	}

}