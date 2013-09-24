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
package org.flowerplatform.flexdiagram.renderer.selection {
	
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.geom.Point;
	
	import mx.core.UIComponent;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class ChildAnchorsSelectionRenderer extends StandardAnchorsSelectionRenderer {
		
		override protected function handleTargetMoveResize(event:Event):void {
			var targetGlobalPos:Point = UIComponent(target).contentToGlobal(new Point(target.x, target.y));
			var parentGlobalPos:Point = UIComponent(parent).contentToGlobal(new Point(parent.x, parent.y));
			setLayoutBoundsPosition(targetGlobalPos.x - parentGlobalPos.x, targetGlobalPos.y - parentGlobalPos.y);
			setLayoutBoundsSize(target.width, target.height);
		}
		
	}
}