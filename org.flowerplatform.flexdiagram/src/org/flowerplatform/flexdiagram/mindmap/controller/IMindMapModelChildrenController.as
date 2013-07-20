package org.flowerplatform.flexdiagram.mindmap.controller {
	
	import mx.collections.IList;
	
	import org.flowerplatform.flexdiagram.controller.model_children.IModelChildrenController;
	
	public interface IMindMapModelChildrenController extends IModelChildrenController {
		
		function getChildrenBasedOnSide(model:Object, side:int = 0):IList;
		
	}
}