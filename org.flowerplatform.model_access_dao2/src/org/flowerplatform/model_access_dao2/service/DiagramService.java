package org.flowerplatform.model_access_dao2.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.flowerplatform.model_access_dao2.DAOFactory;
import org.flowerplatform.model_access_dao2.UUIDGenerator;
import org.flowerplatform.model_access_dao2.model.CodeSyncElement1;
import org.flowerplatform.model_access_dao2.model.Diagram1;
import org.flowerplatform.model_access_dao2.model.Node1;
import org.flowerplatform.model_access_dao2.registry.Design;

public class DiagramService {

	/**
	 * Will create the diagram at a path relative to the repository
	 * where this design belongs.
	 */
	public UUID createDiagram(String path, UUID designId) {
		Design design = DAOFactory.getRegistryDAO().getDesign(designId);
		UUID resourceId = DAOFactory.getRegistryDAO().createResource(path, design.getRepoId(), null);
		DAOFactory.getDiagramDAO().createDiagram(design.getRepoId(), resourceId, null);
		DAOFactory.getRegistryDAO().saveResource(design.getRepoId(), resourceId);
		return resourceId;
	}
	
	public List<Node1> openDiagram(String path, UUID designId) {
		List<Node1> result = new ArrayList<Node1>();
		Diagram1 diagram = DAOFactory.getDiagramDAO().getDiagram(designId, path);
		Design design = DAOFactory.getRegistryDAO().getDesign(designId);
		UUID resourceId = design.getResourceUUIDForPath(path);
		getChildrenNodes(designId, resourceId, diagram, result);
		return result;
	}
	
	protected void copyDiagram() {
		
	}
	
	protected void getChildrenNodes(UUID designId, UUID resourceId, Node1 parent, List<Node1> nodes) {
		nodes.add(parent);
		UUID parentId = UUIDGenerator.fromString(parent.getId());
		for (Node1 child : DAOFactory.getNodeDAO().getChildren(designId, resourceId, parentId)) {
			getChildrenNodes(designId, resourceId, child, nodes);
		}
	}
	
	public void updateFeatureValue(UUID designId, UUID resourceId, UUID cseId, String feature, String value) {
		CodeSyncElement1 codeSyncElement = DAOFactory.getCodeSyncElementDAO().getCodeSyncElement(designId, resourceId, cseId);
		codeSyncElement.setName(codeSyncElement.getName() + "_MODIF_FROM_DGR");
		DAOFactory.getCodeSyncElementDAO().updateCodeSyncElement(designId, resourceId, codeSyncElement);
	}
	
}
