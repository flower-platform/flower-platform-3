package org.flowerplatform.flexdiagram.samples.mindmap {
	import mx.collections.ArrayList;
	import mx.core.INavigatorContent;
	
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
	import org.flowerplatform.flexdiagram.util.ParentAwareArrayList;
	
	public class MindMapDiagramShell extends DiagramShell implements IControllerProvider {
		
		public static const HORIZONTAL_PADDING_DEFAULT:int = 20;
		public static const VERTICAL_PADDING_DEFAULT:int = 5;
		
		public var horizontalPadding:int = HORIZONTAL_PADDING_DEFAULT;
		public var verticalPadding:int = VERTICAL_PADDING_DEFAULT;
		
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
	
		private function getExpandedHeight(model:Object):Number {
			var expandedHeight:Number = DynamicModelExtraInfoController(getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model).expandedHeight;
			if (isNaN(expandedHeight)) {
				expandedHeight = getMindMapController(model).getHeight(model);
			}
			return expandedHeight;
		}
		
		private function setExpandedHeight(model:Object, value:Number):void {
			DynamicModelExtraInfoController(getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model).expandedHeight = value;
		}
		
		private function getExpandedY(model:Object):Number {
			var expandedY:Number = DynamicModelExtraInfoController(getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model).expandedY;
			if (isNaN(expandedY)) {
				expandedY = model.y;
			}
			return expandedY;
		}
		
		private function setExpandedY(model:Object, value:Number):void {
			DynamicModelExtraInfoController(getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model).expandedY = value;
		}
		
		public function refreshNodePositions(model:Object):void {			
			var oldExpandedHeight:Number = getExpandedHeight(model);
			var oldExpandedHeightLeft:Number = DynamicModelExtraInfoController(getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model).expandedHeightLeft;			
			var oldExpandedHeightRight:Number = DynamicModelExtraInfoController(getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model).expandedHeightRight
			
			calculateRootNodeExpandedHeight(model.side);
						
			if (model.side == 0 || model.side == MindMapModel.LEFT) { 
				if (model.side == 0) {
					setExpandedHeight(model, DynamicModelExtraInfoController(getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model).expandedHeightLeft);
					oldExpandedHeight = oldExpandedHeightLeft;
				}
				changeCoordinates(model, oldExpandedHeight, getExpandedHeight(model), model.side == 0 ? MindMapModel.LEFT : model.side);
			}
			if (model.side == 0 || model.side == MindMapModel.RIGHT) { 
				if (model.side == 0) {
					setExpandedHeight(model, DynamicModelExtraInfoController(getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model).expandedHeightRight);
					oldExpandedHeight = oldExpandedHeightRight;
				}
				changeCoordinates(model, oldExpandedHeight, getExpandedHeight(model), model.side == 0 ? MindMapModel.RIGHT : model.side);
			}
		}
		
		private function calculateRootNodeExpandedHeight(side:int):void {
			var model:Object = ParentAwareArrayList(rootModel).getItemAt(0);
			if (side == 0 || side == MindMapModel.LEFT) { 
				calculateExpandedHeight(model, MindMapModel.LEFT);
				DynamicModelExtraInfoController(getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model).expandedHeightLeft = getExpandedHeight(model);
			}
			if (side == 0 || side == MindMapModel.RIGHT) { 
				calculateExpandedHeight(model, MindMapModel.RIGHT);
				DynamicModelExtraInfoController(getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model).expandedHeightRight = getExpandedHeight(model);
			}
		}
		
		private function calculateExpandedHeight(model:Object, side:int):Number {			
			var expandedHeight:Number = 0;
			var children:ArrayList = getMindMapController(model).getChildrenBasedOnSide(model, side);
			if (model.expanded && children.length > 0) {
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children.getItemAt(i);
					expandedHeight += calculateExpandedHeight(child, side);
					if (i < children.length - 1) {
						expandedHeight += verticalPadding;
					}
				}				
			} else {
				expandedHeight = getMindMapController(model).getHeight(model);				
			}
			setExpandedHeight(model, expandedHeight);
			return expandedHeight;
		}
		
		private function changeCoordinates(model:Object, oldExpandedHeight:Number, newExpandedHeight:Number, side:int):void {			
			if (newExpandedHeight != oldExpandedHeight) {
				setExpandedY(model, model.y - (newExpandedHeight - getMindMapController(model).getHeight(model))/2);
				changeChildrenCoordinates(model, side, true);				
				changeSiblingCoordinates(model, (newExpandedHeight - oldExpandedHeight)/2, side);
			}
		}		
		
		private function changeChildrenCoordinates(model:Object, side:int, changeOnlyForChildren:Boolean = false):void {	
			if (!changeOnlyForChildren) {				
				var padding:Number = 0;
				if (model.parent != null && getMindMapController(model).getChildren(model.parent).getItemAt(0) != model) {
					padding = verticalPadding;
				}
				model.y = getExpandedY(model) + (getExpandedHeight(model) - getMindMapController(model).getHeight(model))/2 + padding;
				if (model.side == MindMapModel.LEFT) {
					model.x = model.parent.x - getMindMapController(model).getWidth(model) - horizontalPadding;	
				} else {
					model.x = model.parent.x + getMindMapController(model).getWidth(model) + horizontalPadding;	
				}
			} else {
				setExpandedY(model, model.y - (getExpandedHeight(model) - getMindMapController(model).getHeight(model))/2);		
			}
			if (model.expanded) {				
				var children:ArrayList = getMindMapController(model).getChildrenBasedOnSide(model, side);				
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children.getItemAt(i);
					if (i == 0) {
						setExpandedY(child, getExpandedY(model));						
					} else {
						var previousChild:Object = children.getItemAt(i - 1);
						setExpandedY(child, getExpandedY(previousChild) + getExpandedHeight(previousChild));
					}					
					changeChildrenCoordinates(child, side);			
				}				
			}
		}
		
		private function changeSiblingCoordinates(model:Object, diff:Number, side:int):void {
			var parent:Object =  MindMapModel(model).parent;
			if (parent != null) {
				var children:ArrayList = getMindMapController(parent).getChildrenBasedOnSide(parent, side);				
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children.getItemAt(i);
					if (children.getItemIndex(model) > children.getItemIndex(child)) {				
						child.y -= diff;						
						changeSiblingChildrenCoordinates(child, -diff, side);
					} else if (children.getItemIndex(model) < children.getItemIndex(child)) {
						child.y += diff;
						changeSiblingChildrenCoordinates(child, diff, side);
					}
				}
				changeSiblingCoordinates(parent, diff, side);
			}
		}
		
		private function changeSiblingChildrenCoordinates(model:Object, diff:Number, side:int):void {
			var children:ArrayList = getMindMapController(model).getChildrenBasedOnSide(model, side);				
			for (var i:int = 0; i < children.length; i++) {
				var child:Object = children.getItemAt(i);
				child.y += diff;				
				changeSiblingChildrenCoordinates(child, diff, side);
			}
		}
		
	}
}