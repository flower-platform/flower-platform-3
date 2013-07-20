package org.flowerplatform.editor.model {
	import flash.utils.Dictionary;
	
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.model.action.DragOnDiagramAction;
	import org.flowerplatform.editor.model.controller.AbsoluteNodePlaceHolderDragController;
	import org.flowerplatform.editor.model.controller.BasicModelRendererController;
	import org.flowerplatform.editor.model.controller.NodeAbsoluteLayoutRectangleController;
	import org.flowerplatform.editor.model.controller.ViewModelChildrenController;
	import org.flowerplatform.editor.model.remote.ViewDetailsUpdate;
	import org.flowerplatform.editor.model.remote.command.MoveResizeServerCommand;
	import org.flowerplatform.editor.model.renderer.AttributesSeparatorRenderer;
	import org.flowerplatform.editor.model.renderer.BoxChildIconItemRenderer;
	import org.flowerplatform.editor.model.renderer.OperationsSeparatorRenderer;
	import org.flowerplatform.editor.model.renderer.SeparatorRenderer;
	import org.flowerplatform.emf_model.notation.Bounds;
	import org.flowerplatform.emf_model.notation.Diagram;
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
				
			composedControllerProviderFactory = new ComposedControllerProviderFactory();
			composedControllerProviderFactories["classDiagram"] = composedControllerProviderFactory;
			composedControllerProviderFactory.modelExtraInfoControllerClass = new ControllerFactory(LightweightModelExtraInfoController);
			// children
			composedControllerProviderFactory.modelChildrenControllerClass = new ControllerFactory(ViewModelChildrenController);
			composedControllerProviderFactory.visualChildrenControllerClass = new ControllerFactory(AbsoluteLayoutVisualChildrenController);
			
			composedControllerProviderFactory = new ComposedControllerProviderFactory();
			composedControllerProviderFactories["class"] = composedControllerProviderFactory;
			composedControllerProviderFactory.modelExtraInfoControllerClass = new ControllerFactory(DynamicModelExtraInfoController);
			composedControllerProviderFactory.absoluteLayoutRectangleControllerClass = new ControllerFactory(NodeAbsoluteLayoutRectangleController);
			composedControllerProviderFactory.rendererControllerClass = new ControllerFactory(BasicModelRendererController);
			composedControllerProviderFactory.selectionControllerClass = new ControllerFactory(SelectionController, { selectionRendererClass: StandardAnchorsSelectionRenderer });
			composedControllerProviderFactory.dragControllerClass = new ControllerFactory(AbsoluteNodePlaceHolderDragController);
			// children
			composedControllerProviderFactory.visualChildrenControllerClass = new ControllerFactory(SequentialLayoutVisualChildrenController);
			composedControllerProviderFactory.modelChildrenControllerClass = new ControllerFactory(ViewModelChildrenController);
			
			composedControllerProviderFactory = new ComposedControllerProviderFactory();
			composedControllerProviderFactory.modelExtraInfoControllerClass = new ControllerFactory(LightweightModelExtraInfoController);
			composedControllerProviderFactory.rendererControllerClass = new ControllerFactory(ClassReferenceRendererController, { rendererClass: BoxChildIconItemRenderer});
//			composedControllerProviderFactory.selectionControllerClass = new ControllerFactory(AnchorsSelectionController);
			composedControllerProviderFactories["classAttribute"] = composedControllerProviderFactory;
			composedControllerProviderFactories["classOperation"] = composedControllerProviderFactory;
			composedControllerProviderFactories["classTitle"] = composedControllerProviderFactory;
			
			composedControllerProviderFactory = new ComposedControllerProviderFactory();
			composedControllerProviderFactory.modelExtraInfoControllerClass = new ControllerFactory(LightweightModelExtraInfoController);
			composedControllerProviderFactory.rendererControllerClass = new ControllerFactory(ClassReferenceRendererController, { rendererClass: AttributesSeparatorRenderer});
			composedControllerProviderFactories["classAttributesCompartmentSeparator"] = composedControllerProviderFactory;
			
			composedControllerProviderFactory = new ComposedControllerProviderFactory();
			composedControllerProviderFactory.modelExtraInfoControllerClass = new ControllerFactory(LightweightModelExtraInfoController);
			composedControllerProviderFactory.rendererControllerClass = new ControllerFactory(ClassReferenceRendererController, { rendererClass: OperationsSeparatorRenderer});
			composedControllerProviderFactories["classOperationsCompartmentSeparator"] = composedControllerProviderFactory;
		}
		
		override public function start():void {
			super.start();
			WebCommonPlugin.getInstance().explorerTreeClassFactoryActionProvider.actionClasses.push(DragOnDiagramAction);
		}
		
		override protected function registerClassAliases():void {
			registerClassAliasFromAnnotation(View);
			registerClassAliasFromAnnotation(Node);
			registerClassAliasFromAnnotation(Diagram);
			registerClassAliasFromAnnotation(Location);
			registerClassAliasFromAnnotation(Bounds);
			
			registerClassAliasFromAnnotation(MoveResizeServerCommand);
			
			registerClassAliasFromAnnotation(ViewDetailsUpdate);
		}
		
		override protected function registerMessageBundle():void {
			// do nothing; this plugin doesn't have a .resources (yet)
		}
		
	}
}
