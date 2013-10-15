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
package com.crispico.flower.mp.codesync.code.java;

import org.eclipse.emf.ecore.EObject;
import org.flowerplatform.codesync.processor.CodeSyncElementRelationsChangesProcessor;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.editor.model.change_processor.AbstractFeatureChangesProcessor;
import org.flowerplatform.editor.model.change_processor.DependentFeature;
import org.flowerplatform.editor.model.java.JavaClassAttributeProcessor;
import org.flowerplatform.editor.model.java.JavaClassOperationProcessor;
import org.flowerplatform.editor.model.java.JavaClassProcessor;
import org.flowerplatform.editor.model.java.JavaClassTitleProcessor;
import org.flowerplatform.editor.model.java.JavaScenarioElementProcessor;
import org.flowerplatform.editor.model.java.remote.JavaClassDiagramOperationsService;
import org.flowerplatform.emf_model.notation.NotationPackage;
import org.flowerplatform.emf_model.notation.View;
import org.osgi.framework.BundleContext;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.codesync.code.adapter.FolderModelAdapter;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.Relation;

/**
 * @author Mariana
 */
public class CodeSyncCodeJavaPlugin extends AbstractFlowerJavaPlugin {

	public static final String TECHNOLOGY = "java";
	
	protected static CodeSyncCodeJavaPlugin INSTANCE;
	
	private FolderModelAdapter folderModelAdapter;
	
	public static CodeSyncCodeJavaPlugin getInstance() {
		return INSTANCE;
	}
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
		
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("class", new JavaClassProcessor());
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classTitle", new JavaClassTitleProcessor());
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classAttribute", new JavaClassAttributeProcessor());
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classOperation", new JavaClassOperationProcessor());
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("scenarioInterraction", new JavaScenarioElementProcessor());
		
		CodeSyncElementRelationsChangesProcessor relationsProcessor = new CodeSyncElementRelationsChangesProcessor();
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("class", relationsProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classAttribute", relationsProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classOperation", relationsProcessor);
		
		AbstractFeatureChangesProcessor processor = new AbstractFeatureChangesProcessor();
		// if model element removed => remove view
		processor.getDependentFeatures().add(new DependentFeature(EObject.class, NotationPackage.eINSTANCE.getView_DiagrammableElement()));
		// if model element removed => remove relations
		processor.getDependentFeatures().add(new DependentFeature(CodeSyncElement.class, CodeSyncPackage.eINSTANCE.getRelation_Source()));
		processor.getDependentFeatures().add(new DependentFeature(CodeSyncElement.class, CodeSyncPackage.eINSTANCE.getRelation_Target()));
		// if view removed => remove edges
		processor.getDependentFeatures().add(new DependentFeature(View.class, NotationPackage.eINSTANCE.getView_SourceEdges()));
		processor.getDependentFeatures().add(new DependentFeature(View.class, NotationPackage.eINSTANCE.getView_TargetEdges()));
		
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("class", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classAttribute", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classOperation", processor);
		
		CodeSyncPlugin.getInstance().getFullyQualifiedNameProvider().addDelegateProvider(new JavaFullyQualifiedNameProvider());
//		ResourcesPlugin.getWorkspace().addResourceChangeListener(new JavaResourceChangeListener());
//		JavaCore.addElementChangedListener(new JavaElementChangedListener(), ElementChangedEvent.POST_RECONCILE);
		
		CommunicationPlugin.getInstance().getServiceRegistry().registerService(JavaClassDiagramOperationsService.SERVICE_ID, new JavaClassDiagramOperationsService());
	}

	public FolderModelAdapter getFolderModelAdapter() {
		return folderModelAdapter;
	}
	
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		INSTANCE = null;
	}
	
	@Override
	public void registerMessageBundle() throws Exception {
		// nothing to do yet
	}

}