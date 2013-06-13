/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.web.entity.impl;

import java.util.Properties;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.teneo.hibernate.HbDataStore;
import org.eclipse.emf.teneo.hibernate.HbHelper;

import org.flowerplatform.web.entity.AuditEntry;
import org.flowerplatform.web.entity.DBVersion;
import org.flowerplatform.web.entity.Entity;
import org.flowerplatform.web.entity.EntityFactory;
import org.flowerplatform.web.entity.EntityPackage;
import org.flowerplatform.web.entity.FavoriteItem;
import org.flowerplatform.web.entity.Group;
import org.flowerplatform.web.entity.GroupUser;
import org.flowerplatform.web.entity.ISecurityEntity;
import org.flowerplatform.web.entity.NamedEntity;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.OrganizationMembershipStatus;
import org.flowerplatform.web.entity.OrganizationUser;
import org.flowerplatform.web.entity.PermissionEntity;
import org.flowerplatform.web.entity.PerspectiveUserEntry;
import org.flowerplatform.web.entity.RecentResource;
import org.flowerplatform.web.entity.SVNCommentEntity;
import org.flowerplatform.web.entity.SVNRepositoryURLEntity;
import org.flowerplatform.web.entity.User;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EntityPackageImpl extends EPackageImpl implements EntityPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass entityEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass namedEntityEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iSecurityEntityEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass organizationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass groupEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass userEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass organizationUserEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass groupUserEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass permissionEntityEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass svnRepositoryURLEntityEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass svnCommentEntityEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass perspectiveUserEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass favoriteItemEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass recentResourceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass auditEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dbVersionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum organizationMembershipStatusEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.flowerplatform.web.entity.EntityPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private EntityPackageImpl() {
		super(eNS_URI, EntityFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link EntityPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static EntityPackage init() {
		if (isInited) return (EntityPackage)EPackage.Registry.INSTANCE.getEPackage(EntityPackage.eNS_URI);

		// Obtain or create and register package
		EntityPackageImpl theEntityPackage = (EntityPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof EntityPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new EntityPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theEntityPackage.createPackageContents();

		// Initialize created meta-data
		theEntityPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theEntityPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(EntityPackage.eNS_URI, theEntityPackage);
		return theEntityPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEntity() {
		return entityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEntity_Id() {
		return (EAttribute)entityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNamedEntity() {
		return namedEntityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNamedEntity_Name() {
		return (EAttribute)namedEntityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getISecurityEntity() {
		return iSecurityEntityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getOrganization() {
		return organizationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOrganization_Label() {
		return (EAttribute)organizationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOrganization_URL() {
		return (EAttribute)organizationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOrganization_LogoURL() {
		return (EAttribute)organizationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOrganization_IconURL() {
		return (EAttribute)organizationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOrganization_Activated() {
		return (EAttribute)organizationEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOrganization_Groups() {
		return (EReference)organizationEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOrganization_OrganizationUsers() {
		return (EReference)organizationEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOrganization_SvnRepositoryURLs() {
		return (EReference)organizationEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOrganization_ProjectsCount() {
		return (EAttribute)organizationEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOrganization_FilesCount() {
		return (EAttribute)organizationEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOrganization_ModelsCount() {
		return (EAttribute)organizationEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOrganization_DiagramsCount() {
		return (EAttribute)organizationEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGroup() {
		return groupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGroup_Organization() {
		return (EReference)groupEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGroup_GroupUsers() {
		return (EReference)groupEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUser() {
		return userEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUser_Login() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUser_HashedPassword() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUser_Email() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUser_Activated() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUser_ActivationCode() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUser_GroupUsers() {
		return (EReference)userEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUser_OrganizationUsers() {
		return (EReference)userEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUser_PerspectiveUserEntries() {
		return (EReference)userEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUser_LastPerspective() {
		return (EAttribute)userEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUser_SvnComments() {
		return (EReference)userEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUser_FavoriteItems() {
		return (EReference)userEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getOrganizationUser() {
		return organizationUserEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOrganizationUser_Organization() {
		return (EReference)organizationUserEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOrganizationUser_User() {
		return (EReference)organizationUserEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOrganizationUser_Status() {
		return (EAttribute)organizationUserEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGroupUser() {
		return groupUserEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGroupUser_Group() {
		return (EReference)groupUserEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGroupUser_User() {
		return (EReference)groupUserEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPermissionEntity() {
		return permissionEntityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPermissionEntity_Type() {
		return (EAttribute)permissionEntityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPermissionEntity_Actions() {
		return (EAttribute)permissionEntityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPermissionEntity_AssignedTo() {
		return (EAttribute)permissionEntityEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSVNRepositoryURLEntity() {
		return svnRepositoryURLEntityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSVNRepositoryURLEntity_Organization() {
		return (EReference)svnRepositoryURLEntityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSVNCommentEntity() {
		return svnCommentEntityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSVNCommentEntity_Body() {
		return (EAttribute)svnCommentEntityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSVNCommentEntity_User() {
		return (EReference)svnCommentEntityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSVNCommentEntity_Timestamp() {
		return (EAttribute)svnCommentEntityEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPerspectiveUserEntry() {
		return perspectiveUserEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPerspectiveUserEntry_User() {
		return (EReference)perspectiveUserEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPerspectiveUserEntry_SerializedLayoutData() {
		return (EAttribute)perspectiveUserEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFavoriteItem() {
		return favoriteItemEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFavoriteItem_User() {
		return (EReference)favoriteItemEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFavoriteItem_Resource() {
		return (EReference)favoriteItemEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFavoriteItem_Organization() {
		return (EReference)favoriteItemEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFavoriteItem_Category() {
		return (EAttribute)favoriteItemEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRecentResource() {
		return recentResourceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRecentResource_Label() {
		return (EAttribute)recentResourceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRecentResource_Path() {
		return (EAttribute)recentResourceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRecentResource_Organization() {
		return (EReference)recentResourceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRecentResource_LastAccess() {
		return (EAttribute)recentResourceEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRecentResource_LastAccessUser() {
		return (EReference)recentResourceEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRecentResource_LastSave() {
		return (EAttribute)recentResourceEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRecentResource_LastSaveUser() {
		return (EReference)recentResourceEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAuditEntry() {
		return auditEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAuditEntry_Timestamp() {
		return (EAttribute)auditEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAuditEntry_Username() {
		return (EAttribute)auditEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAuditEntry_IpAddress() {
		return (EAttribute)auditEntryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAuditEntry_AuditCategory() {
		return (EAttribute)auditEntryEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAuditEntry_Duration() {
		return (EAttribute)auditEntryEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAuditEntry_Param0() {
		return (EAttribute)auditEntryEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAuditEntry_Param1() {
		return (EAttribute)auditEntryEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAuditEntry_Param2() {
		return (EAttribute)auditEntryEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDBVersion() {
		return dbVersionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDBVersion_Id() {
		return (EAttribute)dbVersionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDBVersion_DbVersion() {
		return (EAttribute)dbVersionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getOrganizationMembershipStatus() {
		return organizationMembershipStatusEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntityFactory getEntityFactory() {
		return (EntityFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		entityEClass = createEClass(ENTITY);
		createEAttribute(entityEClass, ENTITY__ID);

		namedEntityEClass = createEClass(NAMED_ENTITY);
		createEAttribute(namedEntityEClass, NAMED_ENTITY__NAME);

		iSecurityEntityEClass = createEClass(ISECURITY_ENTITY);

		organizationEClass = createEClass(ORGANIZATION);
		createEAttribute(organizationEClass, ORGANIZATION__LABEL);
		createEAttribute(organizationEClass, ORGANIZATION__URL);
		createEAttribute(organizationEClass, ORGANIZATION__LOGO_URL);
		createEAttribute(organizationEClass, ORGANIZATION__ICON_URL);
		createEAttribute(organizationEClass, ORGANIZATION__ACTIVATED);
		createEReference(organizationEClass, ORGANIZATION__GROUPS);
		createEReference(organizationEClass, ORGANIZATION__ORGANIZATION_USERS);
		createEReference(organizationEClass, ORGANIZATION__SVN_REPOSITORY_UR_LS);
		createEAttribute(organizationEClass, ORGANIZATION__PROJECTS_COUNT);
		createEAttribute(organizationEClass, ORGANIZATION__FILES_COUNT);
		createEAttribute(organizationEClass, ORGANIZATION__MODELS_COUNT);
		createEAttribute(organizationEClass, ORGANIZATION__DIAGRAMS_COUNT);

		groupEClass = createEClass(GROUP);
		createEReference(groupEClass, GROUP__ORGANIZATION);
		createEReference(groupEClass, GROUP__GROUP_USERS);

		userEClass = createEClass(USER);
		createEAttribute(userEClass, USER__LOGIN);
		createEAttribute(userEClass, USER__HASHED_PASSWORD);
		createEAttribute(userEClass, USER__EMAIL);
		createEAttribute(userEClass, USER__ACTIVATED);
		createEAttribute(userEClass, USER__ACTIVATION_CODE);
		createEReference(userEClass, USER__GROUP_USERS);
		createEReference(userEClass, USER__ORGANIZATION_USERS);
		createEReference(userEClass, USER__PERSPECTIVE_USER_ENTRIES);
		createEAttribute(userEClass, USER__LAST_PERSPECTIVE);
		createEReference(userEClass, USER__SVN_COMMENTS);
		createEReference(userEClass, USER__FAVORITE_ITEMS);

		organizationUserEClass = createEClass(ORGANIZATION_USER);
		createEReference(organizationUserEClass, ORGANIZATION_USER__ORGANIZATION);
		createEReference(organizationUserEClass, ORGANIZATION_USER__USER);
		createEAttribute(organizationUserEClass, ORGANIZATION_USER__STATUS);

		groupUserEClass = createEClass(GROUP_USER);
		createEReference(groupUserEClass, GROUP_USER__GROUP);
		createEReference(groupUserEClass, GROUP_USER__USER);

		permissionEntityEClass = createEClass(PERMISSION_ENTITY);
		createEAttribute(permissionEntityEClass, PERMISSION_ENTITY__TYPE);
		createEAttribute(permissionEntityEClass, PERMISSION_ENTITY__ACTIONS);
		createEAttribute(permissionEntityEClass, PERMISSION_ENTITY__ASSIGNED_TO);

		svnRepositoryURLEntityEClass = createEClass(SVN_REPOSITORY_URL_ENTITY);
		createEReference(svnRepositoryURLEntityEClass, SVN_REPOSITORY_URL_ENTITY__ORGANIZATION);

		svnCommentEntityEClass = createEClass(SVN_COMMENT_ENTITY);
		createEAttribute(svnCommentEntityEClass, SVN_COMMENT_ENTITY__BODY);
		createEReference(svnCommentEntityEClass, SVN_COMMENT_ENTITY__USER);
		createEAttribute(svnCommentEntityEClass, SVN_COMMENT_ENTITY__TIMESTAMP);

		perspectiveUserEntryEClass = createEClass(PERSPECTIVE_USER_ENTRY);
		createEReference(perspectiveUserEntryEClass, PERSPECTIVE_USER_ENTRY__USER);
		createEAttribute(perspectiveUserEntryEClass, PERSPECTIVE_USER_ENTRY__SERIALIZED_LAYOUT_DATA);

		favoriteItemEClass = createEClass(FAVORITE_ITEM);
		createEReference(favoriteItemEClass, FAVORITE_ITEM__USER);
		createEReference(favoriteItemEClass, FAVORITE_ITEM__RESOURCE);
		createEReference(favoriteItemEClass, FAVORITE_ITEM__ORGANIZATION);
		createEAttribute(favoriteItemEClass, FAVORITE_ITEM__CATEGORY);

		recentResourceEClass = createEClass(RECENT_RESOURCE);
		createEAttribute(recentResourceEClass, RECENT_RESOURCE__LABEL);
		createEAttribute(recentResourceEClass, RECENT_RESOURCE__PATH);
		createEReference(recentResourceEClass, RECENT_RESOURCE__ORGANIZATION);
		createEAttribute(recentResourceEClass, RECENT_RESOURCE__LAST_ACCESS);
		createEReference(recentResourceEClass, RECENT_RESOURCE__LAST_ACCESS_USER);
		createEAttribute(recentResourceEClass, RECENT_RESOURCE__LAST_SAVE);
		createEReference(recentResourceEClass, RECENT_RESOURCE__LAST_SAVE_USER);

		auditEntryEClass = createEClass(AUDIT_ENTRY);
		createEAttribute(auditEntryEClass, AUDIT_ENTRY__TIMESTAMP);
		createEAttribute(auditEntryEClass, AUDIT_ENTRY__USERNAME);
		createEAttribute(auditEntryEClass, AUDIT_ENTRY__IP_ADDRESS);
		createEAttribute(auditEntryEClass, AUDIT_ENTRY__AUDIT_CATEGORY);
		createEAttribute(auditEntryEClass, AUDIT_ENTRY__DURATION);
		createEAttribute(auditEntryEClass, AUDIT_ENTRY__PARAM0);
		createEAttribute(auditEntryEClass, AUDIT_ENTRY__PARAM1);
		createEAttribute(auditEntryEClass, AUDIT_ENTRY__PARAM2);

		dbVersionEClass = createEClass(DB_VERSION);
		createEAttribute(dbVersionEClass, DB_VERSION__ID);
		createEAttribute(dbVersionEClass, DB_VERSION__DB_VERSION);

		// Create enums
		organizationMembershipStatusEEnum = createEEnum(ORGANIZATION_MEMBERSHIP_STATUS);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		namedEntityEClass.getESuperTypes().add(this.getEntity());
		organizationEClass.getESuperTypes().add(this.getNamedEntity());
		organizationEClass.getESuperTypes().add(this.getISecurityEntity());
		groupEClass.getESuperTypes().add(this.getNamedEntity());
		groupEClass.getESuperTypes().add(this.getISecurityEntity());
		userEClass.getESuperTypes().add(this.getNamedEntity());
		userEClass.getESuperTypes().add(this.getISecurityEntity());
		organizationUserEClass.getESuperTypes().add(this.getEntity());
		groupUserEClass.getESuperTypes().add(this.getEntity());
		permissionEntityEClass.getESuperTypes().add(this.getNamedEntity());
		svnRepositoryURLEntityEClass.getESuperTypes().add(this.getNamedEntity());
		svnCommentEntityEClass.getESuperTypes().add(this.getEntity());
		perspectiveUserEntryEClass.getESuperTypes().add(this.getNamedEntity());
		favoriteItemEClass.getESuperTypes().add(this.getEntity());
		recentResourceEClass.getESuperTypes().add(this.getEntity());
		auditEntryEClass.getESuperTypes().add(this.getEntity());

		// Initialize classes and features; add operations and parameters
		initEClass(entityEClass, Entity.class, "Entity", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEntity_Id(), ecorePackage.getELong(), "id", null, 0, 1, Entity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(namedEntityEClass, NamedEntity.class, "NamedEntity", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNamedEntity_Name(), ecorePackage.getEString(), "name", null, 0, 1, NamedEntity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iSecurityEntityEClass, ISecurityEntity.class, "ISecurityEntity", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		EOperation op = addEOperation(iSecurityEntityEClass, ecorePackage.getEBoolean(), "contains", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getISecurityEntity(), "securityEntity", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(organizationEClass, Organization.class, "Organization", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getOrganization_Label(), ecorePackage.getEString(), "label", null, 0, 1, Organization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOrganization_URL(), ecorePackage.getEString(), "URL", null, 0, 1, Organization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOrganization_LogoURL(), ecorePackage.getEString(), "logoURL", null, 0, 1, Organization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOrganization_IconURL(), ecorePackage.getEString(), "iconURL", null, 0, 1, Organization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOrganization_Activated(), ecorePackage.getEBoolean(), "activated", null, 0, 1, Organization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOrganization_Groups(), this.getGroup(), null, "groups", null, 0, -1, Organization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOrganization_OrganizationUsers(), this.getOrganizationUser(), null, "organizationUsers", null, 0, -1, Organization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOrganization_SvnRepositoryURLs(), this.getSVNRepositoryURLEntity(), null, "svnRepositoryURLs", null, 0, -1, Organization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOrganization_ProjectsCount(), ecorePackage.getEInt(), "projectsCount", null, 0, 1, Organization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOrganization_FilesCount(), ecorePackage.getEInt(), "filesCount", null, 0, 1, Organization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOrganization_ModelsCount(), ecorePackage.getEInt(), "modelsCount", null, 0, 1, Organization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOrganization_DiagramsCount(), ecorePackage.getEInt(), "diagramsCount", null, 0, 1, Organization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(groupEClass, Group.class, "Group", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getGroup_Organization(), this.getOrganization(), null, "organization", null, 0, 1, Group.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGroup_GroupUsers(), this.getGroupUser(), null, "groupUsers", null, 0, -1, Group.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(userEClass, User.class, "User", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getUser_Login(), ecorePackage.getEString(), "login", null, 0, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_HashedPassword(), ecorePackage.getEString(), "hashedPassword", null, 0, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_Email(), ecorePackage.getEString(), "email", null, 0, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_Activated(), ecorePackage.getEBoolean(), "activated", null, 0, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_ActivationCode(), ecorePackage.getEString(), "activationCode", null, 0, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getUser_GroupUsers(), this.getGroupUser(), null, "groupUsers", null, 0, -1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getUser_OrganizationUsers(), this.getOrganizationUser(), null, "organizationUsers", null, 0, -1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getUser_PerspectiveUserEntries(), this.getPerspectiveUserEntry(), null, "perspectiveUserEntries", null, 0, -1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUser_LastPerspective(), ecorePackage.getEString(), "lastPerspective", null, 0, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getUser_SvnComments(), this.getSVNCommentEntity(), null, "svnComments", null, 0, -1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getUser_FavoriteItems(), this.getFavoriteItem(), null, "favoriteItems", null, 0, -1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = addEOperation(userEClass, this.getFavoriteItem(), "getFavoriteItem", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEJavaObject(), "item", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEInt(), "category", 0, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(userEClass, ecorePackage.getEBoolean(), "isAdmin", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(organizationUserEClass, OrganizationUser.class, "OrganizationUser", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getOrganizationUser_Organization(), this.getOrganization(), null, "organization", null, 0, 1, OrganizationUser.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOrganizationUser_User(), this.getUser(), null, "user", null, 0, 1, OrganizationUser.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOrganizationUser_Status(), this.getOrganizationMembershipStatus(), "status", null, 0, 1, OrganizationUser.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(groupUserEClass, GroupUser.class, "GroupUser", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getGroupUser_Group(), this.getGroup(), null, "group", null, 0, 1, GroupUser.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGroupUser_User(), this.getUser(), null, "user", null, 0, 1, GroupUser.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(permissionEntityEClass, PermissionEntity.class, "PermissionEntity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPermissionEntity_Type(), ecorePackage.getEString(), "type", null, 0, 1, PermissionEntity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPermissionEntity_Actions(), ecorePackage.getEString(), "actions", null, 0, 1, PermissionEntity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPermissionEntity_AssignedTo(), ecorePackage.getEString(), "assignedTo", null, 0, 1, PermissionEntity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(svnRepositoryURLEntityEClass, SVNRepositoryURLEntity.class, "SVNRepositoryURLEntity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSVNRepositoryURLEntity_Organization(), this.getOrganization(), null, "organization", null, 0, 1, SVNRepositoryURLEntity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(svnCommentEntityEClass, SVNCommentEntity.class, "SVNCommentEntity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSVNCommentEntity_Body(), ecorePackage.getEString(), "body", null, 0, 1, SVNCommentEntity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSVNCommentEntity_User(), this.getUser(), null, "user", null, 0, 1, SVNCommentEntity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSVNCommentEntity_Timestamp(), ecorePackage.getELong(), "timestamp", null, 0, 1, SVNCommentEntity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(perspectiveUserEntryEClass, PerspectiveUserEntry.class, "PerspectiveUserEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPerspectiveUserEntry_User(), this.getUser(), null, "user", null, 0, 1, PerspectiveUserEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPerspectiveUserEntry_SerializedLayoutData(), ecorePackage.getEString(), "serializedLayoutData", null, 0, 1, PerspectiveUserEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(favoriteItemEClass, FavoriteItem.class, "FavoriteItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFavoriteItem_User(), this.getUser(), null, "user", null, 0, 1, FavoriteItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFavoriteItem_Resource(), this.getRecentResource(), null, "resource", null, 0, 1, FavoriteItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFavoriteItem_Organization(), this.getOrganization(), null, "organization", null, 0, 1, FavoriteItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFavoriteItem_Category(), ecorePackage.getEInt(), "category", null, 0, 1, FavoriteItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(favoriteItemEClass, ecorePackage.getEJavaObject(), "getItem", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(favoriteItemEClass, null, "setItem", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEJavaObject(), "item", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(recentResourceEClass, RecentResource.class, "RecentResource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getRecentResource_Label(), ecorePackage.getEString(), "label", null, 0, 1, RecentResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRecentResource_Path(), ecorePackage.getEString(), "path", null, 0, 1, RecentResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRecentResource_Organization(), this.getOrganization(), null, "organization", null, 0, 1, RecentResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRecentResource_LastAccess(), ecorePackage.getEDate(), "lastAccess", null, 0, 1, RecentResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRecentResource_LastAccessUser(), this.getUser(), null, "lastAccessUser", null, 0, 1, RecentResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRecentResource_LastSave(), ecorePackage.getEDate(), "lastSave", null, 0, 1, RecentResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRecentResource_LastSaveUser(), this.getUser(), null, "lastSaveUser", null, 0, 1, RecentResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(auditEntryEClass, AuditEntry.class, "AuditEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAuditEntry_Timestamp(), ecorePackage.getEDate(), "timestamp", null, 0, 1, AuditEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAuditEntry_Username(), ecorePackage.getEString(), "username", null, 0, 1, AuditEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAuditEntry_IpAddress(), ecorePackage.getEString(), "ipAddress", null, 0, 1, AuditEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAuditEntry_AuditCategory(), ecorePackage.getEString(), "auditCategory", null, 0, 1, AuditEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAuditEntry_Duration(), ecorePackage.getELong(), "duration", null, 0, 1, AuditEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAuditEntry_Param0(), ecorePackage.getEString(), "param0", null, 0, 1, AuditEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAuditEntry_Param1(), ecorePackage.getEString(), "param1", null, 0, 1, AuditEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAuditEntry_Param2(), ecorePackage.getEString(), "param2", null, 0, 1, AuditEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dbVersionEClass, DBVersion.class, "DBVersion", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDBVersion_Id(), ecorePackage.getELong(), "id", null, 0, 1, DBVersion.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDBVersion_DbVersion(), ecorePackage.getELong(), "dbVersion", null, 0, 1, DBVersion.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(organizationMembershipStatusEEnum, OrganizationMembershipStatus.class, "OrganizationMembershipStatus");
		addEEnumLiteral(organizationMembershipStatusEEnum, OrganizationMembershipStatus.MEMBER);
		addEEnumLiteral(organizationMembershipStatusEEnum, OrganizationMembershipStatus.ADMIN);
		addEEnumLiteral(organizationMembershipStatusEEnum, OrganizationMembershipStatus.PENDING_MEMBERSHIP_APPROVAL);

		// Create resource
		createResource(eNS_URI);
	}

	/**
	 * @author Mariana
	 */
	@Override
	public HbDataStore createAndInitializeHbDataStore(Properties properties) {
		try {
			// the name of the session factory
			String hbName = "Entity";
			// create the HbDataStore using the name
			final HbDataStore hbds = HbHelper.INSTANCE.createRegisterDataStore(hbName);
			 
			// set the properties
			hbds.setDataStoreProperties(properties);
			// sets its epackages stored in this datastore
			hbds.setEPackages(new EPackage[] { EntityPackage.eINSTANCE });
			 
			// initialize
			hbds.initialize();
			return hbds;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

} //EntityPackageImpl
