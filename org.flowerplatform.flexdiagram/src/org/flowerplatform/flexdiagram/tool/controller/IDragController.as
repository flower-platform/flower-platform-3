package org.flowerplatform.flexdiagram.tool.controller {
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public interface IDragController {
		
		function activate(model:Object, initialX:Number, initialY:Number):void;
		function drag(model:Object, deltaX:Number, deltaY:Number):void;		
		function drop(model:Object):void;
		function deactivate(model:Object):void;
		
	}
}