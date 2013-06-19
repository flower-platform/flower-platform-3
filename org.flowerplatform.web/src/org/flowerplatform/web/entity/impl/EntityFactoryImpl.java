/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.web.entity.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.flowerplatform.web.entity.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EntityFactoryImpl extends EFactoryImpl implements EntityFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static EntityFactory init() {
		try {
			EntityFactory theEntityFactory = (EntityFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.flower-platform.com/xmi/entity_1.0.0"); 
			if (theEntityFactory != null) {
				return theEntityFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new EntityFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntityFactoryImpl() {
		super();
	}

	/**
	 * @author Mariana
	 */
	@Override
	public Object create(String className) {
		EClass eClass = (EClass) EntityPackage.eINSTANCE.getEClassifier(className);
		return create(eClass);
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case EntityPackage.ORGANIZATION: return createOrganization();
			case EntityPackage.GROUP: return createGroup();
			case EntityPackage.USER: return createUser();
			case EntityPackage.ORGANIZATION_USER: return createOrganizationUser();
			case EntityPackage.GROUP_USER: return createGroupUser();
			case EntityPackage.PERMISSION_ENTITY: return createPermissionEntity();
			case EntityPackage.SVN_REPOSITORY_URL_ENTITY: return createSVNRepositoryURLEntity();
			case EntityPackage.SVN_COMMENT_ENTITY: return createSVNCommentEntity();
			case EntityPackage.PERSPECTIVE_USER_ENTRY: return createPerspectiveUserEntry();
			case EntityPackage.FAVORITE_ITEM: return createFavoriteItem();
			case EntityPackage.RECENT_RESOURCE: return createRecentResource();
			case EntityPackage.AUDIT_ENTRY: return createAuditEntry();
			case EntityPackage.DB_VERSION: return createDBVersion();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case EntityPackage.ORGANIZATION_MEMBERSHIP_STATUS:
				return createOrganizationMembershipStatusFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case EntityPackage.ORGANIZATION_MEMBERSHIP_STATUS:
				return convertOrganizationMembershipStatusToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Organization createOrganization() {
		OrganizationImpl organization = new OrganizationImpl();
		return organization;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Group createGroup() {
		GroupImpl group = new GroupImpl();
		return group;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public User createUser() {
		UserImpl user = new UserImpl();
		return user;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OrganizationUser createOrganizationUser() {
		OrganizationUserImpl organizationUser = new OrganizationUserImpl();
		return organizationUser;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GroupUser createGroupUser() {
		GroupUserImpl groupUser = new GroupUserImpl();
		return groupUser;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PermissionEntity createPermissionEntity() {
		PermissionEntityImpl permissionEntity = new PermissionEntityImpl();
		return permissionEntity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SVNRepositoryURLEntity createSVNRepositoryURLEntity() {
		SVNRepositoryURLEntityImpl svnRepositoryURLEntity = new SVNRepositoryURLEntityImpl();
		return svnRepositoryURLEntity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SVNCommentEntity createSVNCommentEntity() {
		SVNCommentEntityImpl svnCommentEntity = new SVNCommentEntityImpl();
		return svnCommentEntity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PerspectiveUserEntry createPerspectiveUserEntry() {
		PerspectiveUserEntryImpl perspectiveUserEntry = new PerspectiveUserEntryImpl();
		return perspectiveUserEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FavoriteItem createFavoriteItem() {
		FavoriteItemImpl favoriteItem = new FavoriteItemImpl();
		return favoriteItem;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RecentResource createRecentResource() {
		RecentResourceImpl recentResource = new RecentResourceImpl();
		return recentResource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AuditEntry createAuditEntry() {
		AuditEntryImpl auditEntry = new AuditEntryImpl();
		return auditEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DBVersion createDBVersion() {
		DBVersionImpl dbVersion = new DBVersionImpl();
		return dbVersion;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OrganizationMembershipStatus createOrganizationMembershipStatusFromString(EDataType eDataType, String initialValue) {
		OrganizationMembershipStatus result = OrganizationMembershipStatus.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertOrganizationMembershipStatusToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntityPackage getEntityPackage() {
		return (EntityPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static EntityPackage getPackage() {
		return EntityPackage.eINSTANCE;
	}

} //EntityFactoryImpl
