package org.flowerplatform.model_access_dao;

import db.DBCodeSyncElementDAO;
import db.DBNodeDAO;
import db.DBRegistryDAO;
import emf.EMFCodeSyncElementDAO;
import emf.EMFNodeDAO;
import emf.EMFRegistryDAO;

public abstract class DAOFactory {
	
	public static DAOFactory emf = new DAOFactory() {

		@Override
		public RegistryDAO getRegistryDao() {
			return new EMFRegistryDAO();
		}

		@Override
		public CodeSyncElementDAO getCSEDAO() {
			return new EMFCodeSyncElementDAO();
		}

		@Override
		public NodeDAO getNodeDAO() {
			return new EMFNodeDAO();
		}
	};
	
	public static DAOFactory db = new DAOFactory() {

		@Override
		public RegistryDAO getRegistryDao() {
			return new DBRegistryDAO();
		}

		@Override
		public CodeSyncElementDAO getCSEDAO() {
			return new DBCodeSyncElementDAO();
		}

		@Override
		public NodeDAO getNodeDAO() {
			return new DBNodeDAO();
		} 
	};
	
	public abstract RegistryDAO getRegistryDao();
	public abstract CodeSyncElementDAO getCSEDAO();
	public abstract NodeDAO getNodeDAO();
	
	public static DAOFactory internal = emf;
	
	public static RegistryDAO registryDAO = internal.getRegistryDao();

	public static CodeSyncElementDAO codeSyncElementDAO = internal.getCSEDAO();
	
	public static NodeDAO nodeDAO = internal.getNodeDAO();
	
}
