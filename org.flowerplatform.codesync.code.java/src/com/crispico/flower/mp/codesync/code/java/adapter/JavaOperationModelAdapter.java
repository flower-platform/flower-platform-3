package com.crispico.flower.mp.codesync.code.java.adapter;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;

import com.crispico.flower.mp.codesync.code.CodeSyncElementTypeConstants;
import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * Mapped to {@link MethodDeclaration}. Does not return any children.
 * 
 * @author Mariana
 */
public class JavaOperationModelAdapter extends JavaAbstractAstNodeModelAdapter {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<?> getFeatures(Object element) {
		List features = super.getFeatures(element);
		features.add(AstCacheCodePackage.eINSTANCE.getDocumentableElement_Documentation());
		features.add(AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers());
		features.add(AstCacheCodePackage.eINSTANCE.getTypedElement_Type());
		features.add(AstCacheCodePackage.eINSTANCE.getOperation_Parameters());
		return features;
	}
	
	@Override
	public List<?> getChildren(Object modelElement) {
		return Collections.emptyList();
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (AstCacheCodePackage.eINSTANCE.getOperation_Parameters().equals(feature)) {
			return ((MethodDeclaration) element).parameters();
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name().equals(feature)) {
			return getLabel(element);
		}
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Type().equals(feature)) {
			return CodeSyncElementTypeConstants.OPERATION;
		}
		if (AstCacheCodePackage.eINSTANCE.getTypedElement_Type().equals(feature)) {
			return getStringFromType(getMethodDeclaration(element).getReturnType2());
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name().equals(feature)) {
			MethodDeclaration method = getMethodDeclaration(element);
			String name = (String) value;
			int index = name.indexOf("(");
			if (index == -1) {
				index = name.length();
			}
			name = name.substring(0, index);
			method.setName(method.getAST().newSimpleName(name));
		}
		if (AstCacheCodePackage.eINSTANCE.getTypedElement_Type().equals(feature)) {
			MethodDeclaration method = getMethodDeclaration(element);
			Type type = getTypeFromString(method.getAST(), (String) value);
			method.setReturnType2(type);
		}
		super.setValueFeatureValue(element, feature, value);
	}

	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild) {
		if (AstCacheCodePackage.eINSTANCE.getOperation_Parameters().equals(feature)) {
			MethodDeclaration method = (MethodDeclaration) element;
			AST ast = method.getAST();
			SingleVariableDeclaration parameter = ast.newSingleVariableDeclaration();
			method.parameters().add(parameter);
			return parameter;
		}
		return super.createChildOnContainmentFeature(element, feature, correspondingChild);
	}
	
	private MethodDeclaration getMethodDeclaration(Object element) {
		return (MethodDeclaration) element;
	}
	
	@Override
	public Object getMatchKey(Object modelElement) {
		MethodDeclaration method = getMethodDeclaration(modelElement);
		String label = method.getName().getIdentifier();
		label += "(";
		for (Object param : method.parameters()) {
			label += ((SingleVariableDeclaration) param).getType().toString() + ",";
		}
		if (label.endsWith(",")) {
			label = label.substring(0, label.length() - 1);
		}
		label += ")";
		return label;
	}

	@Override
	public Object createCorrespondingModelElement(Object element) {
		return AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createOperation();
	}
	
}
