package org.flowerplatform.flexdiagram.tool.controller {
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public interface IResizeController {
		
		function activate(model:Object):void;
		function drag(model:Object, deltaX:Number, deltaY:Number, type:String):void;
		function drop(model:Object):void;
		function deactivate(model:Object):void;
	
	}
}