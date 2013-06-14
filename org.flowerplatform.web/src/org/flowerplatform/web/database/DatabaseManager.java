package org.flowerplatform.web.database;

import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Properties;

import javax.security.auth.Subject;

import org.eclipse.emf.teneo.PersistenceOptions;
import org.eclipse.emf.teneo.hibernate.HbDataStore;
import org.hibernate.Session;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.PostgresPlusDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.flowerplatform.web.FlowerWebProperties;
import org.flowerplatform.web.FlowerWebProperties.AddBooleanProperty;
import org.flowerplatform.web.FlowerWebProperties.AddProperty;
import org.flowerplatform.web.WebPlugin;
import org.flowerplatform.web.entity.DBVersion;
import org.flowerplatform.web.entity.EntityFactory;
import org.flowerplatform.web.entity.EntityPackage;
import org.flowerplatform.web.entity.Group;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.OrganizationMembershipStatus;
import org.flowerplatform.web.entity.OrganizationUser;
import org.flowerplatform.web.entity.User;
import org.flowerplatform.web.entity.dao.Dao;
import org.flowerplatform.web.security.dto.OrganizationAdminUIDto;
import org.flowerplatform.web.security.permission.AdminSecurityEntitiesPermission;
import org.flowerplatform.web.security.permission.FlowerWebFilePermission;
import org.flowerplatform.web.security.permission.ModifyTreePermissionsPermission;
import org.flowerplatform.web.security.sandbox.FlowerWebPrincipal;
import org.flowerplatform.web.security.service.OrganizationService;
import org.flowerplatform.web.security.service.UserService;
import org.flowerplatform.web.temp.GeneralService;

public class DatabaseManager {

	private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
	
	private static final String PROP_DB_PERSISTENCE_UNIT = "database.persistence-unit";
	
	private static final String PROP_DEFAULT_PROD_PERSISTENCE_UNIT = "production";
	
	private static final String PROP_DB_INIT_WITH_TEST_DATA = "database.init-with-test-data";
	
	private static final String PROP_DB_INIT_WITH_TEST_DATA_INTERNAL = "internal";
	
	public DatabaseManager() {
		// adding properties
		WebPlugin.getInstance().getFlowerWebProperties().addProperty(new AddProperty(PROP_DB_PERSISTENCE_UNIT, PROP_DEFAULT_PROD_PERSISTENCE_UNIT) {
			@Override
			protected String validateProperty(String input) {
				// nothing to do; if the persistence unit is not found, an exception will be
				// thrown in the code below
				return null;
			}
		});
		WebPlugin.getInstance().getFlowerWebProperties().addProperty(new AddBooleanProperty(PROP_DB_INIT_WITH_TEST_DATA, "false") {

			@Override
			protected String validateProperty(String input) {
				if (PROP_DB_INIT_WITH_TEST_DATA_INTERNAL.equals(input)) {
					return null;
				} else {
					return super.validateProperty(input);
				}
			}
			
		});
		
		Properties props = new Properties();
		props.setProperty(Environment.DRIVER, "org.postgresql.Driver");
		props.setProperty(Environment.USER, "postgres");
		props.setProperty(Environment.URL, "jdbc:postgresql://localhost/flower-dev-center");
		props.setProperty(Environment.PASS, "postgres");
		props.setProperty(Environment.DIALECT, PostgresPlusDialect.class.getName());
		props.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
		props.setProperty(Environment.HBM2DDL_AUTO, "create-drop");
		props.setProperty(Environment.SHOW_SQL, "true");
		
		props.setProperty(PersistenceOptions.CASCADE_POLICY_ON_NON_CONTAINMENT, "REFRESH,PERSIST,MERGE");
		props.setProperty(PersistenceOptions.PERSISTENCE_XML, "annotations.xml");
		props.setProperty(PersistenceOptions.JOIN_TABLE_FOR_NON_CONTAINED_ASSOCIATIONS, "false");
		props.setProperty(PersistenceOptions.ALWAYS_VERSION, "false");
		props.setProperty(PersistenceOptions.INHERITANCE_MAPPING, "TABLE_PER_CLASS");
		
		HbDataStore hbds = EntityPackage.eINSTANCE.createAndInitializeHbDataStore(props);
		WebPlugin.getInstance().getDao().factory = hbds.getSessionFactory();
		initWithTestData();

		Session session = WebPlugin.getInstance().getDao().getFactory().openSession();
		DBVersion dbVersion = null;
		try {
			dbVersion = WebPlugin.getInstance().getDao().find(DBVersion.class, DBVersion.SINGLETON_RECORD_ID);
			if (dbVersion == null) {
				logger.error("Cannot find database version from the database.");
			} else if (dbVersion.getDbVersion() < FlowerWebProperties.DB_VERSION) {
				logger.error("Database version mismatch. Database version = {} is lower than expected = {}. Please run the DB update scripts.", dbVersion.getDbVersion(), FlowerWebProperties.DB_VERSION);
			} else if (dbVersion.getDbVersion() > FlowerWebProperties.DB_VERSION) {
				logger.warn("Database version mismatch. Database version = {} is greater than expected = {}. You may continue, but please be aware that the application may malfunction and/or corrupt the data in the DB.", dbVersion.getDbVersion(), FlowerWebProperties.DB_VERSION);
			} else {
				// same version; everything is OK
				logger.info("Database version check OK. Version = {}", dbVersion.getDbVersion());
			}
		} finally {
			session.close();
		}
	}
	
	/**
	 * Creates an organization with the given name, an admin, anonymous user, groups and permissions
	 * for the organization.
	 * 
	 * @author Mariana
	 */
	private void createTestOrganization(final String organizationName, final String label, final String URL, final String logo) {
		Subject subject = new Subject();
		final Principal principal = new FlowerWebPrincipal(1); // FDC admin
		subject.getPrincipals().add(principal);
		Subject.doAsPrivileged(subject, new PrivilegedAction<Void>() {

			@Override
			public Void run() {
				Dao dao = WebPlugin.getInstance().getDao();
				Session session = dao.getFactory().openSession();
				try {
					// add org admin to DB
					GeneralService service = new GeneralService();
					User user = service.createUser(organizationName + "Admin", null);
					
					// add org to DB, add the user as admin and make sure the organization is not activated
					Organization organization = service.createOrganization(organizationName);
					if (label != null) {
						organization.setLabel(label);
					}
					organization.setURL(URL);
					organization.setLogoURL(logo);
					OrganizationUser ou = EntityFactory.eINSTANCE.createOrganizationUser();
					ou.setUser(user);
					ou.setOrganization(organization);
					ou.setStatus(OrganizationMembershipStatus.ADMIN);
					ou = dao.merge(ou);
					user.getOrganizationUsers().add(ou);
					organization.getOrganizationUsers().add(ou);
					user = dao.merge(user);
					organization.setActivated(false);
					organization = dao.merge(organization);
					
					// approve org => will automatically create groups, users, permissions
					OrganizationAdminUIDto dto = OrganizationService.getInstance().convertOrganizationToOrganizationAdminUIDto(organization, user);
					UserService.getInstance().approveDenyNewOrganization(null, dto, true, null);
					
					organization = dao.find(Organization.class, organization.getId());
					
					// SVN test data
					service.createSVNRepositoryURLAndAddToOrg("svn://csp1/flower2", organization);
					service.createSVNCommentAndAddToUser("comment " + user.getName(), user);
					
					return null;
				} catch (Exception e) {
					System.out.println(e);
					return null;
				} finally {
					session.close();
				}
			}
			
		}, null);
	}
	
	/**
	 * @author Cristi
	 * @author Mariana
	 */
	//TODO: eventually, this will have to be removed
	protected void initWithTestData() {
		if ("false".equals(WebPlugin.getInstance().getFlowerWebProperties().getProperty(PROP_DB_INIT_WITH_TEST_DATA))) {
			return;
		}

		////////////////////////////////////////////////////////////////////////
		// Initializations for all environments (production, test)
		////////////////////////////////////////////////////////////////////////
		Dao dao = WebPlugin.getInstance().getDao();
		Session session = dao.getFactory().openSession();
		
		try {
			// db version
			DBVersion dbVersion = EntityFactory.eINSTANCE.createDBVersion();
			dbVersion.setId(DBVersion.SINGLETON_RECORD_ID);
			dbVersion.setDbVersion(FlowerWebProperties.DB_VERSION);
			dao.merge(dbVersion);
			
			GeneralService service = new GeneralService();
			// ALL group is not supposed to exist in the DB, it is a group where all users in the DB belong
			Group all = EntityFactory.eINSTANCE.createGroup();
			all.setName("ALL");
	
			// everybody should be able to read .metadata
			service.createPermission(FlowerWebFilePermission.class, ".metadata", all, FlowerWebFilePermission.READ_WRITE_DELETE);
			service.createPermission(FlowerWebFilePermission.class, ".metadata/*", all, FlowerWebFilePermission.READ_WRITE_DELETE);
			
			// this seems to be mandatory for SVN;
			service.createPermission(FlowerWebFilePermission.class, "root", all, FlowerWebFilePermission.READ_WRITE);
			
			service.createPermission(FlowerWebFilePermission.class, "*", all, FlowerWebFilePermission.READ);
			
			// global admin group
			Group fdc_admin = service.createGroup("fdc_admin", null);
			service.createUserAndAddToGroups("admin", null, Collections.singletonList(fdc_admin));
			service.createUserAndAddToGroups("anonymous", "anonymous", Collections.singletonList(fdc_admin));
			
			service.createPermission(AdminSecurityEntitiesPermission.class, "", fdc_admin, "*");
			// make sure that admin can create/edit/delete any permission
			service.createPermission(ModifyTreePermissionsPermission.class, "*", fdc_admin, "*");
			service.createPermission(FlowerWebFilePermission.class, "root/*", fdc_admin, FlowerWebFilePermission.READ_WRITE_DELETE);
		} finally {
			session.close();
		}
		
		if (!PROP_DB_INIT_WITH_TEST_DATA_INTERNAL.equals(WebPlugin.getInstance().getFlowerWebProperties().getProperty(PROP_DB_INIT_WITH_TEST_DATA))) {
			return;
		}

		////////////////////////////////////////////////////////////////////////
		// Initializations for test environment only!!!
		// These records don't exist in a production environment!!! Make sure that the
		// app works correctly without them!
		////////////////////////////////////////////////////////////////////////
		
		createTestOrganization("org1", null, null, null);
		createTestOrganization("org2", null, null, null);
		createTestOrganization("crispico", null, null, null);
		createTestOrganization("hibernate", "Hibernate", "http://www.hibernate.org/", "https://forum.hibernate.org/styles/hibernate/imageset/site_logo.gif");
		createTestOrganization("spring", "Spring", "http://www.springsource.org/", "http://www.springsource.org/files/Logo_Spring_258x151.png");
		createTestOrganization("flex", "Apache Flex", "http://flex.apache.org/", "http://flex.apache.org/images/logo_01_fullcolor-sm.png");
	}
	
}
