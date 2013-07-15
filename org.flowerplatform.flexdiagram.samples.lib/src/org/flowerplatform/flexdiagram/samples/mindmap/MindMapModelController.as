package org.flowerplatform.flexdiagram.samples.mindmap {
	import flash.events.IEventDispatcher;
	import flash.geom.Rectangle;
	
	import mx.collections.ArrayList;
	import mx.events.PropertyChangeEvent;
	import mx.events.PropertyChangeEventKind;
	
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelController;
	import org.flowerplatform.flexdiagram.samples.mindmap.model.MindMapModel;
	
	public class MindMapModelController implements IMindMapModelController {
		
		public function getParent(model:Object):Object {
			return MindMapModel(model).parent;
		}
		
		public function setParent(model:Object, value:Object):void {
			MindMapModel(model).parent = MindMapModel(value);
		}
		
		public function getChildren(model:Object):ArrayList {
			return MindMapModel(model).children;
		}
		
		public function getChildrenBasedOnSide(model:Object, side:int = 0):ArrayList {	
			if (side == 0) {
				side = model.side;
			}
			var list:ArrayList = new ArrayList();
			for (var i:int = 0; i < MindMapModel(model).children.length; i++) {
				var child:Object = MindMapModel(model).children.getItemAt(i);
				if (side == 0 || side == child.side) {
					list.addItem(child);
				}
			}
			return list;
		}
		
		public function setChildren(model:Object, value:ArrayList):void {
			MindMapModel(model).children = value;
		}
		
		public function addChild(model:Object, value:Object):void {
			var oldValue:ArrayList = MindMapModel(model).children;
			
			MindMapModel(model).children.addItem(value);
			
			IEventDispatcher(model).dispatchEvent(new PropertyChangeEvent(PropertyChangeEvent.PROPERTY_CHANGE, false, false, null, "children", oldValue, MindMapModel(model).children));
		}
		
		public function removeChild(model:Object, value:Object):void {
			var oldValue:ArrayList = MindMapModel(model).children;
			
			MindMapModel(model).children.removeItem(value);
			
			IEventDispatcher(model).dispatchEvent(new PropertyChangeEvent(PropertyChangeEvent.PROPERTY_CHANGE, false, false, null, "children", oldValue, MindMapModel(model).children));
		}
		
		public function getX(model:Object):Number {
			return MindMapModel(model).x;
		}
		
		public function setX(model:Object, value:Number):void {
			MindMapModel(model).x = value;
		}
		
		public function getY(model:Object):Number {
			return MindMapModel(model).y;
		}
		
		public function setY(model:Object, value:Number):void {
			MindMapModel(model).y = value;
		}
		
		public function getWidth(model:Object):Number {		
			return MindMapModel(model).width;
		}
		
		public function setWidth(model:Object, value:Number):void {			
			MindMapModel(model).width = value;
		}
		
		public function getHeight(model:Object):Number {
			return MindMapModel(model).height;
		}
		
		public function setHeight(model:Object, value:Number):void {
			MindMapModel(model).height = value;
		}
		
		public function getExpanded(model:Object):Boolean {
			return MindMapModel(model).expanded;
		}
		
		public function setExpanded(model:Object, value:Boolean):void {
			MindMapModel(model).expanded = value;
		}
		
		public function getSide(model:Object):int {
			return MindMapModel(model).side;
		}
		
		public function setSide(model:Object, value:int):void {
			MindMapModel(model).side = value;
		}
	}
}