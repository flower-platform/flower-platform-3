package org.flowerplatform.web.projects.remote;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class ProjectsService {

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
		{
			// workspace configuration: this should be done only once, because these settings are persistent.
			// However, I don't see how we could see that "this == first time" in a reliable manner. That's
			// why we do it every time, and it doesn't hurt as I don't think they are expensive methods
			
			// turn off auto build
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IWorkspaceDescription description = workspace.getDescription();
			description.setAutoBuilding(false);
			workspace.setDescription(description);
			
			// add the workspace location to the variables map. In desktop eclipse some default vars seem to be populated
			// automatically, but in the web config this doesn't seem to happen
			workspace.getPathVariableManager().setURIValue(WORKSPACE_LOCATION_VAR, CommonPlugin.getInstance().getWorkspaceRoot().toURI());
		}
		
		// fill the map with working directories
		@SuppressWarnings("unchecked")
		List<WorkingDirectory> workingDirectories = (List<WorkingDirectory>) new DatabaseOperationWrapper(new DatabaseOperation() {
			@Override
			public void run() {
				wrapper.setOperationResult(wrapper.findAll(WorkingDirectory.class));
			}
		}).getOperationResult();

		for (WorkingDirectory workingDirectory : workingDirectories) {
			File workingDirFile = new File(getOrganizationDir(workingDirectory.getOrganization().getName()), workingDirectory.getPathFromOrganization());
			workingDirectoryToProjectsMap.put(workingDirFile, new ArrayList<File>());
		}
		
		File workingDirectoryFile = null;
		for (IProject projectWrapper : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			// I don't use String.split, because it takes a regex, which means I should have another constant,
			// because the current separator has regex special chars
			StringTokenizer st = new StringTokenizer(projectWrapper.getName(), PROJECT_WRAPPER_NAME_SEPARATOR);
			if (st.countTokens() < 3) {
				logger.warn("Project = {} is invalid. We were expecting at minimum tree tokens (i.e. org/work_dir/proj), but got only = {}", projectWrapper, st.countTokens());
				continue;
			}
			
			// we begin directly with something like org/work_dir
			// at the end of the iteration, this will be the file of the project
			File currentFile = new File(CommonPlugin.getInstance().getWorkspaceRoot(), st.nextToken() + "/" + st.nextToken());
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
				logger.warn("Project = {} is invalid. The file = {} doesn't point to an existing directory", projectWrapper, currentFile);
				continue;
			}
			
			workingDirectoryToProjectsMap.get(workingDirectoryFile).add(currentFile);
			projectToWorkingDirectoryAndIProjectMap.put(currentFile, new Pair<File, IProject>(workingDirectoryFile, projectWrapper));
			if (logger.isDebugEnabled()) {
				logger.debug("Project added succesfully in maps; project wrapper = {}, working directory = {}, project dir = {}", new Object[] { projectWrapper, workingDirectoryFile, currentFile});
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<WorkingDirectory> getWorkingDirectoriesForOrganizationName(final String organizationName) {
		return (List<WorkingDirectory>) new DatabaseOperationWrapper(new DatabaseOperation() {
			@Override
			public void run() {
				Query q = wrapper.createQuery(String.format("SELECT e from WorkingDirectory e where e.organization.name = '%s'", organizationName));
				wrapper.setOperationResult(q.list());
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
	protected Pair<File, Boolean> getOrganizationDirForWorkingDirectoryOrWorkindDirectoryForProject(CommunicationChannel channel, String errorTitleMessageKey,
			File workingDirectoryOrProject, boolean parameterIsWorkingDirectory) {
		File currentFile = workingDirectoryOrProject;
		// loop that does validations and find the organization dir
		while (currentFile != null && currentFile != CommonPlugin.getInstance().getWorkspaceRoot()) {
			if (projectToWorkingDirectoryAndIProjectMap.get(currentFile) != null) {
				// a parent is a project; error in both cases
				channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(WebPlugin.getInstance().getMessage(errorTitleMessageKey), WebPlugin.getInstance()
						.getMessage("explorer.markAsWorkingDirectory.error.isInProj", CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(workingDirectoryOrProject),
								CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(currentFile)), DisplaySimpleMessageClientCommand.ICON_ERROR));
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
					channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand(WebPlugin.getInstance().getMessage(errorTitleMessageKey), WebPlugin
							.getInstance().getMessage("explorer.markAsWorkingDirectory.error.isInWD",
									CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(workingDirectoryOrProject),
									CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(currentFile)), DisplaySimpleMessageClientCommand.ICON_ERROR));
					return new Pair<File, Boolean>(null, true);
				}
			}

			if (parameterIsWorkingDirectory && CommonPlugin.getInstance().getWorkspaceRoot().equals(currentFile.getParentFile())) {
				// we found the org dir
				return new Pair<File, Boolean>(currentFile, false);

			}
			currentFile = currentFile.getParentFile();
		}

		return new Pair<File, Boolean>(null, false);
	}
	
	@RemoteInvocation
	public void markAsWorkingDirectory(ServiceInvocationContext context, List<PathFragment> pathWithRoot) {
		@SuppressWarnings("unchecked")
		final File file = ((Pair<File, String>) GenericTreeStatefulService.getNodeByPathFor(pathWithRoot, null)).a;
		final Pair<File, Boolean> organizationDir = getOrganizationDirForWorkingDirectoryOrWorkindDirectoryForProject(context.getCommunicationChannel(),
				"explorer.markAsWorkingDirectory.error.title", file, true);

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
				// this shouldn't happen, as the action is not visible on organization nodes
				throw new IllegalArgumentException("The prosed working directory and organization dir are the same: " + file);
			}
		}

		new DatabaseOperationWrapper(new DatabaseOperation() {
			@Override
			public void run() {
				String relativePath = CommonPlugin.getInstance().getPathRelativeToFile(file, organizationDir.a);
				List<Organization> orgs = (List<Organization>) wrapper.findByField(Organization.class, "name", getOrganizationName(organizationDir.a));
				if (orgs.isEmpty()) {
					throw new IllegalArgumentException("Cannot find organization with name = " + getOrganizationName(organizationDir.a));
				}
				WorkingDirectory wd = EntityFactory.eINSTANCE.createWorkingDirectory();
				wd.setPathFromOrganization(relativePath);
				wd.setOrganization(orgs.get(0));
				wrapper.getSession().persist(wd);
			}
		});
		workingDirectoryToProjectsMap.put(file, new ArrayList<File>());
		if (logger.isDebugEnabled()) {
			logger.debug("In Organization = {}, added a new Working Directory = {}", getOrganizationName(organizationDir.a), file);
		}
	}

	@RemoteInvocation
	public void createOrImportProject(ServiceInvocationContext context, List<PathFragment> pathWithRoot) throws CoreException, URISyntaxException {
		@SuppressWarnings("unchecked")
		final File file = ((Pair<File, String>) GenericTreeStatefulService.getNodeByPathFor(pathWithRoot, null)).a;
		final Pair<File, Boolean> workingDirectoryDir = getOrganizationDirForWorkingDirectoryOrWorkindDirectoryForProject(context.getCommunicationChannel(),
				"explorer.createOrImportProject.error.title", file, false);

		if (workingDirectoryDir.a == null) {
			if (workingDirectoryDir.b) {
				// we didn't find it, but it's a validation error; not an
				// illegal state; a message was sent to the client
			} else {
				// the file is not in a working dir
				context.getCommunicationChannel().appendCommandToCurrentHttpResponse(
						new DisplaySimpleMessageClientCommand(WebPlugin.getInstance().getMessage("explorer.createOrImportProject.error.title"), WebPlugin.getInstance().getMessage(
								"explorer.createOrImportProject.error.cannotFindWD", CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(file)),
								DisplaySimpleMessageClientCommand.ICON_ERROR));
			}
			return;
		}
		
		// create the project in the workspace
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot wsRoot = workspace.getRoot();
		String projectPath = CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(file);
		if (projectPath.indexOf(PROJECT_WRAPPER_NAME_SEPARATOR) >= 0) {
			// ooops; the dir contains the separator; not good! I hope this won't happen
			throw new UnsupportedOperationException(String.format("The project to import contains our separator = %s in its name = $s", PROJECT_WRAPPER_NAME_SEPARATOR, projectPath));
		}
		String projectWrapperName = projectPath.replace("/", PROJECT_WRAPPER_NAME_SEPARATOR);
		String organizationDirName = projectPath.substring(0, projectPath.indexOf('/'));
		// e.g. WORKSPACE_LOC/myOrg/project-wrappers/myOrg.git_repos.work_dir1.proj1
		URI projectWrapperLocationURI = new URI(WORKSPACE_LOCATION_VAR + "/" + organizationDirName + "/" + PROJECT_WRAPPERS + "/" + projectWrapperName);
		
		IProject projectWrapper = wsRoot.getProject(projectWrapperName);
		if (projectWrapper.exists()) {
			logger.warn("The project {} was found in the workspace while trying to import it; deleting it.", projectWrapper);
			projectWrapper.delete(true, null);
		}

		IStatus status = workspace.validateProjectLocationURI(projectWrapper, projectWrapperLocationURI);
		if (!status.isOK()) {
			throw new RuntimeException(String.format("The project location URI = %s is not valid; we have the following validation error message = %s", projectWrapperLocationURI, status.getMessage()));
		}

		IProjectDescription pd = ResourcesPlugin.getWorkspace().newProjectDescription(projectWrapper.getName());
		pd.setLocationURI(projectWrapperLocationURI);
		projectWrapper.create(pd, null);
		projectWrapper.open(null);
		
		// create the linked folder, from the project wrapper -> the project itself
		IFolder linkToProjectFolder = projectWrapper.getFolder(LINK_TO_PROJECT);
		URI linkToProjectURI = new URI(WORKSPACE_LOCATION_VAR + "/" + projectPath);
		status = workspace.validateLinkLocationURI(linkToProjectFolder, linkToProjectURI);
		if (!status.isOK()) {
			throw new RuntimeException(String.format("The linkToProjectURI = %s is not valid; we have the following validation error message = %s", linkToProjectURI, status.getMessage()));
		}
		linkToProjectFolder.createLink(linkToProjectURI, IResource.NONE, null);
		
		workingDirectoryToProjectsMap.get(workingDirectoryDir.a).add(file);
		projectToWorkingDirectoryAndIProjectMap.put(file, new Pair<File, IProject>(workingDirectoryDir.a, projectWrapper));
	
		if (logger.isDebugEnabled()) {
			logger.debug("In Working Directory = {}, added a new Project = {}", workingDirectoryDir.a, file);
		}
		
	}

}
