/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.codesync.code.javascript.regex_ast.impl;

import java.util.Map;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.flowerplatform.codesync.code.javascript.regex_ast.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class RegExAstFactoryImpl extends EFactoryImpl implements RegExAstFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static RegExAstFactory init() {
		try {
			RegExAstFactory theRegExAstFactory = (RegExAstFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.flower-platform.com/xmi/regex_ast_1.0.0"); 
			if (theRegExAstFactory != null) {
				return theRegExAstFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new RegExAstFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RegExAstFactoryImpl() {
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
			case RegExAstPackage.REG_EX_AST_NODE: return createRegExAstNode();
			case RegExAstPackage.STRING_TO_INTEGER_ENTRY: return (EObject)createStringToIntegerEntry();
			case RegExAstPackage.PARAMETER: return createParameter();
			case RegExAstPackage.REG_EX_AST_CODE_SYNC_ELEMENT: return createRegExAstCodeSyncElement();
			case RegExAstPackage.REG_EX_AST_CACHE_ELEMENT: return createRegExAstCacheElement();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RegExAstNode createRegExAstNode() {
		RegExAstNodeImpl regExAstNode = new RegExAstNodeImpl();
		return regExAstNode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<String, Integer> createStringToIntegerEntry() {
		StringToIntegerEntryImpl stringToIntegerEntry = new StringToIntegerEntryImpl();
		return stringToIntegerEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Parameter createParameter() {
		ParameterImpl parameter = new ParameterImpl();
		return parameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RegExAstCodeSyncElement createRegExAstCodeSyncElement() {
		RegExAstCodeSyncElementImpl regExAstCodeSyncElement = new RegExAstCodeSyncElementImpl();
		return regExAstCodeSyncElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RegExAstCacheElement createRegExAstCacheElement() {
		RegExAstCacheElementImpl regExAstCacheElement = new RegExAstCacheElementImpl();
		return regExAstCacheElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RegExAstPackage getRegExAstPackage() {
		return (RegExAstPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static RegExAstPackage getPackage() {
		return RegExAstPackage.eINSTANCE;
	}

} //RegExAstFactoryImpl
