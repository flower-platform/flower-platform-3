package org.flowerplatform.flexdiagram.mindmap.controller {
	import mx.collections.ArrayList;

	/**
	 * @author Cristina Constantinescu
	 */
	public interface IMindMapModelController {
		
		function getParent(model:Object):Object;		
		function setParent(model:Object, value:Object):void;	
		
		function getChildren(model:Object):ArrayList;		
		function getChildrenBasedOnSide(model:Object, side:int = 0):ArrayList;		
		function setChildren(model:Object, value:ArrayList):void;
		
		function getX(model:Object):Number;		
		function setX(model:Object, value:Number):void;
		
		function getY(model:Object):Number;		
		function setY(model:Object, value:Number):void;
		
		function getWidth(model:Object):Number;		
		function setWidth(model:Object, value:Number):void;
		
		function getHeight(model:Object):Number;		
		function setHeight(model:Object, value:Number):void;
				
		function getExpanded(model:Object):Boolean;		
		function setExpanded(model:Object, value:Boolean):void;
		
		function getSide(model:Object):int;
		function setSide(model:Object, value:int):void;
	}
}