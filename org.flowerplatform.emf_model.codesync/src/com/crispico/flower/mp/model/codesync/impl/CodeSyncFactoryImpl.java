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

import com.crispico.flower.mp.model.codesync.*;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncFactory;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;
import com.crispico.flower.mp.model.codesync.FeatureChange;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CodeSyncFactoryImpl extends EFactoryImpl implements CodeSyncFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static CodeSyncFactory init() {
		try {
			CodeSyncFactory theCodeSyncFactory = (CodeSyncFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.flower-platform.com/xmi/codesync_1.0.0"); 
			if (theCodeSyncFactory != null) {
				return theCodeSyncFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new CodeSyncFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CodeSyncFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case CodeSyncPackage.CODE_SYNC_ELEMENT: return createCodeSyncElement();
			case CodeSyncPackage.ESTRUCTURAL_FEATURE_TO_FEATURE_CHANGE_ENTRY: return (EObject)createEStructuralFeatureToFeatureChangeEntry();
			case CodeSyncPackage.FEATURE_CHANGE: return createFeatureChange();
			case CodeSyncPackage.CODE_SYNC_ROOT: return createCodeSyncRoot();
			case CodeSyncPackage.SCENARIO_ELEMENT: return createScenarioElement();
			case CodeSyncPackage.MIND_MAP_ELEMENT: return createMindMapElement();
			case CodeSyncPackage.MIND_MAP_ROOT: return createMindMapRoot();
			case CodeSyncPackage.RELATION: return createRelation();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CodeSyncElement createCodeSyncElement() {
		CodeSyncElementImpl codeSyncElement = new CodeSyncElementImpl();
		return codeSyncElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<EStructuralFeature, FeatureChange> createEStructuralFeatureToFeatureChangeEntry() {
		EStructuralFeatureToFeatureChangeEntryImpl eStructuralFeatureToFeatureChangeEntry = new EStructuralFeatureToFeatureChangeEntryImpl();
		return eStructuralFeatureToFeatureChangeEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureChange createFeatureChange() {
		FeatureChangeImpl featureChange = new FeatureChangeImpl();
		return featureChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CodeSyncRoot createCodeSyncRoot() {
		CodeSyncRootImpl codeSyncRoot = new CodeSyncRootImpl();
		return codeSyncRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioElement createScenarioElement() {
		ScenarioElementImpl scenarioElement = new ScenarioElementImpl();
		return scenarioElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MindMapElement createMindMapElement() {
		MindMapElementImpl mindMapElement = new MindMapElementImpl();
		return mindMapElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MindMapRoot createMindMapRoot() {
		MindMapRootImpl mindMapRoot = new MindMapRootImpl();
		return mindMapRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Relation createRelation() {
		RelationImpl relation = new RelationImpl();
		return relation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CodeSyncPackage getCodeSyncPackage() {
		return (CodeSyncPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static CodeSyncPackage getPackage() {
		return CodeSyncPackage.eINSTANCE;
	}

} //CodeSyncFactoryImpl