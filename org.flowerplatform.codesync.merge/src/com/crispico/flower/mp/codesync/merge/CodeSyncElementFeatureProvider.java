package com.crispico.flower.mp.codesync.merge;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * @author Mariana
 */
public class CodeSyncElementFeatureProvider {

	public String getFeatureName(Object feature) {
		return ((EStructuralFeature) feature).getName();
	}
	
	public List<?> getFeatures(Object element) {
		return Arrays.asList(
				// values will be obtained directly from CodeSyncElement
				CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name(),
				CodeSyncPackage.eINSTANCE.getCodeSyncElement_Type(),
				CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children(),		// containment feature
				
				// values will be obtained from the AstCacheElement of the CSE
						
				// TypedElement features
				AstCacheCodePackage.eINSTANCE.getTypedElement_Type(),
				
				// DocumentableElement features
				AstCacheCodePackage.eINSTANCE.getDocumentableElement_Documentation(),
					
				// ModifiableElement features
				AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers(),	// containment feature
				
				// Class features
				AstCacheCodePackage.eINSTANCE.getClass_SuperClasses(),
				AstCacheCodePackage.eINSTANCE.getClass_SuperInterfaces(),
				
				// Attribute features
				AstCacheCodePackage.eINSTANCE.getAttribute_Initializer(),
				
				// Operation features
				AstCacheCodePackage.eINSTANCE.getOperation_Parameters(),		// containment feature

				// Parameter features
				AstCacheCodePackage.eINSTANCE.getParameter_Name(),
					
				// Modifier features
				AstCacheCodePackage.eINSTANCE.getModifier_Type(),
					
				// Annotation features
				AstCacheCodePackage.eINSTANCE.getAnnotation_Name(),
				AstCacheCodePackage.eINSTANCE.getAnnotation_Values(),			// containment feature
				AstCacheCodePackage.eINSTANCE.getAnnotationValue_Name(),
				AstCacheCodePackage.eINSTANCE.getAnnotationValue_Value(),
				
				// Enum Constant features
				AstCacheCodePackage.eINSTANCE.getEnumConstant_Arguments(),		// containment feature
				
				// Annotation Member features
				AstCacheCodePackage.eINSTANCE.getAnnotationMember_DefaultValue()
				);
	}
	
	public int getFeatureType(Object feature) {
		EStructuralFeature structuralFeature = (EStructuralFeature) feature;
		if (structuralFeature instanceof EReference && ((EReference) structuralFeature).isContainment())
			return IModelAdapter.FEATURE_TYPE_CONTAINMENT;
		else 
			return IModelAdapter.FEATURE_TYPE_VALUE;
	}
}
