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
package org.flowerplatform.editor.model {
	import flash.external.ExternalInterface;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayList;
	
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.model.action.AddRelatedElementsAction;
	import org.flowerplatform.editor.model.action.AddScenarioAction;
	import org.flowerplatform.editor.model.action.AddScenarioCommentAction;
	import org.flowerplatform.editor.model.action.ContentAssistAction;
	import org.flowerplatform.editor.model.action.DeleteScenarioElementAction;
	import org.flowerplatform.editor.model.action.DisplayMissingRelationsAction;
	import org.flowerplatform.editor.model.action.NewModelComposedAction;
	import org.flowerplatform.editor.model.action.RemoveFromDiagramAction;
	import org.flowerplatform.editor.model.action.RenameAction;
	import org.flowerplatform.editor.model.action.SearchAction;
	import org.flowerplatform.editor.model.controller.AbsoluteNodePlaceHolderDragController;
	import org.flowerplatform.editor.model.controller.BoxRendererController;
	import org.flowerplatform.editor.model.controller.DiagramModelChildrenController;
	import org.flowerplatform.editor.model.controller.EdgeRendererController;
	import org.flowerplatform.editor.model.controller.InplaceEditorController;
	import org.flowerplatform.editor.model.controller.NodeAbsoluteLayoutRectangleController;
	import org.flowerplatform.editor.model.controller.ResizeController;
	import org.flowerplatform.editor.model.controller.ViewModelChildrenController;
	import org.flowerplatform.editor.model.location_new_elements.LocationForNewElementsDialog;
	import org.flowerplatform.editor.model.properties.ILocationForNewElementsDialog;
	import org.flowerplatform.editor.model.properties.remote.DiagramSelectedItem;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.ViewDetailsUpdate;
	import org.flowerplatform.editor.model.remote.command.MoveResizeServerCommand;
	import org.flowerplatform.editor.model.renderer.BoxChildIconItemRenderer;
	import org.flowerplatform.editor.model.renderer.CenteredBoxChildRenderer;
	import org.flowerplatform.editor.model.renderer.ConnectionAnchorsSelectionRenderer;
	import org.flowerplatform.editor.model.renderer.DiagramNoteRenderer;
	import org.flowerplatform.editor.model.renderer.SeparatorRenderer;
	import org.flowerplatform.emf_model.notation.Bounds;
	import org.flowerplatform.emf_model.notation.Diagram;
	import org.flowerplatform.emf_model.notation.Edge;
	import org.flowerplatform.emf_model.notation.ExpandableNode;
	import org.flowerplatform.emf_model.notation.Location;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.emf_model.notation.Note;
	import org.flowerplatform.emf_model.notation.View;
	import org.flowerplatform.flexdiagram.controller.ComposedControllerProviderFactory;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.renderer.ClassReferenceRendererController;
	import org.flowerplatform.flexdiagram.controller.selection.SelectionController;
	import org.flowerplatform.flexdiagram.controller.visual_children.AbsoluteLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.controller.visual_children.SequentialLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.renderer.selection.StandardAnchorsSelectionRenderer;
	import org.flowerplatform.flexdiagram.tool.controller.DragToCreateRelationController;
	import org.flowerplatform.flexdiagram.tool.controller.SelectOrDragToCreateElementController;
	import org.flowerplatform.flexutil.FactoryWithInitialization;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.action.ClassFactoryActionProvider;
	import org.flowerplatform.flexutil.action.IActionProvider;
	import org.flowerplatform.flexutil.content_assist.ContentAssistItem;
	import org.flowerplatform.flexutil.dialog.IDialogResultHandler;
	import org.flowerplatform.flexutil.text.AutoGrowTextArea;
	import org.flowerplatform.properties.PropertiesPlugin;
	import org.flowerplatform.properties.action.ShowPropertiesAction;
	import org.flowerplatform.properties.property_renderer.StringWithButtonPropertyRenderer;
	
	import spark.components.TextInput;
	
	/**
	 * @author Cristi
	 */
	public class EditorModelPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:EditorModelPlugin;
		
		public var notationDiagramActionProviders:Vector.<IActionProvider> = new Vector.<IActionProvider>();
		
		public var notationDiagramClassFactoryActionProvider:ClassFactoryActionProvider = new ClassFactoryActionProvider();
		
		/**
		 * Map[String] -> ComposedControllerProviderFactory.
		 * 
		 * <p>
		 * Holds controllerProviderFactories for standard/common figure types. E.g. topLevelBox, topLevelBoxChild, etc.
		 * Registrations should be made during <code>preStart()</code>. The entries can be consumed starting form <code>
		 * start()</code>.
		 */
		public var standardControllerProviderFactories:Dictionary = new Dictionary();
		
		public static function getInstance():EditorModelPlugin {
			return INSTANCE;
		}
		
		/**
		 * key = viewType
		 */
		public var composedControllerProviderFactories:Dictionary = new Dictionary();
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
			
			var composedControllerProviderFactory:ComposedControllerProviderFactory; 
			
			// some standard standardControllerProviderFactories
			composedControllerProviderFactory = new ComposedControllerProviderFactory();
			standardControllerProviderFactories["topLevelBox"] = composedControllerProviderFactory;
			composedControllerProviderFactory.modelExtraInfoControllerClass = new FactoryWithInitialization(DynamicModelExtraInfoController);
			composedControllerProviderFactory.absoluteLayoutRectangleControllerClass = new FactoryWithInitialization(NodeAbsoluteLayoutRectangleController);
			composedControllerProviderFactory.rendererControllerClass = new FactoryWithInitialization(BoxRendererController, { removeRendererIfModelIsDisposed: true });
			composedControllerProviderFactory.selectionControllerClass = new FactoryWithInitialization(SelectionController, { selectionRendererClass: StandardAnchorsSelectionRenderer });
			composedControllerProviderFactory.dragControllerClass = new FactoryWithInitialization(AbsoluteNodePlaceHolderDragController);
			composedControllerProviderFactory.visualChildrenControllerClass = new FactoryWithInitialization(SequentialLayoutVisualChildrenController);
			composedControllerProviderFactory.modelChildrenControllerClass = new FactoryWithInitialization(ViewModelChildrenController);
			composedControllerProviderFactory.dragToCreateRelationControllerClass = new FactoryWithInitialization(DragToCreateRelationController);
			composedControllerProviderFactory.resizeControllerClass = new FactoryWithInitialization(ResizeController);
			
			composedControllerProviderFactory = new ComposedControllerProviderFactory();
			standardControllerProviderFactories["topLevelBoxChild"] = composedControllerProviderFactory;
			composedControllerProviderFactory.modelExtraInfoControllerClass = new FactoryWithInitialization(DynamicModelExtraInfoController);
			composedControllerProviderFactory.rendererControllerClass = new FactoryWithInitialization(ClassReferenceRendererController, { rendererClass: BoxChildIconItemRenderer});
			composedControllerProviderFactory.selectionControllerClass = new FactoryWithInitialization(SelectionController, { selectionRendererClass: StandardAnchorsSelectionRenderer });
			composedControllerProviderFactory.modelChildrenControllerClass = new FactoryWithInitialization(ViewModelChildrenController);
			composedControllerProviderFactory.dragToCreateRelationControllerClass = new FactoryWithInitialization(DragToCreateRelationController);
			if (!FlexUtilGlobals.getInstance().isMobile) {
				composedControllerProviderFactory.inplaceEditorControllerClass = new FactoryWithInitialization(InplaceEditorController, {rendererClass : TextInput});
			}
			
			// these controllerProvFactories are registered directly in composedControllerProviderFactories; not in standardControllerProviderFactories
			composedControllerProviderFactory = new ComposedControllerProviderFactory();
			composedControllerProviderFactory.modelExtraInfoControllerClass = new FactoryWithInitialization(DynamicModelExtraInfoController);
			composedControllerProviderFactory.rendererControllerClass = new FactoryWithInitialization(ClassReferenceRendererController, { rendererClass: CenteredBoxChildRenderer});
			if (!FlexUtilGlobals.getInstance().isMobile) {
				composedControllerProviderFactory.inplaceEditorControllerClass = new FactoryWithInitialization(InplaceEditorController, {rendererClass : TextInput});
			}
			EditorModelPlugin.getInstance().composedControllerProviderFactories["topLevelBoxTitle"] = composedControllerProviderFactory;
			
			// TODO CC/JS to delete, and change to model desc			
			composedControllerProviderFactory = new ComposedControllerProviderFactory();			
			composedControllerProviderFactory.modelExtraInfoControllerClass = new FactoryWithInitialization(DynamicModelExtraInfoController);
			composedControllerProviderFactory.absoluteLayoutRectangleControllerClass = new FactoryWithInitialization(NodeAbsoluteLayoutRectangleController);
			composedControllerProviderFactory.selectionControllerClass = new FactoryWithInitialization(SelectionController, { selectionRendererClass: StandardAnchorsSelectionRenderer });
			composedControllerProviderFactory.dragControllerClass = new FactoryWithInitialization(AbsoluteNodePlaceHolderDragController);
			composedControllerProviderFactory.dragToCreateRelationControllerClass = new FactoryWithInitialization(DragToCreateRelationController);
			composedControllerProviderFactory.resizeControllerClass = new FactoryWithInitialization(ResizeController);			
			composedControllerProviderFactory.rendererControllerClass = new FactoryWithInitialization(ClassReferenceRendererController, { rendererClass: DiagramNoteRenderer});
			if (!FlexUtilGlobals.getInstance().isMobile) {
				composedControllerProviderFactory.inplaceEditorControllerClass = new FactoryWithInitialization(InplaceEditorController, {rendererClass : AutoGrowTextArea});
			}			
			EditorModelPlugin.getInstance().composedControllerProviderFactories["classDiagram.note"] = composedControllerProviderFactory;
			
			composedControllerProviderFactory = new ComposedControllerProviderFactory();
			composedControllerProviderFactory.rendererControllerClass = new FactoryWithInitialization(ClassReferenceRendererController, { rendererClass: SeparatorRenderer });
			EditorModelPlugin.getInstance().composedControllerProviderFactories["categorySeparator"] = composedControllerProviderFactory;
			
			// other initializations
			notationDiagramActionProviders.push(notationDiagramClassFactoryActionProvider);
			
			var editorDescriptor:NotationDiagramEditorDescriptor = new NotationDiagramEditorDescriptor();
			EditorPlugin.getInstance().editorDescriptors.push(editorDescriptor);
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(editorDescriptor);
			
			// TODO CS/JS to delete, and change to model desc
			// classDiagram
			composedControllerProviderFactory = new ComposedControllerProviderFactory();
			composedControllerProviderFactory.modelExtraInfoControllerClass = new FactoryWithInitialization(DynamicModelExtraInfoController);
			composedControllerProviderFactory.modelChildrenControllerClass = new FactoryWithInitialization(DiagramModelChildrenController);
			composedControllerProviderFactory.visualChildrenControllerClass = new FactoryWithInitialization(AbsoluteLayoutVisualChildrenController);
			composedControllerProviderFactory.selectOrDragToCreateElementControllerClass = new FactoryWithInitialization(SelectOrDragToCreateElementController);
			composedControllerProviderFactories["classDiagram"] = composedControllerProviderFactory;
			
			// class
			composedControllerProviderFactory = new ComposedControllerProviderFactory();
			composedControllerProviderFactory.modelExtraInfoControllerClass = new FactoryWithInitialization(DynamicModelExtraInfoController);
			composedControllerProviderFactory.absoluteLayoutRectangleControllerClass = new FactoryWithInitialization(NodeAbsoluteLayoutRectangleController);
			composedControllerProviderFactory.rendererControllerClass = new FactoryWithInitialization(BoxRendererController, { removeRendererIfModelIsDisposed: true });
			composedControllerProviderFactory.selectionControllerClass = new FactoryWithInitialization(SelectionController, { selectionRendererClass: StandardAnchorsSelectionRenderer });
			composedControllerProviderFactory.dragControllerClass = new FactoryWithInitialization(AbsoluteNodePlaceHolderDragController);
			composedControllerProviderFactory.visualChildrenControllerClass = new FactoryWithInitialization(SequentialLayoutVisualChildrenController);
			composedControllerProviderFactory.modelChildrenControllerClass = new FactoryWithInitialization(ViewModelChildrenController);
			composedControllerProviderFactory.dragToCreateRelationControllerClass = new FactoryWithInitialization(DragToCreateRelationController);			
			composedControllerProviderFactories["classDiagram.javaClass"] = composedControllerProviderFactory;
			
			// class members
			composedControllerProviderFactory = new ComposedControllerProviderFactory();
			composedControllerProviderFactory.modelExtraInfoControllerClass = new FactoryWithInitialization(DynamicModelExtraInfoController);
			composedControllerProviderFactory.rendererControllerClass = new FactoryWithInitialization(ClassReferenceRendererController, { rendererClass: BoxChildIconItemRenderer});
			composedControllerProviderFactory.selectionControllerClass = new FactoryWithInitialization(SelectionController, { selectionRendererClass: StandardAnchorsSelectionRenderer });
			composedControllerProviderFactory.modelChildrenControllerClass = new FactoryWithInitialization(ViewModelChildrenController);
			composedControllerProviderFactory.dragToCreateRelationControllerClass = new FactoryWithInitialization(DragToCreateRelationController);
			if (!FlexUtilGlobals.getInstance().isMobile) {
				composedControllerProviderFactory.inplaceEditorControllerClass = new FactoryWithInitialization(InplaceEditorController);
			}
			composedControllerProviderFactories["classDiagram.javaClass.javaAttribute"] = composedControllerProviderFactory;
			composedControllerProviderFactories["classDiagram.javaClass.javaOperation"] = composedControllerProviderFactory;
				
			composedControllerProviderFactory = new ComposedControllerProviderFactory();
			composedControllerProviderFactory.rendererControllerClass = new FactoryWithInitialization(ClassReferenceRendererController, { rendererClass: CenteredBoxChildRenderer});
			composedControllerProviderFactories["classTitle"] = composedControllerProviderFactory;
			composedControllerProviderFactories["classDiagram.javaClass.title"] = composedControllerProviderFactory;
			
			// scenario interaction
			composedControllerProviderFactory = new ComposedControllerProviderFactory();
			composedControllerProviderFactory.modelExtraInfoControllerClass = new FactoryWithInitialization(DynamicModelExtraInfoController);
			composedControllerProviderFactory.rendererControllerClass = new FactoryWithInitialization(EdgeRendererController, { removeRendererIfModelIsDisposed: true });
			composedControllerProviderFactory.selectionControllerClass = new FactoryWithInitialization(SelectionController, { selectionRendererClass: ConnectionAnchorsSelectionRenderer });
			composedControllerProviderFactory.modelChildrenControllerClass = new FactoryWithInitialization(ViewModelChildrenController);
			composedControllerProviderFactories["edge"] = composedControllerProviderFactory;
			
			notationDiagramClassFactoryActionProvider.actionClasses.push(RenameAction);
			notationDiagramClassFactoryActionProvider.actionClasses.push(RemoveFromDiagramAction);
			notationDiagramClassFactoryActionProvider.actionClasses.push(DisplayMissingRelationsAction);
			notationDiagramClassFactoryActionProvider.actionClasses.push(AddRelatedElementsAction);
			notationDiagramClassFactoryActionProvider.actionClasses.push(AddScenarioAction);
			notationDiagramClassFactoryActionProvider.actionClasses.push(AddScenarioCommentAction);

			notationDiagramClassFactoryActionProvider.actionClasses.push(DeleteScenarioElementAction);

			notationDiagramClassFactoryActionProvider.actionClasses.push(ContentAssistAction);

			notationDiagramClassFactoryActionProvider.actionClasses.push(NewModelComposedAction);

			notationDiagramClassFactoryActionProvider.actionClasses.push(SearchAction);
			notationDiagramClassFactoryActionProvider.actionClasses.push(ShowPropertiesAction);
			
			
			if (ExternalInterface.available) { 
				ExternalInterface.addCallback("dragOnDiagram", dragOnDiagram); 
			}
			
			// register PropertiesPlugin Renderer
			//TODO decoment
			PropertiesPlugin.getInstance().propertyRendererClasses["StringWithDialog"] = new FactoryWithInitialization
				(StringWithButtonPropertyRenderer, {
					clickHandler: function(itemRendererHandler:IDialogResultHandler, propertyName:String, propertyValue:Object):void {
						var dialog:ILocationForNewElementsDialog = new LocationForNewElementsDialog();
						dialog.setResultHandler(itemRendererHandler);
						dialog.selectionOfItems = PropertiesPlugin.getInstance().propertiesView.selectionForServer;
						dialog.currentLocationForNewElements = propertyValue;
						
						FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
						.setViewContent(dialog)						
						.setWidth(470)
						.setHeight(450)
						.show();
					},
					
					getNewPropertyValueHandler: function (dialogResult:Object):String {
						return dialogResult.location;
					}
				});
		}
		
		override protected function registerClassAliases():void {
			registerClassAliasFromAnnotation(View);
			registerClassAliasFromAnnotation(Node);
			registerClassAliasFromAnnotation(Edge);
			registerClassAliasFromAnnotation(Note);
			registerClassAliasFromAnnotation(Diagram);
			registerClassAliasFromAnnotation(Location);
			registerClassAliasFromAnnotation(Bounds);			
			
			registerClassAliasFromAnnotation(ExpandableNode);
			registerClassAliasFromAnnotation(MoveResizeServerCommand);			
			registerClassAliasFromAnnotation(ViewDetailsUpdate);
						
			registerClassAliasFromAnnotation(DiagramSelectedItem);

			registerClassAliasFromAnnotation(ContentAssistItem);
		}	
		

		/**
		 * @author Sebastian Solomon
		 * 
		 */
		public function dragOnDiagram(paths:String):String {
			var alPaths:ArrayList;
			if (paths.charAt(0) == '['){
				paths = paths.substr(1);
			}
			if (paths.charAt(paths.length - 1) == ']'){
				paths = paths.substr(0, paths.length - 1);
			}
			alPaths = new ArrayList(paths.split(","));
			NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).service_handleDragOnDiagram(alPaths);
			return "";
		}
		
	}
}
