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


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Perspective User Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.web.entity.PerspectiveUserEntry#getUser <em>User</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.PerspectiveUserEntry#getSerializedLayoutData <em>Serialized Layout Data</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.web.entity.EntityPackage#getPerspectiveUserEntry()
 * @model
 * @generated
 */
public interface PerspectiveUserEntry extends NamedEntity {
	/**
	 * Returns the value of the '<em><b>User</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.flowerplatform.web.entity.User#getPerspectiveUserEntries <em>Perspective User Entries</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>User</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>User</em>' reference.
	 * @see #setUser(User)
	 * @see org.flowerplatform.web.entity.EntityPackage#getPerspectiveUserEntry_User()
	 * @see org.flowerplatform.web.entity.User#getPerspectiveUserEntries
	 * @model opposite="perspectiveUserEntries"
	 * @generated
	 */
	User getUser();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.PerspectiveUserEntry#getUser <em>User</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>User</em>' reference.
	 * @see #getUser()
	 * @generated
	 */
	void setUser(User value);

	/**
	 * Returns the value of the '<em><b>Serialized Layout Data</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Serialized Layout Data</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Serialized Layout Data</em>' attribute.
	 * @see #setSerializedLayoutData(String)
	 * @see org.flowerplatform.web.entity.EntityPackage#getPerspectiveUserEntry_SerializedLayoutData()
	 * @model
	 * @generated
	 */
	String getSerializedLayoutData();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.PerspectiveUserEntry#getSerializedLayoutData <em>Serialized Layout Data</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Serialized Layout Data</em>' attribute.
	 * @see #getSerializedLayoutData()
	 * @generated
	 */
	void setSerializedLayoutData(String value);

} // PerspectiveUserEntry