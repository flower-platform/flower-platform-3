package org.flowerplatform.web.tests.listener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

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

public class DeleteListenerTest {

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
		organization = new File(pathOfWorkspaceRoot + "/" + SetUpTestEnviroment.FOLDER_TO_BE_COPIED_FOR_DELETE);
		context = new ServiceInvocationContext(communicationChannel);
		SetUpTestEnviroment.populateWorkspace(context, SetUpTestEnviroment.EVENT_DELETE);
	}

	@Test
	public void deleteProject() {
		String projectToBeDeletedName = "Project24";
		searchFileInDir(organization, projectToBeDeletedName);
		File projectToBeDeleted = getFileSearched();

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot wsRoot = workspace.getRoot();
		String workingDirectoryName = "WorkingDirectory16";
		searchFileInDir(organization, workingDirectoryName);
		File workingDirectory = getFileSearched();

		// check if the maps are ok
		Map<File, List<File>> workingDirectoryToProjectsMapBeforeDelete = ProjectsService.getInstance()
				.getWorkingDirectoryToProjectsMap();
		Map<File, Pair<File, IProject>> projectToWorkingDirectoryAndIProjectMapBeforeDelete = ProjectsService
				.getInstance().getProjectToWorkingDirectoryAndIProjectMap();
		int numberOfWorkingDirectoriesBeforeDelete = workingDirectoryToProjectsMapBeforeDelete.size();
		int numberOfProjectsInWorkingDirectory = workingDirectoryToProjectsMapBeforeDelete.get(workingDirectory).size();
		int numberOfProjects = projectToWorkingDirectoryAndIProjectMapBeforeDelete.size();

		assertEquals(true, workingDirectoryToProjectsMapBeforeDelete.get(workingDirectory).contains(projectToBeDeleted));
		assertNotNull(projectToWorkingDirectoryAndIProjectMapBeforeDelete.get(projectToBeDeleted));

		// check if projectWrapper exists
		String projectPath = CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(projectToBeDeleted);
		String projectWrapperName = projectPath.replace("/", ProjectsService.PROJECT_WRAPPER_NAME_SEPARATOR);
		IProject projectWrapper = wsRoot.getProject(projectWrapperName);

		assertEquals(true, projectWrapper.exists());

		// delete the project
		FileManagerService.getInstance().testDeleteFile(context, projectToBeDeleted);

		// check if the maps are up to date

		Map<File, List<File>> workingDirectoryToProjectsMapAfterDelete = ProjectsService.getInstance()
				.getWorkingDirectoryToProjectsMap();
		Map<File, Pair<File, IProject>> projectToWorkingDirectoryAndIProjectMapAfterDelete = ProjectsService
				.getInstance().getProjectToWorkingDirectoryAndIProjectMap();
		assertEquals(false, workingDirectoryToProjectsMapAfterDelete.get(workingDirectory).contains(projectToBeDeleted));
		assertEquals(null, projectToWorkingDirectoryAndIProjectMapAfterDelete.get(projectToBeDeleted));
		assertEquals(false, projectToWorkingDirectoryAndIProjectMapAfterDelete.isEmpty());
		assertEquals(false, workingDirectoryToProjectsMapAfterDelete.isEmpty());
		assertEquals(numberOfWorkingDirectoriesBeforeDelete, workingDirectoryToProjectsMapAfterDelete.size());
		assertEquals(numberOfProjectsInWorkingDirectory - 1,
				workingDirectoryToProjectsMapAfterDelete.get(workingDirectory).size());
		assertEquals(numberOfProjects - 1, projectToWorkingDirectoryAndIProjectMapAfterDelete.size());

		// check if projectWrapper was deleted
		assertEquals(false, projectWrapper.exists());
	}

	@Test
	public void deletePathToProject() {
		String projectToBeDeletedName = "Folder39";
		searchFileInDir(organization, projectToBeDeletedName);
		File folderToBeDeleted = getFileSearched();

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot wsRoot = workspace.getRoot();

		String[] deletedProjectsNames = { "Project42", "Project43", "Project45" };
		String[] notDeletedProjectsNames = { "Project40", "Project41" };
		List<File> deletedProjects = new ArrayList<File>();
		List<File> notDeletedProjects = new ArrayList<File>();

		for (String deletedProjectName : deletedProjectsNames) {
			searchFileInDir(organization, deletedProjectName);
			deletedProjects.add(getFileSearched());
		}

		for (String notDeletedProjectName : notDeletedProjectsNames) {
			searchFileInDir(organization, notDeletedProjectName);
			notDeletedProjects.add(getFileSearched());
		}

		// check if the maps are ok
		String workingDirectoryName = "WorkingDirectory15";
		searchFileInDir(organization, workingDirectoryName);
		File workingDirectory = getFileSearched();
		Map<File, List<File>> workingDirectoryToProjectsMapBeforeDelete = ProjectsService.getInstance()
				.getWorkingDirectoryToProjectsMap();
		Map<File, Pair<File, IProject>> projectToWorkingDirectoryAndIProjectMapBeforeDelete = ProjectsService
				.getInstance().getProjectToWorkingDirectoryAndIProjectMap();
		int numberOfWorkingDirectoriesBeforeDelete = workingDirectoryToProjectsMapBeforeDelete.size();
		int numberOfProjectsInWorkingDirectory = workingDirectoryToProjectsMapBeforeDelete.get(workingDirectory).size();
		int numberOfProjects = projectToWorkingDirectoryAndIProjectMapBeforeDelete.size();

		for (File projectToBeDeleted : deletedProjects) {
			assertEquals(true,
					workingDirectoryToProjectsMapBeforeDelete.get(workingDirectory).contains(projectToBeDeleted));
			assertNotNull(projectToWorkingDirectoryAndIProjectMapBeforeDelete.get(projectToBeDeleted));

			String projectPath = CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(projectToBeDeleted);
			String projectWrapperName = projectPath.replace("/", ProjectsService.PROJECT_WRAPPER_NAME_SEPARATOR);
			IProject projectWrapper = wsRoot.getProject(projectWrapperName);
			assertEquals(true, projectWrapper.exists());
		}

		// delete the path/to
		FileManagerService.getInstance().testDeleteFile(context, folderToBeDeleted);

		Map<File, List<File>> workingDirectoryToProjectsMapAfterDelete = ProjectsService.getInstance()
				.getWorkingDirectoryToProjectsMap();
		Map<File, Pair<File, IProject>> projectToWorkingDirectoryAndIProjectMapAfterDelete = ProjectsService
				.getInstance().getProjectToWorkingDirectoryAndIProjectMap();

		assertEquals(numberOfWorkingDirectoriesBeforeDelete, workingDirectoryToProjectsMapAfterDelete.size());
		assertEquals(numberOfProjectsInWorkingDirectory - 3,
				workingDirectoryToProjectsMapAfterDelete.get(workingDirectory).size());
		assertEquals(numberOfProjects - 3, projectToWorkingDirectoryAndIProjectMapAfterDelete.size());
		assertEquals(false, projectToWorkingDirectoryAndIProjectMapAfterDelete.isEmpty());
		assertEquals(false, workingDirectoryToProjectsMapAfterDelete.isEmpty());

		for (File projectToBeDeleted : deletedProjects) {
			assertEquals(false,
					workingDirectoryToProjectsMapAfterDelete.get(workingDirectory).contains(projectToBeDeleted));
			assertEquals(null, projectToWorkingDirectoryAndIProjectMapAfterDelete.get(projectToBeDeleted));

			String projectPath = CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(projectToBeDeleted);
			String projectWrapperName = projectPath.replace("/", ProjectsService.PROJECT_WRAPPER_NAME_SEPARATOR);
			IProject projectWrapper = wsRoot.getProject(projectWrapperName);
			assertEquals(false, projectWrapper.exists());
		}

		for (File projectNotToBeDeleted : notDeletedProjects) {
			assertEquals(true,
					workingDirectoryToProjectsMapAfterDelete.get(workingDirectory).contains(projectNotToBeDeleted));
			assertNotNull(projectToWorkingDirectoryAndIProjectMapAfterDelete.get(projectNotToBeDeleted));

			String projectPath = CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(projectNotToBeDeleted);
			String projectWrapperName = projectPath.replace("/", ProjectsService.PROJECT_WRAPPER_NAME_SEPARATOR);
			IProject projectWrapper = wsRoot.getProject(projectWrapperName);
			assertEquals(true, projectWrapper.exists());
		}
	}

	@Test
	public void deleteWorkingDirectory() {
		String workingDirectoryToBeDeletedName = "WorkingDirectory10";
		searchFileInDir(organization, workingDirectoryToBeDeletedName);
		File workingDirectoryToBeDeleted = getFileSearched();

		// check database
		List<WorkingDirectory> workDirs = getWorkingDirectoriesForOrganizationName(organization.getName());
		int workDirsNumber;
		String relativeNameFromOrg = ProjectsService.getInstance().getRelativePathFromOrganization(
				workingDirectoryToBeDeleted);
		WorkingDirectory workDir = getWorkingDirectory(organization.getName(),
				relativeNameFromOrg.substring(0, relativeNameFromOrg.length() - 1));
		if (workDirs.isEmpty()) {
			workDirsNumber = 0;
		} else {
			workDirsNumber = workDirs.size();
		}
		assertNotSame(0, workDirsNumber);
		assertEquals(true, workDirs.contains(workDir));

		// check maps before delete
		String[] deletedProjectsNames = { "Project49", "Project50", "Project53", "Project54", "Project55", "Project58",
				"Project59" };
		List<File> deletedProjects = new ArrayList<File>();

		for (String deletedProjectName : deletedProjectsNames) {
			searchFileInDir(organization, deletedProjectName);
			deletedProjects.add(getFileSearched());
		}

		Map<File, List<File>> workingDirectoryToProjectsMapBeforeDelete = ProjectsService.getInstance()
				.getWorkingDirectoryToProjectsMap();
		Map<File, Pair<File, IProject>> projectToWorkingDirectoryAndIProjectMapBeforeDelete = ProjectsService
				.getInstance().getProjectToWorkingDirectoryAndIProjectMap();
		int numberOfWorkingDirectoriesBeforeDelete = workingDirectoryToProjectsMapBeforeDelete.size();
		int numberOfProjects = projectToWorkingDirectoryAndIProjectMapBeforeDelete.size();

		for (File projectToBeDeleted : deletedProjects) {
			assertEquals(
					true,
					workingDirectoryToProjectsMapBeforeDelete.get(workingDirectoryToBeDeleted).contains(
							projectToBeDeleted));
			assertNotNull(projectToWorkingDirectoryAndIProjectMapBeforeDelete.get(projectToBeDeleted));
		}

		assertNotNull(workingDirectoryToProjectsMapBeforeDelete.get(workingDirectoryToBeDeleted));

		FileManagerService.getInstance().testDeleteFile(context, workingDirectoryToBeDeleted);

		// check maps after delete
		Map<File, List<File>> workingDirectoryToProjectsMapAfterDelete = ProjectsService.getInstance()
				.getWorkingDirectoryToProjectsMap();
		Map<File, Pair<File, IProject>> projectToWorkingDirectoryAndIProjectMapAfterDelete = ProjectsService
				.getInstance().getProjectToWorkingDirectoryAndIProjectMap();

		assertEquals(numberOfWorkingDirectoriesBeforeDelete - 1, workingDirectoryToProjectsMapAfterDelete.size());
		assertEquals(numberOfProjects - 6, projectToWorkingDirectoryAndIProjectMapAfterDelete.size());
		assertEquals(null, workingDirectoryToProjectsMapBeforeDelete.get(workingDirectoryToBeDeleted));

		// check database
		workDirs = getWorkingDirectoriesForOrganizationName(organization.getName());
		assertEquals(false, workDirs.contains(workDir));
		assertEquals(workDirsNumber - 1, workDirs.size());

		if (workDirs.isEmpty()) {
			workDirsNumber = 0;
		} else {
			workDirsNumber = workDirs.size();
		}
		assertNotSame(0, workDirsNumber);

	}

	@Test
	public void deletePathToWorkingDirectory() {
		String projectToBeDeletedName = "Folder11";
		searchFileInDir(organization, projectToBeDeletedName);
		File folderToBeDeleted = getFileSearched();

		String[] deletedProjectsNames = { "Project70", "Project73", "Project76", "Project69", "Project75", "Project82",
				"Project83", "Project84", "Project85", "Project87", "Project88" };
		String[] deletedWorkingDirectoriesNames = { "WorkingDirectory66", "WorkingDirectory67" };
		List<File> deletedProjects = new ArrayList<File>();
		List<File> deletedWorkingDirectories = new ArrayList<File>();
		for (String deletedProjectName : deletedProjectsNames) {
			searchFileInDir(organization, deletedProjectName);
			deletedProjects.add(getFileSearched());
		}
		for (String deletedWDName : deletedWorkingDirectoriesNames) {
			searchFileInDir(organization, deletedWDName);
			deletedWorkingDirectories.add(getFileSearched());
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
		for (File workingDirectoryToBeDeleted : deletedWorkingDirectories) {
			String relativeNameFromOrg = ProjectsService.getInstance().getRelativePathFromOrganization(
					workingDirectoryToBeDeleted);
			WorkingDirectory workDir = getWorkingDirectory(organization.getName(),
					relativeNameFromOrg.substring(0, relativeNameFromOrg.length() - 1));
			assertEquals(true, workDirs.contains(workDir));
		}

		// check maps
		Map<File, List<File>> workingDirectoryToProjectsMapBeforeDelete = ProjectsService.getInstance()
				.getWorkingDirectoryToProjectsMap();
		Map<File, Pair<File, IProject>> projectToWorkingDirectoryAndIProjectMapBeforeDelete = ProjectsService
				.getInstance().getProjectToWorkingDirectoryAndIProjectMap();
		int numberOfWorkingDirectoriesBeforeDelete = workingDirectoryToProjectsMapBeforeDelete.size();
		int numberOfProjects = projectToWorkingDirectoryAndIProjectMapBeforeDelete.size();
		for (File projectToBeDeleted : deletedProjects) {
			assertNotNull(projectToWorkingDirectoryAndIProjectMapBeforeDelete.get(projectToBeDeleted));
		}
		for (File workingDirectoryToBeDeleted : deletedWorkingDirectories) {
			assertEquals(true, workingDirectoryToProjectsMapBeforeDelete.containsKey(workingDirectoryToBeDeleted));
			assertNotNull(workingDirectoryToProjectsMapBeforeDelete.get(workingDirectoryToBeDeleted));
		}

		FileManagerService.getInstance().testDeleteFile(context, folderToBeDeleted);

		// check maps after delete
		Map<File, List<File>> workingDirectoryToProjectsMapAfterDelete = ProjectsService.getInstance()
				.getWorkingDirectoryToProjectsMap();
		Map<File, Pair<File, IProject>> projectToWorkingDirectoryAndIProjectMapAfterDelete = ProjectsService
				.getInstance().getProjectToWorkingDirectoryAndIProjectMap();

		assertEquals(numberOfWorkingDirectoriesBeforeDelete - 2, workingDirectoryToProjectsMapAfterDelete.size());
		assertEquals(numberOfProjects - 11, projectToWorkingDirectoryAndIProjectMapAfterDelete.size());
		for (File workingDirectoryToBeDeleted : deletedWorkingDirectories) {
			assertEquals(false, workingDirectoryToProjectsMapBeforeDelete.containsKey(workingDirectoryToBeDeleted));
		}
		for (File projectToBeDeleted : deletedProjects) {
			assertEquals(false, projectToWorkingDirectoryAndIProjectMapBeforeDelete.containsKey(projectToBeDeleted));
		}
		// check database after delete
		workDirs = getWorkingDirectoriesForOrganizationName(organization.getName());

		for (File workingDirectoryToBeDeleted : deletedWorkingDirectories) {
			String relativeNameFromOrg = ProjectsService.getInstance().getRelativePathFromOrganization(
					workingDirectoryToBeDeleted);
			WorkingDirectory workDir = getWorkingDirectory(organization.getName(),
					relativeNameFromOrg.substring(0, relativeNameFromOrg.length() - 1));
			assertEquals(false, workDirs.contains(workDir));
		}
		assertEquals(workDirsNumber - 2, workDirs.size());

		if (workDirs.isEmpty()) {
			workDirsNumber = 0;
		} else {
			workDirsNumber = workDirs.size();
		}
		assertNotSame(0, workDirsNumber);
	}
}
