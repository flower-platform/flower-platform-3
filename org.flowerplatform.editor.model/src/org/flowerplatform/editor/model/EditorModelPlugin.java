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

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.flowerplatform.blazeds.custom_serialization.CustomSerializationDescriptor;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.editor.model.change_processor.ComposedChangeProcessor;
import org.flowerplatform.editor.model.change_processor.DiagramUpdaterChangeProcessor;
import org.flowerplatform.emf_model.notation.Bounds;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.notation.Edge;
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
	
	protected ComposedChangeProcessor composedChangeProcessor;
	
	protected DiagramUpdaterChangeProcessor diagramUpdaterChangeProcessor;
	
	protected ComposedDragOnDiagramHandler composedDragOnDiagramHandler;
	
	public ComposedChangeProcessor getComposedChangeProcessor() {
		return composedChangeProcessor;
	}

	public DiagramUpdaterChangeProcessor getDiagramUpdaterChangeProcessor() {
		return diagramUpdaterChangeProcessor;
	}

	public ComposedDragOnDiagramHandler getComposedDragOnDiagramHandler() {
		return composedDragOnDiagramHandler;
	}

	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
		
		composedChangeProcessor = new ComposedChangeProcessor();
		diagramUpdaterChangeProcessor = new DiagramUpdaterChangeProcessor();
		composedChangeProcessor.addChangeDescriptionProcessor(diagramUpdaterChangeProcessor);
		composedDragOnDiagramHandler = new ComposedDragOnDiagramHandler();
		
		IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.flowerplatform.editor.model.dragOnDiagramHandler");
		for (IConfigurationElement configurationElement : configurationElements) {
			IDragOnDiagramHandler handler = (IDragOnDiagramHandler) configurationElement.createExecutableExtension("dragOnDiagramHandler");
			composedDragOnDiagramHandler.addDelegateHandler(handler);
		}
		
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
	}

	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// do nothing, because we don't have messages (yet)
	}

}