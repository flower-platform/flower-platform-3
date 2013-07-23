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

import org.flowerplatform.web.WebPlugin;
import org.flowerplatform.web.explorer.AbstractVirtualItemInOrganizationNodeDataProvider;

/**
 * Node is a virtual node, i.e. Pair<parent org, nodeType>.
 * 
 * @author Cristi
 */
public class WorkingDirectoriesNodeDataProvider extends AbstractVirtualItemInOrganizationNodeDataProvider {

	public WorkingDirectoriesNodeDataProvider() {
		super();
		nodeInfo.put(WorkingDirectories_OrganizationChildrenProvider.NODE_TYPE_WORKING_DIRECTORIES, 
				new String[] {
					WebPlugin.getInstance().getMessage("explorer.workingDirectories"), 
					WebPlugin.getInstance().getResourceUrl("images/workset.gif")});
	}

}