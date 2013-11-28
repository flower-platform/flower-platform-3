package db;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.model_access_dao.CodeSyncElementDAO;
import org.flowerplatform.model_access_dao.DAOFactory;
import org.flowerplatform.model_access_dao.UUID;
import org.flowerplatform.model_access_dao.model.CodeSyncElement1;
import org.flowerplatform.model_access_dao.model.ModelFactory;
import org.flowerplatform.model_access_dao.model.Relation1;
import org.flowerplatform.model_access_dao.registry.Repository;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;

public class DBCodeSyncElementDAO implements CodeSyncElementDAO {

	@Override
	public String createCodeSyncElement(String repoId, String resourceId, String id, String parentId) {
		if (id == null) {
			id = UUID.newUUID();
		}
		
		Repository repo = DAOFactory.registryDAO.getRepository(repoId);
		if (repo.getMasterId() != null) {
			repoId = repo.getMasterId();
		}
		
		System.out.println("> created CSE " + id);
		
		PreparedStatement stmt = CassandraData.getSession().prepare(
				"INSERT INTO cse (repoId, cseId, parentId) VALUES (?, ?, ?)");
		CassandraData.getSession().execute(stmt.bind(repoId, id, parentId));
		
		return id;
	}

	@Override
	public CodeSyncElement1 getCodeSyncElement(String repoId, String resourceId, String id) {
		// TODO may replace with DD logic
		Repository repo = DAOFactory.registryDAO.getRepository(repoId);
		if (repo.getMasterId() != null) {
			repoId = repo.getMasterId();
		}
		
		// find the element in the resource
		Resource resource = DAOFactory.registryDAO.loadResource(repoId, resourceId);
		CodeSyncElement1 element = (CodeSyncElement1) resource.getEObject(id);
		if (element != null) {
			return element; // TODO should we merge with the DB?
		}
		
		// get the element from the DB
		String query = "SELECT * FROM cse WHERE repoId = ? AND cseId = ?";
		PreparedStatement stmt = CassandraData.getSession().prepare(query);
		List<Row> results = CassandraData.getSession().execute(stmt.bind(repoId, id)).all();

		if (results.size() == 0) {
			return null;
		}
		
		if (results.size() > 1) {
			throw new RuntimeException("Multiple rows returned for query = " + query);
		}
		
		Row row = results.get(0);
		element = toCodeSyncElement(row);
		
		// recreate the element's parents structure
		String parentId = row.getString("parentId");
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
	public void updateCodeSyncElement(String repoId, String resourceId,	CodeSyncElement1 element) {
		PreparedStatement stmt = CassandraData.getSession().prepare(
				"UPDATE cse SET name = ? WHERE repoId = ? AND cseId = ?");
		CassandraData.getSession().execute(stmt.bind(element.getName(), repoId, element.getId()));
	}

	@Override
	public List<CodeSyncElement1> getCodeSyncElements(String repoId, String resourceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CodeSyncElement1> getChildren(CodeSyncElement1 element, String repoId, String resourceId) {
		PreparedStatement stmt = CassandraData.getSession().prepare(
				"SELECT * FROM cse WHERE repoId = ? AND parentId = ?");
		List<CodeSyncElement1> children = new ArrayList<CodeSyncElement1>();
		for (Row row : CassandraData.getSession().execute(stmt.bind(repoId, element.getId())).all()) {
			children.add(toCodeSyncElement(row));
		}
		return children;
	}

	@Override
	public CodeSyncElement1 getParent(CodeSyncElement1 element, String repoId, String resourceId) {
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
	public void addRelation(CodeSyncElement1 source, CodeSyncElement1 target, String repoId, String resourceId) {
		PreparedStatement stmt = CassandraData.getSession().prepare(
				"INSERT INTO relation (repoId, relationId, sourceId, targetId) VALUES (?, ?, ?, ?)");
		CassandraData.getSession().execute(stmt.bind(repoId, UUID.newUUID(), source.getId(), target.getId()));
	}

	@Override
	public List<CodeSyncElement1> getReferencedElements(CodeSyncElement1 source, String repoId, String resourceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CodeSyncElement1 getSource(Relation1 relation, String repoId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CodeSyncElement1 getTarget(Relation1 relation, String repoId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private CodeSyncElement1 toCodeSyncElement(Row row) {
		CodeSyncElement1 element = ModelFactory.eINSTANCE.createCodeSyncElement1();
		element.setId(row.getString("cseId"));
		element.setName(row.getString("name"));
		return element;
	}

}
