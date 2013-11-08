/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.emf_model.dual_resource.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.flowerplatform.emf_model.dual_resource.DualResourcePackage;
import org.flowerplatform.emf_model.dual_resource.TestReferenceHolder;
import org.flowerplatform.emf_model.dual_resource.TestReferencedObject;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Test Reference Holder</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.flowerplatform.emf_model.dual_resource.impl.TestReferenceHolderImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.dual_resource.impl.TestReferenceHolderImpl#getChildren <em>Children</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.dual_resource.impl.TestReferenceHolderImpl#getReferencedObject <em>Referenced Object</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TestReferenceHolderImpl extends DualResourceObjectImpl implements TestReferenceHolder {
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
	 * The cached value of the '{@link #getChildren() <em>Children</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildren()
	 * @generated
	 * @ordered
	 */
	protected EList<TestReferenceHolder> children;

	/**
	 * The cached value of the '{@link #getReferencedObject() <em>Referenced Object</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferencedObject()
	 * @generated
	 * @ordered
	 */
	protected TestReferencedObject referencedObject;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TestReferenceHolderImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DualResourcePackage.Literals.TEST_REFERENCE_HOLDER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TestReferencedObject getReferencedObject() {
		if (referencedObject != null && referencedObject.eIsProxy()) {
			InternalEObject oldReferencedObject = (InternalEObject)referencedObject;
			referencedObject = (TestReferencedObject)eResolveProxy(oldReferencedObject);
			if (referencedObject != oldReferencedObject) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DualResourcePackage.TEST_REFERENCE_HOLDER__REFERENCED_OBJECT, oldReferencedObject, referencedObject));
			}
		}
		return referencedObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TestReferencedObject basicGetReferencedObject() {
		return referencedObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReferencedObject(TestReferencedObject newReferencedObject) {
		TestReferencedObject oldReferencedObject = referencedObject;
		referencedObject = newReferencedObject;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DualResourcePackage.TEST_REFERENCE_HOLDER__REFERENCED_OBJECT, oldReferencedObject, referencedObject));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DualResourcePackage.TEST_REFERENCE_HOLDER__CHILDREN:
				return ((InternalEList<?>)getChildren()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
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
			eNotify(new ENotificationImpl(this, Notification.SET, DualResourcePackage.TEST_REFERENCE_HOLDER__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TestReferenceHolder> getChildren() {
		if (children == null) {
			children = new EObjectContainmentEList<TestReferenceHolder>(TestReferenceHolder.class, this, DualResourcePackage.TEST_REFERENCE_HOLDER__CHILDREN);
		}
		return children;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DualResourcePackage.TEST_REFERENCE_HOLDER__NAME:
				return getName();
			case DualResourcePackage.TEST_REFERENCE_HOLDER__CHILDREN:
				return getChildren();
			case DualResourcePackage.TEST_REFERENCE_HOLDER__REFERENCED_OBJECT:
				if (resolve) return getReferencedObject();
				return basicGetReferencedObject();
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
			case DualResourcePackage.TEST_REFERENCE_HOLDER__NAME:
				setName((String)newValue);
				return;
			case DualResourcePackage.TEST_REFERENCE_HOLDER__CHILDREN:
				getChildren().clear();
				getChildren().addAll((Collection<? extends TestReferenceHolder>)newValue);
				return;
			case DualResourcePackage.TEST_REFERENCE_HOLDER__REFERENCED_OBJECT:
				setReferencedObject((TestReferencedObject)newValue);
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
			case DualResourcePackage.TEST_REFERENCE_HOLDER__NAME:
				setName(NAME_EDEFAULT);
				return;
			case DualResourcePackage.TEST_REFERENCE_HOLDER__CHILDREN:
				getChildren().clear();
				return;
			case DualResourcePackage.TEST_REFERENCE_HOLDER__REFERENCED_OBJECT:
				setReferencedObject((TestReferencedObject)null);
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
			case DualResourcePackage.TEST_REFERENCE_HOLDER__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case DualResourcePackage.TEST_REFERENCE_HOLDER__CHILDREN:
				return children != null && !children.isEmpty();
			case DualResourcePackage.TEST_REFERENCE_HOLDER__REFERENCED_OBJECT:
				return referencedObject != null;
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
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //TestReferenceHolderImpl
