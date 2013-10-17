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
package org.flowerplatform.emf_model.notation;

import org.eclipse.emf.common.util.EList;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.emf_model.notation.Diagram#getName <em>Name</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.notation.Diagram#getPersistentEdges <em>Persistent Edges</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.notation.Diagram#getNewElementsPath <em>New Elements Path</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.notation.Diagram#isShowNewElementsPathDialog <em>Show New Elements Path Dialog</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.emf_model.notation.NotationPackage#getDiagram()
 * @model
 * @generated
 */
public interface Diagram extends View {
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
	 * @see org.flowerplatform.emf_model.notation.NotationPackage#getDiagram_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.flowerplatform.emf_model.notation.Diagram#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Persistent Edges</b></em>' containment reference list.
	 * The list contents are of type {@link org.flowerplatform.emf_model.notation.Edge}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Persistent Edges</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Persistent Edges</em>' containment reference list.
	 * @see org.flowerplatform.emf_model.notation.NotationPackage#getDiagram_PersistentEdges()
	 * @model containment="true"
	 * @generated
	 */
	EList<Edge> getPersistentEdges();

	/**
	 * Returns the value of the '<em><b>New Elements Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Elements Path</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Elements Path</em>' attribute.
	 * @see #setNewElementsPath(String)
	 * @see org.flowerplatform.emf_model.notation.NotationPackage#getDiagram_NewElementsPath()
	 * @model
	 * @generated
	 */
	String getNewElementsPath();

	/**
	 * Sets the value of the '{@link org.flowerplatform.emf_model.notation.Diagram#getNewElementsPath <em>New Elements Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Elements Path</em>' attribute.
	 * @see #getNewElementsPath()
	 * @generated
	 */
	void setNewElementsPath(String value);

	/**
	 * Returns the value of the '<em><b>Show New Elements Path Dialog</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Show New Elements Path Dialog</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Show New Elements Path Dialog</em>' attribute.
	 * @see #setShowNewElementsPathDialog(boolean)
	 * @see org.flowerplatform.emf_model.notation.NotationPackage#getDiagram_ShowNewElementsPathDialog()
	 * @model default="true"
	 * @generated
	 */
	boolean isShowNewElementsPathDialog();

	/**
	 * Sets the value of the '{@link org.flowerplatform.emf_model.notation.Diagram#isShowNewElementsPathDialog <em>Show New Elements Path Dialog</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Show New Elements Path Dialog</em>' attribute.
	 * @see #isShowNewElementsPathDialog()
	 * @generated
	 */
	void setShowNewElementsPathDialog(boolean value);

} // Diagram