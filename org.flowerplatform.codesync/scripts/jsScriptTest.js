// Adding a new descriptor

importClass(org.flowerplatform.codesync.remote.CodeSyncElementDescriptor);
importClass(com.crispico.flower.mp.codesync.base.CodeSyncPlugin);

var descriptor = new CodeSyncElementDescriptor();
descriptor.codeSyncType = "backboneClass1";
descriptor.addCodeSyncTypeCategory("topLevel")
descriptor.label = "Backbone Class (js)";
descriptor.iconUrl = "images/full/obj16/jcu_obj.gif";
descriptor.defaultName = "NewBackboxClass (from js)";
descriptor.extension = "js";
descriptor.addChildrenCodeSyncTypeCategory("backboneClassMember1");
descriptor.addChildrenCodeSyncTypeCategory("requiredEntry");
descriptor.addFeature("superClass1");
descriptor.addFeature("name");
descriptor.keyFeature = "name";
descriptor.standardDiagramControllerProviderFactory = "topLevelBox";
CodeSyncPlugin.instance.codeSyncElementDescriptors.add(descriptor);

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
CodeSyncPlugin.instance.codeSyncElementDescriptors.add(descriptor);

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
CodeSyncPlugin.instance.codeSyncElementDescriptors.add(descriptor);

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

		var attribute = CodeSyncOperationsService.instance.create("javaScriptAttribute1");
		CodeSyncOperationsService.instance.setFeatureValue(attribute, "codeSyncName", "atrFromJSProcessor1");
		CodeSyncOperationsService.instance.add(object, attribute);

//		if (!(object instanceof CodeSyncElement)/* || !object.codeSyncType.equals("backboneClass1") */|| changes.getAddedToContainer() == null) {
//			return;
//		}
		
		var attribute = CodeSyncOperationsService.instance.create("javaScriptAttribute1");
		CodeSyncOperationsService.instance.setFeatureValue(attribute, "codeSyncName", "atrFromJSProcessor");
		CodeSyncOperationsService.instance.add(object, attribute);
	}
});

// and another processor by extending an existing one
importClass(org.flowerplatform.codesync.remote.CodeSyncOperationsService);
importClass(com.crispico.flower.mp.model.codesync.CodeSyncPackage);
importClass(org.flowerplatform.codesync.processor.CodeSyncDecoratorsProcessor);

var proc = new JavaAdapter(CodeSyncDecoratorsProcessor, {
	getLabel: function(object, forEditing) {
		var name = CodeSyncOperationsService.instance
				.getFeatureValue(object, CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name());
		return name + "-js";
	}
});

EditorModelPlugin.instance.diagramUpdaterChangeProcessor.addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass1.javaScriptOperation1", proc);
EditorModelPlugin.instance.diagramUpdaterChangeProcessor.addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass1.javaScriptAttribute1", proc);
