/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.web.entity;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Group User</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.web.entity.GroupUser#getGroup <em>Group</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.GroupUser#getUser <em>User</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.web.entity.EntityPackage#getGroupUser()
 * @model
 * @generated
 */
public interface GroupUser extends Entity {
	/**
	 * Returns the value of the '<em><b>Group</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Group</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group</em>' reference.
	 * @see #setGroup(Group)
	 * @see org.flowerplatform.web.entity.EntityPackage#getGroupUser_Group()
	 * @model
	 * @generated
	 */
	Group getGroup();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.GroupUser#getGroup <em>Group</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Group</em>' reference.
	 * @see #getGroup()
	 * @generated
	 */
	void setGroup(Group value);

	/**
	 * Returns the value of the '<em><b>User</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>User</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>User</em>' reference.
	 * @see #setUser(User)
	 * @see org.flowerplatform.web.entity.EntityPackage#getGroupUser_User()
	 * @model
	 * @generated
	 */
	User getUser();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.GroupUser#getUser <em>User</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>User</em>' reference.
	 * @see #getUser()
	 * @generated
	 */
	void setUser(User value);

} // GroupUser
