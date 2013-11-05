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
package org.flowerplatform.flexdiagram.tool {
	import flash.display.DisplayObject;
	import flash.display.Stage;
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	import flash.events.KeyboardEvent;
	import flash.geom.Point;
	import flash.ui.Keyboard;
	
	import mx.core.IDataRenderer;
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	
	import spark.components.Scroller;
	
	[Bindable]
	
	/**
	 * @author Cristina Constantinescu
	 */	
	public class Tool {
		
		protected var diagramShell:DiagramShell;
		
		public var context:Object = new Object();
				
		public function Tool(diagramShell:DiagramShell) {
			this.diagramShell = diagramShell;
		}		
					
		public function activateDozingMode():void {				
		}
		
		public function deactivateDozingMode():void { 				
		}
		
		public function activateAsMainTool():void {					
		}
		
		public function deactivateAsMainTool():void {			
		}
		
		public function get diagramRenderer():DiagramRenderer {			
			return DiagramRenderer(diagramShell.diagramRenderer);
		}
			
		protected function getRendererFromDisplayCoordinates(ignoreDiagramRenderer:Boolean = false):IVisualElement {
			var stage:Stage = diagramRenderer.stage;
			var arr:Array = stage.getObjectsUnderPoint(new Point(stage.mouseX, stage.mouseY));
						
			var renderer:IVisualElement;
			var i:int;
			for (i = arr.length - 1; i >= 0;  i--) {
				renderer = getRendererFromDisplay(arr[i]);
				if (renderer != null) {
					if (!(ignoreDiagramRenderer && renderer is DiagramRenderer)) {
						return renderer;
					}					
				}
			}
			return null;
		}
		
		protected function getRendererFromDisplay(obj:Object):IVisualElement {			
			// in order for us to traverse its hierrarchy
			// it has to be a DisplayObject
			if (!(obj is DisplayObject)) {
				return null;
			}
			
			// traverse all the obj's hierarchy	
			while (obj != null) {
				if (obj is DiagramRenderer) {
					return IVisualElement(obj);
				}
				if (obj is IDataRenderer && diagramShell.modelToExtraInfoMap[IDataRenderer(obj).data] != null) {
					// found it
					return IVisualElement(obj);					
				}
				obj = DisplayObject(obj).parent;
			}
			
			// no found on the obj's hierarchy
			return null;
		}
		
		protected function globalToDiagram(x:Number, y:Number):Point { 
			var localPoint:Point = diagramRenderer.globalToLocal(new Point(x, y));
			localPoint = diagramRenderer.localToContent(localPoint);
			return localPoint;
		}
		
		public function reset():void {			
		}
		
		/**
		 * This function should be called by Tools that support lock-unlock behavior to announce when
		 * they finished working. An unlocked Tool will be deactivated as soon as its job is over.
		 * 
		 * <p>
		 * Note that there are Tools that work in locked behavior by default: they must not dispatch events when finished
		 * because their work can be declared ended only by user when selecting another Tool.
		 * However,(see SelectOrDragToCreateEleemntTool) there are Tools that have a well delimited action like creation Tools.
		 * For theese, the job ends when the user finished a drag, a drop, etc.
		 * 
		 * @see #canLock();		
		 */
		public function jobFinished():void {
			diagramShell.jobFinishedForExclusiveTool(this);
		}
		
		protected function addModelByResettingSelection(model:Object):void {
			if (diagramShell.selectedItems.length == 1 && diagramShell.selectedItems.getItemAt(0) == model) {
				// don't add if the selection already has it and its the only one selected
				return;
			}
			try {
				// Because an addItem is called after, the eventsCanBeIgnored is set to true,
				// this way listeners can limit the number of unwanted events.
				diagramShell.selectedItems.removeEventsCanBeIgnored = true;
				diagramShell.selectedItems.removeAll();							
			} finally {
				diagramShell.selectedItems.removeEventsCanBeIgnored = false;
			}
			diagramShell.selectedItems.addItem(model);
		}
	}	
	
}