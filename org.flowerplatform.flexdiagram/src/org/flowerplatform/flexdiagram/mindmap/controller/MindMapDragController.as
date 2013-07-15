package org.flowerplatform.flexdiagram.mindmap.controller
{
	import flash.display.DisplayObject;
	import flash.display.Stage;
	import flash.geom.Point;
	
	import mx.core.IDataRenderer;
	import mx.core.IVisualElement;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.tool.controller.drag.IDragController;
	
	public class MindMapDragController extends ControllerBase implements IDragController {
		
		public function MindMapDragController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function activate(model:Object, initialX:Number, initialY:Number):void {
			diagramShell.modelToExtraInfoMap[model].initialX = initialX;
			diagramShell.modelToExtraInfoMap[model].initialY = initialY;
		}
		
		public function drag(model:Object, deltaX:Number, deltaY:Number):void {
			diagramShell.modelToExtraInfoMap[model].finalX = diagramShell.modelToExtraInfoMap[model].initialX + deltaX;
			diagramShell.modelToExtraInfoMap[model].finalY = diagramShell.modelToExtraInfoMap[model].initialY + deltaY;
		}
		
		public function drop(model:Object):void {
			var finalPoint:Point = new Point(diagramShell.modelToExtraInfoMap[model].finalX, diagramShell.modelToExtraInfoMap[model].finalY);
			var renderer:IVisualElement = getRendererFromCoordinates(finalPoint);
			
			if (renderer is IDataRenderer) {
				var dropModel:Object = IDataRenderer(renderer).data;
				var dragParentModel:Object = MindMapDiagramShell(diagramShell).getMindMapController(model).getParent(model);
				
				MindMapDiagramShell(diagramShell).getMindMapController(dragParentModel).removeChild(dragParentModel, model);
				
				MindMapDiagramShell(diagramShell).getMindMapController(model).setParent(model, dropModel);	
				MindMapDiagramShell(diagramShell).getMindMapController(dropModel).addChild(dropModel, model);	
				
				UIComponent(renderer).invalidateDisplayList();
			}
		}
		
		public function deactivate(model:Object):void {
//			delete diagramShell.modelToExtraInfoMap[model].initialX;
//			delete diagramShell.modelToExtraInfoMap[model].initialY;
//			delete diagramShell.modelToExtraInfoMap[model].finalX;
//			delete diagramShell.modelToExtraInfoMap[model].finalY;
		}
		
		protected function getRendererFromCoordinates(point:Point):IVisualElement {
			var stage:Stage = DisplayObject(diagramShell.diagramRenderer).stage;
			var arr:Array = stage.getObjectsUnderPoint(new Point(stage.mouseX, stage.mouseY));
			
			var renderer:IVisualElement;
			var i:int;
			for (i = arr.length - 1; i >= 0;  i--) {
				renderer = getRendererFromDisplay(arr[i]);
				if (renderer != null) {					
					return renderer;
				}
			}
			return null;
		}
		
		protected function getRendererFromDisplay(obj:Object):IVisualElement {			
			// in order for us to traverse its hierrarchy
			// it has to be a DisplayObject
			if (!(obj is DisplayObject)) {
				return null;
			}
			
			// traverse all the obj's hierarchy	
			while (obj != null) {
				if (obj is DiagramRenderer) {
					return IVisualElement(obj);
				}
				if (obj is IDataRenderer && diagramShell.modelToExtraInfoMap[IDataRenderer(obj).data] != null) {
					// found it
					return IVisualElement(obj);					
				}
				obj = DisplayObject(obj).parent;
			}
			
			// no found on the obj's hierarchy
			return null;
		}
	}
}