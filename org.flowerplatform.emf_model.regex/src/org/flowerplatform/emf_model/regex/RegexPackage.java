/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.emf_model.regex;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

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
 * @see org.flowerplatform.emf_model.regex.RegexFactory
 * @model kind="package"
 * @generated
 */
public interface RegexPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "regex";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.flower-platform.com/xmi/regex_1.0.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "regex";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	RegexPackage eINSTANCE = org.flowerplatform.emf_model.regex.impl.RegexPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.flowerplatform.emf_model.regex.impl.MacroRegexImpl <em>Macro Regex</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.emf_model.regex.impl.MacroRegexImpl
	 * @see org.flowerplatform.emf_model.regex.impl.RegexPackageImpl#getMacroRegex()
	 * @generated
	 */
	int MACRO_REGEX = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MACRO_REGEX__NAME = 0;

	/**
	 * The feature id for the '<em><b>Regex</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MACRO_REGEX__REGEX = 1;

	/**
	 * The number of structural features of the '<em>Macro Regex</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MACRO_REGEX_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.flowerplatform.emf_model.regex.impl.ParserRegexImpl <em>Parser Regex</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.emf_model.regex.impl.ParserRegexImpl
	 * @see org.flowerplatform.emf_model.regex.impl.RegexPackageImpl#getParserRegex()
	 * @generated
	 */
	int PARSER_REGEX = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARSER_REGEX__NAME = MACRO_REGEX__NAME;

	/**
	 * The feature id for the '<em><b>Regex</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARSER_REGEX__REGEX = MACRO_REGEX__REGEX;

	/**
	 * The feature id for the '<em><b>Action</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARSER_REGEX__ACTION = MACRO_REGEX_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Parser Regex</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARSER_REGEX_FEATURE_COUNT = MACRO_REGEX_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.flowerplatform.emf_model.regex.impl.RootImpl <em>Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.emf_model.regex.impl.RootImpl
	 * @see org.flowerplatform.emf_model.regex.impl.RegexPackageImpl#getRoot()
	 * @generated
	 */
	int ROOT = 2;

	/**
	 * The feature id for the '<em><b>Parser Regexes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROOT__PARSER_REGEXES = 0;

	/**
	 * The feature id for the '<em><b>Macro Regexes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROOT__MACRO_REGEXES = 1;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROOT__EXTENSIONS = 2;

	/**
	 * The number of structural features of the '<em>Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROOT_FEATURE_COUNT = 3;


	/**
	 * Returns the meta object for class '{@link org.flowerplatform.emf_model.regex.ParserRegex <em>Parser Regex</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parser Regex</em>'.
	 * @see org.flowerplatform.emf_model.regex.ParserRegex
	 * @generated
	 */
	EClass getParserRegex();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.emf_model.regex.ParserRegex#getAction <em>Action</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Action</em>'.
	 * @see org.flowerplatform.emf_model.regex.ParserRegex#getAction()
	 * @see #getParserRegex()
	 * @generated
	 */
	EAttribute getParserRegex_Action();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.emf_model.regex.MacroRegex <em>Macro Regex</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Macro Regex</em>'.
	 * @see org.flowerplatform.emf_model.regex.MacroRegex
	 * @generated
	 */
	EClass getMacroRegex();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.emf_model.regex.MacroRegex#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.flowerplatform.emf_model.regex.MacroRegex#getName()
	 * @see #getMacroRegex()
	 * @generated
	 */
	EAttribute getMacroRegex_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.emf_model.regex.MacroRegex#getRegex <em>Regex</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Regex</em>'.
	 * @see org.flowerplatform.emf_model.regex.MacroRegex#getRegex()
	 * @see #getMacroRegex()
	 * @generated
	 */
	EAttribute getMacroRegex_Regex();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.emf_model.regex.Root <em>Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Root</em>'.
	 * @see org.flowerplatform.emf_model.regex.Root
	 * @generated
	 */
	EClass getRoot();

	/**
	 * Returns the meta object for the containment reference list '{@link org.flowerplatform.emf_model.regex.Root#getParserRegexes <em>Parser Regexes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parser Regexes</em>'.
	 * @see org.flowerplatform.emf_model.regex.Root#getParserRegexes()
	 * @see #getRoot()
	 * @generated
	 */
	EReference getRoot_ParserRegexes();

	/**
	 * Returns the meta object for the containment reference list '{@link org.flowerplatform.emf_model.regex.Root#getMacroRegexes <em>Macro Regexes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Macro Regexes</em>'.
	 * @see org.flowerplatform.emf_model.regex.Root#getMacroRegexes()
	 * @see #getRoot()
	 * @generated
	 */
	EReference getRoot_MacroRegexes();

	/**
	 * Returns the meta object for the attribute list '{@link org.flowerplatform.emf_model.regex.Root#getExtensions <em>Extensions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Extensions</em>'.
	 * @see org.flowerplatform.emf_model.regex.Root#getExtensions()
	 * @see #getRoot()
	 * @generated
	 */
	EAttribute getRoot_Extensions();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	RegexFactory getRegexFactory();

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
		 * The meta object literal for the '{@link org.flowerplatform.emf_model.regex.impl.ParserRegexImpl <em>Parser Regex</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.emf_model.regex.impl.ParserRegexImpl
		 * @see org.flowerplatform.emf_model.regex.impl.RegexPackageImpl#getParserRegex()
		 * @generated
		 */
		EClass PARSER_REGEX = eINSTANCE.getParserRegex();

		/**
		 * The meta object literal for the '<em><b>Action</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARSER_REGEX__ACTION = eINSTANCE.getParserRegex_Action();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.emf_model.regex.impl.MacroRegexImpl <em>Macro Regex</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.emf_model.regex.impl.MacroRegexImpl
		 * @see org.flowerplatform.emf_model.regex.impl.RegexPackageImpl#getMacroRegex()
		 * @generated
		 */
		EClass MACRO_REGEX = eINSTANCE.getMacroRegex();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MACRO_REGEX__NAME = eINSTANCE.getMacroRegex_Name();

		/**
		 * The meta object literal for the '<em><b>Regex</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MACRO_REGEX__REGEX = eINSTANCE.getMacroRegex_Regex();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.emf_model.regex.impl.RootImpl <em>Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.emf_model.regex.impl.RootImpl
		 * @see org.flowerplatform.emf_model.regex.impl.RegexPackageImpl#getRoot()
		 * @generated
		 */
		EClass ROOT = eINSTANCE.getRoot();

		/**
		 * The meta object literal for the '<em><b>Parser Regexes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROOT__PARSER_REGEXES = eINSTANCE.getRoot_ParserRegexes();

		/**
		 * The meta object literal for the '<em><b>Macro Regexes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROOT__MACRO_REGEXES = eINSTANCE.getRoot_MacroRegexes();

		/**
		 * The meta object literal for the '<em><b>Extensions</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROOT__EXTENSIONS = eINSTANCE.getRoot_Extensions();

	}

} //RegexPackage
