/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.web.entity;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Organization User</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.web.entity.OrganizationUser#getOrganization <em>Organization</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.OrganizationUser#getUser <em>User</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.OrganizationUser#getStatus <em>Status</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.web.entity.EntityPackage#getOrganizationUser()
 * @model
 * @generated
 */
public interface OrganizationUser extends Entity {
	/**
	 * Returns the value of the '<em><b>Organization</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.flowerplatform.web.entity.Organization#getOrganizationUsers <em>Organization Users</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Organization</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Organization</em>' reference.
	 * @see #setOrganization(Organization)
	 * @see org.flowerplatform.web.entity.EntityPackage#getOrganizationUser_Organization()
	 * @see org.flowerplatform.web.entity.Organization#getOrganizationUsers
	 * @model opposite="organizationUsers"
	 * @generated
	 */
	Organization getOrganization();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.OrganizationUser#getOrganization <em>Organization</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Organization</em>' reference.
	 * @see #getOrganization()
	 * @generated
	 */
	void setOrganization(Organization value);

	/**
	 * Returns the value of the '<em><b>User</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.flowerplatform.web.entity.User#getOrganizationUsers <em>Organization Users</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>User</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>User</em>' reference.
	 * @see #setUser(User)
	 * @see org.flowerplatform.web.entity.EntityPackage#getOrganizationUser_User()
	 * @see org.flowerplatform.web.entity.User#getOrganizationUsers
	 * @model opposite="organizationUsers"
	 * @generated
	 */
	User getUser();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.OrganizationUser#getUser <em>User</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>User</em>' reference.
	 * @see #getUser()
	 * @generated
	 */
	void setUser(User value);

	/**
	 * Returns the value of the '<em><b>Status</b></em>' attribute.
	 * The literals are from the enumeration {@link org.flowerplatform.web.entity.OrganizationMembershipStatus}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Status</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Status</em>' attribute.
	 * @see org.flowerplatform.web.entity.OrganizationMembershipStatus
	 * @see #setStatus(OrganizationMembershipStatus)
	 * @see org.flowerplatform.web.entity.EntityPackage#getOrganizationUser_Status()
	 * @model
	 * @generated
	 */
	OrganizationMembershipStatus getStatus();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.OrganizationUser#getStatus <em>Status</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Status</em>' attribute.
	 * @see org.flowerplatform.web.entity.OrganizationMembershipStatus
	 * @see #getStatus()
	 * @generated
	 */
	void setStatus(OrganizationMembershipStatus value);

} // OrganizationUser
