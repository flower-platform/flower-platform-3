// Adding a new descriptor
import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;

import org.flowerplatform.codesync.code.javascript.operation_extension.JavaScriptFeatureAccessExtension;

descriptors = new java.util.ArrayList();

descriptor = new CodeSyncElementDescriptor();
descriptor.codeSyncType = "backboneClass2";
descriptor.addCodeSyncTypeCategory("topLevel")
descriptor.label = "Backbone Class (groovy)";
descriptor.iconUrl = "images/full/obj16/jcu_obj.gif";
descriptor.defaultName = "NewBackboneClass (from groovy)";
descriptor.extension = "js";
descriptor.addChildrenCodeSyncTypeCategory("backboneClassMember2");
descriptor.addChildrenCodeSyncTypeCategory("requiredEntry");
descriptor.addFeature("superClass");
descriptor.addFeature("name");
descriptor.keyFeature = "name";
descriptor.standardDiagramControllerProviderFactory = "topLevelBox";
descriptors.add(descriptor);

descriptor = new CodeSyncElementDescriptor();
descriptor.codeSyncType = "javaScriptOperation2";
descriptor.label = "Operation (groovy)";
descriptor.iconUrl = "images/full/obj16/methpub_obj.gif";
descriptor.defaultName = "newOperation (from groovy)";
descriptor.addCodeSyncTypeCategory("backboneClassMember2");
descriptor.nextSiblingSeparator = ", ";
descriptor.category = "operations";
descriptor.addFeature("name");
descriptor.addFeature("parameters");
descriptor.keyFeature = "name";
descriptor.standardDiagramControllerProviderFactory = "topLevelBoxChild";
descriptors.add(descriptor);

descriptor = new CodeSyncElementDescriptor();
descriptor.codeSyncType = "javaScriptAttribute2";
descriptor.label = "Attribute (groovy)";
descriptor.iconUrl = "images/full/obj16/field_public_obj.gif";
descriptor.defaultName = "newAttribute (from groovy)";
descriptor.addCodeSyncTypeCategory("backboneClassMember2");
descriptor.category = "attributes";
descriptor.nextSiblingSeparator = ", ";
descriptor.addFeature("name");
descriptor.addFeature("defaultValue");
descriptor.keyFeature = "name";
descriptor.standardDiagramControllerProviderFactory = "topLevelBoxChild";
descriptors.add(descriptor);

CodeSyncPlugin.instance.codeSyncElementDescriptors.addAll(descriptors);

CodeSyncPlugin.instance.featureAccessExtensions.add(new JavaScriptFeatureAccessExtension(descriptors));

import org.flowerplatform.codesync.processor.ChildrenUpdaterDiagramProcessor;
import org.flowerplatform.editor.model.EditorModelPlugin;

parentElementProcessor = new ChildrenUpdaterDiagramProcessor();
EditorModelPlugin.instance.diagramUpdaterChangeProcessor.addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass2", parentElementProcessor);


// adding a new processor by implementing an interface
import org.flowerplatform.editor.model.changes_processor.IChangesProcessor;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;

changeProcessor = [processChanges: {Object[] args -> 
		children = args[1].children;
		if (children.size() == 0) {
			attribute = CodeSyncOperationsService.instance.create("javaScriptOperation2");
			CodeSyncOperationsService.instance.setFeatureValue(attribute, "codeSyncName", "atrFromJSProcessor");
			CodeSyncOperationsService.instance.add(args[1], attribute);

			attribute = CodeSyncOperationsService.instance.create("javaScriptAttribute2");
			CodeSyncOperationsService.instance.setFeatureValue(attribute, "codeSyncName", "atrFromJSProcessor");
			CodeSyncOperationsService.instance.add(args[1], attribute);
		}
}] as IChangesProcessor;

CodeSyncPlugin.instance.codeSyncTypeCriterionDispatcherProcessor.addProcessor("backboneClass2", changeProcessor);

// and another processor by extending an existing one
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import org.flowerplatform.codesync.processor.TopLevelElementChildProcessor;


import org.eclipse.emf.ecore.EObject;
import org.flowerplatform.emf_model.notation.View;

TopLevelElementChildProcessor proc = new TopLevelElementChildProcessor() {
	public String getLabel(EObject object, View view, boolean forEditing) {
		return super.getLabel(object, view, forEditing) + "-gv";
	}
};


EditorModelPlugin.instance.diagramUpdaterChangeProcessor.addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass2.javaScriptOperation2", proc);
EditorModelPlugin.instance.diagramUpdaterChangeProcessor.addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass2.javaScriptAttribute2", proc);
