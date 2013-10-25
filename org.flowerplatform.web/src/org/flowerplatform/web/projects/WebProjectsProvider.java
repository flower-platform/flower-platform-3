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

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;
import org.flowerplatform.codesync.projects.IProjectsProvider;
import org.flowerplatform.web.projects.remote.ProjectsService;

/**
 * @author Mariana Gheorghe
 */
public class WebProjectsProvider implements IProjectsProvider {

	/**
	 * @param path relative to project
	 */
	@Override
	public File getFile(File project, String path) {
		IProject wrapper = (IProject) ProjectsService.getInstance().getProjectWrapperResourceFromFile(project);
		if (wrapper == null) {
			return null;
		}
		IResource resource = wrapper.getFile(new Path(ProjectsService.LINK_TO_PROJECT + "/" + path));
		if (resource == null) {
			return null;
		}
		return ProjectsService.getInstance().getFileFromProjectWrapperResource(resource);
	}

	@Override
	public File getContainingProjectForFile(File file) {
		IResource wrapper = ProjectsService.getInstance().getProjectWrapperResourceFromFile(file);
		if (wrapper == null) {
			return null;
		}
		IProject project = wrapper.getProject();
		if (project == null) {
			return null;
		}
		return ProjectsService.getInstance().getFileFromProjectWrapperResource(project);
	}

	@Override
	public String getPathRelativeToProject(File file) {
		IResource wrapper = ProjectsService.getInstance().getProjectWrapperResourceFromFile(file);
		return wrapper.getFullPath().toString();
	}
	
}
