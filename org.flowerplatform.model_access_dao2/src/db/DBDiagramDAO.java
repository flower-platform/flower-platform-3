package db;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.flowerplatform.model_access_dao2.DAOFactory;
import org.flowerplatform.model_access_dao2.DiagramDAO;
import org.flowerplatform.model_access_dao2.UUIDGenerator;
import org.flowerplatform.model_access_dao2.model.Diagram1;
import org.flowerplatform.model_access_dao2.model.ModelFactory;
import org.flowerplatform.model_access_dao2.model.Node1;
import org.flowerplatform.model_access_dao2.model.ResourceInfo;
import org.flowerplatform.model_access_dao2.registry.Design;
import org.flowerplatform.model_access_dao2.service.RegistryService;

import com.datastax.driver.core.Row;

public class DBDiagramDAO implements DiagramDAO {

	@Override
	public UUID createDiagram(UUID designId, UUID resourceId, UUID diagramId) {
		if (diagramId == null) {
			diagramId = UUIDGenerator.newUUID();
		}
		
		CassandraData.execute("INSERT INTO diagram (designId, resourceId, diagramId) VALUES (?, ?, ?)", 
				designId, resourceId, diagramId);
		
		return diagramId;
	}

	@Override
	public Diagram1 getDiagram(UUID designId, UUID resourceId, UUID diagramId) {
		List<Row> result = diagramId == null ?
				CassandraData.execute("SELECT * FROM diagram WHERE designId = ? AND resourceId = ?", 
						designId, resourceId).all()
				:
					CassandraData.execute("SELECT * FROM diagram WHERE designId = ? AND resourceId = ? AND diagramId = ?", 
				designId, resourceId, diagramId).all();
		if (result.size() == 0) {
			return null;
		}
		return toDiagram(result.get(0));
	}
	
	private Diagram1 toDiagram(Row row) {
		Diagram1 diagram = ModelFactory.eINSTANCE.createDiagram1();
		diagram.setId(row.getUUID("diagramId").toString());
		// TODO create the resource
		return diagram;
	}

	@Override
	public void updateDiagram(UUID designId, UUID resourceId, Diagram1 diagram) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveDiagram(UUID designId, UUID resourceId, Diagram1 diagram) {
		createDiagram(designId, resourceId, UUIDGenerator.fromString(diagram.getId()));
		updateDiagram(designId, resourceId, diagram);
	}

	@Override
	public void deleteDiagram(UUID designId, UUID resourceId, UUID diagramId) {
		// TODO Auto-generated method stub

	}

	@Override
	public Diagram1 getDiagram(UUID designId, String path) {
		Design design = DAOFactory.getRegistryDAO().getDesign(designId);
		UUID resourceId = design.getResourceUUIDForPath(path);
//		if (resourceId == null) {
//			resourceId = importFromDisk(design, path);
//		}
		
		return getDiagram(designId, resourceId, null);
	}

	private UUID importFromDisk(Design design, String path) {
		File file = RegistryService.INSTANCE.getResourceFile(design, path);
		ResourceSet resourceSet = RegistryService.INSTANCE.createResourceSet();
		Resource resource = resourceSet.getResource(
				RegistryService.INSTANCE.getPlatformDependentURI(file.getPath()), true);
		ResourceInfo info = (ResourceInfo) resource.getContents().get(0);
		UUID resourceId = UUIDGenerator.fromString(info.getResourceId());
		UUID designId = design.getDesignId();
		DAOFactory.getRegistryDAO().createResource(path, designId, resourceId);
		Diagram1 diagram = (Diagram1) resource.getContents().get(1);
		saveDiagram(designId, resourceId, diagram);
		importNodes(designId, resourceId, diagram);
		return resourceId;
	}
	
	private void importNodes(UUID designId, UUID resourceId, Node1 parent) {
		for (Node1 child : parent.getChildren()) {
			// save only child nodes, because the diagram was saved before
			EObject diagrammableElement = child.getDiagrammableElement();
			if (diagrammableElement.eIsProxy()) {
				resolveDiagrammableElement(diagrammableElement);
				
			}
			DAOFactory.getNodeDAO().saveNode(designId, resourceId, child);
			importNodes(designId, resourceId, child);
		}
	}
	
	private void resolveDiagrammableElement(EObject diagrammableElement) {
		URI uri = ((InternalEObject) diagrammableElement).eProxyURI();
		String diagrammableElementId = uri.fragment();
		String mappingId = uri.opaquePart();
		return;
	}

}
