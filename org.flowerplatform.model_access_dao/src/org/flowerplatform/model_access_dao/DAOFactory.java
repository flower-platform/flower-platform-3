package org.flowerplatform.model_access_dao;


import emf.EMFCodeSyncElementDAO;
import emf.EMFNodeDAO;
import emf.EMFRegistryDAO;


public class DAOFactory {
	
	public static RegistryDAO registryDAO = new EMFRegistryDAO();

	public static CodeSyncElementDAO codeSyncElementDAO = new EMFCodeSyncElementDAO();
	
	public static NodeDAO nodeDAO = new EMFNodeDAO();
	
}
