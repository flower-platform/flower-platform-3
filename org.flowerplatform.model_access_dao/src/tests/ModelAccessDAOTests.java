package tests;

import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.Map.Entry;
import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.flowerplatform.model_access_dao.DAOFactory;
import org.flowerplatform.model_access_dao.model.CodeSyncElement1;
import org.flowerplatform.model_access_dao.model.Diagram1;
import org.flowerplatform.model_access_dao.model.Node1;
import org.flowerplatform.model_access_dao.model.Relation1;
import org.flowerplatform.model_access_dao.model.ResourceInfo;
import org.flowerplatform.model_access_dao.registry.Repository;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	CreateGlobalModel.class,
	CreateDiscussableDesign.class,
//	OpenDiscussableDesignDiagram.class,
//	MoveAndOpenDiscussableDesignDiagram.class
	ImportTest.class
})
public class ModelAccessDAOTests {

	public static CodeSyncElement1 createCSEAndAssertNotNull(UUID repoId, UUID resourceId, UUID id, UUID parentId) {
		id = DAOFactory.codeSyncElementDAO.createCodeSyncElement(repoId, resourceId, id, parentId);
		CodeSyncElement1 cse = DAOFactory.codeSyncElementDAO.getCodeSyncElement(repoId, resourceId, id);
		assertNotNull(cse);
		return cse;
	}
	
	public static void assertCSE(CodeSyncElement1 cse, UUID id, String name) {
		assertEquals(id, cse.getId());
		assertEquals(name, cse.getName());
	}
	
	public static void assertResourceInfo(Resource resource, String repoId, String resourceId) {
		ResourceInfo info = (ResourceInfo) resource.getContents().get(0);
		assertEquals("Wrong repo id", repoId, info.getRepoId());
		assertEquals("Wrong resource id", resourceId, info.getResourceId());
	}
	
	public static Resource loadResource(String path) {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new ResourceFactoryImpl() {
			
			@Override
			public Resource createResource(URI uri) {
				return new XMIResourceImpl(uri);
			}
		});
		
		URI uri = URI.createFileURI(path);
		return resourceSet.getResource(uri, true);
	}
	
	public static UUID getResourceId(Repository dirWithResources, String suffix) {
		for (Entry<UUID, URI> entry : dirWithResources.getResources().entrySet()) {
			if (entry.getValue().toFileString().endsWith(suffix)) {
				return entry.getKey();
			}
		}
		throw new RuntimeException(String.format("No resource with suffix %s for %s", suffix, dirWithResources.getDir()));
	}
	
	public static void printContents(Resource resource, UUID repoId) {
		for (EObject object : resource.getContents()) {
			printContents(object, repoId);
		}
	}
	
	private static void printContents(EObject object, UUID repoId) {
		if (object instanceof Diagram1) {
			Diagram1 dgr = (Diagram1) object;
			for (EObject child : dgr.getChildren()) {
				printContents(child, repoId);
			}
		} else if (object instanceof Node1) {
			Node1 node = (Node1) object;
			CodeSyncElement1 cse = DAOFactory.nodeDAO.getDiagrammableElement(node, repoId);
			if (cse.eIsProxy()) {
				fail("Diagrammable element is proxy");
			}
			System.out.println(String.format("NODE [name = %s] -> DGR ELT [name = %s]", 
					node.getName(), cse.getName()));
			printContents(cse, repoId);
			UUID resourceId = UUID.fromString(node.eResource().getURI().opaquePart());
			for (EObject child : DAOFactory.nodeDAO.getChildren(node, repoId, resourceId)) {
				printContents(child, repoId);
			}
		} else if (object instanceof ResourceInfo) {
			ResourceInfo info = (ResourceInfo) object;
			Repository repo = DAOFactory.registryDAO.getRepository(UUID.fromString(info.getRepoId()));
			File repoFile = repo.getDir();
			URI resourceUri = repo.getResources().get(info.getResourceId());
			System.out.println(String.format("RESOURCE INFO [repo = %s, uri = %s]", repoFile, resourceUri));
		} else if (object instanceof Relation1) {
			Relation1 relation = (Relation1) object;
			CodeSyncElement1 source = DAOFactory.codeSyncElementDAO.getSource(relation, repoId);
			CodeSyncElement1 target = DAOFactory.codeSyncElementDAO.getTarget(relation, repoId);
			System.out.println(String.format("RELATION [source = %s, target = %s]", source.getName(), target.getName()));
		} else if (object instanceof CodeSyncElement1) {
			CodeSyncElement1 codeSyncElement = (CodeSyncElement1) object;
			System.out.println(String.format("CSE [name = %s]", codeSyncElement.getName()));
			UUID resourceId = UUID.fromString(codeSyncElement.eResource().getURI().opaquePart());
			for (EObject child : DAOFactory.codeSyncElementDAO.getChildren(codeSyncElement, repoId, resourceId)) {
				printContents(child, repoId);
			}
			for (Relation1 relation : codeSyncElement.getRelations()) {
				printContents(relation, repoId);
			}
		}
	}
	
}
