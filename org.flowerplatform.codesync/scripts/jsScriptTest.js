// Adding a new descriptor
utils.load('utils/zfunc.js');
importClass(org.flowerplatform.codesync.remote.CodeSyncElementDescriptor);
importClass(com.crispico.flower.mp.codesync.base.CodeSyncPlugin);

importClass(org.flowerplatform.codesync.code.javascript.operation_extension.JavaScriptFeatureAccessExtension);

var descriptors = new java.util.ArrayList();

var descriptor = new CodeSyncElementDescriptor();
descriptor.codeSyncType = "backboneClass1";
descriptor.addCodeSyncTypeCategory("topLevel")
descriptor.label = "Backbone Class (js)";
descriptor.iconUrl = "images/full/obj16/jcu_obj.gif";
descriptor.defaultName = "NewBackboneClass (from js)";
descriptor.extension = "js";
descriptor.addChildrenCodeSyncTypeCategory("backboneClassMember1");
descriptor.addChildrenCodeSyncTypeCategory("requiredEntry");
descriptor.addFeature("superClass");
descriptor.addFeature("name");
descriptor.keyFeature = "name";
descriptor.standardDiagramControllerProviderFactory = "topLevelBox";
descriptors.add(descriptor);

descriptor = new CodeSyncElementDescriptor();
descriptor.codeSyncType = "javaScriptOperation1";
descriptor.label = "Operation (js)";
descriptor.iconUrl = "images/full/obj16/methpub_obj.gif";
descriptor.defaultName = "newOperation (from js)";
descriptor.addCodeSyncTypeCategory("backboneClassMember1");
descriptor.nextSiblingSeparator = ", ";
descriptor.category = "operations";
descriptor.addFeature("name");
descriptor.addFeature("parameters");
descriptor.keyFeature = "name";
descriptor.standardDiagramControllerProviderFactory = "topLevelBoxChild";
descriptors.add(descriptor);

descriptor = new CodeSyncElementDescriptor();
descriptor.codeSyncType = "javaScriptAttribute1";
descriptor.label = "Attribute (js)";
descriptor.iconUrl = "images/full/obj16/field_public_obj.gif";
descriptor.defaultName = "newAttribute (from js)";
descriptor.addCodeSyncTypeCategory("backboneClassMember1");
descriptor.category = "attributes";
descriptor.nextSiblingSeparator = ", ";
descriptor.addFeature("name");
descriptor.addFeature("defaultValue");
descriptor.keyFeature = "name";
descriptor.standardDiagramControllerProviderFactory = "topLevelBoxChild";
descriptors.add(descriptor);

CodeSyncPlugin.instance.codeSyncElementDescriptors.addAll(descriptors);

CodeSyncPlugin.instance.featureAccessExtensions.add(new JavaScriptFeatureAccessExtension(descriptors));

importClass(org.flowerplatform.codesync.processor.ChildrenUpdaterDiagramProcessor);
importClass(org.flowerplatform.editor.model.EditorModelPlugin);

var parentElementProcessor = new ChildrenUpdaterDiagramProcessor();
EditorModelPlugin.instance.diagramUpdaterChangeProcessor.addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass1", parentElementProcessor);


// adding a new processor by implementing an interface
importClass(org.flowerplatform.editor.model.changes_processor.IChangesProcessor);
importClass(com.crispico.flower.mp.model.codesync.CodeSyncElement);
importClass(org.flowerplatform.codesync.remote.CodeSyncOperationsService);

CodeSyncPlugin.instance.codeSyncTypeCriterionDispatcherProcessor.addProcessor("backboneClass1", new IChangesProcessor() {
	processChanges: function(context, object, changes) {
		var children = object.children;
		if (children.size() == 0) {
			var attribute = CodeSyncOperationsService.instance.create("javaScriptOperation1");
			CodeSyncOperationsService.instance.setFeatureValue(attribute, "codeSyncName", "atrFromJSProcessor1");
			CodeSyncOperationsService.instance.add(object, attribute);

//		if (!(object instanceof CodeSyncElement)/* || !object.codeSyncType.equals("backboneClass1") */|| changes.getAddedToContainer() == null) {
//			return;
//		}
		
			var attribute = CodeSyncOperationsService.instance.create("javaScriptAttribute1");
			CodeSyncOperationsService.instance.setFeatureValue(attribute, "codeSyncName", "atrFromJSProcessor");
			CodeSyncOperationsService.instance.add(object, attribute);
		}
	}
});

// and another processor by extending an existing one
importClass(org.flowerplatform.codesync.remote.CodeSyncOperationsService);
importClass(com.crispico.flower.mp.model.codesync.CodeSyncPackage);
importClass(org.flowerplatform.codesync.processor.TopLevelElementChildProcessor);

var proc = new JavaAdapter(TopLevelElementChildProcessor, {
	processor: new TopLevelElementChildProcessor(),
	getLabel: function(object, view, forEditing) {
		try {
			var name = this.processor.getLabel(object, view, forEditing);
			return name + zfunc_fs();
		} catch (e) {
			return "error: " + e.message;
		}
	}
});

EditorModelPlugin.instance.diagramUpdaterChangeProcessor.addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass1.javaScriptOperation1", proc);
EditorModelPlugin.instance.diagramUpdaterChangeProcessor.addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass1.javaScriptAttribute1", proc);
