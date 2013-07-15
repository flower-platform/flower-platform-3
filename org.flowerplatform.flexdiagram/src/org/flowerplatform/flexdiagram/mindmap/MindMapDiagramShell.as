package org.flowerplatform.flexdiagram.mindmap {
	import mx.collections.ArrayList;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.IControllerProvider;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapControllerProvider;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelController;
	import org.flowerplatform.flexdiagram.util.ParentAwareArrayList;
	
	public class MindMapDiagramShell extends DiagramShell {
		
		public static const NONE:int = 0;
		public static const LEFT:int = -1;
		public static const RIGHT:int = 1;
		
		public static const HORIZONTAL_PADDING_DEFAULT:int = 20;
		public static const VERTICAL_PADDING_DEFAULT:int = 5;
		
		public var horizontalPadding:int = HORIZONTAL_PADDING_DEFAULT;
		public var verticalPadding:int = VERTICAL_PADDING_DEFAULT;
		
		public function MindMapDiagramShell() {
			super();			
		}
				
		public function getMindMapController(model:Object):IMindMapModelController {
			return IMindMapControllerProvider(getControllerProvider(model)).getMindMapModelController(model);
		}
		
		private function getExpandedHeight(model:Object):Number {
			var expandedHeight:Number = DynamicModelExtraInfoController(getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model).expandedHeight;
			if (isNaN(expandedHeight)) {
				expandedHeight =getMindMapController(model).getHeight(model);
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
		
			var side:int = getMindMapController(model).getSide(model);
			if (side == NONE || side == LEFT) { 
				if (side == NONE) {
					setExpandedHeight(model, DynamicModelExtraInfoController(getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model).expandedHeightLeft);
					oldExpandedHeight = oldExpandedHeightLeft;
				}				
				changeCoordinates(model, oldExpandedHeight, getExpandedHeight(model), side == NONE ? LEFT : side);
			}
			if (side == NONE || side == RIGHT) { 
				if (side == NONE) {
					setExpandedHeight(model, DynamicModelExtraInfoController(getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model).expandedHeightRight);
					oldExpandedHeight = oldExpandedHeightRight;
				}				
				changeCoordinates(model, oldExpandedHeight, getExpandedHeight(model), side == NONE ? RIGHT : side);
			}
		}
		
		private function calculateRootNodeExpandedHeight(side:int):void {
			var model:Object = ParentAwareArrayList(rootModel).getItemAt(0);
			if (side == NONE || side == LEFT) { 
				calculateExpandedHeight(model, LEFT);
				DynamicModelExtraInfoController(getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model).expandedHeightLeft = getExpandedHeight(model);
			}
			if (side == NONE || side == RIGHT) { 
				calculateExpandedHeight(model, RIGHT);
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
			setExpandedY(model, getMindMapController(model).getY(model) - (newExpandedHeight - getMindMapController(model).getHeight(model))/2);
			
			changeChildrenCoordinates(model, side, model.parent == null);				
			changeSiblingCoordinates(model, (newExpandedHeight - oldExpandedHeight)/2, side);
		}		
		
		private function changeChildrenCoordinates(model:Object, side:int, changeOnlyForChildren:Boolean = false):void {	
			if (!changeOnlyForChildren) {	
				getMindMapController(model).setY(model, getExpandedY(model) + (getExpandedHeight(model) - getMindMapController(model).getHeight(model))/2);		
				var parent:Object = getMindMapController(model).getParent(model);
				if (getMindMapController(model).getSide(model) == LEFT) {
					getMindMapController(model).setX(model, getMindMapController(parent).getX(parent) - getMindMapController(model).getWidth(model) - horizontalPadding);	
				} else {
					getMindMapController(model).setX(model, getMindMapController(parent).getX(parent) + getMindMapController(parent).getWidth(parent) + horizontalPadding);	
				}
			} else {
				setExpandedY(model, getMindMapController(model).getY(model) - (getExpandedHeight(model) - getMindMapController(model).getHeight(model))/2);		
			}
			if (getMindMapController(model).getExpanded(model)) {				
				var children:ArrayList = getMindMapController(model).getChildrenBasedOnSide(model, side);				
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children.getItemAt(i);
					if (i == 0) {
						setExpandedY(child, getExpandedY(model));						
					} else {
						var previousChild:Object = children.getItemAt(i - 1);
						setExpandedY(child, getExpandedY(previousChild) + getExpandedHeight(previousChild) + verticalPadding);
					}					
					changeChildrenCoordinates(child, side);			
				}				
			}
		}
		
		private function changeSiblingCoordinates(model:Object, diff:Number, side:int):void {
			var parent:Object =  getMindMapController(model).getParent(model);
			if (parent != null) {
				var children:ArrayList = getMindMapController(parent).getChildrenBasedOnSide(parent, side);				
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children.getItemAt(i);
					if (children.getItemIndex(model) > children.getItemIndex(child)) {				
						getMindMapController(child).setY(child, getMindMapController(child).getY(child) - diff);					
						changeSiblingChildrenCoordinates(child, -diff, side);
					} else if (children.getItemIndex(model) < children.getItemIndex(child)) {
						getMindMapController(child).setY(child, getMindMapController(child).getY(child) + diff);					
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
				getMindMapController(child).setY(child, getMindMapController(child).getY(child) + diff);					
				changeSiblingChildrenCoordinates(child, diff, side);
			}
		}
		
	}
}