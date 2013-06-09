package org.flowerplatform.flexdiagram.controller {
	
	public interface IInplaceEditorController {
		
		function startEditing(model:Object):void;
		
		function commit(model:Object):void;
		function abort(model:Object):void;
		
		function endEditing(model:Object):void;
	}
}