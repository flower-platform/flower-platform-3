/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.crispico.flower.mp.model.astcache.code.impl;

import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;
import com.crispico.flower.mp.model.astcache.code.DocumentableElement;
import com.crispico.flower.mp.model.astcache.code.EnumConstant;
import com.crispico.flower.mp.model.astcache.code.ExtendedModifier;
import com.crispico.flower.mp.model.astcache.code.ModifiableElement;

import com.crispico.flower.mp.model.codesync.impl.AstCacheElementImpl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Enum Constant</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.crispico.flower.mp.model.astcache.code.impl.EnumConstantImpl#getModifiers <em>Modifiers</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.astcache.code.impl.EnumConstantImpl#getDocumentation <em>Documentation</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.astcache.code.impl.EnumConstantImpl#getArguments <em>Arguments</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EnumConstantImpl extends AstCacheElementImpl implements EnumConstant {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The cached value of the '{@link #getModifiers() <em>Modifiers</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModifiers()
	 * @generated
	 * @ordered
	 */
	protected EList<ExtendedModifier> modifiers;

	/**
	 * The default value of the '{@link #getDocumentation() <em>Documentation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDocumentation()
	 * @generated
	 * @ordered
	 */
	protected static final String DOCUMENTATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDocumentation() <em>Documentation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDocumentation()
	 * @generated
	 * @ordered
	 */
	protected String documentation = DOCUMENTATION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getArguments() <em>Arguments</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArguments()
	 * @generated
	 * @ordered
	 */
	protected EList<String> arguments;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EnumConstantImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AstCacheCodePackage.Literals.ENUM_CONSTANT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ExtendedModifier> getModifiers() {
		if (modifiers == null) {
			modifiers = new EObjectContainmentEList<ExtendedModifier>(ExtendedModifier.class, this, AstCacheCodePackage.ENUM_CONSTANT__MODIFIERS);
		}
		return modifiers;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDocumentation() {
		return documentation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDocumentation(String newDocumentation) {
		String oldDocumentation = documentation;
		documentation = newDocumentation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AstCacheCodePackage.ENUM_CONSTANT__DOCUMENTATION, oldDocumentation, documentation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getArguments() {
		if (arguments == null) {
			arguments = new EDataTypeUniqueEList<String>(String.class, this, AstCacheCodePackage.ENUM_CONSTANT__ARGUMENTS);
		}
		return arguments;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AstCacheCodePackage.ENUM_CONSTANT__MODIFIERS:
				return ((InternalEList<?>)getModifiers()).basicRemove(otherEnd, msgs);
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
			case AstCacheCodePackage.ENUM_CONSTANT__MODIFIERS:
				return getModifiers();
			case AstCacheCodePackage.ENUM_CONSTANT__DOCUMENTATION:
				return getDocumentation();
			case AstCacheCodePackage.ENUM_CONSTANT__ARGUMENTS:
				return getArguments();
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
			case AstCacheCodePackage.ENUM_CONSTANT__MODIFIERS:
				getModifiers().clear();
				getModifiers().addAll((Collection<? extends ExtendedModifier>)newValue);
				return;
			case AstCacheCodePackage.ENUM_CONSTANT__DOCUMENTATION:
				setDocumentation((String)newValue);
				return;
			case AstCacheCodePackage.ENUM_CONSTANT__ARGUMENTS:
				getArguments().clear();
				getArguments().addAll((Collection<? extends String>)newValue);
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
			case AstCacheCodePackage.ENUM_CONSTANT__MODIFIERS:
				getModifiers().clear();
				return;
			case AstCacheCodePackage.ENUM_CONSTANT__DOCUMENTATION:
				setDocumentation(DOCUMENTATION_EDEFAULT);
				return;
			case AstCacheCodePackage.ENUM_CONSTANT__ARGUMENTS:
				getArguments().clear();
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
			case AstCacheCodePackage.ENUM_CONSTANT__MODIFIERS:
				return modifiers != null && !modifiers.isEmpty();
			case AstCacheCodePackage.ENUM_CONSTANT__DOCUMENTATION:
				return DOCUMENTATION_EDEFAULT == null ? documentation != null : !DOCUMENTATION_EDEFAULT.equals(documentation);
			case AstCacheCodePackage.ENUM_CONSTANT__ARGUMENTS:
				return arguments != null && !arguments.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == ModifiableElement.class) {
			switch (derivedFeatureID) {
				case AstCacheCodePackage.ENUM_CONSTANT__MODIFIERS: return AstCacheCodePackage.MODIFIABLE_ELEMENT__MODIFIERS;
				default: return -1;
			}
		}
		if (baseClass == DocumentableElement.class) {
			switch (derivedFeatureID) {
				case AstCacheCodePackage.ENUM_CONSTANT__DOCUMENTATION: return AstCacheCodePackage.DOCUMENTABLE_ELEMENT__DOCUMENTATION;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == ModifiableElement.class) {
			switch (baseFeatureID) {
				case AstCacheCodePackage.MODIFIABLE_ELEMENT__MODIFIERS: return AstCacheCodePackage.ENUM_CONSTANT__MODIFIERS;
				default: return -1;
			}
		}
		if (baseClass == DocumentableElement.class) {
			switch (baseFeatureID) {
				case AstCacheCodePackage.DOCUMENTABLE_ELEMENT__DOCUMENTATION: return AstCacheCodePackage.ENUM_CONSTANT__DOCUMENTATION;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
		result.append(" (documentation: ");
		result.append(documentation);
		result.append(", arguments: ");
		result.append(arguments);
		result.append(')');
		return result.toString();
	}

} //EnumConstantImpl