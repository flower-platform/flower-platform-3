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
	import flash.utils.Dictionary;
	
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.model.action.AddScenarioAction;
	import org.flowerplatform.editor.model.action.AddScenarioCommentAction;
	import org.flowerplatform.editor.model.action.ContentAssistAction;
	import org.flowerplatform.editor.model.action.DeleteAction;
	import org.flowerplatform.editor.model.action.DeleteScenarioElementAction;
	import org.flowerplatform.editor.model.action.ExpandAttributesCompartmentAction;
	import org.flowerplatform.editor.model.action.ExpandOperationsCompartmentAction;
	import org.flowerplatform.editor.model.action.RenameAction;
	import org.flowerplatform.editor.model.controller.AbsoluteNodePlaceHolderDragController;
	import org.flowerplatform.editor.model.controller.BoxRendererController;
	import org.flowerplatform.editor.model.controller.DiagramModelChildrenController;
	import org.flowerplatform.editor.model.controller.DragToCreateRelationController;
	import org.flowerplatform.editor.model.controller.EdgeRendererController;
	import org.flowerplatform.editor.model.controller.InplaceEditorController;
	import org.flowerplatform.editor.model.controller.NodeAbsoluteLayoutRectangleController;
	import org.flowerplatform.editor.model.controller.ViewModelChildrenController;
	import org.flowerplatform.editor.model.remote.ViewDetailsUpdate;
	import org.flowerplatform.editor.model.remote.command.MoveResizeServerCommand;
	import org.flowerplatform.editor.model.renderer.AttributesSeparatorRenderer;
	import org.flowerplatform.editor.model.renderer.BoxChildIconItemRenderer;
	import org.flowerplatform.editor.model.renderer.CenteredBoxChildIconItemRenderer;
	import org.flowerplatform.editor.model.renderer.OperationsSeparatorRenderer;
	import org.flowerplatform.emf_model.notation.Bounds;
	import org.flowerplatform.emf_model.notation.Diagram;
	import org.flowerplatform.emf_model.notation.Edge;
	import org.flowerplatform.emf_model.notation.ExpandableNode;
	import org.flowerplatform.emf_model.notation.Location;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.emf_model.notation.View;
	import org.flowerplatform.flexdiagram.controller.ComposedControllerProviderFactory;
	import org.flowerplatform.flexdiagram.controller.ControllerFactory;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.LightweightModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.renderer.ClassReferenceRendererController;
	import org.flowerplatform.flexdiagram.controller.selection.SelectionController;
	import org.flowerplatform.flexdiagram.controller.visual_children.AbsoluteLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.controller.visual_children.SequentialLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.renderer.selection.ChildAnchorsSelectionRenderer;
	import org.flowerplatform.flexdiagram.renderer.selection.StandardAnchorsSelectionRenderer;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.action.ClassFactoryActionProvider;
	import org.flowerplatform.flexutil.action.IActionProvider;
	import org.flowerplatform.flexutil.content_assist.ContentAssistItem;
	
	/**
	 * @author Cristi
	 */
	public class EditorModelPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:EditorModelPlugin;
		
		public var notationDiagramActionProviders:Vector.<IActionProvider> = new Vector.<IActionProvider>();
		
		public var notationDiagramClassFactoryActionProvider:ClassFactoryActionProvider = new ClassFactoryActionProvider();
		
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
			
			notationDiagramActionProviders.push(notationDiagramClassFactoryActionProvider);
			
			var editorDescriptor:NotationDiagramEditorDescriptor = new NotationDiagramEditorDescriptor();
			EditorPlugin.getInstance().editorDescriptors.push(editorDescriptor);
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(editorDescriptor);
			
			var composedControllerProviderFactory:ComposedControllerProviderFactory; 
			
			// classDiagram
			composedControllerProviderFactory = new ComposedControllerProviderFactory();
			composedControllerProviderFactory.modelExtraInfoControllerClass = new ControllerFactory(LightweightModelExtraInfoController);
			composedControllerProviderFactory.modelChildrenControllerClass = new ControllerFactory(DiagramModelChildrenController);
			composedControllerProviderFactory.visualChildrenControllerClass = new ControllerFactory(AbsoluteLayoutVisualChildrenController);
			composedControllerProviderFactories["classDiagram"] = composedControllerProviderFactory;
			
			// class
			composedControllerProviderFactory = new ComposedControllerProviderFactory();
			composedControllerProviderFactory.modelExtraInfoControllerClass = new ControllerFactory(DynamicModelExtraInfoController);
			composedControllerProviderFactory.absoluteLayoutRectangleControllerClass = new ControllerFactory(NodeAbsoluteLayoutRectangleController);
			composedControllerProviderFactory.rendererControllerClass = new ControllerFactory(BoxRendererController, { removeRendererIfModelIsDisposed: true });
			composedControllerProviderFactory.selectionControllerClass = new ControllerFactory(SelectionController, { selectionRendererClass: StandardAnchorsSelectionRenderer });
			composedControllerProviderFactory.dragControllerClass = new ControllerFactory(AbsoluteNodePlaceHolderDragController);
			composedControllerProviderFactory.visualChildrenControllerClass = new ControllerFactory(SequentialLayoutVisualChildrenController);
			composedControllerProviderFactory.modelChildrenControllerClass = new ControllerFactory(ViewModelChildrenController);
			composedControllerProviderFactory.dragToCreateRelationControllerClass = new ControllerFactory(DragToCreateRelationController);
			composedControllerProviderFactories["class"] = composedControllerProviderFactory;
			
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
			composedControllerProviderFactories["classAttribute"] = composedControllerProviderFactory;
			composedControllerProviderFactories["classOperation"] = composedControllerProviderFactory;
			composedControllerProviderFactories["classDiagram.BackboneClass.Operation"] = composedControllerProviderFactory;
			composedControllerProviderFactories["classDiagram.BackboneClass.Attribute"] = composedControllerProviderFactory;
			
			composedControllerProviderFactory = new ComposedControllerProviderFactory();
			composedControllerProviderFactory.rendererControllerClass = new ControllerFactory(ClassReferenceRendererController, { rendererClass: CenteredBoxChildIconItemRenderer});
			composedControllerProviderFactories["classTitle"] = composedControllerProviderFactory;
			composedControllerProviderFactories["title"] = composedControllerProviderFactory;
			
			// class separators
			composedControllerProviderFactory = new ComposedControllerProviderFactory();
			composedControllerProviderFactory.rendererControllerClass = new ControllerFactory(ClassReferenceRendererController, { rendererClass: AttributesSeparatorRenderer});
			composedControllerProviderFactories["classAttributesCompartmentSeparator"] = composedControllerProviderFactory;
			
			composedControllerProviderFactory = new ComposedControllerProviderFactory();
			composedControllerProviderFactory.rendererControllerClass = new ControllerFactory(ClassReferenceRendererController, { rendererClass: OperationsSeparatorRenderer});
			composedControllerProviderFactories["classOperationsCompartmentSeparator"] = composedControllerProviderFactory;

			// scenario interaction
			composedControllerProviderFactory = new ComposedControllerProviderFactory();
			composedControllerProviderFactory.modelExtraInfoControllerClass = new ControllerFactory(LightweightModelExtraInfoController);
			composedControllerProviderFactory.rendererControllerClass = new ControllerFactory(EdgeRendererController, { removeRendererIfModelIsDisposed: true });
			composedControllerProviderFactory.modelChildrenControllerClass = new ControllerFactory(ViewModelChildrenController);
			composedControllerProviderFactories["scenarioInterraction"] = composedControllerProviderFactory;
			
			notationDiagramClassFactoryActionProvider.actionClasses.push(RenameAction);
			notationDiagramClassFactoryActionProvider.actionClasses.push(DeleteAction);
			notationDiagramClassFactoryActionProvider.actionClasses.push(ExpandAttributesCompartmentAction);
			notationDiagramClassFactoryActionProvider.actionClasses.push(ExpandOperationsCompartmentAction);
			notationDiagramClassFactoryActionProvider.actionClasses.push(AddScenarioAction);
			notationDiagramClassFactoryActionProvider.actionClasses.push(AddScenarioCommentAction);

			notationDiagramClassFactoryActionProvider.actionClasses.push(DeleteScenarioElementAction);
			notationDiagramClassFactoryActionProvider.actionClasses.push(ContentAssistAction);
			notationDiagramClassFactoryActionProvider.actionClasses.push(ContentAssistAction);
		}
		
		override protected function registerClassAliases():void {
			registerClassAliasFromAnnotation(View);
			registerClassAliasFromAnnotation(Node);
			registerClassAliasFromAnnotation(Edge);
			registerClassAliasFromAnnotation(Diagram);
			registerClassAliasFromAnnotation(Location);
			registerClassAliasFromAnnotation(Bounds);
			registerClassAliasFromAnnotation(ExpandableNode);
			registerClassAliasFromAnnotation(MoveResizeServerCommand);			
			registerClassAliasFromAnnotation(ViewDetailsUpdate);
			registerClassAliasFromAnnotation(ContentAssistItem);
		}
		
	}
}
