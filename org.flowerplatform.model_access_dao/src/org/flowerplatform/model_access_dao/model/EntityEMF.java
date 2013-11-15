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
 * A representation of the model object '<em><b>Entity EMF</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.model_access_dao.model.EntityEMF#getReferencedElements <em>Referenced Elements</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.model_access_dao.model.ModelPackage#getEntityEMF()
 * @model
 * @generated
 */
public interface EntityEMF extends CodeSyncElement1EMF {
	/**
	 * Returns the value of the '<em><b>Referenced Elements</b></em>' reference list.
	 * The list contents are of type {@link org.flowerplatform.model_access_dao.model.CodeSyncElement1}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Referenced Elements</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Referenced Elements</em>' reference list.
	 * @see org.flowerplatform.model_access_dao.model.ModelPackage#getEntityEMF_ReferencedElements()
	 * @model
	 * @generated
	 */
	EList<CodeSyncElement1> getReferencedElements();

} // EntityEMF
