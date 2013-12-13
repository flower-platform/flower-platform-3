package emf;

import java.util.UUID;

import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.model_access_dao2.CodeSyncElementDAO;
import org.flowerplatform.model_access_dao2.DAOFactory;
import org.flowerplatform.model_access_dao2.UUIDGenerator;
import org.flowerplatform.model_access_dao2.model.CodeSyncElement1;
import org.flowerplatform.model_access_dao2.model.ModelFactory;

public class EMFCodeSyncElementDAO implements CodeSyncElementDAO {

	@Override
	public UUID createCodeSyncElement(UUID designId, UUID resourceId, UUID parentId,
			UUID codeSyncElementId) {
		CodeSyncElement1 element = ModelFactory.eINSTANCE.createCodeSyncElement1();
		if (codeSyncElementId == null) {
			codeSyncElementId = UUIDGenerator.newUUID();
		}
		element.setId(codeSyncElementId.toString());
		
		if (parentId == null) {
			Resource resource = DAOFactory.getRegistryDAO().loadResource(designId, resourceId);
			resource.getContents().add(element);
		} else {
			setParent(designId, resourceId, parentId, element);
		}
		
		return UUIDGenerator.fromString(element.getId());
	}

	@Override
	public CodeSyncElement1 getCodeSyncElement(UUID designId, UUID resourceId,
			UUID codeSyncElementId) {
		Resource resource = DAOFactory.getRegistryDAO().loadResource(designId, resourceId);
		return (CodeSyncElement1) resource.getEObject(codeSyncElementId.toString());
	}

	@Override
	public void updateCodeSyncElement(UUID designId, UUID resourceId,
			CodeSyncElement1 codeSyncElement) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveCodeSyncElement(UUID designId, UUID resourceId,
			CodeSyncElement1 codeSyncElement) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteCodeSyncElement(UUID designId, UUID resourceId,
			UUID codeSyncElementId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setParent(UUID designId, UUID resourceId, UUID parentId,
			CodeSyncElement1 child) {
		CodeSyncElement1 parent = getCodeSyncElement(designId, resourceId, parentId);
		parent.getChildren().add(child);
	}

}
