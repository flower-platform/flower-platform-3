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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.FeatureChange;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Feature Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.FeatureChangeImpl#getOldValue <em>Old Value</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.FeatureChangeImpl#getOldValueAsString <em>Old Value As String</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.FeatureChangeImpl#getOldValueAsContainmentList <em>Old Value As Containment List</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.FeatureChangeImpl#getNewValue <em>New Value</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.FeatureChangeImpl#getNewValueAsString <em>New Value As String</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.FeatureChangeImpl#getNewValueAsContainmentList <em>New Value As Containment List</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FeatureChangeImpl extends EObjectImpl implements FeatureChange {
	/**
	 * The default value of the '{@link #getOldValue() <em>Old Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOldValue()
	 * @generated
	 * @ordered
	 */
	protected static final Object OLD_VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOldValue() <em>Old Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOldValue()
	 * @generated
	 * @ordered
	 */
	protected Object oldValue = OLD_VALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getOldValueAsString() <em>Old Value As String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOldValueAsString()
	 * @generated
	 * @ordered
	 */
	protected static final String OLD_VALUE_AS_STRING_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOldValueAsString() <em>Old Value As String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOldValueAsString()
	 * @generated
	 * @ordered
	 */
	protected String oldValueAsString = OLD_VALUE_AS_STRING_EDEFAULT;

	/**
	 * The cached value of the '{@link #getOldValueAsContainmentList() <em>Old Value As Containment List</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOldValueAsContainmentList()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> oldValueAsContainmentList;

	/**
	 * The default value of the '{@link #getNewValue() <em>New Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewValue()
	 * @generated
	 * @ordered
	 */
	protected static final Object NEW_VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNewValue() <em>New Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewValue()
	 * @generated
	 * @ordered
	 */
	protected Object newValue = NEW_VALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getNewValueAsString() <em>New Value As String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewValueAsString()
	 * @generated
	 * @ordered
	 */
	protected static final String NEW_VALUE_AS_STRING_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNewValueAsString() <em>New Value As String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewValueAsString()
	 * @generated
	 * @ordered
	 */
	protected String newValueAsString = NEW_VALUE_AS_STRING_EDEFAULT;

	/**
	 * The cached value of the '{@link #getNewValueAsContainmentList() <em>New Value As Containment List</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewValueAsContainmentList()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> newValueAsContainmentList;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FeatureChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CodeSyncPackage.Literals.FEATURE_CHANGE;
	}

	/**
	 * @author Cristi
	 * @author Mariana
	 */
	private EDataType getEDataTypeOfFeature() {
		if (eContainer instanceof EStructuralFeatureToFeatureChangeEntryImpl) {
			EStructuralFeature feature = ((EStructuralFeatureToFeatureChangeEntryImpl) eContainer).getKey();
			if (feature instanceof EAttribute) {
				return ((EAttribute) feature).getEAttributeType();
			}
		}
		return null;
	}
	
	/**
	 * @author Mariana
	 */
	private boolean isMany() {
		if (eContainer instanceof EStructuralFeatureToFeatureChangeEntryImpl) {
			EStructuralFeature feature = ((EStructuralFeatureToFeatureChangeEntryImpl) eContainer).getKey();
			return feature.isMany();
		}
		return false;
	}
	
	/**
	 * @author Mariana
	 */
	private String encode(byte[] bytes) {
		return new String(new URLCodec().encode(bytes));
	}
	
	private byte[] decode(byte[] bytes) {
		try {
			return URLCodec.decodeUrl(bytes);
		} catch (DecoderException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Transient and lazy initialized on first access, by converting <code>*valueAsString</code>
	 * to Object (using the current feature).
	 * 
	 * @generated NOT
	 * @author Cristi
	 * @author Mariana
	 */
	public Object getOldValue() {
		if (oldValue == null) {
			EDataType eDataTypeOfFeature = getEDataTypeOfFeature();
			if (eDataTypeOfFeature != null && !isMany()) {
				oldValue = EcoreUtil.createFromString(eDataTypeOfFeature, getOldValueAsString());
			} else {
				if (getOldValueAsString() == null) {
					oldValue = getOldValueAsContainmentList();
				} else {
					try {
						ByteArrayInputStream input = new ByteArrayInputStream(decode(getOldValueAsString().getBytes()));
						ObjectInputStream ois = new ObjectInputStream(input);
						oldValue = ois.readObject();
						ois.close();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return oldValue;
	}

	/**
	 * Sets the cached value and sets also <code>*valueAsString</code> by converting the 
	 * value to string, using the current feature.
	 * 
	 * @generated NOT
	 * @author Cristi
	 * @author Mariana
	 */
	public void setOldValue(Object newOldValue) {
		Object oldOldValue = oldValue;
		oldValue = newOldValue;
		
		EDataType eDataTypeOfFeature = getEDataTypeOfFeature();
		if (eDataTypeOfFeature != null && !isMany()) {
			setOldValueAsString(EcoreUtil.convertToString(eDataTypeOfFeature, oldValue));
		} else {
			if (newOldValue instanceof Collection && eDataTypeOfFeature == null) {
				Collection<EObject> collection = (Collection<EObject>) newOldValue;
				getOldValueAsContainmentList().clear();
				for (EObject value : collection) {
					getOldValueAsContainmentList().add(EcoreUtil.copy(value));
				}
				oldValue = oldValueAsContainmentList;
			} else {
				try {
					ByteArrayOutputStream output = new ByteArrayOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(output);
					Collection collection = new ArrayList<>();
					collection.addAll((Collection) newOldValue);
					oos.writeObject(collection);
					oos.close();
					setOldValueAsString(encode(output.toByteArray()));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CodeSyncPackage.FEATURE_CHANGE__OLD_VALUE, oldOldValue, oldValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getOldValueAsString() {
		return oldValueAsString;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOldValueAsString(String newOldValueAsString) {
		String oldOldValueAsString = oldValueAsString;
		oldValueAsString = newOldValueAsString;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CodeSyncPackage.FEATURE_CHANGE__OLD_VALUE_AS_STRING, oldOldValueAsString, oldValueAsString));
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EObject> getOldValueAsContainmentList() {
		if (oldValueAsContainmentList == null) {
			oldValueAsContainmentList = new EObjectContainmentEList<EObject>(EObject.class, this, CodeSyncPackage.FEATURE_CHANGE__OLD_VALUE_AS_CONTAINMENT_LIST);
		}
		return oldValueAsContainmentList;
	}

	/**
	 * Transient and lazy initialized on first access, by converting <code>*valueAsString</code>
	 * to Object (using the current feature).
	 * 
	 * @generated NOT
	 * @author Cristi
	 * @author Mariana
	 */
	public Object getNewValue() {
		if (newValue == null) {
			EDataType eDataTypeOfFeature = getEDataTypeOfFeature();
			if (eDataTypeOfFeature != null && !isMany()) {
				newValue = EcoreUtil.createFromString(eDataTypeOfFeature, getNewValueAsString());
			} else {
				if (getNewValueAsString() == null) {
					newValue = getNewValueAsContainmentList();
				} else {
					try {
						ByteArrayInputStream input = new ByteArrayInputStream(decode(getNewValueAsString().getBytes()));
						ObjectInputStream ois = new ObjectInputStream(input);
						newValue = ois.readObject();
						ois.close();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return newValue;
	}

	/**
	 * Sets the cached value and sets also <code>*valueAsString</code> by converting the 
	 * value to string, using the current feature.
	 * 
	 * @generated NOT
	 * @author Cristi
	 * @author Mariana
	 */
	public void setNewValue(Object newNewValue) {
		Object oldNewValue = newValue;
		newValue = newNewValue;

		EDataType eDataTypeOfFeature = getEDataTypeOfFeature();
		if (eDataTypeOfFeature != null && !isMany()) {
			setNewValueAsString(EcoreUtil.convertToString(eDataTypeOfFeature, newValue));
		} else {
			if (newNewValue instanceof Collection && eDataTypeOfFeature == null) {
				Collection<EObject> collection = (Collection<EObject>) newNewValue;
				getNewValueAsContainmentList().clear();
				for (EObject value : collection) {
					getNewValueAsContainmentList().add(EcoreUtil.copy(value));
				}
				newValue = newValueAsContainmentList;
			} else {
				try {
					ByteArrayOutputStream output = new ByteArrayOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(output);
					Collection collection = new ArrayList<>();
					collection.addAll((Collection) newNewValue);
					oos.writeObject(collection);
					oos.close();
					setNewValueAsString(encode(output.toByteArray()));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CodeSyncPackage.FEATURE_CHANGE__NEW_VALUE, oldNewValue, newValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getNewValueAsString() {
		return newValueAsString;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewValueAsString(String newNewValueAsString) {
		String oldNewValueAsString = newValueAsString;
		newValueAsString = newNewValueAsString;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CodeSyncPackage.FEATURE_CHANGE__NEW_VALUE_AS_STRING, oldNewValueAsString, newValueAsString));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EObject> getNewValueAsContainmentList() {
		if (newValueAsContainmentList == null) {
			newValueAsContainmentList = new EObjectContainmentEList<EObject>(EObject.class, this, CodeSyncPackage.FEATURE_CHANGE__NEW_VALUE_AS_CONTAINMENT_LIST);
		}
		return newValueAsContainmentList;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CodeSyncPackage.FEATURE_CHANGE__OLD_VALUE_AS_CONTAINMENT_LIST:
				return ((InternalEList<?>)getOldValueAsContainmentList()).basicRemove(otherEnd, msgs);
			case CodeSyncPackage.FEATURE_CHANGE__NEW_VALUE_AS_CONTAINMENT_LIST:
				return ((InternalEList<?>)getNewValueAsContainmentList()).basicRemove(otherEnd, msgs);
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
			case CodeSyncPackage.FEATURE_CHANGE__OLD_VALUE:
				return getOldValue();
			case CodeSyncPackage.FEATURE_CHANGE__OLD_VALUE_AS_STRING:
				return getOldValueAsString();
			case CodeSyncPackage.FEATURE_CHANGE__OLD_VALUE_AS_CONTAINMENT_LIST:
				return getOldValueAsContainmentList();
			case CodeSyncPackage.FEATURE_CHANGE__NEW_VALUE:
				return getNewValue();
			case CodeSyncPackage.FEATURE_CHANGE__NEW_VALUE_AS_STRING:
				return getNewValueAsString();
			case CodeSyncPackage.FEATURE_CHANGE__NEW_VALUE_AS_CONTAINMENT_LIST:
				return getNewValueAsContainmentList();
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
			case CodeSyncPackage.FEATURE_CHANGE__OLD_VALUE:
				setOldValue(newValue);
				return;
			case CodeSyncPackage.FEATURE_CHANGE__OLD_VALUE_AS_STRING:
				setOldValueAsString((String)newValue);
				return;
			case CodeSyncPackage.FEATURE_CHANGE__OLD_VALUE_AS_CONTAINMENT_LIST:
				getOldValueAsContainmentList().clear();
				getOldValueAsContainmentList().addAll((Collection<? extends EObject>)newValue);
				return;
			case CodeSyncPackage.FEATURE_CHANGE__NEW_VALUE:
				setNewValue(newValue);
				return;
			case CodeSyncPackage.FEATURE_CHANGE__NEW_VALUE_AS_STRING:
				setNewValueAsString((String)newValue);
				return;
			case CodeSyncPackage.FEATURE_CHANGE__NEW_VALUE_AS_CONTAINMENT_LIST:
				getNewValueAsContainmentList().clear();
				getNewValueAsContainmentList().addAll((Collection<? extends EObject>)newValue);
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
			case CodeSyncPackage.FEATURE_CHANGE__OLD_VALUE:
				setOldValue(OLD_VALUE_EDEFAULT);
				return;
			case CodeSyncPackage.FEATURE_CHANGE__OLD_VALUE_AS_STRING:
				setOldValueAsString(OLD_VALUE_AS_STRING_EDEFAULT);
				return;
			case CodeSyncPackage.FEATURE_CHANGE__OLD_VALUE_AS_CONTAINMENT_LIST:
				getOldValueAsContainmentList().clear();
				return;
			case CodeSyncPackage.FEATURE_CHANGE__NEW_VALUE:
				setNewValue(NEW_VALUE_EDEFAULT);
				return;
			case CodeSyncPackage.FEATURE_CHANGE__NEW_VALUE_AS_STRING:
				setNewValueAsString(NEW_VALUE_AS_STRING_EDEFAULT);
				return;
			case CodeSyncPackage.FEATURE_CHANGE__NEW_VALUE_AS_CONTAINMENT_LIST:
				getNewValueAsContainmentList().clear();
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
			case CodeSyncPackage.FEATURE_CHANGE__OLD_VALUE:
				return OLD_VALUE_EDEFAULT == null ? oldValue != null : !OLD_VALUE_EDEFAULT.equals(oldValue);
			case CodeSyncPackage.FEATURE_CHANGE__OLD_VALUE_AS_STRING:
				return OLD_VALUE_AS_STRING_EDEFAULT == null ? oldValueAsString != null : !OLD_VALUE_AS_STRING_EDEFAULT.equals(oldValueAsString);
			case CodeSyncPackage.FEATURE_CHANGE__OLD_VALUE_AS_CONTAINMENT_LIST:
				return oldValueAsContainmentList != null && !oldValueAsContainmentList.isEmpty();
			case CodeSyncPackage.FEATURE_CHANGE__NEW_VALUE:
				return NEW_VALUE_EDEFAULT == null ? newValue != null : !NEW_VALUE_EDEFAULT.equals(newValue);
			case CodeSyncPackage.FEATURE_CHANGE__NEW_VALUE_AS_STRING:
				return NEW_VALUE_AS_STRING_EDEFAULT == null ? newValueAsString != null : !NEW_VALUE_AS_STRING_EDEFAULT.equals(newValueAsString);
			case CodeSyncPackage.FEATURE_CHANGE__NEW_VALUE_AS_CONTAINMENT_LIST:
				return newValueAsContainmentList != null && !newValueAsContainmentList.isEmpty();
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
		result.append(" (oldValue: ");
		result.append(oldValue);
		result.append(", oldValueAsString: ");
		result.append(oldValueAsString);
		result.append(", newValue: ");
		result.append(newValue);
		result.append(", newValueAsString: ");
		result.append(newValueAsString);
		result.append(')');
		return result.toString();
	}

} //FeatureChangeImpl