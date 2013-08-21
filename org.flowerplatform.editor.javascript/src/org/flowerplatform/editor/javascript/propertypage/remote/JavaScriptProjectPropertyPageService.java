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
package org.flowerplatform.editor.javascript.propertypage.remote;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.ISchedulingRule;


import org.eclipse.wst.jsdt.core.*;
import org.eclipse.wst.jsdt.internal.core.JavaProject;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.editor.javascript.propertypage.remote.JavaScriptProjectPropertyPageService;
import org.flowerplatform.web.projects.remote.ProjectsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author Razvan Tache
 * @see JavaProjectPropertyPageService
 */
public class JavaScriptProjectPropertyPageService {
	
	private static final String SERVICE_ID = "javaScriptProjectPropertyPageService";

	private static final Logger logger = LoggerFactory.getLogger(JavaScriptProjectPropertyPageService.class);
		
	public static JavaScriptProjectPropertyPageService getInstance() {
		return (JavaScriptProjectPropertyPageService) CommunicationPlugin.getInstance().getServiceRegistry().getService(SERVICE_ID);
	}
		
	private boolean isJavaScriptProject(IProject project) {
		try {
			return project.hasNature(JavaScriptCore.NATURE_ID);
		} catch (CoreException e) {
			logger.error("Exception thrown while getting java nature for {}", project.getName(), e);
		}
		return false;
	}
	
	@RemoteInvocation
	public boolean hasJavaScriptNature(ServiceInvocationContext context, List<PathFragment> path) {
		@SuppressWarnings("unchecked")
		Pair<File, String> node = (Pair<File, String>) GenericTreeStatefulService.getNodeByPathFor(path, null);
		File projectFile = node.a;
		
		IProject project = ProjectsService.getInstance().getProjectToWorkingDirectoryAndIProjectMap().get(projectFile).b;
		return isJavaScriptProject(project);
	}
	
	public boolean setJavaScriptNature(ServiceInvocationContext context, List<PathFragment> path) {
		@SuppressWarnings("unchecked")
		Pair<File, String> node = (Pair<File, String>) GenericTreeStatefulService.getNodeByPathFor(path, null);
		File projectFile = node.a;
		
		final IProject project = ProjectsService.getInstance().getProjectToWorkingDirectoryAndIProjectMap().get(projectFile).b;
		
		ISchedulingRule rule = project.getWorkspace().getRuleFactory().modifyRule(project); // scheduling rule for modifying the project
		IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				IProjectDescription description = project.getDescription();
				String[] prevNatures = description.getNatureIds();
				String[] newNatures;
				if (isJavaScriptProject(project)) { // remove java nature
					newNatures = new String[prevNatures.length - 1];
					int i = 0;
					for (String nature : prevNatures) {
						if (!JavaScriptCore.NATURE_ID.equals(nature)) {
							newNatures[i++] = nature;
						}
					}
				} else { // add java nature				
					newNatures = new String[prevNatures.length + 1];
					System.arraycopy(prevNatures, 0, newNatures, 0,	prevNatures.length);
					newNatures[prevNatures.length] = JavaScriptCore.NATURE_ID;
				}
				description.setNatureIds(newNatures);
				project.setDescription(description, IResource.AVOID_NATURE_CONFIG, null);
			}
		};
		try {
			project.getWorkspace().run(runnable, rule, IWorkspace.AVOID_UPDATE, null);
		} catch (CoreException e) {
			logger.error("Exception thrown while setting java nature for {}", project.getName(), e);
			return false;
		}		
		return true;
	}
		
	public Object getClasspathEntries(ServiceInvocationContext context, List<PathFragment> path) {
		@SuppressWarnings("unchecked")
		Pair<File, String> node = (Pair<File, String>) GenericTreeStatefulService.getNodeByPathFor(path, null);
		File projectFile = node.a;
		File wd = ProjectsService.getInstance().getProjectToWorkingDirectoryAndIProjectMap().get(projectFile).a;
		
		IProject project = ProjectsService.getInstance().getProjectToWorkingDirectoryAndIProjectMap().get(projectFile).b;
		IJavaScriptProject javaScriptProject = JavaScriptCore.create(project);
				
		List<String> srcFolders = new ArrayList<String>();
		List<String> projects = new ArrayList<String>();
		List<String> libraries = new ArrayList<String>();
		
		
		try {
			if (!javaScriptProject.getJSDTScopeFile().exists()) {
				return null;
			}
			@SuppressWarnings("restriction")
			IIncludePathEntry[] entries = ((JavaProject)javaScriptProject).readFileEntriesWithException(null);
			for (IIncludePathEntry entry : entries) {
				if (entry.getEntryKind() == IIncludePathEntry.CPE_SOURCE) {
					srcFolders.add(entry.getPath().makeRelativeTo(project.getFullPath()).toFile().getPath());
				} else if (entry.getEntryKind() == IIncludePathEntry.CPE_PROJECT) {
					File file = ProjectsService.getInstance().getFileFromProjectWrapperResource(ResourcesPlugin.getWorkspace().getRoot().getProject(entry.getPath().lastSegment()));					
					projects.add(CommonPlugin.getInstance().getPathRelativeToFile(file, wd));
				} else if (entry.getEntryKind() == IIncludePathEntry.CPE_LIBRARY && entry.getContentKind() == IPackageFragmentRoot.K_BINARY) {
					IFile resource = ResourcesPlugin.getWorkspace().getRoot().getFile(entry.getPath());
					File file = ProjectsService.getInstance().getFileFromProjectWrapperResource(resource);
					libraries.add(CommonPlugin.getInstance().getPathRelativeToFile(file, wd));
				}
			}		
			
		} catch (CoreException | IOException e) {
			logger.error("Exception thrown while getting java classpath entries for {}", project.getName(), e);
			return null;
		}
		
		return new Object[] {srcFolders, projects, libraries};	
	}
	
	public boolean setClasspathEntries(ServiceInvocationContext context, List<PathFragment> path, List<String> srcFolders, List<String> projects, List<String> libraries) {
		@SuppressWarnings("unchecked")
		Pair<File, String> node = (Pair<File, String>) GenericTreeStatefulService.getNodeByPathFor(path, null);
		File projectFile = node.a;
		
		IProject project = ProjectsService.getInstance().getProjectToWorkingDirectoryAndIProjectMap().get(projectFile).b;
		IJavaScriptProject javaScriptProject = JavaScriptCore.create(project);
		File wd = ProjectsService.getInstance().getProjectToWorkingDirectoryAndIProjectMap().get(projectFile).a;
		
		List<IIncludePathEntry> entries = new ArrayList<IIncludePathEntry>();
		for (String srcFolder : srcFolders) {
			entries.add(JavaScriptCore.newSourceEntry(project.getFullPath().append(srcFolder)));
		}
		for (String projectName : projects) {		
			entries.add(JavaScriptCore.newProjectEntry(ProjectsService.getInstance().getProjectWrapperResourceFromFile(new File(wd, projectName)).getFullPath()));
		}		
		for (String library : libraries) {			
			entries.add(JavaScriptCore.newLibraryEntry(ProjectsService.getInstance().getProjectWrapperResourceFromFile(new File(wd, library)).getFullPath(), null, null));
		}
		// default entry
		entries.add(JavaScriptCore.newContainerEntry(new Path("org.eclipse.wdt.jsdt.launching.JRE_CONTAINER"), false));
		
		try {
			javaScriptProject.setRawIncludepath(entries.toArray(new IIncludePathEntry[entries.size()]), new NullProgressMonitor());
		} catch (JavaScriptModelException e) {
			logger.error("Exception thrown while setting java classpath entries for {}", project.getName(), e);
			return false;
		}
		
		return true;
	}
	
}
