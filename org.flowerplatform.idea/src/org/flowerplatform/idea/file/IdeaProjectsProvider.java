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

import org.flowerplatform.codesync.projects.IProjectAccessController;

import myPackage.IdeaUtil;
import myPackage.MyEditor;


/**
 * @author Mariana Gheorghe
 */
public class IdeaProjectsProvider implements IProjectAccessController {

	/**
	 * @param path relative to project
	 */

	@Override
	public Object getFile(Object project, String path) {
		return new File((File)project, path);
		//return IdeaUtil.getFile(project, path);
	}

	@Override
	public Object getContainingProjectForFile(Object file) {
//		return IdeaUtil.getContainingProjectForFile(file);
		return new File("D:/data/java_work/runtime-flower-platform-idea/TestIdea");
	}

	@Override
	public String getPathRelativeToProject(Object file) {
		return IdeaUtil.getPathRelativeToProject((File)file);
	}

	@Override
	public Object getFolder(Object project, String path) {
//		return IdeaUtil.getFile(project, path);
		return null;
	}

	
	
}
