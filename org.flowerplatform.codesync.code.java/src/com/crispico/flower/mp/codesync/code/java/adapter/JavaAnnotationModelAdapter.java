package com.crispico.flower.mp.codesync.code.java.adapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

import com.crispico.flower.mp.codesync.code.CodeSyncCodePlugin;
import com.crispico.flower.mp.model.astcache.code.AnnotationValue;
import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * Mapped to {@link Annotation}. Does not have a corresponding {@link CodeSyncElement}.
 * 
 * @author Mariana
 */
public class JavaAnnotationModelAdapter extends JavaAbstractAstNodeModelAdapter {

	@Override
	public List<?> getFeatures(Object element) {
		return Arrays.asList(
			AstCacheCodePackage.eINSTANCE.getAnnotation_Name(),
			AstCacheCodePackage.eINSTANCE.getAnnotation_Values()
		);
	}
	
	@Override
	public Object getMatchKey(Object modelElement) {
		Annotation annotation = (Annotation) modelElement;
		String matchKey = (String) getAnnotationName(modelElement);
		if (annotation instanceof MarkerAnnotation) {
			matchKey += CodeSyncCodePlugin.MARKER_ANNOTATION;
		} else {
			if (annotation instanceof SingleMemberAnnotation) {
				matchKey += CodeSyncCodePlugin.SINGLE_MEMBER_ANNOTATION;
			} else {
				matchKey += CodeSyncCodePlugin.NORMAL_ANNOTATION;
			}
		}
		return matchKey;
	}

	@Override
	public Object createCorrespondingModelElement(Object element) {
		com.crispico.flower.mp.model.astcache.code.Annotation annotation = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAnnotation();
		annotation.setName((String) getAnnotationName(element));
		// don't add any value for MarkerAnnotations
		return annotation;
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		return Collections.emptyList();
	}
	
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (AstCacheCodePackage.eINSTANCE.getAnnotation_Name().equals(feature)) {
			return getAnnotationName(element);
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
	
	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		if (AstCacheCodePackage.eINSTANCE.getAnnotation_Name().equals(feature)) {
			if (element instanceof Annotation) {
				Annotation annotation = (Annotation) element;
				String name = (String) value;
				annotation.setTypeName(annotation.getAST().newName(name));
			}
			return;
		}
		super.setValueFeatureValue(element, feature, value);
	}
	
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (AstCacheCodePackage.eINSTANCE.getAnnotation_Values().equals(feature)) {
			if (element instanceof NormalAnnotation) {
				return ((NormalAnnotation) element).values();
			}
			if (element instanceof SingleMemberAnnotation) {
				AST ast = AST.newAST(AST.JLS4);
				MemberValuePair pair = ast.newMemberValuePair();
				Expression value = ((SingleMemberAnnotation) element).getValue();
				ASTNode newValue = ASTNode.copySubtree(ast, value);
				pair.setName(ast.newSimpleName(CodeSyncCodePlugin.SINGLE_MEMBER_ANNOTATION_VALUE_NAME));
				pair.setValue((Expression) newValue);
				return Collections.singletonList(pair);
			}
			return Collections.emptyList();
		}
		
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}
	
	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild) {
		if (AstCacheCodePackage.eINSTANCE.getAnnotation_Values().equals(feature)) {
			ASTNode child = null;
			ASTNode parent = (ASTNode) element;
			AST ast = parent.getAST();
			
			// for an existing NormalAnnotation, just add the new value
			if (parent instanceof NormalAnnotation) {
				MemberValuePair pair = ast.newMemberValuePair();
				((NormalAnnotation) parent).values().add(pair);
				child = pair;
			} else {
				AnnotationValue value = (AnnotationValue) correspondingChild;
				// if the existing annotation is a SingleMemberAnnotation, then set its value
				if (parent instanceof SingleMemberAnnotation) {
					ASTNode expression = getExpressionFromString(parent.getAST(), value.getValue());
					((SingleMemberAnnotation) parent).setValue((Expression) expression);
					child = ast.newMemberValuePair(); // avoid NPE later
				}
			}
			
			return child;
		}
		
		return super.createChildOnContainmentFeature(element, feature, correspondingChild);
	}

	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		if (AstCacheCodePackage.eINSTANCE.getAnnotation_Values().equals(feature) && !(parent instanceof NormalAnnotation)) {
			return;
		}
		super.removeChildrenOnContainmentFeature(parent, feature, child);
	}

	private Object getAnnotationName(Object element) {
		return ((Annotation) element).getTypeName().getFullyQualifiedName();
	}
	
}
