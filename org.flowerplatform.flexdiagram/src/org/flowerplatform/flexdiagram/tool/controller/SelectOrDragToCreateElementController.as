package org.flowerplatform.flexdiagram.tool.controller {
	
	import flash.display.DisplayObject;
	import flash.geom.Rectangle;
	import flash.system.System;
	
	import mx.charts.chartClasses.DualStyleObject;
	import mx.collections.ArrayList;
	import mx.collections.IList;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.IAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.tool.SelectOnClickTool;
	import org.flowerplatform.flexdiagram.tool.SelectOrDragToCreateElementTool;
	import org.flowerplatform.flexdiagram.ui.MoveResizePlaceHolder;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class SelectOrDragToCreateElementController extends ControllerBase implements ISelectOrDragToCreateElementController {
				
		private static const DRAG_TO_CREATE_MIN_WIDTH_DEFAULT:int = 30;
		private var _dragToCreateMinWidth:Number = DRAG_TO_CREATE_MIN_WIDTH_DEFAULT;
		
		private static const DRAG_TO_CREATE_MIN_HEIGHT_DEFAULT:int = 30;
		private var _dragToCreateMinHeight:Number = DRAG_TO_CREATE_MIN_HEIGHT_DEFAULT;
		
		public static const DRAG_TO_CREATE_ALPHA_DEFAULT:Number = 0.4;		
		private var _dragToCreateAlpha:Number = DRAG_TO_CREATE_ALPHA_DEFAULT;
		
		public static const DRAG_TO_SELECT_COLOR_DEFAULT:uint = 0x00007F;		
		private var _dragToSelectColor:uint = DRAG_TO_SELECT_COLOR_DEFAULT;
		
		public static const DRAG_TO_CREATE_COLOR_DEFAULT:uint = 0x007F00;		
		private var _dragToCreateColor:uint = DRAG_TO_CREATE_COLOR_DEFAULT;
		
		public static const DRAG_TO_SELECT_ALPHA_DEFAULT:Number = 0.4;		
		private var _dragToSelectAlpha:Number = DRAG_TO_SELECT_ALPHA_DEFAULT;
		
		public function get dragToSelectColor():uint {
			return _dragToSelectColor;
		}
		
		public function set dragToSelectColor(value:uint):void {
			_dragToSelectColor = value;
		}
		
		public function get dragToCreateColor():uint {
			return _dragToCreateColor;
		}
		
		public function set dragToCreateColor(value:uint):void {
			_dragToCreateColor = value;
		}
				
		public function get dragToSelectAlpha():Number {
			return _dragToSelectAlpha;
		} 
		
		public function set dragToSelectAlpha(value:Number):void {
			_dragToSelectAlpha = value;
		}
		
		public function get dragToCreateAlpha():Number {
			return _dragToCreateAlpha;
		}
		
		public function set dragToCreateAlpha(value:Number):void {
			_dragToCreateAlpha = value;
		}
			
		public function get dragToCreateMinWidth():int {
			return _dragToCreateMinWidth;
		}
				
		public function set dragToCreateMinWidth(value:int):void {
			_dragToCreateMinWidth = value;
		}
			
		public function get dragToCreateMinHeight():int {
			return _dragToCreateMinHeight;
		}
		
		public function set dragToCreateMinHeight(value:int):void {
			_dragToCreateMinHeight = value;
		}
		
		public function SelectOrDragToCreateElementController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function activate(model:Object, initialX:Number, initialY:Number, mode:String):void {
			// create placeholder
			var selectDragToCreatePlaceHolder:MoveResizePlaceHolder = new MoveResizePlaceHolder();
			selectDragToCreatePlaceHolder.x = initialX;
			selectDragToCreatePlaceHolder.y = initialY;
			
			// put placeholder in model's extra info
			diagramShell.modelToExtraInfoMap[model].selectDragToCreatePlaceHolder = selectDragToCreatePlaceHolder;
			diagramShell.modelToExtraInfoMap[model].selectDragToCreateMode = mode;
			
			// add it to diagram
			diagramShell.diagramRenderer.addElement(selectDragToCreatePlaceHolder);			
		}
		
		public function drag(model:Object, deltaX:Number, deltaY:Number):void {	
			// update placeholder dimensions
			var selectDragToCreatePlaceHolder:MoveResizePlaceHolder = diagramShell.modelToExtraInfoMap[model].selectDragToCreatePlaceHolder;
			
			selectDragToCreatePlaceHolder.width = deltaX;
			selectDragToCreatePlaceHolder.height = deltaY;
			
			var models:IList = getModelsUnderPlaceHolder(model, selectDragToCreatePlaceHolder);
				
			if (models.length != 0 || 
				selectDragToCreatePlaceHolder.width < _dragToCreateMinWidth || 
				selectDragToCreatePlaceHolder.height < _dragToCreateMinHeight) { 
				// SELECT MODE
				selectDragToCreatePlaceHolder.setColorAndAlpha(_dragToSelectColor, _dragToSelectAlpha);
			} else { 
				// DRAG MODE
				selectDragToCreatePlaceHolder.setColorAndAlpha(_dragToCreateColor, _dragToCreateAlpha);
			}			
		}
		
		public function drop(model:Object):void	{
			var selectDragToCreatePlaceHolder:MoveResizePlaceHolder = diagramShell.modelToExtraInfoMap[model].selectDragToCreatePlaceHolder;			
			var mode:String = diagramShell.modelToExtraInfoMap[model].selectDragToCreateMode;
			
			var models:IList = getModelsUnderPlaceHolder(model, selectDragToCreatePlaceHolder);
			
			if (models.length == 0) {
				// TODO CC: add here drag to create element behavior
			} else {
				// select/deselect models
				for (var i:int = 0; i < models.length; i++) {
					var obj:Object = models.getItemAt(i);
					if (mode == SelectOrDragToCreateElementTool.SELECT_MODE_ADD) {
						diagramShell.selectedItems.addItem(obj);
					} else if (mode == SelectOrDragToCreateElementTool.SELECT_MODE_SUBSTRACT) {
						if (diagramShell.selectedItems.getItemIndex(obj) != -1) {
							diagramShell.selectedItems.removeItem(obj);
						} else {
							diagramShell.selectedItems.addItem(obj);
						}
					}
				}
			}
			// done
			diagramShell.mainToolFinishedItsJob();
		}
		
		public function deactivate(model:Object):void {		
			// remove placeholder from diagram
			diagramShell.diagramRenderer.removeElement(diagramShell.modelToExtraInfoMap[model].selectDragToCreatePlaceHolder);
			
			// remove placeholder from model's extra info
			delete diagramShell.modelToExtraInfoMap[model].selectDragToCreatePlaceHolder;
			delete diagramShell.modelToExtraInfoMap[model].selectDragToCreateMode;
		}
		
		private function getModelsUnderPlaceHolder(model:Object, selectDragToCreatePlaceHolder:MoveResizePlaceHolder):IList {
			var models:ArrayList = new ArrayList();
			var children:IList = diagramShell.getControllerProvider(model).getModelChildrenController(model).getChildren(model);
			for (var i:int = 0; i < children.length; i++) {
				var child:Object = children.getItemAt(i);
				var absLayoutRectangleController:IAbsoluteLayoutRectangleController = 
					diagramShell.getControllerProvider(child).getAbsoluteLayoutRectangleController(child);
				if (absLayoutRectangleController != null) {
					var placeHolderBounds:Rectangle = selectDragToCreatePlaceHolder.getBounds(DisplayObject(diagramShell.diagramRenderer));
					if (placeHolderBounds.intersects(absLayoutRectangleController.getBounds(child))) { 
						// intersects hit area
						models.addItem(child);
					}
				}
			}
			return models;
		}
	}
}