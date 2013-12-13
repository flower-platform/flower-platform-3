package emf;

import java.util.UUID;

import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.model_access_dao2.DAOFactory;
import org.flowerplatform.model_access_dao2.DiagramDAO;
import org.flowerplatform.model_access_dao2.UUIDGenerator;
import org.flowerplatform.model_access_dao2.model.Diagram1;
import org.flowerplatform.model_access_dao2.model.ModelFactory;
import org.flowerplatform.model_access_dao2.registry.Design;

public class EMFDiagramDAO implements DiagramDAO {

	@Override
	public UUID createDiagram(UUID designId, UUID resourceId, UUID diagramId) {
		if (diagramId == null) {
			diagramId = UUIDGenerator.newUUID();
		}
		
		Diagram1 diagram = ModelFactory.eINSTANCE.createDiagram1();
		diagram.setId(diagramId.toString());
		
		Resource resource = DAOFactory.getRegistryDAO().loadResource(designId, resourceId);
		resource.getContents().add(diagram);
		
		return diagramId;
	}

	@Override
	public Diagram1 getDiagram(UUID designId, UUID resourceId, UUID diagramId) {
		Resource resource = DAOFactory.getRegistryDAO().loadResource(designId, resourceId);
		return (Diagram1) resource.getEObject(diagramId.toString());
	}

	@Override
	public void updateDiagram(UUID designId, UUID resourceId, Diagram1 diagram) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveDiagram(UUID designId, UUID resourceId, Diagram1 diagram) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteDiagram(UUID designId, UUID resourceId, UUID diagramId) {
		// TODO Auto-generated method stub

	}

	@Override
	public Diagram1 getDiagram(UUID designId, String path) {
		Design design = DAOFactory.getRegistryDAO().getDesign(designId);
		UUID resourceId = design.getResourceUUIDForPath(path);
		if (resourceId == null) {
			UUID repoId = design.getRepoId();
			if (repoId != null) {
				return getDiagram(repoId, path);
			}
		}
		if (resourceId == null) {
			throw new RuntimeException("No resource at path " + path);
		}
		Resource resource = DAOFactory.getRegistryDAO().loadResource(designId, resourceId);
		return (Diagram1) resource.getContents().get(1);
	}

}
