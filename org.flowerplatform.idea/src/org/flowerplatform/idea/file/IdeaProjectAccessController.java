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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import myPackage.FlowerDiagramEditor;


/**
 * @author Solomon Sebastian
 */
public class IdeaProjectAccessController implements IProjectAccessController {

	/**
	 * @param path relative to module
	 */

	@Override
	public Object getFile(Object module, String path)  {
		String filePath = ((VirtualFile)module).getPath() + path;
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
		return LocalFileSystem.getInstance().findFileByIoFile(file);
	}

	@Override
	public Object getContainingProjectForFile(Object file) {
        //TODO test
		Project ideaProject = FlowerDiagramEditor.getEditorProject();
        File moduleFile = new File(getModulePath(ideaProject, (VirtualFile)file));
		if (moduleFile.exists()) {
			return LocalFileSystem.getInstance().findFileByIoFile(moduleFile);
		}
		return null;
	}

	@Override
	public String getPathRelativeToProject(Object file) {
		//return  absolute Path

//		if (file == null){
//			return "";
//		}
//		return getPathRelativeToModule((VirtualFile)file);
		return ((VirtualFile)file).getPath();
	}

	@Override
	public Object getFolder(Object project, String path) {
		throw new UnsupportedOperationException();
	}

//	private String getPathRelativeToModule(VirtualFile file) {
//        Project project = FlowerDiagramEditor.getEditorProject();
//        VirtualFile moduleSourceRoot = ProjectRootManager.getInstance(project).getFileIndex().getSourceRootForFile(file);
//        String modulePath = removeLastSegment(removeLastSegment(moduleSourceRoot.getPath()));
//        String filePath = file.getPath();
//
//        return filePath.substring(filePath.indexOf(modulePath) + modulePath.length());
//    }

    private String removeLastSegment(String path) {
        if (path.lastIndexOf("/") > 0) {
            return path.substring(0, path.lastIndexOf("/"));
        }
        return path;
    }
    
    private String getModulePath(Project project, VirtualFile file) {
    	return removeLastSegment(ProjectRootManager.getInstance(project).getFileIndex().getModuleForFile(file).getModuleFilePath());
    }
	
}
