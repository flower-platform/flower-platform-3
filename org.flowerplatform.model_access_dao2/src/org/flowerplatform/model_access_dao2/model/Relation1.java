/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.model_access_dao2.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Relation1</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.model_access_dao2.model.Relation1#getSource <em>Source</em>}</li>
 *   <li>{@link org.flowerplatform.model_access_dao2.model.Relation1#getTarget <em>Target</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.model_access_dao2.model.ModelPackage#getRelation1()
 * @model
 * @generated
 */
public interface Relation1 extends EObject {
	/**
	 * Returns the value of the '<em><b>Source</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.flowerplatform.model_access_dao2.model.CodeSyncElement1#getRelations <em>Relations</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source</em>' container reference.
	 * @see #setSource(CodeSyncElement1)
	 * @see org.flowerplatform.model_access_dao2.model.ModelPackage#getRelation1_Source()
	 * @see org.flowerplatform.model_access_dao2.model.CodeSyncElement1#getRelations
	 * @model opposite="relations" resolveProxies="false" transient="false"
	 * @generated
	 */
	CodeSyncElement1 getSource();

	/**
	 * Sets the value of the '{@link org.flowerplatform.model_access_dao2.model.Relation1#getSource <em>Source</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source</em>' container reference.
	 * @see #getSource()
	 * @generated
	 */
	void setSource(CodeSyncElement1 value);

	/**
	 * Returns the value of the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' reference.
	 * @see #setTarget(CodeSyncElement1)
	 * @see org.flowerplatform.model_access_dao2.model.ModelPackage#getRelation1_Target()
	 * @model resolveProxies="false"
	 * @generated
	 */
	CodeSyncElement1 getTarget();

	/**
	 * Sets the value of the '{@link org.flowerplatform.model_access_dao2.model.Relation1#getTarget <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' reference.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(CodeSyncElement1 value);

} // Relation1
