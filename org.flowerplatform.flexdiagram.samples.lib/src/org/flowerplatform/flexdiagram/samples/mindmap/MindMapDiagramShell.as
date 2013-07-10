package org.flowerplatform.flexdiagram.samples.mindmap {
	import mx.collections.ArrayList;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.IAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.controller.IControllerProvider;
	import org.flowerplatform.flexdiagram.controller.model_children.IModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.model_children.ParentAwareArrayListModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.IModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.renderer.IRendererController;
	import org.flowerplatform.flexdiagram.controller.selection.ISelectionController;
	import org.flowerplatform.flexdiagram.controller.visual_children.AbsoluteLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.controller.visual_children.IVisualChildrenController;
	import org.flowerplatform.flexdiagram.samples.mindmap.controller.MindMapModelAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.samples.mindmap.controller.MindMapModelRendererController;
	import org.flowerplatform.flexdiagram.samples.mindmap.model.MindMapModel;
	import org.flowerplatform.flexdiagram.tool.controller.IDragToCreateRelationController;
	import org.flowerplatform.flexdiagram.tool.controller.IInplaceEditorController;
	import org.flowerplatform.flexdiagram.tool.controller.IResizeController;
	import org.flowerplatform.flexdiagram.tool.controller.ISelectOrDragToCreateElementController;
	import org.flowerplatform.flexdiagram.tool.controller.drag.IDragController;
	
	public class MindMapDiagramShell extends DiagramShell implements IControllerProvider {
		
		private var mindMapController:MindMapModelController;
		private var dynamicModelExtraInfoController:DynamicModelExtraInfoController;
		
		private var absoluteLayoutVisualChildrenController:AbsoluteLayoutVisualChildrenController;
		private var arrayListModelChildrenController:ParentAwareArrayListModelChildrenController;
		private var mindMapModelRendererController:MindMapModelRendererController;
		private var minMapModelAbsoluteLayoutRectangleController:MindMapModelAbsoluteLayoutRectangleController;
		
		public function MindMapDiagramShell() {
			super();
			
			mindMapController = new MindMapModelController();
			dynamicModelExtraInfoController = new DynamicModelExtraInfoController(this);
			absoluteLayoutVisualChildrenController = new AbsoluteLayoutVisualChildrenController(this);
			arrayListModelChildrenController = new ParentAwareArrayListModelChildrenController(this, true);
			mindMapModelRendererController = new MindMapModelRendererController(this, null);
			minMapModelAbsoluteLayoutRectangleController = new MindMapModelAbsoluteLayoutRectangleController(this);
		}
		
		public function getAbsoluteLayoutRectangleController(model:Object):IAbsoluteLayoutRectangleController {
			if (model is MindMapModel) {
				return minMapModelAbsoluteLayoutRectangleController;
			}
			return null;
		}
		
		public function getDragController(model:Object):IDragController {
			// TODO Auto Generated method stub
			return null;
		}
		
		public function getDragToCreateRelationController(model:Object):IDragToCreateRelationController {
			// TODO Auto Generated method stub
			return null;
		}
		
		public function getInplaceEditorController(model:Object):IInplaceEditorController {
			// TODO Auto Generated method stub
			return null;
		}
		
		public function getModelChildrenController(model:Object):IModelChildrenController {
			if (model is ArrayList) {
				return arrayListModelChildrenController;
			}
			return null;
		}
		
		public function getModelExtraInfoController(model:Object):IModelExtraInfoController {
			// TODO Auto Generated method stub
			return dynamicModelExtraInfoController;
		}
		
		public function getRendererController(model:Object):IRendererController	{
			if (model is MindMapModel) {
				return mindMapModelRendererController;
			}
			return null;
		}
		
		public function getResizeController(model:Object):IResizeController {
			// TODO Auto Generated method stub
			return null;
		}
		
		public function getSelectOrDragToCreateElementController(model:Object):ISelectOrDragToCreateElementController {
			// TODO Auto Generated method stub
			return null;
		}
		
		public function getSelectionController(model:Object):ISelectionController {
			// TODO Auto Generated method stub
			return null;
		}
		
		public function getVisualChildrenController(model:Object):IVisualChildrenController {
			if (model is ArrayList) {
				return absoluteLayoutVisualChildrenController;
			} return null;
		}
		
		
		public function getMindMapController(model:Object):MindMapModelController {
			return mindMapController;
		}
		
		override public function getControllerProvider(model:Object):IControllerProvider {
			return this;
		}
			
		public function refreshNodePositions(model:Object):void {
			var expandedHeight:Number = getExpandedHeight(model);
			trace(expandedHeight);
			changeChildrenCoordinates(model);
		}
		
		private function getExpandedHeight(model:Object):Number {
			var expandedHeight:Number = 0;
			var children = getMindMapController(model).getChildren(model);
			if (model.expanded && children.length > 0) {
				for (var i:int = 0; i < children.length; i++) {
					expandedHeight += getExpandedHeight(children.getItemAt(i));					
				}				
			} else {
				expandedHeight = getMindMapController(model).getHeight(model);				
			}
			//trace(expandedHeight);
			DynamicModelExtraInfoController(getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model).expandedHeight = expandedHeight;
			return expandedHeight;
		}
		
		
		private function changeChildrenCoordinates(model:Object, includeParent:Boolean = false):void {	
			if (includeParent) {
				var expandedHeight:Number = DynamicModelExtraInfoController(getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model).expandedHeight;
				var expandedY:Number = DynamicModelExtraInfoController(getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model).expandedY;
				trace(model.text + (expandedY) + " " + (expandedHeight - model.height)/2);
				model.y = expandedY + (expandedHeight - model.height)/2;
				model.x = model.parent.x - 10 - model.width;				
			}
			if (model.expanded) {				
				var children:ArrayList = getMindMapController(model).getChildren(model);				
				for (var i:int = 0; i < children.length; i++) {
					if (i == 0) {
						DynamicModelExtraInfoController(getControllerProvider(children.getItemAt(i)).getModelExtraInfoController(children.getItemAt(i))).getDynamicObject(children.getItemAt(i)).expandedY = 
							model.y - (DynamicModelExtraInfoController(getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model).expandedHeight - model.height)/2;						
					} else {
						DynamicModelExtraInfoController(getControllerProvider(children.getItemAt(i)).getModelExtraInfoController(children.getItemAt(i))).getDynamicObject(children.getItemAt(i)).expandedY = 
							DynamicModelExtraInfoController(getControllerProvider(children.getItemAt(i - 1)).getModelExtraInfoController(children.getItemAt(i - 1))).getDynamicObject(children.getItemAt(i - 1)).expandedY + DynamicModelExtraInfoController(getControllerProvider(children.getItemAt(i - 1)).getModelExtraInfoController(children.getItemAt(i - 1))).getDynamicObject(children.getItemAt(i - 1)).expandedHeight;
					}
					trace(DynamicModelExtraInfoController(getControllerProvider(children.getItemAt(i)).getModelExtraInfoController(children.getItemAt(i))).getDynamicObject(children.getItemAt(i)).expandedY);
					changeChildrenCoordinates(children.getItemAt(i), true);			
				}				
			}			
		}
		
	}
}