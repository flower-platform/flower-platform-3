/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.flexdiagram.mindmap.controller {
	import mx.collections.ArrayList;

	/**
	 * @author Cristina Constantinescu
	 */
	public interface IMindMapModelController {
		
		function getParent(model:Object):Object;		
		function setParent(model:Object, value:Object):void;	
		
//		function getChildren(model:Object):ArrayList;		
//		function getChildrenBasedOnSide(model:Object, side:int = 0):ArrayList;		
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