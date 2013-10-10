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
package com.crispico.flower.mp.model.codesync.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

import com.crispico.flower.mp.model.codesync.AstCacheElement;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.FeatureChange;
import com.crispico.flower.mp.model.codesync.Relation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Element</b></em>'.
 * 
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.CodeSyncElementImpl#isSynchronized <em>Synchronized</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.CodeSyncElementImpl#isChildrenSynchronized <em>Children Synchronized</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.CodeSyncElementImpl#isAdded <em>Added</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.CodeSyncElementImpl#isDeleted <em>Deleted</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.CodeSyncElementImpl#getStatusFlags <em>Status Flags</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.CodeSyncElementImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.CodeSyncElementImpl#getType <em>Type</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.CodeSyncElementImpl#getAstCacheElement <em>Ast Cache Element</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.CodeSyncElementImpl#getChildren <em>Children</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.CodeSyncElementImpl#getFeatureChanges <em>Feature Changes</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.CodeSyncElementImpl#getRelations <em>Relations</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CodeSyncElementImpl extends EObjectImpl implements CodeSyncElement {
	
	/**
	 * @author Cristi
	 */
	protected static final int STATUS_FLAG_SYNCHRONIZED = 0x0001;
	
	/**
	 * @author Cristi
	 */
	protected static final int STATUS_FLAG_CHILDREN_SYNCHRONIZED = 0x0002;
	
	/**
	 * @author Cristi
	 */
	protected static final int STATUS_FLAG_ADDED = 0x0004;
	
	/**
	 * @author Cristi
	 */
	protected static final int STATUS_FLAG_DELETED = 0x0008;
	
	/**
	 * The default value of the '{@link #isSynchronized() <em>Synchronized</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSynchronized()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SYNCHRONIZED_EDEFAULT = false;

	/**
	 * The default value of the '{@link #isChildrenSynchronized() <em>Children Synchronized</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isChildrenSynchronized()
	 * @generated
	 * @ordered
	 */
	protected static final boolean CHILDREN_SYNCHRONIZED_EDEFAULT = false;

	/**
	 * The default value of the '{@link #isAdded() <em>Added</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAdded()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ADDED_EDEFAULT = false;

	/**
	 * The default value of the '{@link #isDeleted() <em>Deleted</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDeleted()
	 * @generated
	 * @ordered
	 */
	protected static final boolean DELETED_EDEFAULT = false;

	/**
	 * The default value of the '{@link #getStatusFlags() <em>Status Flags</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStatusFlags()
	 * @generated
	 * @ordered
	 */
	protected static final int STATUS_FLAGS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getStatusFlags() <em>Status Flags</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStatusFlags()
	 * @generated
	 * @ordered
	 */
	protected int statusFlags = STATUS_FLAGS_EDEFAULT;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final String TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected String type = TYPE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getAstCacheElement() <em>Ast Cache Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAstCacheElement()
	 * @generated
	 * @ordered
	 */
	protected AstCacheElement astCacheElement;

	/**
	 * The cached value of the '{@link #getChildren() <em>Children</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildren()
	 * @generated
	 * @ordered
	 */
	protected EList<CodeSyncElement> children;

	/**
	 * The cached value of the '{@link #getFeatureChanges() <em>Feature Changes</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeatureChanges()
	 * @generated
	 * @ordered
	 */
	protected EMap<EStructuralFeature, FeatureChange> featureChanges;

	/**
	 * The cached value of the '{@link #getRelations() <em>Relations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRelations()
	 * @generated
	 * @ordered
	 */
	protected EList<Relation> relations;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CodeSyncElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CodeSyncPackage.Literals.CODE_SYNC_ELEMENT;
	}

	/**
	 * @generated NOT
	 * @author Cristi
	 */
	public boolean isSynchronized() {
		return (statusFlags & STATUS_FLAG_SYNCHRONIZED) != 0;
	}

	/**
	 * @generated NOT
	 * @author Cristi
	 */
	public void setSynchronized(boolean newSynchronized) {
		if (newSynchronized) {
			statusFlags |= STATUS_FLAG_SYNCHRONIZED;
		} else {
			statusFlags &= ~STATUS_FLAG_SYNCHRONIZED;
		}
	}

	/**
	 * @generated NOT
	 * @author Cristi
	 */
	public boolean isChildrenSynchronized() {
		return (statusFlags & STATUS_FLAG_CHILDREN_SYNCHRONIZED) != 0;
	}

	/**
	 * @generated NOT
	 * @author Cristi
	 */
	public void setChildrenSynchronized(boolean newChildrenSynchronized) {
		if (newChildrenSynchronized) {
			statusFlags |= STATUS_FLAG_CHILDREN_SYNCHRONIZED;
		} else {
			statusFlags &= ~STATUS_FLAG_CHILDREN_SYNCHRONIZED;
		}
	}

	/**
	 * @generated NOT
	 * @author Cristi
	 */
	public boolean isAdded() {
		return (statusFlags & STATUS_FLAG_ADDED) != 0;
	}

	/**
	 * @generated NOT
	 * @author Cristi
	 */
	public void setAdded(boolean newAdded) {
		if (newAdded) {
			statusFlags |= STATUS_FLAG_ADDED;
		} else {
			statusFlags &= ~STATUS_FLAG_ADDED;
		}
	}

	/**
	 * @generated NOT
	 * @author Cristi
	 */
	public boolean isDeleted() {
		return (statusFlags & STATUS_FLAG_DELETED) != 0;
	}

	/**
	 * @generated NOT
	 * @author Cristi
	 */
	public void setDeleted(boolean newDeleted) {
		if (newDeleted) {
			statusFlags |= STATUS_FLAG_DELETED;
		} else {
			statusFlags &= ~STATUS_FLAG_DELETED;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getStatusFlags() {
		return statusFlags;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStatusFlags(int newStatusFlags) {
		int oldStatusFlags = statusFlags;
		statusFlags = newStatusFlags;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CodeSyncPackage.CODE_SYNC_ELEMENT__STATUS_FLAGS, oldStatusFlags, statusFlags));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CodeSyncPackage.CODE_SYNC_ELEMENT__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(String newType) {
		String oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CodeSyncPackage.CODE_SYNC_ELEMENT__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AstCacheElement getAstCacheElement() {
		if (astCacheElement != null && astCacheElement.eIsProxy()) {
			InternalEObject oldAstCacheElement = (InternalEObject)astCacheElement;
			astCacheElement = (AstCacheElement)eResolveProxy(oldAstCacheElement);
			if (astCacheElement != oldAstCacheElement) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CodeSyncPackage.CODE_SYNC_ELEMENT__AST_CACHE_ELEMENT, oldAstCacheElement, astCacheElement));
			}
		}
		return astCacheElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AstCacheElement basicGetAstCacheElement() {
		return astCacheElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAstCacheElement(AstCacheElement newAstCacheElement, NotificationChain msgs) {
		AstCacheElement oldAstCacheElement = astCacheElement;
		astCacheElement = newAstCacheElement;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CodeSyncPackage.CODE_SYNC_ELEMENT__AST_CACHE_ELEMENT, oldAstCacheElement, newAstCacheElement);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAstCacheElement(AstCacheElement newAstCacheElement) {
		if (newAstCacheElement != astCacheElement) {
			NotificationChain msgs = null;
			if (astCacheElement != null)
				msgs = ((InternalEObject)astCacheElement).eInverseRemove(this, CodeSyncPackage.AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT, AstCacheElement.class, msgs);
			if (newAstCacheElement != null)
				msgs = ((InternalEObject)newAstCacheElement).eInverseAdd(this, CodeSyncPackage.AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT, AstCacheElement.class, msgs);
			msgs = basicSetAstCacheElement(newAstCacheElement, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CodeSyncPackage.CODE_SYNC_ELEMENT__AST_CACHE_ELEMENT, newAstCacheElement, newAstCacheElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<CodeSyncElement> getChildren() {
		if (children == null) {
			children = new EObjectContainmentEList<CodeSyncElement>(CodeSyncElement.class, this, CodeSyncPackage.CODE_SYNC_ELEMENT__CHILDREN);
		}
		return children;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMap<EStructuralFeature, FeatureChange> getFeatureChanges() {
		if (featureChanges == null) {
			featureChanges = new EcoreEMap<EStructuralFeature,FeatureChange>(CodeSyncPackage.Literals.ESTRUCTURAL_FEATURE_TO_FEATURE_CHANGE_ENTRY, EStructuralFeatureToFeatureChangeEntryImpl.class, this, CodeSyncPackage.CODE_SYNC_ELEMENT__FEATURE_CHANGES);
		}
		return featureChanges;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Relation> getRelations() {
		if (relations == null) {
			relations = new EObjectContainmentWithInverseEList<Relation>(Relation.class, this, CodeSyncPackage.CODE_SYNC_ELEMENT__RELATIONS, CodeSyncPackage.RELATION__SOURCE);
		}
		return relations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CodeSyncPackage.CODE_SYNC_ELEMENT__AST_CACHE_ELEMENT:
				if (astCacheElement != null)
					msgs = ((InternalEObject)astCacheElement).eInverseRemove(this, CodeSyncPackage.AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT, AstCacheElement.class, msgs);
				return basicSetAstCacheElement((AstCacheElement)otherEnd, msgs);
			case CodeSyncPackage.CODE_SYNC_ELEMENT__RELATIONS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getRelations()).basicAdd(otherEnd, msgs);
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
			case CodeSyncPackage.CODE_SYNC_ELEMENT__AST_CACHE_ELEMENT:
				return basicSetAstCacheElement(null, msgs);
			case CodeSyncPackage.CODE_SYNC_ELEMENT__CHILDREN:
				return ((InternalEList<?>)getChildren()).basicRemove(otherEnd, msgs);
			case CodeSyncPackage.CODE_SYNC_ELEMENT__FEATURE_CHANGES:
				return ((InternalEList<?>)getFeatureChanges()).basicRemove(otherEnd, msgs);
			case CodeSyncPackage.CODE_SYNC_ELEMENT__RELATIONS:
				return ((InternalEList<?>)getRelations()).basicRemove(otherEnd, msgs);
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
			case CodeSyncPackage.CODE_SYNC_ELEMENT__SYNCHRONIZED:
				return isSynchronized();
			case CodeSyncPackage.CODE_SYNC_ELEMENT__CHILDREN_SYNCHRONIZED:
				return isChildrenSynchronized();
			case CodeSyncPackage.CODE_SYNC_ELEMENT__ADDED:
				return isAdded();
			case CodeSyncPackage.CODE_SYNC_ELEMENT__DELETED:
				return isDeleted();
			case CodeSyncPackage.CODE_SYNC_ELEMENT__STATUS_FLAGS:
				return getStatusFlags();
			case CodeSyncPackage.CODE_SYNC_ELEMENT__NAME:
				return getName();
			case CodeSyncPackage.CODE_SYNC_ELEMENT__TYPE:
				return getType();
			case CodeSyncPackage.CODE_SYNC_ELEMENT__AST_CACHE_ELEMENT:
				if (resolve) return getAstCacheElement();
				return basicGetAstCacheElement();
			case CodeSyncPackage.CODE_SYNC_ELEMENT__CHILDREN:
				return getChildren();
			case CodeSyncPackage.CODE_SYNC_ELEMENT__FEATURE_CHANGES:
				if (coreType) return getFeatureChanges();
				else return getFeatureChanges().map();
			case CodeSyncPackage.CODE_SYNC_ELEMENT__RELATIONS:
				return getRelations();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case CodeSyncPackage.CODE_SYNC_ELEMENT__SYNCHRONIZED:
				setSynchronized((Boolean)newValue);
				return;
			case CodeSyncPackage.CODE_SYNC_ELEMENT__CHILDREN_SYNCHRONIZED:
				setChildrenSynchronized((Boolean)newValue);
				return;
			case CodeSyncPackage.CODE_SYNC_ELEMENT__ADDED:
				setAdded((Boolean)newValue);
				return;
			case CodeSyncPackage.CODE_SYNC_ELEMENT__DELETED:
				setDeleted((Boolean)newValue);
				return;
			case CodeSyncPackage.CODE_SYNC_ELEMENT__STATUS_FLAGS:
				setStatusFlags((Integer)newValue);
				return;
			case CodeSyncPackage.CODE_SYNC_ELEMENT__NAME:
				setName((String)newValue);
				return;
			case CodeSyncPackage.CODE_SYNC_ELEMENT__TYPE:
				setType((String)newValue);
				return;
			case CodeSyncPackage.CODE_SYNC_ELEMENT__AST_CACHE_ELEMENT:
				setAstCacheElement((AstCacheElement)newValue);
				return;
			case CodeSyncPackage.CODE_SYNC_ELEMENT__CHILDREN:
				getChildren().clear();
				getChildren().addAll((Collection<? extends CodeSyncElement>)newValue);
				return;
			case CodeSyncPackage.CODE_SYNC_ELEMENT__FEATURE_CHANGES:
				((EStructuralFeature.Setting)getFeatureChanges()).set(newValue);
				return;
			case CodeSyncPackage.CODE_SYNC_ELEMENT__RELATIONS:
				getRelations().clear();
				getRelations().addAll((Collection<? extends Relation>)newValue);
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
			case CodeSyncPackage.CODE_SYNC_ELEMENT__SYNCHRONIZED:
				setSynchronized(SYNCHRONIZED_EDEFAULT);
				return;
			case CodeSyncPackage.CODE_SYNC_ELEMENT__CHILDREN_SYNCHRONIZED:
				setChildrenSynchronized(CHILDREN_SYNCHRONIZED_EDEFAULT);
				return;
			case CodeSyncPackage.CODE_SYNC_ELEMENT__ADDED:
				setAdded(ADDED_EDEFAULT);
				return;
			case CodeSyncPackage.CODE_SYNC_ELEMENT__DELETED:
				setDeleted(DELETED_EDEFAULT);
				return;
			case CodeSyncPackage.CODE_SYNC_ELEMENT__STATUS_FLAGS:
				setStatusFlags(STATUS_FLAGS_EDEFAULT);
				return;
			case CodeSyncPackage.CODE_SYNC_ELEMENT__NAME:
				setName(NAME_EDEFAULT);
				return;
			case CodeSyncPackage.CODE_SYNC_ELEMENT__TYPE:
				setType(TYPE_EDEFAULT);
				return;
			case CodeSyncPackage.CODE_SYNC_ELEMENT__AST_CACHE_ELEMENT:
				setAstCacheElement((AstCacheElement)null);
				return;
			case CodeSyncPackage.CODE_SYNC_ELEMENT__CHILDREN:
				getChildren().clear();
				return;
			case CodeSyncPackage.CODE_SYNC_ELEMENT__FEATURE_CHANGES:
				getFeatureChanges().clear();
				return;
			case CodeSyncPackage.CODE_SYNC_ELEMENT__RELATIONS:
				getRelations().clear();
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
			case CodeSyncPackage.CODE_SYNC_ELEMENT__SYNCHRONIZED:
				return isSynchronized() != SYNCHRONIZED_EDEFAULT;
			case CodeSyncPackage.CODE_SYNC_ELEMENT__CHILDREN_SYNCHRONIZED:
				return isChildrenSynchronized() != CHILDREN_SYNCHRONIZED_EDEFAULT;
			case CodeSyncPackage.CODE_SYNC_ELEMENT__ADDED:
				return isAdded() != ADDED_EDEFAULT;
			case CodeSyncPackage.CODE_SYNC_ELEMENT__DELETED:
				return isDeleted() != DELETED_EDEFAULT;
			case CodeSyncPackage.CODE_SYNC_ELEMENT__STATUS_FLAGS:
				return statusFlags != STATUS_FLAGS_EDEFAULT;
			case CodeSyncPackage.CODE_SYNC_ELEMENT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case CodeSyncPackage.CODE_SYNC_ELEMENT__TYPE:
				return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
			case CodeSyncPackage.CODE_SYNC_ELEMENT__AST_CACHE_ELEMENT:
				return astCacheElement != null;
			case CodeSyncPackage.CODE_SYNC_ELEMENT__CHILDREN:
				return children != null && !children.isEmpty();
			case CodeSyncPackage.CODE_SYNC_ELEMENT__FEATURE_CHANGES:
				return featureChanges != null && !featureChanges.isEmpty();
			case CodeSyncPackage.CODE_SYNC_ELEMENT__RELATIONS:
				return relations != null && !relations.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (statusFlags: ");
		result.append(statusFlags);
		result.append(", name: ");
		result.append(name);
		result.append(", type: ");
		result.append(type);
		result.append(')');
		return result.toString();
	}

} //CodeSyncElementImpl