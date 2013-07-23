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

import java.util.Collections;

import org.flowerplatform.communication.tree.IGenericTreeStatefulServiceAware;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.web.explorer.AbstractVirtualItemChildrenProvider;
import org.flowerplatform.web.git.GitNodeType;
import org.flowerplatform.web.git.GitPlugin;

/**
 * Parent node = Organization (i.e. File).<br/>
 * Child node is a virtual item, i.e. Pair<Org File, nodeType>.
 * 
 * @author Cristina Constantinescu
 */
public class GitRepositories_OrganizationChildrenProvider extends AbstractVirtualItemChildrenProvider implements IGenericTreeStatefulServiceAware {

	public GitRepositories_OrganizationChildrenProvider() {
		super();
		childNodeTypes = Collections.singletonList(GitNodeType.NODE_TYPE_GIT_REPOSITORIES);
	}
	
	@Override
	public void setGenericTreeStatefulService(GenericTreeStatefulService genericTreeStatefulService) {
		GitPlugin.getInstance().getTreeStatefulServicesDisplayingGitContent().add(genericTreeStatefulService);
	}

}