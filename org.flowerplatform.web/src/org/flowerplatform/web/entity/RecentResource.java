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

import java.util.Date;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Recent Resource</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.web.entity.RecentResource#getLabel <em>Label</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.RecentResource#getPath <em>Path</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.RecentResource#getOrganization <em>Organization</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.RecentResource#getLastAccess <em>Last Access</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.RecentResource#getLastAccessUser <em>Last Access User</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.RecentResource#getLastSave <em>Last Save</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.RecentResource#getLastSaveUser <em>Last Save User</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.web.entity.EntityPackage#getRecentResource()
 * @model
 * @generated
 */
public interface RecentResource extends Entity {
	/**
	 * Returns the value of the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Label</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Label</em>' attribute.
	 * @see #setLabel(String)
	 * @see org.flowerplatform.web.entity.EntityPackage#getRecentResource_Label()
	 * @model
	 * @generated
	 */
	String getLabel();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.RecentResource#getLabel <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Label</em>' attribute.
	 * @see #getLabel()
	 * @generated
	 */
	void setLabel(String value);

	/**
	 * Returns the value of the '<em><b>Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Path</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Path</em>' attribute.
	 * @see #setPath(String)
	 * @see org.flowerplatform.web.entity.EntityPackage#getRecentResource_Path()
	 * @model
	 * @generated
	 */
	String getPath();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.RecentResource#getPath <em>Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Path</em>' attribute.
	 * @see #getPath()
	 * @generated
	 */
	void setPath(String value);

	/**
	 * Returns the value of the '<em><b>Organization</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Organization</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Organization</em>' reference.
	 * @see #setOrganization(Organization)
	 * @see org.flowerplatform.web.entity.EntityPackage#getRecentResource_Organization()
	 * @model
	 * @generated
	 */
	Organization getOrganization();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.RecentResource#getOrganization <em>Organization</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Organization</em>' reference.
	 * @see #getOrganization()
	 * @generated
	 */
	void setOrganization(Organization value);

	/**
	 * Returns the value of the '<em><b>Last Access</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Last Access</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Last Access</em>' attribute.
	 * @see #setLastAccess(Date)
	 * @see org.flowerplatform.web.entity.EntityPackage#getRecentResource_LastAccess()
	 * @model
	 * @generated
	 */
	Date getLastAccess();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.RecentResource#getLastAccess <em>Last Access</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Last Access</em>' attribute.
	 * @see #getLastAccess()
	 * @generated
	 */
	void setLastAccess(Date value);

	/**
	 * Returns the value of the '<em><b>Last Access User</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Last Access User</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Last Access User</em>' reference.
	 * @see #setLastAccessUser(User)
	 * @see org.flowerplatform.web.entity.EntityPackage#getRecentResource_LastAccessUser()
	 * @model
	 * @generated
	 */
	User getLastAccessUser();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.RecentResource#getLastAccessUser <em>Last Access User</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Last Access User</em>' reference.
	 * @see #getLastAccessUser()
	 * @generated
	 */
	void setLastAccessUser(User value);

	/**
	 * Returns the value of the '<em><b>Last Save</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Last Save</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Last Save</em>' attribute.
	 * @see #setLastSave(Date)
	 * @see org.flowerplatform.web.entity.EntityPackage#getRecentResource_LastSave()
	 * @model
	 * @generated
	 */
	Date getLastSave();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.RecentResource#getLastSave <em>Last Save</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Last Save</em>' attribute.
	 * @see #getLastSave()
	 * @generated
	 */
	void setLastSave(Date value);

	/**
	 * Returns the value of the '<em><b>Last Save User</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Last Save User</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Last Save User</em>' reference.
	 * @see #setLastSaveUser(User)
	 * @see org.flowerplatform.web.entity.EntityPackage#getRecentResource_LastSaveUser()
	 * @model
	 * @generated
	 */
	User getLastSaveUser();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.RecentResource#getLastSaveUser <em>Last Save User</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Last Save User</em>' reference.
	 * @see #getLastSaveUser()
	 * @generated
	 */
	void setLastSaveUser(User value);

} // RecentResource