package org.flowerplatform.web.tests.listener;

import java.io.File;

import static org.junit.Assert.*;

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
	public void testSimpleDeleteAndRename() {
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
	
	

}
