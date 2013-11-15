/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.model_access_dao.model;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Code Sync Element1 EMF</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.model_access_dao.model.CodeSyncElement1EMF#getChildren <em>Children</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.model_access_dao.model.ModelPackage#getCodeSyncElement1EMF()
 * @model
 * @generated
 */
public interface CodeSyncElement1EMF extends CodeSyncElement1 {
	/**
	 * Returns the value of the '<em><b>Children</b></em>' containment reference list.
	 * The list contents are of type {@link org.flowerplatform.model_access_dao.model.CodeSyncElement1}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children</em>' containment reference list.
	 * @see org.flowerplatform.model_access_dao.model.ModelPackage#getCodeSyncElement1EMF_Children()
	 * @model containment="true"
	 * @generated
	 */
	EList<CodeSyncElement1> getChildren();

} // CodeSyncElement1EMF
