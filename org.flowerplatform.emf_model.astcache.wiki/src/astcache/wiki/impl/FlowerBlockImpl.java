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

import astcache.wiki.FlowerBlock;
import astcache.wiki.WikiPackage;

import com.crispico.flower.mp.model.codesync.impl.AstCacheElementImpl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Flower Block</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link astcache.wiki.impl.FlowerBlockImpl#getContent <em>Content</em>}</li>
 *   <li>{@link astcache.wiki.impl.FlowerBlockImpl#getLineStart <em>Line Start</em>}</li>
 *   <li>{@link astcache.wiki.impl.FlowerBlockImpl#getLineEnd <em>Line End</em>}</li>
 *   <li>{@link astcache.wiki.impl.FlowerBlockImpl#isConflict <em>Conflict</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FlowerBlockImpl extends AstCacheElementImpl implements FlowerBlock {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The default value of the '{@link #getContent() <em>Content</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContent()
	 * @generated
	 * @ordered
	 */
	protected static final String CONTENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getContent() <em>Content</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContent()
	 * @generated
	 * @ordered
	 */
	protected String content = CONTENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getLineStart() <em>Line Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLineStart()
	 * @generated
	 * @ordered
	 */
	protected static final int LINE_START_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getLineStart() <em>Line Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLineStart()
	 * @generated
	 * @ordered
	 */
	protected int lineStart = LINE_START_EDEFAULT;

	/**
	 * The default value of the '{@link #getLineEnd() <em>Line End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLineEnd()
	 * @generated
	 * @ordered
	 */
	protected static final int LINE_END_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getLineEnd() <em>Line End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLineEnd()
	 * @generated
	 * @ordered
	 */
	protected int lineEnd = LINE_END_EDEFAULT;

	/**
	 * The default value of the '{@link #isConflict() <em>Conflict</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isConflict()
	 * @generated
	 * @ordered
	 */
	protected static final boolean CONFLICT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isConflict() <em>Conflict</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isConflict()
	 * @generated
	 * @ordered
	 */
	protected boolean conflict = CONFLICT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FlowerBlockImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return WikiPackage.Literals.FLOWER_BLOCK;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getContent() {
		return content;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContent(String newContent) {
		String oldContent = content;
		content = newContent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WikiPackage.FLOWER_BLOCK__CONTENT, oldContent, content));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getLineStart() {
		return lineStart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLineStart(int newLineStart) {
		int oldLineStart = lineStart;
		lineStart = newLineStart;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WikiPackage.FLOWER_BLOCK__LINE_START, oldLineStart, lineStart));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getLineEnd() {
		return lineEnd;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLineEnd(int newLineEnd) {
		int oldLineEnd = lineEnd;
		lineEnd = newLineEnd;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WikiPackage.FLOWER_BLOCK__LINE_END, oldLineEnd, lineEnd));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isConflict() {
		return conflict;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConflict(boolean newConflict) {
		boolean oldConflict = conflict;
		conflict = newConflict;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WikiPackage.FLOWER_BLOCK__CONFLICT, oldConflict, conflict));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case WikiPackage.FLOWER_BLOCK__CONTENT:
				return getContent();
			case WikiPackage.FLOWER_BLOCK__LINE_START:
				return new Integer(getLineStart());
			case WikiPackage.FLOWER_BLOCK__LINE_END:
				return new Integer(getLineEnd());
			case WikiPackage.FLOWER_BLOCK__CONFLICT:
				return isConflict() ? Boolean.TRUE : Boolean.FALSE;
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
			case WikiPackage.FLOWER_BLOCK__CONTENT:
				setContent((String)newValue);
				return;
			case WikiPackage.FLOWER_BLOCK__LINE_START:
				setLineStart(((Integer)newValue).intValue());
				return;
			case WikiPackage.FLOWER_BLOCK__LINE_END:
				setLineEnd(((Integer)newValue).intValue());
				return;
			case WikiPackage.FLOWER_BLOCK__CONFLICT:
				setConflict(((Boolean)newValue).booleanValue());
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
			case WikiPackage.FLOWER_BLOCK__CONTENT:
				setContent(CONTENT_EDEFAULT);
				return;
			case WikiPackage.FLOWER_BLOCK__LINE_START:
				setLineStart(LINE_START_EDEFAULT);
				return;
			case WikiPackage.FLOWER_BLOCK__LINE_END:
				setLineEnd(LINE_END_EDEFAULT);
				return;
			case WikiPackage.FLOWER_BLOCK__CONFLICT:
				setConflict(CONFLICT_EDEFAULT);
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
			case WikiPackage.FLOWER_BLOCK__CONTENT:
				return CONTENT_EDEFAULT == null ? content != null : !CONTENT_EDEFAULT.equals(content);
			case WikiPackage.FLOWER_BLOCK__LINE_START:
				return lineStart != LINE_START_EDEFAULT;
			case WikiPackage.FLOWER_BLOCK__LINE_END:
				return lineEnd != LINE_END_EDEFAULT;
			case WikiPackage.FLOWER_BLOCK__CONFLICT:
				return conflict != CONFLICT_EDEFAULT;
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
		result.append(" (content: ");
		result.append(content);
		result.append(", lineStart: ");
		result.append(lineStart);
		result.append(", lineEnd: ");
		result.append(lineEnd);
		result.append(", conflict: ");
		result.append(conflict);
		result.append(')');
		return result.toString();
	}

} //FlowerBlockImpl