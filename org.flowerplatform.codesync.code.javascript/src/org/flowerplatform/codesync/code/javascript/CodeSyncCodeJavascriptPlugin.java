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

import org.flowerplatform.codesync.code.javascript.operation_extension.JavaScriptFeatureAccessExtension;
import org.flowerplatform.codesync.code.javascript.processor.JavascriptElementProcessor;
import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.editor.model.change_processor.DiagramPropertiesChangeProcessor;
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
				.setDefaultName("newOperation")
				.addCodeSyncTypeCategory("backboneClassMember")
				.addFeature("name")
				.setKeyFeature("name"));
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("javaScriptAttribute")
				.setLabel("Attribute")
				.setDefaultName("newAttribute")
				.addCodeSyncTypeCategory("backboneClassMember")
				.addFeature("defaultValue")
				.addFeature("name")
				.setKeyFeature("name"));
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("requireEntry")
				.setLabel("RequireEntry")
				.setDefaultName("newRequireEntry")
				.addCodeSyncTypeCategory("requireEntry")
				.addFeature("varName")
				.addFeature("dependencyPath")
				.setKeyFeature("varName"));
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("eventsAttribute")
				.setLabel("Events")
				.setDefaultName("events")
				.addCodeSyncTypeCategory("backboneClassMember")
				.addChildrenCodeSyncTypeCategory("eventsAttributeEntry")
				.addFeature("name")
				.setKeyFeature("name"));
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("routesAttribute")
				.setLabel("Routes")
				.setDefaultName("routes")
				.addCodeSyncTypeCategory("backboneClassMember")
				.addChildrenCodeSyncTypeCategory("routesAttributeEntry")
				.addFeature("name")
				.setKeyFeature("name"));
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("eventsAttributeEntry")
				.setLabel("Event")
				.setDefaultName("event")
				.addCodeSyncTypeCategory("eventsAttributeEntry")
				.addFeature("event")
				.addFeature("selector")
				.addFeature("function")
				.setKeyFeature("event"));
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("routesAttributeEntry")
				.setLabel("Route")
				.setDefaultName("route")
				.addCodeSyncTypeCategory("routesAttributeEntry")
				.addFeature("path")
				.addFeature("function")
				.setKeyFeature("path"));
		
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("table")
				.setLabel("Table")
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
				.setDefaultName("newHeaderEntry")
				.addCodeSyncTypeCategory("tableHeaderEntry")
				.addFeature("title")
				.setKeyFeature("title"));
		
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("tableItem")
				.setLabel("Table Item")
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
				.setDefaultName("newTableItemEntry")
				.addCodeSyncTypeCategory("tableItemEntry")
				.addFeature("valueExpression")
				.setKeyFeature("valueExpression"));
		
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("form")
				.setLabel("Form")
				.setDefaultName("NewForm")
				.setExtension("html")
				.addChildrenCodeSyncTypeCategory("formItem")
				.addFeature("name")
				.setKeyFeature("name"));
		CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType("formItem")
				.setLabel("Form Item")
				.setDefaultName("newFormItem")
				.addCodeSyncTypeCategory("formItem")
				.addFeature("valueExpression")
				.addFeature("editId")
				.addFeature("title")
				.setKeyFeature("title"));
		
		CodeSyncPlugin.getInstance().getFeatureAccessExtensions().add(new JavaScriptFeatureAccessExtension());
		
		JavascriptElementProcessor processor = new JavascriptElementProcessor();
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.title", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.javaScriptOperation", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.javaScriptAttribute", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.requireEntry", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.eventsAttribute", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.routesAttribute", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.eventsAttribute.eventsAttributeEntry", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.backboneClass.routesAttribute.routesAttributeEntry", processor);
		
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.table.title", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.table.tableHeaderEntry", processor);
		
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.tableItem.title", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.tableItem.tableItemEntry", processor);
		
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.form.title", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.form.formItem", processor);
	}
	
}
