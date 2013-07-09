package com.crispico.flower.mp.codesync.code.java;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.MemberValuePair;

import com.crispico.flower.mp.model.astcache.code.AnnotationValue;
import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * Mapped to {@link MemberValuePair}. Does not return any children. Does not have a corresponding {@link CodeSyncElement}.
 * 
 * @author Mariana
 */
public class JavaMemberValuePairModelAdapter extends JavaAbstractAstNodeModelAdapter {

	@Override
	public List<?> getChildren(Object modelElement) {
		return Collections.emptyList();
	}

	@Override
	public Object getMatchKey(Object modelElement) {
		return ((MemberValuePair) modelElement).getName().getIdentifier();
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (AstCacheCodePackage.eINSTANCE.getAnnotationValue_Name().equals(feature)) {
			return ((MemberValuePair) element).getName().getIdentifier();
		}
		if (AstCacheCodePackage.eINSTANCE.getAnnotationValue_Value().equals(feature)) {
			return getStringFromExpression(((MemberValuePair) element).getValue());
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
	
	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		if (AstCacheCodePackage.eINSTANCE.getAnnotationValue_Name().equals(feature)) {
			MemberValuePair pair = (MemberValuePair) element;
			String name = (String) value;
			pair.setName(pair.getAST().newSimpleName(name));
		}
		if (AstCacheCodePackage.eINSTANCE.getAnnotationValue_Value().equals(feature)) {
			MemberValuePair pair = (MemberValuePair) element;
			String expression = (String) value;
			pair.setValue(getExpressionFromString(pair.getAST(), expression));
		}
		super.setValueFeatureValue(element, feature, value);
	}

	/**
	 * Creates a {@link AnnotationValue} instance. Also set the name, in case the AST cache was deleted.
	 */
	@Override
	public Object createCorrespondingModelElement(Object element) {
		AnnotationValue value = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAnnotationValue();
		value.setName(((MemberValuePair) element).getName().getIdentifier());
		return value;
	}

}
