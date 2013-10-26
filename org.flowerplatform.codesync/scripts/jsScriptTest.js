// Adding a new descriptor

importClass(org.flowerplatform.codesync.remote.CodeSyncElementDescriptor);

var descriptor = new CodeSyncElementDescriptor();
descriptor.codeSyncType = "backboneClass";
descriptor.label = "Backbone Class (js)";
descriptor.defaultName = "NewBackboxClass";
descriptor.extension = "js";
descriptor.addChildrenCodeSyncTypeCategory("backboneClassMember");
descriptor.addChildrenCodeSyncTypeCategory("requiredEntry");
descriptor.addFeature("superClass");
descriptor.addFeature("name");
descriptor.keyFeature = "name";

importClass(com.crispico.flower.mp.codesync.base.CodeSyncPlugin);
CodeSyncPlugin.instance.codeSyncElementDescriptors.add(descriptor);

// adding a new processor by implementing an interface
importClass(org.flowerplatform.editor.model.changes_processor.IChangesProcessor);
importClass(com.crispico.flower.mp.model.codesync.CodeSyncElement);
importClass(org.flowerplatform.codesync.remote.CodeSyncOperationsService);
var tablProcessor = new IChangesProcessor() {
	processChanges: function(context, object, changes) {
		if (changes.getAddedToContainer() == null) {
			return;
		}
		
		var attribute = CodeSyncOperationsService.instance.create("javaScriptAttribute");
		CodeSyncOperationsService.instance.setFeatureValue(attribute, "name", "tpl");
		CodeSyncOperationsService.instance.add(object, attribute);
	}
};

CodeSyncPlugin.instance.codeSyncTypeCriterionDispatcherProcessor.addProcessor("backboneClass", tablProcessor);

// and another processor by extending an existing one
importClass(org.flowerplatform.codesync.code.javascript.processor.JavascriptElementProcessor);
importClass(org.flowerplatform.codesync.remote.CodeSyncOperationsService);
importClass(com.crispico.flower.mp.model.codesync.CodeSyncPackage);
var proc = new JavaAdapter(JavascriptElementProcessor, {
	getLabel: function(object, forEditing) {
		var name = CodeSyncOperationsService.instance
				.getFeatureValue(object, CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name());
		return name + "(from js)";
	}
});

importClass(org.flowerplatform.editor.model.EditorModelPlugin);
EditorModelPlugin.instance.diagramUpdaterChangeProcessor.addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.javaScriptAttribute", proc);
