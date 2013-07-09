/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.crispico.flower.mp.model.codesync.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import com.crispico.flower.mp.model.codesync.AstCacheElement;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

import java.io.IOException;
import java.io.Serializable;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ast Cache Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.AstCacheElementImpl#getCodeSyncElement <em>Code Sync Element</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class AstCacheElementImpl extends EObjectImpl implements AstCacheElement {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The cached value of the '{@link #getCodeSyncElement() <em>Code Sync Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCodeSyncElement()
	 * @generated
	 * @ordered
	 */
	protected CodeSyncElement codeSyncElement;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AstCacheElementImpl() {
		super();
	}

	/**
	 * Special serialization: the {@link #codeSyncElement} is not serialized.
	 * 
	 * @author Mariana
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		// nothing to do
	}
	
	/**
	 * @author Mariana
	 */
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		// nothing to do
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CodeSyncPackage.Literals.AST_CACHE_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CodeSyncElement getCodeSyncElement() {
		if (codeSyncElement != null && codeSyncElement.eIsProxy()) {
			InternalEObject oldCodeSyncElement = (InternalEObject)codeSyncElement;
			codeSyncElement = (CodeSyncElement)eResolveProxy(oldCodeSyncElement);
			if (codeSyncElement != oldCodeSyncElement) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CodeSyncPackage.AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT, oldCodeSyncElement, codeSyncElement));
			}
		}
		return codeSyncElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CodeSyncElement basicGetCodeSyncElement() {
		return codeSyncElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCodeSyncElement(CodeSyncElement newCodeSyncElement, NotificationChain msgs) {
		CodeSyncElement oldCodeSyncElement = codeSyncElement;
		codeSyncElement = newCodeSyncElement;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CodeSyncPackage.AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT, oldCodeSyncElement, newCodeSyncElement);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCodeSyncElement(CodeSyncElement newCodeSyncElement) {
		if (newCodeSyncElement != codeSyncElement) {
			NotificationChain msgs = null;
			if (codeSyncElement != null)
				msgs = ((InternalEObject)codeSyncElement).eInverseRemove(this, CodeSyncPackage.CODE_SYNC_ELEMENT__AST_CACHE_ELEMENT, CodeSyncElement.class, msgs);
			if (newCodeSyncElement != null)
				msgs = ((InternalEObject)newCodeSyncElement).eInverseAdd(this, CodeSyncPackage.CODE_SYNC_ELEMENT__AST_CACHE_ELEMENT, CodeSyncElement.class, msgs);
			msgs = basicSetCodeSyncElement(newCodeSyncElement, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CodeSyncPackage.AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT, newCodeSyncElement, newCodeSyncElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CodeSyncPackage.AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT:
				if (codeSyncElement != null)
					msgs = ((InternalEObject)codeSyncElement).eInverseRemove(this, CodeSyncPackage.CODE_SYNC_ELEMENT__AST_CACHE_ELEMENT, CodeSyncElement.class, msgs);
				return basicSetCodeSyncElement((CodeSyncElement)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CodeSyncPackage.AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT:
				return basicSetCodeSyncElement(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CodeSyncPackage.AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT:
				if (resolve) return getCodeSyncElement();
				return basicGetCodeSyncElement();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case CodeSyncPackage.AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT:
				setCodeSyncElement((CodeSyncElement)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case CodeSyncPackage.AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT:
				setCodeSyncElement((CodeSyncElement)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case CodeSyncPackage.AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT:
				return codeSyncElement != null;
		}
		return super.eIsSet(featureID);
	}

} //AstCacheElementImpl
