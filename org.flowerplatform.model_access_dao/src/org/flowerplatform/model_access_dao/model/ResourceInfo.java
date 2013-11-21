/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.model_access_dao.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Resource Info</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.model_access_dao.model.ResourceInfo#getRepoId <em>Repo Id</em>}</li>
 *   <li>{@link org.flowerplatform.model_access_dao.model.ResourceInfo#getResourceId <em>Resource Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.model_access_dao.model.ModelPackage#getResourceInfo()
 * @model
 * @generated
 */
public interface ResourceInfo extends EObject {
	/**
	 * Returns the value of the '<em><b>Repo Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Repo Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Repo Id</em>' attribute.
	 * @see #setRepoId(String)
	 * @see org.flowerplatform.model_access_dao.model.ModelPackage#getResourceInfo_RepoId()
	 * @model
	 * @generated
	 */
	String getRepoId();

	/**
	 * Sets the value of the '{@link org.flowerplatform.model_access_dao.model.ResourceInfo#getRepoId <em>Repo Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Repo Id</em>' attribute.
	 * @see #getRepoId()
	 * @generated
	 */
	void setRepoId(String value);

	/**
	 * Returns the value of the '<em><b>Resource Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Resource Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resource Id</em>' attribute.
	 * @see #setResourceId(String)
	 * @see org.flowerplatform.model_access_dao.model.ModelPackage#getResourceInfo_ResourceId()
	 * @model
	 * @generated
	 */
	String getResourceId();

	/**
	 * Sets the value of the '{@link org.flowerplatform.model_access_dao.model.ResourceInfo#getResourceId <em>Resource Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Resource Id</em>' attribute.
	 * @see #getResourceId()
	 * @generated
	 */
	void setResourceId(String value);

} // ResourceInfo
