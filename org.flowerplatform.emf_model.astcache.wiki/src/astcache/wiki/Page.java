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
package astcache.wiki;

import com.crispico.flower.mp.model.codesync.AstCacheElement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Page</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link astcache.wiki.Page#getInitialContent <em>Initial Content</em>}</li>
 *   <li>{@link astcache.wiki.Page#getLineDelimiter <em>Line Delimiter</em>}</li>
 *   <li>{@link astcache.wiki.Page#getDiff <em>Diff</em>}</li>
 * </ul>
 * </p>
 *
 * @see astcache.wiki.WikiPackage#getPage()
 * @model
 * @generated
 */
public interface Page extends AstCacheElement {
	/**
	 * Returns the value of the '<em><b>Initial Content</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Initial Content</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Initial Content</em>' attribute.
	 * @see #setInitialContent(String)
	 * @see astcache.wiki.WikiPackage#getPage_InitialContent()
	 * @model
	 * @generated
	 */
	String getInitialContent();

	/**
	 * Sets the value of the '{@link astcache.wiki.Page#getInitialContent <em>Initial Content</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Initial Content</em>' attribute.
	 * @see #getInitialContent()
	 * @generated
	 */
	void setInitialContent(String value);

	/**
	 * Returns the value of the '<em><b>Line Delimiter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Line Delimiter</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Line Delimiter</em>' attribute.
	 * @see #setLineDelimiter(String)
	 * @see astcache.wiki.WikiPackage#getPage_LineDelimiter()
	 * @model
	 * @generated
	 */
	String getLineDelimiter();

	/**
	 * Sets the value of the '{@link astcache.wiki.Page#getLineDelimiter <em>Line Delimiter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Line Delimiter</em>' attribute.
	 * @see #getLineDelimiter()
	 * @generated
	 */
	void setLineDelimiter(String value);

	/**
	 * Returns the value of the '<em><b>Diff</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Diff</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Diff</em>' attribute.
	 * @see #setDiff(Object)
	 * @see astcache.wiki.WikiPackage#getPage_Diff()
	 * @model transient="true"
	 * @generated
	 */
	Object getDiff();

	/**
	 * Sets the value of the '{@link astcache.wiki.Page#getDiff <em>Diff</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Diff</em>' attribute.
	 * @see #getDiff()
	 * @generated
	 */
	void setDiff(Object value);

} // Page