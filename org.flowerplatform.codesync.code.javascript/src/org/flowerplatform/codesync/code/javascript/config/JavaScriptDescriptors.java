package org.flowerplatform.codesync.code.javascript.config;

import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
import org.flowerplatform.codesync.remote.RelationDescriptor;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;

public class JavaScriptDescriptors implements Runnable {

	public static final String TYPE_BACKBONE_CLASS = "backboneClass";
	public static final String TYPE_JAVASCRIPT_ATTRIBUTE = "javaScriptAttribute";
	public static final String TYPE_REQUIRE_ENTRY = "requireEntry";
	public static final String TYPE_CLASS_DEPENDENCY = "classDependency";
	public static final String TYPE_HTML_TEMPLATE_DEPENDENCY = "htmlTemplateDependency";
	public static final String TYPE_REQUIRE_CLASS_DEPENDENCY = "requireClassDependency";
	public static final String TYPE_REQUIRE_HTML_TEMPLATE_DEPENDENCY = "requireHtmlTemplateDependency";
	
	public static final String FEATURE_DEPENDENCY_PATH = "dependencyPath";
	public static final String FEATURE_VAR_NAME = "varName";
	public static final String FEATURE_DEFAULT_VALUE = "defaultValue";

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
		// descriptors
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
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
				.addFeature("superClass")
				.addFeature("name")
				.setKeyFeature("name")
				.setStandardDiagramControllerProviderFactory("topLevelBox")
		);
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("javaScriptOperation")
				.setLabel("Operation")
				.setIconUrl("images/full/obj16/methpub_obj.gif")
				.setDefaultName("newOperation")
				.addCodeSyncTypeCategory("backboneClassMember")
				.setCategory("operations")
				.setNextSiblingSeparator(", ")
				.addFeature("name")
				.addFeature("parameters")
				.setKeyFeature("name")
				.setStandardDiagramControllerProviderFactory("topLevelBoxChild")
		);
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType(TYPE_JAVASCRIPT_ATTRIBUTE)
				.setLabel("Attribute")
				.setIconUrl("images/full/obj16/field_public_obj.gif")
				.setDefaultName("newAttribute")
				.addCodeSyncTypeCategory("backboneClassMember")
				.setCategory("attributes")
				.setNextSiblingSeparator(", ")
				.addFeature(FEATURE_DEFAULT_VALUE)
				.addFeature("name")
				.setKeyFeature("name")
				.setStandardDiagramControllerProviderFactory("topLevelBoxChild")
		);
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
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
		);
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("eventsAttribute")
				.setLabel("Events")
				.setIconUrl("images/full/obj16/time_go.png")
				.setDefaultName("events")
				.addCodeSyncTypeCategory("backboneClassMember")
				.addChildrenCodeSyncTypeCategory("eventsAttributeEntry")
				.setCategory("attributes")
				.setNextSiblingSeparator(", ")
				.addFeature("name")
				.setKeyFeature("name")
				.setStandardDiagramControllerProviderFactory("topLevelBoxChild")
		);
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("routesAttribute")
				.setLabel("Routes")
				.setIconUrl("images/full/obj16/arrow_right.png")
				.setDefaultName("routes")
				.addCodeSyncTypeCategory("backboneClassMember")
				.addChildrenCodeSyncTypeCategory("routesAttributeEntry")
				.setCategory("attributes")
				.setNextSiblingSeparator(", ")
				.addFeature("name")
				.setKeyFeature("name")
				.setStandardDiagramControllerProviderFactory("topLevelBoxChild")
		);
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
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
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
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
		
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("table")
				.addCodeSyncTypeCategory("topLevel")
				.addCodeSyncTypeCategory("htmlTemplate")
				.setLabel("Table")
				.setIconUrl("images/full/obj16/table.png")
				.setDefaultName("NewTable")
				.setExtension("html")
				.addChildrenCodeSyncTypeCategory("tableHeaderEntry")
				.addFeature("tableId")
				.addFeature("headerRowId")
				.addFeature("name")
				.setKeyFeature("name")
				.setStandardDiagramControllerProviderFactory("topLevelBox")
		);

		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
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
		
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("tableItem")
				.addCodeSyncTypeCategory("topLevel")
				.addCodeSyncTypeCategory("htmlTemplate")
				.setLabel("Table Item")
				.setIconUrl("images/full/obj16/table_select_row.png")
				.setDefaultName("NewTableItem")
				.setExtension("html")
				.addChildrenCodeSyncTypeCategory("tableItemEntry")
				.addFeature("itemUrl")
				.addFeature("name")
				.setKeyFeature("name")
				.setStandardDiagramControllerProviderFactory("topLevelBox")
		);
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
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
		
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.addCodeSyncTypeCategory("topLevel")
				.setCodeSyncType("form")
				.addCodeSyncTypeCategory("htmlTemplate")
				.setLabel("Form")
				.setIconUrl("images/full/obj16/application_form.png")
				.setDefaultName("NewForm")
				.setExtension("html")
				.addChildrenCodeSyncTypeCategory("formItem")
				.addFeature("name")
				.setKeyFeature("name")
				.setStandardDiagramControllerProviderFactory("topLevelBox")
		);
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
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
		
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.addCodeSyncTypeCategory("topLevel").addCodeSyncTypeCategory("dontNeedLocation")
				.setCodeSyncType("note")
				.setLabel("Note")
				.setIconUrl("images/full/obj16/note.png")
				.setDefaultName("NewNote")				
				.setCreateCodeSyncElement(false)				
		);
		
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
	}

}
