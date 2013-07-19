package org.flowerplatform.editor.model;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.flowerplatform.blazeds.custom_serialization.CustomSerializationDescriptor;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.editor.model.change_processor.ComposedChangeProcessor;
import org.flowerplatform.editor.model.change_processor.DiagramUpdaterChangeProcessor;
import org.flowerplatform.emf_model.notation.Bounds;
import org.flowerplatform.emf_model.notation.Diagram;
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

		new CustomSerializationDescriptor(MindMapNode.class)
		.addDeclaredProperties(viewSD.getDeclaredProperties())
		.addDeclaredProperty("expanded")
		.addDeclaredProperty("side")
		.addDeclaredProperty("hasChildren")
		.register();
		
		new CustomSerializationDescriptor(Diagram.class)
		.addDeclaredProperties(viewSD.getDeclaredProperties())
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
