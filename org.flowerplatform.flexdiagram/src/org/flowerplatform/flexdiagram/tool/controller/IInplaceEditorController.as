package org.flowerplatform.flexdiagram.tool.controller {
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public interface IInplaceEditorController {
		
		function canActivate(model:Object):Boolean;
		
		function activate(model:Object):void;
		
		function commit(model:Object):void;
		function abort(model:Object):void;
		
		function deactivate(model:Object):void;
	}
}