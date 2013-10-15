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
	
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.ui.Keyboard;
	
	import mx.core.Application;
	import mx.core.FlexGlobals;
	import mx.core.IDataRenderer;
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	
	import spark.components.Application;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class SelectOnClickTool extends Tool implements IWakeUpableTool {
		
		public static const ID:String = "SelectOnClickTool";
		
		public function SelectOnClickTool(diagramShell:DiagramShell) {
			super(diagramShell);
			
			WakeUpTool.wakeMeUpIfEventOccurs(diagramShell, this, WakeUpTool.MOUSE_DOWN);
			WakeUpTool.wakeMeUpIfEventOccurs(diagramShell, this, WakeUpTool.MOUSE_UP);
			// execute before DragTool
			WakeUpTool.wakeMeUpIfEventOccurs(diagramShell, this, WakeUpTool.MOUSE_DRAG, -1);
		}
		
		public function wakeUp(eventType:String, initialEvent:MouseEvent):Boolean {
			context.ctrlPressed = initialEvent.ctrlKey;
			context.shiftPressed = initialEvent.shiftKey;
			var renderer:IVisualElement = getRendererFromDisplayCoordinates();	
						
			if (eventType == WakeUpTool.MOUSE_DOWN && context.ctrlPressed && renderer is DiagramRenderer) {
				// SelectOrDragToCreateElementTool behavior, deselect items if ctrl + click on diagram
				return true;
			} else if (eventType == WakeUpTool.MOUSE_DOWN) {
				// first tool to process, add in wakedByMouseDownEvent if it can be activated
				// the activation will be done later (when mouse drag/up)
				context.wakedByMouseDownEvent = false;
				if (renderer is DiagramRenderer) {
					context.wakedByMouseDownEvent = true;
					return false;
				}
				
				if (renderer is IDataRenderer) {
					var model:Object;
					model = IDataRenderer(renderer).data;
					if (diagramShell.getControllerProvider(model).getSelectionController(model) != null) {						
						context.wakedByMouseDownEvent = (diagramShell.selectedItems.length > 1 || diagramShell.selectedItems.getItemIndex(model) == -1) || context.ctrlPressed || context.shiftPressed;
					}				
				}				
			} else if (context.wakedByMouseDownEvent) {
				if (eventType == WakeUpTool.MOUSE_DRAG && renderer is DiagramRenderer) {
					// DragTool behavior, don't activate
					return false;
				}
				// activate tool (here we know exactly that the selection tool is the right one)
				return true;
			}
			return false;
		}
			
		override public function activateAsMainTool():void {			
			var renderer:IDataRenderer = IDataRenderer(getRendererFromDisplayCoordinates());
			if (renderer is DiagramRenderer) {
				// reset selection
				diagramShell.selectedItems.removeAll();
			} else {
				var model:Object = renderer.data;
				var selected:Boolean = diagramShell.selectedItems.getItemIndex(model) != -1;
				
				if (context.ctrlPressed) { // substract mode
					if (selected) {
						diagramShell.selectedItems.removeItem(model);
					} else {
						diagramShell.selectedItems.addItem(model);
						diagramShell.mainSelectedItem = model;
					}				
				} else if (context.shiftPressed) { // add mode
					if (!selected) {
						diagramShell.selectedItems.addItem(model);
					}
					diagramShell.mainSelectedItem = model;
				} else {
					diagramShell.selectedItems.removeAll();
					diagramShell.selectedItems.addItem(model);										
				}
			}
			diagramShell.mainToolFinishedItsJob();
		}
		
		override public function deactivateAsMainTool():void {
			delete context.ctrlPressed;
			delete context.shiftPressed;		
		}
		
		override public function reset():void {				
			delete context.wakedByMouseDownEvent;
		}
	}	
}