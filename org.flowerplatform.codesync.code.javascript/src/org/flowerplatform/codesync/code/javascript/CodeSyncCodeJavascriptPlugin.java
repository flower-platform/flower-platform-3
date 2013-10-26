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
package org.flowerplatform.codesync.code.javascript;

import org.flowerplatform.codesync.code.javascript.changes_processor.TableViewProcessor;
import org.flowerplatform.codesync.code.javascript.operation_extension.JavaScriptFeatureAccessExtension;
import org.flowerplatform.codesync.code.javascript.processor.JavascriptElementProcessor;
import org.flowerplatform.codesync.processor.ChildrenUpdaterDiagramProcessor;
import org.flowerplatform.codesync.processor.CodeSyncCategorySeparatorProcessor;
import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
import org.flowerplatform.codesync.remote.RelationDescriptor;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.osgi.framework.BundleContext;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncCodeJavascriptPlugin extends AbstractFlowerJavaPlugin {

	protected static CodeSyncCodeJavascriptPlugin INSTANCE;
	
	public static String TECHNOLOGY = "js";
	
	public static CodeSyncCodeJavascriptPlugin getInstance() {
		return INSTANCE;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
		
		CodeSyncPlugin.getInstance().addSrcDir("js");
		
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("backboneClass")
				.addCodeSyncTypeCategory("topLevel")
				.setLabel("Backbone Class")
				.setIconUrl("images/full/obj16/jcu_obj.gif")
				.setDefaultName("NewBackboneClass")
				.setExtension("js")
				.addChildrenCodeSyncTypeCategory("backboneClassMember")
				.addChildrenCodeSyncTypeCategory("requireEntry")
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
				.setCodeSyncType("javaScriptAttribute")
				.setLabel("Attribute")
				.setIconUrl("images/full/obj16/field_public_obj.gif")
				.setDefaultName("newAttribute")
				.addCodeSyncTypeCategory("backboneClassMember")
				.setCategory("attributes")
				.setNextSiblingSeparator(", ")
				.addFeature("defaultValue")
				.addFeature("name")
				.setKeyFeature("name").setStandardDiagramControllerProviderFactory("topLevelBoxChild")
		);
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("requireEntry")
				.setLabel("RequireEntry")
				.setIconUrl("images/full/obj16/imp_obj.gif")
				.setDefaultName("newRequireEntry")
				.addCodeSyncTypeCategory("requireEntry")
				.setCategory("require entries")
				.addFeature("varName")
				.addFeature("dependencyPath")
				.setKeyFeature("varName")
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
		
		CodeSyncPlugin.getInstance().getRelationDescriptors().add(
				new RelationDescriptor()
				.setType("templateDependency")
				.setLabel("Template Dependency")
				.addSourceCodeSyncType("javaScriptAttribute")
				.addTargetCodeSyncTypeCategory("htmlTemplate")
		);

		
		CodeSyncPlugin.getInstance().getRelationDescriptors().add(
				new RelationDescriptor()
				.setType("classDependency")
				.setLabel("Class Dependency")
				.addSourceCodeSyncType("javaScriptAttribute")
				.addTargetCodeSyncType("backboneClass")
		);

		CodeSyncPlugin.getInstance().getFeatureAccessExtensions().add(new JavaScriptFeatureAccessExtension());
		
		ChildrenUpdaterDiagramProcessor parentElementProcessor = new ChildrenUpdaterDiagramProcessor();
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass", parentElementProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.table", parentElementProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.tableItem", parentElementProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.formItem", parentElementProcessor);
		
		JavascriptElementProcessor childElementProcessor = new JavascriptElementProcessor();
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("topLevelBoxTitle", childElementProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.javaScriptOperation", childElementProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.javaScriptAttribute", childElementProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.requireEntry", childElementProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.eventsAttribute", childElementProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.routesAttribute", childElementProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.eventsAttribute.eventsAttributeEntry", childElementProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.routesAttribute.routesAttributeEntry", childElementProcessor);
		
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.table.tableHeaderEntry", childElementProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.tableItem.tableItemEntry", childElementProcessor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.form.formItem", childElementProcessor);
		
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("categorySeparator", new CodeSyncCategorySeparatorProcessor());
		
		CodeSyncPlugin.getInstance().getCodeSyncTypeCriterionDispatcherProcessor().addProcessor("backboneClass", new TableViewProcessor());
	}
	
}
