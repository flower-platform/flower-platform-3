package org.flowerplatform.web.tests.listener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.editor.remote.EditableResource;
import org.flowerplatform.editor.remote.EditorStatefulClientLocalState;
import org.flowerplatform.editor.text.remote.TextEditorStatefulService;
import org.flowerplatform.web.communication.RecordingTestWebCommunicationChannel;
import org.flowerplatform.web.projects.remote.FileManagerService;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileChangeListenerTest {
	
	public static final String TEXT_EDITOR_SERVICE_ID = "textEditorStatefulService";
	private static final CommunicationChannel communicationChannel = new RecordingTestWebCommunicationChannel();
	private static final CommunicationChannel communicationChannelForSecondClient = new RecordingTestWebCommunicationChannel();

	private File fileSearched;

	public static File organization;

	public static ServiceInvocationContext context;
	
	public static StatefulServiceInvocationContext contextForFirstClient ; 
	public static StatefulServiceInvocationContext contextForSecondClient ; 

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
				if (fileName.toLowerCase().equals(f.getName().toLowerCase())) {
					setFileSearched(f);
				}
			}
		}
	}
	
	@BeforeClass
	public static void setUp() {
		File workspaceRoot = CommonPlugin.getInstance().getWorkspaceRoot();
		String pathOfWorkspaceRoot = workspaceRoot.getPath();
		organization = new File(pathOfWorkspaceRoot + "/" + SetUpTestEnviroment.FOLDER_TO_BE_COPIED_FOR_FILE_CHANGED);
		context = new ServiceInvocationContext(communicationChannel);
		contextForFirstClient = new StatefulServiceInvocationContext(communicationChannel);
		contextForSecondClient = new StatefulServiceInvocationContext(communicationChannelForSecondClient);
		SetUpTestEnviroment.populateWorkspaceForFileChanged(context);
	}
	
	@Test
	public void testSimpleModifyText() {
		String fileToBeModifiedName = "file1";
		searchFileInDir(organization, fileToBeModifiedName);
		File fileToBeModified = getFileSearched();
		
		String editableResourcePath = "/" + CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(fileToBeModified);
		TextEditorStatefulService textEditorStatefulService = (TextEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(TEXT_EDITOR_SERVICE_ID);
		textEditorStatefulService.subscribe(contextForFirstClient, new EditorStatefulClientLocalState(editableResourcePath));
		textEditorStatefulService.subscribe(contextForSecondClient, new EditorStatefulClientLocalState(editableResourcePath));
		EditableResource editableResource = textEditorStatefulService.getEditableResource(editableResourcePath);	
		assertNotNull(editableResource);
		long oldTimeStamp = editableResource.getLastModifiedStamp();
		
		PrintWriter writer;
		try {
			writer = new PrintWriter(fileToBeModified);
			writer.println("The first line");
			writer.println("The second line");
			writer.close();
			FileManagerService.getInstance().refreshDirectoryByFile(context, fileToBeModified);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		editableResource = textEditorStatefulService.getEditableResource(editableResourcePath);
		assertNotNull(editableResource);
		assertNotSame(oldTimeStamp, editableResource.getLastModifiedStamp());
	}
	
	@Test
	public void testSimpleDeleteAndRenameText() {
		// delete
		String fileToBeDeletedName = "file2";
		searchFileInDir(organization, fileToBeDeletedName);
		File fileToBeDeleted = getFileSearched();
		
		String editableResourcePath = "/" + CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(fileToBeDeleted);
		TextEditorStatefulService textEditorStatefulService = (TextEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(TEXT_EDITOR_SERVICE_ID);
		textEditorStatefulService.subscribe(contextForFirstClient, new EditorStatefulClientLocalState(editableResourcePath));
		textEditorStatefulService.subscribe(contextForSecondClient, new EditorStatefulClientLocalState(editableResourcePath));
		EditableResource editableResource = textEditorStatefulService.getEditableResource(editableResourcePath);	
		assertNotNull(editableResource);

		FileManagerService.getInstance().testDeleteFile(context, fileToBeDeleted);
		editableResource = textEditorStatefulService.getEditableResource(editableResourcePath);
		assertEquals(null, editableResource);	
		
		// rename
		String fileToBeRenamedName = "file3";
		searchFileInDir(organization, fileToBeRenamedName);
		File fileToBeRenamed = getFileSearched();
		
		editableResourcePath = "/" + CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(fileToBeRenamed);
		textEditorStatefulService.subscribe(contextForFirstClient, new EditorStatefulClientLocalState(editableResourcePath));
		textEditorStatefulService.subscribe(contextForSecondClient, new EditorStatefulClientLocalState(editableResourcePath));
		editableResource = textEditorStatefulService.getEditableResource(editableResourcePath);	
		assertNotNull(editableResource);

		FileManagerService.getInstance().testRename(context, fileToBeRenamed, "file4");
		editableResource = textEditorStatefulService.getEditableResource(editableResourcePath);
		assertEquals(null, editableResource);
	}
	
	
	@Test
	public void deleteProject() {
		String fileToBeDeletedName = "Project3";
		searchFileInDir(organization, fileToBeDeletedName);
		File fileToBeDeleted = getFileSearched();
		
		TextEditorStatefulService textEditorStatefulService = (TextEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(TEXT_EDITOR_SERVICE_ID);
		EditableResource editableResource;
		
		String[] filesToBeOpenedNames = {"file6", "file4"};
		String[] filesToBeOpenedNotDeletedNames = {"file8"};
		ArrayList<File> filesToBeOpened = new ArrayList<File>();
		ArrayList<File> filesToBeOpenedNotDeleted = new ArrayList<File>();

		for(String name : filesToBeOpenedNames) {
			searchFileInDir(organization, name);
			File file = getFileSearched();
			filesToBeOpened.add(file);
			
			String editableResourcePath = "/" + CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(file);
			textEditorStatefulService.subscribe(contextForFirstClient, new EditorStatefulClientLocalState(editableResourcePath));
			textEditorStatefulService.subscribe(contextForSecondClient, new EditorStatefulClientLocalState(editableResourcePath));
			editableResource = textEditorStatefulService.getEditableResource(editableResourcePath);	
			
			assertNotNull(editableResource);
		}		
		
		for(String name : filesToBeOpenedNotDeletedNames) {
			searchFileInDir(organization, name);
			File file = getFileSearched();
			filesToBeOpenedNotDeleted.add(file);
			
			String editableResourcePath = "/" + CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(file);
			textEditorStatefulService.subscribe(contextForFirstClient, new EditorStatefulClientLocalState(editableResourcePath));
			textEditorStatefulService.subscribe(contextForSecondClient, new EditorStatefulClientLocalState(editableResourcePath));
			editableResource = textEditorStatefulService.getEditableResource(editableResourcePath);	
			
			assertNotNull(editableResource);
		}
		
		FileManagerService.getInstance().testDeleteFile(context, fileToBeDeleted);
		
		for(File fileClosed : filesToBeOpened) {
			String editableResourcePath = "/" + CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(fileClosed);
			editableResource = textEditorStatefulService.getEditableResource(editableResourcePath);
			
			assertEquals(null, editableResource);
		}
		
		for(File fileClosed : filesToBeOpenedNotDeleted) {
			String editableResourcePath = "/" + CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(fileClosed);
			editableResource = textEditorStatefulService.getEditableResource(editableResourcePath);
			
			assertNotNull(editableResource);
		}
	}
}
