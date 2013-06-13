/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.web.entity;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Permission Entity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.web.entity.PermissionEntity#getType <em>Type</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.PermissionEntity#getActions <em>Actions</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.PermissionEntity#getAssignedTo <em>Assigned To</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.web.entity.EntityPackage#getPermissionEntity()
 * @model
 * @generated
 */
public interface PermissionEntity extends NamedEntity {
	
	/**
	 * ANY_ENTITY is a placeholder for assignedTo, meaning assignedTo
	 * can have any value including null.
	 */
	public static final String ANY_ENTITY = "*";
	
	public static final String ORGANIZATION_PREFIX = "#";
	
	public static final String GROUP_PREFIX = "@";
	
	public static final String USER_PREFIX = "$";
	
	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see org.flowerplatform.web.entity.EntityPackage#getPermissionEntity_Type()
	 * @model
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.PermissionEntity#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

	/**
	 * Returns the value of the '<em><b>Actions</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Actions</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Actions</em>' attribute.
	 * @see #setActions(String)
	 * @see org.flowerplatform.web.entity.EntityPackage#getPermissionEntity_Actions()
	 * @model
	 * @generated
	 */
	String getActions();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.PermissionEntity#getActions <em>Actions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Actions</em>' attribute.
	 * @see #getActions()
	 * @generated
	 */
	void setActions(String value);

	/**
	 * Returns the value of the '<em><b>Assigned To</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Assigned To</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Assigned To</em>' attribute.
	 * @see #setAssignedTo(String)
	 * @see org.flowerplatform.web.entity.EntityPackage#getPermissionEntity_AssignedTo()
	 * @model
	 * @generated
	 */
	String getAssignedTo();

	/**
	 * Sets the value of the '{@link org.flowerplatform.web.entity.PermissionEntity#getAssignedTo <em>Assigned To</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Assigned To</em>' attribute.
	 * @see #getAssignedTo()
	 * @generated
	 */
	void setAssignedTo(String value);

} // PermissionEntity
