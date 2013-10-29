package org.flowerplatform.codesync.code.javascript.config;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.codesync.code.javascript.config.changes_processor.AttributeWithRequireEntryDependencyProcessor;
import org.flowerplatform.codesync.code.javascript.config.changes_processor.InheritanceProcessor;
import org.flowerplatform.codesync.code.javascript.config.changes_processor.RequireEntryDependencyProcessor;
import org.flowerplatform.codesync.code.javascript.config.extension.AddNewExtension_BackboneClass;
import org.flowerplatform.codesync.code.javascript.config.extension.AddNewExtension_BackboneFormView;
import org.flowerplatform.codesync.code.javascript.config.extension.AddNewExtension_BackboneTableItemView;
import org.flowerplatform.codesync.code.javascript.config.extension.AddNewExtension_BackboneTableView;
import org.flowerplatform.codesync.code.javascript.config.extension.AddNewExtension_BackboneView;
import org.flowerplatform.codesync.code.javascript.operation_extension.JavaScriptFeatureAccessExtension;
import org.flowerplatform.codesync.config.extension.InplaceEditorExtension_RegExFormat;
import org.flowerplatform.codesync.config.extension.NamedElementFeatureAccessExtension;
import org.flowerplatform.codesync.processor.ChildrenUpdaterDiagramProcessor;
import org.flowerplatform.codesync.processor.CodeSyncCategorySeparatorProcessor;
import org.flowerplatform.codesync.processor.TopLevelElementChildProcessor;
import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
import org.flowerplatform.codesync.remote.RelationDescriptor;
import org.flowerplatform.editor.model.EditorModelPlugin;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;

public class JavaScriptDescriptors implements Runnable {

	public static final String TYPE_BACKBONE_CLASS = "backboneClass";
	public static final String TYPE_JAVASCRIPT_ATTRIBUTE = "javaScriptAttribute";
	public static final String TYPE_REQUIRE_ENTRY = "requireEntry";
	public static final String TYPE_CLASS_DEPENDENCY = "classDependency";
	public static final String TYPE_HTML_TEMPLATE_DEPENDENCY = "htmlTemplateDependency";
	public static final String TYPE_REQUIRE_CLASS_DEPENDENCY = "requireClassDependency";
	public static final String TYPE_REQUIRE_HTML_TEMPLATE_DEPENDENCY = "requireHtmlTemplateDependency";
	public static final String TYPE_INHERITANCE = "inheritance";
	
	public static final String TYPE_JAVASCRIPT_FILE = "javaScriptFile";
	public static final String TYPE_HTML_FILE = "htmlFile";
	
	public static final String FEATURE_NAME = "name";
	public static final String FEATURE_DEPENDENCY_PATH = "dependencyPath";
	public static final String FEATURE_VAR_NAME = "varName";
	public static final String FEATURE_DEFAULT_VALUE = "defaultValue";
	public static final String FEATURE_SUPER_CLASS = "superClass";

	public static final String INIT_TYPE_NONE = "";
	public static final String INIT_TYPE_BACKBONE_VIEW = "backboneView";
	public static final String INIT_TYPE_BACKBONE_FORM_VIEW = "backboneFormView";
	public static final String INIT_TYPE_BACKBONE_TABLE_VIEW = "backboneTableView";
	public static final String INIT_TYPE_BACKBONE_TABLE_ITEM_VIEW = "backboneTableItemView";

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		List<CodeSyncElementDescriptor> descriptors = new ArrayList<CodeSyncElementDescriptor>();
		
		/////////////////////////////////////////
		// TOP LEVEL
		/////////////////////////////////////////
		descriptors.add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType(TYPE_BACKBONE_CLASS).setLabel("Backbone Class")
				.addCodeSyncTypeCategory("topLevel")
				.addInitializationType(INIT_TYPE_NONE).addInitializationTypeLabel("Plain Backbone Class")
				.addInitializationType(INIT_TYPE_BACKBONE_VIEW).addInitializationTypeLabel("Plain View")
				.addInitializationType(INIT_TYPE_BACKBONE_TABLE_VIEW).addInitializationTypeLabel("Table View")
				.addInitializationType(INIT_TYPE_BACKBONE_TABLE_ITEM_VIEW).addInitializationTypeLabel("Table Item View")
				.addInitializationType(INIT_TYPE_BACKBONE_FORM_VIEW).addInitializationTypeLabel("Form View")
				.setIconUrl("images/full/obj16/jcu_obj.gif")
				.setDefaultName("NewBackboneClass")
				.setExtension("js")
				.addChildrenCodeSyncTypeCategory("backboneClassMember")
				.addChildrenCodeSyncTypeCategory(TYPE_REQUIRE_ENTRY)
				.addFeature(FEATURE_NAME)
				.addFeature(FEATURE_SUPER_CLASS)
				.setKeyFeature(FEATURE_NAME)
				.setStandardDiagramControllerProviderFactory("topLevelBox")
		);
		descriptors.add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("javaScriptOperation")
				.setLabel("Operation")
				.setIconUrl("images/full/obj16/methpub_obj.gif")
				.setDefaultName("newOperation")
				.addCodeSyncTypeCategory("backboneClassMember")
				.setCategory("operations")
				.setNextSiblingSeparator(", ")
				.addFeature(FEATURE_NAME)
				.addFeature("parameters")
				.setKeyFeature(FEATURE_NAME)
				.setStandardDiagramControllerProviderFactory("topLevelBoxChild")
				.setInplaceEditorFeature("")
		);
		descriptors.add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType(TYPE_JAVASCRIPT_ATTRIBUTE)
				.setLabel("Attribute")
				.setIconUrl("images/full/obj16/field_public_obj.gif")
				.setDefaultName("newAttribute")
				.addCodeSyncTypeCategory("backboneClassMember")
				.setCategory("attributes")
				.setNextSiblingSeparator(", ")
				.addFeature(FEATURE_NAME)
				.addFeature(FEATURE_DEFAULT_VALUE)
				.setKeyFeature(FEATURE_NAME)
				.setStandardDiagramControllerProviderFactory("topLevelBoxChild")
				.setInplaceEditorFeature("")
		);
		descriptors.add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType(JavaScriptDescriptors.TYPE_REQUIRE_ENTRY)
				.setLabel("RequireEntry")
				.setIconUrl("images/full/obj16/imp_obj.gif")
				.setDefaultName("newRequireEntry")
				.addCodeSyncTypeCategory(TYPE_REQUIRE_ENTRY)
				.setCategory("require entries")
				.addFeature(FEATURE_VAR_NAME)
				.addFeature(FEATURE_DEPENDENCY_PATH)
				.setKeyFeature(FEATURE_VAR_NAME)
				.setStandardDiagramControllerProviderFactory("topLevelBoxChild")
				.setInplaceEditorFeature("")
		);
		descriptors.add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("eventsAttribute")
				.setLabel("Events")
				.setIconUrl("images/full/obj16/time_go.png")
				.setDefaultName("events")
				.addCodeSyncTypeCategory("backboneClassMember")
				.addChildrenCodeSyncTypeCategory("eventsAttributeEntry")
				.setCategory("attributes")
				.setNextSiblingSeparator(", ")
				.addFeature(FEATURE_NAME)
				.setKeyFeature(FEATURE_NAME)
				.setStandardDiagramControllerProviderFactory("topLevelBoxChild")
		);
		descriptors.add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("routesAttribute")
				.setLabel("Routes")
				.setIconUrl("images/full/obj16/arrow_right.png")
				.setDefaultName("routes")
				.addCodeSyncTypeCategory("backboneClassMember")
				.addChildrenCodeSyncTypeCategory("routesAttributeEntry")
				.setCategory("attributes")
				.setNextSiblingSeparator(", ")
				.addFeature(FEATURE_NAME)
				.setKeyFeature(FEATURE_NAME)
				.setStandardDiagramControllerProviderFactory("topLevelBoxChild")
		);
		descriptors.add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("eventsAttributeEntry")
				.setLabel("Event")
				.setIconUrl("images/full/obj16/time_go.png")
				.setDefaultName("event")
				.addCodeSyncTypeCategory("eventsAttributeEntry")
				.setNextSiblingSeparator(", ")
				.addFeature("event")
				.addFeature("selector")
				.addFeature("function")
				.setKeyFeature("event")
				.setStandardDiagramControllerProviderFactory("topLevelBoxChild")
		);
		descriptors.add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("routesAttributeEntry")
				.setLabel("Route")
				.setIconUrl("images/full/obj16/arrow_right.png")
				.setDefaultName("route")
				.addCodeSyncTypeCategory("routesAttributeEntry")
				.setNextSiblingSeparator(", ")
				.addFeature("path")
				.addFeature("function")
				.setKeyFeature("path")
				.setStandardDiagramControllerProviderFactory("topLevelBoxChild")
		);
		
		/////////////////////////////////////////
		// TOP LEVEL
		/////////////////////////////////////////
		descriptors.add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType(TYPE_JAVASCRIPT_FILE)
				.addCodeSyncTypeCategory("topLevel")
				.setLabel("JavaScript File")
				.setIconUrl("images/full/obj16/jcu_obj.gif")
				.setDefaultName("NewJavaScriptFile")
				.setExtension(".js")
				.addChildrenCodeSyncTypeCategory("backboneClassMember")
				.addFeature(FEATURE_NAME)
				.setKeyFeature(FEATURE_NAME)
				.setStandardDiagramControllerProviderFactory("topLevelBox"));
		
		/////////////////////////////////////////
		// TOP LEVEL
		/////////////////////////////////////////
		descriptors.add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("table")
				.addCodeSyncTypeCategory("topLevel")
				.addCodeSyncTypeCategory("htmlTemplate")
				.setLabel("Table HTML Template")
				.setIconUrl("images/full/obj16/table.png")
				.setDefaultName("NewTableTemplate")
				.setExtension("html")
				.addChildrenCodeSyncTypeCategory("tableHeaderEntry")
				.addFeature(FEATURE_NAME)
				.addFeature("tableId")
				.addFeature("headerRowId")
				.setKeyFeature(FEATURE_NAME)
				.setStandardDiagramControllerProviderFactory("topLevelBox")
		);

		descriptors.add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("tableHeaderEntry")
				.setLabel("Table Header Entry")
				.setIconUrl("images/full/obj16/table_select_column.png")
				.setDefaultName("newHeaderEntry")
				.addCodeSyncTypeCategory("tableHeaderEntry")
				.setCategory("header entries")
				.addFeature("title")
				.setKeyFeature("title")
				.setStandardDiagramControllerProviderFactory("topLevelBoxChild")
		);
		
		/////////////////////////////////////////
		// TOP LEVEL
		/////////////////////////////////////////
		descriptors.add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("tableItem")
				.addCodeSyncTypeCategory("topLevel")
				.addCodeSyncTypeCategory("htmlTemplate")
				.setLabel("Table Item HTML Template")
				.setIconUrl("images/full/obj16/table_select_row.png")
				.setDefaultName("NewTableItemTemplate")
				.setExtension("html")
				.addChildrenCodeSyncTypeCategory("tableItemEntry")
				.addFeature(FEATURE_NAME)
				.addFeature("itemUrl")
				.setKeyFeature(FEATURE_NAME)
				.setStandardDiagramControllerProviderFactory("topLevelBox")
		);
		descriptors.add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("tableItemEntry")
				.setLabel("Table Item Entry")
				.setIconUrl("images/full/obj16/table_select.png")
				.setDefaultName("newTableItemEntry")
				.addCodeSyncTypeCategory("tableItemEntry")
				.setCategory("item entries")
				.addFeature("valueExpression")
				.setKeyFeature("valueExpression")
				.setStandardDiagramControllerProviderFactory("topLevelBoxChild")
		);
		
		/////////////////////////////////////////
		// TOP LEVEL
		/////////////////////////////////////////
		descriptors.add(
				new CodeSyncElementDescriptor()
				.addCodeSyncTypeCategory("topLevel")
				.setCodeSyncType("form")
				.addCodeSyncTypeCategory("htmlTemplate")
				.setLabel("Form HTML Template")
				.setIconUrl("images/full/obj16/application_form.png")
				.setDefaultName("NewFormTemplate")
				.setExtension("html")
				.addChildrenCodeSyncTypeCategory("formItem")
				.addFeature(FEATURE_NAME)
				.setKeyFeature(FEATURE_NAME)
				.setStandardDiagramControllerProviderFactory("topLevelBox")
		);
		descriptors.add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("formItem")
				.setLabel("Form Item")
				.setIconUrl("images/full/obj16/bullet_textfield.png")
				.setDefaultName("newFormItem")
				.addCodeSyncTypeCategory("formItem")
				.setCategory("items")
				.addFeature("valueExpression")
				.addFeature("editId")
				.addFeature("title")
				.setKeyFeature("title")
				.setStandardDiagramControllerProviderFactory("topLevelBoxChild")
		);
		
		/////////////////////////////////////////
		// TOP LEVEL
		/////////////////////////////////////////
		descriptors.add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType(TYPE_HTML_FILE)
				.addFeature(FEATURE_NAME)
				.setKeyFeature(FEATURE_NAME));
		
		/////////////////////////////////////////
		// TOP LEVEL
		/////////////////////////////////////////
		descriptors.add(
				new CodeSyncElementDescriptor()
				.addCodeSyncTypeCategory("topLevel").addCodeSyncTypeCategory("dontNeedLocation")
				.setCodeSyncType("note")
				.setLabel("Note")
				.setIconUrl("images/full/obj16/note.png")
				.setDefaultName("NewNote")				
				.setCreateCodeSyncElement(false)				
		);
		
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().addAll(descriptors);
		
		CodeSyncPlugin.getInstance().getRelationDescriptors().add(
				new RelationDescriptor()
				.setType(TYPE_HTML_TEMPLATE_DEPENDENCY)
				.setLabel("HTML Template Dependency")
				.addSourceCodeSyncType(TYPE_JAVASCRIPT_ATTRIBUTE)
				.addTargetCodeSyncTypeCategory("htmlTemplate")
		);
			
		CodeSyncPlugin.getInstance().getRelationDescriptors().add(
				new RelationDescriptor()
				.setType(TYPE_CLASS_DEPENDENCY)
				.setLabel("Class Dependency")
				.addSourceCodeSyncType(TYPE_JAVASCRIPT_ATTRIBUTE)
				.addTargetCodeSyncType(TYPE_BACKBONE_CLASS)
		);
		CodeSyncPlugin.getInstance().getRelationDescriptors().add(
				new RelationDescriptor()
				.setType(TYPE_REQUIRE_CLASS_DEPENDENCY)
				.setLabel("Require Class")
				.addSourceCodeSyncType(TYPE_REQUIRE_ENTRY)
				.addTargetCodeSyncType(TYPE_BACKBONE_CLASS)
		);
		CodeSyncPlugin.getInstance().getRelationDescriptors().add(
				new RelationDescriptor()
				.setType(TYPE_REQUIRE_HTML_TEMPLATE_DEPENDENCY)
				.setLabel("Require HTML Template")
				.addSourceCodeSyncType(TYPE_REQUIRE_ENTRY)
				.addTargetCodeSyncTypeCategory("htmlTemplate")
		);
		CodeSyncPlugin.getInstance().getRelationDescriptors().add(
				new RelationDescriptor()
				.setType(TYPE_INHERITANCE)
				.setLabel("Inherits")
				.addSourceCodeSyncType(TYPE_BACKBONE_CLASS)
				.addTargetCodeSyncType(TYPE_BACKBONE_CLASS)
		);
		
		// extensions
		CodeSyncPlugin.getInstance().getFeatureAccessExtensions().add(new JavaScriptFeatureAccessExtension(descriptors));
		CodeSyncPlugin.getInstance().getAddNewExtensions().add(new AddNewExtension_BackboneClass());
		CodeSyncPlugin.getInstance().getAddNewExtensions().add(new AddNewExtension_BackboneView());
		CodeSyncPlugin.getInstance().getAddNewExtensions().add(new AddNewExtension_BackboneTableView());
		CodeSyncPlugin.getInstance().getAddNewExtensions().add(new AddNewExtension_BackboneTableItemView());
		CodeSyncPlugin.getInstance().getAddNewExtensions().add(new AddNewExtension_BackboneFormView());
		
		InplaceEditorExtension_RegExFormat javascriptAttributeInplaceEditorExtension = 
				new InplaceEditorExtension_RegExFormat(
						"javaScriptAttribute", 
						new String[] {NamedElementFeatureAccessExtension.NAME, "defaultValue"}, 
						"%1$s=%2$s", 
						"^(.\\w*?)[=]*=(.*?)$");
		CodeSyncPlugin.getInstance().getInplaceEditorExtensions().add(javascriptAttributeInplaceEditorExtension);
		
		InplaceEditorExtension_RegExFormat javascriptOperationInplaceEditorExtension = 
				new InplaceEditorExtension_RegExFormat(
						"javaScriptOperation", 
						new String[] {NamedElementFeatureAccessExtension.NAME, "parameters"}, 
						"%1$s(%2$s)", 
						"(.\\w*?)[\\(\\s]*\\([\\s]*(.*?)[\\s]*[\\)\\s]*\\)");
		CodeSyncPlugin.getInstance().getInplaceEditorExtensions().add(javascriptOperationInplaceEditorExtension);
		
		InplaceEditorExtension_RegExFormat requireEntryInplaceEditorExtension = 
				new InplaceEditorExtension_RegExFormat(
						JavaScriptDescriptors.TYPE_REQUIRE_ENTRY, 
						new String[] {FEATURE_VAR_NAME, FEATURE_DEPENDENCY_PATH}, 
						"%1$s:%2$s", 
						"^(.\\w*?)[:]*:(.*?)$");
		CodeSyncPlugin.getInstance().getInplaceEditorExtensions().add(requireEntryInplaceEditorExtension);
		
		// processors
		ChildrenUpdaterDiagramProcessor parentElementProcessor = new ChildrenUpdaterDiagramProcessor();
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass", parentElementProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.javaScriptFile", parentElementProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.table", parentElementProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.tableItem", parentElementProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.formItem", parentElementProcessor);
		
		TopLevelElementChildProcessor childElementProcessor = new TopLevelElementChildProcessor();
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("topLevelBoxTitle", childElementProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.requireEntry", childElementProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.eventsAttribute", childElementProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.routesAttribute", childElementProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.eventsAttribute.eventsAttributeEntry", childElementProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.routesAttribute.routesAttributeEntry", childElementProcessor);
		
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.javaScriptOperation", new TopLevelElementChildProcessor(javascriptOperationInplaceEditorExtension));
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.javaScriptAttribute", new TopLevelElementChildProcessor(javascriptAttributeInplaceEditorExtension));
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.javaScriptFile.javaScriptOperation", new TopLevelElementChildProcessor(javascriptOperationInplaceEditorExtension));
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.javaScriptFile.javaScriptAttribute", new TopLevelElementChildProcessor(javascriptAttributeInplaceEditorExtension));
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.requireEntry", new TopLevelElementChildProcessor(requireEntryInplaceEditorExtension));

		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.table.tableHeaderEntry", childElementProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.tableItem.tableItemEntry", childElementProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.form.formItem", childElementProcessor);
		
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("categorySeparator", new CodeSyncCategorySeparatorProcessor());
		
		CodeSyncPlugin.getInstance().getCodeSyncTypeCriterionDispatcherProcessor().addProcessor(JavaScriptDescriptors.TYPE_CLASS_DEPENDENCY, new AttributeWithRequireEntryDependencyProcessor(null, false, new String[] {CodeSyncPlugin.FILE}));
		CodeSyncPlugin.getInstance().getCodeSyncTypeCriterionDispatcherProcessor().addProcessor(JavaScriptDescriptors.TYPE_HTML_TEMPLATE_DEPENDENCY, new AttributeWithRequireEntryDependencyProcessor("text!", true, null));
		CodeSyncPlugin.getInstance().getCodeSyncTypeCriterionDispatcherProcessor().addProcessor(JavaScriptDescriptors.TYPE_REQUIRE_CLASS_DEPENDENCY, new RequireEntryDependencyProcessor(null, false, new String[] {CodeSyncPlugin.FILE}));
		CodeSyncPlugin.getInstance().getCodeSyncTypeCriterionDispatcherProcessor().addProcessor(JavaScriptDescriptors.TYPE_REQUIRE_HTML_TEMPLATE_DEPENDENCY, new RequireEntryDependencyProcessor("text!", true, null));
		CodeSyncPlugin.getInstance().getCodeSyncTypeCriterionDispatcherProcessor().addProcessor(JavaScriptDescriptors.TYPE_INHERITANCE, new InheritanceProcessor());
	}

}
