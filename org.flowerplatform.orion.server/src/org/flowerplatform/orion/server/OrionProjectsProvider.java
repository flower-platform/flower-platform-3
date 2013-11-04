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
package org.flowerplatform.orion.server;

import java.io.File;

import org.eclipse.core.runtime.Path;
import org.flowerplatform.codesync.projects.IProjectsProvider;
import org.flowerplatform.common.CommonPlugin;

/**
 * @author Cristina Constantinescu
 */
public class OrionProjectsProvider implements IProjectsProvider {

	@Override
	public File getFile(File project, String path) {		
		return new File(project, path);
	}

	@Override
	public File getContainingProjectForFile(File file) {
		Path path = new Path(CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(file));
		return new File(CommonPlugin.getInstance().getWorkspaceRoot(), path.segment(0));		
	}

	@Override
	public String getPathRelativeToProject(File file) {		
		Path path = new Path(CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(file));
		File project = new File(CommonPlugin.getInstance().getWorkspaceRoot(), path.segment(0));
		return file.getAbsolutePath().substring(project.getAbsolutePath().length());		
	}
	
}
