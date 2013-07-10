package org.flowerplatform.editor.java.propertypage.remote;

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
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JavaProject;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.web.projects.remote.ProjectsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristina Constantinescu
 */
public class JavaProjectPropertyPageService {

	private static final String SERVICE_ID = "javaProjectPropertyPageService";
	
	private static final Logger logger = LoggerFactory.getLogger(JavaProjectPropertyPageService.class);
		
	public static JavaProjectPropertyPageService getInstance() {
		return (JavaProjectPropertyPageService) CommunicationPlugin.getInstance().getServiceRegistry().getService(SERVICE_ID);
	}
	
	private boolean isJavaProject(IProject project) {
		try {
			return project.hasNature(JavaCore.NATURE_ID);
		} catch (CoreException e) {
			logger.error("Exception thrown while getting java nature for {}", project.getName(), e);
		}
		return false;
	}
	
	@RemoteInvocation
	public boolean hasJavaNature(ServiceInvocationContext context, List<PathFragment> path) {
		@SuppressWarnings("unchecked")
		Pair<File, String> node = (Pair<File, String>) GenericTreeStatefulService.getNodeByPathFor(path, null);
		File projectFile = node.a;
		
		IProject project = ProjectsService.getInstance().getProjectToWorkingDirectoryAndIProjectMap().get(projectFile).b;
		return isJavaProject(project);
	}
		
	public boolean setJavaNature(ServiceInvocationContext context, List<PathFragment> path) {
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
				if (isJavaProject(project)) { // remove java nature
					newNatures = new String[prevNatures.length - 1];
					int i = 0;
					for (String nature : prevNatures) {
						if (!JavaCore.NATURE_ID.equals(nature)) {
							newNatures[i++] = nature;
						}
					}
				} else { // add java nature				
					newNatures = new String[prevNatures.length + 1];
					System.arraycopy(prevNatures, 0, newNatures, 0,	prevNatures.length);
					newNatures[prevNatures.length] = JavaCore.NATURE_ID;
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
		IJavaProject javaProject = JavaCore.create(project);
				
		List<String> srcFolders = new ArrayList<String>();
		List<String> projects = new ArrayList<String>();
		List<String> libraries = new ArrayList<String>();
		
		try {
			if (!project.getFile(IJavaProject.CLASSPATH_FILE_NAME).exists()) {
				return null;
			}
			@SuppressWarnings("restriction")
			IClasspathEntry[][] entries = ((JavaProject) javaProject).readFileEntriesWithException(null);
			for (IClasspathEntry entry : entries[0]) {
				if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
					srcFolders.add(entry.getPath().makeRelativeTo(project.getFullPath()).toFile().getPath());
				} else if (entry.getEntryKind() == IClasspathEntry.CPE_PROJECT) {
					File file = ProjectsService.getInstance().getFileFromProjectWrapperResource(ResourcesPlugin.getWorkspace().getRoot().getProject(entry.getPath().lastSegment()));					
					projects.add(CommonPlugin.getInstance().getPathRelativeToFile(file, wd));
				} else if (entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY && entry.getContentKind() == IPackageFragmentRoot.K_BINARY) {
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
		IJavaProject javaProject = JavaCore.create(project);
		File wd = ProjectsService.getInstance().getProjectToWorkingDirectoryAndIProjectMap().get(projectFile).a;
		
		List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
		for (String srcFolder : srcFolders) {
			entries.add(JavaCore.newSourceEntry(project.getFullPath().append(srcFolder)));
		}
		for (String projectName : projects) {		
			entries.add(JavaCore.newProjectEntry(ProjectsService.getInstance().getProjectWrapperResourceFromFile(new File(wd, projectName)).getFullPath()));
		}		
		for (String library : libraries) {			
			entries.add(JavaCore.newLibraryEntry(ProjectsService.getInstance().getProjectWrapperResourceFromFile(new File(wd, library)).getFullPath(), null, null));
		}
		// default entry
		entries.add(JavaCore.newContainerEntry(new Path("org.eclipse.jdt.launching.JRE_CONTAINER"), false));
		
		try {
			javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), new NullProgressMonitor());
		} catch (JavaModelException e) {
			logger.error("Exception thrown while setting java classpath entries for {}", project.getName(), e);
			return false;
		}
		
		return true;
	}
	
}
