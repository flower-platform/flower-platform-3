package org.flowerplatform.web.tests.listener;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.CoreException;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;
import org.flowerplatform.web.projects.remote.ProjectsService;
import org.flowerplatform.web.temp.GeneralService;
import org.flowerplatform.web.tests.TestUtil;

public class SetUpTestEnviroment {
	
	final static String RESOURCE_FOLDER_NAME = "listener";
	
	static final String FOLDER_TO_BE_COPIED_FOR_DELETE = "ListenerOrg";
	
	static final String FOLDER_TO_BE_COPIED_FOR_RENAME = "ListenerOrg2";
	
	static final String FOLDER_TO_BE_COPIED_FOR_FILE_CHANGED = "FileChangedListenerOrg";
	
	private static final String WORKING_DIRECTORY_PREFIX = "WorkingDirectory";
	
	private static final String PROJECT_PREFIX = "Project";
	
	public static final int EVENT_DELETE = 1;
	public static final int EVENT_RENAME = 2;
	
	private static void markSpecialFolders (ServiceInvocationContext context, File folder) {
		
		String pathOfFolder = folder.getName();
		if(pathOfFolder.toLowerCase().contains(WORKING_DIRECTORY_PREFIX.toLowerCase())) {
			System.out.println("intra " + pathOfFolder);
			ProjectsService.getInstance().markAsWorkingDirectoryForFile(context, folder);
			System.out.println("iese " + pathOfFolder);
		} else if(pathOfFolder.toLowerCase().contains(PROJECT_PREFIX.toLowerCase())) {
			try {
				ProjectsService.getInstance().createOrImportProjectFromFile(context, folder);
			} catch (URISyntaxException | CoreException e) {
				e.printStackTrace();
			}
		}
		
		File[] files = folder.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					markSpecialFolders(context, f);
				} 
			}
		}
		
	}
	
	public static void populateWorkspace (ServiceInvocationContext context,final int event) {
		// getPathRelativeToWorkspaceRoot
		File workspaceRoot = CommonPlugin.getInstance().getWorkspaceRoot();
		String pathOfWorkspaceRoot = workspaceRoot.getPath();
		File sourceDir = null;
		File destDir = null;
		if(event == EVENT_DELETE) {
			sourceDir = new File(
					TestUtil.getResourcesDir(SetUpTestEnviroment.class) + "/" 
					+ TestUtil.INITIAL_TO_BE_COPIED + "/" 
					+ FOLDER_TO_BE_COPIED_FOR_DELETE);
			destDir = new File(pathOfWorkspaceRoot + "/" + FOLDER_TO_BE_COPIED_FOR_DELETE);
		} else {
			sourceDir = new File(
					TestUtil.getResourcesDir(SetUpTestEnviroment.class) + "/" 
					+ TestUtil.INITIAL_TO_BE_COPIED + "/" 
					+ FOLDER_TO_BE_COPIED_FOR_DELETE);
			destDir = new File(pathOfWorkspaceRoot + "/" + FOLDER_TO_BE_COPIED_FOR_RENAME);
		}
		
		try {
			FileUtils.copyDirectory(sourceDir, destDir);
			System.out.println("Coppied " + sourceDir.getPath() + " in " + destDir.getPath());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				if(event == EVENT_DELETE) {
					new GeneralService().createOrganization(FOLDER_TO_BE_COPIED_FOR_DELETE, wrapper);
				} else {
					new GeneralService().createOrganization(FOLDER_TO_BE_COPIED_FOR_RENAME, wrapper);
				}
			}
		});
		
		markSpecialFolders(context, destDir);		
	}
	
	public static void populateWorkspaceForFileChanged(ServiceInvocationContext context) {
		File workspaceRoot = CommonPlugin.getInstance().getWorkspaceRoot();
		String pathOfWorkspaceRoot = workspaceRoot.getPath();
		File sourceDir = new File(
				TestUtil.getResourcesDir(SetUpTestEnviroment.class) + "/" 
				+ TestUtil.INITIAL_TO_BE_COPIED + "/" 
				+ FOLDER_TO_BE_COPIED_FOR_FILE_CHANGED);
		File destDir = new File(pathOfWorkspaceRoot + "/" + FOLDER_TO_BE_COPIED_FOR_FILE_CHANGED);
	
		try {
			FileUtils.copyDirectory(sourceDir, destDir);
			System.out.println("Coppied " + sourceDir.getPath() + " in " + destDir.getPath());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		new DatabaseOperationWrapper(new DatabaseOperation() {
			
			@Override
			public void run() {
				new GeneralService().createOrganization(FOLDER_TO_BE_COPIED_FOR_FILE_CHANGED, wrapper);
			}
		});
		
		markSpecialFolders(context, destDir);		

	}
}
