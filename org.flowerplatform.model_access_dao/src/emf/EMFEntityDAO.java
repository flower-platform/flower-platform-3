package emf;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.model_access_dao.EntityDAO;
import org.flowerplatform.model_access_dao.model.CodeSyncElement1;
import org.flowerplatform.model_access_dao.model.EntityEMF;
import org.flowerplatform.model_access_dao.model.ModelFactory;

public class EMFEntityDAO extends EMFCodeSyncElementDAO implements EntityDAO {

	@Override
	public List<CodeSyncElement1> getReferencedElements(CodeSyncElement1 entity, 
			String repoId, String discussableDesignId, String resourceId) {
		Map<String, CodeSyncElement1> referencedElements = new HashMap<String, CodeSyncElement1>();
		
		if (discussableDesignId != null) {
			entity = getCodeSyncElement(repoId, null, resourceId, entity.getId());
			for (CodeSyncElement1 refElt : getReferencedElements(entity, repoId, null, resourceId)) {
				referencedElements.put(refElt.getId(), refElt);
			}
		}
		
		for (CodeSyncElement1 refElt : getEntity(entity).getReferencedElements()) {
			referencedElements.put(refElt.getId(), refElt);
		}
		
		return Arrays.asList(referencedElements.values().toArray(new CodeSyncElement1[0]));
	}

	@Override
	public void addReferencedElement(CodeSyncElement1 entity, CodeSyncElement1 element) {
		getEntity(entity).getReferencedElements().add(element);
	}
	
	protected EntityEMF getEntity(CodeSyncElement1 entity) {
		return (EntityEMF) entity;
	}

	@Override
	protected CodeSyncElement1 createElement() {
		return ModelFactory.eINSTANCE.createEntityEMF();
	}

}
