package db;

import java.util.UUID;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.flowerplatform.model_access_dao2.DAOFactory;
import org.flowerplatform.model_access_dao2.model.CodeSyncElement1;
import org.flowerplatform.model_access_dao2.model.Diagram1;
import org.flowerplatform.model_access_dao2.model.Node1;

public class EObjectDAO {
	
	public static EObjectDAO INSTANCE = new EObjectDAO();
	
	private EObjectDAO() {
	}

	public void saveEObject(UUID designId, UUID resourceId, EObject eObject) {
		if (eObject instanceof Diagram1) {
			DAOFactory.getDiagramDAO().saveDiagram(designId, resourceId, (Diagram1) eObject);
		} else if (eObject instanceof Node1) {
			DAOFactory.getNodeDAO().saveNode(designId, resourceId, (Node1) eObject);
		} else if (eObject instanceof CodeSyncElement1) {
			DAOFactory.getCodeSyncElementDAO().saveCodeSyncElement(designId, resourceId, (CodeSyncElement1) eObject);
		}
	}

	public EList<? extends EObject> getChildren(EObject eObject) {
		if (eObject instanceof Diagram1) {
			return ((Diagram1) eObject).getChildren();
		} 
		if (eObject instanceof Node1) {
			return ((Node1) eObject).getChildren();
		} 
		if (eObject instanceof CodeSyncElement1) {
			return ((CodeSyncElement1) eObject).getChildren();
		}
		return null;
	}

}
