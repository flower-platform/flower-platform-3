package org.flowerplatform.flexdiagram.controller.renderer {
	import flash.display.DisplayObject;
	import flash.events.IEventDispatcher;
	import flash.geom.Rectangle;
	import flash.utils.getDefinitionByName;
	
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.UpdateConnectionEndsEvent;
	import org.flowerplatform.flexdiagram.controller.IAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.controller.model_children.IModelChildrenController;
	import org.flowerplatform.flexdiagram.renderer.connection.BindablePoint;
	import org.flowerplatform.flexdiagram.renderer.connection.ClipUtils;
	import org.flowerplatform.flexdiagram.renderer.connection.ConnectionFigure;
	
	public class ConnectionRendererController extends ClassReferenceRendererController {
		public function ConnectionRendererController(diagramShell:DiagramShell, rendererClass:Class=null) {
			super(diagramShell, rendererClass);
		}
		
		override public function associatedModelToRenderer(model:Object, renderer:IVisualElement):void {
			updateConnectionEnds(model, null);
		}
		
		/**
		 * This is invoked by the renderer. See comment there to see why.
		 */
		public function updateConnectionEnds(connectionModel:Object, modifiedEnd:Object):void {
			var connectionRenderer:ConnectionFigure = ConnectionFigure(diagramShell.getRendererForModel(connectionModel));
			var sourceRect:Array;
			var targetRect:Array;
			var sourceModel:Object = getSourceModel(connectionModel);
			var targetModel:Object = getTargetModel(connectionModel);

//			if (modifiedEnd == null || modifiedEnd == sourceModel) {
				// source
				if (sourceModel == null) {
					// shouldn't normally happen
					sourceRect = [0, 0, 0, 0];					
				} else {
					var sourceRenderer:IVisualElement = diagramShell.getRendererForModel(sourceModel);
					if (sourceRenderer == null) {
						// the renderer is not on the screen; => provide estimates
						sourceRect = getEstimatedRectForElementNotVisible(sourceModel);
					} else {
						// renderer on screen => provide real data from renderer
						var rectRelativeToDiagram:Rectangle = DisplayObject(sourceRenderer).getBounds(DisplayObject(diagramShell.diagramRenderer));
						sourceRect = [rectRelativeToDiagram.x, rectRelativeToDiagram.y, rectRelativeToDiagram.width, rectRelativeToDiagram.height];
					}
				}
//			}
//			if (modifiedEnd == null || modifiedEnd == targetModel) {
				// target
				if (targetModel == null) {
					// shouldn't normally happen
					targetRect = [50, 50, 50, 50];		
				} else {
					var targetRenderer:IVisualElement = diagramShell.getRendererForModel(targetModel);
					if (targetRenderer == null) {
						// the renderer is not on the screen; => provide estimates
						targetRect = getEstimatedRectForElementNotVisible(targetModel);	
					} else {
						// renderer on screen => provide real data from renderer
						rectRelativeToDiagram = DisplayObject(targetRenderer).getBounds(DisplayObject(diagramShell.diagramRenderer));
						targetRect = [rectRelativeToDiagram.x, rectRelativeToDiagram.y, rectRelativeToDiagram.width, rectRelativeToDiagram.height];
					}
				}
//			}
			
			var nextPointOfConnection:BindablePoint = new BindablePoint(0,0);
			var clippedPoint:Array;
			// source
			nextPointOfConnection.x = targetRect[0] + targetRect[2] / 2;
			nextPointOfConnection.y = targetRect[1] + targetRect[3] / 2;
			clippedPoint = ClipUtils.computeClipBindablePoint(sourceRect, nextPointOfConnection);
			connectionRenderer._sourcePoint.x = clippedPoint[0];
			connectionRenderer._sourcePoint.y = clippedPoint[1];
			
			// target
			nextPointOfConnection.x = clippedPoint[0];
			nextPointOfConnection.y = clippedPoint[1];
			clippedPoint = ClipUtils.computeClipBindablePoint(targetRect, nextPointOfConnection);
			connectionRenderer._targetPoint.x = clippedPoint[0];
			connectionRenderer._targetPoint.y = clippedPoint[1];
		}
		
		protected function getEstimatedRectForElementNotVisible(model:Object):Array {
			if (model == null) {
				throw new Error("No parent that has a IAbsoluteLayoutRectangleController has been found!");
			}
			
			var controller:IAbsoluteLayoutRectangleController = diagramShell.getControllerProvider(model).getAbsoluteLayoutRectangleController(model);
			if (controller != null) {
				var rect:Rectangle = controller.getBounds(model);
				return [rect.x, rect.y, rect.width, rect.height];
			} else {
				// look to find the parent that is child of the diagram, i.e. has IAbsoluteLayoutRectangleController
				var childrenController:IModelChildrenController = diagramShell.getControllerProvider(model).getModelChildrenController(model);
				if (childrenController == null) {
					throw new Error("Cannot find a IModelChildrenController for model = " + model + ". Elements should provide IModelChildrenController is they need to be connectable, even if they don't have children!");
				}
				return getEstimatedRectForElementNotVisible(childrenController.getParent(model));
			}
		}
		
		public function getSourceModel(connectionModel:Object):Object {
			throw new Error("This method should be implemented");
		}
		
		public function getTargetModel(connectionModel:Object):Object {
			throw new Error("This method should be implemented");
		}
	}
}