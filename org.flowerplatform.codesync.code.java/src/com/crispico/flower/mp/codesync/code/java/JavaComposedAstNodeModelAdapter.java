package com.crispico.flower.mp.codesync.code.java;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import com.crispico.flower.mp.codesync.base.CodeSyncAlgorithm;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactory;

/**
 * Mapped to {@link ASTNode}. Uses an internal {@link ModelAdapterFactory} to delegate to adapters that handle 
 * different types of nodes (types, methods, fields), as the {@link CodeSyncAlgorithm} assumes that all 
 * the children of a model element are handled by the same adapter.
 * 
 * @author Mariana
 */
public class JavaComposedAstNodeModelAdapter extends JavaAbstractAstNodeModelAdapter {

	private ModelAdapterFactory astNodeModelAdapterFactory;
	
	public JavaComposedAstNodeModelAdapter() {
		super();
		astNodeModelAdapterFactory = new ModelAdapterFactory();
		astNodeModelAdapterFactory.addModelAdapter(AbstractTypeDeclaration.class, new JavaTypeModelAdapter());
		astNodeModelAdapterFactory.addModelAdapter(FieldDeclaration.class, new JavaAttributeModelAdapter());
		astNodeModelAdapterFactory.addModelAdapter(MethodDeclaration.class, new JavaOperationModelAdapter());
		astNodeModelAdapterFactory.addModelAdapter(SingleVariableDeclaration.class, new JavaParameterModelAdapter());
		astNodeModelAdapterFactory.addModelAdapter(IExtendedModifier.class, new JavaExtendedModifierModelAdapter());
		astNodeModelAdapterFactory.addModelAdapter(MemberValuePair.class, new JavaMemberValuePairModelAdapter());
		astNodeModelAdapterFactory.addModelAdapter(EnumConstantDeclaration.class, new JavaEnumConstantDeclarationModelAdapter());
		astNodeModelAdapterFactory.addModelAdapter(AnnotationTypeMemberDeclaration.class, new JavaAnnotationTypeMemberDeclarationModelAdapter());
	}
	
	protected JavaAbstractAstNodeModelAdapter getAstNodeModelAdapter(Object element) {
		return (JavaAbstractAstNodeModelAdapter) astNodeModelAdapterFactory.getModelAdapter(element);
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		return getAstNodeModelAdapter(modelElement).getChildren(modelElement);
	}

	@Override
	public Object getMatchKey(Object modelElement) {
		return getAstNodeModelAdapter(modelElement).getMatchKey(modelElement);
	}
	
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		return getAstNodeModelAdapter(element).getContainmentFeatureIterable(element, feature, correspondingIterable);
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		return getAstNodeModelAdapter(element).getValueFeatureValue(element, feature, correspondingValue);
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		getAstNodeModelAdapter(element).setValueFeatureValue(element, feature, value);
	}

	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild) {
		return getAstNodeModelAdapter(element).createChildOnContainmentFeature(element, feature, correspondingChild);
	}

	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		getAstNodeModelAdapter(parent).removeChildrenOnContainmentFeature(parent, feature, child);
	}
	
	@Override
	public Object createCorrespondingModelElement(Object element) {
		return getAstNodeModelAdapter(element).createCorrespondingModelElement(element);
	}
}
