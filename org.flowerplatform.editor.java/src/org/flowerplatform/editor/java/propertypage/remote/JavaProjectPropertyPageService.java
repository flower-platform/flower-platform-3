package org.flowerplatform.editor.java.propertypage.remote;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.ClasspathEntry.AssertionFailedException;
import org.eclipse.jdt.internal.core.JavaProject;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.web.projects.remote.ProjectsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		
		IProject project = ProjectsService.getInstance().getProjectToWorkingDirectoryAndIProjectMap().get(projectFile).b;
		IJavaProject javaProject = JavaCore.create(project);
	
		try {
			if (!project.getFile(IJavaProject.CLASSPATH_FILE_NAME).exists()) {
				return null;
			}
			IClasspathEntry[][] entries = ((JavaProject) javaProject).readFileEntriesWithException(null);
			for (IClasspathEntry entry : entries[0]) {
				entry.getEntryKind();
			}
		} catch (AssertionFailedException | CoreException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;	
	}
}
