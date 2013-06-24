package org.flowerplatform.editor.model.teneo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.emf_model.notation.Bounds;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.notation.Node;
import org.hibernate.cfg.Environment;

public class TestFlowerTeneoChangeRecorder {

	private ChangeRecorder fileChangeRecorder;
	
	private Diagram fileDiagram;
	private String dbDiagramId;
	
	private Bounds fileBoundsToTest;
	private String dbBoundsToTestId;
	
//	@Before
//	public void setUp() throws Exception {
//		TeneoConfig.INSTANCE.initProperties();
//		TeneoConfig.INSTANCE.hibernateProperties.setProperty(Environment.HBM2DDL_AUTO, "create");// "create-drop");
//		TeneoConfig.INSTANCE.init();
//		
//		Resource fileResource = new XMIResourceImpl(URI.createFileURI("sample_emf_diagram/DiagramForTest.flower_diagram"));
//		fileResource.load(Collections.EMPTY_MAP);
//		for (EObject obj : fileResource.getContents()) {
//			if (obj instanceof Diagram) {
//				fileDiagram = (Diagram) obj;
//			}
//		}
//		assertNotNull("The diagram was loaded from disk.", fileDiagram);
//		fileChangeRecorder = new ChangeRecorder();
//		
//		{
//			Resource teneoResource = getResource(null);
//			teneoResource.getContents().add(fileDiagram);
//			teneoResource.save(Collections.EMPTY_MAP);
//			dbDiagramId = teneoResource.getURIFragment(fileDiagram);
//			teneoResource.unload();
//
//			// reload the file resource
//			fileResource.unload();
//			fileResource.load(Collections.EMPTY_MAP);
//			for (EObject obj : fileResource.getContents()) {
//				if (obj instanceof Diagram) {
//					fileDiagram = (Diagram) obj;
//				}
//			}
//
//		}
//		
//		{
//			Resource teneoResource = getResource(null);
//			Diagram teneoDiagram = (Diagram) teneoResource.getEObject(dbDiagramId);
//			assertTrue("The diagram from DB != the orginal diagram", fileDiagram != teneoDiagram);
//			
//			// SHOULD BREAK
//			// I was curious if changing stuff would be detected by .equals(); it is.
//	//		teneoDiagram.setName("modif");
//	//		teneoDiagram.getPersistentChildren().add(FlowerDiagramNotationFactory.eINSTANCE.createNode());
//	//		teneoDiagram.getPersistentChildren().get(0).setViewType("modif");
//			
//			assertTrue("The diagram from DB equals (structurally) the original diagram. I.e. it has been correctly imported",
//					new DebugEqualityHelper().equals(fileDiagram, teneoDiagram));
//			
//			fileBoundsToTest = (Bounds) fileDiagram.getPersistentChildren().get(0).getLayoutConstraint();
//			dbBoundsToTestId = teneoResource.getURIFragment(teneoDiagram.getPersistentChildren().get(0).getLayoutConstraint());
//			teneoResource.unload();
//		}
//	}
//	
//	private FlowerTeneoResource getResource(String query) {
//		String uri = TeneoConfig.RESOURCE_URI;
//		if (query != null) {
//			uri += "&query=" + query;
//		}
//		return new FlowerTeneoResource(URI.createURI(uri));
//	}
//	
//	private void compareLists(List<?> originalList, List<?> expectedList) {
//		List<Object> originalListCopy = new ArrayList<Object>(originalList.size());
//		originalListCopy.addAll(originalList);
//		
//		for (Object o : expectedList) {
//			assertTrue("Original list contains element from initial list " + o, originalListCopy.remove(o));
//		}
//		
//		assertEquals("(Copy of) original list empty after removing all expected elements", 0, originalListCopy.size());
//	}
//	
//	public static void setPrivateField(Object instance, Class<?> classOfInstance, String fieldName, Object value) {
//		try {
//			Field field = classOfInstance.getDeclaredField(fieldName);
//			field.setAccessible(true);
//			field.set(instance, value);
//		} catch (Exception e) {
//			throw new RuntimeException(String.format("Could not set value = %s for private field = %s for object = %s", value, fieldName, instance ), e);
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	public static <T> T getPrivateField(Object instance, Class<?> classOfInstance, String fieldName) {
//		try {
//			Field field = classOfInstance.getDeclaredField(fieldName);
//			field.setAccessible(true);
//			return (T) field.get(instance);
//		} catch(Exception e) {
//			throw new RuntimeException(String.format("Could not get value for private field = %s for object = %s", fieldName, instance ), e);
//		}
//	}
//	
//	@Test
//	public void testModifyBounds() {
//		fileChangeRecorder.beginRecording(Collections.singleton(fileDiagram));
//		FlowerTeneoResource teneoResource = getResource(null);
//		Bounds teneoBoundsToTest = (Bounds) teneoResource.getEObject(dbBoundsToTestId);
//		/////////////////////////////
//		// Check results: bounds are equal
//		///////////////////////////
//		assertTrue("Bounds from FS equald Bounds from DB", new DebugEqualityHelper().equals(fileBoundsToTest, teneoBoundsToTest));
//		
//		/////////////////////////////
//		// Do action: modify bounds
//		/////////////////////////////
//		fileBoundsToTest.setX(500);
//		fileBoundsToTest.setY(500);
//		fileBoundsToTest.setHeight(500);
//		fileBoundsToTest.setWidth(500);
//		
//		teneoBoundsToTest.setX(500);
//		teneoBoundsToTest.setY(500);
//		teneoBoundsToTest.setHeight(500);
//		teneoBoundsToTest.setWidth(500);
//		
//		teneoResource.save(Collections.EMPTY_MAP);
//		
//		/////////////////////////////
//		// Check results: check that the change recorder is installed only on these objects
//		/////////////////////////////
//
//		// SHOULD BREAK because would load lazily more objects than expected
////		System.out.println(((Node) teneoBoundsToTest.eContainer()).getPersistentChildren());
//
//		List<?> changeRecorderTargetObjects = getPrivateField(teneoResource.changeRecorder, ChangeRecorder.class, "targetObjects");
//		compareLists(changeRecorderTargetObjects, Arrays.asList(
//				teneoBoundsToTest, // bounds
//				teneoBoundsToTest.eContainer(), // node/class
//				teneoBoundsToTest.eContainer().eContainer() // diagram
//				));
//
//		/////////////////////////////
//		// Check results: change descriptions equal
//		/////////////////////////////
//		ChangeDescription fileCD = fileChangeRecorder.endRecording();
//		ChangeDescription dbCD = teneoResource.changeRecorder.endRecording();
//		
//		assertTrue("ChangeDescriptions from FS and DB are equal", new DebugEqualityHelper().equals(fileCD, dbCD));
//		
//		teneoResource.unload();
//	}
//
//	@Test
//	public void testAddNode() {
//		fileChangeRecorder.beginRecording(Collections.singleton(fileDiagram));
//		FlowerTeneoResource dbResource = getResource(null);
//		Diagram dbDiagram = (Diagram) dbResource.getEObject(dbDiagramId);
//		
//		/////////////////////////////
//		// Do action: add node
//		/////////////////////////////
//		Node newFileNode = FlowerDiagramNotationFactory.eINSTANCE.createNode();
//		newFileNode.setViewType("newNode");
//		fileDiagram.getPersistentChildren().add(newFileNode);
//
//		Node newDbNode = FlowerDiagramNotationFactory.eINSTANCE.createNode();
//		newDbNode.setViewType("newNode");
//		dbDiagram.getPersistentChildren().add(newDbNode);
//		
//		dbResource.save(Collections.EMPTY_MAP);
//		
//		/////////////////////////////
//		// Check results: check that the change recorder is installed only on these objects
//		/////////////////////////////
//		List<?> changeRecorderTargetObjects = getPrivateField(dbResource.changeRecorder, ChangeRecorder.class, "targetObjects");
//		List<Object> expectedTargetObjects = new ArrayList<Object>(); 
//		for (Node node : dbDiagram.getPersistentChildren()) {
//			expectedTargetObjects.add(node);
//			if (node.getLayoutConstraint() != null) {
//				// add the Bounds as well; because it is a 1-to-1 relation, the bounds are loaded together with
//				// the parent
//				expectedTargetObjects.add(node.getLayoutConstraint());
//			}
//		}
//		expectedTargetObjects.add(dbDiagram);
//		compareLists(changeRecorderTargetObjects, expectedTargetObjects);
//		
//		/////////////////////////////
//		// Check results: change descriptions equal
//		/////////////////////////////
//		ChangeDescription fileCD = fileChangeRecorder.endRecording();
//		ChangeDescription dbCD = dbResource.changeRecorder.endRecording();
//		
//		assertTrue("ChangeDescriptions from FS and DB are equal", new DebugEqualityHelper().equals(fileCD, dbCD));
//		
//		/////////////////////////////
//		// Check results: diagrams are equal
//		// we test this at the end, because it will fetch from the DB the whole diagram tree, and it would
//		// mess with the above test
//		/////////////////////////////
//		assertTrue("Bounds from FS equald Bounds from DB", new DebugEqualityHelper().equals(fileDiagram, dbDiagram));
//		
//		dbResource.unload();
//	}
//
//	@Test
//	public void testRemoveNode() {
//		fileChangeRecorder.beginRecording(Collections.singleton(fileDiagram));
//		FlowerTeneoResource dbResource = getResource(null);
//		Diagram dbDiagram = (Diagram) dbResource.getEObject(dbDiagramId);
//		
//		/////////////////////////////
//		// Do action: remove node
//		/////////////////////////////
//		fileDiagram.getPersistentChildren().remove(0);
//		dbDiagram.getPersistentChildren().get(0); // needed because of bug: https://bugs.eclipse.org/bugs/show_bug.cgi?id=407290
//		dbDiagram.getPersistentChildren().remove(0);
//		
//		dbResource.save(Collections.EMPTY_MAP);
//		
//		// I skipped the part with testing what's in the ChangeRecorder.targetObjects, because I see that the removed objects are still
//		// there. However, I see that the original behavior is like this as well. I.e. the objects are not removed from there until 
//		// ChangeRecorder.dispose()
//		
//		/////////////////////////////
//		// Check results: change descriptions equal
//		/////////////////////////////
//		ChangeDescription fileCD = fileChangeRecorder.endRecording();
//		ChangeDescription dbCD = dbResource.changeRecorder.endRecording();
//		
//		assertTrue("ChangeDescriptions from FS and DB are equal", new DebugEqualityHelper().equals(fileCD, dbCD));
//		
//		/////////////////////////////
//		// Check results: diagrams are equal
//		// we test this at the end, because it will fetch from the DB the whole diagram tree, and it would
//		// mess with the above test
//		/////////////////////////////
//		assertTrue("Bounds from FS equald Bounds from DB", new DebugEqualityHelper().equals(fileDiagram, dbDiagram));
//		
//		dbResource.unload();
//	}
	
}
