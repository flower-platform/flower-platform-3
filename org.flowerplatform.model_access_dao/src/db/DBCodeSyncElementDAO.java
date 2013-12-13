package db;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.model_access_dao.CodeSyncElementDAO;
import org.flowerplatform.model_access_dao.DAOFactory;
import org.flowerplatform.model_access_dao.UUIDGenerator;
import org.flowerplatform.model_access_dao.model.CodeSyncElement1;
import org.flowerplatform.model_access_dao.model.ModelFactory;
import org.flowerplatform.model_access_dao.model.Relation1;
import org.flowerplatform.model_access_dao.registry.Repository;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;

public class DBCodeSyncElementDAO implements CodeSyncElementDAO {

	@Override
	public UUID createCodeSyncElement(UUID repoId, UUID resourceId, UUID id, UUID parentId) {
		if (id == null) {
			id = org.flowerplatform.model_access_dao.UUIDGenerator.newUUID();
		}
		
//		Repository repo = DAOFactory.registryDAO.getRepository(repoId);
//		if (repo.getMasterId() != null) {
//			repoId = repo.getMasterId();
//		}
		
		System.out.println("> created CSE " + id);
		
		PreparedStatement stmt = CassandraData.getSession().prepare(
				"INSERT INTO cse (repoId, resourceId, cseId, parentId) VALUES (?, ?, ?, ?)");
		CassandraData.getSession().execute(stmt.bind(repoId, resourceId, id, parentId));
		
		return id;
	}

	@Override
	public CodeSyncElement1 getCodeSyncElement(UUID repoId, UUID resourceId, UUID id) {
		// TODO may replace with DD logic
//		Repository repo = DAOFactory.registryDAO.getRepository(repoId);
//		if (repo.getMasterId() != null) {
//			repoId = repo.getMasterId();
//		}
		
		// find the element in the resource
		Resource resource = DAOFactory.registryDAO.loadResource(repoId, resourceId);
		CodeSyncElement1 element = (CodeSyncElement1) resource.getEObject(id.toString());
		if (element != null) {
			return element; // TODO should we merge with the DB?
		}
		
		// get the element from the DB
		String query = "SELECT * FROM cse WHERE repoId = ? AND resourceId = ? AND cseId = ?";
		PreparedStatement stmt = CassandraData.getSession().prepare(query);
		List<Row> results = CassandraData.getSession().execute(stmt.bind(repoId, resourceId, id)).all();

		if (results.size() == 0) {
			return null;
		}
		
		if (results.size() > 1) {
			throw new RuntimeException("Multiple rows returned for query = " + query);
		}
		
		Row row = results.get(0);
		element = toCodeSyncElement(row);
		
		// recreate the element's parents structure
		UUID parentId = row.getUUID("parentId");
		if (parentId != null) { 
			CodeSyncElement1 parent = getCodeSyncElement(repoId, resourceId, parentId);
			parent.getChildren().add(element);
		} else {
			// this is the root element, simply add it to the resource contents
			resource.getContents().add(element);
		}
		
		return element;
	}
	
	@Override
	public void updateCodeSyncElement(UUID repoId, UUID resourceId,	CodeSyncElement1 element) {
//		Repository repo = DAOFactory.registryDAO.getRepository(repoId);
//		if (repo.getMasterId() != null) {
//			repoId = repo.getMasterId();
//		}
		
		PreparedStatement stmt = CassandraData.getSession().prepare(
				"UPDATE cse SET name = ? WHERE repoId = ? AND resourceId = ? AND cseId = ?");
		CassandraData.getSession().execute(stmt.bind(element.getName(), repoId, resourceId, UUID.fromString(element.getId())));
	}

	@Override
	public List<CodeSyncElement1> getCodeSyncElements(UUID repoId, UUID resourceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CodeSyncElement1> getChildren(CodeSyncElement1 element, UUID repoId, UUID resourceId) {
		PreparedStatement stmt = CassandraData.getSession().prepare(
				"SELECT * FROM cse WHERE repoId = ? AND resourceId = ? AND parentId = ?");
		List<CodeSyncElement1> children = new ArrayList<CodeSyncElement1>();
		for (Row row : CassandraData.getSession().execute(stmt.bind(repoId, resourceId, UUID.fromString(element.getId()))).all()) {
			children.add(toCodeSyncElement(row));
		}
		return children;
	}

	@Override
	public CodeSyncElement1 getParent(CodeSyncElement1 element, UUID repoId, UUID resourceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(CodeSyncElement1 parent, CodeSyncElement1 element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteCodeSyncElement(CodeSyncElement1 element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRelation(CodeSyncElement1 source, CodeSyncElement1 target, UUID repoId, UUID resourceId) {
		PreparedStatement stmt = CassandraData.getSession().prepare(
				"INSERT INTO relation (repoId, relationId, sourceId, targetId) VALUES (?, ?, ?, ?)");
		CassandraData.getSession().execute(stmt.bind(repoId, UUIDGenerator.newUUID(), UUID.fromString(source.getId()), UUID.fromString(target.getId())));
	}

	@Override
	public List<CodeSyncElement1> getReferencedElements(CodeSyncElement1 source, UUID repoId, UUID resourceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CodeSyncElement1 getSource(Relation1 relation, UUID repoId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CodeSyncElement1 getTarget(Relation1 relation, UUID repoId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private CodeSyncElement1 toCodeSyncElement(Row row) {
		CodeSyncElement1 element = ModelFactory.eINSTANCE.createCodeSyncElement1();
		element.setId(row.getUUID("cseId").toString());
		element.setName(row.getString("name"));
		return element;
	}

	@Override
	public void saveCodeSyncElement(UUID repoId, UUID resourceId, CodeSyncElement1 element) {
		UUID parentId = null;
		if (element.eContainer() != null) {
			CodeSyncElement1 parent = (CodeSyncElement1) element.eContainer();
			parentId = UUID.fromString(parent.getId());
		}
		UUID id = UUID.fromString(element.getId());
		createCodeSyncElement(repoId, resourceId, id, parentId);
		updateCodeSyncElement(repoId, resourceId, element);
	}

}
