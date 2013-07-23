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
package org.flowerplatform.codesync.remote;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.flowerplatform.communication.command.AbstractServerCommand;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.web.projects.remote.ProjectsService;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;

/**
 * @author Mariana
 */
public class CodeSyncAction extends AbstractServerCommand {

	public List<PathFragment> pathWithRoot;
	
	public String technology;
	
	@Override
	public void executeCommand() {
		IResource resource = ProjectsService.getInstance().getProjectWrapperResourceFromFile(pathWithRoot);
		CodeSyncPlugin.getInstance().getCodeSyncAlgorithmRunner().runCodeSyncAlgorithm(resource.getProject(), resource, technology, communicationChannel, true);
	}

}