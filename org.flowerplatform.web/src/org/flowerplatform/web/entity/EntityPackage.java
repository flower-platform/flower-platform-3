/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.web.entity;

import java.util.Properties;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.teneo.hibernate.HbDataStore;
import org.hibernate.cfg.Configuration;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.flowerplatform.web.entity.EntityFactory
 * @model kind="package"
 * @generated
 */
public interface EntityPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "entity";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.flower-platform.com/xmi/entity_1.0.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.flowerplatform.web.entity";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	EntityPackage eINSTANCE = org.flowerplatform.web.entity.impl.EntityPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.flowerplatform.web.entity.impl.EntityImpl <em>Entity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.web.entity.impl.EntityImpl
	 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getEntity()
	 * @generated
	 */
	int ENTITY = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY__ID = 0;

	/**
	 * The number of structural features of the '<em>Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.flowerplatform.web.entity.impl.NamedEntityImpl <em>Named Entity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.web.entity.impl.NamedEntityImpl
	 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getNamedEntity()
	 * @generated
	 */
	int NAMED_ENTITY = 1;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ENTITY__ID = ENTITY__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ENTITY__NAME = ENTITY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Named Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ENTITY_FEATURE_COUNT = ENTITY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.flowerplatform.web.entity.ISecurityEntity <em>ISecurity Entity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.web.entity.ISecurityEntity
	 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getISecurityEntity()
	 * @generated
	 */
	int ISECURITY_ENTITY = 2;

	/**
	 * The number of structural features of the '<em>ISecurity Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISECURITY_ENTITY_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.flowerplatform.web.entity.impl.OrganizationImpl <em>Organization</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.web.entity.impl.OrganizationImpl
	 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getOrganization()
	 * @generated
	 */
	int ORGANIZATION = 3;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORGANIZATION__ID = NAMED_ENTITY__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORGANIZATION__NAME = NAMED_ENTITY__NAME;

	/**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORGANIZATION__LABEL = NAMED_ENTITY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORGANIZATION__URL = NAMED_ENTITY_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Logo URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORGANIZATION__LOGO_URL = NAMED_ENTITY_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Icon URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORGANIZATION__ICON_URL = NAMED_ENTITY_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Activated</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORGANIZATION__ACTIVATED = NAMED_ENTITY_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Groups</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORGANIZATION__GROUPS = NAMED_ENTITY_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Organization Users</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORGANIZATION__ORGANIZATION_USERS = NAMED_ENTITY_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Svn Repository UR Ls</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORGANIZATION__SVN_REPOSITORY_UR_LS = NAMED_ENTITY_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Projects Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORGANIZATION__PROJECTS_COUNT = NAMED_ENTITY_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Files Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORGANIZATION__FILES_COUNT = NAMED_ENTITY_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Models Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORGANIZATION__MODELS_COUNT = NAMED_ENTITY_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Diagrams Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORGANIZATION__DIAGRAMS_COUNT = NAMED_ENTITY_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Working Directories</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORGANIZATION__WORKING_DIRECTORIES = NAMED_ENTITY_FEATURE_COUNT + 12;

	/**
	 * The number of structural features of the '<em>Organization</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORGANIZATION_FEATURE_COUNT = NAMED_ENTITY_FEATURE_COUNT + 13;

	/**
	 * The meta object id for the '{@link org.flowerplatform.web.entity.impl.GroupImpl <em>Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.web.entity.impl.GroupImpl
	 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getGroup()
	 * @generated
	 */
	int GROUP = 4;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP__ID = NAMED_ENTITY__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP__NAME = NAMED_ENTITY__NAME;

	/**
	 * The feature id for the '<em><b>Organization</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP__ORGANIZATION = NAMED_ENTITY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Group Users</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP__GROUP_USERS = NAMED_ENTITY_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_FEATURE_COUNT = NAMED_ENTITY_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.flowerplatform.web.entity.impl.UserImpl <em>User</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.web.entity.impl.UserImpl
	 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getUser()
	 * @generated
	 */
	int USER = 5;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER__ID = NAMED_ENTITY__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER__NAME = NAMED_ENTITY__NAME;

	/**
	 * The feature id for the '<em><b>Login</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER__LOGIN = NAMED_ENTITY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Hashed Password</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER__HASHED_PASSWORD = NAMED_ENTITY_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Email</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER__EMAIL = NAMED_ENTITY_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Activated</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER__ACTIVATED = NAMED_ENTITY_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Activation Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER__ACTIVATION_CODE = NAMED_ENTITY_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Group Users</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER__GROUP_USERS = NAMED_ENTITY_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Organization Users</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER__ORGANIZATION_USERS = NAMED_ENTITY_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Perspective User Entries</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER__PERSPECTIVE_USER_ENTRIES = NAMED_ENTITY_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Last Perspective</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER__LAST_PERSPECTIVE = NAMED_ENTITY_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Svn Comments</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER__SVN_COMMENTS = NAMED_ENTITY_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Favorite Items</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER__FAVORITE_ITEMS = NAMED_ENTITY_FEATURE_COUNT + 10;

	/**
	 * The number of structural features of the '<em>User</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_FEATURE_COUNT = NAMED_ENTITY_FEATURE_COUNT + 11;

	/**
	 * The meta object id for the '{@link org.flowerplatform.web.entity.impl.OrganizationUserImpl <em>Organization User</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.web.entity.impl.OrganizationUserImpl
	 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getOrganizationUser()
	 * @generated
	 */
	int ORGANIZATION_USER = 6;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORGANIZATION_USER__ID = ENTITY__ID;

	/**
	 * The feature id for the '<em><b>Organization</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORGANIZATION_USER__ORGANIZATION = ENTITY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>User</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORGANIZATION_USER__USER = ENTITY_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Status</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORGANIZATION_USER__STATUS = ENTITY_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Organization User</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORGANIZATION_USER_FEATURE_COUNT = ENTITY_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.flowerplatform.web.entity.impl.GroupUserImpl <em>Group User</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.web.entity.impl.GroupUserImpl
	 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getGroupUser()
	 * @generated
	 */
	int GROUP_USER = 7;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_USER__ID = ENTITY__ID;

	/**
	 * The feature id for the '<em><b>Group</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_USER__GROUP = ENTITY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>User</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_USER__USER = ENTITY_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Group User</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_USER_FEATURE_COUNT = ENTITY_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.flowerplatform.web.entity.impl.PermissionEntityImpl <em>Permission Entity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.web.entity.impl.PermissionEntityImpl
	 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getPermissionEntity()
	 * @generated
	 */
	int PERMISSION_ENTITY = 8;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERMISSION_ENTITY__ID = NAMED_ENTITY__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERMISSION_ENTITY__NAME = NAMED_ENTITY__NAME;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERMISSION_ENTITY__TYPE = NAMED_ENTITY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Actions</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERMISSION_ENTITY__ACTIONS = NAMED_ENTITY_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Assigned To</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERMISSION_ENTITY__ASSIGNED_TO = NAMED_ENTITY_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Permission Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERMISSION_ENTITY_FEATURE_COUNT = NAMED_ENTITY_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.flowerplatform.web.entity.impl.SVNRepositoryURLEntityImpl <em>SVN Repository URL Entity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.web.entity.impl.SVNRepositoryURLEntityImpl
	 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getSVNRepositoryURLEntity()
	 * @generated
	 */
	int SVN_REPOSITORY_URL_ENTITY = 9;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SVN_REPOSITORY_URL_ENTITY__ID = NAMED_ENTITY__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SVN_REPOSITORY_URL_ENTITY__NAME = NAMED_ENTITY__NAME;

	/**
	 * The feature id for the '<em><b>Organization</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SVN_REPOSITORY_URL_ENTITY__ORGANIZATION = NAMED_ENTITY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>SVN Repository URL Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SVN_REPOSITORY_URL_ENTITY_FEATURE_COUNT = NAMED_ENTITY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.flowerplatform.web.entity.impl.SVNCommentEntityImpl <em>SVN Comment Entity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.web.entity.impl.SVNCommentEntityImpl
	 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getSVNCommentEntity()
	 * @generated
	 */
	int SVN_COMMENT_ENTITY = 10;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SVN_COMMENT_ENTITY__ID = ENTITY__ID;

	/**
	 * The feature id for the '<em><b>Body</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SVN_COMMENT_ENTITY__BODY = ENTITY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>User</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SVN_COMMENT_ENTITY__USER = ENTITY_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Timestamp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SVN_COMMENT_ENTITY__TIMESTAMP = ENTITY_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>SVN Comment Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SVN_COMMENT_ENTITY_FEATURE_COUNT = ENTITY_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.flowerplatform.web.entity.impl.PerspectiveUserEntryImpl <em>Perspective User Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.web.entity.impl.PerspectiveUserEntryImpl
	 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getPerspectiveUserEntry()
	 * @generated
	 */
	int PERSPECTIVE_USER_ENTRY = 11;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERSPECTIVE_USER_ENTRY__ID = NAMED_ENTITY__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERSPECTIVE_USER_ENTRY__NAME = NAMED_ENTITY__NAME;

	/**
	 * The feature id for the '<em><b>User</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERSPECTIVE_USER_ENTRY__USER = NAMED_ENTITY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Serialized Layout Data</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERSPECTIVE_USER_ENTRY__SERIALIZED_LAYOUT_DATA = NAMED_ENTITY_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Perspective User Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERSPECTIVE_USER_ENTRY_FEATURE_COUNT = NAMED_ENTITY_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.flowerplatform.web.entity.impl.FavoriteItemImpl <em>Favorite Item</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.web.entity.impl.FavoriteItemImpl
	 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getFavoriteItem()
	 * @generated
	 */
	int FAVORITE_ITEM = 12;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FAVORITE_ITEM__ID = ENTITY__ID;

	/**
	 * The feature id for the '<em><b>User</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FAVORITE_ITEM__USER = ENTITY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Resource</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FAVORITE_ITEM__RESOURCE = ENTITY_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Organization</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FAVORITE_ITEM__ORGANIZATION = ENTITY_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FAVORITE_ITEM__CATEGORY = ENTITY_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Favorite Item</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FAVORITE_ITEM_FEATURE_COUNT = ENTITY_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link org.flowerplatform.web.entity.impl.RecentResourceImpl <em>Recent Resource</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.web.entity.impl.RecentResourceImpl
	 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getRecentResource()
	 * @generated
	 */
	int RECENT_RESOURCE = 13;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECENT_RESOURCE__ID = ENTITY__ID;

	/**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECENT_RESOURCE__LABEL = ENTITY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECENT_RESOURCE__PATH = ENTITY_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Organization</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECENT_RESOURCE__ORGANIZATION = ENTITY_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Last Access</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECENT_RESOURCE__LAST_ACCESS = ENTITY_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Last Access User</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECENT_RESOURCE__LAST_ACCESS_USER = ENTITY_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Last Save</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECENT_RESOURCE__LAST_SAVE = ENTITY_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Last Save User</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECENT_RESOURCE__LAST_SAVE_USER = ENTITY_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Recent Resource</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECENT_RESOURCE_FEATURE_COUNT = ENTITY_FEATURE_COUNT + 7;

	/**
	 * The meta object id for the '{@link org.flowerplatform.web.entity.impl.AuditEntryImpl <em>Audit Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.web.entity.impl.AuditEntryImpl
	 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getAuditEntry()
	 * @generated
	 */
	int AUDIT_ENTRY = 14;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AUDIT_ENTRY__ID = ENTITY__ID;

	/**
	 * The feature id for the '<em><b>Timestamp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AUDIT_ENTRY__TIMESTAMP = ENTITY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Username</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AUDIT_ENTRY__USERNAME = ENTITY_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Ip Address</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AUDIT_ENTRY__IP_ADDRESS = ENTITY_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Audit Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AUDIT_ENTRY__AUDIT_CATEGORY = ENTITY_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AUDIT_ENTRY__DURATION = ENTITY_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Param0</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AUDIT_ENTRY__PARAM0 = ENTITY_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Param1</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AUDIT_ENTRY__PARAM1 = ENTITY_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Param2</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AUDIT_ENTRY__PARAM2 = ENTITY_FEATURE_COUNT + 7;

	/**
	 * The number of structural features of the '<em>Audit Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AUDIT_ENTRY_FEATURE_COUNT = ENTITY_FEATURE_COUNT + 8;

	/**
	 * The meta object id for the '{@link org.flowerplatform.web.entity.impl.DBVersionImpl <em>DB Version</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.web.entity.impl.DBVersionImpl
	 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getDBVersion()
	 * @generated
	 */
	int DB_VERSION = 15;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DB_VERSION__ID = 0;

	/**
	 * The feature id for the '<em><b>Db Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DB_VERSION__DB_VERSION = 1;

	/**
	 * The number of structural features of the '<em>DB Version</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DB_VERSION_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.flowerplatform.web.entity.impl.WorkingDirectoryImpl <em>Working Directory</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.web.entity.impl.WorkingDirectoryImpl
	 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getWorkingDirectory()
	 * @generated
	 */
	int WORKING_DIRECTORY = 16;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WORKING_DIRECTORY__ID = ENTITY__ID;

	/**
	 * The feature id for the '<em><b>Organization</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WORKING_DIRECTORY__ORGANIZATION = ENTITY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Path From Organization</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WORKING_DIRECTORY__PATH_FROM_ORGANIZATION = ENTITY_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Color</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WORKING_DIRECTORY__COLOR = ENTITY_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Working Directory</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WORKING_DIRECTORY_FEATURE_COUNT = ENTITY_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.flowerplatform.web.entity.OrganizationMembershipStatus <em>Organization Membership Status</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.web.entity.OrganizationMembershipStatus
	 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getOrganizationMembershipStatus()
	 * @generated
	 */
	int ORGANIZATION_MEMBERSHIP_STATUS = 17;

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.web.entity.Entity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entity</em>'.
	 * @see org.flowerplatform.web.entity.Entity
	 * @generated
	 */
	EClass getEntity();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.Entity#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.flowerplatform.web.entity.Entity#getId()
	 * @see #getEntity()
	 * @generated
	 */
	EAttribute getEntity_Id();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.web.entity.NamedEntity <em>Named Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Named Entity</em>'.
	 * @see org.flowerplatform.web.entity.NamedEntity
	 * @generated
	 */
	EClass getNamedEntity();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.NamedEntity#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.flowerplatform.web.entity.NamedEntity#getName()
	 * @see #getNamedEntity()
	 * @generated
	 */
	EAttribute getNamedEntity_Name();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.web.entity.ISecurityEntity <em>ISecurity Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ISecurity Entity</em>'.
	 * @see org.flowerplatform.web.entity.ISecurityEntity
	 * @generated
	 */
	EClass getISecurityEntity();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.web.entity.Organization <em>Organization</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Organization</em>'.
	 * @see org.flowerplatform.web.entity.Organization
	 * @generated
	 */
	EClass getOrganization();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.Organization#getLabel <em>Label</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Label</em>'.
	 * @see org.flowerplatform.web.entity.Organization#getLabel()
	 * @see #getOrganization()
	 * @generated
	 */
	EAttribute getOrganization_Label();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.Organization#getURL <em>URL</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>URL</em>'.
	 * @see org.flowerplatform.web.entity.Organization#getURL()
	 * @see #getOrganization()
	 * @generated
	 */
	EAttribute getOrganization_URL();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.Organization#getLogoURL <em>Logo URL</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Logo URL</em>'.
	 * @see org.flowerplatform.web.entity.Organization#getLogoURL()
	 * @see #getOrganization()
	 * @generated
	 */
	EAttribute getOrganization_LogoURL();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.Organization#getIconURL <em>Icon URL</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Icon URL</em>'.
	 * @see org.flowerplatform.web.entity.Organization#getIconURL()
	 * @see #getOrganization()
	 * @generated
	 */
	EAttribute getOrganization_IconURL();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.Organization#isActivated <em>Activated</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Activated</em>'.
	 * @see org.flowerplatform.web.entity.Organization#isActivated()
	 * @see #getOrganization()
	 * @generated
	 */
	EAttribute getOrganization_Activated();

	/**
	 * Returns the meta object for the reference list '{@link org.flowerplatform.web.entity.Organization#getGroups <em>Groups</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Groups</em>'.
	 * @see org.flowerplatform.web.entity.Organization#getGroups()
	 * @see #getOrganization()
	 * @generated
	 */
	EReference getOrganization_Groups();

	/**
	 * Returns the meta object for the reference list '{@link org.flowerplatform.web.entity.Organization#getOrganizationUsers <em>Organization Users</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Organization Users</em>'.
	 * @see org.flowerplatform.web.entity.Organization#getOrganizationUsers()
	 * @see #getOrganization()
	 * @generated
	 */
	EReference getOrganization_OrganizationUsers();

	/**
	 * Returns the meta object for the reference list '{@link org.flowerplatform.web.entity.Organization#getSvnRepositoryURLs <em>Svn Repository UR Ls</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Svn Repository UR Ls</em>'.
	 * @see org.flowerplatform.web.entity.Organization#getSvnRepositoryURLs()
	 * @see #getOrganization()
	 * @generated
	 */
	EReference getOrganization_SvnRepositoryURLs();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.Organization#getProjectsCount <em>Projects Count</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Projects Count</em>'.
	 * @see org.flowerplatform.web.entity.Organization#getProjectsCount()
	 * @see #getOrganization()
	 * @generated
	 */
	EAttribute getOrganization_ProjectsCount();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.Organization#getFilesCount <em>Files Count</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Files Count</em>'.
	 * @see org.flowerplatform.web.entity.Organization#getFilesCount()
	 * @see #getOrganization()
	 * @generated
	 */
	EAttribute getOrganization_FilesCount();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.Organization#getModelsCount <em>Models Count</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Models Count</em>'.
	 * @see org.flowerplatform.web.entity.Organization#getModelsCount()
	 * @see #getOrganization()
	 * @generated
	 */
	EAttribute getOrganization_ModelsCount();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.Organization#getDiagramsCount <em>Diagrams Count</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Diagrams Count</em>'.
	 * @see org.flowerplatform.web.entity.Organization#getDiagramsCount()
	 * @see #getOrganization()
	 * @generated
	 */
	EAttribute getOrganization_DiagramsCount();

	/**
	 * Returns the meta object for the reference list '{@link org.flowerplatform.web.entity.Organization#getWorkingDirectories <em>Working Directories</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Working Directories</em>'.
	 * @see org.flowerplatform.web.entity.Organization#getWorkingDirectories()
	 * @see #getOrganization()
	 * @generated
	 */
	EReference getOrganization_WorkingDirectories();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.web.entity.Group <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Group</em>'.
	 * @see org.flowerplatform.web.entity.Group
	 * @generated
	 */
	EClass getGroup();

	/**
	 * Returns the meta object for the reference '{@link org.flowerplatform.web.entity.Group#getOrganization <em>Organization</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Organization</em>'.
	 * @see org.flowerplatform.web.entity.Group#getOrganization()
	 * @see #getGroup()
	 * @generated
	 */
	EReference getGroup_Organization();

	/**
	 * Returns the meta object for the reference list '{@link org.flowerplatform.web.entity.Group#getGroupUsers <em>Group Users</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Group Users</em>'.
	 * @see org.flowerplatform.web.entity.Group#getGroupUsers()
	 * @see #getGroup()
	 * @generated
	 */
	EReference getGroup_GroupUsers();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.web.entity.User <em>User</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>User</em>'.
	 * @see org.flowerplatform.web.entity.User
	 * @generated
	 */
	EClass getUser();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.User#getLogin <em>Login</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Login</em>'.
	 * @see org.flowerplatform.web.entity.User#getLogin()
	 * @see #getUser()
	 * @generated
	 */
	EAttribute getUser_Login();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.User#getHashedPassword <em>Hashed Password</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hashed Password</em>'.
	 * @see org.flowerplatform.web.entity.User#getHashedPassword()
	 * @see #getUser()
	 * @generated
	 */
	EAttribute getUser_HashedPassword();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.User#getEmail <em>Email</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Email</em>'.
	 * @see org.flowerplatform.web.entity.User#getEmail()
	 * @see #getUser()
	 * @generated
	 */
	EAttribute getUser_Email();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.User#isActivated <em>Activated</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Activated</em>'.
	 * @see org.flowerplatform.web.entity.User#isActivated()
	 * @see #getUser()
	 * @generated
	 */
	EAttribute getUser_Activated();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.User#getActivationCode <em>Activation Code</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Activation Code</em>'.
	 * @see org.flowerplatform.web.entity.User#getActivationCode()
	 * @see #getUser()
	 * @generated
	 */
	EAttribute getUser_ActivationCode();

	/**
	 * Returns the meta object for the reference list '{@link org.flowerplatform.web.entity.User#getGroupUsers <em>Group Users</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Group Users</em>'.
	 * @see org.flowerplatform.web.entity.User#getGroupUsers()
	 * @see #getUser()
	 * @generated
	 */
	EReference getUser_GroupUsers();

	/**
	 * Returns the meta object for the reference list '{@link org.flowerplatform.web.entity.User#getOrganizationUsers <em>Organization Users</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Organization Users</em>'.
	 * @see org.flowerplatform.web.entity.User#getOrganizationUsers()
	 * @see #getUser()
	 * @generated
	 */
	EReference getUser_OrganizationUsers();

	/**
	 * Returns the meta object for the reference list '{@link org.flowerplatform.web.entity.User#getPerspectiveUserEntries <em>Perspective User Entries</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Perspective User Entries</em>'.
	 * @see org.flowerplatform.web.entity.User#getPerspectiveUserEntries()
	 * @see #getUser()
	 * @generated
	 */
	EReference getUser_PerspectiveUserEntries();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.User#getLastPerspective <em>Last Perspective</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Last Perspective</em>'.
	 * @see org.flowerplatform.web.entity.User#getLastPerspective()
	 * @see #getUser()
	 * @generated
	 */
	EAttribute getUser_LastPerspective();

	/**
	 * Returns the meta object for the reference list '{@link org.flowerplatform.web.entity.User#getSvnComments <em>Svn Comments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Svn Comments</em>'.
	 * @see org.flowerplatform.web.entity.User#getSvnComments()
	 * @see #getUser()
	 * @generated
	 */
	EReference getUser_SvnComments();

	/**
	 * Returns the meta object for the reference list '{@link org.flowerplatform.web.entity.User#getFavoriteItems <em>Favorite Items</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Favorite Items</em>'.
	 * @see org.flowerplatform.web.entity.User#getFavoriteItems()
	 * @see #getUser()
	 * @generated
	 */
	EReference getUser_FavoriteItems();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.web.entity.OrganizationUser <em>Organization User</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Organization User</em>'.
	 * @see org.flowerplatform.web.entity.OrganizationUser
	 * @generated
	 */
	EClass getOrganizationUser();

	/**
	 * Returns the meta object for the reference '{@link org.flowerplatform.web.entity.OrganizationUser#getOrganization <em>Organization</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Organization</em>'.
	 * @see org.flowerplatform.web.entity.OrganizationUser#getOrganization()
	 * @see #getOrganizationUser()
	 * @generated
	 */
	EReference getOrganizationUser_Organization();

	/**
	 * Returns the meta object for the reference '{@link org.flowerplatform.web.entity.OrganizationUser#getUser <em>User</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>User</em>'.
	 * @see org.flowerplatform.web.entity.OrganizationUser#getUser()
	 * @see #getOrganizationUser()
	 * @generated
	 */
	EReference getOrganizationUser_User();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.OrganizationUser#getStatus <em>Status</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Status</em>'.
	 * @see org.flowerplatform.web.entity.OrganizationUser#getStatus()
	 * @see #getOrganizationUser()
	 * @generated
	 */
	EAttribute getOrganizationUser_Status();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.web.entity.GroupUser <em>Group User</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Group User</em>'.
	 * @see org.flowerplatform.web.entity.GroupUser
	 * @generated
	 */
	EClass getGroupUser();

	/**
	 * Returns the meta object for the reference '{@link org.flowerplatform.web.entity.GroupUser#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Group</em>'.
	 * @see org.flowerplatform.web.entity.GroupUser#getGroup()
	 * @see #getGroupUser()
	 * @generated
	 */
	EReference getGroupUser_Group();

	/**
	 * Returns the meta object for the reference '{@link org.flowerplatform.web.entity.GroupUser#getUser <em>User</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>User</em>'.
	 * @see org.flowerplatform.web.entity.GroupUser#getUser()
	 * @see #getGroupUser()
	 * @generated
	 */
	EReference getGroupUser_User();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.web.entity.PermissionEntity <em>Permission Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Permission Entity</em>'.
	 * @see org.flowerplatform.web.entity.PermissionEntity
	 * @generated
	 */
	EClass getPermissionEntity();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.PermissionEntity#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.flowerplatform.web.entity.PermissionEntity#getType()
	 * @see #getPermissionEntity()
	 * @generated
	 */
	EAttribute getPermissionEntity_Type();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.PermissionEntity#getActions <em>Actions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Actions</em>'.
	 * @see org.flowerplatform.web.entity.PermissionEntity#getActions()
	 * @see #getPermissionEntity()
	 * @generated
	 */
	EAttribute getPermissionEntity_Actions();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.PermissionEntity#getAssignedTo <em>Assigned To</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Assigned To</em>'.
	 * @see org.flowerplatform.web.entity.PermissionEntity#getAssignedTo()
	 * @see #getPermissionEntity()
	 * @generated
	 */
	EAttribute getPermissionEntity_AssignedTo();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.web.entity.SVNRepositoryURLEntity <em>SVN Repository URL Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>SVN Repository URL Entity</em>'.
	 * @see org.flowerplatform.web.entity.SVNRepositoryURLEntity
	 * @generated
	 */
	EClass getSVNRepositoryURLEntity();

	/**
	 * Returns the meta object for the reference '{@link org.flowerplatform.web.entity.SVNRepositoryURLEntity#getOrganization <em>Organization</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Organization</em>'.
	 * @see org.flowerplatform.web.entity.SVNRepositoryURLEntity#getOrganization()
	 * @see #getSVNRepositoryURLEntity()
	 * @generated
	 */
	EReference getSVNRepositoryURLEntity_Organization();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.web.entity.SVNCommentEntity <em>SVN Comment Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>SVN Comment Entity</em>'.
	 * @see org.flowerplatform.web.entity.SVNCommentEntity
	 * @generated
	 */
	EClass getSVNCommentEntity();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.SVNCommentEntity#getBody <em>Body</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Body</em>'.
	 * @see org.flowerplatform.web.entity.SVNCommentEntity#getBody()
	 * @see #getSVNCommentEntity()
	 * @generated
	 */
	EAttribute getSVNCommentEntity_Body();

	/**
	 * Returns the meta object for the reference '{@link org.flowerplatform.web.entity.SVNCommentEntity#getUser <em>User</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>User</em>'.
	 * @see org.flowerplatform.web.entity.SVNCommentEntity#getUser()
	 * @see #getSVNCommentEntity()
	 * @generated
	 */
	EReference getSVNCommentEntity_User();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.SVNCommentEntity#getTimestamp <em>Timestamp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Timestamp</em>'.
	 * @see org.flowerplatform.web.entity.SVNCommentEntity#getTimestamp()
	 * @see #getSVNCommentEntity()
	 * @generated
	 */
	EAttribute getSVNCommentEntity_Timestamp();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.web.entity.PerspectiveUserEntry <em>Perspective User Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Perspective User Entry</em>'.
	 * @see org.flowerplatform.web.entity.PerspectiveUserEntry
	 * @generated
	 */
	EClass getPerspectiveUserEntry();

	/**
	 * Returns the meta object for the reference '{@link org.flowerplatform.web.entity.PerspectiveUserEntry#getUser <em>User</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>User</em>'.
	 * @see org.flowerplatform.web.entity.PerspectiveUserEntry#getUser()
	 * @see #getPerspectiveUserEntry()
	 * @generated
	 */
	EReference getPerspectiveUserEntry_User();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.PerspectiveUserEntry#getSerializedLayoutData <em>Serialized Layout Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Serialized Layout Data</em>'.
	 * @see org.flowerplatform.web.entity.PerspectiveUserEntry#getSerializedLayoutData()
	 * @see #getPerspectiveUserEntry()
	 * @generated
	 */
	EAttribute getPerspectiveUserEntry_SerializedLayoutData();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.web.entity.FavoriteItem <em>Favorite Item</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Favorite Item</em>'.
	 * @see org.flowerplatform.web.entity.FavoriteItem
	 * @generated
	 */
	EClass getFavoriteItem();

	/**
	 * Returns the meta object for the reference '{@link org.flowerplatform.web.entity.FavoriteItem#getUser <em>User</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>User</em>'.
	 * @see org.flowerplatform.web.entity.FavoriteItem#getUser()
	 * @see #getFavoriteItem()
	 * @generated
	 */
	EReference getFavoriteItem_User();

	/**
	 * Returns the meta object for the reference '{@link org.flowerplatform.web.entity.FavoriteItem#getResource <em>Resource</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Resource</em>'.
	 * @see org.flowerplatform.web.entity.FavoriteItem#getResource()
	 * @see #getFavoriteItem()
	 * @generated
	 */
	EReference getFavoriteItem_Resource();

	/**
	 * Returns the meta object for the reference '{@link org.flowerplatform.web.entity.FavoriteItem#getOrganization <em>Organization</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Organization</em>'.
	 * @see org.flowerplatform.web.entity.FavoriteItem#getOrganization()
	 * @see #getFavoriteItem()
	 * @generated
	 */
	EReference getFavoriteItem_Organization();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.FavoriteItem#getCategory <em>Category</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Category</em>'.
	 * @see org.flowerplatform.web.entity.FavoriteItem#getCategory()
	 * @see #getFavoriteItem()
	 * @generated
	 */
	EAttribute getFavoriteItem_Category();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.web.entity.RecentResource <em>Recent Resource</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Recent Resource</em>'.
	 * @see org.flowerplatform.web.entity.RecentResource
	 * @generated
	 */
	EClass getRecentResource();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.RecentResource#getLabel <em>Label</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Label</em>'.
	 * @see org.flowerplatform.web.entity.RecentResource#getLabel()
	 * @see #getRecentResource()
	 * @generated
	 */
	EAttribute getRecentResource_Label();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.RecentResource#getPath <em>Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Path</em>'.
	 * @see org.flowerplatform.web.entity.RecentResource#getPath()
	 * @see #getRecentResource()
	 * @generated
	 */
	EAttribute getRecentResource_Path();

	/**
	 * Returns the meta object for the reference '{@link org.flowerplatform.web.entity.RecentResource#getOrganization <em>Organization</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Organization</em>'.
	 * @see org.flowerplatform.web.entity.RecentResource#getOrganization()
	 * @see #getRecentResource()
	 * @generated
	 */
	EReference getRecentResource_Organization();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.RecentResource#getLastAccess <em>Last Access</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Last Access</em>'.
	 * @see org.flowerplatform.web.entity.RecentResource#getLastAccess()
	 * @see #getRecentResource()
	 * @generated
	 */
	EAttribute getRecentResource_LastAccess();

	/**
	 * Returns the meta object for the reference '{@link org.flowerplatform.web.entity.RecentResource#getLastAccessUser <em>Last Access User</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Last Access User</em>'.
	 * @see org.flowerplatform.web.entity.RecentResource#getLastAccessUser()
	 * @see #getRecentResource()
	 * @generated
	 */
	EReference getRecentResource_LastAccessUser();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.RecentResource#getLastSave <em>Last Save</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Last Save</em>'.
	 * @see org.flowerplatform.web.entity.RecentResource#getLastSave()
	 * @see #getRecentResource()
	 * @generated
	 */
	EAttribute getRecentResource_LastSave();

	/**
	 * Returns the meta object for the reference '{@link org.flowerplatform.web.entity.RecentResource#getLastSaveUser <em>Last Save User</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Last Save User</em>'.
	 * @see org.flowerplatform.web.entity.RecentResource#getLastSaveUser()
	 * @see #getRecentResource()
	 * @generated
	 */
	EReference getRecentResource_LastSaveUser();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.web.entity.AuditEntry <em>Audit Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Audit Entry</em>'.
	 * @see org.flowerplatform.web.entity.AuditEntry
	 * @generated
	 */
	EClass getAuditEntry();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.AuditEntry#getTimestamp <em>Timestamp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Timestamp</em>'.
	 * @see org.flowerplatform.web.entity.AuditEntry#getTimestamp()
	 * @see #getAuditEntry()
	 * @generated
	 */
	EAttribute getAuditEntry_Timestamp();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.AuditEntry#getUsername <em>Username</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Username</em>'.
	 * @see org.flowerplatform.web.entity.AuditEntry#getUsername()
	 * @see #getAuditEntry()
	 * @generated
	 */
	EAttribute getAuditEntry_Username();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.AuditEntry#getIpAddress <em>Ip Address</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ip Address</em>'.
	 * @see org.flowerplatform.web.entity.AuditEntry#getIpAddress()
	 * @see #getAuditEntry()
	 * @generated
	 */
	EAttribute getAuditEntry_IpAddress();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.AuditEntry#getAuditCategory <em>Audit Category</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Audit Category</em>'.
	 * @see org.flowerplatform.web.entity.AuditEntry#getAuditCategory()
	 * @see #getAuditEntry()
	 * @generated
	 */
	EAttribute getAuditEntry_AuditCategory();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.AuditEntry#getDuration <em>Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Duration</em>'.
	 * @see org.flowerplatform.web.entity.AuditEntry#getDuration()
	 * @see #getAuditEntry()
	 * @generated
	 */
	EAttribute getAuditEntry_Duration();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.AuditEntry#getParam0 <em>Param0</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Param0</em>'.
	 * @see org.flowerplatform.web.entity.AuditEntry#getParam0()
	 * @see #getAuditEntry()
	 * @generated
	 */
	EAttribute getAuditEntry_Param0();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.AuditEntry#getParam1 <em>Param1</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Param1</em>'.
	 * @see org.flowerplatform.web.entity.AuditEntry#getParam1()
	 * @see #getAuditEntry()
	 * @generated
	 */
	EAttribute getAuditEntry_Param1();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.AuditEntry#getParam2 <em>Param2</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Param2</em>'.
	 * @see org.flowerplatform.web.entity.AuditEntry#getParam2()
	 * @see #getAuditEntry()
	 * @generated
	 */
	EAttribute getAuditEntry_Param2();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.web.entity.DBVersion <em>DB Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>DB Version</em>'.
	 * @see org.flowerplatform.web.entity.DBVersion
	 * @generated
	 */
	EClass getDBVersion();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.DBVersion#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.flowerplatform.web.entity.DBVersion#getId()
	 * @see #getDBVersion()
	 * @generated
	 */
	EAttribute getDBVersion_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.DBVersion#getDbVersion <em>Db Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Db Version</em>'.
	 * @see org.flowerplatform.web.entity.DBVersion#getDbVersion()
	 * @see #getDBVersion()
	 * @generated
	 */
	EAttribute getDBVersion_DbVersion();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.web.entity.WorkingDirectory <em>Working Directory</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Working Directory</em>'.
	 * @see org.flowerplatform.web.entity.WorkingDirectory
	 * @generated
	 */
	EClass getWorkingDirectory();

	/**
	 * Returns the meta object for the reference '{@link org.flowerplatform.web.entity.WorkingDirectory#getOrganization <em>Organization</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Organization</em>'.
	 * @see org.flowerplatform.web.entity.WorkingDirectory#getOrganization()
	 * @see #getWorkingDirectory()
	 * @generated
	 */
	EReference getWorkingDirectory_Organization();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.WorkingDirectory#getPathFromOrganization <em>Path From Organization</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Path From Organization</em>'.
	 * @see org.flowerplatform.web.entity.WorkingDirectory#getPathFromOrganization()
	 * @see #getWorkingDirectory()
	 * @generated
	 */
	EAttribute getWorkingDirectory_PathFromOrganization();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.web.entity.WorkingDirectory#getColor <em>Color</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Color</em>'.
	 * @see org.flowerplatform.web.entity.WorkingDirectory#getColor()
	 * @see #getWorkingDirectory()
	 * @generated
	 */
	EAttribute getWorkingDirectory_Color();

	/**
	 * Returns the meta object for enum '{@link org.flowerplatform.web.entity.OrganizationMembershipStatus <em>Organization Membership Status</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Organization Membership Status</em>'.
	 * @see org.flowerplatform.web.entity.OrganizationMembershipStatus
	 * @generated
	 */
	EEnum getOrganizationMembershipStatus();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	EntityFactory getEntityFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.flowerplatform.web.entity.impl.EntityImpl <em>Entity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.web.entity.impl.EntityImpl
		 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getEntity()
		 * @generated
		 */
		EClass ENTITY = eINSTANCE.getEntity();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENTITY__ID = eINSTANCE.getEntity_Id();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.web.entity.impl.NamedEntityImpl <em>Named Entity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.web.entity.impl.NamedEntityImpl
		 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getNamedEntity()
		 * @generated
		 */
		EClass NAMED_ENTITY = eINSTANCE.getNamedEntity();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NAMED_ENTITY__NAME = eINSTANCE.getNamedEntity_Name();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.web.entity.ISecurityEntity <em>ISecurity Entity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.web.entity.ISecurityEntity
		 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getISecurityEntity()
		 * @generated
		 */
		EClass ISECURITY_ENTITY = eINSTANCE.getISecurityEntity();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.web.entity.impl.OrganizationImpl <em>Organization</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.web.entity.impl.OrganizationImpl
		 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getOrganization()
		 * @generated
		 */
		EClass ORGANIZATION = eINSTANCE.getOrganization();

		/**
		 * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ORGANIZATION__LABEL = eINSTANCE.getOrganization_Label();

		/**
		 * The meta object literal for the '<em><b>URL</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ORGANIZATION__URL = eINSTANCE.getOrganization_URL();

		/**
		 * The meta object literal for the '<em><b>Logo URL</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ORGANIZATION__LOGO_URL = eINSTANCE.getOrganization_LogoURL();

		/**
		 * The meta object literal for the '<em><b>Icon URL</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ORGANIZATION__ICON_URL = eINSTANCE.getOrganization_IconURL();

		/**
		 * The meta object literal for the '<em><b>Activated</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ORGANIZATION__ACTIVATED = eINSTANCE.getOrganization_Activated();

		/**
		 * The meta object literal for the '<em><b>Groups</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ORGANIZATION__GROUPS = eINSTANCE.getOrganization_Groups();

		/**
		 * The meta object literal for the '<em><b>Organization Users</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ORGANIZATION__ORGANIZATION_USERS = eINSTANCE.getOrganization_OrganizationUsers();

		/**
		 * The meta object literal for the '<em><b>Svn Repository UR Ls</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ORGANIZATION__SVN_REPOSITORY_UR_LS = eINSTANCE.getOrganization_SvnRepositoryURLs();

		/**
		 * The meta object literal for the '<em><b>Projects Count</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ORGANIZATION__PROJECTS_COUNT = eINSTANCE.getOrganization_ProjectsCount();

		/**
		 * The meta object literal for the '<em><b>Files Count</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ORGANIZATION__FILES_COUNT = eINSTANCE.getOrganization_FilesCount();

		/**
		 * The meta object literal for the '<em><b>Models Count</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ORGANIZATION__MODELS_COUNT = eINSTANCE.getOrganization_ModelsCount();

		/**
		 * The meta object literal for the '<em><b>Diagrams Count</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ORGANIZATION__DIAGRAMS_COUNT = eINSTANCE.getOrganization_DiagramsCount();

		/**
		 * The meta object literal for the '<em><b>Working Directories</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ORGANIZATION__WORKING_DIRECTORIES = eINSTANCE.getOrganization_WorkingDirectories();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.web.entity.impl.GroupImpl <em>Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.web.entity.impl.GroupImpl
		 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getGroup()
		 * @generated
		 */
		EClass GROUP = eINSTANCE.getGroup();

		/**
		 * The meta object literal for the '<em><b>Organization</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GROUP__ORGANIZATION = eINSTANCE.getGroup_Organization();

		/**
		 * The meta object literal for the '<em><b>Group Users</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GROUP__GROUP_USERS = eINSTANCE.getGroup_GroupUsers();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.web.entity.impl.UserImpl <em>User</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.web.entity.impl.UserImpl
		 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getUser()
		 * @generated
		 */
		EClass USER = eINSTANCE.getUser();

		/**
		 * The meta object literal for the '<em><b>Login</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USER__LOGIN = eINSTANCE.getUser_Login();

		/**
		 * The meta object literal for the '<em><b>Hashed Password</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USER__HASHED_PASSWORD = eINSTANCE.getUser_HashedPassword();

		/**
		 * The meta object literal for the '<em><b>Email</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USER__EMAIL = eINSTANCE.getUser_Email();

		/**
		 * The meta object literal for the '<em><b>Activated</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USER__ACTIVATED = eINSTANCE.getUser_Activated();

		/**
		 * The meta object literal for the '<em><b>Activation Code</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USER__ACTIVATION_CODE = eINSTANCE.getUser_ActivationCode();

		/**
		 * The meta object literal for the '<em><b>Group Users</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference USER__GROUP_USERS = eINSTANCE.getUser_GroupUsers();

		/**
		 * The meta object literal for the '<em><b>Organization Users</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference USER__ORGANIZATION_USERS = eINSTANCE.getUser_OrganizationUsers();

		/**
		 * The meta object literal for the '<em><b>Perspective User Entries</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference USER__PERSPECTIVE_USER_ENTRIES = eINSTANCE.getUser_PerspectiveUserEntries();

		/**
		 * The meta object literal for the '<em><b>Last Perspective</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USER__LAST_PERSPECTIVE = eINSTANCE.getUser_LastPerspective();

		/**
		 * The meta object literal for the '<em><b>Svn Comments</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference USER__SVN_COMMENTS = eINSTANCE.getUser_SvnComments();

		/**
		 * The meta object literal for the '<em><b>Favorite Items</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference USER__FAVORITE_ITEMS = eINSTANCE.getUser_FavoriteItems();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.web.entity.impl.OrganizationUserImpl <em>Organization User</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.web.entity.impl.OrganizationUserImpl
		 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getOrganizationUser()
		 * @generated
		 */
		EClass ORGANIZATION_USER = eINSTANCE.getOrganizationUser();

		/**
		 * The meta object literal for the '<em><b>Organization</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ORGANIZATION_USER__ORGANIZATION = eINSTANCE.getOrganizationUser_Organization();

		/**
		 * The meta object literal for the '<em><b>User</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ORGANIZATION_USER__USER = eINSTANCE.getOrganizationUser_User();

		/**
		 * The meta object literal for the '<em><b>Status</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ORGANIZATION_USER__STATUS = eINSTANCE.getOrganizationUser_Status();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.web.entity.impl.GroupUserImpl <em>Group User</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.web.entity.impl.GroupUserImpl
		 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getGroupUser()
		 * @generated
		 */
		EClass GROUP_USER = eINSTANCE.getGroupUser();

		/**
		 * The meta object literal for the '<em><b>Group</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GROUP_USER__GROUP = eINSTANCE.getGroupUser_Group();

		/**
		 * The meta object literal for the '<em><b>User</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GROUP_USER__USER = eINSTANCE.getGroupUser_User();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.web.entity.impl.PermissionEntityImpl <em>Permission Entity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.web.entity.impl.PermissionEntityImpl
		 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getPermissionEntity()
		 * @generated
		 */
		EClass PERMISSION_ENTITY = eINSTANCE.getPermissionEntity();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PERMISSION_ENTITY__TYPE = eINSTANCE.getPermissionEntity_Type();

		/**
		 * The meta object literal for the '<em><b>Actions</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PERMISSION_ENTITY__ACTIONS = eINSTANCE.getPermissionEntity_Actions();

		/**
		 * The meta object literal for the '<em><b>Assigned To</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PERMISSION_ENTITY__ASSIGNED_TO = eINSTANCE.getPermissionEntity_AssignedTo();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.web.entity.impl.SVNRepositoryURLEntityImpl <em>SVN Repository URL Entity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.web.entity.impl.SVNRepositoryURLEntityImpl
		 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getSVNRepositoryURLEntity()
		 * @generated
		 */
		EClass SVN_REPOSITORY_URL_ENTITY = eINSTANCE.getSVNRepositoryURLEntity();

		/**
		 * The meta object literal for the '<em><b>Organization</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SVN_REPOSITORY_URL_ENTITY__ORGANIZATION = eINSTANCE.getSVNRepositoryURLEntity_Organization();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.web.entity.impl.SVNCommentEntityImpl <em>SVN Comment Entity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.web.entity.impl.SVNCommentEntityImpl
		 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getSVNCommentEntity()
		 * @generated
		 */
		EClass SVN_COMMENT_ENTITY = eINSTANCE.getSVNCommentEntity();

		/**
		 * The meta object literal for the '<em><b>Body</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SVN_COMMENT_ENTITY__BODY = eINSTANCE.getSVNCommentEntity_Body();

		/**
		 * The meta object literal for the '<em><b>User</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SVN_COMMENT_ENTITY__USER = eINSTANCE.getSVNCommentEntity_User();

		/**
		 * The meta object literal for the '<em><b>Timestamp</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SVN_COMMENT_ENTITY__TIMESTAMP = eINSTANCE.getSVNCommentEntity_Timestamp();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.web.entity.impl.PerspectiveUserEntryImpl <em>Perspective User Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.web.entity.impl.PerspectiveUserEntryImpl
		 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getPerspectiveUserEntry()
		 * @generated
		 */
		EClass PERSPECTIVE_USER_ENTRY = eINSTANCE.getPerspectiveUserEntry();

		/**
		 * The meta object literal for the '<em><b>User</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PERSPECTIVE_USER_ENTRY__USER = eINSTANCE.getPerspectiveUserEntry_User();

		/**
		 * The meta object literal for the '<em><b>Serialized Layout Data</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PERSPECTIVE_USER_ENTRY__SERIALIZED_LAYOUT_DATA = eINSTANCE.getPerspectiveUserEntry_SerializedLayoutData();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.web.entity.impl.FavoriteItemImpl <em>Favorite Item</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.web.entity.impl.FavoriteItemImpl
		 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getFavoriteItem()
		 * @generated
		 */
		EClass FAVORITE_ITEM = eINSTANCE.getFavoriteItem();

		/**
		 * The meta object literal for the '<em><b>User</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FAVORITE_ITEM__USER = eINSTANCE.getFavoriteItem_User();

		/**
		 * The meta object literal for the '<em><b>Resource</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FAVORITE_ITEM__RESOURCE = eINSTANCE.getFavoriteItem_Resource();

		/**
		 * The meta object literal for the '<em><b>Organization</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FAVORITE_ITEM__ORGANIZATION = eINSTANCE.getFavoriteItem_Organization();

		/**
		 * The meta object literal for the '<em><b>Category</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FAVORITE_ITEM__CATEGORY = eINSTANCE.getFavoriteItem_Category();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.web.entity.impl.RecentResourceImpl <em>Recent Resource</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.web.entity.impl.RecentResourceImpl
		 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getRecentResource()
		 * @generated
		 */
		EClass RECENT_RESOURCE = eINSTANCE.getRecentResource();

		/**
		 * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RECENT_RESOURCE__LABEL = eINSTANCE.getRecentResource_Label();

		/**
		 * The meta object literal for the '<em><b>Path</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RECENT_RESOURCE__PATH = eINSTANCE.getRecentResource_Path();

		/**
		 * The meta object literal for the '<em><b>Organization</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RECENT_RESOURCE__ORGANIZATION = eINSTANCE.getRecentResource_Organization();

		/**
		 * The meta object literal for the '<em><b>Last Access</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RECENT_RESOURCE__LAST_ACCESS = eINSTANCE.getRecentResource_LastAccess();

		/**
		 * The meta object literal for the '<em><b>Last Access User</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RECENT_RESOURCE__LAST_ACCESS_USER = eINSTANCE.getRecentResource_LastAccessUser();

		/**
		 * The meta object literal for the '<em><b>Last Save</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RECENT_RESOURCE__LAST_SAVE = eINSTANCE.getRecentResource_LastSave();

		/**
		 * The meta object literal for the '<em><b>Last Save User</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RECENT_RESOURCE__LAST_SAVE_USER = eINSTANCE.getRecentResource_LastSaveUser();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.web.entity.impl.AuditEntryImpl <em>Audit Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.web.entity.impl.AuditEntryImpl
		 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getAuditEntry()
		 * @generated
		 */
		EClass AUDIT_ENTRY = eINSTANCE.getAuditEntry();

		/**
		 * The meta object literal for the '<em><b>Timestamp</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AUDIT_ENTRY__TIMESTAMP = eINSTANCE.getAuditEntry_Timestamp();

		/**
		 * The meta object literal for the '<em><b>Username</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AUDIT_ENTRY__USERNAME = eINSTANCE.getAuditEntry_Username();

		/**
		 * The meta object literal for the '<em><b>Ip Address</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AUDIT_ENTRY__IP_ADDRESS = eINSTANCE.getAuditEntry_IpAddress();

		/**
		 * The meta object literal for the '<em><b>Audit Category</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AUDIT_ENTRY__AUDIT_CATEGORY = eINSTANCE.getAuditEntry_AuditCategory();

		/**
		 * The meta object literal for the '<em><b>Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AUDIT_ENTRY__DURATION = eINSTANCE.getAuditEntry_Duration();

		/**
		 * The meta object literal for the '<em><b>Param0</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AUDIT_ENTRY__PARAM0 = eINSTANCE.getAuditEntry_Param0();

		/**
		 * The meta object literal for the '<em><b>Param1</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AUDIT_ENTRY__PARAM1 = eINSTANCE.getAuditEntry_Param1();

		/**
		 * The meta object literal for the '<em><b>Param2</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AUDIT_ENTRY__PARAM2 = eINSTANCE.getAuditEntry_Param2();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.web.entity.impl.DBVersionImpl <em>DB Version</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.web.entity.impl.DBVersionImpl
		 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getDBVersion()
		 * @generated
		 */
		EClass DB_VERSION = eINSTANCE.getDBVersion();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DB_VERSION__ID = eINSTANCE.getDBVersion_Id();

		/**
		 * The meta object literal for the '<em><b>Db Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DB_VERSION__DB_VERSION = eINSTANCE.getDBVersion_DbVersion();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.web.entity.impl.WorkingDirectoryImpl <em>Working Directory</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.web.entity.impl.WorkingDirectoryImpl
		 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getWorkingDirectory()
		 * @generated
		 */
		EClass WORKING_DIRECTORY = eINSTANCE.getWorkingDirectory();

		/**
		 * The meta object literal for the '<em><b>Organization</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference WORKING_DIRECTORY__ORGANIZATION = eINSTANCE.getWorkingDirectory_Organization();

		/**
		 * The meta object literal for the '<em><b>Path From Organization</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WORKING_DIRECTORY__PATH_FROM_ORGANIZATION = eINSTANCE.getWorkingDirectory_PathFromOrganization();

		/**
		 * The meta object literal for the '<em><b>Color</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WORKING_DIRECTORY__COLOR = eINSTANCE.getWorkingDirectory_Color();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.web.entity.OrganizationMembershipStatus <em>Organization Membership Status</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.web.entity.OrganizationMembershipStatus
		 * @see org.flowerplatform.web.entity.impl.EntityPackageImpl#getOrganizationMembershipStatus()
		 * @generated
		 */
		EEnum ORGANIZATION_MEMBERSHIP_STATUS = eINSTANCE.getOrganizationMembershipStatus();

	}

} //EntityPackage
