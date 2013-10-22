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
package org.flowerplatform.codesync.code.java;

import static com.crispico.flower.mp.codesync.code.java.adapter.JavaAnnotationTypeMemberDeclarationModelAdapter.ANNOTATION_MEMBER;
import static com.crispico.flower.mp.codesync.code.java.adapter.JavaAttributeModelAdapter.ATTRIBUTE;
import static com.crispico.flower.mp.codesync.code.java.adapter.JavaEnumConstantDeclarationModelAdapter.ENUM_CONSTANT;
import static com.crispico.flower.mp.codesync.code.java.adapter.JavaOperationModelAdapter.OPERATION;
import static com.crispico.flower.mp.codesync.code.java.adapter.JavaTypeModelAdapter.ANNOTATION;
import static com.crispico.flower.mp.codesync.code.java.adapter.JavaTypeModelAdapter.CLASS;
import static com.crispico.flower.mp.codesync.code.java.adapter.JavaTypeModelAdapter.ENUM;
import static com.crispico.flower.mp.codesync.code.java.adapter.JavaTypeModelAdapter.INTERFACE;

import java.io.File;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import com.crispico.flower.mp.codesync.base.CodeSyncElementFeatureProvider;
import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactory;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactorySet;
import com.crispico.flower.mp.codesync.code.CodeSyncModelAdapterFactory;
import com.crispico.flower.mp.codesync.code.adapter.AnnotationMemberModelAdapter;
import com.crispico.flower.mp.codesync.code.adapter.AnnotationModelAdapter;
import com.crispico.flower.mp.codesync.code.adapter.AttributeModelAdapter;
import com.crispico.flower.mp.codesync.code.adapter.ClassModelAdapter;
import com.crispico.flower.mp.codesync.code.adapter.CodeSyncElementModelAdapter;
import com.crispico.flower.mp.codesync.code.adapter.CodeSyncElementModelAdapterAncestor;
import com.crispico.flower.mp.codesync.code.adapter.CodeSyncElementModelAdapterLeft;
import com.crispico.flower.mp.codesync.code.adapter.EnumConstantModelAdapter;
import com.crispico.flower.mp.codesync.code.adapter.FolderModelAdapter;
import com.crispico.flower.mp.codesync.code.adapter.OperationModelAdapter;
import com.crispico.flower.mp.codesync.code.adapter.ParameterModelAdapter;
import com.crispico.flower.mp.codesync.code.adapter.StringModelAdapter;
import com.crispico.flower.mp.codesync.code.featureprovider.StringFeatureProvider;
import com.crispico.flower.mp.codesync.code.java.CodeSyncCodeJavaPlugin;
import com.crispico.flower.mp.codesync.code.java.adapter.AnnotationValueModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaAnnotationModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaAnnotationTypeMemberDeclarationModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaAttributeModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaEnumConstantDeclarationModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaFileModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaMemberValuePairModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaModifierModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaOperationModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaParameterModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaTypeModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.ModifierModelAdapter;
import com.crispico.flower.mp.codesync.code.java.featureprovider.JavaAnnotationFeatureProvider;
import com.crispico.flower.mp.codesync.code.java.featureprovider.JavaAnnotationTypeMemberDeclarationFeatureProvider;
import com.crispico.flower.mp.codesync.code.java.featureprovider.JavaAttributeFeatureProvider;
import com.crispico.flower.mp.codesync.code.java.featureprovider.JavaEnumConstantDeclarationFeatureProvider;
import com.crispico.flower.mp.codesync.code.java.featureprovider.JavaMemberValuePairFeatureProvider;
import com.crispico.flower.mp.codesync.code.java.featureprovider.JavaModifierFeatureProvider;
import com.crispico.flower.mp.codesync.code.java.featureprovider.JavaOperationFeatureProvider;
import com.crispico.flower.mp.codesync.code.java.featureprovider.JavaParameterFeatureProvider;
import com.crispico.flower.mp.codesync.code.java.featureprovider.JavaTypeFeatureProvider;
import com.crispico.flower.mp.model.astcache.code.AnnotationValue;
import com.crispico.flower.mp.model.astcache.code.Parameter;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana
 */
public class JavaModelAdapterFactorySet extends ModelAdapterFactorySet {

	@Override
	public void initialize(Resource astCache, String limitedPath, boolean useUIDs) {
		super.initialize(astCache, limitedPath, useUIDs);
		
		// right - AST
		rightFactory = new ModelAdapterFactory();
		
		// folder adapter
		FolderModelAdapter folderModelAdapter = (FolderModelAdapter) createAstModelAdapter(new FolderModelAdapter());
		folderModelAdapter.setLimitedPath(limitedPath);
		rightFactory.addModelAdapter(File.class, folderModelAdapter, "");
		
		// java specific adapters
		JavaFileModelAdapter fileModelAdapter = (JavaFileModelAdapter) createAstModelAdapter(new JavaFileModelAdapter());
		rightFactory.addModelAdapter(File.class, fileModelAdapter, CodeSyncCodeJavaPlugin.TECHNOLOGY);
		rightFactory.addModelAdapter(AbstractTypeDeclaration.class, createAstModelAdapter(new JavaTypeModelAdapter()));
		rightFactory.addModelAdapter(FieldDeclaration.class, createAstModelAdapter(new JavaAttributeModelAdapter()));
		rightFactory.addModelAdapter(MethodDeclaration.class, createAstModelAdapter(new JavaOperationModelAdapter()));
		rightFactory.addModelAdapter(SingleVariableDeclaration.class, createAstModelAdapter(new JavaParameterModelAdapter()));
		rightFactory.addModelAdapter(Annotation.class, createAstModelAdapter(new JavaAnnotationModelAdapter()));
		rightFactory.addModelAdapter(Modifier.class, createAstModelAdapter(new JavaModifierModelAdapter()));
		rightFactory.addModelAdapter(MemberValuePair.class, createAstModelAdapter(new JavaMemberValuePairModelAdapter()));
		rightFactory.addModelAdapter(EnumConstantDeclaration.class, createAstModelAdapter(new JavaEnumConstantDeclarationModelAdapter()));
		rightFactory.addModelAdapter(AnnotationTypeMemberDeclaration.class, createAstModelAdapter(new JavaAnnotationTypeMemberDeclarationModelAdapter()));
		rightFactory.addModelAdapter(String.class, createAstModelAdapter(new StringModelAdapter()));
		
		// ancestor - CodeSyncElements
		this.ancestorFactory = createCodeSyncModelAdapterFactory(null, false);
		
		// left - CodeSyncElements
		leftFactory = createCodeSyncModelAdapterFactory(astCache, true);
		
		// feature providers
		CodeSyncElementFeatureProvider featureProvider = new CodeSyncElementFeatureProvider();
		addFeatureProvider(File.class, featureProvider);
		addFeatureProvider(CodeSyncPlugin.FOLDER, featureProvider);
		addFeatureProvider(File.class, featureProvider);
		addFeatureProvider(CodeSyncPlugin.FILE, featureProvider);
		
		JavaTypeFeatureProvider typeFeatureProvider = new JavaTypeFeatureProvider();
		addFeatureProvider(AbstractTypeDeclaration.class, typeFeatureProvider);
		addFeatureProvider(CLASS, typeFeatureProvider);
		addFeatureProvider(INTERFACE, typeFeatureProvider);
		addFeatureProvider(ENUM, typeFeatureProvider);
		addFeatureProvider(ANNOTATION, typeFeatureProvider);
		
		JavaAttributeFeatureProvider attributeFeatureProvider = new JavaAttributeFeatureProvider();
		addFeatureProvider(FieldDeclaration.class, attributeFeatureProvider);
		addFeatureProvider(ATTRIBUTE, attributeFeatureProvider);
		
		JavaOperationFeatureProvider operationFeatureProvider = new JavaOperationFeatureProvider();
		addFeatureProvider(MethodDeclaration.class, operationFeatureProvider);
		addFeatureProvider(OPERATION, operationFeatureProvider);
		
		JavaEnumConstantDeclarationFeatureProvider enumCtFeatureProvider = new JavaEnumConstantDeclarationFeatureProvider();
		addFeatureProvider(EnumConstantDeclaration.class, enumCtFeatureProvider);
		addFeatureProvider(ENUM_CONSTANT, enumCtFeatureProvider);
		
		JavaAnnotationTypeMemberDeclarationFeatureProvider annotationMemberFeatureProvider = new JavaAnnotationTypeMemberDeclarationFeatureProvider();
		addFeatureProvider(AnnotationTypeMemberDeclaration.class, annotationMemberFeatureProvider);
		addFeatureProvider(ANNOTATION_MEMBER, annotationMemberFeatureProvider);
		
		JavaAnnotationFeatureProvider annotationFeatureProvider = new JavaAnnotationFeatureProvider();
		addFeatureProvider(Annotation.class, annotationFeatureProvider);
		addFeatureProvider(com.crispico.flower.mp.model.astcache.code.Annotation.class, annotationFeatureProvider);
		
		JavaMemberValuePairFeatureProvider annotationValueFeatureProvider = new JavaMemberValuePairFeatureProvider();
		addFeatureProvider(MemberValuePair.class, annotationValueFeatureProvider);
		addFeatureProvider(AnnotationValue.class, annotationValueFeatureProvider);
		
		JavaModifierFeatureProvider modifierFeatureProvider = new JavaModifierFeatureProvider();
		addFeatureProvider(Modifier.class, modifierFeatureProvider);
		addFeatureProvider(com.crispico.flower.mp.model.astcache.code.Modifier.class, modifierFeatureProvider);
		
		JavaParameterFeatureProvider parameterFeatureProvider = new JavaParameterFeatureProvider();
		addFeatureProvider(SingleVariableDeclaration.class, parameterFeatureProvider);
		addFeatureProvider(Parameter.class, parameterFeatureProvider);
		
		addFeatureProvider(String.class, new StringFeatureProvider());
	}
	
	private CodeSyncModelAdapterFactory createCodeSyncModelAdapterFactory(Resource resource, boolean isLeft) {
		CodeSyncModelAdapterFactory factory = new CodeSyncModelAdapterFactory(this, rightFactory, resource, isLeft);
		ClassModelAdapter typeModelAdapter = new ClassModelAdapter();
		factory.addModelAdapter(CLASS, typeModelAdapter);
		factory.addModelAdapter(INTERFACE, typeModelAdapter);
		factory.addModelAdapter(ENUM, typeModelAdapter);
		factory.addModelAdapter(ANNOTATION, typeModelAdapter);
		
		factory.addModelAdapter(ATTRIBUTE, new AttributeModelAdapter());
		factory.addModelAdapter(OPERATION, new OperationModelAdapter());
		factory.addModelAdapter(ENUM_CONSTANT, new EnumConstantModelAdapter());
		factory.addModelAdapter(ANNOTATION_MEMBER, new AnnotationMemberModelAdapter());
		
		factory.addModelAdapter(com.crispico.flower.mp.model.astcache.code.Annotation.class, new AnnotationModelAdapter());
		factory.addModelAdapter(AnnotationValue.class, new AnnotationValueModelAdapter());
		factory.addModelAdapter(com.crispico.flower.mp.model.astcache.code.Modifier.class, new ModifierModelAdapter());
		factory.addModelAdapter(Parameter.class, new ParameterModelAdapter());
		factory.addModelAdapter(String.class, new StringModelAdapter());
		
		CodeSyncElementModelAdapter cseAdapter = isLeft 
				? new CodeSyncElementModelAdapterLeft()
				: new CodeSyncElementModelAdapterAncestor();
		cseAdapter.setModelAdapterFactory(factory);
		cseAdapter.setEObjectConverter(rightFactory);
		
		factory.addModelAdapter(CodeSyncElement.class, cseAdapter);
		
		return factory;
	}
	
	private IModelAdapter createAstModelAdapter(IModelAdapter adapter) {
		return adapter.setModelAdapterFactorySet(this);
	}
	
}