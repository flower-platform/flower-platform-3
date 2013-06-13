/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.web.entity;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>DB Version</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.web.entity.DBVersion#getId <em>Id</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.DBVersion#getDbVersion <em>Db Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.web.entity.EntityPackage#getDBVersion()
 * @model
 * @generated
 */
public interface DBVersion extends EObject {
	
	public static final long SINGLETON_RECORD_ID = 1;
	
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(long)
	 * @see org.flowerplatform.web.entity.EntityPackage#getDBVersion_Id()
	 * @model id="true"
	 * @generated
	 */
	long getId();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.DBVersion#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(long value);

	/**
	 * Returns the value of the '<em><b>Db Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Db Version</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Db Version</em>' attribute.
	 * @see #setDbVersion(long)
	 * @see org.flowerplatform.web.entity.EntityPackage#getDBVersion_DbVersion()
	 * @model
	 * @generated
	 */
	long getDbVersion();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.DBVersion#getDbVersion <em>Db Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Db Version</em>' attribute.
	 * @see #getDbVersion()
	 * @generated
	 */
	void setDbVersion(long value);

} // DBVersion
