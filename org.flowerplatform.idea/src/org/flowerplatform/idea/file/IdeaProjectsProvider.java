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
package org.flowerplatform.idea.file;

import java.io.File;

import org.flowerplatform.codesync.projects.IProjectsProvider;

import myPackage.IdeaUtil;


/**
 * @author Mariana Gheorghe
 */
public class IdeaProjectsProvider implements IProjectsProvider {

	/**
	 * @param path relative to project
	 */
	@Override
	public File getFile(File project, String path) {
		return IdeaUtil.getFile(project, path);
	}

	@Override
	public File getContainingProjectForFile(File file) {
		return IdeaUtil.getContainingProjectForFile(file);
	}

	@Override
	public String getPathRelativeToProject(File file) {
		// TODO Auto-generated method stub
		return IdeaUtil.getPathRelativeToProject(file);
	}

	
	
}
