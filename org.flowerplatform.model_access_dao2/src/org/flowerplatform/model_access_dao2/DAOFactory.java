package org.flowerplatform.model_access_dao2;

import db.DBCodeSyncElementDAO;
import db.DBDiagramDAO;
import db.DBNodeDAO;
import db.DBRegistryDAO;
import emf.EMFCodeSyncElementDAO;
import emf.EMFDiagramDAO;
import emf.EMFNodeDAO;
import emf.EMFRegistryDAO;

public class DAOFactory {

	public static final int EMF = 1, DB = 2; 
	
	public static int mode;
	
	private static RegistryDAO emf_registryDao = new EMFRegistryDAO();
	private static NodeDAO emf_nodeDao = new EMFNodeDAO();
	private static DiagramDAO emf_diagramDao = new EMFDiagramDAO();
	private static CodeSyncElementDAO emf_cseDao = new EMFCodeSyncElementDAO();
	
	
	private static RegistryDAO db_registryDao = new DBRegistryDAO();
	private static NodeDAO db_nodeDao = new DBNodeDAO();
	private static DiagramDAO db_diagramDao = new DBDiagramDAO();
	private static CodeSyncElementDAO db_cseDao = new DBCodeSyncElementDAO();
	
	public static RegistryDAO getRegistryDAO() {
		switch (mode) {
		case 1: return emf_registryDao;
		case 2: return db_registryDao;
		default: return null;
		}
	}
	
	public static NodeDAO getNodeDAO() {
		switch (mode) {
		case 1: return emf_nodeDao;
		case 2: return db_nodeDao;
		default: return null;
		}
	}
	
	public static DiagramDAO getDiagramDAO() {
		switch (mode) {
		case 1: return emf_diagramDao;
		case 2: return db_diagramDao;
		default: return null;
		}
	}
	
	public static CodeSyncElementDAO getCodeSyncElementDAO() {
		switch (mode) {
		case 1: return emf_cseDao;
		case 2: return db_cseDao;
		default: return null;
		}
	}
	
}
