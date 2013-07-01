/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.crispico.flower.mp.model.astcache.code;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Annotation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.crispico.flower.mp.model.astcache.code.Annotation#getName <em>Name</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.astcache.code.Annotation#getValues <em>Values</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage#getAnnotation()
 * @model
 * @generated
 */
public interface Annotation extends ExtendedModifier {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage#getAnnotation_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link com.crispico.flower.mp.model.astcache.code.Annotation#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Values</b></em>' containment reference list.
	 * The list contents are of type {@link com.crispico.flower.mp.model.astcache.code.AnnotationValue}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Values</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Values</em>' containment reference list.
	 * @see com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage#getAnnotation_Values()
	 * @model containment="true"
	 * @generated
	 */
	EList<AnnotationValue> getValues();

} // Annotation
