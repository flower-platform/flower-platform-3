package org.flowerplatform.flexdiagram.tool.controller {
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public interface IDragToCreateRelationController {
		
		function activate(model:Object):void;
		function drag(model:Object):void;
		function drop(model:Object):void;
		function deactivate(model:Object):void;
		
	}	
}