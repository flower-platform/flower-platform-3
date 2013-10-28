package org.flowerplatform.editor.model.changes_processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.codesync.config.extension.AddNewExtension_TopLevelElement;
import org.flowerplatform.codesync.remote.CodeSyncDiagramOperationsService1;
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
import org.flowerplatform.web.tests.TestUtil;
import org.junit.BeforeClass;
import org.junit.Test;

import com.crispico.flower.mp.codesync.base.communication.CodeSyncEditorStatefulService;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncFactory;

public class ChangesProcessorTest {
	
	public static final String PROJECT = "editor.model.changes_descriptor";
	
	// TODO CS: here without /; in other places with /
	private static final String ER_PATH = "org/ws_trunk/" + PROJECT + "/changesDescriptionDiagram.notation";
	
	private static CommunicationChannel communicationChannel = new RecordingTestWebCommunicationChannel();
	private static DiagramEditorStatefulService diagramEditorStatefulService;
	private static CodeSyncEditorStatefulService codeSyncEditorStatefulService;
	private static DiagramEditableResource diagramEditableResource;
	
	private static Map<EObject, Changes> observedChanges = new HashMap<EObject, Changes>();
	
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
				observedChanges.put(object, changes);	
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
	public void testAddRemove() {
		// TODO CS the static references
		final Resource codeSyncMappingResource = CodeSyncDiagramOperationsService1.getCodeSyncMappingResource(diagramEditableResource);
		final CodeSyncElement srcDir = AddNewExtension_TopLevelElement.getOrCreateCodeSyncElementForLocation(codeSyncMappingResource, new String[] { "js" });
		
		/**
		 * Adding on an empty list.
		 */
		{
			observedChanges.clear();
			final CodeSyncElement a = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
			final CodeSyncElement b = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
			final CodeSyncElement c = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
			
			executeInDiagramEditorStatefulService(new Runnable() {
				@Override
				public void run() {
					a.setName("a"); b.setName("b"); c.setName("c");
					srcDir.getChildren().add(a); srcDir.getChildren().add(b); srcDir.getChildren().add(c);
				}
			});
			Changes changes;
			changes = observedChanges.remove(a);
			assertNotNull("Changes expected", changes);
			assertEquals("1 addedTo item", 1, changes.getAddedTo().size());
			assertNull("No removedItems", changes.getRemovedFrom());
			assertSame("addedTo item", srcDir, changes.getAddedTo().get(0).a);

			changes = observedChanges.remove(b);
			assertNotNull("Changes expected", changes);
			assertEquals("1 addedTo item", 1, changes.getAddedTo().size());
			assertNull("No removedItems", changes.getRemovedFrom());
			assertSame("addedTo item", srcDir, changes.getAddedTo().get(0).a);

			changes = observedChanges.remove(c);
			assertNotNull("Changes expected", changes);
			assertEquals("1 addedTo item", 1, changes.getAddedTo().size());
			assertNull("No removedItems", changes.getRemovedFrom());
			assertSame("addedTo item", srcDir, changes.getAddedTo().get(0).a);
			
			observedChanges.remove(srcDir);
			assertEquals("No other objects changed", 0, observedChanges.size());
		}
		
		/**
		 * Same as above. But this time the list is not empty!
		 */
		{
			observedChanges.clear();
			final CodeSyncElement a = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
			final CodeSyncElement b = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
			final CodeSyncElement c = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
			
			executeInDiagramEditorStatefulService(new Runnable() {
				@Override
				public void run() {
					a.setName("a1"); b.setName("b1"); c.setName("c1");
					srcDir.getChildren().add(a); srcDir.getChildren().add(b); srcDir.getChildren().add(c);
				}
			});
			Changes changes;
			changes = observedChanges.remove(a);
			assertNotNull("Changes expected", changes);
			assertEquals("1 addedTo item", 1, changes.getAddedTo().size());
			assertNull("No removedItems", changes.getRemovedFrom());
			assertSame("addedTo item", srcDir, changes.getAddedTo().get(0).a);

			changes = observedChanges.remove(b);
			assertNotNull("Changes expected", changes);
			assertEquals("1 addedTo item", 1, changes.getAddedTo().size());
			assertNull("No removedItems", changes.getRemovedFrom());
			assertSame("addedTo item", srcDir, changes.getAddedTo().get(0).a);

			changes = observedChanges.remove(c);
			assertNotNull("Changes expected", changes);
			assertEquals("1 addedTo item", 1, changes.getAddedTo().size());
			assertNull("No removedItems", changes.getRemovedFrom());
			assertSame("addedTo item", srcDir, changes.getAddedTo().get(0).a);
			
			observedChanges.remove(srcDir);
			assertEquals("No other objects changed", 0, observedChanges.size());
		}
		
		/**
		 * Same as above. But this time the list is not empty!
		 * And we insert (not add at the end).
		 */
		{
			observedChanges.clear();
			final CodeSyncElement a = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
			final CodeSyncElement b = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
			final CodeSyncElement c = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
			
			executeInDiagramEditorStatefulService(new Runnable() {
				@Override
				public void run() {
					a.setName("a2"); b.setName("b2"); c.setName("c2");
					srcDir.getChildren().add(srcDir.getChildren().size() - 1, a); srcDir.getChildren().add(0, b); srcDir.getChildren().add(srcDir.getChildren().size() / 2, c);
				}
			});
			Changes changes;
			changes = observedChanges.remove(a);
			assertNotNull("Changes expected", changes);
			assertEquals("1 addedTo item", 1, changes.getAddedTo().size());
			assertNull("No removedItems", changes.getRemovedFrom());
			assertSame("addedTo item", srcDir, changes.getAddedTo().get(0).a);

			changes = observedChanges.remove(b);
			assertNotNull("Changes expected", changes);
			assertEquals("1 addedTo item", 1, changes.getAddedTo().size());
			assertNull("No removedItems", changes.getRemovedFrom());
			assertSame("addedTo item", srcDir, changes.getAddedTo().get(0).a);

			changes = observedChanges.remove(c);
			assertNotNull("Changes expected", changes);
			assertEquals("1 addedTo item", 1, changes.getAddedTo().size());
			assertNull("No removedItems", changes.getRemovedFrom());
			assertSame("addedTo item", srcDir, changes.getAddedTo().get(0).a);
			
			observedChanges.remove(srcDir);
			assertEquals("No other objects changed", 0, observedChanges.size());
		}
		
		/**
		 * Delete elements at various indexes.
		 */
		{
			observedChanges.clear();
			assertEquals("Number of elements in srcDir", 9, srcDir.getChildren().size());
			final CodeSyncElement a = srcDir.getChildren().get(8);
			final CodeSyncElement b = srcDir.getChildren().get(7);
			final CodeSyncElement c = srcDir.getChildren().get(4);
			final CodeSyncElement d = srcDir.getChildren().get(0);
			
			executeInDiagramEditorStatefulService(new Runnable() {
				@Override
				public void run() {
					srcDir.getChildren().remove(a); srcDir.getChildren().remove(b);
					srcDir.getChildren().remove(c); srcDir.getChildren().remove(d);
				}
			});

			Changes changes;
			changes = observedChanges.remove(a);
			assertNotNull("Changes expected", changes);
			assertNull("No addedItems", changes.getAddedTo());
			assertEquals("1 removedFrom items", 1, changes.getRemovedFrom().size());
			assertSame("removedFrom item", srcDir, changes.getRemovedFrom().get(0).a);

			changes = observedChanges.remove(b);
			assertNotNull("Changes expected", changes);
			assertNull("No addedItems", changes.getAddedTo());
			assertEquals("1 removedFrom items", 1, changes.getRemovedFrom().size());
			assertSame("removedFrom item", srcDir, changes.getRemovedFrom().get(0).a);

			changes = observedChanges.remove(c);
			assertNotNull("Changes expected", changes);
			assertNull("No addedItems", changes.getAddedTo());
			assertEquals("1 removedFrom items", 1, changes.getRemovedFrom().size());
			assertSame("removedFrom item", srcDir, changes.getRemovedFrom().get(0).a);

			changes = observedChanges.remove(d);
			assertNotNull("Changes expected", changes);
			assertNull("No addedItems", changes.getAddedTo());
			assertEquals("1 removedFrom items", 1, changes.getRemovedFrom().size());
			assertSame("removedFrom item", srcDir, changes.getRemovedFrom().get(0).a);

			observedChanges.remove(srcDir);
			assertEquals("No other objects changed", 0, observedChanges.size());
		}

		/**
		 * Mixed add and delete
		 */
		{
			observedChanges.clear();
			assertEquals("Number of elements in srcDir", 5, srcDir.getChildren().size());
			final CodeSyncElement a = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
			final CodeSyncElement b = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
			final CodeSyncElement c = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
			final CodeSyncElement d = srcDir.getChildren().get(0);
			final CodeSyncElement e = srcDir.getChildren().get(srcDir.getChildren().size() - 1);
			
			executeInDiagramEditorStatefulService(new Runnable() {
				@Override
				public void run() {
					srcDir.getChildren().remove(d);
					a.setName("a3"); b.setName("b3"); c.setName("c3");
					srcDir.getChildren().add(srcDir.getChildren().size() - 1, a); srcDir.getChildren().add(0, b); srcDir.getChildren().add(srcDir.getChildren().size() / 2, c);
					srcDir.getChildren().remove(e);
				}
			});
			Changes changes;
			changes = observedChanges.remove(a);
			assertNotNull("Changes expected", changes);
			assertEquals("1 addedTo item", 1, changes.getAddedTo().size());
			assertNull("No removedItems", changes.getRemovedFrom());
			assertSame("addedTo item", srcDir, changes.getAddedTo().get(0).a);

			changes = observedChanges.remove(b);
			assertNotNull("Changes expected", changes);
			assertEquals("1 addedTo item", 1, changes.getAddedTo().size());
			assertNull("No removedItems", changes.getRemovedFrom());
			assertSame("addedTo item", srcDir, changes.getAddedTo().get(0).a);

			changes = observedChanges.remove(c);
			assertNotNull("Changes expected", changes);
			assertEquals("1 addedTo item", 1, changes.getAddedTo().size());
			assertNull("No removedItems", changes.getRemovedFrom());
			assertSame("addedTo item", srcDir, changes.getAddedTo().get(0).a);

			changes = observedChanges.remove(d);
			assertNotNull("Changes expected", changes);
			assertNull("No addedItems", changes.getAddedTo());
			assertEquals("1 removedFrom items", 1, changes.getRemovedFrom().size());
			assertSame("removedFrom item", srcDir, changes.getRemovedFrom().get(0).a);

			changes = observedChanges.remove(e);
			assertNotNull("Changes expected", changes);
			assertNull("No addedItems", changes.getAddedTo());
			assertEquals("1 removedFrom items", 1, changes.getRemovedFrom().size());
			assertSame("removedFrom item", srcDir, changes.getRemovedFrom().get(0).a);
			
			observedChanges.remove(srcDir);
			assertEquals("No other objects changed", 0, observedChanges.size());
		}

	}

}
