/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.web.entity.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.flowerplatform.web.entity.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.flowerplatform.web.entity.EntityPackage
 * @generated
 */
public class EntitySwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static EntityPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntitySwitch() {
		if (modelPackage == null) {
			modelPackage = EntityPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @parameter ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case EntityPackage.ENTITY: {
				Entity entity = (Entity)theEObject;
				T result = caseEntity(entity);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EntityPackage.NAMED_ENTITY: {
				NamedEntity namedEntity = (NamedEntity)theEObject;
				T result = caseNamedEntity(namedEntity);
				if (result == null) result = caseEntity(namedEntity);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EntityPackage.ISECURITY_ENTITY: {
				ISecurityEntity iSecurityEntity = (ISecurityEntity)theEObject;
				T result = caseISecurityEntity(iSecurityEntity);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EntityPackage.ORGANIZATION: {
				Organization organization = (Organization)theEObject;
				T result = caseOrganization(organization);
				if (result == null) result = caseNamedEntity(organization);
				if (result == null) result = caseISecurityEntity(organization);
				if (result == null) result = caseEntity(organization);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EntityPackage.GROUP: {
				Group group = (Group)theEObject;
				T result = caseGroup(group);
				if (result == null) result = caseNamedEntity(group);
				if (result == null) result = caseISecurityEntity(group);
				if (result == null) result = caseEntity(group);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EntityPackage.USER: {
				User user = (User)theEObject;
				T result = caseUser(user);
				if (result == null) result = caseNamedEntity(user);
				if (result == null) result = caseISecurityEntity(user);
				if (result == null) result = caseEntity(user);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EntityPackage.ORGANIZATION_USER: {
				OrganizationUser organizationUser = (OrganizationUser)theEObject;
				T result = caseOrganizationUser(organizationUser);
				if (result == null) result = caseEntity(organizationUser);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EntityPackage.GROUP_USER: {
				GroupUser groupUser = (GroupUser)theEObject;
				T result = caseGroupUser(groupUser);
				if (result == null) result = caseEntity(groupUser);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EntityPackage.PERMISSION_ENTITY: {
				PermissionEntity permissionEntity = (PermissionEntity)theEObject;
				T result = casePermissionEntity(permissionEntity);
				if (result == null) result = caseNamedEntity(permissionEntity);
				if (result == null) result = caseEntity(permissionEntity);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EntityPackage.SVN_REPOSITORY_URL_ENTITY: {
				SVNRepositoryURLEntity svnRepositoryURLEntity = (SVNRepositoryURLEntity)theEObject;
				T result = caseSVNRepositoryURLEntity(svnRepositoryURLEntity);
				if (result == null) result = caseNamedEntity(svnRepositoryURLEntity);
				if (result == null) result = caseEntity(svnRepositoryURLEntity);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EntityPackage.SVN_COMMENT_ENTITY: {
				SVNCommentEntity svnCommentEntity = (SVNCommentEntity)theEObject;
				T result = caseSVNCommentEntity(svnCommentEntity);
				if (result == null) result = caseEntity(svnCommentEntity);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EntityPackage.PERSPECTIVE_USER_ENTRY: {
				PerspectiveUserEntry perspectiveUserEntry = (PerspectiveUserEntry)theEObject;
				T result = casePerspectiveUserEntry(perspectiveUserEntry);
				if (result == null) result = caseNamedEntity(perspectiveUserEntry);
				if (result == null) result = caseEntity(perspectiveUserEntry);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EntityPackage.FAVORITE_ITEM: {
				FavoriteItem favoriteItem = (FavoriteItem)theEObject;
				T result = caseFavoriteItem(favoriteItem);
				if (result == null) result = caseEntity(favoriteItem);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EntityPackage.RECENT_RESOURCE: {
				RecentResource recentResource = (RecentResource)theEObject;
				T result = caseRecentResource(recentResource);
				if (result == null) result = caseEntity(recentResource);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EntityPackage.AUDIT_ENTRY: {
				AuditEntry auditEntry = (AuditEntry)theEObject;
				T result = caseAuditEntry(auditEntry);
				if (result == null) result = caseEntity(auditEntry);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EntityPackage.DB_VERSION: {
				DBVersion dbVersion = (DBVersion)theEObject;
				T result = caseDBVersion(dbVersion);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Entity</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Entity</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEntity(Entity object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Named Entity</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Named Entity</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNamedEntity(NamedEntity object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ISecurity Entity</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ISecurity Entity</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseISecurityEntity(ISecurityEntity object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Organization</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Organization</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOrganization(Organization object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Group</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Group</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGroup(Group object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>User</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>User</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUser(User object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Organization User</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Organization User</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOrganizationUser(OrganizationUser object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Group User</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Group User</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGroupUser(GroupUser object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Permission Entity</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Permission Entity</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePermissionEntity(PermissionEntity object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>SVN Repository URL Entity</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>SVN Repository URL Entity</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSVNRepositoryURLEntity(SVNRepositoryURLEntity object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>SVN Comment Entity</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>SVN Comment Entity</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSVNCommentEntity(SVNCommentEntity object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Perspective User Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Perspective User Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePerspectiveUserEntry(PerspectiveUserEntry object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Favorite Item</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Favorite Item</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFavoriteItem(FavoriteItem object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Recent Resource</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Recent Resource</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRecentResource(RecentResource object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Audit Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Audit Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAuditEntry(AuditEntry object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>DB Version</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>DB Version</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDBVersion(DBVersion object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} //EntitySwitch
