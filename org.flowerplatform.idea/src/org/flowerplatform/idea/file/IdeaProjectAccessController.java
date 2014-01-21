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

import myPackage.FlowerVirtualFileWrapper;

import org.flowerplatform.codesync.projects.IProjectAccessController;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectLocator;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.LocalFileSystem;

/**
 * @author Solomon Sebastian
 */
public class IdeaProjectAccessController implements IProjectAccessController {

	/**
	 * @param path relative to module
	 */

	@Override
	public Object getFile(Object module, String path)  {
		String filePath = ((FlowerVirtualFileWrapper)module).getPath() + path;
		return new FlowerVirtualFileWrapper(filePath);
	}

	@Override
	public Object getContainingProjectForFile(Object file) {
		Project ideaProject = ProjectLocator.getInstance().guessProjectForFile(
				((FlowerVirtualFileWrapper) file).getVirtualFile());
		File moduleFile = new File(getModulePath(ideaProject,
				(FlowerVirtualFileWrapper) file));
		if (moduleFile.exists()) {
			return new FlowerVirtualFileWrapper(LocalFileSystem.getInstance()
					.findFileByIoFile(moduleFile));
		}
		return null;
	}

	@Override
	public String getPathRelativeToProject(Object file) {
		// return  absolute Path
		return ((FlowerVirtualFileWrapper)file).getPath();
	}

	@Override
	public Object getFolder(Object project, String path) {
        return getFile(project, path);
	}

    private String removeLastSegment(String path) {
        if (path.lastIndexOf("/") > 0) {
            return path.substring(0, path.lastIndexOf("/"));
        }
        return path;
    }
    
	private String getModulePath(Project project, FlowerVirtualFileWrapper file) {
		return removeLastSegment(ProjectRootManager.getInstance(project)
				.getFileIndex().getModuleForFile(file.getVirtualFile())
				.getModuleFilePath());
	}
	
}
