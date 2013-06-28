package org.flowerplatform.web.tests.security.sandbox;

import java.security.Policy;
import java.util.Properties;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.teneo.PersistenceOptions;
import org.eclipse.emf.teneo.hibernate.HbDataStore;
import org.eclipse.emf.teneo.hibernate.HbHelper;
import org.eclipse.emf.teneo.hibernate.HbSessionDataStore;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import sun.security.provider.PolicyFile;

import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.web.WebPlugin;
import org.flowerplatform.web.entity.EntityPackage;
import org.flowerplatform.web.security.mail.SendMailService;
import org.flowerplatform.web.security.sandbox.SecurityEntityListener;
import org.flowerplatform.web.tests.EclipseDependentTestSuiteBase;
import org.flowerplatform.web.tests.TestUtil;
import org.flowerplatform.web.tests.security.sandbox.helpers.FlowerWebPolicyTest;
import org.flowerplatform.web.tests.security.sandbox.helpers.Utils;

/**
 * @author Mariana
 */
@RunWith(Suite.class)
@SuiteClasses({ 
	RegistrationAndActivationTest.class,		// test registration and activation flow
	AdminSecurityEntitiesTest.class, 			// test permissions over security entities (users, groups, organizations)
	FlowerWebFilePermissionsTest.class,			// test permissions over files
	ModifyTreePermissionsPermissionTest.class,	// test permissions over permission entities
	TreePermissionCollectionTest.class,			// test tree permissions
	SecurityEntityListenerTest.class,			// test the security entity listener responsible with updating permission caches
	ServiceObserverTest.class					// test the service observer responsible with updating/deleting permission entities on security entities update/delete
})
public class SecurityPermissionsTests extends EclipseDependentTestSuiteBase {
	
	@BeforeClass 
	public static void setUp() {
		EclipseDependentTestSuiteBase.setUp();
		
		Configuration config = new Configuration();
		Properties props = config.getProperties();
		props.setProperty(Environment.DRIVER, "org.h2.Driver");
		props.setProperty(Environment.USER, "sa");
		props.setProperty(Environment.URL, "jdbc:h2:mem:temp_flower_web;MVCC=TRUE");
		props.setProperty(Environment.PASS, "");
		props.setProperty(Environment.DIALECT, H2Dialect.class.getName());
		props.setProperty(Environment.HBM2DDL_AUTO, "create");
		props.setProperty(Environment.SHOW_SQL, "true");
		
		props.setProperty(PersistenceOptions.CASCADE_POLICY_ON_NON_CONTAINMENT, "REFRESH,PERSIST,MERGE");
		props.setProperty(PersistenceOptions.PERSISTENCE_XML, "annotations.xml");
		props.setProperty(PersistenceOptions.JOIN_TABLE_FOR_NON_CONTAINED_ASSOCIATIONS, "false");
		props.setProperty(PersistenceOptions.ALWAYS_VERSION, "false");
		props.setProperty(PersistenceOptions.INHERITANCE_MAPPING, "TABLE_PER_CLASS");
		props.setProperty(PersistenceOptions.ADD_INDEX_FOR_FOREIGN_KEY, "false");

//		props.setProperty(Environment.DRIVER, "org.postgresql.Driver");
//		props.setProperty(Environment.USER, "postgres");
//		props.setProperty(Environment.URL, "jdbc:postgresql://localhost/flower-dev-center");
//		props.setProperty(Environment.PASS, "postgres");
//		props.setProperty(Environment.DIALECT, PostgresPlusDialect.class.getName());
//		props.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
//		props.setProperty(Environment.HBM2DDL_AUTO, "create-drop");
//		props.setProperty(Environment.SHOW_SQL, "true");
		
			// the name of the session factory
			String hbName = "Entity";
			// create the HbDataStore using the name
			final HbDataStore hbds = HbHelper.INSTANCE.createRegisterDataStore(hbName);
			 
			// set the properties
			hbds.setDataStoreProperties(props);
			// sets its epackages stored in this datastore
			hbds.setEPackages(new EPackage[] { EntityPackage.eINSTANCE });
			((HbSessionDataStore) hbds).setConfiguration(config); 
			
			try {
			// initialize
			hbds.initialize();
		} catch (Exception e) {
			System.out.println(e);
		}

		
		WebPlugin.getInstance().getDatabaseManager().setFactory(hbds.getSessionFactory());
		EventListenerRegistry registry = ((SessionFactoryImpl) hbds.getSessionFactory()).getServiceRegistry().getService(EventListenerRegistry.class);
		SecurityEntityListener listener = new SecurityEntityListener();
		registry.appendListeners(EventType.POST_INSERT, listener);
		registry.appendListeners(EventType.POST_DELETE, listener);
		registry.appendListeners(EventType.POST_UPDATE, listener);
		registry.appendListeners(EventType.PRE_DELETE, listener);
		
		Utils.deleteAllData();
		
		TestUtil.copyFilesAndCreateProject(TestUtil.ECLIPSE_DEPENDENT_FILES_DIR + "/user_admin", "user_admin");
		System.setProperty("java.security.policy", TestUtil.getWorkspaceResourceAbsolutePath("/user_admin/all.policy"));
		PolicyFile policyFile = new PolicyFile();
		Policy.setPolicy(new FlowerWebPolicyTest(policyFile));
		Policy.getPolicy().refresh();
		System.setSecurityManager(new SecurityManager());
		
		// disable sending mails during testing
		CommonPlugin.getInstance().getFlowerWebProperties().remove("mail.smtp.host");
		SendMailService service = (SendMailService) CommunicationPlugin.getInstance().getServiceRegistry().getService(SendMailService.SERVICE_ID);
		service.initializeProperties();
	}
}
