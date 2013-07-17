package org.flowerplatform.flexdiagram.mindmap.controller {
	import org.flowerplatform.flexdiagram.controller.IControllerProvider;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public interface IMindMapControllerProvider extends IControllerProvider {
		
		function getMindMapModelController(model:Object):IMindMapModelController;
		
	}
}