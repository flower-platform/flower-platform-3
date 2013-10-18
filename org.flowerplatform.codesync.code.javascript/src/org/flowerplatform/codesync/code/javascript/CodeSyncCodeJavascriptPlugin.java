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

import org.flowerplatform.codesync.code.javascript.processor.JavascriptElementProcessor;
import org.flowerplatform.codesync.code.javascript.remote.JavascriptClassDiagramOperationsService;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
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
		
		CommunicationPlugin.getInstance().getServiceRegistry().registerService(JavascriptClassDiagramOperationsService.SERVICE_ID, new JavascriptClassDiagramOperationsService());
		
		JavascriptElementProcessor processor = new JavascriptElementProcessor();
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("title", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.BackboneClass.Operation", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.BackboneClass.Attribute", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.BackboneClass.RequireEntry", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.BackboneClass.EventsAttribute", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.BackboneClass.RoutesAttribute", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.BackboneClass.EventsAttribute.EventsAttributeEntry", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.BackboneClass.RoutesAttribute.RoutesAttributeEntry", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.Table.TableHeaderEntry", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.TableItem.TableItemEntry", processor);
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.Form.FormItem", processor);
	
	}
	
}
