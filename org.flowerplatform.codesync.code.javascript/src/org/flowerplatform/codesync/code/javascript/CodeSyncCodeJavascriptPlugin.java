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

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.FileLocator;
import org.flowerplatform.codesync.code.javascript.config.JavaScriptDescriptors;
import org.flowerplatform.codesync.code.javascript.config.changes_processor.AbstractDependencyProcessor;
import org.flowerplatform.codesync.code.javascript.config.changes_processor.AttributeWithRequireEntryDependencyProcessor;
import org.flowerplatform.codesync.code.javascript.config.changes_processor.InheritanceProcessor;
import org.flowerplatform.codesync.code.javascript.config.changes_processor.RequireEntryDependencyProcessor;
import org.flowerplatform.codesync.code.javascript.config.extension.AddNewExtension_BackboneClass;
import org.flowerplatform.codesync.code.javascript.config.extension.AddNewExtension_BackboneFormView;
import org.flowerplatform.codesync.code.javascript.config.extension.AddNewExtension_BackboneTableView;
import org.flowerplatform.codesync.code.javascript.config.extension.AddNewExtension_BackboneTableItemView;
import org.flowerplatform.codesync.code.javascript.config.extension.AddNewExtension_BackboneView;
import org.flowerplatform.codesync.code.javascript.operation_extension.JavaScriptFeatureAccessExtension;
import org.flowerplatform.codesync.code.javascript.processor.JavascriptElementProcessor;
import org.flowerplatform.codesync.processor.ChildrenUpdaterDiagramProcessor;
import org.flowerplatform.codesync.processor.CodeSyncCategorySeparatorProcessor;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;
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

	/**
	 * @author Mariana Gheorghe
	 * @author Cristina Constantinescu
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
		
		// descriptors for js code
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new JavaScriptDescriptors());
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new Runnable() {
			@Override
			public void run() {
				// extensions
				CodeSyncPlugin.getInstance().getFeatureAccessExtensions().add(new JavaScriptFeatureAccessExtension());
				CodeSyncPlugin.getInstance().getAddNewExtensions().add(new AddNewExtension_BackboneClass());
				CodeSyncPlugin.getInstance().getAddNewExtensions().add(new AddNewExtension_BackboneView());
				CodeSyncPlugin.getInstance().getAddNewExtensions().add(new AddNewExtension_BackboneTableView());
				CodeSyncPlugin.getInstance().getAddNewExtensions().add(new AddNewExtension_BackboneTableItemView());
				CodeSyncPlugin.getInstance().getAddNewExtensions().add(new AddNewExtension_BackboneFormView());
				
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
				
				CodeSyncPlugin.getInstance().getCodeSyncTypeCriterionDispatcherProcessor().addProcessor(JavaScriptDescriptors.TYPE_CLASS_DEPENDENCY, new AttributeWithRequireEntryDependencyProcessor(null));
				CodeSyncPlugin.getInstance().getCodeSyncTypeCriterionDispatcherProcessor().addProcessor(JavaScriptDescriptors.TYPE_HTML_TEMPLATE_DEPENDENCY, new AttributeWithRequireEntryDependencyProcessor("text!"));
				CodeSyncPlugin.getInstance().getCodeSyncTypeCriterionDispatcherProcessor().addProcessor(JavaScriptDescriptors.TYPE_REQUIRE_CLASS_DEPENDENCY, new RequireEntryDependencyProcessor(null));
				CodeSyncPlugin.getInstance().getCodeSyncTypeCriterionDispatcherProcessor().addProcessor(JavaScriptDescriptors.TYPE_REQUIRE_HTML_TEMPLATE_DEPENDENCY, new RequireEntryDependencyProcessor("text!"));
				CodeSyncPlugin.getInstance().getCodeSyncTypeCriterionDispatcherProcessor().addProcessor(JavaScriptDescriptors.TYPE_INHERITANCE, new InheritanceProcessor());
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
