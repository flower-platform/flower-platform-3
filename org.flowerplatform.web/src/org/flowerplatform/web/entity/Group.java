/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.web.entity;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.web.entity.Group#getOrganization <em>Organization</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.Group#getGroupUsers <em>Group Users</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.web.entity.EntityPackage#getGroup()
 * @model
 * @generated
 */
public interface Group extends NamedEntity, ISecurityEntity {
	
	/**
	 * Represents a group that contains all users, inclusive anonymous user.
	 */
	public static final String ALL = "ALL";
	
	/**
	 * Returns the value of the '<em><b>Organization</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.flowerplatform.web.entity.Organization#getGroups <em>Groups</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Organization</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Organization</em>' reference.
	 * @see #setOrganization(Organization)
	 * @see org.flowerplatform.web.entity.EntityPackage#getGroup_Organization()
	 * @see org.flowerplatform.web.entity.Organization#getGroups
	 * @model opposite="groups"
	 * @generated
	 */
	Organization getOrganization();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.Group#getOrganization <em>Organization</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Organization</em>' reference.
	 * @see #getOrganization()
	 * @generated
	 */
	void setOrganization(Organization value);

	/**
	 * Returns the value of the '<em><b>Group Users</b></em>' reference list.
	 * The list contents are of type {@link org.flowerplatform.web.entity.GroupUser}.
	 * It is bidirectional and its opposite is '{@link org.flowerplatform.web.entity.GroupUser#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Group Users</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group Users</em>' reference list.
	 * @see org.flowerplatform.web.entity.EntityPackage#getGroup_GroupUsers()
	 * @see org.flowerplatform.web.entity.GroupUser#getGroup
	 * @model opposite="group"
	 * @generated
	 */
	EList<GroupUser> getGroupUsers();

} // Group
