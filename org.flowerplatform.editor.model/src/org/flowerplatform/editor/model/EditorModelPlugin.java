package org.flowerplatform.editor.model;

import org.flowerplatform.blazeds.custom_serialization.CustomSerializationDescriptor;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.emf_model.notation.Bounds;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.notation.Location;
import org.flowerplatform.emf_model.notation.Node;
import org.flowerplatform.emf_model.notation.View;
import org.osgi.framework.BundleContext;

public class EditorModelPlugin extends AbstractFlowerJavaPlugin {

	protected static EditorModelPlugin INSTANCE;
	
	public static EditorModelPlugin getInstance() {
		return INSTANCE;
	}
	
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
		
		CustomSerializationDescriptor viewSD = new CustomSerializationDescriptor(View.class)
		.addDeclaredProperty("id")
		.addDeclaredProperty("viewType")
		.addDeclaredProperty("persistentChildren_RH")
		.register();

		new CustomSerializationDescriptor(Node.class)
		.addDeclaredProperties(viewSD.getDeclaredProperties())
		.addDeclaredProperty("layoutConstraint_RH")
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

}
