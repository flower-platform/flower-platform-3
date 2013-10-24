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
package org.flowerplatform.editor.model;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.flowerplatform.blazeds.custom_serialization.CustomSerializationDescriptor;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.editor.model.change_processor.ComposedChangeProcessor;
import org.flowerplatform.editor.model.change_processor.DiagramPropertiesChangeProcessor;
import org.flowerplatform.editor.model.change_processor.DiagramUpdaterChangeProcessor;
import org.flowerplatform.editor.model.changes_processor.ClassCriterionDispatcherProcessor;
import org.flowerplatform.editor.model.changes_processor.MainChangesDispatcher;
import org.flowerplatform.emf_model.notation.Bounds;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.notation.Edge;
import org.flowerplatform.emf_model.notation.ExpandableNode;
import org.flowerplatform.emf_model.notation.Location;
import org.flowerplatform.emf_model.notation.MindMapNode;
import org.flowerplatform.emf_model.notation.Node;
import org.flowerplatform.emf_model.notation.View;
import org.osgi.framework.BundleContext;

public class EditorModelPlugin extends AbstractFlowerJavaPlugin {

	protected static EditorModelPlugin INSTANCE;
	
	public static EditorModelPlugin getInstance() {
		return INSTANCE;
	}
	
	protected MainChangesDispatcher mainChangesDispatcher;
	
	protected ClassCriterionDispatcherProcessor classCriterionDispatcherProcessor;
	
	protected ComposedChangeProcessor composedChangeProcessor;
	
	protected DiagramUpdaterChangeProcessor diagramUpdaterChangeProcessor;
	
	protected ComposedDragOnDiagramHandler composedDragOnDiagramHandler;

	private IModelAccessController modelAccessController;

	protected ComposedContentAssist composedContentAssist;

	public MainChangesDispatcher getMainChangesDispatcher() {
		return mainChangesDispatcher;
	}

	public ClassCriterionDispatcherProcessor getClassCriterionDispatcherProcessor() {
		return classCriterionDispatcherProcessor;
	}

	public ComposedChangeProcessor getComposedChangeProcessor() {
		return composedChangeProcessor;
	}

	public DiagramUpdaterChangeProcessor getDiagramUpdaterChangeProcessor() {
		return diagramUpdaterChangeProcessor;
	}

	public ComposedDragOnDiagramHandler getComposedDragOnDiagramHandler() {
		return composedDragOnDiagramHandler;
	}
	
	public ComposedContentAssist getComposedContentAssist() {
		return composedContentAssist;
	}

	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
		
		mainChangesDispatcher = new MainChangesDispatcher();
		classCriterionDispatcherProcessor = new ClassCriterionDispatcherProcessor();
		mainChangesDispatcher.addProcessor(classCriterionDispatcherProcessor);
		
		composedChangeProcessor = new ComposedChangeProcessor();
		diagramUpdaterChangeProcessor = new DiagramUpdaterChangeProcessor();
		composedChangeProcessor.addChangeDescriptionProcessor(diagramUpdaterChangeProcessor);
		
		initExtensionPoint_dragOnDiagramHandler();
		initExtensionPoint_contentAssist();
		
		CustomSerializationDescriptor viewSD = new CustomSerializationDescriptor(View.class)
		.addDeclaredProperty("id")
		.addDeclaredProperty("viewType")
		.addDeclaredProperty("persistentChildren_RH")
		.addDeclaredProperty("parentView_RH")
		.register();

		new CustomSerializationDescriptor(Node.class)
		.addDeclaredProperties(viewSD.getDeclaredProperties())
		.addDeclaredProperty("layoutConstraint_RH")
		.register();

		new CustomSerializationDescriptor(Edge.class)
		.addDeclaredProperties(viewSD.getDeclaredProperties())
		.addDeclaredProperty("source_RH")
		.addDeclaredProperty("target_RH")
		.register();
		
		new CustomSerializationDescriptor(ExpandableNode.class)
		.addDeclaredProperties(viewSD.getDeclaredProperties())

		.addDeclaredProperty("expanded")
		.addDeclaredProperty("hasChildren")
		.addDeclaredProperty("template")
		.register();
		
		new CustomSerializationDescriptor(MindMapNode.class)
		.addDeclaredProperties(viewSD.getDeclaredProperties())

		.register();
		
		new CustomSerializationDescriptor(Diagram.class)
		.addDeclaredProperties(viewSD.getDeclaredProperties())
		.addDeclaredProperty("persistentEdges_RH")
		.register();
		
		CustomSerializationDescriptor superClassSD = new CustomSerializationDescriptor(Location.class)
		.addDeclaredProperty("id")
		.addDeclaredProperty("y")
		.addDeclaredProperty("x")
		.register();

		new CustomSerializationDescriptor(Bounds.class)
		.addDeclaredProperties(superClassSD.getDeclaredProperties())
		.addDeclaredProperty("width")
		.addDeclaredProperty("height")
		.register();
		
		getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram", new DiagramPropertiesChangeProcessor());
	}

	protected void initExtensionPoint_dragOnDiagramHandler() throws CoreException {
		composedDragOnDiagramHandler = new ComposedDragOnDiagramHandler();
		IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.flowerplatform.editor.model.dragOnDiagramHandler");
		for (IConfigurationElement configurationElement : configurationElements) {
			IDragOnDiagramHandler handler = (IDragOnDiagramHandler) configurationElement.createExecutableExtension("dragOnDiagramHandler");
			composedDragOnDiagramHandler.addDelegateHandler(handler);
		}
	}
	
	protected void initExtensionPoint_contentAssist() throws CoreException {
		composedContentAssist = new ComposedContentAssist();
		IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.flowerplatform.editor.model.contentAssist");
		for (IConfigurationElement configurationElement : configurationElements) {
			IContentAssist contentAssist = (IContentAssist) configurationElement.createExecutableExtension("contentAssist");
			composedContentAssist.addDelegateSearchEngine(contentAssist);
		}
	}
	
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}

	public IModelAccessController getModelAccessController() {
		return modelAccessController;
	}

	public void setModelAccessController(IModelAccessController modelAccessController) {
		this.modelAccessController = modelAccessController;
	}
	
}
