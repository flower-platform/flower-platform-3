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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.codesync.code.javascript.changes_processor.TableViewProcessor;
import org.flowerplatform.codesync.code.javascript.operation_extension.JavaScriptFeatureAccessExtension;
import org.flowerplatform.codesync.code.javascript.processor.JavascriptElementProcessor;
import org.flowerplatform.codesync.operation_extension.AddNewExtension;
import org.flowerplatform.codesync.operation_extension.FeatureAccessExtension;
import org.flowerplatform.codesync.processor.ChildrenUpdaterDiagramProcessor;
import org.flowerplatform.codesync.processor.CodeSyncCategorySeparatorProcessor;
import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;
import org.flowerplatform.codesync.remote.RelationDescriptor;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.common.util.Utils;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.emf_model.notation.View;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;
import org.osgi.framework.BundleContext;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncCodeJavascriptPlugin extends AbstractFlowerJavaPlugin {

	protected static CodeSyncCodeJavascriptPlugin INSTANCE;
	
	public static String TECHNOLOGY = "js";
	
	public static CodeSyncCodeJavascriptPlugin getInstance() {
		return INSTANCE;
	}

	/**
	 * @author Mariana Gheorghe
	 * @author Cristina Constantinescu
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
		
		// descriptors for js code
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new Runnable() {
			@Override
			public void run() {
				// descriptors
				CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
						new CodeSyncElementDescriptor()
						.setCodeSyncType("backboneClass").setLabel("Backbone Class")
						.addCodeSyncTypeCategory("topLevel")
						.addInitializationType("").addInitializationTypeLabel("Plain Backbone Class")
						.addInitializationType("backboneTableView").addInitializationTypeLabel("Table View")
						.addInitializationType("backboneTableItemView").addInitializationTypeLabel("Table Item View")
						.addInitializationType("backboneFormView").addInitializationTypeLabel("Form View")
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
						.setKeyFeature("name")
						.setStandardDiagramControllerProviderFactory("topLevelBoxChild")
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
		
				// extensions
				CodeSyncPlugin.getInstance().getFeatureAccessExtensions().add(new JavaScriptFeatureAccessExtension());
				CodeSyncPlugin.getInstance().getAddNewExtensions().add(new AddNewExtension() {
					
					@Override
					public boolean addNew(CodeSyncElement codeSyncElement, View parent,
							Resource codeSyncMappingResource, Map<String, Object> parameters) {
						String initializationType = (String) Utils.getValueSafe(parameters, CodeSyncPlugin.CONTEXT_INITIALIZATION_TYPE);
						if (!"backboneTableView".equals(initializationType)) {
							return false;
						}
						
//						CodeSyncElement attribute;
//						
//						attribute = CodeSyncOperationsService.getInstance().create("javaScriptAttribute");
//						CodeSyncOperationsService.getInstance().setFeatureValue(attribute, FeatureAccessExtension.CODE_SYNC_NAME, "tableItemViewClass");
//						CodeSyncOperationsService.getInstance().add(codeSyncElement, attribute);		
//
//						attribute = CodeSyncOperationsService.getInstance().create("javaScriptAttribute");
//						CodeSyncOperationsService.getInstance().setFeatureValue(attribute, FeatureAccessExtension.CODE_SYNC_NAME, "test");
//						CodeSyncOperationsService.getInstance().add(codeSyncElement, attribute);		
						
						return false;
					}
				});
				
				// processors
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
		});
	
		// descriptors registered from sripts
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new Runnable() {
			@Override
			public void run() {
				// search for js files and register them
				jsScriptExtensions();
			}
		});		

		CodeSyncPlugin.getInstance().addSrcDir("js");
	}
			
	/**
	 * Loads and executes javascript files from codesync/scripts
	 * 
	 * @author Mircea Negreanu
	 */
	public void jsScriptExtensions() {
		// use rhino as a scripting engine instead of javax.scripting as we want to give
		// the users the possibility to extend existing java classes (and not only implement
		// interfaces)
		Context cx = Context.enter();
		try {
			// we want ImporterTopLevel so we can just write importClass inside the js and 
			// not use a JavaImporter()
			Scriptable scope = new ImporterTopLevel(cx);
			
			URL url = CodeSyncPlugin.getInstance().getBundleContext().getBundle().getResource("scripts");
			File folder = new File(FileLocator.resolve(url).toURI());
			// read each file and evaluate it
			for (File file: folder.listFiles()) {
				cx.evaluateString(scope, FileUtils.readFileToString(file), file.getName(), 0, null);
			}
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException("JS scripts loading error", e);
		} finally {
			Context.exit();
		}
	}
	
}
