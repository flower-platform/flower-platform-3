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
package com.crispico.flower.mp.model.codesync;

import java.io.Serializable;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ast Cache Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.crispico.flower.mp.model.codesync.AstCacheElement#getCodeSyncElement <em>Code Sync Element</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getAstCacheElement()
 * @model abstract="true" superTypes="com.crispico.flower.mp.model.codesync.Serializable"
 * @generated
 */
public interface AstCacheElement extends EObject, Serializable {
	/**
	 * Returns the value of the '<em><b>Code Sync Element</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#getAstCacheElement <em>Ast Cache Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Code Sync Element</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Code Sync Element</em>' reference.
	 * @see #setCodeSyncElement(CodeSyncElement)
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getAstCacheElement_CodeSyncElement()
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncElement#getAstCacheElement
	 * @model opposite="astCacheElement"
	 * @generated
	 */
	CodeSyncElement getCodeSyncElement();

	/**
	 * Sets the value of the '{@link com.crispico.flower.mp.model.codesync.AstCacheElement#getCodeSyncElement <em>Code Sync Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Code Sync Element</em>' reference.
	 * @see #getCodeSyncElement()
	 * @generated
	 */
	void setCodeSyncElement(CodeSyncElement value);

} // AstCacheElement