package org.flowerplatform.editor.model {
	import flash.utils.Dictionary;
	
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.model.action.DragOnDiagramAction;
	import org.flowerplatform.editor.model.controller.AbsoluteNodePlaceHolderDragController;
	import org.flowerplatform.editor.model.controller.BoxRendererController;
	import org.flowerplatform.editor.model.controller.DiagramModelChildrenController;
	import org.flowerplatform.editor.model.controller.EdgeRendererController;
	import org.flowerplatform.editor.model.controller.NodeAbsoluteLayoutRectangleController;
	import org.flowerplatform.editor.model.controller.ViewModelChildrenController;
	import org.flowerplatform.editor.model.remote.NewJavaClassDiagramAction;
	import org.flowerplatform.editor.model.remote.ViewDetailsUpdate;
	import org.flowerplatform.editor.model.remote.command.MoveResizeServerCommand;
	import org.flowerplatform.editor.model.renderer.AttributesSeparatorRenderer;
	import org.flowerplatform.editor.model.renderer.BoxChildIconItemRenderer;
	import org.flowerplatform.editor.model.renderer.OperationsSeparatorRenderer;
	import org.flowerplatform.emf_model.notation.Bounds;
	import org.flowerplatform.emf_model.notation.Diagram;
	import org.flowerplatform.emf_model.notation.Edge;
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
	import org.flowerplatform.flexdiagram.renderer.selection.StandardAnchorsSelectionRenderer;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.popup.ClassFactoryActionProvider;
	import org.flowerplatform.flexutil.popup.IActionProvider;
	import org.flowerplatform.web.common.WebCommonPlugin;
	
	/**
	 * @author Cristi
	 */
	public class EditorModelPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:EditorModelPlugin;
		
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
			composedControllerProviderFactory.rendererControllerClass = new ControllerFactory(BoxRendererController);
			composedControllerProviderFactory.selectionControllerClass = new ControllerFactory(SelectionController, { selectionRendererClass: StandardAnchorsSelectionRenderer });
			composedControllerProviderFactory.dragControllerClass = new ControllerFactory(AbsoluteNodePlaceHolderDragController);
			composedControllerProviderFactory.visualChildrenControllerClass = new ControllerFactory(SequentialLayoutVisualChildrenController);
			composedControllerProviderFactory.modelChildrenControllerClass = new ControllerFactory(ViewModelChildrenController);
			composedControllerProviderFactories["class"] = composedControllerProviderFactory;
			
			// class members
			composedControllerProviderFactory = new ComposedControllerProviderFactory();
			composedControllerProviderFactory.modelExtraInfoControllerClass = new ControllerFactory(LightweightModelExtraInfoController);
			composedControllerProviderFactory.rendererControllerClass = new ControllerFactory(ClassReferenceRendererController, { rendererClass: BoxChildIconItemRenderer});
//			composedControllerProviderFactory.selectionControllerClass = new ControllerFactory(AnchorsSelectionController);
			composedControllerProviderFactory.modelChildrenControllerClass = new ControllerFactory(ViewModelChildrenController);
			composedControllerProviderFactories["classAttribute"] = composedControllerProviderFactory;
			composedControllerProviderFactories["classOperation"] = composedControllerProviderFactory;
			composedControllerProviderFactories["classTitle"] = composedControllerProviderFactory;
			
			// class separators
			composedControllerProviderFactory = new ComposedControllerProviderFactory();
			composedControllerProviderFactory.modelExtraInfoControllerClass = new ControllerFactory(LightweightModelExtraInfoController);
			composedControllerProviderFactory.rendererControllerClass = new ControllerFactory(ClassReferenceRendererController, { rendererClass: AttributesSeparatorRenderer});
			composedControllerProviderFactories["classAttributesCompartmentSeparator"] = composedControllerProviderFactory;
			
			composedControllerProviderFactory = new ComposedControllerProviderFactory();
			composedControllerProviderFactory.modelExtraInfoControllerClass = new ControllerFactory(LightweightModelExtraInfoController);
			composedControllerProviderFactory.rendererControllerClass = new ControllerFactory(ClassReferenceRendererController, { rendererClass: OperationsSeparatorRenderer});
			composedControllerProviderFactories["classOperationsCompartmentSeparator"] = composedControllerProviderFactory;

			// scenario interaction
			composedControllerProviderFactory = new ComposedControllerProviderFactory();
			composedControllerProviderFactory.modelExtraInfoControllerClass = new ControllerFactory(LightweightModelExtraInfoController);
			composedControllerProviderFactory.rendererControllerClass = new ControllerFactory(EdgeRendererController);
			composedControllerProviderFactory.modelChildrenControllerClass = new ControllerFactory(ViewModelChildrenController);
			composedControllerProviderFactories["scenarioInterraction"] = composedControllerProviderFactory;
		}
		
		override public function start():void {
			super.start();
			WebCommonPlugin.getInstance().explorerTreeClassFactoryActionProvider.actionClasses.push(DragOnDiagramAction);
			WebCommonPlugin.getInstance().explorerTreeClassFactoryActionProvider.actionClasses.push(NewJavaClassDiagramAction);
		}
		
		override protected function registerClassAliases():void {
			registerClassAliasFromAnnotation(View);
			registerClassAliasFromAnnotation(Node);
			registerClassAliasFromAnnotation(Edge);
			registerClassAliasFromAnnotation(Diagram);
			registerClassAliasFromAnnotation(Location);
			registerClassAliasFromAnnotation(Bounds);
			
			registerClassAliasFromAnnotation(MoveResizeServerCommand);
			
			registerClassAliasFromAnnotation(ViewDetailsUpdate);
			
			registerClassAliasFromAnnotation(NewJavaClassDiagramAction);
		}
		
		override protected function registerMessageBundle():void {
			// do nothing; this plugin doesn't have a .resources (yet)
		}
		
	}
}
