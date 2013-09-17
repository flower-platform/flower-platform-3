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
package org.flowerplatform.web.tests.diagram_processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.IStatefulClientLocalState;
import org.flowerplatform.communication.stateful_service.InvokeStatefulClientMethodClientCommand;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.editor.mindmap.MindMapModelPlugin;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.editor.model.remote.ViewDetailsUpdate;
import org.flowerplatform.editor.remote.EditorStatefulClientLocalState;
import org.flowerplatform.emf_model.notation.View;
import org.flowerplatform.emf_model.notation.impl.DiagramImpl;
import org.flowerplatform.web.communication.RecordingTestWebCommunicationChannel;
import org.flowerplatform.web.tests.TestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.crispico.flower.mp.model.codesync.MindMapRoot;

public class MindMapDiagramProcessorsTest {

	public static final String PROJECT = "diagram_processors";
	public static final String MINDMAP = "test.mindmap";
	public static final String EDITABLE_RESOURCE_PATH = "/org/ws_trunk/"
			+ PROJECT + "/" + MINDMAP;
	public static final String DIR = TestUtil.ECLIPSE_DEPENDENT_FILES_DIR + "/"
			+ PROJECT + "/";

	private static RecordingTestWebCommunicationChannel communicationChannel = new RecordingTestWebCommunicationChannel();

	private static DiagramEditorStatefulService diagramService;
	private static MindMapRoot mmRoot;
	private static Resource mainResource;
	private static DiagramImpl diagImpl;

	@BeforeClass
	public static void setUpBeforeClass() {
		ServiceInvocationContext context = new ServiceInvocationContext(
				communicationChannel);
		TestUtil.copyFilesAndCreateProject(context, DIR
				+ TestUtil.INITIAL_TO_BE_COPIED, PROJECT);
		MindMapModelPlugin.getInstance();
		diagramService = (DiagramEditorStatefulService) CommunicationPlugin
				.getInstance().getServiceRegistry()
				.getService("mindmapEditorStatefulService");

	}

	@Before
	public void setUp() throws Exception {
		StatefulServiceInvocationContext context = new StatefulServiceInvocationContext(
				communicationChannel);
		IStatefulClientLocalState statefulClientLocalState = new EditorStatefulClientLocalState(
				EDITABLE_RESOURCE_PATH);
		diagramService.subscribe(context, statefulClientLocalState);
		communicationChannel.getRecordedCommands().clear();
		mainResource = ((DiagramEditableResource) diagramService
				.getEditableResource(EDITABLE_RESOURCE_PATH)).getMainResource();
		diagImpl = (DiagramImpl) mainResource.getContents().get(0);
		mmRoot = (MindMapRoot) mainResource.getContents().get(1);
	}

	@After
	public void tearDown() throws Exception {
		diagramService.unsubscribeAllClientsForcefully(EDITABLE_RESOURCE_PATH,
				false);
	}

	@Test
	public void testSetText() {
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(diagImpl.getPersistentChildren().get(0)
				.getPersistentChildren().get(0).getIdBeforeRemoval()); // viewId
																		// Child
																		// 1
																		// "_LxU90Rn1EeOhZIwGt-yCqA"
		parameters.add("New text");
		// to update
		Collection<Object> expectedObjectsToUpdate = new ArrayList<Object>();
		// to Dispose
		Collection<String> expectedIdsToDispose = new ArrayList<String>();
		// DetailsUpdates
		Collection<ViewDetailsUpdate> expectedViewDetailsUpdates = new ArrayList<ViewDetailsUpdate>();
		ViewDetailsUpdate viewToUpdate = new ViewDetailsUpdate();
		Map<String, Object> viewDetails = new HashMap<String, Object>();
		viewDetails.put("text", "New text");
		viewToUpdate.setViewDetails(viewDetails);
		viewToUpdate.setViewId(diagImpl.getPersistentChildren().get(0)
				.getPersistentChildren().get(0).getIdBeforeRemoval());
		expectedViewDetailsUpdates.add(viewToUpdate);

		invokeServiceMethod("setText", parameters);
		assertSentCommandExpend(expectedObjectsToUpdate, expectedIdsToDispose,
				expectedViewDetailsUpdates);

	}

	@Test
	public void testChangeParent() {
		// TODO
	}

	@Test
	public void testSetSide() {
		// TODO
	}

	@Test
	public void testMoveUp() {
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(diagImpl.getPersistentChildren().get(0)
				.getPersistentChildren().get(1).getIdBeforeRemoval());// viewId
																		// (Child
																		// 2)
		// to update
		Collection<Object> expectedObjectsToUpdate = new ArrayList<Object>();
		expectedObjectsToUpdate.add(mmRoot);

		// to Dispose
		Collection<String> expectedIdsToDispose = new ArrayList<String>();
		// DetailsUpdates
		Collection<ViewDetailsUpdate> expectedViewDetailsUpdates = new ArrayList<ViewDetailsUpdate>();
		ViewDetailsUpdate viewToUpdate = new ViewDetailsUpdate();
		Map<String, Object> viewDetails = new HashMap<String, Object>();
		String[] label = new String[7];
		ArrayList<Object> value = new ArrayList<Object>();
		label[0] = "text";
		value.add("Root");
		label[1] = "hasChildren";
		value.add(new Boolean(true));
		label[2] = "side";
		value.add(new Integer(0));
		label[3] = ("minWidth");
		value.add(new Long(1));
		label[4] = ("maxWidth");
		value.add(new Long(600));
		label[5] = "icons";
		value.add(new ArrayList<>()); // EDataTypeEList
		label[6] = "expanded";
		value.add(new Boolean(true));
		createViewDetail(label, value, viewDetails);
		viewToUpdate.setViewId(diagImpl.getPersistentChildren().get(0));
		viewToUpdate.setViewDetails(viewDetails);
		expectedViewDetailsUpdates.add(viewToUpdate);
		;

		invokeServiceMethod("moveUp", parameters);
		assertSentCommandExpend(expectedObjectsToUpdate, expectedIdsToDispose,
				expectedViewDetailsUpdates);
	}

	@Test
	public void testMoveDown() {
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(diagImpl.getPersistentChildren().get(0)
				.getPersistentChildren().get(0).getIdBeforeRemoval());// viewId
																		// (Child
																		// 1)
		// to update
		Collection<Object> expectedObjectsToUpdate = new ArrayList<Object>();
		expectedObjectsToUpdate.add(mmRoot);

		// to Dispose
		Collection<String> expectedIdsToDispose = new ArrayList<String>();
		// DetailsUpdates
		Collection<ViewDetailsUpdate> expectedViewDetailsUpdates = new ArrayList<ViewDetailsUpdate>();
		ViewDetailsUpdate viewToUpdate = new ViewDetailsUpdate();
		Map<String, Object> viewDetails = new HashMap<String, Object>();
		String[] label = new String[7];
		ArrayList<Object> value = new ArrayList<Object>();
		label[0] = "text";
		value.add("Root");
		label[1] = "hasChildren";
		value.add(new Boolean(true));
		label[2] = "side";
		value.add(new Integer(0));
		label[3] = ("minWidth");
		value.add(new Long(1));
		label[4] = ("maxWidth");
		value.add(new Long(600));
		label[5] = "icons";
		value.add(new ArrayList<>()); // EDataTypeEList
		label[6] = "expanded";
		value.add(new Boolean(true));
		createViewDetail(label, value, viewDetails);
		viewToUpdate.setViewId(diagImpl.getPersistentChildren().get(0));
		viewToUpdate.setViewDetails(viewDetails);
		expectedViewDetailsUpdates.add(viewToUpdate);
		;

		invokeServiceMethod("moveDown", parameters);
		assertSentCommandExpend(expectedObjectsToUpdate, expectedIdsToDispose,
				expectedViewDetailsUpdates);
	}

	@Test
	public void testCreateNode() {
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(diagImpl.getPersistentChildren().get(0)
				.getPersistentChildren().get(0).getIdBeforeRemoval());// viewId
																		// (Child
																		// 1)
		parameters.add("default");
		invokeServiceMethod("createNode", parameters);
		// to update
		Collection<Object> expectedObjectsToUpdate = new ArrayList<Object>();
		expectedObjectsToUpdate.add(mmRoot.getChildren().get(0));
		expectedObjectsToUpdate.add(mmRoot.getChildren().get(0).getChildren()
				.get(1));

		// to Dispose
		Collection<String> expectedIdsToDispose = new ArrayList<String>();
		// DetailsUpdates
		Collection<ViewDetailsUpdate> expectedViewDetailsUpdates = new ArrayList<ViewDetailsUpdate>();
		ViewDetailsUpdate viewToUpdate = new ViewDetailsUpdate();
		// child 1
		Map<String, Object> viewDetails = new HashMap<String, Object>();
		String[] label = new String[7];
		ArrayList<Object> value = new ArrayList<Object>();
		label[0] = "text";
		value.add("Child 1");
		label[1] = "hasChildren";
		value.add(new Boolean(true));
		label[2] = "side";
		value.add(new Integer(1));
		label[3] = ("minWidth");
		value.add(new Long(1));
		label[4] = ("maxWidth");
		value.add(new Long(600));
		label[5] = "icons";
		value.add(new ArrayList<>()); // EDataTypeEList
		label[6] = "expanded";
		value.add(new Boolean(true));
		createViewDetail(label, value, viewDetails);
		viewToUpdate.setViewId(diagImpl.getPersistentChildren().get(0)
				.getPersistentChildren().get(0));
		viewToUpdate.setViewDetails(viewDetails);
		expectedViewDetailsUpdates.add(viewToUpdate);
		// Child 1.2
		ViewDetailsUpdate viewToUpdate2 = new ViewDetailsUpdate();
		ArrayList<Object> value2 = new ArrayList<Object>();
		value2.add("New default");
		value2.add(new Boolean(false));
		value2.add(new Integer(1));
		value2.add(new Long(1));
		value2.add(new Long(600));
		value2.add(new ArrayList<>()); // EDataTypeEList
		value2.add(new Boolean(false));
		createViewDetail(label, value2, viewDetails);
		viewToUpdate2.setViewId(diagImpl.getPersistentChildren().get(0)
				.getPersistentChildren().get(0).getPersistentChildren().get(1));
		viewToUpdate2.setViewDetails(viewDetails);
		expectedViewDetailsUpdates.add(viewToUpdate2);

		assertSentCommandExpend(expectedObjectsToUpdate, expectedIdsToDispose,
				expectedViewDetailsUpdates);
	}

	@Test
	public void testDelete() {
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(diagImpl.getPersistentChildren().get(0)
				.getPersistentChildren().get(0).getIdBeforeRemoval());// viewId
																		// (Child
																		// 1)

		// to update
		Collection<Object> expectedObjectsToUpdate = new ArrayList<Object>();
		expectedObjectsToUpdate.add(mmRoot);

		// to Dispose
		Collection<String> expectedIdsToDispose = new ArrayList<String>();
		expectedIdsToDispose.add(diagImpl.getPersistentChildren().get(0)
				.getPersistentChildren().get(0).getIdBeforeRemoval());
		expectedIdsToDispose.add(diagImpl.getPersistentChildren().get(0)
				.getPersistentChildren().get(0).getPersistentChildren().get(0)
				.getIdBeforeRemoval());

		// DetailsUpdates
		Collection<ViewDetailsUpdate> expectedViewDetailsUpdates = new ArrayList<ViewDetailsUpdate>();
		ViewDetailsUpdate viewToUpdate = new ViewDetailsUpdate();
		// child 1
		Map<String, Object> viewDetails = new HashMap<String, Object>();
		String[] label = new String[7];
		ArrayList<Object> value = new ArrayList<Object>();
		label[0] = "text";
		value.add("Root");
		label[1] = "hasChildren";
		value.add(new Boolean(true));
		label[2] = "side";
		value.add(new Integer(0));
		label[3] = ("minWidth");
		value.add(new Long(1));
		label[4] = ("maxWidth");
		value.add(new Long(600));
		label[5] = "icons";
		value.add(new ArrayList<>()); // EDataTypeEList
		label[6] = "expanded";
		value.add(new Boolean(true));
		createViewDetail(label, value, viewDetails);
		viewToUpdate.setViewId(diagImpl.getPersistentChildren().get(0));
		viewToUpdate.setViewDetails(viewDetails);
		expectedViewDetailsUpdates.add(viewToUpdate);

		invokeServiceMethod("delete", parameters);
		assertSentCommandExpend(expectedObjectsToUpdate, expectedIdsToDispose,
				expectedViewDetailsUpdates);
	}

	@Test
	public void testRemoveAllIcons() {
		// TODO
	}

	@Test
	public void testRemoveFirstIcon() {
		// TODO
	}

	@Test
	public void testRemoveLastIcon() {
		// TODO
	}

	@Test
	public void testAddIcon() {
		List<Object> parameters = new ArrayList<Object>();
		List<String> viewIds= new ArrayList<>();
		String viewIdtoUpdate1 = diagImpl.getPersistentChildren().get(0).getPersistentChildren().get(0).getIdBeforeRemoval();
		String viewIdtoUpdate2 = diagImpl.getPersistentChildren().get(0).getPersistentChildren().get(1).getIdBeforeRemoval();
		String iconToAdd = "/images/icons/full-1.png";
		viewIds.add(viewIdtoUpdate1);
		viewIds.add(viewIdtoUpdate2);		
		parameters.add(viewIds);
		parameters.add(iconToAdd);
		
		
		//to update
		Collection<Object> expectedObjectsToUpdate = new ArrayList<Object>();
		//to Dispose
		Collection<String> expectedIdsToDispose = new ArrayList<String>();		
		//DetailsUpdates
		Collection<ViewDetailsUpdate> expectedViewDetailsUpdates = new ArrayList<ViewDetailsUpdate>();
		for (String id : viewIds){			
			ViewDetailsUpdate viewToUpdate = new ViewDetailsUpdate();						
			Map<String, Object> viewDetails = new HashMap<String, Object>();
			List<String> iconList = new ArrayList<>();
			iconList.add(iconToAdd);
			viewDetails.put("icons",iconList );
			viewToUpdate.setViewDetails(viewDetails);
			viewToUpdate.setViewId(id);
			expectedViewDetailsUpdates.add(viewToUpdate);
		}
		invokeServiceMethod("addIcon", parameters);
		assertSentCommandExpend(expectedObjectsToUpdate, expectedIdsToDispose, expectedViewDetailsUpdates);

	}

	@Test
	public void testSetMinMaxWidth() {
		List<Object> parameters = new ArrayList<Object>();
		List<String> viewIds = new ArrayList<>();
		long minWidth = 2;
		long maxWidth = 599;
		String viewIdtoUpdate1 = diagImpl.getPersistentChildren().get(0)
				.getPersistentChildren().get(0).getIdBeforeRemoval();
		String viewIdtoUpdate2 = diagImpl.getPersistentChildren().get(0)
				.getPersistentChildren().get(1).getIdBeforeRemoval();
		viewIds.add(viewIdtoUpdate1);
		viewIds.add(viewIdtoUpdate2);
		parameters.add(viewIds); // viewId Child 1 & Child 2.
									// "_LxU90Rn1EeOhZIwGt-yCqA"
		parameters.add(minWidth);
		parameters.add(maxWidth);
		// to update
		Collection<Object> expectedObjectsToUpdate = new ArrayList<Object>();
		// to Dispose
		Collection<String> expectedIdsToDispose = new ArrayList<String>();
		// DetailsUpdates
		Collection<ViewDetailsUpdate> expectedViewDetailsUpdates = new ArrayList<ViewDetailsUpdate>();
		for (String id : viewIds) {
			ViewDetailsUpdate viewToUpdate = new ViewDetailsUpdate();
			Map<String, Object> viewDetails = new HashMap<String, Object>();
			viewDetails.put("minWidth", minWidth);
			viewDetails.put("maxWidth", maxWidth);
			viewToUpdate.setViewDetails(viewDetails);
			viewToUpdate.setViewId(id);
			expectedViewDetailsUpdates.add(viewToUpdate);
		}
		invokeServiceMethod("setMinMaxWidth", parameters);
		assertSentCommandExpend(expectedObjectsToUpdate, expectedIdsToDispose,
				expectedViewDetailsUpdates);
	}

	@Test
	public void testSetExpandedFalse() {
		List<Object> parameters = new ArrayList<Object>();
		parameters.add("_LxU90Rn1EeOhZIwGt-yCqA"); // viewId
		parameters.add(false); // expanded
		// to update
		Collection<Object> expectedObjectsToUpdate = new ArrayList<Object>();
		expectedObjectsToUpdate.add(mmRoot.getChildren().get(0));

		// to Dispose
		Collection<String> expectedIdsToDispose = new ArrayList<String>();
		expectedIdsToDispose.add(diagImpl.getPersistentChildren().get(0)
				.getPersistentChildren().get(0).getPersistentChildren().get(0)
				.getIdBeforeRemoval()); // _N6C90Bn1EeOhZIwGt-yCqA

		// DetailsUpdates
		Collection<ViewDetailsUpdate> expectedViewDetailsUpdates = new ArrayList<ViewDetailsUpdate>();
		ViewDetailsUpdate viewToUpdate = new ViewDetailsUpdate();
		Map<String, Object> viewDetails = new HashMap<String, Object>();
		// 1
		viewDetails.put("expanded", new Boolean(false));
		viewToUpdate.setViewId(diagImpl.getPersistentChildren().get(0)
				.getPersistentChildren().get(0).getIdBeforeRemoval());
		viewToUpdate.setViewDetails(viewDetails);
		expectedViewDetailsUpdates.add(viewToUpdate);
		// 2
		ViewDetailsUpdate viewToUpdate2 = new ViewDetailsUpdate();
		Map<String, Object> viewDetails2 = new HashMap<String, Object>();
		String[] label = new String[7];
		ArrayList<Object> value = new ArrayList<Object>();
		label[0] = "text";
		value.add("Child 1");
		label[1] = "hasChildren";
		value.add(new Boolean(true));
		label[2] = "side";
		value.add(new Integer(1));
		label[3] = ("minWidth");
		value.add(new Long(1));
		label[4] = ("maxWidth");
		value.add(new Long(600));
		label[5] = "icons";
		value.add(new ArrayList<>()); // EDataTypeEList
		label[6] = "expanded";
		value.add(new Boolean(false));
		createViewDetail(label, value, viewDetails2);
		viewToUpdate2.setViewDetails(viewDetails2);
		expectedViewDetailsUpdates.add(viewToUpdate2);

		invokeServiceMethod("setExpanded", parameters);
		assertSentCommandExpend(expectedObjectsToUpdate, expectedIdsToDispose,
				expectedViewDetailsUpdates);
	}

	@Test
	public void testSetExpandedTrue() {
		List<Object> parameters = new ArrayList<Object>();
		parameters.add("_rXBwsRorEeOhZIwGt-yCqA"); // viewId
		parameters.add(true); // expanded = true

		// to update
		Collection<Object> expectedObjectsToUpdate = new ArrayList<Object>();
		expectedObjectsToUpdate.add(mmRoot.getChildren().get(1));
		expectedObjectsToUpdate.add(mmRoot.getChildren().get(1).getChildren()
				.get(0));
		// to dispose
		Collection<String> expectedIdsToDispose = new ArrayList<String>();
		// details updates
		Collection<ViewDetailsUpdate> expectedViewDetailsUpdates = new ArrayList<ViewDetailsUpdate>();
		// 1
		ViewDetailsUpdate viewToUpdate1 = new ViewDetailsUpdate();
		Map<String, Object> viewDetails = new HashMap<String, Object>();
		viewDetails.put("expanded", new Boolean(true));
		viewToUpdate1.setViewDetails(viewDetails);
		viewToUpdate1.setViewId(diagImpl.getPersistentChildren().get(0)
				.getPersistentChildren().get(1).getIdBeforeRemoval());
		expectedViewDetailsUpdates.add(viewToUpdate1);
		// 2
		ViewDetailsUpdate viewToUpdate2 = new ViewDetailsUpdate();
		Map<String, Object> viewDetails2 = new HashMap<String, Object>();
		String[] label = new String[7];
		ArrayList<Object> value = new ArrayList<Object>();
		label[0] = "text";
		value.add("Child 2.1");
		label[1] = "hasChildren";
		value.add(new Boolean(false));
		label[2] = "side";
		value.add(new Integer(1));
		label[3] = ("minWidth");
		value.add(new Long(1));
		label[4] = ("maxWidth");
		value.add(new Long(600));
		label[5] = "icons";
		value.add(new ArrayList<>()); // EDataTypeEList
		label[6] = "expanded";
		value.add(new Boolean(false));
		createViewDetail(label, value, viewDetails2);
		viewToUpdate2.setViewDetails(viewDetails2);
		expectedViewDetailsUpdates.add(viewToUpdate2);

		invokeServiceMethod("setExpanded", parameters);
		assertSentCommandExpend(expectedObjectsToUpdate, expectedIdsToDispose,
				expectedViewDetailsUpdates);
	}

	private void createViewDetail(String[] label, ArrayList<Object> value,
			Map<String, Object> viewDetails) {
		for (int i = 0; i < label.length; i++) {
			viewDetails.put(label[i], value.get(i));
		}
	}

	private void assertSentCommandExpend(
			Collection<Object> expectedObjectsToUpdate,
			Collection<String> expectedIdsToDispose,
			Collection<ViewDetailsUpdate> expectedViewDetailsUpdates) {
		InvokeStatefulClientMethodClientCommand command = (InvokeStatefulClientMethodClientCommand) communicationChannel
				.getRecordedCommands().get(0);
		assertEquals("", "updateTransferableObjects", command.getMethodName());
		Object[] updates = command.getParameters();

		// objects to update
		Collection<View> objectsToUpdate = (Collection<View>) updates[0];
		// assertEquals(expectedObjectsToUpdate.size(), objectsToUpdate.size());
		for (Object id : expectedObjectsToUpdate) {
			boolean exists = false;

			for (View view : objectsToUpdate) {
				EObject model = view.getDiagrammableElement();
				assertNotNull(model);
				if (model.equals((id))) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				fail("Update not sent for " + id);
			}
		}

		// ids to dispose
		Collection<Object> idsToDispose = (Collection<Object>) updates[1];
		assertEquals(expectedIdsToDispose.size(), idsToDispose.size());
		for (Object id : expectedIdsToDispose) {
			boolean exists = false;
			for (Object ob : idsToDispose) {
				if (ob.equals(id)) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				fail("Dispose not sent for " + id);
			}
		}

		// view details updates
		Collection<ViewDetailsUpdate> viewDetailUpdates = (Collection<ViewDetailsUpdate>) updates[2];
		assertEquals(expectedViewDetailsUpdates.size(),
				viewDetailUpdates.size());
		for (ViewDetailsUpdate viewDetail : expectedViewDetailsUpdates) {
			boolean exists = false;
			for (ViewDetailsUpdate view : viewDetailUpdates) {
				if (equalMaps(view.getViewDetails(),
						viewDetail.getViewDetails())) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				fail("UpdateDetail not sent for " + viewDetail.getViewId());
			}
		}

	}

	boolean equalMaps(Map<String, Object> m1, Map<String, Object> m2) {
		if (m1.size() != m2.size())
			return false;
		for (String key : m1.keySet())
			if (m1.get(key) instanceof List) {
				if ( ((List) m1.get(key)).size() != ((List) m2.get(key)).size() )
					return false;
				else {
					if (!((List) m1.get(key)).equals((List) m2.get(key)))
						return false;
				}
			} else if (!m1.get(key).equals(m2.get(key)))
				return false;
		return true;
	}

	private void invokeServiceMethod(String methodName, List<Object> parameters) {
		InvokeServiceMethodServerCommand command = new InvokeServiceMethodServerCommand();
		command.setServiceId("mindMapDiagramOperationsService");
		command.setMethodName(methodName);
		command.setParameters(parameters);
		StatefulServiceInvocationContext context = new StatefulServiceInvocationContext(
				communicationChannel);
		diagramService.attemptUpdateEditableResourceContent(context,
				EDITABLE_RESOURCE_PATH, command);
	}

}
