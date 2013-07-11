package org.flowerplatform.flexdiagram.samples.mindmap {
	import flash.geom.Rectangle;
	
	import mx.collections.ArrayList;
	
	import org.flowerplatform.flexdiagram.samples.mindmap.model.MindMapModel;
	
	public class MindMapModelController {
		
		public function getChildren(model:Object):ArrayList {
			return MindMapModel(model).children;
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
		
	}
}