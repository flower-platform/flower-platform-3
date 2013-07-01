/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.crispico.flower.mp.model.astcache.code.impl;

import com.crispico.flower.mp.model.astcache.code.Annotation;
import com.crispico.flower.mp.model.astcache.code.AnnotationMember;
import com.crispico.flower.mp.model.astcache.code.AnnotationValue;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.crispico.flower.mp.model.astcache.code.AstCacheCodeFactory;
import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;
import com.crispico.flower.mp.model.astcache.code.Attribute;
import com.crispico.flower.mp.model.astcache.code.EnumConstant;
import com.crispico.flower.mp.model.astcache.code.ModifiableElement;
import com.crispico.flower.mp.model.astcache.code.Modifier;
import com.crispico.flower.mp.model.astcache.code.Operation;
import com.crispico.flower.mp.model.astcache.code.Parameter;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class AstCacheCodeFactoryImpl extends EFactoryImpl implements AstCacheCodeFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static AstCacheCodeFactory init() {
		try {
			AstCacheCodeFactory theAstCacheCodeFactory = (AstCacheCodeFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.flower-platform.com/xmi/astcache_code_1.0.0"); 
			if (theAstCacheCodeFactory != null) {
				return theAstCacheCodeFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new AstCacheCodeFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AstCacheCodeFactoryImpl() {
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
			case AstCacheCodePackage.MODIFIER: return createModifier();
			case AstCacheCodePackage.ANNOTATION: return createAnnotation();
			case AstCacheCodePackage.ANNOTATION_VALUE: return createAnnotationValue();
			case AstCacheCodePackage.CLASS: return createClass();
			case AstCacheCodePackage.ATTRIBUTE: return createAttribute();
			case AstCacheCodePackage.OPERATION: return createOperation();
			case AstCacheCodePackage.PARAMETER: return createParameter();
			case AstCacheCodePackage.ENUM_CONSTANT: return createEnumConstant();
			case AstCacheCodePackage.ANNOTATION_MEMBER: return createAnnotationMember();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Modifier createModifier() {
		ModifierImpl modifier = new ModifierImpl();
		return modifier;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Annotation createAnnotation() {
		AnnotationImpl annotation = new AnnotationImpl();
		return annotation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnnotationValue createAnnotationValue() {
		AnnotationValueImpl annotationValue = new AnnotationValueImpl();
		return annotationValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public com.crispico.flower.mp.model.astcache.code.Class createClass() {
		ClassImpl class_ = new ClassImpl();
		return class_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Attribute createAttribute() {
		AttributeImpl attribute = new AttributeImpl();
		return attribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Operation createOperation() {
		OperationImpl operation = new OperationImpl();
		return operation;
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
	public EnumConstant createEnumConstant() {
		EnumConstantImpl enumConstant = new EnumConstantImpl();
		return enumConstant;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnnotationMember createAnnotationMember() {
		AnnotationMemberImpl annotationMember = new AnnotationMemberImpl();
		return annotationMember;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AstCacheCodePackage getAstCacheCodePackage() {
		return (AstCacheCodePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static AstCacheCodePackage getPackage() {
		return AstCacheCodePackage.eINSTANCE;
	}

} //AstCacheCodeFactoryImpl
