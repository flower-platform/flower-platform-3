package org.flowerplatform.flexdiagram.samples.controller {
	import mx.collections.ArrayList;
	import mx.collections.IList;
	
	import org.flowerplatform.flexdiagram.controller.model_children.IModelChildrenController;
	import org.flowerplatform.flexdiagram.samples.model.BasicSubModel;
	
	public class BasicSubModelChildrenProvider implements IModelChildrenController {

		private const children:ArrayList = new ArrayList();
		
		public function getParent(model:Object):Object {
			return BasicSubModel(model).parent;
		}
		
		public function getChildren(model:Object):IList {
			return children;
		}
		
		public function beginListeningForChanges(model:Object):void {
		}
		
		public function endListeningForChanges(model:Object):void {
		}
	}
}