package tests;

import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

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
import org.flowerplatform.model_access_dao.model.ResourceInfo;
import org.flowerplatform.model_access_dao.registry.DiscussableDesign;
import org.flowerplatform.model_access_dao.registry.Repository;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	CreateGlobalModel.class,
	CreateDiscussableDesign.class,
	OpenDiscussableDesignDiagram.class,
	MoveAndOpenDiscussableDesignDiagram.class
})
public class ModelAccessDAOTests {

	public static CodeSyncElement1 createCSEAndAsserNotNull(String repoId, String discussableDesignId, String resourceId, String id, String parentId) {
		id = DAOFactory.codeSyncElementDAO.createCodeSyncElement(repoId, null, resourceId, id, parentId);
		CodeSyncElement1 cse = DAOFactory.codeSyncElementDAO.getCodeSyncElement(repoId, null, resourceId, id);
		assertNotNull(cse);
		return cse;
	}

	public static void assertCSE(CodeSyncElement1 cse, String id, String name) {
		assertEquals(id, cse.getId());
		assertEquals(name, cse.getName());
	}
	
	public static void assertResourceInfo(Resource resource, String repoId, String discussableDesignId, String resourceId) {
		ResourceInfo info = (ResourceInfo) resource.getContents().get(0);
		assertEquals("Wrong repo id", repoId, info.getRepoId());
		assertEquals("Wrong discussable design id", discussableDesignId, info.getDiscussableDesignId());
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
	
	public static void printContents(Resource resource, String repoId, String ddId) {
		for (EObject object : resource.getContents()) {
			printContents(object, repoId, ddId);
		}
	}
	
	private static void printContents(EObject object, String repoId, String discussableDesignId) {
		if (object instanceof Diagram1) {
			Diagram1 dgr = (Diagram1) object;
			for (EObject child : dgr.getChildren()) {
				printContents(child, repoId, discussableDesignId);
			}
		} else if (object instanceof Node1) {
			Node1 node = (Node1) object;
			CodeSyncElement1 cse = DAOFactory.nodeDAO.getDiagrammableElement(node, repoId, discussableDesignId);
			if (cse.eIsProxy()) {
				fail("Diagrammable element is proxy");
			}
			System.out.println(String.format("NODE [name = %s] -> CSE [name = %s]", 
					node.getName(), cse.getName()));
			for (EObject child : DAOFactory.nodeDAO.getChildren(node, repoId, discussableDesignId, null)) {
				printContents(child, repoId, discussableDesignId);
			}
		} else if (object instanceof ResourceInfo) {
			ResourceInfo info = (ResourceInfo) object;
			Repository repo = DAOFactory.registryDAO.getRepository(info.getRepoId());
			File repoFile = repo.getDir();
			DiscussableDesign dd = repo.getDiscussableDesigns().get(info.getDiscussableDesignId());
			File ddFile = dd == null ? null : dd.getDir();
			URI resourceUri = dd == null ? repo.getResources().get(info.getResourceId()) : dd.getResources().get(info.getResourceId());
			System.out.println(String.format("RESOURCE INFO [repo = %s, dd = %s, uri = %s]", repoFile, ddFile, resourceUri));
		} else if (object instanceof CodeSyncElement1) {
			CodeSyncElement1 codeSyncElement = (CodeSyncElement1) object;
			System.out.println(String.format("CSE [name = %s]", codeSyncElement.getName()));
		}
	}
	
}
