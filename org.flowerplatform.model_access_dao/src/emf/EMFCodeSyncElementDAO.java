package emf;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.flowerplatform.model_access_dao.CodeSyncElementDAO;
import org.flowerplatform.model_access_dao.DAOFactory;
import org.flowerplatform.model_access_dao.UUIDGenerator;
import org.flowerplatform.model_access_dao.model.CodeSyncElement1;
import org.flowerplatform.model_access_dao.model.ModelFactory;
import org.flowerplatform.model_access_dao.model.Relation1;

public class EMFCodeSyncElementDAO implements CodeSyncElementDAO {

	@Override
	public UUID createCodeSyncElement(UUID repoId, UUID resourceId, UUID id, UUID parentId) {
		CodeSyncElement1 element = ModelFactory.eINSTANCE.createCodeSyncElement1();
		if (id == null) {
			id = UUIDGenerator.newUUID();
		}
		element.setId(id.toString());
		
		System.out.println("> created CSE " + element.getId());
		
		if (parentId == null) {
			Resource resource = DAOFactory.registryDAO.loadResource(repoId, resourceId);
			resource.getContents().add(element);
		} else {
			CodeSyncElement1 parent = getCodeSyncElement(repoId, resourceId, parentId);
			setParent(parent, element);
		}
		
		return UUID.fromString(element.getId());
	}

	@Override
	public CodeSyncElement1 getCodeSyncElement(UUID repoId, UUID resourceId, UUID uid) {
		Resource resource = DAOFactory.registryDAO.loadResource(repoId, resourceId);

		// search for the referenced object in this resource
		CodeSyncElement1 referencedObject = null;
		if (resource != null) {
			referencedObject = (CodeSyncElement1) resource.getEObject(uid.toString());
			if (referencedObject != null) {
				return (CodeSyncElement1) referencedObject;
			}
		}
		
		UUID masterRepoId = DAOFactory.registryDAO.getMasterRepositoryId(repoId);
		if (masterRepoId == null) {
			return null;
		}

		// no slave resource, or the referenced object is not in this resource
		// => search in the master resource
		Resource masterResource = DAOFactory.registryDAO.loadResource(masterRepoId, resourceId);
		
		if (masterResource == null) {
			throw new RuntimeException("No global resource for repo " + masterRepoId);
		}
		
		referencedObject = (CodeSyncElement1) masterResource.getEObject(uid.toString());

		// duplicate the object from the master resource
		if (resource != null) {
			referencedObject = duplicateCodeSyncElement(repoId, resourceId, referencedObject);
		}
		return (CodeSyncElement1) referencedObject;
	}

	private CodeSyncElement1 duplicateCodeSyncElement(UUID repoId, UUID resourceId, CodeSyncElement1 referencedObject) {
		CodeSyncElement1 parent = getParent(referencedObject, repoId, resourceId);
		UUID parentId = parent == null ? null : UUID.fromString(parent.getId());
		CodeSyncElement1 duplicate = getCodeSyncElement(repoId, resourceId, 
				createCodeSyncElement(repoId, resourceId, UUID.fromString(referencedObject.getId()), parentId));
		copyProperties(referencedObject, duplicate);
		return duplicate;
	}
	
	protected void copyProperties(CodeSyncElement1 source, CodeSyncElement1 target) {
		target.setName(source.getName());
	}
	
	public List<CodeSyncElement1> getCodeSyncElements(UUID repoId, UUID resourceId) {
		// use LinkedHashMap to preserve insertion-order
		Map<String, CodeSyncElement1> codeSyncElements = new LinkedHashMap<String, CodeSyncElement1>();
		
		// merge with master resource if we're in a slave repo
		UUID masterRepoId = DAOFactory.registryDAO.getMasterRepositoryId(repoId);
		if (masterRepoId != null) {
			for (CodeSyncElement1 codeSyncElement : getCodeSyncElements(masterRepoId, resourceId)) {
				codeSyncElements.put(codeSyncElement.getId(), codeSyncElement);
			}
		}
		
		Resource resource = DAOFactory.registryDAO.loadResource(repoId, resourceId);
		for (EObject eObject : resource.getContents()) {
			if (eObject instanceof CodeSyncElement1) {
				addToMap((CodeSyncElement1) eObject, codeSyncElements);
			}
		}
		
		return Arrays.asList(codeSyncElements.values().toArray(new CodeSyncElement1[0]));
	}
	
	private void addToMap(CodeSyncElement1 codeSyncElement, Map<String, CodeSyncElement1> codeSyncElements) {
		codeSyncElements.put(codeSyncElement.getId(), codeSyncElement);
		for (CodeSyncElement1 child : codeSyncElement.getChildren()) {
			addToMap(child, codeSyncElements);
		}
	}
	
	@Override
	public List<CodeSyncElement1> getChildren(CodeSyncElement1 element, UUID repoId, UUID resourceId) {
		// use LinkedHashMap to preserve insertion-order
		Map<String, CodeSyncElement1> children = new LinkedHashMap<String, CodeSyncElement1>();
		
		// merge with master resource if we're in a slave repo
		UUID masterRepoId = DAOFactory.registryDAO.getMasterRepositoryId(repoId);
		if (masterRepoId != null) {
			CodeSyncElement1 masterElement = getCodeSyncElement(masterRepoId, resourceId, UUID.fromString(element.getId()));
			if (masterElement != null) {
				for (CodeSyncElement1 child : getChildren(masterElement, masterRepoId, resourceId)) {
					children.put(child.getId(), child);
				}
			}
		}
		
		for (CodeSyncElement1 child : element.getChildren()) {
			children.put(child.getId(), child);
		}
		
		return Arrays.asList(children.values().toArray(new CodeSyncElement1[0]));
	}

	@Override
	public CodeSyncElement1 getParent(CodeSyncElement1 element, UUID repoId, UUID resourceId) {
		CodeSyncElement1 parent = (CodeSyncElement1) element.eContainer();
		return parent == null ? null : getCodeSyncElement(repoId, resourceId, UUID.fromString(parent.getId()));
	}

	@Override
	public void setParent(CodeSyncElement1 parent, CodeSyncElement1 element) {
		if (parent != null) {
			parent.getChildren().add(element);
		} else {
			parent = (CodeSyncElement1) element.eContainer();
			if (parent == null) {
				// remove from resource TODO
			} else {
				parent.getChildren().remove(element);
			}
		}
	}
	
	@Override
	public void deleteCodeSyncElement(CodeSyncElement1 element) {
		ECrossReferenceAdapter adapter = ECrossReferenceAdapter.getCrossReferenceAdapter(element);
		if (adapter.getNonNavigableInverseReferences(element).size() == 0) {
			setParent(null, element);
		}
	}
	
	@Override
	public void addRelation(CodeSyncElement1 source, CodeSyncElement1 target, 
			UUID repoId, UUID resourceId) {
		Relation1 relation = ModelFactory.eINSTANCE.createRelation1();
		relation.setSource(source);
		relation.setTarget(target);
		source.getRelations().add(relation);
	}

	@Override
	public List<CodeSyncElement1> getReferencedElements(CodeSyncElement1 source, UUID repoId, UUID resourceId) {
		Map<String, CodeSyncElement1> referencedElements = new LinkedHashMap<String, CodeSyncElement1>();

		UUID masterRepoId = DAOFactory.registryDAO.getMasterRepositoryId(repoId);
		if (masterRepoId != null) {
			CodeSyncElement1 globalSource = getCodeSyncElement(masterRepoId, resourceId, UUID.fromString(source.getId()));
			for (CodeSyncElement1 refElt : getReferencedElements(globalSource, masterRepoId, resourceId)) {
				referencedElements.put(refElt.getId(), refElt);
			}
		}

		for (Relation1 relation : source.getRelations()) {
			referencedElements.put(relation.getTarget().getId(), relation.getTarget());
		}

		return Arrays.asList(referencedElements.values().toArray(new CodeSyncElement1[0]));
	}

	@Override
	public CodeSyncElement1 getSource(Relation1 relation, UUID repoId) {
		return resolveProxy(relation.getSource(), repoId);
	}

	@Override
	public CodeSyncElement1 getTarget(Relation1 relation, UUID repoId) {
		return resolveProxy(relation.getTarget(), repoId);
	}
	
	protected CodeSyncElement1 resolveProxy(CodeSyncElement1 element, UUID repoId) {
		if (element.eIsProxy()) {
			URI uri = ((InternalEObject) element).eProxyURI();
			UUID uid = UUID.fromString(uri.fragment());
			UUID resourceId = UUID.fromString(uri.opaquePart());
			element = getCodeSyncElement(repoId, resourceId, uid);
		}
		return element;
	}

	@Override
	public void updateCodeSyncElement(UUID repoId, UUID resourceId, CodeSyncElement1 element) {
		// nothing to do
	}

	@Override
	public void saveCodeSyncElement(UUID repoId, UUID resourceId, CodeSyncElement1 element) {
		// TODO Auto-generated method stub
		
	}
	
}
