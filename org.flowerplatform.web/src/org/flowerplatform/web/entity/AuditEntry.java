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
 * A representation of the model object '<em><b>Audit Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.web.entity.AuditEntry#getTimestamp <em>Timestamp</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.AuditEntry#getUsername <em>Username</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.AuditEntry#getIpAddress <em>Ip Address</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.AuditEntry#getAuditCategory <em>Audit Category</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.AuditEntry#getDuration <em>Duration</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.AuditEntry#getParam0 <em>Param0</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.AuditEntry#getParam1 <em>Param1</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.AuditEntry#getParam2 <em>Param2</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.web.entity.EntityPackage#getAuditEntry()
 * @model
 * @generated
 */
public interface AuditEntry extends Entity {
	/**
	 * Returns the value of the '<em><b>Timestamp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Timestamp</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Timestamp</em>' attribute.
	 * @see #setTimestamp(Date)
	 * @see org.flowerplatform.web.entity.EntityPackage#getAuditEntry_Timestamp()
	 * @model
	 * @generated
	 */
	Date getTimestamp();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.AuditEntry#getTimestamp <em>Timestamp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Timestamp</em>' attribute.
	 * @see #getTimestamp()
	 * @generated
	 */
	void setTimestamp(Date value);

	/**
	 * Returns the value of the '<em><b>Username</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Username</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Username</em>' attribute.
	 * @see #setUsername(String)
	 * @see org.flowerplatform.web.entity.EntityPackage#getAuditEntry_Username()
	 * @model
	 * @generated
	 */
	String getUsername();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.AuditEntry#getUsername <em>Username</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Username</em>' attribute.
	 * @see #getUsername()
	 * @generated
	 */
	void setUsername(String value);

	/**
	 * Returns the value of the '<em><b>Ip Address</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ip Address</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ip Address</em>' attribute.
	 * @see #setIpAddress(String)
	 * @see org.flowerplatform.web.entity.EntityPackage#getAuditEntry_IpAddress()
	 * @model
	 * @generated
	 */
	String getIpAddress();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.AuditEntry#getIpAddress <em>Ip Address</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ip Address</em>' attribute.
	 * @see #getIpAddress()
	 * @generated
	 */
	void setIpAddress(String value);

	/**
	 * Returns the value of the '<em><b>Audit Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Audit Category</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Audit Category</em>' attribute.
	 * @see #setAuditCategory(String)
	 * @see org.flowerplatform.web.entity.EntityPackage#getAuditEntry_AuditCategory()
	 * @model
	 * @generated
	 */
	String getAuditCategory();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.AuditEntry#getAuditCategory <em>Audit Category</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Audit Category</em>' attribute.
	 * @see #getAuditCategory()
	 * @generated
	 */
	void setAuditCategory(String value);

	/**
	 * Returns the value of the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Duration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Duration</em>' attribute.
	 * @see #setDuration(long)
	 * @see org.flowerplatform.web.entity.EntityPackage#getAuditEntry_Duration()
	 * @model
	 * @generated
	 */
	long getDuration();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.AuditEntry#getDuration <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Duration</em>' attribute.
	 * @see #getDuration()
	 * @generated
	 */
	void setDuration(long value);

	/**
	 * Returns the value of the '<em><b>Param0</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Param0</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Param0</em>' attribute.
	 * @see #setParam0(String)
	 * @see org.flowerplatform.web.entity.EntityPackage#getAuditEntry_Param0()
	 * @model
	 * @generated
	 */
	String getParam0();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.AuditEntry#getParam0 <em>Param0</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Param0</em>' attribute.
	 * @see #getParam0()
	 * @generated
	 */
	void setParam0(String value);

	/**
	 * Returns the value of the '<em><b>Param1</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Param1</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Param1</em>' attribute.
	 * @see #setParam1(String)
	 * @see org.flowerplatform.web.entity.EntityPackage#getAuditEntry_Param1()
	 * @model
	 * @generated
	 */
	String getParam1();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.AuditEntry#getParam1 <em>Param1</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Param1</em>' attribute.
	 * @see #getParam1()
	 * @generated
	 */
	void setParam1(String value);

	/**
	 * Returns the value of the '<em><b>Param2</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Param2</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Param2</em>' attribute.
	 * @see #setParam2(String)
	 * @see org.flowerplatform.web.entity.EntityPackage#getAuditEntry_Param2()
	 * @model
	 * @generated
	 */
	String getParam2();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.AuditEntry#getParam2 <em>Param2</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Param2</em>' attribute.
	 * @see #getParam2()
	 * @generated
	 */
	void setParam2(String value);

} // AuditEntry