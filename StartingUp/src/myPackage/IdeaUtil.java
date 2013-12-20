//package myPackage;
///* license-start
//*
//* Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
//*
//* This program is free software: you can redistribute it and/or modify
//* it under the terms of the GNU General Public License as published by
//* the Free Software Foundation version 3.
//*
//* This program is distributed in the hope that it will be useful,
//* but WITHOUT ANY WARRANTY; without even the implied warranty of
//* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//* GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
//*
//* Contributors:
//*   Crispico - Initial API and implementation
//*
//* license-end
//*/
//
//import java.io.File;
//
//import com.intellij.openapi.project.Project;
//
//import com.intellij.openapi.roots.ProjectRootManager;
//import com.intellij.openapi.vfs.VirtualFile;
//
//
//public class IdeaUtil{
//
//	/**
//	 * @param path relative to project/module
//	 */
//	public static File getFile(File project, String path) {
//    	return new File(project, path);
//	}
//
//	public static File getContainingProjectForFile(VirtualFile file) {
////		IResource wrapper = ProjectsService.getInstance().getProjectWrapperResourceFromFile(file);
////		if (wrapper == null) {
////			return null;
////		}
////		IProject project = wrapper.getProject();
////		if (project == null) {
////			return null;
////		}
////		return ProjectsService.getInstance().getFileFromProjectWrapperResource(project);
//
////        Project project = MyEditor.getEditorProject();
////        VirtualFile moduleSourceRoot = ProjectRootManager.getInstance(project).getFileIndex().getSourceRootForFile((VirtualFile)file);
//        //return moduleSourceRoot;
////        LocalFileSystem.getInstance().findFileByIoFile();
//        Project project = MyEditor.getEditorProject();
//        //new VirtualFileImpl();
//        VirtualFile moduleSourceRoot = ProjectRootManager.getInstance(project).getFileIndex().getSourceRootForFile(file);
//        //moduleSourceRoot.create
//        //return new File("D:/data/java_work/runtime-flower-platform-eclipse/test2");
//        return new File(MyEditor.getEditorProject().getBasePath());
//    }
//
//	public static String getPathRelativeToProject(File file) {
////		IResource wrapper = ProjectsService.getInstance().getProjectWrapperResourceFromFile(file);
////		return wrapper.getFullPath().toString();
//        Project project = MyEditor.getEditorProject();
//        String projectPath = project.getLocation().substring(0,project.getLocation().lastIndexOf("/"));
//        return (file).getPath().substring( (file).getPath().indexOf(projectPath )+ projectPath.length());
//	}
//
//    public String getPathRelativeToModule(VirtualFile file) {
//        Project project = MyEditor.getEditorProject();
//        VirtualFile moduleSourceRoot = ProjectRootManager.getInstance(project).getFileIndex().getSourceRootForFile(file);
//        String modulePath = moduleSourceRoot.getPath();
//        String filePath = file.getPath();
//
//        return filePath.substring(filePath.indexOf(modulePath) + modulePath.length());
//    }
//}
//
