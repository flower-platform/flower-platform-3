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
package com.crispico.flower.mp.model.astcache.code;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.crispico.flower.mp.model.astcache.code.AstCacheCodeFactory
 * @model kind="package"
 * @generated
 */
public interface AstCacheCodePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "code";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.flower-platform.com/xmi/astcache_code_1.0.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "astcache_code";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	AstCacheCodePackage eINSTANCE = com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl.init();

	/**
	 * The meta object id for the '{@link com.crispico.flower.mp.model.astcache.code.impl.ClassImpl <em>Class</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.crispico.flower.mp.model.astcache.code.impl.ClassImpl
	 * @see com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl#getClass_()
	 * @generated
	 */
	int CLASS = 7;

	/**
	 * The meta object id for the '{@link com.crispico.flower.mp.model.astcache.code.impl.AttributeImpl <em>Attribute</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.crispico.flower.mp.model.astcache.code.impl.AttributeImpl
	 * @see com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl#getAttribute()
	 * @generated
	 */
	int ATTRIBUTE = 8;

	/**
	 * The meta object id for the '{@link com.crispico.flower.mp.model.astcache.code.impl.OperationImpl <em>Operation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.crispico.flower.mp.model.astcache.code.impl.OperationImpl
	 * @see com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl#getOperation()
	 * @generated
	 */
	int OPERATION = 9;

	/**
	 * The meta object id for the '{@link com.crispico.flower.mp.model.astcache.code.DocumentableElement <em>Documentable Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.crispico.flower.mp.model.astcache.code.DocumentableElement
	 * @see com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl#getDocumentableElement()
	 * @generated
	 */
	int DOCUMENTABLE_ELEMENT = 0;

	/**
	 * The feature id for the '<em><b>Documentation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENTABLE_ELEMENT__DOCUMENTATION = 0;

	/**
	 * The number of structural features of the '<em>Documentable Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENTABLE_ELEMENT_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link com.crispico.flower.mp.model.astcache.code.TypedElement <em>Typed Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.crispico.flower.mp.model.astcache.code.TypedElement
	 * @see com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl#getTypedElement()
	 * @generated
	 */
	int TYPED_ELEMENT = 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPED_ELEMENT__TYPE = 0;

	/**
	 * The number of structural features of the '<em>Typed Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPED_ELEMENT_FEATURE_COUNT = 1;


	/**
	 * The meta object id for the '{@link com.crispico.flower.mp.model.astcache.code.ModifiableElement <em>Modifiable Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.crispico.flower.mp.model.astcache.code.ModifiableElement
	 * @see com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl#getModifiableElement()
	 * @generated
	 */
	int MODIFIABLE_ELEMENT = 2;

	/**
	 * The feature id for the '<em><b>Modifiers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODIFIABLE_ELEMENT__MODIFIERS = 0;

	/**
	 * The number of structural features of the '<em>Modifiable Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODIFIABLE_ELEMENT_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link com.crispico.flower.mp.model.astcache.code.impl.ExtendedModifierImpl <em>Extended Modifier</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.crispico.flower.mp.model.astcache.code.impl.ExtendedModifierImpl
	 * @see com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl#getExtendedModifier()
	 * @generated
	 */
	int EXTENDED_MODIFIER = 3;

	/**
	 * The feature id for the '<em><b>Code Sync Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTENDED_MODIFIER__CODE_SYNC_ELEMENT = CodeSyncPackage.AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT;

	/**
	 * The number of structural features of the '<em>Extended Modifier</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTENDED_MODIFIER_FEATURE_COUNT = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.crispico.flower.mp.model.astcache.code.impl.ModifierImpl <em>Modifier</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.crispico.flower.mp.model.astcache.code.impl.ModifierImpl
	 * @see com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl#getModifier()
	 * @generated
	 */
	int MODIFIER = 4;

	/**
	 * The feature id for the '<em><b>Code Sync Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODIFIER__CODE_SYNC_ELEMENT = EXTENDED_MODIFIER__CODE_SYNC_ELEMENT;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODIFIER__TYPE = EXTENDED_MODIFIER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Modifier</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODIFIER_FEATURE_COUNT = EXTENDED_MODIFIER_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.crispico.flower.mp.model.astcache.code.impl.AnnotationImpl <em>Annotation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.crispico.flower.mp.model.astcache.code.impl.AnnotationImpl
	 * @see com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl#getAnnotation()
	 * @generated
	 */
	int ANNOTATION = 5;

	/**
	 * The feature id for the '<em><b>Code Sync Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION__CODE_SYNC_ELEMENT = EXTENDED_MODIFIER__CODE_SYNC_ELEMENT;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION__NAME = EXTENDED_MODIFIER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Values</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION__VALUES = EXTENDED_MODIFIER_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Annotation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_FEATURE_COUNT = EXTENDED_MODIFIER_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.crispico.flower.mp.model.astcache.code.impl.AnnotationValueImpl <em>Annotation Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.crispico.flower.mp.model.astcache.code.impl.AnnotationValueImpl
	 * @see com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl#getAnnotationValue()
	 * @generated
	 */
	int ANNOTATION_VALUE = 6;

	/**
	 * The feature id for the '<em><b>Code Sync Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_VALUE__CODE_SYNC_ELEMENT = CodeSyncPackage.AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_VALUE__NAME = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_VALUE__VALUE = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Annotation Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_VALUE_FEATURE_COUNT = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Code Sync Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS__CODE_SYNC_ELEMENT = CodeSyncPackage.AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT;

	/**
	 * The feature id for the '<em><b>Modifiers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS__MODIFIERS = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Documentation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS__DOCUMENTATION = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Super Classes</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS__SUPER_CLASSES = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Super Interfaces</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS__SUPER_INTERFACES = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Class</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_FEATURE_COUNT = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Code Sync Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__CODE_SYNC_ELEMENT = CodeSyncPackage.AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT;

	/**
	 * The feature id for the '<em><b>Modifiers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__MODIFIERS = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Documentation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__DOCUMENTATION = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__TYPE = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Initializer</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__INITIALIZER = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_FEATURE_COUNT = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Code Sync Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION__CODE_SYNC_ELEMENT = CodeSyncPackage.AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT;

	/**
	 * The feature id for the '<em><b>Modifiers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION__MODIFIERS = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Documentation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION__DOCUMENTATION = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION__TYPE = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION__PARAMETERS = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Operation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_FEATURE_COUNT = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link com.crispico.flower.mp.model.astcache.code.impl.ParameterImpl <em>Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.crispico.flower.mp.model.astcache.code.impl.ParameterImpl
	 * @see com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl#getParameter()
	 * @generated
	 */
	int PARAMETER = 10;

	/**
	 * The feature id for the '<em><b>Code Sync Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__CODE_SYNC_ELEMENT = CodeSyncPackage.AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT;

	/**
	 * The feature id for the '<em><b>Modifiers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__MODIFIERS = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__TYPE = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__NAME = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_FEATURE_COUNT = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 3;


	/**
	 * The meta object id for the '{@link com.crispico.flower.mp.model.astcache.code.impl.EnumConstantImpl <em>Enum Constant</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.crispico.flower.mp.model.astcache.code.impl.EnumConstantImpl
	 * @see com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl#getEnumConstant()
	 * @generated
	 */
	int ENUM_CONSTANT = 11;

	/**
	 * The feature id for the '<em><b>Code Sync Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_CONSTANT__CODE_SYNC_ELEMENT = CodeSyncPackage.AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT;

	/**
	 * The feature id for the '<em><b>Modifiers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_CONSTANT__MODIFIERS = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Documentation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_CONSTANT__DOCUMENTATION = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Arguments</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_CONSTANT__ARGUMENTS = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Enum Constant</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_CONSTANT_FEATURE_COUNT = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.crispico.flower.mp.model.astcache.code.impl.AnnotationMemberImpl <em>Annotation Member</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.crispico.flower.mp.model.astcache.code.impl.AnnotationMemberImpl
	 * @see com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl#getAnnotationMember()
	 * @generated
	 */
	int ANNOTATION_MEMBER = 12;

	/**
	 * The feature id for the '<em><b>Code Sync Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_MEMBER__CODE_SYNC_ELEMENT = CodeSyncPackage.AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT;

	/**
	 * The feature id for the '<em><b>Modifiers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_MEMBER__MODIFIERS = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Documentation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_MEMBER__DOCUMENTATION = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_MEMBER__TYPE = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_MEMBER__DEFAULT_VALUE = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Annotation Member</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_MEMBER_FEATURE_COUNT = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 4;


	/**
	 * Returns the meta object for class '{@link com.crispico.flower.mp.model.astcache.code.Class <em>Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Class</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.Class
	 * @generated
	 */
	EClass getClass_();

	/**
	 * Returns the meta object for the attribute list '{@link com.crispico.flower.mp.model.astcache.code.Class#getSuperClasses <em>Super Classes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Super Classes</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.Class#getSuperClasses()
	 * @see #getClass_()
	 * @generated
	 */
	EAttribute getClass_SuperClasses();

	/**
	 * Returns the meta object for the attribute list '{@link com.crispico.flower.mp.model.astcache.code.Class#getSuperInterfaces <em>Super Interfaces</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Super Interfaces</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.Class#getSuperInterfaces()
	 * @see #getClass_()
	 * @generated
	 */
	EAttribute getClass_SuperInterfaces();

	/**
	 * Returns the meta object for class '{@link com.crispico.flower.mp.model.astcache.code.Attribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attribute</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.Attribute
	 * @generated
	 */
	EClass getAttribute();

	/**
	 * Returns the meta object for the attribute '{@link com.crispico.flower.mp.model.astcache.code.Attribute#getInitializer <em>Initializer</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Initializer</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.Attribute#getInitializer()
	 * @see #getAttribute()
	 * @generated
	 */
	EAttribute getAttribute_Initializer();

	/**
	 * Returns the meta object for class '{@link com.crispico.flower.mp.model.astcache.code.Operation <em>Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Operation</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.Operation
	 * @generated
	 */
	EClass getOperation();

	/**
	 * Returns the meta object for the containment reference list '{@link com.crispico.flower.mp.model.astcache.code.Operation#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameters</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.Operation#getParameters()
	 * @see #getOperation()
	 * @generated
	 */
	EReference getOperation_Parameters();

	/**
	 * Returns the meta object for class '{@link com.crispico.flower.mp.model.astcache.code.DocumentableElement <em>Documentable Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Documentable Element</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.DocumentableElement
	 * @generated
	 */
	EClass getDocumentableElement();

	/**
	 * Returns the meta object for the attribute '{@link com.crispico.flower.mp.model.astcache.code.DocumentableElement#getDocumentation <em>Documentation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Documentation</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.DocumentableElement#getDocumentation()
	 * @see #getDocumentableElement()
	 * @generated
	 */
	EAttribute getDocumentableElement_Documentation();

	/**
	 * Returns the meta object for class '{@link com.crispico.flower.mp.model.astcache.code.TypedElement <em>Typed Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Typed Element</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.TypedElement
	 * @generated
	 */
	EClass getTypedElement();

	/**
	 * Returns the meta object for the attribute '{@link com.crispico.flower.mp.model.astcache.code.TypedElement#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.TypedElement#getType()
	 * @see #getTypedElement()
	 * @generated
	 */
	EAttribute getTypedElement_Type();

	/**
	 * Returns the meta object for class '{@link com.crispico.flower.mp.model.astcache.code.ModifiableElement <em>Modifiable Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Modifiable Element</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.ModifiableElement
	 * @generated
	 */
	EClass getModifiableElement();

	/**
	 * Returns the meta object for the containment reference list '{@link com.crispico.flower.mp.model.astcache.code.ModifiableElement#getModifiers <em>Modifiers</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Modifiers</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.ModifiableElement#getModifiers()
	 * @see #getModifiableElement()
	 * @generated
	 */
	EReference getModifiableElement_Modifiers();

	/**
	 * Returns the meta object for class '{@link com.crispico.flower.mp.model.astcache.code.ExtendedModifier <em>Extended Modifier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Extended Modifier</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.ExtendedModifier
	 * @generated
	 */
	EClass getExtendedModifier();

	/**
	 * Returns the meta object for class '{@link com.crispico.flower.mp.model.astcache.code.Modifier <em>Modifier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Modifier</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.Modifier
	 * @generated
	 */
	EClass getModifier();

	/**
	 * Returns the meta object for the attribute '{@link com.crispico.flower.mp.model.astcache.code.Modifier#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.Modifier#getType()
	 * @see #getModifier()
	 * @generated
	 */
	EAttribute getModifier_Type();

	/**
	 * Returns the meta object for class '{@link com.crispico.flower.mp.model.astcache.code.Annotation <em>Annotation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Annotation</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.Annotation
	 * @generated
	 */
	EClass getAnnotation();

	/**
	 * Returns the meta object for the attribute '{@link com.crispico.flower.mp.model.astcache.code.Annotation#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.Annotation#getName()
	 * @see #getAnnotation()
	 * @generated
	 */
	EAttribute getAnnotation_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link com.crispico.flower.mp.model.astcache.code.Annotation#getValues <em>Values</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Values</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.Annotation#getValues()
	 * @see #getAnnotation()
	 * @generated
	 */
	EReference getAnnotation_Values();

	/**
	 * Returns the meta object for class '{@link com.crispico.flower.mp.model.astcache.code.AnnotationValue <em>Annotation Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Annotation Value</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.AnnotationValue
	 * @generated
	 */
	EClass getAnnotationValue();

	/**
	 * Returns the meta object for the attribute '{@link com.crispico.flower.mp.model.astcache.code.AnnotationValue#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.AnnotationValue#getName()
	 * @see #getAnnotationValue()
	 * @generated
	 */
	EAttribute getAnnotationValue_Name();

	/**
	 * Returns the meta object for the attribute '{@link com.crispico.flower.mp.model.astcache.code.AnnotationValue#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.AnnotationValue#getValue()
	 * @see #getAnnotationValue()
	 * @generated
	 */
	EAttribute getAnnotationValue_Value();

	/**
	 * Returns the meta object for class '{@link com.crispico.flower.mp.model.astcache.code.Parameter <em>Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parameter</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.Parameter
	 * @generated
	 */
	EClass getParameter();

	/**
	 * Returns the meta object for the attribute '{@link com.crispico.flower.mp.model.astcache.code.Parameter#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.Parameter#getName()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_Name();

	/**
	 * Returns the meta object for class '{@link com.crispico.flower.mp.model.astcache.code.EnumConstant <em>Enum Constant</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Enum Constant</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.EnumConstant
	 * @generated
	 */
	EClass getEnumConstant();

	/**
	 * Returns the meta object for the attribute list '{@link com.crispico.flower.mp.model.astcache.code.EnumConstant#getArguments <em>Arguments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Arguments</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.EnumConstant#getArguments()
	 * @see #getEnumConstant()
	 * @generated
	 */
	EAttribute getEnumConstant_Arguments();

	/**
	 * Returns the meta object for class '{@link com.crispico.flower.mp.model.astcache.code.AnnotationMember <em>Annotation Member</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Annotation Member</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.AnnotationMember
	 * @generated
	 */
	EClass getAnnotationMember();

	/**
	 * Returns the meta object for the attribute '{@link com.crispico.flower.mp.model.astcache.code.AnnotationMember#getDefaultValue <em>Default Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Value</em>'.
	 * @see com.crispico.flower.mp.model.astcache.code.AnnotationMember#getDefaultValue()
	 * @see #getAnnotationMember()
	 * @generated
	 */
	EAttribute getAnnotationMember_DefaultValue();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	AstCacheCodeFactory getAstCacheCodeFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link com.crispico.flower.mp.model.astcache.code.impl.ClassImpl <em>Class</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.crispico.flower.mp.model.astcache.code.impl.ClassImpl
		 * @see com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl#getClass_()
		 * @generated
		 */
		EClass CLASS = eINSTANCE.getClass_();

		/**
		 * The meta object literal for the '<em><b>Super Classes</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CLASS__SUPER_CLASSES = eINSTANCE.getClass_SuperClasses();

		/**
		 * The meta object literal for the '<em><b>Super Interfaces</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CLASS__SUPER_INTERFACES = eINSTANCE.getClass_SuperInterfaces();

		/**
		 * The meta object literal for the '{@link com.crispico.flower.mp.model.astcache.code.impl.AttributeImpl <em>Attribute</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.crispico.flower.mp.model.astcache.code.impl.AttributeImpl
		 * @see com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl#getAttribute()
		 * @generated
		 */
		EClass ATTRIBUTE = eINSTANCE.getAttribute();

		/**
		 * The meta object literal for the '<em><b>Initializer</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTRIBUTE__INITIALIZER = eINSTANCE.getAttribute_Initializer();

		/**
		 * The meta object literal for the '{@link com.crispico.flower.mp.model.astcache.code.impl.OperationImpl <em>Operation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.crispico.flower.mp.model.astcache.code.impl.OperationImpl
		 * @see com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl#getOperation()
		 * @generated
		 */
		EClass OPERATION = eINSTANCE.getOperation();

		/**
		 * The meta object literal for the '<em><b>Parameters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATION__PARAMETERS = eINSTANCE.getOperation_Parameters();

		/**
		 * The meta object literal for the '{@link com.crispico.flower.mp.model.astcache.code.DocumentableElement <em>Documentable Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.crispico.flower.mp.model.astcache.code.DocumentableElement
		 * @see com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl#getDocumentableElement()
		 * @generated
		 */
		EClass DOCUMENTABLE_ELEMENT = eINSTANCE.getDocumentableElement();

		/**
		 * The meta object literal for the '<em><b>Documentation</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOCUMENTABLE_ELEMENT__DOCUMENTATION = eINSTANCE.getDocumentableElement_Documentation();

		/**
		 * The meta object literal for the '{@link com.crispico.flower.mp.model.astcache.code.TypedElement <em>Typed Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.crispico.flower.mp.model.astcache.code.TypedElement
		 * @see com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl#getTypedElement()
		 * @generated
		 */
		EClass TYPED_ELEMENT = eINSTANCE.getTypedElement();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TYPED_ELEMENT__TYPE = eINSTANCE.getTypedElement_Type();

		/**
		 * The meta object literal for the '{@link com.crispico.flower.mp.model.astcache.code.ModifiableElement <em>Modifiable Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.crispico.flower.mp.model.astcache.code.ModifiableElement
		 * @see com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl#getModifiableElement()
		 * @generated
		 */
		EClass MODIFIABLE_ELEMENT = eINSTANCE.getModifiableElement();

		/**
		 * The meta object literal for the '<em><b>Modifiers</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODIFIABLE_ELEMENT__MODIFIERS = eINSTANCE.getModifiableElement_Modifiers();

		/**
		 * The meta object literal for the '{@link com.crispico.flower.mp.model.astcache.code.impl.ExtendedModifierImpl <em>Extended Modifier</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.crispico.flower.mp.model.astcache.code.impl.ExtendedModifierImpl
		 * @see com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl#getExtendedModifier()
		 * @generated
		 */
		EClass EXTENDED_MODIFIER = eINSTANCE.getExtendedModifier();

		/**
		 * The meta object literal for the '{@link com.crispico.flower.mp.model.astcache.code.impl.ModifierImpl <em>Modifier</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.crispico.flower.mp.model.astcache.code.impl.ModifierImpl
		 * @see com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl#getModifier()
		 * @generated
		 */
		EClass MODIFIER = eINSTANCE.getModifier();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODIFIER__TYPE = eINSTANCE.getModifier_Type();

		/**
		 * The meta object literal for the '{@link com.crispico.flower.mp.model.astcache.code.impl.AnnotationImpl <em>Annotation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.crispico.flower.mp.model.astcache.code.impl.AnnotationImpl
		 * @see com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl#getAnnotation()
		 * @generated
		 */
		EClass ANNOTATION = eINSTANCE.getAnnotation();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ANNOTATION__NAME = eINSTANCE.getAnnotation_Name();

		/**
		 * The meta object literal for the '<em><b>Values</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANNOTATION__VALUES = eINSTANCE.getAnnotation_Values();

		/**
		 * The meta object literal for the '{@link com.crispico.flower.mp.model.astcache.code.impl.AnnotationValueImpl <em>Annotation Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.crispico.flower.mp.model.astcache.code.impl.AnnotationValueImpl
		 * @see com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl#getAnnotationValue()
		 * @generated
		 */
		EClass ANNOTATION_VALUE = eINSTANCE.getAnnotationValue();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ANNOTATION_VALUE__NAME = eINSTANCE.getAnnotationValue_Name();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ANNOTATION_VALUE__VALUE = eINSTANCE.getAnnotationValue_Value();

		/**
		 * The meta object literal for the '{@link com.crispico.flower.mp.model.astcache.code.impl.ParameterImpl <em>Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.crispico.flower.mp.model.astcache.code.impl.ParameterImpl
		 * @see com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl#getParameter()
		 * @generated
		 */
		EClass PARAMETER = eINSTANCE.getParameter();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__NAME = eINSTANCE.getParameter_Name();

		/**
		 * The meta object literal for the '{@link com.crispico.flower.mp.model.astcache.code.impl.EnumConstantImpl <em>Enum Constant</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.crispico.flower.mp.model.astcache.code.impl.EnumConstantImpl
		 * @see com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl#getEnumConstant()
		 * @generated
		 */
		EClass ENUM_CONSTANT = eINSTANCE.getEnumConstant();

		/**
		 * The meta object literal for the '<em><b>Arguments</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENUM_CONSTANT__ARGUMENTS = eINSTANCE.getEnumConstant_Arguments();

		/**
		 * The meta object literal for the '{@link com.crispico.flower.mp.model.astcache.code.impl.AnnotationMemberImpl <em>Annotation Member</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.crispico.flower.mp.model.astcache.code.impl.AnnotationMemberImpl
		 * @see com.crispico.flower.mp.model.astcache.code.impl.AstCacheCodePackageImpl#getAnnotationMember()
		 * @generated
		 */
		EClass ANNOTATION_MEMBER = eINSTANCE.getAnnotationMember();

		/**
		 * The meta object literal for the '<em><b>Default Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ANNOTATION_MEMBER__DEFAULT_VALUE = eINSTANCE.getAnnotationMember_DefaultValue();

	}

} //AstCacheCodePackage