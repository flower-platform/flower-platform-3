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
package org.flowerplatform.web.projects.remote;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.file_event.FileEvent;
import org.flowerplatform.file_event.IFileEventListener;
import org.flowerplatform.web.WebPlugin;
import org.flowerplatform.web.database.DatabaseManager;
import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;
import org.flowerplatform.web.entity.EntityFactory;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.WorkingDirectory;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This service is registered programmatically in {@link WebPlugin}, to make
 * sure that it's instantiated after the {@link DatabaseManager} was created.
 * 
 * @author Cristian Spiescu
 */
public class ProjectsService implements IFileEventListener {

	private static Logger logger = LoggerFactory.getLogger(ProjectsService.class);

	public static ProjectsService getInstance() {
		return (ProjectsService) CommunicationPlugin.getInstance().getServiceRegistry().getService(SERVICE_ID);
	}

	public static final String SERVICE_ID = "projectsService";

	public static final String WORKSPACE_LOCATION_VAR = "WORKSPACE_LOC";

	public static final String PROJECT_WRAPPERS = "project-wrappers";

	public static final String LINK_TO_PROJECT = "link-to-project";

	public static final String PROJECT_WRAPPER_NAME_SEPARATOR = "=+=";

	public File getOrganizationDir(String organizationName) {
		return new File(CommonPlugin.getInstance().getWorkspaceRoot(), organizationName);
	}

	public String getOrganizationName(File organizationDir) {
		return organizationDir.getName();
	}

	protected Map<File, List<File>> workingDirectoryToProjectsMap = new HashMap<File, List<File>>();

	protected Map<File, Pair<File, IProject>> projectToWorkingDirectoryAndIProjectMap = new HashMap<File, Pair<File, IProject>>();

	public Map<File, List<File>> getWorkingDirectoryToProjectsMap() {
		return workingDirectoryToProjectsMap;
	}

	public Map<File, Pair<File, IProject>> getProjectToWorkingDirectoryAndIProjectMap() {
		return projectToWorkingDirectoryAndIProjectMap;
	}

	public ProjectsService() throws CoreException, URISyntaxException {
		super();
		CommonPlugin.getInstance().getFileEventDispatcher().addFileEventListener(this);
		{
			// workspace configuration: this should be done only once, because
			// these settings are persistent.
			// However, I don't see how we could see that "this == first time"
			// in a reliable manner. That's
			// why we do it every time, and it doesn't hurt as I don't think
			// they are expensive methods

			// turn off auto build
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IWorkspaceDescription description = workspace.getDescription();
			description.setAutoBuilding(false);
			workspace.setDescription(description);

			// add the workspace location to the variables map. In desktop
			// eclipse some default vars seem to be populated
			// automatically, but in the web config this doesn't seem to happen
			workspace.getPathVariableManager().setURIValue(WORKSPACE_LOCATION_VAR,
					CommonPlugin.getInstance().getWorkspaceRoot().toURI());
		}

		// fill the map with working directories
		@SuppressWarnings("unchecked")
		List<WorkingDirectory> workingDirectories = (List<WorkingDirectory>) new DatabaseOperationWrapper(
				new DatabaseOperation() {
					@Override
					public void run() {
						wrapper.setOperationResult(wrapper.findAll(WorkingDirectory.class));
					}
				}).getOperationResult();

		for (WorkingDirectory workingDirectory : workingDirectories) {
			File workingDirFile = new File(getOrganizationDir(workingDirectory.getOrganization().getName()),
					workingDirectory.getPathFromOrganization());
			workingDirectoryToProjectsMap.put(workingDirFile, new ArrayList<File>());
		}

		File workingDirectoryFile = null;
		for (IProject projectWrapper : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			// I don't use String.split, because it takes a regex, which means I
			// should have another constant,
			// because the current separator has regex special chars
			StringTokenizer st = new StringTokenizer(projectWrapper.getName(), PROJECT_WRAPPER_NAME_SEPARATOR);
			if (st.countTokens() < 3) {
				logger.warn(
						"Project = {} is invalid. We were expecting at minimum tree tokens (i.e. org/work_dir/proj), but got only = {}",
						projectWrapper, st.countTokens());
				continue;
			}

			// we begin directly with something like org/work_dir
			// at the end of the iteration, this will be the file of the project
			File currentFile = new File(CommonPlugin.getInstance().getWorkspaceRoot(), st.nextToken() + "/"
					+ st.nextToken());
			do {
				if (workingDirectoryToProjectsMap.get(currentFile) != null) {
					// found the working directory
					workingDirectoryFile = currentFile;
				}
				currentFile = new File(currentFile, st.nextToken());
			} while (st.hasMoreTokens());

			if (workingDirectoryFile == null) {
				logger.warn("Project = {} is invalid. Couldn't find correspondig WorkingDirectory", projectWrapper);
				continue;
			}

			if (!currentFile.exists() || !currentFile.isDirectory()) {
				logger.warn("Project = {} is invalid. The file = {} doesn't point to an existing directory",
						projectWrapper, currentFile);
				continue;
			}

			workingDirectoryToProjectsMap.get(workingDirectoryFile).add(currentFile);
			projectToWorkingDirectoryAndIProjectMap.put(currentFile, new Pair<File, IProject>(workingDirectoryFile,
					projectWrapper));
			if (logger.isDebugEnabled()) {
				logger.debug(
						"Project added succesfully in maps; project wrapper = {}, working directory = {}, project dir = {}",
						new Object[] { projectWrapper, workingDirectoryFile, currentFile });
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<WorkingDirectory> getWorkingDirectoriesForOrganizationName(final String organizationName) {
		return (List<WorkingDirectory>) new DatabaseOperationWrapper(new DatabaseOperation() {
			@Override
			public void run() {
				Query q = wrapper.createQuery(String.format(
						"SELECT e from WorkingDirectory e where e.organization.name = '%s'", organizationName));
				wrapper.setOperationResult(q.list());
			}
		}).getOperationResult();
	}

	public WorkingDirectory getWorkingDirectory(final String organizationName, final String pathFromOrganization) {
		return (WorkingDirectory) new DatabaseOperationWrapper(new DatabaseOperation() {

			@Override
			public void run() {
				Query q = wrapper.createQuery(String
						.format("SELECT e from WorkingDirectory e where e.organization.name = '%s' and e.pathFromOrganization = '%s' ",
								organizationName, pathFromOrganization));
				wrapper.setOperationResult(q.uniqueResult());
			}
		}).getOperationResult();
	}

	/**
	 * It's ugly because the method behaves differently/takes other params
	 * (based on <code>parameterIsWorkingDirectory</code>). But I do this
	 * because the code is similar, and I don't want to create classes to
	 * inherit, etc. So it's excused, because the purpose is code reuse.
	 * 
	 * @param channel
	 * @param errorTitleMessageKey
	 * @param workingDirectoryOrProject
	 * @param parameterIsWorkingDirectory
	 * @return A pair. The first element is a) the organization (if
	 *         parameterIsWorkingDirectory) or b) working directory (if
	 *         !parameterIsWorkingDirectory). The second element is a boolean
	 *         that indicates whether a message was sent to the client or not.
	 */
	protected Pair<File, Boolean> getOrganizationDirForWorkingDirectoryOrWorkindDirectoryForProject(
			CommunicationChannel channel, String errorTitleMessageKey, File workingDirectoryOrProject,
			boolean parameterIsWorkingDirectory) {
		File currentFile = workingDirectoryOrProject;
		// loop that does validations and find the organization dir
		while (currentFile != null && currentFile != CommonPlugin.getInstance().getWorkspaceRoot()) {
			if (projectToWorkingDirectoryAndIProjectMap.get(currentFile) != null) {
				// a parent is a project; error in both cases
				channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(WebPlugin
						.getInstance().getMessage(errorTitleMessageKey), WebPlugin.getInstance().getMessage(
						"explorer.markAsWorkingDirectory.error.isInProj",
						CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(workingDirectoryOrProject),
						CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(currentFile)),
						DisplaySimpleMessageClientCommand.ICON_ERROR));
				return new Pair<File, Boolean>(null, true);
			}

			if (workingDirectoryToProjectsMap.get(currentFile) != null) {
				// a parent is a working directory
				if (!parameterIsWorkingDirectory) {
					// I'm looking for the working dir for the project param; so
					// we'are happy of finding it
					return new Pair<File, Boolean>(currentFile, false);
				} else {
					// I'm looking for the org dir; we found a working dir,
					// which is invalid => error
					channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(WebPlugin
							.getInstance().getMessage(errorTitleMessageKey), WebPlugin.getInstance().getMessage(
							"explorer.markAsWorkingDirectory.error.isInWD",
							CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(workingDirectoryOrProject),
							CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(currentFile)),
							DisplaySimpleMessageClientCommand.ICON_ERROR));
					return new Pair<File, Boolean>(null, true);
				}
			}

			if (parameterIsWorkingDirectory
					&& CommonPlugin.getInstance().getWorkspaceRoot().equals(currentFile.getParentFile())) {
				// we found the org dir
				return new Pair<File, Boolean>(currentFile, false);

			}
			currentFile = currentFile.getParentFile();
		}

		return new Pair<File, Boolean>(null, false);
	}

	public File getFileFromProjectWrapperResource(IResource resource) {
		IProject projectWrapper = resource.getProject();
		// I don't use String.split, because it takes a regex, which means I
		// should have another constant,
		// because the current separator has regex special chars
		StringTokenizer st = new StringTokenizer(projectWrapper.getName(), PROJECT_WRAPPER_NAME_SEPARATOR);
		if (st.countTokens() < 3) {
			logger.warn(
					"Project = {} is invalid. We were expecting at minimum tree tokens (i.e. org/work_dir/proj), but got only = {}",
					projectWrapper, st.countTokens());
			return null;
		}

		// we begin directly with something like org/work_dir
		// at the end of the iteration, this will be the file of the project
		File currentFile = new File(CommonPlugin.getInstance().getWorkspaceRoot(), st.nextToken() + "/"
				+ st.nextToken());
		do {
			currentFile = new File(currentFile, st.nextToken());
		} while (st.hasMoreTokens());

		if (resource instanceof IProject) {
			return currentFile;
		} else {
			return new File(currentFile, resource.getFullPath()
					.makeRelativeTo(resource.getProject().getFullPath().append(LINK_TO_PROJECT)).toFile().getPath());
		}
	}

	@RemoteInvocation
	public void markAsWorkingDirectory(ServiceInvocationContext context, List<PathFragment> pathWithRoot) {
		@SuppressWarnings("unchecked")
		File file = ((Pair<File, String>) GenericTreeStatefulService.getNodeByPathFor(pathWithRoot, null)).a;
		markAsWorkingDirectoryForFile(context, file);
	}

	public void markAsWorkingDirectoryForFile(ServiceInvocationContext context, final File file) {
		final Pair<File, Boolean> organizationDir = getOrganizationDirForWorkingDirectoryOrWorkindDirectoryForProject(
				context.getCommunicationChannel(), "explorer.markAsWorkingDirectory.error.title", file, true);

		if (organizationDir.a == null) {
			if (organizationDir.b) {
				// we didn't find it, but it's a validation error; not an
				// illegal state; a message was sent to the client
				return;
			} else {
				// it's very wrong if we have this case, i.e. don't find the
				// organization
				throw new IllegalArgumentException("Couldn't find organization for input = " + file);
			}
		} else {
			if (file.equals(organizationDir.a)) {
				// this shouldn't happen, as the action is not visible on
				// organization nodes
				throw new IllegalArgumentException("The prosed working directory and organization dir are the same: "
						+ file);
			}
		}

		new DatabaseOperationWrapper(new DatabaseOperation() {
			@Override
			public void run() {
				String relativePath = CommonPlugin.getInstance().getPathRelativeToFile(file, organizationDir.a);
				List<Organization> orgs = (List<Organization>) wrapper.findByField(Organization.class, "name",
						getOrganizationName(organizationDir.a));
				if (orgs.isEmpty()) {
					throw new IllegalArgumentException("Cannot find organization with name = "
							+ getOrganizationName(organizationDir.a));
				}
				WorkingDirectory wd = EntityFactory.eINSTANCE.createWorkingDirectory();
				wd.setPathFromOrganization(relativePath);
				wd.setOrganization(orgs.get(0));
				wrapper.getSession().persist(wd);
			}
		});
		workingDirectoryToProjectsMap.put(file, new ArrayList<File>());
		if (logger.isDebugEnabled()) {
			logger.debug("In Organization = {}, added a new Working Directory = {}",
					getOrganizationName(organizationDir.a), file);
		}
	}

	@RemoteInvocation
	public void createOrImportProject(ServiceInvocationContext context, List<PathFragment> pathWithRoot)
			throws CoreException, URISyntaxException {
		@SuppressWarnings("unchecked")
		File file = ((Pair<File, String>) GenericTreeStatefulService.getNodeByPathFor(pathWithRoot, null)).a;
		createOrImportProjectFromFile(context, file);
	}

	public void createOrImportProjectFromFile(ServiceInvocationContext context, final File file)
			throws URISyntaxException, CoreException {
		final Pair<File, Boolean> workingDirectoryDir = getOrganizationDirForWorkingDirectoryOrWorkindDirectoryForProject(
				context.getCommunicationChannel(), "explorer.createOrImportProject.error.title", file, false);

		if (workingDirectoryDir.a == null) {
			if (workingDirectoryDir.b) {
				// we didn't find it, but it's a validation error; not an
				// illegal state; a message was sent to the client
			} else {
				// the file is not in a working dir
				context.getCommunicationChannel().appendCommandToCurrentHttpResponse(
						new DisplaySimpleMessageClientCommand(WebPlugin.getInstance().getMessage(
								"explorer.createOrImportProject.error.title"), WebPlugin.getInstance().getMessage(
								"explorer.createOrImportProject.error.cannotFindWD",
								CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(file)),
								DisplaySimpleMessageClientCommand.ICON_ERROR));
			}
			return;
		}

		// create the project in the workspace
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot wsRoot = workspace.getRoot();
		String projectPath = CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(file);
		if (projectPath.indexOf(PROJECT_WRAPPER_NAME_SEPARATOR) >= 0) {
			// ooops; the dir contains the separator; not good! I hope this
			// won't happen
			throw new UnsupportedOperationException(String.format(
					"The project to import contains our separator = %s in its name = $s",
					PROJECT_WRAPPER_NAME_SEPARATOR, projectPath));
		}
		String projectWrapperName = projectPath.replace("/", PROJECT_WRAPPER_NAME_SEPARATOR);
		String organizationDirName = projectPath.substring(0, projectPath.indexOf('/'));
		// e.g.
		// WORKSPACE_LOC/myOrg/project-wrappers/myOrg.git_repos.work_dir1.proj1
		URI projectWrapperLocationURI = new URI(WORKSPACE_LOCATION_VAR + "/" + organizationDirName + "/"
				+ PROJECT_WRAPPERS + "/" + projectWrapperName);

		IProject projectWrapper = wsRoot.getProject(projectWrapperName);
		if (projectWrapper.exists()) {
			logger.warn("The project {} was found in the workspace while trying to import it; deleting it.",
					projectWrapper);
			projectWrapper.delete(true, null);
		}

		IStatus status = workspace.validateProjectLocationURI(projectWrapper, projectWrapperLocationURI);
		if (!status.isOK()) {
			throw new RuntimeException(String.format(
					"The project location URI = %s is not valid; we have the following validation error message = %s",
					projectWrapperLocationURI, status.getMessage()));
		}

		IProjectDescription pd = ResourcesPlugin.getWorkspace().newProjectDescription(projectWrapper.getName());
		pd.setLocationURI(projectWrapperLocationURI);
		projectWrapper.create(pd, null);
		projectWrapper.open(null);

		// create the linked folder, from the project wrapper -> the project
		// itself
		IFolder linkToProjectFolder = projectWrapper.getFolder(LINK_TO_PROJECT);
		URI linkToProjectURI = new URI(WORKSPACE_LOCATION_VAR + "/" + projectPath);
		status = workspace.validateLinkLocationURI(linkToProjectFolder, linkToProjectURI);
		if (!status.isOK()) {
			throw new RuntimeException(String.format(
					"The linkToProjectURI = %s is not valid; we have the following validation error message = %s",
					linkToProjectURI, status.getMessage()));
		}
		linkToProjectFolder.createLink(linkToProjectURI, IResource.NONE, null);

		workingDirectoryToProjectsMap.get(workingDirectoryDir.a).add(file);
		projectToWorkingDirectoryAndIProjectMap.put(file, new Pair<File, IProject>(workingDirectoryDir.a,
				projectWrapper));

		if (logger.isDebugEnabled()) {
			logger.debug("In Working Directory = {}, added a new Project = {}", workingDirectoryDir.a, file);
		}
	}

	/**
	 * @author Tache Razvan Mihai
	 * 
	 * @param file
	 */
	public void deleteProject(final File file) {

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot wsRoot = workspace.getRoot();

		String projectPath = CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(file);
		String projectWrapperName = projectPath.replace("/", ProjectsService.PROJECT_WRAPPER_NAME_SEPARATOR);
		IProject projectWrapper = wsRoot.getProject(projectWrapperName);

		if (projectWrapper.exists()) {

			IFolder linkToProjectFolder = projectWrapper.getFolder(ProjectsService.LINK_TO_PROJECT);

			try {
				// TODO Decide if needed
				// IStatus status =
				// workspace.validateLinkLocationURI(linkToProjectFolder,
				// linkToProjectURI);
				// if (!status.isOK()) {
				// throw new
				// RuntimeException(String.format("The linkToProjectURI = %s is not valid; we have the following validation error message = %s",
				// linkToProjectURI, status.getMessage()));
				// }
				projectWrapper.delete(true, null);
				linkToProjectFolder.delete(true, null);
			} catch (CoreException e) {
				// TODO the component I need to display messages, is waiting to
				// be merged within flower/main
				// FileManagerService.printSimpleMessageToUser(context,
				// "Error",
				// e.getMessage() + " Check the console for more informations");
				e.printStackTrace();
			}
			workingDirectoryToProjectsMap.get(projectToWorkingDirectoryAndIProjectMap.get(file).a).remove(file);
			projectToWorkingDirectoryAndIProjectMap.remove(file);
		} else {
			// FileManagerService.printSimpleMessageToUser(context, "error",
			// "Project doesn't exist");
		}
	}

	public void renameProject(final File file, final File newFile) {
		try {
			renameProject(file, newFile, null);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void renameProject(final File file, final File newFile, File newWorkingDirectory) throws URISyntaxException {

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot wsRoot = workspace.getRoot();

		String projectPath = CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(file);
		String projectWrapperName = projectPath.replace("/", ProjectsService.PROJECT_WRAPPER_NAME_SEPARATOR);
		String newProjectPath = CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(newFile);
		String newProjectWrapperName = newProjectPath.replace("/", ProjectsService.PROJECT_WRAPPER_NAME_SEPARATOR);

		String organizationDirName = newProjectPath.substring(0, newProjectPath.indexOf('/'));

		File projectWrapperFile = new File(workspace.getRoot().getLocationURI().getPath(), organizationDirName + "/"
				+ PROJECT_WRAPPERS + "/" + projectWrapperName);
		File newProjectWrapperFile = new File(workspace.getRoot().getLocationURI().getPath(), organizationDirName + "/"
				+ PROJECT_WRAPPERS + "/" + newProjectWrapperName);
		if (newProjectPath.indexOf(PROJECT_WRAPPER_NAME_SEPARATOR) >= 0) {
			// ooops; the dir contains the separator; not good! I hope this
			// won't happen
			throw new UnsupportedOperationException(String.format(
					"The project to import contains our separator = %s in its name = $s",
					PROJECT_WRAPPER_NAME_SEPARATOR, projectPath));
		}

		URI newProjectWrapperLocationURI = new URI(WORKSPACE_LOCATION_VAR + "/" + organizationDirName + "/"
				+ PROJECT_WRAPPERS + "/" + newProjectWrapperName);

		IProject projectWrapper = wsRoot.getProject(projectWrapperName);
		IProjectDescription description = null;
		IProject newProjectWrapper = null;
		try {
			description = projectWrapper.getDescription();
			description.setLocationURI(newProjectWrapperLocationURI);

			IFolder linkToProjectFolder = projectWrapper.getFolder(ProjectsService.LINK_TO_PROJECT);
			linkToProjectFolder.delete(true, null);
			projectWrapper.delete(false, true, null);

			boolean result = projectWrapperFile.renameTo(newProjectWrapperFile);
			if (result) {
				newProjectWrapper = wsRoot.getProject(newProjectWrapperName);
				newProjectWrapper.create(description, null);
				if (newProjectWrapper.exists()) {

					// set the new name and uri in the description
					newProjectWrapper.open(null);
					description = newProjectWrapper.getDescription();
					description.setName(newProjectWrapperName);

					// update the project
					newProjectWrapper.move(description, true, null);

					// create the linked folder, from the project wrapper -> the
					// project itself
					linkToProjectFolder = newProjectWrapper.getFolder(LINK_TO_PROJECT);
					URI linkToProjectURI = new URI(WORKSPACE_LOCATION_VAR + "/" + newProjectPath);
					linkToProjectFolder.createLink(linkToProjectURI, IResource.NONE, null);

					// update the maps
					File workingDirectory = projectToWorkingDirectoryAndIProjectMap.get(file).a;
					if (newWorkingDirectory == null) {
						newWorkingDirectory = workingDirectory;
					}
					int index = workingDirectoryToProjectsMap.get(newWorkingDirectory).indexOf(file);
					workingDirectoryToProjectsMap.get(newWorkingDirectory).set(index, newFile);
					projectToWorkingDirectoryAndIProjectMap.remove(file);
					projectToWorkingDirectoryAndIProjectMap.put(newFile, new Pair<File, IProject>(newWorkingDirectory,
							newProjectWrapper));
				} else {
					// TODO after getting the message method, implement it to
					// print an error
				}

			} else {
				// TODO after getting the message method, implement it to print
				// an error
			}
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
	}

	public IResource getProjectWrapperResourceFromFile(List<PathFragment> pathWithRoot) {
		@SuppressWarnings("unchecked")
		File file = ((Pair<File, String>) GenericTreeStatefulService.getNodeByPathFor(pathWithRoot, null)).a;
		return getProjectWrapperResourceFromFile(file);
	}

	public IResource getProjectWrapperResourceFromFile(final File file) {
		File currentFile = file;
		IProject projectWrapper = null;
		while (currentFile != null && currentFile != CommonPlugin.getInstance().getWorkspaceRoot()) {
			Pair<File, IProject> pair = projectToWorkingDirectoryAndIProjectMap.get(currentFile);
			if (pair != null) {
				projectWrapper = pair.b;
				break;
			}
			currentFile = currentFile.getParentFile();
		}
		// at the end of the loop, current file will be pointing toward the
		// project file

		if (projectWrapper == null) {
			// the path doesn't point on a file within a project
			return null;
		}

		String relativePath = CommonPlugin.getInstance().getPathRelativeToFile(file, currentFile);
		if (relativePath.isEmpty()) {
			return projectWrapper;
		}
		String pathInProjectWrapper = LINK_TO_PROJECT + "/" + relativePath;
		if (file.isDirectory()) {
			return projectWrapper.getFolder(pathInProjectWrapper);
		} else {
			return projectWrapper.getFile(pathInProjectWrapper);
		}

	}

	public String getOrganizationNameFromFile(File file) {
		String relativePathToWorkspace = CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(file);
		String[] folders = relativePathToWorkspace.split("/");
		return folders[0];
	}

	@Override
	public void notify(FileEvent event) {

		File file = event.getFile();
		String org = ProjectsService.getInstance().getOrganizationNameFromFile(event.getFile());
		File orgDir = getOrganizationDir(org);
		if (event.getEvent() == FileEvent.FILE_RENAMED) {
			file = event.getOldFile();
		}
		if (workingDirectoryToProjectsMap.get(file) != null) {
			// it's an working directory
			String pathFromOrgForFile = getRelativePathFromOrganization(file);
			WorkingDirectory workingDirectory = getWorkingDirectory(org, pathFromOrgForFile);
			if (event.getEvent() == FileEvent.FILE_DELETED) {
				deleteWorkingDirectory(event.getFile(), workingDirectory);
			} else if(event.getEvent() == FileEvent.FILE_RENAMED) {
				renameWorkingDirectory(file, event.getFile());
			}
		} else if (projectToWorkingDirectoryAndIProjectMap.get(file) != null) {
			// it's an project
			if (event.getEvent() == FileEvent.FILE_DELETED) {
				deleteProject(event.getFile());
			} else if(event.getEvent() == FileEvent.FILE_RENAMED) {
				renameProject(file, event.getFile());
			}
		} else {
			File fileIterator = file.getParentFile();
			while (!fileIterator.equals(orgDir)) {
				if (workingDirectoryToProjectsMap.get(fileIterator) != null) {
					// has found an working directory to the left
					List<File> projectFiles = workingDirectoryToProjectsMap.get(fileIterator);
					ListIterator<File> iter = projectFiles.listIterator();
					while (iter.hasNext()) {
						File projectFile = (File) iter.next();
						if (projectFile.getPath().contains(file.getPath())) {

							if (event.getEvent() == FileEvent.FILE_DELETED) {
								iter.remove();
								deleteProject(projectFile);
							} else if(event.getEvent() == FileEvent.FILE_RENAMED) {
								String projectPath = projectFile.getPath();
								String newProjectPath = projectPath.replace(event.getOldFile().getPath(), event
										.getFile().getPath());
								renameProject(projectFile, new File(newProjectPath));
							}
						}
					}
					break;
				}
				fileIterator = fileIterator.getParentFile();
			}

			if (fileIterator.equals(orgDir)) {
				List<WorkingDirectory> workingDirectories = ProjectsService.getInstance()
						.getWorkingDirectoriesForOrganizationName(org);
				ListIterator<WorkingDirectory> iter = workingDirectories.listIterator();
				while (iter.hasNext()) {
					WorkingDirectory workingDirectory = (WorkingDirectory) iter.next();
					if (workingDirectory.getPathFromOrganization().contains(getRelativePathFromOrganization(file))) {
						if (event.getEvent() == FileEvent.FILE_DELETED) {
							deleteWorkingDirectory(new File(orgDir, workingDirectory.getPathFromOrganization()),
									workingDirectory);
						} else if(event.getEvent() == FileEvent.FILE_RENAMED) {
							File oldWD = new File(orgDir, workingDirectory.getPathFromOrganization());
							String oldWDpath = oldWD.getPath();
							String newWDpath = oldWDpath.replace(event.getOldFile().getPath(), event.getFile()
									.getPath());
							renameWorkingDirectory(oldWD, new File(newWDpath));
						}
					}
				}
			}
		}
	}

	private void renameWorkingDirectory(final File file, final File newFile) {
		final String org = ProjectsService.getInstance().getOrganizationNameFromFile(file);
		final File orgDir = getOrganizationDir(org);
		final String relativePath = CommonPlugin.getInstance().getPathRelativeToFile(file, orgDir);

		final String newOrg = ProjectsService.getInstance().getOrganizationNameFromFile(newFile);
		final File newOrgDir = getOrganizationDir(newOrg);
		final String newRelativePath = CommonPlugin.getInstance().getPathRelativeToFile(newFile, newOrgDir);

		new DatabaseOperationWrapper(new DatabaseOperation() {
			@Override
			public void run() {
				WorkingDirectory wd = getWorkingDirectory(org, relativePath);
				List<Organization> orgs = (List<Organization>) wrapper.findByField(Organization.class, "name",
						getOrganizationName(newOrgDir));
				if (orgs.isEmpty()) {
					throw new IllegalArgumentException("Cannot find organization with name = "
							+ getOrganizationName(newOrgDir));
				}
				wd.setPathFromOrganization(newRelativePath);
				// wd.setOrganization(orgs.get(0));
				wrapper.getSession().merge(wd);
			}
		});

		List<File> backupProjects = workingDirectoryToProjectsMap.remove(file);
		workingDirectoryToProjectsMap.put(newFile, backupProjects);

		List<File> projectFiles = workingDirectoryToProjectsMap.get(newFile);
		ListIterator<File> iter = projectFiles.listIterator();
		while (iter.hasNext()) {
			File projectFile = (File) iter.next();
			String projectPath = projectFile.getPath();
			String newProjectPath = projectPath.replace(file.getPath(), newFile.getPath());
			try {
				renameProject(projectFile, new File(newProjectPath), newFile);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	private void deleteWorkingDirectory(final File file, final WorkingDirectory workingDirectory) {
		String org = ProjectsService.getInstance().getOrganizationNameFromFile(file);
		final File orgDir = getOrganizationDir(org);

		List<File> projectFiles = workingDirectoryToProjectsMap.get(file);
		ListIterator<File> iter = projectFiles.listIterator();
		while (iter.hasNext()) {
			File projectFile = (File) iter.next();
			iter.remove();
			deleteProject(projectFile);
		}
		workingDirectoryToProjectsMap.remove(file);

		new DatabaseOperationWrapper(new DatabaseOperation() {
			@Override
			public void run() {
				List<Organization> orgs = (List<Organization>) wrapper.findByField(Organization.class, "name",
						getOrganizationName(orgDir));
				if (orgs.isEmpty()) {
					throw new IllegalArgumentException("Cannot find organization with name = "
							+ getOrganizationName(orgDir));
				}
				wrapper.getSession().delete(workingDirectory);
			}
		});
	}
	/**
	 * @author Tache Razvan Mihai
	 * @param file
	 * @return the path to the file from organiztion without "\" in the end if it's a simple file or with "\" in the end if it's a parent file : like in file.getParent();
	 *
	 */
	public String getRelativePathFromOrganization(File file) {
		String org = ProjectsService.getInstance().getOrganizationNameFromFile(file);
		File orgDir = getOrganizationDir(org);
		String path = file.getPath();
		String base = orgDir.getPath();
		String pathFromOrgForFile = new File(base).toURI().relativize(new File(path).toURI()).getPath();

		return pathFromOrgForFile;
	}

}