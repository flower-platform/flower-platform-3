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
import org.flowerplatform.codesync.processor.CodeSyncCategorySeparatorProcessor;
import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
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
				.setLabel("Backbone Class")
				.setIconUrl("images/full/obj16/jcu_obj.gif")
				.setDefaultName("NewBackboneClass")
				.setExtension("js")
				.addChildrenCodeSyncTypeCategory("backboneClassMember")
				.addChildrenCodeSyncTypeCategory("requireEntry")
				.addFeature("superClass")
				.addFeature("name")
				.setKeyFeature("name"));
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
				.setKeyFeature("name"));
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
				.setKeyFeature("name"));
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("requireEntry")
				.setLabel("RequireEntry")
				.setIconUrl("images/full/obj16/imp_obj.gif")
				.setDefaultName("newRequireEntry")
				.addCodeSyncTypeCategory("requireEntry")
				.setCategory("require")
				.addFeature("varName")
				.addFeature("dependencyPath")
				.setKeyFeature("varName"));
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
				.setKeyFeature("name"));
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
				.setKeyFeature("name"));
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
				.setKeyFeature("event"));
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
				.setKeyFeature("path"));
		
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("table")
				.setLabel("Table")
				.setIconUrl("images/full/obj16/table.png")
				.setDefaultName("NewTable")
				.setExtension("html")
				.addChildrenCodeSyncTypeCategory("tableHeaderEntry")
				.addFeature("tableId")
				.addFeature("headerRowId")
				.addFeature("name")
				.setKeyFeature("name"));
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("tableHeaderEntry")
				.setLabel("Table Header Entry")
				.setIconUrl("images/full/obj16/table_select_column.png")
				.setDefaultName("newHeaderEntry")
				.addCodeSyncTypeCategory("tableHeaderEntry")
				.addFeature("title")
				.setKeyFeature("title"));
		
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("tableItem")
				.setLabel("Table Item")
				.setIconUrl("images/full/obj16/table_select_row.png")
				.setDefaultName("NewTableItem")
				.setExtension("html")
				.addChildrenCodeSyncTypeCategory("tableItemEntry")
				.addFeature("itemUrl")
				.addFeature("name")
				.setKeyFeature("name"));
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("tableItemEntry")
				.setLabel("Table Item Entry")
				.setIconUrl("images/full/obj16/table_select.png")
				.setDefaultName("newTableItemEntry")
				.addCodeSyncTypeCategory("tableItemEntry")
				.addFeature("valueExpression")
				.setKeyFeature("valueExpression"));
		
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("form")
				.setLabel("Form")
				.setIconUrl("images/full/obj16/application_form.png")
				.setDefaultName("NewForm")
				.setExtension("html")
				.addChildrenCodeSyncTypeCategory("formItem")
				.addFeature("name")
				.setKeyFeature("name"));
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("formItem")
				.setLabel("Form Item")
				.setIconUrl("images/full/obj16/bullet_textfield.png")
				.setDefaultName("newFormItem")
				.addCodeSyncTypeCategory("formItem")
				.addFeature("valueExpression")
				.addFeature("editId")
				.addFeature("title")
				.setKeyFeature("title"));
		
		CodeSyncPlugin.getInstance().getFeatureAccessExtensions().add(new JavaScriptFeatureAccessExtension());
		
		JavascriptElementProcessor processor = new JavascriptElementProcessor();
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.javaScriptOperation", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.javaScriptAttribute", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.requireEntry", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.eventsAttribute", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.routesAttribute", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.eventsAttribute.eventsAttributeEntry", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.routesAttribute.routesAttributeEntry", processor);
		
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.table.tableHeaderEntry", processor);
		
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.tableItem.tableItemEntry", processor);
		
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.form.formItem", processor);
		
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("title", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("categorySeparator", new CodeSyncCategorySeparatorProcessor());
		
		CodeSyncPlugin.getInstance().getCodeSyncTypeCriterionDispatcherProcessor().addProcessor("backboneClass", new TableViewProcessor());
	}
	
}
