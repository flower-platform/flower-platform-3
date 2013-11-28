package org.flowerplatform.model_access_dao;

import db.DBCodeSyncElementDAO;
import db.DBNodeDAO;
import db.DBRegistryDAO;
import emf.EMFCodeSyncElementDAO;
import emf.EMFNodeDAO;
import emf.EMFRegistryDAO;

public class DAOFactory {
	
	public static RegistryDAO registryDAO = 
			new DBRegistryDAO(); 
//			new EMFRegistryDAO();

	public static CodeSyncElementDAO codeSyncElementDAO = 
			new DBCodeSyncElementDAO();
//			new EMFCodeSyncElementDAO();
	
	public static NodeDAO nodeDAO = 
			new DBNodeDAO(); 
//			new EMFNodeDAO();
	
}
