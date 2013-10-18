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
package org.flowerplatform.editor.model.renderer {
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import org.flowerplatform.flexdiagram.renderer.connection.ConnectionFigure;
	import org.flowerplatform.flexdiagram.renderer.selection.AnchorsSelectionRenderer;
	import org.flowerplatform.flexdiagram.ui.ResizeAnchor;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class ConnectionAnchorsSelectionRenderer extends AnchorsSelectionRenderer {
		
		protected var sourceAnchor:ResizeAnchor;
		protected var targetAnchor:ResizeAnchor;
		
		override protected function createChildren():void {
			sourceAnchor = new ResizeAnchor();
			addChild(sourceAnchor);
			targetAnchor = new ResizeAnchor();
			addChild(targetAnchor);
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			var connectionFigure:ConnectionFigure = ConnectionFigure(target);
			sourceAnchor.x = connectionFigure._sourcePoint.x;
			sourceAnchor.y = connectionFigure._sourcePoint.y;
			targetAnchor.x = connectionFigure._targetPoint.x;
			targetAnchor.y = connectionFigure._targetPoint.y;
		}
		
		override protected function mouseOverHandler(event:MouseEvent):void {
			if (event.target is ResizeAnchor) {
				cursorManager.removeAllCursors();
				cursorManager.setCursor(crossCursor, 2, -16, -16);
			} else {
				super.mouseOverHandler(event);
			}
		}
		
	}
}