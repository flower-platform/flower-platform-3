package org.flowerplatform.web.tests.listener;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.web.communication.RecordingTestWebCommunicationChannel;
import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;
import org.flowerplatform.web.entity.WorkingDirectory;
import org.flowerplatform.web.projects.remote.FileManagerService;
import org.flowerplatform.web.projects.remote.ProjectsService;
import org.hibernate.Query;
import org.junit.BeforeClass;
import org.junit.Test;

public class RenameListenerTest {

	private static final CommunicationChannel communicationChannel = new RecordingTestWebCommunicationChannel();

	private File fileSearched;

	public static File organization;

	public static ServiceInvocationContext context;

	public File getFileSearched() {
		return fileSearched;
	}

	public void setFileSearched(File fileSearched) {
		this.fileSearched = fileSearched;
	}

	private void searchFileInDir(File dir, String fileName) {
		File[] files = dir.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					searchFileInDir(f, fileName);
				}
			}
		}
		if (fileName.toLowerCase().equals(dir.getName().toLowerCase())) {
			setFileSearched(dir);
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

	@BeforeClass
	public static void setUp() {
		File workspaceRoot = CommonPlugin.getInstance().getWorkspaceRoot();
		String pathOfWorkspaceRoot = workspaceRoot.getPath();
		organization = new File(pathOfWorkspaceRoot + "/" + SetUpTestEnviroment.FOLDER_TO_BE_COPIED_FOR_RENAME);
		context = new ServiceInvocationContext(communicationChannel);
		SetUpTestEnviroment.populateWorkspace(context, SetUpTestEnviroment.EVENT_RENAME);
	}

	@Test
	public void renameProject() {
		String projectToBeRenamedName = "Project24";
		searchFileInDir(organization, projectToBeRenamedName);
		File projectToBeRenamed = getFileSearched();

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot wsRoot = workspace.getRoot();
		String workingDirectoryName = "WorkingDirectory16";
		searchFileInDir(organization, workingDirectoryName);
		File workingDirectory = getFileSearched();

		// check if the wrapper exists
		String projectPath = CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(projectToBeRenamed);
		String projectWrapperName = projectPath.replace("/", ProjectsService.PROJECT_WRAPPER_NAME_SEPARATOR);
		IProject projectWrapper = wsRoot.getProject(projectWrapperName);
		assertEquals(true, projectWrapper.exists());

		// check if the maps are ok
		Map<File, List<File>> workingDirectoryToProjectsMapBeforeRename = ProjectsService.getInstance()
				.getWorkingDirectoryToProjectsMap();
		Map<File, Pair<File, IProject>> projectToWorkingDirectoryAndIProjectMapBeforeRename = ProjectsService
				.getInstance().getProjectToWorkingDirectoryAndIProjectMap();
		int numberOfWorkingDirectoriesBeforeRename = workingDirectoryToProjectsMapBeforeRename.size();
		int numberOfProjectsInWorkingDirectory = workingDirectoryToProjectsMapBeforeRename.get(workingDirectory).size();
		int numberOfProjects = projectToWorkingDirectoryAndIProjectMapBeforeRename.size();

		assertEquals(true, workingDirectoryToProjectsMapBeforeRename.get(workingDirectory).contains(projectToBeRenamed));
		assertNotNull(projectToWorkingDirectoryAndIProjectMapBeforeRename.get(projectToBeRenamed));

		// do the rename
		String renamedFileName = "NewProject24";
		FileManagerService.getInstance().testRename(context, projectToBeRenamed, renamedFileName);
		searchFileInDir(organization, renamedFileName);
		File renamedFile = getFileSearched();

		// check the 2 file states
		assertEquals(false, projectToBeRenamed.exists());
		assertEquals(true, renamedFile.exists());
		assertEquals(false, projectWrapper.exists());

		String newProjectPath = CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(renamedFile);
		String newProjectWrapperName = newProjectPath.replace("/", ProjectsService.PROJECT_WRAPPER_NAME_SEPARATOR);
		IProject newProjectWrapper = wsRoot.getProject(newProjectWrapperName);
		assertEquals(true, newProjectWrapper.exists());

		// check the maps
		assertEquals(numberOfWorkingDirectoriesBeforeRename, workingDirectoryToProjectsMapBeforeRename.size());
		assertEquals(numberOfProjectsInWorkingDirectory, workingDirectoryToProjectsMapBeforeRename
				.get(workingDirectory).size());
		assertEquals(numberOfProjects, projectToWorkingDirectoryAndIProjectMapBeforeRename.size());

		assertEquals(true, workingDirectoryToProjectsMapBeforeRename.get(workingDirectory).contains(renamedFile));
		assertEquals(false, workingDirectoryToProjectsMapBeforeRename.get(workingDirectory)
				.contains(projectToBeRenamed));

		assertEquals(true, projectToWorkingDirectoryAndIProjectMapBeforeRename.containsKey(renamedFile));
		assertEquals(false, projectToWorkingDirectoryAndIProjectMapBeforeRename.containsKey(projectToBeRenamed));

		assertEquals(true,
				projectToWorkingDirectoryAndIProjectMapBeforeRename.get(renamedFile).b.equals(newProjectWrapper));
		assertEquals(false,
				projectToWorkingDirectoryAndIProjectMapBeforeRename.get(renamedFile).b.equals(projectWrapper));
	}

	@Test
	public void renamePathToProject() {
		String fileToBeRenamedName = "Folder39";
		searchFileInDir(organization, fileToBeRenamedName);
		File fileToBeRenamed = getFileSearched();

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot wsRoot = workspace.getRoot();

		// check if projects exist
		String[] projectsToBeRenamedNames = { "Project42", "Project43", "Project45" };
		String[] notRenamedProjectsNames = { "Project40", "Project41" };
		List<File> projectsToBeRenamed = new ArrayList<File>();
		List<File> notRenamedProjects = new ArrayList<File>();

		for (String renamedProjectName : projectsToBeRenamedNames) {
			searchFileInDir(organization, renamedProjectName);
			projectsToBeRenamed.add(getFileSearched());
		}

		for (String notRenamedProjectName : notRenamedProjectsNames) {
			searchFileInDir(organization, notRenamedProjectName);
			notRenamedProjects.add(getFileSearched());
		}

		// check if the maps are ok
		String workingDirectoryName = "WorkingDirectory15";
		searchFileInDir(organization, workingDirectoryName);
		File workingDirectory = getFileSearched();
		Map<File, List<File>> workingDirectoryToProjectsMapBeforeRename = ProjectsService.getInstance()
				.getWorkingDirectoryToProjectsMap();
		Map<File, Pair<File, IProject>> projectToWorkingDirectoryAndIProjectMapBeforeRename = ProjectsService
				.getInstance().getProjectToWorkingDirectoryAndIProjectMap();
		int numberOfWorkingDirectoriesBeforeRename = workingDirectoryToProjectsMapBeforeRename.size();
		int numberOfProjectsInWorkingDirectory = workingDirectoryToProjectsMapBeforeRename.get(workingDirectory).size();
		int numberOfProjects = projectToWorkingDirectoryAndIProjectMapBeforeRename.size();

		for (File projectToBeRenamed : projectsToBeRenamed) {
			assertEquals(true,
					workingDirectoryToProjectsMapBeforeRename.get(workingDirectory).contains(projectToBeRenamed));
			assertNotNull(projectToWorkingDirectoryAndIProjectMapBeforeRename.get(projectToBeRenamed));

			String projectPath = CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(projectToBeRenamed);
			String projectWrapperName = projectPath.replace("/", ProjectsService.PROJECT_WRAPPER_NAME_SEPARATOR);
			IProject projectWrapper = wsRoot.getProject(projectWrapperName);
			assertEquals(true, projectWrapper.exists());
		}

		// do the rename
		String renamedFileName = "NewFolder39";
		FileManagerService.getInstance().testRename(context, fileToBeRenamed, renamedFileName);
		searchFileInDir(organization, renamedFileName);
		File renamedFile = getFileSearched();

		// check the 2 file states
		assertEquals(false, fileToBeRenamed.exists());
		assertEquals(true, renamedFile.exists());

		// check the old projects

		for (File projectToBeRenamed : projectsToBeRenamed) {
			assertEquals(false,
					workingDirectoryToProjectsMapBeforeRename.get(workingDirectory).contains(projectToBeRenamed));
			assertEquals(false, projectToWorkingDirectoryAndIProjectMapBeforeRename.containsKey(projectToBeRenamed));

			String projectPath = CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(projectToBeRenamed);
			String projectWrapperName = projectPath.replace("/", ProjectsService.PROJECT_WRAPPER_NAME_SEPARATOR);
			IProject projectWrapper = wsRoot.getProject(projectWrapperName);
			assertEquals(false, projectWrapper.exists());
		}

		for (File notRenamedProject : notRenamedProjects) {
			assertEquals(true,
					workingDirectoryToProjectsMapBeforeRename.get(workingDirectory).contains(notRenamedProject));
			assertEquals(true, projectToWorkingDirectoryAndIProjectMapBeforeRename.containsKey(notRenamedProject));

			String projectPath = CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(notRenamedProject);
			String projectWrapperName = projectPath.replace("/", ProjectsService.PROJECT_WRAPPER_NAME_SEPARATOR);
			IProject projectWrapper = wsRoot.getProject(projectWrapperName);
			assertEquals(true, projectWrapper.exists());
		}

		// check the new project files
		List<File> projectsRenamed = new ArrayList<File>();
		for (String renamedProjectName : projectsToBeRenamedNames) {
			searchFileInDir(organization, renamedProjectName);
			projectsRenamed.add(getFileSearched());
		}
		for (File projectToBeRenamed : projectsRenamed) {
			assertEquals(true,
					workingDirectoryToProjectsMapBeforeRename.get(workingDirectory).contains(projectToBeRenamed));
			assertEquals(true, projectToWorkingDirectoryAndIProjectMapBeforeRename.containsKey(projectToBeRenamed));

			String projectPath = CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(projectToBeRenamed);
			String projectWrapperName = projectPath.replace("/", ProjectsService.PROJECT_WRAPPER_NAME_SEPARATOR);
			IProject projectWrapper = wsRoot.getProject(projectWrapperName);
			assertEquals(true, projectWrapper.exists());
		}

		// check maps
		assertEquals(numberOfWorkingDirectoriesBeforeRename, workingDirectoryToProjectsMapBeforeRename.size());
		assertEquals(numberOfProjectsInWorkingDirectory, workingDirectoryToProjectsMapBeforeRename
				.get(workingDirectory).size());
		assertEquals(numberOfProjects, projectToWorkingDirectoryAndIProjectMapBeforeRename.size());
	}

	@Test
	public void renameWorkingDirectory() {
		String workDirToBeRenamedName = "WorkingDirectory10";
		searchFileInDir(organization, workDirToBeRenamedName);
		File workDirToBeRenamed = getFileSearched();

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot wsRoot = workspace.getRoot();

		// check database
		List<WorkingDirectory> workDirs = getWorkingDirectoriesForOrganizationName(organization.getName());
		int workDirsNumber;
		String relativeNameFromOrg = ProjectsService.getInstance().getRelativePathFromOrganization(workDirToBeRenamed);
		WorkingDirectory workDir = getWorkingDirectory(organization.getName(),
				relativeNameFromOrg.substring(0, relativeNameFromOrg.length() - 1));
		if (workDirs.isEmpty()) {
			workDirsNumber = 0;
		} else {
			workDirsNumber = workDirs.size();
		}
		assertNotSame(0, workDirsNumber);
		assertEquals(true, workDirs.contains(workDir));

		// check maps before rename
		String[] projectsToBeRenamedNames = { "Project49", "Project50", "Project53", "Project54", "Project55",
				"Project58", "Project59" };
		List<File> projectsToBeRenamed = new ArrayList<File>();

		for (String renamedProjectName : projectsToBeRenamedNames) {
			searchFileInDir(organization, renamedProjectName);
			projectsToBeRenamed.add(getFileSearched());
		}

		Map<File, List<File>> workingDirectoryToProjectsMapBeforeRename = ProjectsService.getInstance()
				.getWorkingDirectoryToProjectsMap();
		Map<File, Pair<File, IProject>> projectToWorkingDirectoryAndIProjectMapBeforeRename = ProjectsService
				.getInstance().getProjectToWorkingDirectoryAndIProjectMap();
		int numberOfWorkingDirectoriesBeforeRename = workingDirectoryToProjectsMapBeforeRename.size();
		int numberOfProjects = projectToWorkingDirectoryAndIProjectMapBeforeRename.size();

		for (File projectToBeRenamed : projectsToBeRenamed) {
			assertEquals(true,
					workingDirectoryToProjectsMapBeforeRename.get(workDirToBeRenamed).contains(projectToBeRenamed));
			assertNotNull(projectToWorkingDirectoryAndIProjectMapBeforeRename.get(projectToBeRenamed));

			String projectPath = CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(projectToBeRenamed);
			String projectWrapperName = projectPath.replace("/", ProjectsService.PROJECT_WRAPPER_NAME_SEPARATOR);
			IProject projectWrapper = wsRoot.getProject(projectWrapperName);
			assertEquals(true, projectWrapper.exists());
		}

		assertNotNull(workingDirectoryToProjectsMapBeforeRename.get(workDirToBeRenamed));

		// make the rename
		String renamedFileName = "NewWorkingDirectory10";
		FileManagerService.getInstance().testRename(context, workDirToBeRenamed, renamedFileName);
		searchFileInDir(organization, renamedFileName);
		File renamedFile = getFileSearched();

		// check the 2 file states
		assertEquals(false, workDirToBeRenamed.exists());
		assertEquals(true, renamedFile.exists());

		// check the old projects
		assertEquals(false, workingDirectoryToProjectsMapBeforeRename.containsKey(workDirToBeRenamed));
		for (File projectToBeRenamed : projectsToBeRenamed) {
			assertEquals(false, projectToWorkingDirectoryAndIProjectMapBeforeRename.containsKey(projectToBeRenamed));

			String projectPath = CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(projectToBeRenamed);
			String projectWrapperName = projectPath.replace("/", ProjectsService.PROJECT_WRAPPER_NAME_SEPARATOR);
			IProject projectWrapper = wsRoot.getProject(projectWrapperName);
			assertEquals(false, projectWrapper.exists());
		}

		// check the new ones
		assertEquals(true, workingDirectoryToProjectsMapBeforeRename.containsKey(renamedFile));
		List<File> projectsRenamed = new ArrayList<File>();
		for (String renamedProjectName : projectsToBeRenamedNames) {
			searchFileInDir(organization, renamedProjectName);
			projectsRenamed.add(getFileSearched());
		}
		for (File projectToBeRenamed : projectsRenamed) {
			assertEquals(true, workingDirectoryToProjectsMapBeforeRename.get(renamedFile).contains(projectToBeRenamed));
			assertEquals(true, projectToWorkingDirectoryAndIProjectMapBeforeRename.containsKey(projectToBeRenamed));

			String projectPath = CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(projectToBeRenamed);
			String projectWrapperName = projectPath.replace("/", ProjectsService.PROJECT_WRAPPER_NAME_SEPARATOR);
			IProject projectWrapper = wsRoot.getProject(projectWrapperName);
			assertEquals(true, projectWrapper.exists());
		}

		// check maps
		assertEquals(numberOfWorkingDirectoriesBeforeRename, workingDirectoryToProjectsMapBeforeRename.size());
		assertEquals(numberOfProjects, projectToWorkingDirectoryAndIProjectMapBeforeRename.size());

		// check database
		workDirs = getWorkingDirectoriesForOrganizationName(organization.getName());
		String newRelativeNameFromOrg = ProjectsService.getInstance().getRelativePathFromOrganization(renamedFile);
		workDir = getWorkingDirectory(organization.getName(),
				newRelativeNameFromOrg.substring(0, newRelativeNameFromOrg.length() - 1));
		assertEquals(true, workDirs.contains(workDir));
		assertEquals(workDirsNumber, workDirs.size());
	}

	@Test
	public void renamePathToWorkingDirectory() {
		String fileToBeRenamedName = "Folder11";
		searchFileInDir(organization, fileToBeRenamedName);
		File fileToBeRenamed = getFileSearched();

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot wsRoot = workspace.getRoot();

		String[] projectsToBeRenamedNames = { "Project70", "Project73", "Project76", "Project69", "Project75",
				"Project82", "Project83", "Project84", "Project85", "Project87", "Project88" };
		String[] workDirsToBeRenamedNames = { "WorkingDirectory66", "WorkingDirectory67" };
		List<File> projectsToBeRenamed = new ArrayList<File>();
		List<File> workDirsToBeRenamed = new ArrayList<File>();
		for (String projectToBeRenamedName : projectsToBeRenamedNames) {
			searchFileInDir(organization, projectToBeRenamedName);
			projectsToBeRenamed.add(getFileSearched());
		}
		for (String workDirToBeRenamed : workDirsToBeRenamedNames) {
			searchFileInDir(organization, workDirToBeRenamed);
			workDirsToBeRenamed.add(getFileSearched());
		}

		// Check database
		List<WorkingDirectory> workDirs = getWorkingDirectoriesForOrganizationName(organization.getName());
		int workDirsNumber;
		if (workDirs.isEmpty()) {
			workDirsNumber = 0;
		} else {
			workDirsNumber = workDirs.size();
		}
		assertNotSame(0, workDirsNumber);

		for (File workDirToBeRenamed : workDirsToBeRenamed) {
			String relativeNameFromOrg = ProjectsService.getInstance().getRelativePathFromOrganization(
					workDirToBeRenamed);
			WorkingDirectory workDir = getWorkingDirectory(organization.getName(),
					relativeNameFromOrg.substring(0, relativeNameFromOrg.length() - 1));
			assertEquals(true, workDirs.contains(workDir));
		}

		// check maps
		Map<File, List<File>> workingDirectoryToProjectsMapBeforeRename = ProjectsService.getInstance()
				.getWorkingDirectoryToProjectsMap();
		Map<File, Pair<File, IProject>> projectToWorkingDirectoryAndIProjectMapBeforeRename = ProjectsService
				.getInstance().getProjectToWorkingDirectoryAndIProjectMap();
		int numberOfWorkingDirectoriesBeforeRename = workingDirectoryToProjectsMapBeforeRename.size();
		int numberOfProjects = projectToWorkingDirectoryAndIProjectMapBeforeRename.size();
		for (File projectToBeRenamed : projectsToBeRenamed) {
			assertNotNull(projectToWorkingDirectoryAndIProjectMapBeforeRename.get(projectToBeRenamed));

			String projectPath = CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(projectToBeRenamed);
			String projectWrapperName = projectPath.replace("/", ProjectsService.PROJECT_WRAPPER_NAME_SEPARATOR);
			IProject projectWrapper = wsRoot.getProject(projectWrapperName);
			assertEquals(true, projectWrapper.exists());
		}
		for (File workingDirectoryToBeRenamed : workDirsToBeRenamed) {
			assertEquals(true, workingDirectoryToProjectsMapBeforeRename.containsKey(workingDirectoryToBeRenamed));
			assertNotNull(workingDirectoryToProjectsMapBeforeRename.get(workingDirectoryToBeRenamed));
		}

		// make the rename
		String renamedFileName = "NewFolder11";
		FileManagerService.getInstance().testRename(context, fileToBeRenamed, renamedFileName);
		searchFileInDir(organization, renamedFileName);
		File renamedFile = getFileSearched();

		// check the 2 file states
		assertEquals(false, fileToBeRenamed.exists());
		assertEquals(true, renamedFile.exists());

		// check the old projects
		assertEquals(false, workingDirectoryToProjectsMapBeforeRename.containsKey(fileToBeRenamed));
		for (File projectToBeRenamed : projectsToBeRenamed) {
			assertEquals(false, projectToWorkingDirectoryAndIProjectMapBeforeRename.containsKey(projectToBeRenamed));

			String projectPath = CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(projectToBeRenamed);
			String projectWrapperName = projectPath.replace("/", ProjectsService.PROJECT_WRAPPER_NAME_SEPARATOR);
			IProject projectWrapper = wsRoot.getProject(projectWrapperName);
			assertEquals(false, projectWrapper.exists());
		}

		// check the new ones
		List<File> projectsRenamed = new ArrayList<File>();
		for (String renamedProjectName : projectsToBeRenamedNames) {
			searchFileInDir(organization, renamedProjectName);
			projectsRenamed.add(getFileSearched());
		}
		for (File projectToBeRenamed : projectsRenamed) {
			assertEquals(true, projectToWorkingDirectoryAndIProjectMapBeforeRename.containsKey(projectToBeRenamed));

			String projectPath = CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(projectToBeRenamed);
			String projectWrapperName = projectPath.replace("/", ProjectsService.PROJECT_WRAPPER_NAME_SEPARATOR);
			IProject projectWrapper = wsRoot.getProject(projectWrapperName);
			assertEquals(true, projectWrapper.exists());
		}

		// check the old workdirs
		for (File workingDirectoryToBeRenamed : workDirsToBeRenamed) {
			assertEquals(false, workingDirectoryToProjectsMapBeforeRename.containsKey(workingDirectoryToBeRenamed));
		}

		// check the new ones
		List<File> workDirsRenamed = new ArrayList<File>();
		for (String workDirToBeRenamed : workDirsToBeRenamedNames) {
			searchFileInDir(organization, workDirToBeRenamed);
			workDirsRenamed.add(getFileSearched());
		}

		workDirs = getWorkingDirectoriesForOrganizationName(organization.getName());

		for (File workingDirectoryRenamed : workDirsRenamed) {
			assertEquals(true, workingDirectoryToProjectsMapBeforeRename.containsKey(workingDirectoryRenamed));
			assertNotNull(workingDirectoryToProjectsMapBeforeRename.get(workingDirectoryRenamed));

			// check database
			String newRelativeNameFromOrg = ProjectsService.getInstance().getRelativePathFromOrganization(
					workingDirectoryRenamed);
			WorkingDirectory workDir = getWorkingDirectory(organization.getName(),
					newRelativeNameFromOrg.substring(0, newRelativeNameFromOrg.length() - 1));
			assertEquals(true, workDirs.contains(workDir));
		}

		assertEquals(workDirsNumber, workDirs.size());

		// check maps
		assertEquals(numberOfWorkingDirectoriesBeforeRename, workingDirectoryToProjectsMapBeforeRename.size());
		assertEquals(numberOfProjects, projectToWorkingDirectoryAndIProjectMapBeforeRename.size());

	}
}
