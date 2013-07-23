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
package astcache.wiki.impl;

import astcache.wiki.Page;
import astcache.wiki.WikiPackage;

import com.crispico.flower.mp.model.codesync.impl.AstCacheElementImpl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Page</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link astcache.wiki.impl.PageImpl#getInitialContent <em>Initial Content</em>}</li>
 *   <li>{@link astcache.wiki.impl.PageImpl#getLineDelimiter <em>Line Delimiter</em>}</li>
 *   <li>{@link astcache.wiki.impl.PageImpl#getDiff <em>Diff</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PageImpl extends AstCacheElementImpl implements Page {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The default value of the '{@link #getInitialContent() <em>Initial Content</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitialContent()
	 * @generated
	 * @ordered
	 */
	protected static final String INITIAL_CONTENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getInitialContent() <em>Initial Content</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitialContent()
	 * @generated
	 * @ordered
	 */
	protected String initialContent = INITIAL_CONTENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getLineDelimiter() <em>Line Delimiter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLineDelimiter()
	 * @generated
	 * @ordered
	 */
	protected static final String LINE_DELIMITER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLineDelimiter() <em>Line Delimiter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLineDelimiter()
	 * @generated
	 * @ordered
	 */
	protected String lineDelimiter = LINE_DELIMITER_EDEFAULT;

	/**
	 * The default value of the '{@link #getDiff() <em>Diff</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDiff()
	 * @generated
	 * @ordered
	 */
	protected static final Object DIFF_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDiff() <em>Diff</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDiff()
	 * @generated
	 * @ordered
	 */
	protected Object diff = DIFF_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PageImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return WikiPackage.Literals.PAGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getInitialContent() {
		return initialContent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInitialContent(String newInitialContent) {
		String oldInitialContent = initialContent;
		initialContent = newInitialContent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WikiPackage.PAGE__INITIAL_CONTENT, oldInitialContent, initialContent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLineDelimiter() {
		return lineDelimiter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLineDelimiter(String newLineDelimiter) {
		String oldLineDelimiter = lineDelimiter;
		lineDelimiter = newLineDelimiter;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WikiPackage.PAGE__LINE_DELIMITER, oldLineDelimiter, lineDelimiter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getDiff() {
		return diff;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDiff(Object newDiff) {
		Object oldDiff = diff;
		diff = newDiff;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WikiPackage.PAGE__DIFF, oldDiff, diff));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case WikiPackage.PAGE__INITIAL_CONTENT:
				return getInitialContent();
			case WikiPackage.PAGE__LINE_DELIMITER:
				return getLineDelimiter();
			case WikiPackage.PAGE__DIFF:
				return getDiff();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case WikiPackage.PAGE__INITIAL_CONTENT:
				setInitialContent((String)newValue);
				return;
			case WikiPackage.PAGE__LINE_DELIMITER:
				setLineDelimiter((String)newValue);
				return;
			case WikiPackage.PAGE__DIFF:
				setDiff(newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
			case WikiPackage.PAGE__INITIAL_CONTENT:
				setInitialContent(INITIAL_CONTENT_EDEFAULT);
				return;
			case WikiPackage.PAGE__LINE_DELIMITER:
				setLineDelimiter(LINE_DELIMITER_EDEFAULT);
				return;
			case WikiPackage.PAGE__DIFF:
				setDiff(DIFF_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case WikiPackage.PAGE__INITIAL_CONTENT:
				return INITIAL_CONTENT_EDEFAULT == null ? initialContent != null : !INITIAL_CONTENT_EDEFAULT.equals(initialContent);
			case WikiPackage.PAGE__LINE_DELIMITER:
				return LINE_DELIMITER_EDEFAULT == null ? lineDelimiter != null : !LINE_DELIMITER_EDEFAULT.equals(lineDelimiter);
			case WikiPackage.PAGE__DIFF:
				return DIFF_EDEFAULT == null ? diff != null : !DIFF_EDEFAULT.equals(diff);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (initialContent: ");
		result.append(initialContent);
		result.append(", lineDelimiter: ");
		result.append(lineDelimiter);
		result.append(", diff: ");
		result.append(diff);
		result.append(')');
		return result.toString();
	}

} //PageImpl