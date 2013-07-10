package org.flowerplatform.flexdiagram.samples.mindmap {
	import flash.geom.Rectangle;
	
	import mx.collections.ArrayList;
	
	import org.flowerplatform.flexdiagram.samples.mindmap.model.MindMapModel;
	
	public class MindMapModelController {
		
		public function MindMapModelController() {
		}
		
		public function getChildren(model:Object):ArrayList {
			return MindMapModel(model).children;
		}
		
		public function getWidth(model:Object):Number {
			return MindMapModel(model).width;
		}
		
		public function getHeight(model:Object):Number {
			return MindMapModel(model).height;
		}
		
		public function getExpanded(model:Object):Boolean {
			return MindMapModel(model).expanded;
		}
		
	}
}