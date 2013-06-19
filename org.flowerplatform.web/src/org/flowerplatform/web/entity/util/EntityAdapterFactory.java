/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.web.entity.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.flowerplatform.web.entity.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.flowerplatform.web.entity.EntityPackage
 * @generated
 */
public class EntityAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static EntityPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntityAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = EntityPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EntitySwitch<Adapter> modelSwitch =
		new EntitySwitch<Adapter>() {
			@Override
			public Adapter caseEntity(Entity object) {
				return createEntityAdapter();
			}
			@Override
			public Adapter caseNamedEntity(NamedEntity object) {
				return createNamedEntityAdapter();
			}
			@Override
			public Adapter caseISecurityEntity(ISecurityEntity object) {
				return createISecurityEntityAdapter();
			}
			@Override
			public Adapter caseOrganization(Organization object) {
				return createOrganizationAdapter();
			}
			@Override
			public Adapter caseGroup(Group object) {
				return createGroupAdapter();
			}
			@Override
			public Adapter caseUser(User object) {
				return createUserAdapter();
			}
			@Override
			public Adapter caseOrganizationUser(OrganizationUser object) {
				return createOrganizationUserAdapter();
			}
			@Override
			public Adapter caseGroupUser(GroupUser object) {
				return createGroupUserAdapter();
			}
			@Override
			public Adapter casePermissionEntity(PermissionEntity object) {
				return createPermissionEntityAdapter();
			}
			@Override
			public Adapter caseSVNRepositoryURLEntity(SVNRepositoryURLEntity object) {
				return createSVNRepositoryURLEntityAdapter();
			}
			@Override
			public Adapter caseSVNCommentEntity(SVNCommentEntity object) {
				return createSVNCommentEntityAdapter();
			}
			@Override
			public Adapter casePerspectiveUserEntry(PerspectiveUserEntry object) {
				return createPerspectiveUserEntryAdapter();
			}
			@Override
			public Adapter caseFavoriteItem(FavoriteItem object) {
				return createFavoriteItemAdapter();
			}
			@Override
			public Adapter caseRecentResource(RecentResource object) {
				return createRecentResourceAdapter();
			}
			@Override
			public Adapter caseAuditEntry(AuditEntry object) {
				return createAuditEntryAdapter();
			}
			@Override
			public Adapter caseDBVersion(DBVersion object) {
				return createDBVersionAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link org.flowerplatform.web.entity.Entity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.flowerplatform.web.entity.Entity
	 * @generated
	 */
	public Adapter createEntityAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.flowerplatform.web.entity.NamedEntity <em>Named Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.flowerplatform.web.entity.NamedEntity
	 * @generated
	 */
	public Adapter createNamedEntityAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.flowerplatform.web.entity.ISecurityEntity <em>ISecurity Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.flowerplatform.web.entity.ISecurityEntity
	 * @generated
	 */
	public Adapter createISecurityEntityAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.flowerplatform.web.entity.Organization <em>Organization</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.flowerplatform.web.entity.Organization
	 * @generated
	 */
	public Adapter createOrganizationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.flowerplatform.web.entity.Group <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.flowerplatform.web.entity.Group
	 * @generated
	 */
	public Adapter createGroupAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.flowerplatform.web.entity.User <em>User</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.flowerplatform.web.entity.User
	 * @generated
	 */
	public Adapter createUserAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.flowerplatform.web.entity.OrganizationUser <em>Organization User</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.flowerplatform.web.entity.OrganizationUser
	 * @generated
	 */
	public Adapter createOrganizationUserAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.flowerplatform.web.entity.GroupUser <em>Group User</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.flowerplatform.web.entity.GroupUser
	 * @generated
	 */
	public Adapter createGroupUserAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.flowerplatform.web.entity.PermissionEntity <em>Permission Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.flowerplatform.web.entity.PermissionEntity
	 * @generated
	 */
	public Adapter createPermissionEntityAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.flowerplatform.web.entity.SVNRepositoryURLEntity <em>SVN Repository URL Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.flowerplatform.web.entity.SVNRepositoryURLEntity
	 * @generated
	 */
	public Adapter createSVNRepositoryURLEntityAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.flowerplatform.web.entity.SVNCommentEntity <em>SVN Comment Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.flowerplatform.web.entity.SVNCommentEntity
	 * @generated
	 */
	public Adapter createSVNCommentEntityAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.flowerplatform.web.entity.PerspectiveUserEntry <em>Perspective User Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.flowerplatform.web.entity.PerspectiveUserEntry
	 * @generated
	 */
	public Adapter createPerspectiveUserEntryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.flowerplatform.web.entity.FavoriteItem <em>Favorite Item</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.flowerplatform.web.entity.FavoriteItem
	 * @generated
	 */
	public Adapter createFavoriteItemAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.flowerplatform.web.entity.RecentResource <em>Recent Resource</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.flowerplatform.web.entity.RecentResource
	 * @generated
	 */
	public Adapter createRecentResourceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.flowerplatform.web.entity.AuditEntry <em>Audit Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.flowerplatform.web.entity.AuditEntry
	 * @generated
	 */
	public Adapter createAuditEntryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.flowerplatform.web.entity.DBVersion <em>DB Version</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.flowerplatform.web.entity.DBVersion
	 * @generated
	 */
	public Adapter createDBVersionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //EntityAdapterFactory
