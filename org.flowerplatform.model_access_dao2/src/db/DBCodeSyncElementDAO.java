package db;

import java.util.List;
import java.util.UUID;

import org.flowerplatform.model_access_dao2.CodeSyncElementDAO;
import org.flowerplatform.model_access_dao2.DAOFactory;
import org.flowerplatform.model_access_dao2.UUIDGenerator;
import org.flowerplatform.model_access_dao2.model.CodeSyncElement1;
import org.flowerplatform.model_access_dao2.model.ModelFactory;
import org.flowerplatform.model_access_dao2.registry.Design;

import com.datastax.driver.core.Row;

public class DBCodeSyncElementDAO implements CodeSyncElementDAO {

	@Override
	public UUID createCodeSyncElement(UUID designId, UUID resourceId, UUID parentId, UUID codeSyncElementId) {
		if (codeSyncElementId == null) {
			codeSyncElementId = UUIDGenerator.newUUID();
		}
		CassandraData.execute("INSERT INTO codeSyncElement (designId, resourceId, cseId, parentId) VALUES (?, ?, ?, ?)", 
				designId, resourceId, codeSyncElementId, parentId);
		return codeSyncElementId;
	}

	@Override
	public CodeSyncElement1 getCodeSyncElement(UUID designId, UUID resourceId, UUID codeSyncElementId) {
		List<Row> result = CassandraData.execute("SELECT * FROM codeSyncElement WHERE designId = ? AND resourceId = ? AND cseId = ?", 
				designId, resourceId, codeSyncElementId).all();
		if (result.size() == 0) {
			Design design = DAOFactory.getRegistryDAO().getDesign(designId);
			if (design.getRepoId() != null) {
				return getCodeSyncElement(design.getRepoId(), resourceId, codeSyncElementId);
			}
		}
		return toCodeSyncElement(result.get(0));
	}

	private CodeSyncElement1 toCodeSyncElement(Row row) {
		CodeSyncElement1 cse = ModelFactory.eINSTANCE.createCodeSyncElement1();
		cse.setId(row.getUUID("cseId").toString());
		cse.setName(row.getString("name"));
		return cse;
	}

	@Override
	public void updateCodeSyncElement(UUID designId, UUID resourceId, CodeSyncElement1 codeSyncElement) {
		UUID cseId = UUIDGenerator.fromString(codeSyncElement.getId());
		CassandraData.execute("UPDATE codeSyncElement SET name = ? WHERE designId = ? AND resourceId = ? AND cseId = ?", 
				codeSyncElement.getName(), designId, resourceId, cseId);
	}

	@Override
	public void saveCodeSyncElement(UUID designId, UUID resourceId, CodeSyncElement1 codeSyncElement) {
		CodeSyncElement1 parent = (CodeSyncElement1) codeSyncElement.eContainer();
		UUID parentId = parent == null ? null : UUIDGenerator.fromString(parent.getId());
		UUID cseId = UUIDGenerator.fromString(codeSyncElement.getId());
		createCodeSyncElement(designId, resourceId, parentId, cseId);
		updateCodeSyncElement(designId, resourceId, codeSyncElement);
	}

	@Override
	public void deleteCodeSyncElement(UUID designId, UUID resourceId, UUID codeSyncElementId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setParent(UUID designId, UUID resourceId, UUID parentId, CodeSyncElement1 child) {
		// TODO Auto-generated method stub
		
	}

}
