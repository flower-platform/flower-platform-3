package org.flowerplatform.flexdiagram.samples.mindmap {
	import flash.geom.Rectangle;
	
	import mx.collections.ArrayList;
	
	import org.flowerplatform.flexdiagram.samples.mindmap.model.MindMapModel;
	
	public class MindMapModelController {
		
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
		
	}
}