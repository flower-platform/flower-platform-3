package tests;

import static tests.ModelAccessDAOTests.printContents;

import java.util.Map.Entry;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.model_access_dao.DAOFactory;
import org.flowerplatform.model_access_dao.registry.DirWithResources;
import org.flowerplatform.model_access_dao.registry.DiscussableDesign;
import org.junit.Test;

public class OpenDiscussableDesignDiagram {

	@Test
	public void test() {
		System.out.println("OPEN DISCUSSABLE DESIGN DIAGRAM");
		
		String repoId = (String) DAOFactory.registryDAO.getRepositories().get(0).getId();
		String discussableDesignId = (String) DAOFactory.registryDAO.getRepository(repoId).getDiscussableDesigns().keySet().toArray()[0];
		
		DiscussableDesign discussableDesign = DAOFactory.registryDAO.getDiscussableDesign(repoId, discussableDesignId);
		String resourceId = getDiagramResourceId(discussableDesign);
		Resource resource = DAOFactory.registryDAO.loadResource(repoId, discussableDesignId, resourceId);
		
		printContents(resource, repoId, discussableDesignId);
	}
	
	public static String getDiagramResourceId(DirWithResources dirWithResources) {
		for (Entry<String, URI> entry : dirWithResources.getResources().entrySet()) {
			if (entry.getValue().toFileString().endsWith("dgr.notation")) {
				return entry.getKey();
			}
		}
		throw new RuntimeException("No diagram for " + dirWithResources.getDir());
	}

}
