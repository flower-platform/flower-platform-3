package org.flowerplatform.codesync.code.javascript.config;

import static com.crispico.flower.mp.codesync.base.CodeSyncPlugin.TOP_LEVEL;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.codesync.code.javascript.config.changes_processor.AttributeWithRequireEntryDependencyProcessor;
import org.flowerplatform.codesync.code.javascript.config.changes_processor.InheritanceProcessor;
import org.flowerplatform.codesync.code.javascript.config.changes_processor.RequireEntryDependencyProcessor;
import org.flowerplatform.codesync.code.javascript.config.extension.AddNewExtension_BackboneClass;
import org.flowerplatform.codesync.code.javascript.config.extension.AddNewExtension_BackboneCollectionView;
import org.flowerplatform.codesync.code.javascript.config.extension.AddNewExtension_BackboneFormView;
import org.flowerplatform.codesync.code.javascript.config.extension.AddNewExtension_BackboneTableItemView;
import org.flowerplatform.codesync.code.javascript.config.extension.AddNewExtension_BackboneTableView;
import org.flowerplatform.codesync.code.javascript.config.extension.AddNewExtension_BackboneView;
import org.flowerplatform.codesync.code.javascript.config.extension.AddNewRelationExtension_FromWizardAttribute;
import org.flowerplatform.codesync.code.javascript.config.extension.AddNewRelationExtension_FromWizardElement;
import org.flowerplatform.codesync.code.javascript.config.extension.AddNewRelationExtension_JavaScriptAttr;
import org.flowerplatform.codesync.code.javascript.operation_extension.JavaScriptFeatureAccessExtension;
import org.flowerplatform.codesync.config.extension.InplaceEditorExtension_RegExFormat;
import org.flowerplatform.codesync.config.extension.NamedElementFeatureAccessExtension;
import org.flowerplatform.codesync.processor.ChildrenUpdaterDiagramProcessor;
import org.flowerplatform.codesync.processor.CodeSyncCategorySeparatorProcessor;
import org.flowerplatform.codesync.processor.RelationsChangesDiagramProcessor;
import org.flowerplatform.codesync.processor.TopLevelElementChildProcessor;
import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
import org.flowerplatform.codesync.remote.RelationDescriptor;
import org.flowerplatform.codesync.wizard.WizardDependencyDescriptor;
import org.flowerplatform.codesync.wizard.WizardTargetRelationsProcessor;
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
	
	public static final String TYPE_TABLE = "table";
	public static final String TYPE_FORM = "form";
	
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
	public static final String INIT_TYPE_BACKBONE_COLLECTION_VIEW = "backboneCollectionView";

	public static final String DEPENDENCY_SAMPLE_OBJ_GEN = "sampleObjGen";
	public static final String DEPENDENCY_COLLECTION = "collection";
	public static final String DEPENDENCY_FORM_TEMPLATE = "formTemplate";
	public static final String DEPENDENCY_FORM_VIEW = "formView";
	public static final String DEPENDENCY_TABLE_TEMPLATE = "tableTemplate";
	public static final String DEPENDENCY_TABLE_VIEW = "tableView";
	public static final String DEPENDENCY_TABLE_ITEM_TEMPLATE = "tableItemTemplate";
	public static final String DEPENDENCY_TABLE_ITEM_VIEW = "tableItemView";
	
	public static final String DEPENDENCY_ATTR_SAMPLE_OBJ_GEN = "attrSampleObjGen";
	public static final String DEPENDENCY_ATTR_TABLE_ITEM_TEMPLATE = "attrTableItemTemplate";
	public static final String DEPENDENCY_ATTR_FORM_TEMPLATE = "attrFormTemplate";
	
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
				.addCodeSyncTypeCategory(TOP_LEVEL)
				.addInitializationType(INIT_TYPE_NONE).addInitializationTypeLabel("Plain Backbone Class").addInitializationTypesOrderIndexes(110)
				.addInitializationType(INIT_TYPE_BACKBONE_VIEW).addInitializationTypeLabel("Plain View").addInitializationTypesOrderIndexes(120)
				.addInitializationType(INIT_TYPE_BACKBONE_TABLE_VIEW).addInitializationTypeLabel("Table View").addInitializationTypesOrderIndexes(130)
				.addInitializationType(INIT_TYPE_BACKBONE_TABLE_ITEM_VIEW).addInitializationTypeLabel("Table Item View").addInitializationTypesOrderIndexes(140)
				.addInitializationType(INIT_TYPE_BACKBONE_FORM_VIEW).addInitializationTypeLabel("Form View").addInitializationTypesOrderIndexes(150)
				.addInitializationType(INIT_TYPE_BACKBONE_COLLECTION_VIEW).addInitializationTypeLabel("Collection View").addInitializationTypesOrderIndexes(160)
				.setIconUrl("images/full/obj16/jcu_obj.gif")
				.setDefaultName("NewBackboneClass")
				.setExtension("js")
				.addChildrenCodeSyncTypeCategory("backboneClassMember")
				.addChildrenCodeSyncTypeCategory(TYPE_REQUIRE_ENTRY)
				.addFeature(FEATURE_NAME)
				.addFeature(FEATURE_SUPER_CLASS)
				.setKeyFeature(FEATURE_NAME)
				.setStandardDiagramControllerProviderFactory("topLevelBox")
				.setOrderIndex(10)
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
				.setOrderIndex(130)
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
				.setOrderIndex(120)
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
				.setOrderIndex(110)
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
				.setOrderIndex(140)
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
				.setOrderIndex(150)
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
				.setOrderIndex(160)
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
				.setOrderIndex(170)
		);
		
		/////////////////////////////////////////
		// TOP LEVEL
		/////////////////////////////////////////
		descriptors.add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType(TYPE_JAVASCRIPT_FILE)
//				.addCodeSyncTypeCategory(TOP_LEVEL)
				.setLabel("JavaScript File")
				.setIconUrl("images/full/obj16/jcu_obj.gif")
				.setDefaultName("NewJavaScriptFile")
				.setExtension(".js")
				.addChildrenCodeSyncTypeCategory("backboneClassMember")
				.addFeature(FEATURE_NAME)
				.setKeyFeature(FEATURE_NAME)
				.setStandardDiagramControllerProviderFactory("topLevelBox")				
		);
			
		/////////////////////////////////////////
		// TOP LEVEL
		/////////////////////////////////////////
		descriptors.add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType(TYPE_TABLE)
				.addCodeSyncTypeCategory(TOP_LEVEL)
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
				.setOrderIndex(20)
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
				.setOrderIndex(210)
		);
		
		/////////////////////////////////////////
		// TOP LEVEL
		/////////////////////////////////////////
		descriptors.add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("tableItem")
				.addCodeSyncTypeCategory(TOP_LEVEL)
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
				.setOrderIndex(30)
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
				.setOrderIndex(310)
		);
		
		/////////////////////////////////////////
		// TOP LEVEL
		/////////////////////////////////////////
		descriptors.add(
				new CodeSyncElementDescriptor()
				.addCodeSyncTypeCategory(TOP_LEVEL)
				.setCodeSyncType(TYPE_FORM)
				.addCodeSyncTypeCategory("htmlTemplate")
				.setLabel("Form HTML Template")
				.setIconUrl("images/full/obj16/application_form.png")
				.setDefaultName("NewFormTemplate")
				.setExtension("html")
				.addChildrenCodeSyncTypeCategory("formItem")
				.addFeature(FEATURE_NAME)
				.addFeature("idSuffix")
				.setKeyFeature(FEATURE_NAME)
				.setStandardDiagramControllerProviderFactory("topLevelBox")
				.setOrderIndex(40)
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
				.addFeature("title")
				.setKeyFeature("title")
				.setStandardDiagramControllerProviderFactory("topLevelBoxChild")
				.setOrderIndex(410)
		);
		
		/////////////////////////////////////////
		// TOP LEVEL
		/////////////////////////////////////////
		descriptors.add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType(TYPE_HTML_FILE)
				.setLabel("HTML File")
				.addFeature(FEATURE_NAME)
				.setKeyFeature(FEATURE_NAME));
		
		/////////////////////////////////////////
		// TOP LEVEL
		/////////////////////////////////////////
		descriptors.add(
				new CodeSyncElementDescriptor()
				.addCodeSyncTypeCategory(TOP_LEVEL).addCodeSyncTypeCategory("dontNeedLocation")
				.setCodeSyncType("note")
				.setLabel("Note")
				.setIconUrl("images/full/obj16/note.png")
				.setKeyFeature("text")
				.setDefaultName("NewNote")				
				.setCreateCodeSyncElement(false)	
				.setOrderIndex(99)
		);
				
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().addAll(descriptors);
		
		CodeSyncPlugin.getInstance().getRelationDescriptors().add(
				new RelationDescriptor()
				.setType(TYPE_HTML_TEMPLATE_DEPENDENCY)
				.setLabel("HTML Template Dependency")
				.setTargetEndFigureType(RelationDescriptor.OPEN_ARROW)
				.addSourceCodeSyncType(TYPE_JAVASCRIPT_ATTRIBUTE)
				.addTargetCodeSyncTypeCategory("htmlTemplate")				
		);
			
		CodeSyncPlugin.getInstance().getRelationDescriptors().add(
				new RelationDescriptor()
				.setType(TYPE_CLASS_DEPENDENCY)
				.setLabel("Class Dependency")
				.setTargetEndFigureType(RelationDescriptor.OPEN_ARROW)
				.addSourceCodeSyncType(TYPE_JAVASCRIPT_ATTRIBUTE)
				.addTargetCodeSyncType(TYPE_BACKBONE_CLASS)				
		);
		CodeSyncPlugin.getInstance().getRelationDescriptors().add(
				new RelationDescriptor()
				.setType(TYPE_REQUIRE_CLASS_DEPENDENCY)
				.setLabel("Require Class")
				.setTargetEndFigureType(RelationDescriptor.OPEN_ARROW)
				.addSourceCodeSyncType(TYPE_REQUIRE_ENTRY)
				.addTargetCodeSyncType(TYPE_BACKBONE_CLASS)
		);
		CodeSyncPlugin.getInstance().getRelationDescriptors().add(
				new RelationDescriptor()
				.setType(TYPE_REQUIRE_HTML_TEMPLATE_DEPENDENCY)
				.setLabel("Require HTML Template")
				.setTargetEndFigureType(RelationDescriptor.OPEN_ARROW)
				.addSourceCodeSyncType(TYPE_REQUIRE_ENTRY)
				.addTargetCodeSyncTypeCategory("htmlTemplate")
		);
		CodeSyncPlugin.getInstance().getRelationDescriptors().add(
				new RelationDescriptor()
				.setType(TYPE_INHERITANCE)
				.setLabel("Inherits")
				.setTargetEndFigureType(RelationDescriptor.CLOSED_ARROW)
				.addSourceCodeSyncType(TYPE_BACKBONE_CLASS)
				.addTargetCodeSyncType(TYPE_BACKBONE_CLASS)
		);
			
		CodeSyncPlugin.getInstance().getRelationDescriptors().add(
				new WizardDependencyDescriptor()			
				.setTargetCodeSyncElementLocation("js/models")
				.setNewCodeSyncElementKeyFeatureFormat("%sSampleObject")
				.setType(DEPENDENCY_SAMPLE_OBJ_GEN)
				.setLabel("Sample Object Generator")
				.setTargetEndFigureType(RelationDescriptor.DIAMOND)
				.addSourceCodeSyncType(CodeSyncPlugin.WIZARD_ELEMENT)
				.addTargetCodeSyncType(TYPE_BACKBONE_CLASS)
				.setAcceptTargetNullIfNoCodeSyncTypeDetected(true)
		);
		
		CodeSyncPlugin.getInstance().getRelationDescriptors().add(
				new WizardDependencyDescriptor()				
				.setTargetCodeSyncElementLocation("js/models")
				.setNewCodeSyncElementKeyFeatureFormat("%sCollectionView")
				.setType(DEPENDENCY_COLLECTION)
				.setLabel("Collection View")
				.setTargetEndFigureType(RelationDescriptor.DIAMOND)
				.addSourceCodeSyncType(CodeSyncPlugin.WIZARD_ELEMENT)
				.addTargetCodeSyncType(TYPE_BACKBONE_CLASS)
				.setAcceptTargetNullIfNoCodeSyncTypeDetected(true)
		);
						
		CodeSyncPlugin.getInstance().getRelationDescriptors().add(
				new WizardDependencyDescriptor()	
				.setTargetCodeSyncElementLocation("js/tpl")
				.setNewCodeSyncElementKeyFeatureFormat("%sFormTemplate")
				.setType(DEPENDENCY_FORM_TEMPLATE)
				.setLabel("Form Template")
				.setTargetEndFigureType(RelationDescriptor.DIAMOND)
				.addSourceCodeSyncType(CodeSyncPlugin.WIZARD_ELEMENT)
				.addTargetCodeSyncType("form")
				.setAcceptTargetNullIfNoCodeSyncTypeDetected(true)
		);
				
		CodeSyncPlugin.getInstance().getRelationDescriptors().add(
				new WizardDependencyDescriptor()	
				.setTargetCodeSyncElementLocation("js/view")
				.setNewCodeSyncElementKeyFeatureFormat("%sFormView")
				.setType(DEPENDENCY_FORM_VIEW)
				.setLabel("Form View")
				.setTargetEndFigureType(RelationDescriptor.DIAMOND)
				.addSourceCodeSyncType(CodeSyncPlugin.WIZARD_ELEMENT)
				.addTargetCodeSyncType(TYPE_BACKBONE_CLASS)
				.setAcceptTargetNullIfNoCodeSyncTypeDetected(true)
		);
		
		CodeSyncPlugin.getInstance().getRelationDescriptors().add(
				new WizardDependencyDescriptor()		
				.setTargetCodeSyncElementLocation("js/tpl")
				.setNewCodeSyncElementKeyFeatureFormat("%sTableTemplate")
				.setType(DEPENDENCY_TABLE_TEMPLATE)
				.setLabel("Table Template")
				.setTargetEndFigureType(RelationDescriptor.DIAMOND)
				.addSourceCodeSyncType(CodeSyncPlugin.WIZARD_ELEMENT)
				.addTargetCodeSyncType("table")
				.setAcceptTargetNullIfNoCodeSyncTypeDetected(true)
		);
			
		CodeSyncPlugin.getInstance().getRelationDescriptors().add(
				new WizardDependencyDescriptor()	
				.setTargetCodeSyncElementLocation("js/view")
				.setNewCodeSyncElementKeyFeatureFormat("%sTableView")
				.setType(DEPENDENCY_TABLE_VIEW)
				.setLabel("Table View")
				.setTargetEndFigureType(RelationDescriptor.DIAMOND)
				.addSourceCodeSyncType(CodeSyncPlugin.WIZARD_ELEMENT)
				.addTargetCodeSyncType(TYPE_BACKBONE_CLASS)
				.setAcceptTargetNullIfNoCodeSyncTypeDetected(true)
		);
		
		CodeSyncPlugin.getInstance().getRelationDescriptors().add(
				new WizardDependencyDescriptor()		
				.setTargetCodeSyncElementLocation("js/tpl")
				.setNewCodeSyncElementKeyFeatureFormat("%sTableItemTemplate")
				.setType(DEPENDENCY_TABLE_ITEM_TEMPLATE)
				.setLabel("Table Item Template")
				.setTargetEndFigureType(RelationDescriptor.DIAMOND)
				.addSourceCodeSyncType(CodeSyncPlugin.WIZARD_ELEMENT)
				.addTargetCodeSyncType("tableItem")
				.setAcceptTargetNullIfNoCodeSyncTypeDetected(true)
		);
		
		CodeSyncPlugin.getInstance().getRelationDescriptors().add(
				new WizardDependencyDescriptor()	
				.setTargetCodeSyncElementLocation("js/view")
				.setNewCodeSyncElementKeyFeatureFormat("%sTableItemView")
				.setType(DEPENDENCY_TABLE_ITEM_VIEW)
				.setLabel("Table Item View")
				.setTargetEndFigureType(RelationDescriptor.DIAMOND)
				.addSourceCodeSyncType(CodeSyncPlugin.WIZARD_ELEMENT)
				.addTargetCodeSyncType(TYPE_BACKBONE_CLASS)
				.setAcceptTargetNullIfNoCodeSyncTypeDetected(true)
		);
						
		CodeSyncPlugin.getInstance().getRelationDescriptors().add(
				new WizardDependencyDescriptor()				
				.addRequiredWizardDependencyType(DEPENDENCY_SAMPLE_OBJ_GEN)
				.setType(DEPENDENCY_ATTR_SAMPLE_OBJ_GEN)
				.setLabel("Attribute in Sample Object Generator")
				.setTargetEndFigureType(RelationDescriptor.DIAMOND)
				.addSourceCodeSyncType(CodeSyncPlugin.WIZARD_ATTRIBUTE)
				.addTargetCodeSyncType(TYPE_JAVASCRIPT_ATTRIBUTE)
				.setAcceptTargetNullIfNoCodeSyncTypeDetected(true)
		);

		CodeSyncPlugin.getInstance().getRelationDescriptors().add(
				new WizardDependencyDescriptor()				
				.addRequiredWizardDependencyType(DEPENDENCY_TABLE_ITEM_TEMPLATE)
				.setType(DEPENDENCY_ATTR_TABLE_ITEM_TEMPLATE)
				.setLabel("Item Entry in Table Item Template")
				.setTargetEndFigureType(RelationDescriptor.DIAMOND)
				.addSourceCodeSyncType(CodeSyncPlugin.WIZARD_ATTRIBUTE)
				.addTargetCodeSyncType("tableItemEntry")
				.setAcceptTargetNullIfNoCodeSyncTypeDetected(true)
		);
		
		CodeSyncPlugin.getInstance().getRelationDescriptors().add(
				new WizardDependencyDescriptor()				
				.addRequiredWizardDependencyType(DEPENDENCY_FORM_TEMPLATE)
				.setType(DEPENDENCY_ATTR_FORM_TEMPLATE)
				.setLabel("Item Entry in Form Template")
				.setTargetEndFigureType(RelationDescriptor.DIAMOND)
				.addSourceCodeSyncType(CodeSyncPlugin.WIZARD_ATTRIBUTE)
				.addTargetCodeSyncType("formItem")
				.setAcceptTargetNullIfNoCodeSyncTypeDetected(true)
		);
		
		// extensions
		CodeSyncPlugin.getInstance().getFeatureAccessExtensions().add(new JavaScriptFeatureAccessExtension(descriptors));
		CodeSyncPlugin.getInstance().getAddNewExtensions().add(new AddNewExtension_BackboneClass());
		CodeSyncPlugin.getInstance().getAddNewExtensions().add(new AddNewExtension_BackboneView());
		CodeSyncPlugin.getInstance().getAddNewExtensions().add(new AddNewExtension_BackboneTableView());
		CodeSyncPlugin.getInstance().getAddNewExtensions().add(new AddNewExtension_BackboneTableItemView());
		CodeSyncPlugin.getInstance().getAddNewExtensions().add(new AddNewExtension_BackboneFormView());
		CodeSyncPlugin.getInstance().getAddNewExtensions().add(new AddNewExtension_BackboneCollectionView());
		
		CodeSyncPlugin.getInstance().getAddNewRelationExtensions().add(new AddNewRelationExtension_FromWizardElement(DEPENDENCY_SAMPLE_OBJ_GEN));
		CodeSyncPlugin.getInstance().getAddNewRelationExtensions().add(new AddNewRelationExtension_FromWizardElement(DEPENDENCY_COLLECTION, INIT_TYPE_BACKBONE_COLLECTION_VIEW));
		CodeSyncPlugin.getInstance().getAddNewRelationExtensions().add(new AddNewRelationExtension_FromWizardElement(DEPENDENCY_FORM_TEMPLATE));
		CodeSyncPlugin.getInstance().getAddNewRelationExtensions().add(new AddNewRelationExtension_FromWizardElement(DEPENDENCY_FORM_VIEW, INIT_TYPE_BACKBONE_FORM_VIEW));
		CodeSyncPlugin.getInstance().getAddNewRelationExtensions().add(new AddNewRelationExtension_FromWizardElement(DEPENDENCY_TABLE_ITEM_TEMPLATE));
		CodeSyncPlugin.getInstance().getAddNewRelationExtensions().add(new AddNewRelationExtension_FromWizardElement(DEPENDENCY_TABLE_ITEM_VIEW, INIT_TYPE_BACKBONE_TABLE_ITEM_VIEW));
		CodeSyncPlugin.getInstance().getAddNewRelationExtensions().add(new AddNewRelationExtension_FromWizardElement(DEPENDENCY_TABLE_TEMPLATE));
		CodeSyncPlugin.getInstance().getAddNewRelationExtensions().add(new AddNewRelationExtension_FromWizardElement(DEPENDENCY_TABLE_VIEW, INIT_TYPE_BACKBONE_TABLE_VIEW));
		
		CodeSyncPlugin.getInstance().getAddNewRelationExtensions().add(new AddNewRelationExtension_JavaScriptAttr(DEPENDENCY_ATTR_SAMPLE_OBJ_GEN));
		CodeSyncPlugin.getInstance().getAddNewRelationExtensions().add(new AddNewRelationExtension_FromWizardAttribute(DEPENDENCY_ATTR_TABLE_ITEM_TEMPLATE));
		CodeSyncPlugin.getInstance().getAddNewRelationExtensions().add(new AddNewRelationExtension_FromWizardAttribute(DEPENDENCY_ATTR_FORM_TEMPLATE));
		
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
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.form", parentElementProcessor);
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
		
		RelationsChangesDiagramProcessor relationsChangesProcessor = new RelationsChangesDiagramProcessor();
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass", relationsChangesProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.requireEntry", relationsChangesProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.eventsAttribute", relationsChangesProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.routesAttribute", relationsChangesProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.eventsAttribute.eventsAttributeEntry", relationsChangesProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.routesAttribute.routesAttributeEntry", relationsChangesProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.javaScriptOperation", relationsChangesProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.javaScriptAttribute", relationsChangesProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.javaScriptFile", relationsChangesProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.table", relationsChangesProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.tableItem", relationsChangesProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.table.tableHeaderEntry", relationsChangesProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.tableItem.tableItemEntry", relationsChangesProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.form", relationsChangesProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.formItem", relationsChangesProcessor);
		
		CodeSyncPlugin.getInstance().getCodeSyncTypeCriterionDispatcherProcessor().addProcessor(TYPE_CLASS_DEPENDENCY, new AttributeWithRequireEntryDependencyProcessor(null, false, new String[] {CodeSyncPlugin.FILE}));
		CodeSyncPlugin.getInstance().getCodeSyncTypeCriterionDispatcherProcessor().addProcessor(TYPE_HTML_TEMPLATE_DEPENDENCY, new AttributeWithRequireEntryDependencyProcessor("text!", true, null));
		CodeSyncPlugin.getInstance().getCodeSyncTypeCriterionDispatcherProcessor().addProcessor(TYPE_REQUIRE_CLASS_DEPENDENCY, new RequireEntryDependencyProcessor(null, false, new String[] {CodeSyncPlugin.FILE}));
		CodeSyncPlugin.getInstance().getCodeSyncTypeCriterionDispatcherProcessor().addProcessor(TYPE_REQUIRE_HTML_TEMPLATE_DEPENDENCY, new RequireEntryDependencyProcessor("text!", true, null));
		CodeSyncPlugin.getInstance().getCodeSyncTypeCriterionDispatcherProcessor().addProcessor(TYPE_INHERITANCE, new InheritanceProcessor(null, false, new String[] {CodeSyncPlugin.FILE}));
		
		CodeSyncPlugin.getInstance().getCodeSyncTypeCriterionDispatcherProcessor().addProcessor(TYPE_BACKBONE_CLASS, new WizardTargetRelationsProcessor (DEPENDENCY_COLLECTION, DEPENDENCY_SAMPLE_OBJ_GEN, "sampleObjectGenerator"));
		CodeSyncPlugin.getInstance().getCodeSyncTypeCriterionDispatcherProcessor().addProcessor(TYPE_BACKBONE_CLASS, new WizardTargetRelationsProcessor (DEPENDENCY_TABLE_VIEW, DEPENDENCY_TABLE_ITEM_VIEW, "tableItemViewClass"));
		CodeSyncPlugin.getInstance().getCodeSyncTypeCriterionDispatcherProcessor().addProcessor(TYPE_BACKBONE_CLASS, new WizardTargetRelationsProcessor (DEPENDENCY_TABLE_VIEW, DEPENDENCY_TABLE_TEMPLATE, "tpl"));
		CodeSyncPlugin.getInstance().getCodeSyncTypeCriterionDispatcherProcessor().addProcessor(TYPE_BACKBONE_CLASS, new WizardTargetRelationsProcessor (DEPENDENCY_FORM_VIEW, DEPENDENCY_FORM_TEMPLATE, "tpl"));
		CodeSyncPlugin.getInstance().getCodeSyncTypeCriterionDispatcherProcessor().addProcessor(TYPE_BACKBONE_CLASS, new WizardTargetRelationsProcessor (DEPENDENCY_TABLE_ITEM_VIEW, DEPENDENCY_TABLE_ITEM_TEMPLATE, "tpl"));
			
		CodeSyncPlugin.getInstance().getCodeSyncTypeCriterionDispatcherProcessor().addProcessor("table", new WizardTargetRelationsProcessor (DEPENDENCY_TABLE_VIEW, DEPENDENCY_TABLE_TEMPLATE, "tpl"));
		CodeSyncPlugin.getInstance().getCodeSyncTypeCriterionDispatcherProcessor().addProcessor("form", new WizardTargetRelationsProcessor (DEPENDENCY_FORM_VIEW, DEPENDENCY_FORM_TEMPLATE, "tpl"));
		CodeSyncPlugin.getInstance().getCodeSyncTypeCriterionDispatcherProcessor().addProcessor("tableItem", new WizardTargetRelationsProcessor (DEPENDENCY_TABLE_ITEM_VIEW, DEPENDENCY_TABLE_ITEM_TEMPLATE, "tpl"));
		
	}

}
