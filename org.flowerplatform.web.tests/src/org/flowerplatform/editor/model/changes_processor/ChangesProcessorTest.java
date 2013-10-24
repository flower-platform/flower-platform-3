package org.flowerplatform.editor.model.changes_processor;

import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.codesync.operation_extension.AddNewTopLevelElementExtension;
import org.flowerplatform.codesync.remote.CodeSyncDiagramOperationsService1;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.command.AbstractServerCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.editor.model.java.remote.NewJavaClassDiagramAction;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.web.communication.RecordingTestWebCommunicationChannel;
import org.flowerplatform.web.projects.remote.ProjectsService;
import org.flowerplatform.web.tests.TestUtil;
import org.flowerplatform.web.tests.codesync.CodeSyncJavascriptTest;
import org.junit.BeforeClass;
import org.junit.Test;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.codesync.base.communication.CodeSyncEditorStatefulService;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

public class ChangesProcessorTest {
	

	public static final String PROJECT = "editor.model.changes_descriptor";
//	public static final String DIR = TestUtil.getResourcesDir(CodeSyncJavascriptTest.class);
//	public static final String INITIAL_TO_BE_COPIED = DIR + TestUtil.INITIAL_TO_BE_COPIED + "/javascript";
//	public static final String EXPECTED = DIR + TestUtil.EXPECTED + "/javascript/";
	
	private static CommunicationChannel communicationChannel = new RecordingTestWebCommunicationChannel();

	private static DiagramEditorStatefulService diagramEditorStatefulService;
	
	private static CodeSyncEditorStatefulService codeSyncEditorStatefulService;
	
	private static DiagramEditableResource diagramEditableResource;
	
	// TODO CS: here without /; in other places with /
	private static final String ER_PATH = "org/ws_trunk/" + PROJECT + "/changesDescriptionDiagram1.notation";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// TODO CS: we absolutely need to solve the issue with CommPlugin & services
		// here, the dispatch is done on another thread that starts more slowly
		Thread.sleep(3000);
		TestUtil.copyFilesAndCreateProject(new ServiceInvocationContext(communicationChannel), null, PROJECT);
		
		diagramEditorStatefulService = (DiagramEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry()
				.getService("diagramEditorStatefulService");
		assertNotNull("DiagramEditorStatefulService was not registered", diagramEditorStatefulService);
		codeSyncEditorStatefulService = (CodeSyncEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry()
				.getService("codeSyncEditorStatefulService");
		assertNotNull("CodeSyncEditorStatefulService was not registered", codeSyncEditorStatefulService);
		
		CodeSyncPlugin.getInstance().CSE_MAPPING_FILE_LOCATION = "/" + ProjectsService.LINK_TO_PROJECT + "/CSE.notation";
		CodeSyncPlugin.getInstance().ACE_FILE_LOCATION = "/" + ProjectsService.LINK_TO_PROJECT + "/ACE.notation";
		
		NewJavaClassDiagramAction action = new NewJavaClassDiagramAction();
		action.parentPath = "/org/ws_trunk/" + PROJECT;
		action.name = "changesDescriptionDiagram.notation";
		// TODO CS
		action.openAutomatically = false;
		action.executeCommand();
		// TODO CS the recording CC.appendHttp... should also be overwritten
		// TODO CS in FileBased... there is still a manipulation with file, not using FileAccessContr...
		diagramEditableResource = (DiagramEditableResource) diagramEditorStatefulService.subscribeClientForcefully(communicationChannel, ER_PATH);
		
		EditorModelPlugin.getInstance().getClassCriterionDispatcherProcessor().addProcessor(Object.class, new IChangesProcessor() {
			
			@Override
			public void processChanges(Map<String, Object> context, EObject object, Changes changes) {
				System.out.println(object);				
			}
		});
	}
	
	private void executeInDiagramEditorStatefulService(final Runnable runnable) {
		diagramEditorStatefulService.attemptUpdateEditableResourceContent(new StatefulServiceInvocationContext(communicationChannel), ER_PATH, new AbstractServerCommand() {
			@Override
			public void executeCommand() {
				runnable.run();
			}
		});
	}
	
	@Test
	public void test() {
		// TODO CS the static references
		executeInDiagramEditorStatefulService(new Runnable() {
			@Override
			public void run() {
				Resource codeSyncMappingResource = CodeSyncDiagramOperationsService1.getCodeSyncMappingResource(diagramEditableResource);
				CodeSyncElement srcDir = AddNewTopLevelElementExtension.getOrCreateCodeSyncElementForLocation(codeSyncMappingResource, new String[] { "js" });
				CodeSyncElement newClass = CodeSyncOperationsService.getInstance().create("backboneClass");
				CodeSyncOperationsService.getInstance().add(srcDir, newClass);
			}
		});
	}

}
