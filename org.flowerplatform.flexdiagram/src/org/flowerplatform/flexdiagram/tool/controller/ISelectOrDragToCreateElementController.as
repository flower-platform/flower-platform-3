package org.flowerplatform.flexdiagram.tool.controller {
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public interface ISelectOrDragToCreateElementController {
		
		function activate(model:Object, initialX:Number, initialY:Number, mode:String):void;
		function drag(model:Object, deltaX:Number, deltaY:Number):void;
		function drop(model:Object):void;
		function deactivate(model:Object):void;
		
	}
}