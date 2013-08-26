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

import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.crispico.flower.mp.model.codesync.AstCacheElement;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncFactory;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;
import com.crispico.flower.mp.model.codesync.FeatureChange;
import com.crispico.flower.mp.model.codesync.MindMapElement;
import com.crispico.flower.mp.model.codesync.MindMapRoot;
import com.crispico.flower.mp.model.codesync.ScenarioElement;
import java.io.Serializable;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CodeSyncPackageImpl extends EPackageImpl implements CodeSyncPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass codeSyncElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eStructuralFeatureToFeatureChangeEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass featureChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass codeSyncRootEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass astCacheElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass serializableEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass scenarioElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mindMapElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mindMapRootEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private CodeSyncPackageImpl() {
		super(eNS_URI, CodeSyncFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link CodeSyncPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static CodeSyncPackage init() {
		if (isInited) return (CodeSyncPackage)EPackage.Registry.INSTANCE.getEPackage(CodeSyncPackage.eNS_URI);

		// Obtain or create and register package
		CodeSyncPackageImpl theCodeSyncPackage = (CodeSyncPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof CodeSyncPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new CodeSyncPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		EcorePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theCodeSyncPackage.createPackageContents();

		// Initialize created meta-data
		theCodeSyncPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theCodeSyncPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(CodeSyncPackage.eNS_URI, theCodeSyncPackage);
		return theCodeSyncPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCodeSyncElement() {
		return codeSyncElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCodeSyncElement_Synchronized() {
		return (EAttribute)codeSyncElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCodeSyncElement_ChildrenSynchronized() {
		return (EAttribute)codeSyncElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCodeSyncElement_Added() {
		return (EAttribute)codeSyncElementEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCodeSyncElement_Deleted() {
		return (EAttribute)codeSyncElementEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCodeSyncElement_StatusFlags() {
		return (EAttribute)codeSyncElementEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCodeSyncElement_Name() {
		return (EAttribute)codeSyncElementEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCodeSyncElement_Type() {
		return (EAttribute)codeSyncElementEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCodeSyncElement_AstCacheElement() {
		return (EReference)codeSyncElementEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCodeSyncElement_Children() {
		return (EReference)codeSyncElementEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCodeSyncElement_FeatureChanges() {
		return (EReference)codeSyncElementEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEStructuralFeatureToFeatureChangeEntry() {
		return eStructuralFeatureToFeatureChangeEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEStructuralFeatureToFeatureChangeEntry_Key() {
		return (EReference)eStructuralFeatureToFeatureChangeEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEStructuralFeatureToFeatureChangeEntry_Value() {
		return (EReference)eStructuralFeatureToFeatureChangeEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFeatureChange() {
		return featureChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFeatureChange_OldValue() {
		return (EAttribute)featureChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFeatureChange_OldValueAsString() {
		return (EAttribute)featureChangeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFeatureChange_OldValueAsContainmentList() {
		return (EReference)featureChangeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFeatureChange_NewValue() {
		return (EAttribute)featureChangeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFeatureChange_NewValueAsString() {
		return (EAttribute)featureChangeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFeatureChange_NewValueAsContainmentList() {
		return (EReference)featureChangeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCodeSyncRoot() {
		return codeSyncRootEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAstCacheElement() {
		return astCacheElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAstCacheElement_CodeSyncElement() {
		return (EReference)astCacheElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSerializable() {
		return serializableEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getScenarioElement() {
		return scenarioElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScenarioElement_Number() {
		return (EAttribute)scenarioElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getScenarioElement_Interaction() {
		return (EReference)scenarioElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScenarioElement_Comment() {
		return (EAttribute)scenarioElementEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMindMapElement() {
		return mindMapElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMindMapElement_Icons() {
		return (EAttribute)mindMapElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMindMapElement_MinWidth() {
		return (EAttribute)mindMapElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMindMapElement_MaxWidth() {
		return (EAttribute)mindMapElementEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMindMapRoot() {
		return mindMapRootEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CodeSyncFactory getCodeSyncFactory() {
		return (CodeSyncFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		codeSyncElementEClass = createEClass(CODE_SYNC_ELEMENT);
		createEAttribute(codeSyncElementEClass, CODE_SYNC_ELEMENT__SYNCHRONIZED);
		createEAttribute(codeSyncElementEClass, CODE_SYNC_ELEMENT__CHILDREN_SYNCHRONIZED);
		createEAttribute(codeSyncElementEClass, CODE_SYNC_ELEMENT__ADDED);
		createEAttribute(codeSyncElementEClass, CODE_SYNC_ELEMENT__DELETED);
		createEAttribute(codeSyncElementEClass, CODE_SYNC_ELEMENT__STATUS_FLAGS);
		createEAttribute(codeSyncElementEClass, CODE_SYNC_ELEMENT__NAME);
		createEAttribute(codeSyncElementEClass, CODE_SYNC_ELEMENT__TYPE);
		createEReference(codeSyncElementEClass, CODE_SYNC_ELEMENT__AST_CACHE_ELEMENT);
		createEReference(codeSyncElementEClass, CODE_SYNC_ELEMENT__CHILDREN);
		createEReference(codeSyncElementEClass, CODE_SYNC_ELEMENT__FEATURE_CHANGES);

		eStructuralFeatureToFeatureChangeEntryEClass = createEClass(ESTRUCTURAL_FEATURE_TO_FEATURE_CHANGE_ENTRY);
		createEReference(eStructuralFeatureToFeatureChangeEntryEClass, ESTRUCTURAL_FEATURE_TO_FEATURE_CHANGE_ENTRY__KEY);
		createEReference(eStructuralFeatureToFeatureChangeEntryEClass, ESTRUCTURAL_FEATURE_TO_FEATURE_CHANGE_ENTRY__VALUE);

		featureChangeEClass = createEClass(FEATURE_CHANGE);
		createEAttribute(featureChangeEClass, FEATURE_CHANGE__OLD_VALUE);
		createEAttribute(featureChangeEClass, FEATURE_CHANGE__OLD_VALUE_AS_STRING);
		createEReference(featureChangeEClass, FEATURE_CHANGE__OLD_VALUE_AS_CONTAINMENT_LIST);
		createEAttribute(featureChangeEClass, FEATURE_CHANGE__NEW_VALUE);
		createEAttribute(featureChangeEClass, FEATURE_CHANGE__NEW_VALUE_AS_STRING);
		createEReference(featureChangeEClass, FEATURE_CHANGE__NEW_VALUE_AS_CONTAINMENT_LIST);

		codeSyncRootEClass = createEClass(CODE_SYNC_ROOT);

		astCacheElementEClass = createEClass(AST_CACHE_ELEMENT);
		createEReference(astCacheElementEClass, AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT);

		serializableEClass = createEClass(SERIALIZABLE);

		scenarioElementEClass = createEClass(SCENARIO_ELEMENT);
		createEAttribute(scenarioElementEClass, SCENARIO_ELEMENT__NUMBER);
		createEReference(scenarioElementEClass, SCENARIO_ELEMENT__INTERACTION);
		createEAttribute(scenarioElementEClass, SCENARIO_ELEMENT__COMMENT);

		mindMapElementEClass = createEClass(MIND_MAP_ELEMENT);
		createEAttribute(mindMapElementEClass, MIND_MAP_ELEMENT__ICONS);
		createEAttribute(mindMapElementEClass, MIND_MAP_ELEMENT__MIN_WIDTH);
		createEAttribute(mindMapElementEClass, MIND_MAP_ELEMENT__MAX_WIDTH);

		mindMapRootEClass = createEClass(MIND_MAP_ROOT);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		codeSyncRootEClass.getESuperTypes().add(this.getCodeSyncElement());
		astCacheElementEClass.getESuperTypes().add(this.getSerializable());
		scenarioElementEClass.getESuperTypes().add(this.getCodeSyncElement());
		mindMapElementEClass.getESuperTypes().add(this.getCodeSyncElement());
		mindMapRootEClass.getESuperTypes().add(this.getCodeSyncRoot());
		mindMapRootEClass.getESuperTypes().add(this.getMindMapElement());

		// Initialize classes and features; add operations and parameters
		initEClass(codeSyncElementEClass, CodeSyncElement.class, "CodeSyncElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCodeSyncElement_Synchronized(), ecorePackage.getEBoolean(), "synchronized", null, 0, 1, CodeSyncElement.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getCodeSyncElement_ChildrenSynchronized(), ecorePackage.getEBoolean(), "childrenSynchronized", null, 0, 1, CodeSyncElement.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getCodeSyncElement_Added(), ecorePackage.getEBoolean(), "added", null, 0, 1, CodeSyncElement.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getCodeSyncElement_Deleted(), ecorePackage.getEBoolean(), "deleted", null, 0, 1, CodeSyncElement.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getCodeSyncElement_StatusFlags(), ecorePackage.getEInt(), "statusFlags", null, 0, 1, CodeSyncElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCodeSyncElement_Name(), ecorePackage.getEString(), "name", null, 0, 1, CodeSyncElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCodeSyncElement_Type(), ecorePackage.getEString(), "type", null, 0, 1, CodeSyncElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCodeSyncElement_AstCacheElement(), this.getAstCacheElement(), this.getAstCacheElement_CodeSyncElement(), "astCacheElement", null, 0, 1, CodeSyncElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCodeSyncElement_Children(), this.getCodeSyncElement(), null, "children", null, 0, -1, CodeSyncElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCodeSyncElement_FeatureChanges(), this.getEStructuralFeatureToFeatureChangeEntry(), null, "featureChanges", null, 0, -1, CodeSyncElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eStructuralFeatureToFeatureChangeEntryEClass, Map.Entry.class, "EStructuralFeatureToFeatureChangeEntry", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEStructuralFeatureToFeatureChangeEntry_Key(), theEcorePackage.getEStructuralFeature(), null, "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEStructuralFeatureToFeatureChangeEntry_Value(), this.getFeatureChange(), null, "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(featureChangeEClass, FeatureChange.class, "FeatureChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFeatureChange_OldValue(), ecorePackage.getEJavaObject(), "oldValue", null, 0, 1, FeatureChange.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getFeatureChange_OldValueAsString(), ecorePackage.getEString(), "oldValueAsString", null, 0, 1, FeatureChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFeatureChange_OldValueAsContainmentList(), theEcorePackage.getEObject(), null, "oldValueAsContainmentList", null, 0, -1, FeatureChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFeatureChange_NewValue(), ecorePackage.getEJavaObject(), "newValue", null, 0, 1, FeatureChange.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getFeatureChange_NewValueAsString(), ecorePackage.getEString(), "newValueAsString", null, 0, 1, FeatureChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFeatureChange_NewValueAsContainmentList(), theEcorePackage.getEObject(), null, "newValueAsContainmentList", null, 0, -1, FeatureChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(codeSyncRootEClass, CodeSyncRoot.class, "CodeSyncRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(astCacheElementEClass, AstCacheElement.class, "AstCacheElement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAstCacheElement_CodeSyncElement(), this.getCodeSyncElement(), this.getCodeSyncElement_AstCacheElement(), "codeSyncElement", null, 0, 1, AstCacheElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(serializableEClass, Serializable.class, "Serializable", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);

		initEClass(scenarioElementEClass, ScenarioElement.class, "ScenarioElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getScenarioElement_Number(), theEcorePackage.getEString(), "number", null, 0, 1, ScenarioElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getScenarioElement_Interaction(), this.getCodeSyncElement(), null, "interaction", null, 0, 1, ScenarioElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioElement_Comment(), theEcorePackage.getEString(), "comment", null, 0, 1, ScenarioElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mindMapElementEClass, MindMapElement.class, "MindMapElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMindMapElement_Icons(), theEcorePackage.getEString(), "icons", null, 0, -1, MindMapElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMindMapElement_MinWidth(), ecorePackage.getELongObject(), "minWidth", "1", 0, 1, MindMapElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMindMapElement_MaxWidth(), theEcorePackage.getELongObject(), "maxWidth", "600", 0, 1, MindMapElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mindMapRootEClass, MindMapRoot.class, "MindMapRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //CodeSyncPackageImpl