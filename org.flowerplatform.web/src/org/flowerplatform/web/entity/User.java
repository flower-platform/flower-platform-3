/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
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
 * A representation of the model object '<em><b>User</b></em>'.
 * @implements org.flowerplatform.communication.IUser
 * 
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.web.entity.User#getLogin <em>Login</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.User#getHashedPassword <em>Hashed Password</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.User#getEmail <em>Email</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.User#isActivated <em>Activated</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.User#getActivationCode <em>Activation Code</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.User#getGroupUsers <em>Group Users</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.User#getOrganizationUsers <em>Organization Users</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.User#getPerspectiveUserEntries <em>Perspective User Entries</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.User#getLastPerspective <em>Last Perspective</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.User#getSvnComments <em>Svn Comments</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.User#getFavoriteItems <em>Favorite Items</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.web.entity.EntityPackage#getUser()
 * @model
 * @generated
 */
public interface User extends NamedEntity, ISecurityEntity, org.flowerplatform.communication.IUser {
	/**
	 * Returns the value of the '<em><b>Login</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Login</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Login</em>' attribute.
	 * @see #setLogin(String)
	 * @see org.flowerplatform.web.entity.EntityPackage#getUser_Login()
	 * @model
	 * @generated
	 */
	String getLogin();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.User#getLogin <em>Login</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Login</em>' attribute.
	 * @see #getLogin()
	 * @generated
	 */
	void setLogin(String value);

	/**
	 * Returns the value of the '<em><b>Hashed Password</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hashed Password</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hashed Password</em>' attribute.
	 * @see #setHashedPassword(String)
	 * @see org.flowerplatform.web.entity.EntityPackage#getUser_HashedPassword()
	 * @model
	 * @generated
	 */
	String getHashedPassword();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.User#getHashedPassword <em>Hashed Password</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hashed Password</em>' attribute.
	 * @see #getHashedPassword()
	 * @generated
	 */
	void setHashedPassword(String value);

	/**
	 * Returns the value of the '<em><b>Email</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Email</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Email</em>' attribute.
	 * @see #setEmail(String)
	 * @see org.flowerplatform.web.entity.EntityPackage#getUser_Email()
	 * @model
	 * @generated
	 */
	String getEmail();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.User#getEmail <em>Email</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Email</em>' attribute.
	 * @see #getEmail()
	 * @generated
	 */
	void setEmail(String value);

	/**
	 * Returns the value of the '<em><b>Activated</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Activated</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Activated</em>' attribute.
	 * @see #setActivated(boolean)
	 * @see org.flowerplatform.web.entity.EntityPackage#getUser_Activated()
	 * @model
	 * @generated
	 */
	boolean isActivated();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.User#isActivated <em>Activated</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Activated</em>' attribute.
	 * @see #isActivated()
	 * @generated
	 */
	void setActivated(boolean value);

	/**
	 * Returns the value of the '<em><b>Activation Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Activation Code</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Activation Code</em>' attribute.
	 * @see #setActivationCode(String)
	 * @see org.flowerplatform.web.entity.EntityPackage#getUser_ActivationCode()
	 * @model
	 * @generated
	 */
	String getActivationCode();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.User#getActivationCode <em>Activation Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Activation Code</em>' attribute.
	 * @see #getActivationCode()
	 * @generated
	 */
	void setActivationCode(String value);

	/**
	 * Returns the value of the '<em><b>Group Users</b></em>' reference list.
	 * The list contents are of type {@link org.flowerplatform.web.entity.GroupUser}.
	 * It is bidirectional and its opposite is '{@link org.flowerplatform.web.entity.GroupUser#getUser <em>User</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Group Users</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group Users</em>' reference list.
	 * @see org.flowerplatform.web.entity.EntityPackage#getUser_GroupUsers()
	 * @see org.flowerplatform.web.entity.GroupUser#getUser
	 * @model opposite="user"
	 * @generated
	 */
	EList<GroupUser> getGroupUsers();

	/**
	 * Returns the value of the '<em><b>Organization Users</b></em>' reference list.
	 * The list contents are of type {@link org.flowerplatform.web.entity.OrganizationUser}.
	 * It is bidirectional and its opposite is '{@link org.flowerplatform.web.entity.OrganizationUser#getUser <em>User</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Organization Users</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Organization Users</em>' reference list.
	 * @see org.flowerplatform.web.entity.EntityPackage#getUser_OrganizationUsers()
	 * @see org.flowerplatform.web.entity.OrganizationUser#getUser
	 * @model opposite="user"
	 * @generated
	 */
	EList<OrganizationUser> getOrganizationUsers();

	/**
	 * Returns the value of the '<em><b>Perspective User Entries</b></em>' reference list.
	 * The list contents are of type {@link org.flowerplatform.web.entity.PerspectiveUserEntry}.
	 * It is bidirectional and its opposite is '{@link org.flowerplatform.web.entity.PerspectiveUserEntry#getUser <em>User</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Perspective User Entries</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Perspective User Entries</em>' reference list.
	 * @see org.flowerplatform.web.entity.EntityPackage#getUser_PerspectiveUserEntries()
	 * @see org.flowerplatform.web.entity.PerspectiveUserEntry#getUser
	 * @model opposite="user"
	 * @generated
	 */
	EList<PerspectiveUserEntry> getPerspectiveUserEntries();

	/**
	 * Returns the value of the '<em><b>Last Perspective</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Last Perspective</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Last Perspective</em>' attribute.
	 * @see #setLastPerspective(String)
	 * @see org.flowerplatform.web.entity.EntityPackage#getUser_LastPerspective()
	 * @model
	 * @generated
	 */
	String getLastPerspective();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.User#getLastPerspective <em>Last Perspective</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Last Perspective</em>' attribute.
	 * @see #getLastPerspective()
	 * @generated
	 */
	void setLastPerspective(String value);

	/**
	 * Returns the value of the '<em><b>Svn Comments</b></em>' reference list.
	 * The list contents are of type {@link org.flowerplatform.web.entity.SVNCommentEntity}.
	 * It is bidirectional and its opposite is '{@link org.flowerplatform.web.entity.SVNCommentEntity#getUser <em>User</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Svn Comments</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Svn Comments</em>' reference list.
	 * @see org.flowerplatform.web.entity.EntityPackage#getUser_SvnComments()
	 * @see org.flowerplatform.web.entity.SVNCommentEntity#getUser
	 * @model opposite="user"
	 * @generated
	 */
	EList<SVNCommentEntity> getSvnComments();

	/**
	 * Returns the value of the '<em><b>Favorite Items</b></em>' reference list.
	 * The list contents are of type {@link org.flowerplatform.web.entity.FavoriteItem}.
	 * It is bidirectional and its opposite is '{@link org.flowerplatform.web.entity.FavoriteItem#getUser <em>User</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Favorite Items</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Favorite Items</em>' reference list.
	 * @see org.flowerplatform.web.entity.EntityPackage#getUser_FavoriteItems()
	 * @see org.flowerplatform.web.entity.FavoriteItem#getUser
	 * @model opposite="user"
	 * @generated
	 */
	EList<FavoriteItem> getFavoriteItems();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	FavoriteItem getFavoriteItem(Object item, int category);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	boolean isAdmin();

} // User