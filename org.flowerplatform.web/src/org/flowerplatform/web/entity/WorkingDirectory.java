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
 * A representation of the model object '<em><b>Working Directory</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.web.entity.WorkingDirectory#getOrganization <em>Organization</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.WorkingDirectory#getPathFromOrganization <em>Path From Organization</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.WorkingDirectory#getColor <em>Color</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.web.entity.EntityPackage#getWorkingDirectory()
 * @model
 * @generated
 */
public interface WorkingDirectory extends Entity {
	/**
	 * Returns the value of the '<em><b>Organization</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.flowerplatform.web.entity.Organization#getWorkingDirectories <em>Working Directories</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Organization</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Organization</em>' reference.
	 * @see #setOrganization(Organization)
	 * @see org.flowerplatform.web.entity.EntityPackage#getWorkingDirectory_Organization()
	 * @see org.flowerplatform.web.entity.Organization#getWorkingDirectories
	 * @model opposite="workingDirectories"
	 * @generated
	 */
	Organization getOrganization();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.WorkingDirectory#getOrganization <em>Organization</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Organization</em>' reference.
	 * @see #getOrganization()
	 * @generated
	 */
	void setOrganization(Organization value);

	/**
	 * Returns the value of the '<em><b>Path From Organization</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Path From Organization</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Path From Organization</em>' attribute.
	 * @see #setPathFromOrganization(String)
	 * @see org.flowerplatform.web.entity.EntityPackage#getWorkingDirectory_PathFromOrganization()
	 * @model
	 * @generated
	 */
	String getPathFromOrganization();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.WorkingDirectory#getPathFromOrganization <em>Path From Organization</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Path From Organization</em>' attribute.
	 * @see #getPathFromOrganization()
	 * @generated
	 */
	void setPathFromOrganization(String value);

	/**
	 * Returns the value of the '<em><b>Color</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Color</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Color</em>' attribute.
	 * @see #setColor(int)
	 * @see org.flowerplatform.web.entity.EntityPackage#getWorkingDirectory_Color()
	 * @model
	 * @generated
	 */
	int getColor();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.WorkingDirectory#getColor <em>Color</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Color</em>' attribute.
	 * @see #getColor()
	 * @generated
	 */
	void setColor(int value);

} // WorkingDirectory