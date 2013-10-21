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
package org.flowerplatform.codesync.code.javascript {
	
	import org.flowerplatform.codesync.code.javascript.model.action.DeleteElementAction;
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.editor.model.EditorModelPlugin;
	import org.flowerplatform.editor.model.controller.AbsoluteNodePlaceHolderDragController;
	import org.flowerplatform.editor.model.controller.BoxRendererController;
	import org.flowerplatform.editor.model.controller.InplaceEditorController;
	import org.flowerplatform.editor.model.controller.NodeAbsoluteLayoutRectangleController;
	import org.flowerplatform.editor.model.controller.ViewModelChildrenController;
	import org.flowerplatform.editor.model.renderer.BoxChildIconItemRenderer;
	import org.flowerplatform.flexdiagram.controller.ComposedControllerProviderFactory;
	import org.flowerplatform.flexdiagram.controller.ControllerFactory;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.renderer.ClassReferenceRendererController;
	import org.flowerplatform.flexdiagram.controller.selection.SelectionController;
	import org.flowerplatform.flexdiagram.controller.visual_children.SequentialLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.renderer.selection.ChildAnchorsSelectionRenderer;
	import org.flowerplatform.flexdiagram.renderer.selection.StandardAnchorsSelectionRenderer;
	import org.flowerplatform.flexdiagram.tool.controller.DragToCreateRelationController;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class CodeSyncCodeJavascriptPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:CodeSyncCodeJavascriptPlugin;
		
		public static function getInstance():CodeSyncCodeJavascriptPlugin {
			return INSTANCE;
		}
		
		override public function start():void {
			super.start();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
			
			EditorModelPlugin.getInstance().notationDiagramClassFactoryActionProvider.actionClasses.push(DeleteElementAction);
			
			var composedControllerProviderFactory:ComposedControllerProviderFactory;
			
			// parent box - set as a direct diagram child, positioned absolutely
			composedControllerProviderFactory = new ComposedControllerProviderFactory();
			composedControllerProviderFactory.modelExtraInfoControllerClass = new ControllerFactory(DynamicModelExtraInfoController);
			composedControllerProviderFactory.absoluteLayoutRectangleControllerClass = new ControllerFactory(NodeAbsoluteLayoutRectangleController);
			composedControllerProviderFactory.rendererControllerClass = new ControllerFactory(BoxRendererController, { removeRendererIfModelIsDisposed: true });
			composedControllerProviderFactory.selectionControllerClass = new ControllerFactory(SelectionController, { selectionRendererClass: StandardAnchorsSelectionRenderer });
			composedControllerProviderFactory.dragControllerClass = new ControllerFactory(AbsoluteNodePlaceHolderDragController);
			composedControllerProviderFactory.visualChildrenControllerClass = new ControllerFactory(SequentialLayoutVisualChildrenController);
			composedControllerProviderFactory.modelChildrenControllerClass = new ControllerFactory(ViewModelChildrenController);
//			composedControllerProviderFactory.dragToCreateRelationControllerClass = new ControllerFactory(DragToCreateRelationController);
			EditorModelPlugin.getInstance().composedControllerProviderFactories["classDiagram.backboneClass"] = composedControllerProviderFactory;
			EditorModelPlugin.getInstance().composedControllerProviderFactories["classDiagram.table"] = composedControllerProviderFactory;
			EditorModelPlugin.getInstance().composedControllerProviderFactories["classDiagram.tableItem"] = composedControllerProviderFactory;
			EditorModelPlugin.getInstance().composedControllerProviderFactories["classDiagram.form"] = composedControllerProviderFactory;
			
			// class members
			composedControllerProviderFactory = new ComposedControllerProviderFactory();
			composedControllerProviderFactory.modelExtraInfoControllerClass = new ControllerFactory(DynamicModelExtraInfoController);
			composedControllerProviderFactory.rendererControllerClass = new ControllerFactory(ClassReferenceRendererController, { rendererClass: BoxChildIconItemRenderer});
			composedControllerProviderFactory.selectionControllerClass = new ControllerFactory(SelectionController, { selectionRendererClass: ChildAnchorsSelectionRenderer });
			composedControllerProviderFactory.modelChildrenControllerClass = new ControllerFactory(ViewModelChildrenController);

			composedControllerProviderFactory.dragToCreateRelationControllerClass = new ControllerFactory(DragToCreateRelationController);
			if (!FlexUtilGlobals.getInstance().isMobile) {
				composedControllerProviderFactory.inplaceEditorControllerClass = new ControllerFactory(InplaceEditorController);
			}
			EditorModelPlugin.getInstance().composedControllerProviderFactories["classDiagram.backboneClass.javaScriptOperation"] = composedControllerProviderFactory;
			EditorModelPlugin.getInstance().composedControllerProviderFactories["classDiagram.backboneClass.javaScriptAttribute"] = composedControllerProviderFactory;
			EditorModelPlugin.getInstance().composedControllerProviderFactories["classDiagram.backboneClass.requireEntry"] = composedControllerProviderFactory;
			EditorModelPlugin.getInstance().composedControllerProviderFactories["classDiagram.backboneClass.eventsAttribute"] = composedControllerProviderFactory;
			EditorModelPlugin.getInstance().composedControllerProviderFactories["classDiagram.backboneClass.routesAttribute"] = composedControllerProviderFactory;
			EditorModelPlugin.getInstance().composedControllerProviderFactories["classDiagram.backboneClass.eventsAttribute.eventsAttributeEntry"] = composedControllerProviderFactory;
			EditorModelPlugin.getInstance().composedControllerProviderFactories["classDiagram.backboneClass.routesAttribute.routesAttributeEntry"] = composedControllerProviderFactory;
			EditorModelPlugin.getInstance().composedControllerProviderFactories["classDiagram.table.tableHeaderEntry"] = composedControllerProviderFactory;
			EditorModelPlugin.getInstance().composedControllerProviderFactories["classDiagram.tableItem.tableItemEntry"] = composedControllerProviderFactory;
			EditorModelPlugin.getInstance().composedControllerProviderFactories["classDiagram.form.formItem"] = composedControllerProviderFactory;
		}
		
		override protected function registerMessageBundle():void {
			// no messages yet
		}
		
	}
}
