package org.flowerplatform.flexdiagram.controller {
	
	public interface IDragToCreateRelationController {
		
		function startDragging(model:Object):void;
		function update(model:Object):void;
		function endDragging(model:Object):void;
		
	}	
}